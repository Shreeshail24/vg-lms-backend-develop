package com.samsoft.lms.core.services;

import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.builder.CompareToBuilder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.samsoft.lms.core.dto.OdAmortDto;
import com.samsoft.lms.core.dto.OdAmortScheduleDto;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

@Service
@Slf4j
public class OdAmort {

	@Autowired
	private Environment env;

	@Autowired
	private CoreAmort coreAmort;

	@Autowired
	private InterestService intService;

	@Autowired
	private CommonServices commService;

	@Transactional(rollbackFor = { RuntimeException.class, Error.class, Exception.class })
	public List<OdAmortScheduleDto> getOdAmort(OdAmortDto odDto) throws Exception {
		log.info("Inside OD Amort " + odDto.toString());
		List<OdAmortScheduleDto> result = new ArrayList<OdAmortScheduleDto>();
		String dateFormat = env.getProperty("lms.global.date.format");
		SimpleDateFormat sdf = new SimpleDateFormat(dateFormat);
		Integer addFactorPrin = 0, installmentNo = 0, addFactorInt = 0;
		Double dropAmount = 0d;
		Date prinDate = null, intDate = null;
		if (odDto.getDtPrinStart() != null) {
			prinDate = sdf.parse(odDto.getDtPrinStart());
		}

		if (odDto.getDtInterestStart() != null) {
			intDate = sdf.parse(odDto.getDtInterestStart());
		}
		Double closingBal = odDto.getDrawDownAmount() == null ? 0 : odDto.getDrawDownAmount();
		Double remAmount = odDto.getDropLineAmount() == null ? 0 : odDto.getDropLineAmount();
		try {
			switch (odDto.getDropLineFreq()) {

			case "MONTHLY":
				addFactorPrin = 1;
				break;
			case "BIMONTHLY":
				addFactorPrin = 2;
				break;
			case "QUARTERLY":
				addFactorPrin = 3;
				break;
			case "HALFYEARLY":
				addFactorPrin = 6;
				break;
			case "YEARLY":
				addFactorPrin = 12;
				break;

			}

			switch (odDto.getInterestRepaymentFrequency()) {

			case "MONTHLY":
				addFactorInt = 1;
				break;
			case "BIMONTHLY":
				addFactorInt = 2;
				break;
			case "QUARTERLY":
				addFactorInt = 3;
				break;
			case "HALFYEARLY":
				addFactorInt = 6;
				break;
			case "YEARLY":
				addFactorInt = 12;
				break;

			}

			log.info("odDto.getDropLineODYN() " + odDto.getDropLineODYN());

			if (odDto.getDropLineODYN().equalsIgnoreCase("Y")) {
				double remainingLimit = odDto.getSanctionedAmount();
				if (prinDate == null) {
					log.info("inside prinDate=>>>:{}", prinDate);
					log.info("odDtoodDtoodDto: {}", odDto);
					prinDate = sdf.parse(odDto.getPrincipalPaymentCycleDay() + "-" + odDto.getDtDrawdown().split("-")[1]
							+ "-" + odDto.getDtDrawdown().split("-")[2]);
					log.info("outside prinDate=>>>:{}", prinDate);
					Calendar c1 = Calendar.getInstance();
					c1.setTime(prinDate);
					c1.add(Calendar.MONTH, addFactorPrin);
					prinDate = c1.getTime();

				}

				if (odDto.getDropLineMode().equalsIgnoreCase("P")) {
					dropAmount = roundInstallmentOd(odDto.getSanctionedAmount() * (odDto.getDropLinePerc() / 100),
							"ROUND");
				} else {
					dropAmount = odDto.getDropLineAmount();
				}
				remAmount = odDto.getDrawDownAmount();
				// remainingLimit = remainingLimit - Math.min(dropAmount, remainingLimit);
				// if (odDto.getDrawDownAmount() > remainingLimit) {
				do {
					remainingLimit = remainingLimit - Math.min(dropAmount, remainingLimit);
					OdAmortScheduleDto row = new OdAmortScheduleDto();
					double tmpAmount = 0;
					if (remAmount > remainingLimit) {
						tmpAmount = remAmount - remainingLimit;
					}
					remAmount = remAmount - tmpAmount;
					row.setPreviousInstallmentDate(odDto.getDtDrawdown());
					row.setOpeningBalance(0d);
					row.setInstallmentNo(++installmentNo);
					if (row.getInstallmentNo() == 1) {
						log.info("Inside instal 111: {}", prinDate);
						row.setInstallmentDate(sdf.format(prinDate));
					} else {
						log.info("outsidedd instal 111: {}", prinDate);
						row.setInstallmentDate(
								coreAmort.getNextInstallmentDate(sdf.format(prinDate), odDto.getDropLineFreq()));
					}
					log.info("row.getInstallmentDate(): {}", row.getInstallmentDate());
					prinDate = sdf.parse(row.getInstallmentDate());
					row.setInterestRate(odDto.getInterestRate());
					row.setInterestAmount(0d);
					row.setInstallmentAmount(tmpAmount);
					row.setPrincipalAmount(tmpAmount);
					row.setClosingBalance(closingBal - tmpAmount);
					closingBal = row.getClosingBalance();
					row.setOpeningBalance(closingBal);
					row.setBpiAmount(0d);
					row.setTranType("PRINCIPAL");
					row.setInterestAccrued(0d);
					row.setSortSeq(2);
					result.add(row);
				} while (remainingLimit > 0);
				// }

			} else {

				dropAmount = odDto.getDrawDownAmount();
				OdAmortScheduleDto row = new OdAmortScheduleDto();

				row.setPreviousInstallmentDate(odDto.getDtDrawdown());
				row.setOpeningBalance(dropAmount);
				row.setInstallmentNo(++installmentNo);
				row.setInstallmentDate(odDto.getDtLimitExpired());
				row.setInterestRate(odDto.getInterestRate());
				row.setInterestAmount(0d);
				row.setInstallmentAmount(dropAmount);
				row.setPrincipalAmount(dropAmount);
				row.setClosingBalance(closingBal - dropAmount);
				closingBal = row.getClosingBalance();
				row.setBpiAmount(0d);
				row.setTranType("PRINCIPAL");
				row.setInterestAccrued(0d);
				row.setSortSeq(2);
				log.info("row Amort Od " + row);

				result.add(row);

			}

			log.info("intDate {} ,  PaymentCycleDay {} ", intDate, odDto.getInterestPaymentCycleDay());

			if (intDate == null) {
				if (odDto.getInterestPaymentCycleDay() == 0) {
					Calendar c = Calendar.getInstance();
					c.setTime(sdf.parse(odDto.getDtDrawdown()));
					if (addFactorInt != 1) {
						c.add(Calendar.MONTH, addFactorInt);
					}
					c.set(Calendar.DAY_OF_MONTH, c.getActualMaximum(Calendar.DAY_OF_MONTH));
					intDate = c.getTime();
				} else {
					intDate = sdf.parse(odDto.getInterestPaymentCycleDay() + "-" + odDto.getDtDrawdown().split("-")[1]
							+ "-" + odDto.getDtDrawdown().split("-")[2]);

				}
				log.info("DtDrawdown OD " + sdf.parse(odDto.getDtDrawdown()));

				if (intDate.before(sdf.parse(odDto.getDtDrawdown()))
						|| intDate.equals(sdf.parse(odDto.getDtDrawdown()))) {
					Calendar c = Calendar.getInstance();
					c.setTime(intDate);
					c.add(Calendar.MONTH, addFactorInt);
					intDate = c.getTime();
				}

			}

			installmentNo = 0;
			OdAmortScheduleDto row = null;

			log.info("intDate {} , getDtLimitExpired {} ", sdf.parse(sdf.format(intDate)),
					sdf.parse(odDto.getDtLimitExpired()));
			do {
				row = new OdAmortScheduleDto();

				row.setPreviousInstallmentDate(odDto.getDtDrawdown());
				row.setOpeningBalance(dropAmount);
				row.setInstallmentNo(++installmentNo);
				if (row.getInstallmentNo() == 1) {
					row.setInstallmentDate(sdf.format(intDate));
				} else {
					row.setInstallmentDate(coreAmort.getNextInstallmentDate(sdf.format(intDate),
							odDto.getInterestRepaymentFrequency()));
				}
				intDate = sdf.parse(row.getInstallmentDate());
				row.setInterestRate(odDto.getInterestRate());
				row.setInterestAmount(0d);
				row.setInstallmentAmount(0d);
				row.setPrincipalAmount(0d);
				row.setClosingBalance(row.getOpeningBalance());
				// closingBal = row.getClosingBalance();
				row.setBpiAmount(0d);
				row.setTranType("INTEREST");
				row.setInterestAccrued(0d);
				row.setSortSeq(1);

				// log.info("row Amort Od2 " + row);
				if ((sdf.parse(sdf.format(intDate)).before(sdf.parse(odDto.getDtLimitExpired())))
						|| (sdf.parse(sdf.format(intDate)).equals(sdf.parse(odDto.getDtLimitExpired())))) {
					result.add(row);
				}

			} while ((sdf.parse(sdf.format(intDate)).before(sdf.parse(odDto.getDtLimitExpired())))
					|| (sdf.parse(sdf.format(intDate)).equals(sdf.parse(odDto.getDtLimitExpired()))));

			log.info("Before Sort ");
			Collections.sort(result, new OdScheduleComparator());
			log.info("After Sort ");
			Double principalBal = odDto.getDrawDownAmount();
			installmentNo = 0;
			Double interestAccured = odDto.getBpiAmount();
			String prevInstallment = odDto.getDtDrawdown();
			for (OdAmortScheduleDto odAmortScheduleDto : result) {
				log.info("inside OdAmortScheduleDto loop");
				odAmortScheduleDto.setOpeningBalance(principalBal);
				odAmortScheduleDto.setInstallmentNo(++installmentNo);
				if (odAmortScheduleDto.getInstallmentNo() == 1) {
					odAmortScheduleDto.setPreviousInstallmentDate(odDto.getDtDrawdown());
					odAmortScheduleDto.setBpiAmount(odDto.getBpiAmount());
					interestAccured = odDto.getBpiAmount();
				} else {
					if (odAmortScheduleDto.getTranType().equalsIgnoreCase("INTEREST")) {
						odAmortScheduleDto.setPreviousInstallmentDate(prevInstallment);
					}
				}

				// if (!(odAmortScheduleDto.getPreviousInstallmentDate()
				// .equals(odAmortScheduleDto.getInstallmentDate()))) {

				log.info("Before InterestAccrued");
				if (odAmortScheduleDto.getTranType().equalsIgnoreCase("INTEREST")) {
					interestAccured = commService.numberFormatter(intService.getInterestAmount(principalBal,
							odDto.getInterestRate(), odDto.getInterestBasis(),
							sdf.parse(odAmortScheduleDto.getPreviousInstallmentDate()),
							sdf.parse(odAmortScheduleDto.getInstallmentDate())));
					prevInstallment = odAmortScheduleDto.getInstallmentDate();
				}
				log.info("After InterestAccrued");

				// }
				principalBal = principalBal - odAmortScheduleDto.getPrincipalAmount();
				odAmortScheduleDto.setClosingBalance(principalBal);
				if (odAmortScheduleDto.getTranType().equalsIgnoreCase("INTEREST")) {
					odAmortScheduleDto.setInterestAmount(interestAccured);
					odAmortScheduleDto.setInstallmentAmount(interestAccured);
					interestAccured = 0d;
				}

			}

			log.info("End of Amort ");

		} catch (Exception e) {
			throw e;
		}

		return result;

	}

	public double roundInstallmentOd(double installmentAmount, String roundParameter) {

		switch (roundParameter) {
		case "ROUND":
			return Math.round(installmentAmount);
		case "ROUND_DOWN":
			return Math.floor(installmentAmount);
		case "ROUND_UP":
			return Math.ceil(installmentAmount);
		}

		return 0;
	}

	@Transactional(rollbackFor = { RuntimeException.class, Error.class, Exception.class })
	class OdScheduleComparator implements Comparator<OdAmortScheduleDto> {
		String dateFormat = env.getProperty("lms.global.date.format");
		SimpleDateFormat sdf = new SimpleDateFormat(dateFormat);

		@Override
		public int compare(OdAmortScheduleDto arg0, OdAmortScheduleDto arg1) {
			try {
				return new CompareToBuilder()
						.append(sdf.parse(arg0.getInstallmentDate()), sdf.parse(arg1.getInstallmentDate()))
						.append(arg0.getSortSeq(), arg1.getSortSeq()).toComparison();
			} catch (ParseException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			return 0;
		}

	}

}
