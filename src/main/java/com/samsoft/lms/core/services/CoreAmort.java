package com.samsoft.lms.core.services;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.samsoft.lms.core.dto.AmortParameter;
import com.samsoft.lms.core.entities.Amort;
import com.samsoft.lms.core.exceptions.AmortExceptions;
import com.samsoft.lms.core.repositories.AmortRepository;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.Instant;
import java.time.LocalDate;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.time.temporal.TemporalAdjusters;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Service
@Slf4j
public class CoreAmort {

	@Autowired
	private InterestService interestService;

	@Autowired
	private AmortRepository amortRepo;

	@Autowired
	private Environment env;

	@Autowired
	private CommonServices commService;

	@Transactional(rollbackFor = { RuntimeException.class, Error.class, Exception.class })
	public String getPreviousInstallmentDate(String installmentDate, String repaymentFrequency) {
		String dateFormat = env.getProperty("lms.global.date.format");
		SimpleDateFormat sdf = new SimpleDateFormat(dateFormat);
		DateTimeFormatter dateTime = DateTimeFormatter.ofPattern(dateFormat);
		LocalDate dtInstallmentDate = null;
		LocalDate updatedDate = null;
		try {
			dtInstallmentDate = Instant.ofEpochMilli(sdf.parse(installmentDate).getTime())
					.atZone(ZoneId.systemDefault()).toLocalDate();

		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		switch (repaymentFrequency.toUpperCase()) {
		case "MONTHLY":
			updatedDate = dtInstallmentDate.minusMonths(1);
			break;

		case "BIMONTHLY":
			updatedDate = dtInstallmentDate.minusMonths(2);
			break;

		case "QUARTERLY":
			updatedDate = dtInstallmentDate.minusMonths(3);
			break;

		case "HALFYEARLY":
			updatedDate = dtInstallmentDate.minusMonths(6);
			break;

		case "YEARLY":
			updatedDate = dtInstallmentDate.minusMonths(12);
			break;

		case "WEEKLY":
			updatedDate = dtInstallmentDate.minusWeeks(1);
			break;

		case "FORTNIGHTLY":
			updatedDate = dtInstallmentDate.minusWeeks(2);
			break;
		default:
			updatedDate = dtInstallmentDate.minusDays(Integer.parseInt(repaymentFrequency));
		}
		return updatedDate.format(dateTime);

	}

	@Transactional(rollbackFor = { RuntimeException.class, Error.class, Exception.class })
	public String getNextInstallmentDate(String prevInstallmentDate, String repaymentFrequency) {
		log.info("repaymentFrequency : {}", repaymentFrequency);
		log.info("prevInstallmentDate : {}", prevInstallmentDate);
		String dateFormat = env.getProperty("lms.global.date.format");
		SimpleDateFormat sdf = new SimpleDateFormat(dateFormat);
		DateTimeFormatter dateTime = DateTimeFormatter.ofPattern(dateFormat);

		LocalDate dtPrevInstallmentDate = null;
		try {
			dtPrevInstallmentDate = Instant.ofEpochMilli(sdf.parse(prevInstallmentDate).getTime())
					.atZone(ZoneId.systemDefault()).toLocalDate();

		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		LocalDate tmpDate = dtPrevInstallmentDate;

		log.info("repaymentFrequency : {}", repaymentFrequency);

		switch (repaymentFrequency.toUpperCase()) {
		case "MONTHLY":
			dtPrevInstallmentDate = dtPrevInstallmentDate.plusMonths(1);
			break;

		case "BIMONTHLY":
			dtPrevInstallmentDate = dtPrevInstallmentDate.plusMonths(2);
			break;

		case "QUARTERLY":
			dtPrevInstallmentDate = dtPrevInstallmentDate.plusMonths(3);
			break;

		case "HALFYEARLY":
			dtPrevInstallmentDate = dtPrevInstallmentDate.plusMonths(6);
			break;

		case "YEARLY":
			dtPrevInstallmentDate = dtPrevInstallmentDate.plusMonths(12);
			break;

		case "WEEKLY":
			dtPrevInstallmentDate = dtPrevInstallmentDate.plusWeeks(1);
			break;

		case "FORTNIGHTLY":
			dtPrevInstallmentDate = dtPrevInstallmentDate.plusWeeks(2);
			break;

		default:
			dtPrevInstallmentDate = dtPrevInstallmentDate.plusDays(Integer.parseInt(repaymentFrequency));
		}

		if (tmpDate.getDayOfMonth() == tmpDate.with(TemporalAdjusters.lastDayOfMonth()).getDayOfMonth()) {
			dtPrevInstallmentDate = dtPrevInstallmentDate.with(TemporalAdjusters.lastDayOfMonth());
		}
		return dtPrevInstallmentDate.format(dateTime);

	}

	@Transactional(rollbackFor = { RuntimeException.class, Error.class, Exception.class })
	public double getEMI(double principalAmount, float interestRate, int tenor, String tenorUnit,
			String repaymentFrequency, String interestBasis, String amortizationMethod) {

		// tenor value is considered as months. If input is year then need to multiply
		// it by 12
		int tenorDivisor = 0;
		switch (repaymentFrequency.toUpperCase()) {
		case "MONTHLY":
			tenorDivisor = 12;
			break;

		case "BIMONTHLY":
			tenorDivisor = 6;
			break;

		case "QUARTERLY":
			tenorDivisor = 4;
			break;

		case "HALFYEARLY":
			tenorDivisor = 2;
			break;

		case "YEARLY":
			tenorDivisor = 1;
			break;

		case "WEEKLY":
			tenorDivisor = 52;
			break;

		case "FORTNIGHTLY":
			tenorDivisor = 26;
			break;

		default:
			tenorDivisor = 360 / Integer.parseInt(repaymentFrequency);
		}
		float rate = interestRate / (tenorDivisor * 100);
		if (amortizationMethod.equalsIgnoreCase("FLAT")) {
			return commService.numberFormatter(roundInstallment(
					(((roundInstallment((principalAmount * rate), "ROUND") * tenor) + principalAmount) / tenor),
					"ROUND"));
		} else {
			return commService.numberFormatter(
					(principalAmount * rate * Math.pow(1 + rate, tenor)) / (Math.pow(1 + rate, tenor) - 1));
		}

	}

	public double roundInstallment(double installmentAmount, String roundParameter) {

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
	public List<Amort> getAmort(AmortParameter amortParameterJson) {
		double instAmount = 0;
		String dateFormat = env.getProperty("lms.global.date.format");
		AmortParameter amortParameter = amortParameterJson;
		// CoreInterest objCoreInterest = new CoreInterest();
		List<Amort> amortList = new ArrayList<Amort>();
		try {
			/*
			 * try { Gson g = new Gson();
			 * 
			 * amortParameter = g.fromJson(amortParameterJson, AmortParameter.class);
			 * 
			 * } catch (JsonSyntaxException e) { throw new
			 * JsonSyntaxException(e.getMessage()); }
			 */
			SimpleDateFormat sdf = new SimpleDateFormat(dateFormat);
			Date dtPrevInstDate = null, dtDisbDate = null, dtInstallmentStart = null;

			Amort row = new Amort();
			row.setInstallmentNo(1);
			row.setDtInstallment(amortParameter.getDtInstallmentStart());
			row.setInterestRate(amortParameter.getInterestRate());
			if (amortParameter.getInterestRate() == 0) {
				instAmount = roundInstallment(amortParameter.getLoanAmount() / amortParameter.getTenor(),
						amortParameter.getEmiRounding());
			} else {
				if (amortParameterJson.getEmiAmount() == 0) {
					instAmount = roundInstallment(getEMI(amortParameter.getLoanAmount(),
							amortParameter.getInterestRate(), amortParameter.getTenor(), amortParameter.getTenorUnit(),
							amortParameter.getRepaymentFrequency(), amortParameter.getInterestBasis(),
							amortParameter.getAmortizationMethod()), amortParameter.getEmiRounding());
				} else {
					instAmount = amortParameterJson.getEmiAmount();
				}
			}

			row.setInstallmentAmount(instAmount);
			row.setTotalAdvanceEmiAmount(instAmount * amortParameter.getNoOfAdvanceEmi());
			row.setLoanAmount(amortParameter.getLoanAmount() - row.getTotalAdvanceEmiAmount());
			row.setDtPreviousInstallment(getPreviousInstallmentDate(amortParameter.getDtInstallmentStart(),
					amortParameter.getRepaymentFrequency()));

			/*
			 * try { dtPrevInstDate =
			 * Instant.ofEpochMilli(sdf.parse(row.getDtPreviousInstallment()).getTime())
			 * .atZone(ZoneId.systemDefault()).toLocalDate(); dtDisbDate =
			 * Instant.ofEpochMilli(sdf.parse(amortParameter.getDtDisbursement()).getTime())
			 * .atZone(ZoneId.systemDefault()).toLocalDate(); dtInstallmentStart =
			 * Instant.ofEpochMilli(sdf.parse(amortParameter.getDtInstallmentStart()).
			 * getTime()) .atZone(ZoneId.systemDefault()).toLocalDate();
			 * 
			 * } catch (ParseException e) { // TODO Auto-generated catch block
			 * e.printStackTrace(); }
			 */

			try {
				dtPrevInstDate = new SimpleDateFormat(dateFormat).parse(row.getDtPreviousInstallment());
				dtDisbDate = new SimpleDateFormat(dateFormat).parse(amortParameter.getDtDisbursement());
				dtInstallmentStart = new SimpleDateFormat(dateFormat).parse(amortParameter.getDtInstallmentStart());
			} catch (ParseException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

			if (dtPrevInstDate.compareTo(dtDisbDate) > 0) {

				row.setBpiAmount(roundInstallment(interestService.getInterestAmount(
						amortParameter.getAmortizationMethod().equalsIgnoreCase("FLAT") ? amortParameter.getLoanAmount()
								: row.getLoanAmount(),
						amortParameter.getInterestRate(), amortParameter.getInterestBasis(), dtDisbDate,
						dtPrevInstDate), "ROUND"));
				row.setInterestAmount(roundInstallment(interestService.getInterestAmount(
						amortParameter.getAmortizationMethod().equalsIgnoreCase("FLAT") ? amortParameter.getLoanAmount()
								: row.getLoanAmount(),
						amortParameter.getInterestRate(), amortParameter.getInterestBasis(), dtPrevInstDate,
						dtInstallmentStart), "ROUND"));

				log.info("CoreAmort :: getAmort :: If :: InterestAmount: {}", row.getInterestAmount());

			} else {
				row.setInterestAmount(roundInstallment(interestService.getInterestAmount(
						amortParameter.getAmortizationMethod().equalsIgnoreCase("FLAT") ? amortParameter.getLoanAmount()
								: row.getLoanAmount(),
						amortParameter.getInterestRate(), amortParameter.getInterestBasis(), dtDisbDate,
						dtInstallmentStart), "ROUND"));

				log.info("CoreAmort :: getAmort :: Else :: InterestAmount: {}", row.getInterestAmount());
			}

			if (amortParameter.getBpiHandling().equals("Y")) {
				if ((row.getInterestAmount() + row.getBpiAmount()) > row.getInstallmentAmount()) {
					row.setPrincipalAmount(row.getInstallmentAmount() - row.getInterestAmount());
				} else {
					row.setPrincipalAmount(row.getInstallmentAmount() - (row.getInterestAmount() + row.getBpiAmount()));
				}
			} else if (amortParameter.getBpiHandling().equals("N")) {
				// if (amortParameter.getBpiHandling().equals("COLLECT_SEPARATE")) {
				row.setPrincipalAmount(row.getInstallmentAmount() - row.getInterestAmount());
				// }
			} else {
				row.setBpiAmount(0);
				row.setPrincipalAmount(row.getInstallmentAmount() - row.getInterestAmount());
			}

			row.setOpeningBalance(row.getLoanAmount());
			row.setClosingBalance(row.getLoanAmount() - row.getPrincipalAmount());

			if (amortParameterJson.getTenor() == 1) {
				row.setPrincipalAmount(row.getOpeningBalance());
				row.setClosingBalance(0);
				row.setInstallmentAmount(row.getInterestAmount() + row.getPrincipalAmount());
			}

			amortList.add(row);

			int count = 1;
			double prinBal = 0;
			double prinBalnxt = 0;
			double RoundDiff = 0;
			double closingBal = row.getClosingBalance();
			do {
				if (amortParameterJson.getTenor() != 1) {
					Amort row1 = new Amort();
					row1.setDtPreviousInstallment(row.getDtInstallment());
					row1.setPreviousClosingBalance(row.getClosingBalance());
					row1.setInstallmentNo(++count);
					row1.setDtInstallment(
							getNextInstallmentDate(row.getDtInstallment(), amortParameter.getRepaymentFrequency()));
					row1.setInterestRate(amortParameter.getInterestRate());
					row1.setInstallmentAmount(instAmount);
					Date row1InstallmentDate = null, row1PrevInstallmentDate = null;
					/*
					 * try { row1InstallmentDate =
					 * Instant.ofEpochMilli(sdf.parse(row1.getDtInstallment()).getTime())
					 * .atZone(ZoneId.systemDefault()).toLocalDate(); row1PrevInstallmentDate =
					 * Instant.ofEpochMilli(sdf.parse(row1.getDtPreviousInstallment()).getTime())
					 * .atZone(ZoneId.systemDefault()).toLocalDate();
					 * 
					 * } catch (ParseException e) { // TODO Auto-generated catch block
					 * e.printStackTrace(); }
					 */

					try {
						row1InstallmentDate = new SimpleDateFormat(dateFormat).parse(row1.getDtInstallment());
						row1PrevInstallmentDate = new SimpleDateFormat(dateFormat)
								.parse(row1.getDtPreviousInstallment());

					} catch (ParseException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}

					row1.setInterestAmount(roundInstallment(
							interestService.getInterestAmount(
									amortParameter.getAmortizationMethod().equalsIgnoreCase("FLAT")
											? amortParameter.getLoanAmount()
											: closingBal,
									amortParameter.getInterestRate(), amortParameter.getInterestBasis(),
									row1PrevInstallmentDate, row1InstallmentDate),
							"ROUND"));
					prinBal = row1.getInstallmentAmount() - row1.getInterestAmount();
					if (closingBal - prinBal > 0) {
						row1.setPrincipalAmount(prinBal);
						closingBal = closingBal - row1.getPrincipalAmount();
					} else {
						row1.setPrincipalAmount(closingBal);
						RoundDiff = (closingBal + row1.getInterestAmount()) - roundInstallment(
								closingBal + row1.getInterestAmount(), amortParameter.getEmiRounding());
						row1.setInterestAmount(row1.getInterestAmount() - RoundDiff);
						row1.setInstallmentAmount(closingBal + (row1.getInterestAmount()));
						closingBal = 0;
					}
					row1.setClosingBalance(closingBal);

					if (closingBal < 0) {
						row1.setPrincipalAmount(closingBal);
					}

					if (closingBal < row1.getInstallmentAmount()) {
						row1.setInstallmentThreshold(
								row1.getInstallmentAmount() * (amortParameter.getLastRowThresholdPercentage() / 100));
						row1.setDtNextInstallment(getNextInstallmentDate(row1.getDtInstallment(),
								amortParameter.getRepaymentFrequency()));

						Date row1NextInstallmentDate = null;
						/*
						 * try { row1NextInstallmentDate =
						 * Instant.ofEpochMilli(sdf.parse(row1.getDtNextInstallment()).getTime())
						 * .atZone(ZoneId.systemDefault()).toLocalDate();
						 * 
						 * } catch (ParseException e) { // TODO Auto-generated catch block
						 * e.printStackTrace(); }
						 */

						row1NextInstallmentDate = new SimpleDateFormat(dateFormat).parse(row1.getDtNextInstallment());

						row1.setNextInstallmentInterest(roundInstallment(
								interestService.getInterestAmount(
										amortParameter.getAmortizationMethod().equalsIgnoreCase("FLAT")
												? amortParameter.getLoanAmount()
												: closingBal,
										amortParameter.getInterestRate(), amortParameter.getInterestBasis(),
										row1InstallmentDate, row1NextInstallmentDate),
								amortParameter.getEmiRounding()));
						prinBalnxt = row1.getInstallmentAmount() - row1.getNextInstallmentInterest();
						if ((closingBal - prinBalnxt) < 0) {
							row1.setNextInstallmentAmount(closingBal + row1.getNextInstallmentInterest());
						} else {
							row1.setNextInstallmentAmount(
									row1.getInstallmentAmount() - row1.getNextInstallmentInterest());
						}
						if (row1.getInstallmentThreshold() > row1.getNextInstallmentAmount()) {
							row1.setPrincipalAmount(row1.getPrincipalAmount() + closingBal);
							RoundDiff = (row1.getPrincipalAmount() + row1.getInterestAmount())
									- roundInstallment(row1.getPrincipalAmount() + row1.getInterestAmount(),
											amortParameter.getEmiRounding());
							row1.setInterestAmount(row1.getInterestAmount() - RoundDiff);
							row1.setInstallmentAmount(row1.getPrincipalAmount() + row1.getInterestAmount());
							closingBal = 0;
							row1.setClosingBalance(closingBal);
						}
					}
					/*
					 * if ((row1.getOpeningBalance()-(instAmount- row1.getInterestAmount())) <
					 * row1.getInstallmentThreshold()) { row1.setPrincipalAmount(closingBal); } else
					 * { row1.setPrincipalAmount(instAmount- row1.getInterestAmount()); }
					 */

					row1.setOpeningBalance(row.getClosingBalance());
					amortList.add(row1);
					row = row1;
				}
			} while (closingBal > 0);
			amortRepo.saveAll(amortList);
		} catch (Exception e) {
			throw new AmortExceptions(e.getMessage());
		}

		log.info("CoreAmort :: getAmort :: amortList: {}", amortList);

		for (Amort formattedRow : amortList) {
			formattedRow.setTotalAdvanceEmiAmount(commService.numberFormatter(formattedRow.getTotalAdvanceEmiAmount()));
			formattedRow.setLoanAmount(commService.numberFormatter(formattedRow.getLoanAmount()));
			formattedRow.setBpiAmount(commService.numberFormatter(formattedRow.getBpiAmount()));
			formattedRow.setInterestAmount(commService.numberFormatter(formattedRow.getInterestAmount()));
			formattedRow.setPrincipalAmount(commService.numberFormatter(formattedRow.getPrincipalAmount()));
			formattedRow.setOpeningBalance(commService.numberFormatter(formattedRow.getOpeningBalance()));
			formattedRow.setClosingBalance(commService.numberFormatter(formattedRow.getClosingBalance()));
			formattedRow
					.setPreviousClosingBalance(commService.numberFormatter(formattedRow.getPreviousClosingBalance()));
			formattedRow.setInstallmentThreshold(commService.numberFormatter(formattedRow.getInstallmentThreshold()));
			formattedRow
					.setNextInstallmentInterest(commService.numberFormatter(formattedRow.getNextInstallmentInterest()));
			formattedRow.setNextInstallmentAmount(commService.numberFormatter(formattedRow.getNextInstallmentAmount()));
		}

		return amortList;

	}

}
