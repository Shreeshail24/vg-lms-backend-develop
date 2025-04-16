package com.samsoft.lms.core.services;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.samsoft.lms.core.dto.EventBaseChagesCalculationOutputDto;
import com.samsoft.lms.core.dto.EventFeeOutputDto;
import com.samsoft.lms.core.entities.AgrFeeParam;
import com.samsoft.lms.core.entities.AgrLoans;
import com.samsoft.lms.core.entities.TabOrganization;
import com.samsoft.lms.core.exceptions.CoreDataNotFoundException;
import com.samsoft.lms.core.repositories.AgrFeeParamRepository;
import com.samsoft.lms.core.repositories.AgrLoansRepository;
import com.samsoft.lms.core.repositories.AgrRepayScheduleRepository;
import com.samsoft.lms.core.repositories.TabOrganizationRepository;
import com.samsoft.lms.transaction.dto.ChargesBookingDto;
import com.samsoft.lms.transaction.dto.GstListDto;
import com.samsoft.lms.transaction.services.GstDetailsService;
import com.samsoft.lms.transaction.services.TransactionService;

import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
public class CoreServices {

	@Autowired
	private AgrFeeParamRepository feeRepo;

	@Autowired
	private TransactionService tranService;

	@Autowired
	private TabOrganizationRepository orgRepo;

	@Autowired
	private Environment env;

	@Autowired
	private CommonServices commService;

	@Autowired
	private GstDetailsService gstService;

	@Autowired
	private AgrRepayScheduleRepository repayRepo;

	@Autowired
	private AgrLoansRepository agrLoansRepository;

	@Transactional(rollbackFor = { RuntimeException.class, Error.class, Exception.class })
	public EventBaseChagesCalculationOutputDto getEventBaseChargesCalculation(String mastAgrId, Integer instrumentId,
			String feeEvent, String bookFlag, Double basicAmount, String reverseCalcYn) throws Exception {
		EventBaseChagesCalculationOutputDto result = new EventBaseChagesCalculationOutputDto();
		result.setFeeList(new ArrayList<EventFeeOutputDto>());
		// result.setGstList(new ArrayList<GstListDto>());
		String dateFormat = env.getProperty("lms.global.date.format");
		SimpleDateFormat sdf = new SimpleDateFormat(dateFormat);
		TabOrganization orgObj = orgRepo.findAll().get(0);
		Date businessDate = orgRepo.findAll().get(0).getDtBusiness();
		float totalTaxPercent = 0;
		double feeTempAmount = 0d;
		float actualChargePer = 0f;
		log.info("getEventBaseChargesCalculation 1" + mastAgrId + " " + instrumentId + " " + feeEvent + " "
				+ basicAmount + " " + reverseCalcYn + " "+ bookFlag);

		try {
			List<AgrFeeParam> feeEventList = feeRepo.findByFeeEventAndMasterAgreementMastAgrId(feeEvent, mastAgrId);
			for (AgrFeeParam agrFeeParamVal : feeEventList) {
				if(agrFeeParamVal.getMaxTenor() == null) {
					agrFeeParamVal.setMaxTenor(0);
				}
				if(agrFeeParamVal.getMinTenor() == null) {
					agrFeeParamVal.setMinTenor(0);
				}
				
				actualChargePer = agrFeeParamVal.getFeePercentage();
				log.info("actualChargePer====>" + actualChargePer);
				
				/*if (!(agrFeeParamVal.getMaxTenor() == 0 && agrFeeParamVal.getMinTenor() == 0)) {
					Integer installmentNo = repayRepo.getMaxInstallmentNo(mastAgrId, businessDate);
					log.info("min and max tenor" + agrFeeParamVal.getMaxTenor() + " " + agrFeeParamVal.getMinTenor());
					if (installmentNo <= agrFeeParamVal.getMaxTenor()
							&& installmentNo >= agrFeeParamVal.getMinTenor()) {
						actualChargePer = agrFeeParamVal.getFeePercentage();
						log.info("actualChargePer====>" + actualChargePer);
					}

				}*/

			}

			for (AgrFeeParam agrFeeParam : feeEventList) {
				EventFeeOutputDto feeDto = new EventFeeOutputDto();
				BeanUtils.copyProperties(agrFeeParam, feeDto);
				if (agrFeeParam.getFeeType().equalsIgnoreCase("AMOUNT")) {
					feeDto.setFeeAmount(agrFeeParam.getFeeAmount());
				} else {
					if (reverseCalcYn.equalsIgnoreCase("N")) {
						feeDto.setFeeAmount(basicAmount * actualChargePer / 100);
						log.info("feeeeee=====>"+(basicAmount * actualChargePer / 100)+"\n"+feeDto.getFeeAmount());
					} else {
						// temporary amount set to get total tax percentage
						List<GstListDto> gstListTemp = gstService.getGstList(mastAgrId, feeDto.getFeeCode(), 100d);
						for (GstListDto calTax : gstListTemp) {
							totalTaxPercent += calTax.getTaxPercentage();
						}
						if (totalTaxPercent > 0) {
							float totalTmpPercent = (actualChargePer * totalTaxPercent / 100) + actualChargePer;
							feeDto.setFeeAmount(commService
									.numberFormatter(basicAmount * totalTmpPercent / (100 + totalTmpPercent)));
						} else {
							feeDto.setFeeAmount(commService
									.numberFormatter(basicAmount * actualChargePer / (100 + actualChargePer)));
						}

						feeTempAmount = (commService
								.numberFormatter(feeDto.getFeeAmount() * totalTaxPercent / (100 + totalTaxPercent)));
					}
				}

				log.info("getEventBaseChargesCalculation 2" + mastAgrId + " " + instrumentId + " " + feeEvent);
				List<GstListDto> gstList = gstService.getGstList(mastAgrId, feeDto.getFeeCode(),
						feeDto.getFeeAmount() - feeTempAmount);
				log.info("getEventBaseChargesCalculation 3");
				feeDto.setGstList(gstList);
				result.getFeeList().add(feeDto);
				// result.setGstList(gstList);

				if (bookFlag.equalsIgnoreCase("Y")) {

					//Get Agr Loans
					List<AgrLoans> agrLoansList = agrLoansRepository.findByMasterAgreementMastAgrId(mastAgrId);
					log.info("Loan Id: {}", agrLoansList.get(0).getLoanId());

					log.info(" homeState 3 ");
					ChargesBookingDto chargesBookingDto = new ChargesBookingDto();

					chargesBookingDto.setMastAgrId(mastAgrId);
					chargesBookingDto.setTrandDate(sdf.format(orgRepo.findAll().get(0).getDtBusiness()));
					chargesBookingDto.setSource("INSTR_BOUNCE");
					chargesBookingDto.setSourceId(Integer.toString(instrumentId));
					chargesBookingDto.setTranHead(agrFeeParam.getFeeCode());
					chargesBookingDto.setChargeAmount(agrFeeParam.getFeeAmount());
					chargesBookingDto.setReason("INSTR_BOUNCE");
					chargesBookingDto.setRemark("Instrument Bounce");
					chargesBookingDto.setUserId("EOD");
					chargesBookingDto.setLoanId(agrLoansList.get(0).getLoanId());
					log.info("getEventBaseChargesCalculation 4");
					tranService.chargeBookingBatchApply(chargesBookingDto);
					log.info("getEventBaseChargesCalculation 5");
				}
			}

		} catch (CoreDataNotFoundException e) {
			throw e;
		} catch (Exception e) {
			throw e;
		}

		return result;
	}

}
