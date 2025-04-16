package com.samsoft.lms.core.services;

import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.samsoft.lms.batch.exceptions.EodExceptions;
import com.samsoft.lms.core.dto.AmortParameter;
import com.samsoft.lms.core.dto.AmortVariationDto;
import com.samsoft.lms.core.dto.AmortVariationListDto;
import com.samsoft.lms.core.entities.Amort;

@Service
public class CoreAmortVariation {

	@Autowired
	private CoreAmort coreAmortServ;

	@Autowired
	private Environment env;

	@Autowired
	private InterestService interestService;
	
	@Autowired
	private CommonServices commService;

	@Transactional(rollbackFor = { RuntimeException.class, Error.class, Exception.class })
	public List<Amort> getAmortVariation(AmortVariationDto amortDto) throws Exception {
		List<Amort> amortListMain = null;
		List<Amort> amortListTemp = null;
		List<Amort> amortSkipListMain = new ArrayList<Amort>();
		boolean stepCheck = true;
		Integer installmentNo = 0;
		Integer amortInstallmentNo = 0;
		String dateFormat = env.getProperty("lms.global.date.format");
		SimpleDateFormat sdf = new SimpleDateFormat(dateFormat);
		try {
			AmortParameter amortParam = new AmortParameter();
			amortParam.setAmortizationMethod(amortDto.getAmortizationMethod());
			amortParam.setAmortizationType(amortDto.getAmortizationType());
			amortParam.setBpiHandling(amortDto.getBpiHandling());
			amortParam.setDtDisbursement(amortDto.getDtDisbursement());
			amortParam.setDtInstallmentStart(amortDto.getDtInstallmentStart());
			amortParam.setEmiBasis(amortDto.getEmiBasis());
			amortParam.setEmiRounding(amortDto.getEmiRounding());
			amortParam.setInterestBasis(amortDto.getInterestBasis());
			amortParam.setInterestRate(amortDto.getInterestRate());
			amortParam.setLastRowThresholdPercentage(amortDto.getLastRowThreshold());
			amortParam.setLoanAmount(amortDto.getLoanAmount());
			amortParam.setNoOfAdvanceEmi(amortDto.getNoOfAdvEmi());
			amortParam.setRepaymentFrequency(amortDto.getRepaymentFreq());
			amortParam.setTenor(amortDto.getTenor());
			amortParam.setTenorUnit(amortDto.getTenorUnit());

			amortListMain = coreAmortServ.getAmort(amortParam);
			amortListTemp = new ArrayList<Amort>(amortListMain);

			for (AmortVariationListDto variation : amortDto.getVariationList()) {
				installmentNo = 0;

				for (int i = 0; i < amortListMain.size(); i++) {
					Amort amort = amortListMain.get(i);
					Amort nextAmortRow = null;
					if (i < amortListMain.size() - 1) {
						nextAmortRow = amortListMain.get(i + 1);
					}

					if (amort.getInstallmentNo() >= variation.getFromInstNo()) {
						installmentNo = installmentNo + 1;
						amortInstallmentNo = amort.getInstallmentNo();
						if (variation.getVariationType().equalsIgnoreCase("SKIP")) {
							if (variation.getVariationOption().equalsIgnoreCase("FREE")) {
								amort.setClosingBalance(amort.getOpeningBalance());
								nextAmortRow.setOpeningBalance(amort.getOpeningBalance());
								// nextAortRow.setClosingBalance(amort.getOpeningBalance());
								amort.setInterestAmount(0);
								amort.setPrincipalAmount(0);
								amort.setInstallmentAmount(0);
							} else if (variation.getVariationOption().equalsIgnoreCase("CAPITALIZE")) {
								double interestAmount = 0;
								if (amort.getInstallmentNo() == 1) {
									interestAmount = interestService.getInterestAmount(amort.getOpeningBalance(),
											amortDto.getInterestRate(), amortDto.getInterestBasis(),
											sdf.parse(amortDto.getDtDisbursement()),
											sdf.parse(amort.getDtInstallment()));
								} else {
									interestAmount = interestService.getInterestAmount(amort.getOpeningBalance(),
											amortDto.getInterestRate(), amortDto.getInterestBasis(),
											sdf.parse(amortListMain.get(i - 1).getDtInstallment()),
											sdf.parse(amort.getDtInstallment()));
								}

								amort.setClosingBalance(amort.getOpeningBalance() + Math.round(interestAmount));
								nextAmortRow.setOpeningBalance(amort.getClosingBalance());
								amort.setInterestAmount(0);
								amort.setPrincipalAmount(0);
								amort.setInstallmentAmount(0);
							} else {
								amort.setClosingBalance(amort.getOpeningBalance());
								nextAmortRow.setOpeningBalance(amort.getOpeningBalance());
								amort.setPrincipalAmount(0);
								if (amort.getInstallmentNo() == 1) {
									amort.setInstallmentAmount(Math.round(interestService.getInterestAmount(
											amort.getOpeningBalance(), amortDto.getInterestRate(),
											amortDto.getInterestBasis(), sdf.parse(amortDto.getDtDisbursement()),
											sdf.parse(amort.getDtInstallment()))));
								} else {
									amort.setInstallmentAmount(Math.round(
											interestService.getInterestAmount(amort.getOpeningBalance(),
													amortDto.getInterestRate(), amortDto.getInterestBasis(),
													sdf.parse(amortListMain.get(i - 1).getDtInstallment()),
													sdf.parse(amort.getDtInstallment()))));
								}

							}

						}
						double newEmiAmount = 0, upAmount = 0;
						if (variation.getVariationType().equalsIgnoreCase("STEPUP")) {
							if (variation.getVariationOption().equalsIgnoreCase("AMOUNT")) {
								newEmiAmount = Math.round(amort.getInstallmentAmount() + variation.getVariationValue());
							} else if (variation.getVariationOption().equalsIgnoreCase("PERCENTAGE")) {
								if (stepCheck) {
									upAmount = commService.numberFormatter(
											amort.getInstallmentAmount() * variation.getVariationValue() / 100);
									newEmiAmount = Math.round(amort.getInstallmentAmount() + upAmount);
								}

							}

							if (amort.getInstallmentNo() == variation.getFromInstNo()) {
								if (stepCheck) {
									AmortParameter amortSkipParam = new AmortParameter();
									amortSkipParam.setAmortizationMethod(amortDto.getAmortizationMethod());
									amortSkipParam.setAmortizationType(amortDto.getAmortizationType());
									amortSkipParam.setBpiHandling(amortDto.getBpiHandling());
									if ((amortInstallmentNo - 2) < 0) {
										amortSkipParam.setDtDisbursement(amortDto.getDtDisbursement());
										amortSkipParam.setLoanAmount(amortDto.getLoanAmount());

									} else {
										amortSkipParam.setDtDisbursement(
												amortListTemp.get(amortInstallmentNo - 2).getDtInstallment());
										amortSkipParam.setLoanAmount(
												amortListMain.get(amortInstallmentNo - 2).getClosingBalance());

									}
									amortSkipParam.setDtInstallmentStart(
											amortListTemp.get(amortInstallmentNo - 1).getDtInstallment());
									amortSkipParam.setEmiBasis(amortDto.getEmiBasis());
									amortSkipParam.setEmiRounding(amortDto.getEmiRounding());
									amortSkipParam.setInterestBasis(amortDto.getInterestBasis());
									amortSkipParam.setInterestRate(amortDto.getInterestRate());
									amortSkipParam.setLastRowThresholdPercentage(amortDto.getLastRowThreshold());
									amortSkipParam.setNoOfAdvanceEmi(amortDto.getNoOfAdvEmi());
									amortSkipParam.setRepaymentFrequency(amortDto.getRepaymentFreq());
									amortSkipParam.setTenor(amortDto.getTenor());
									amortSkipParam.setTenorUnit(amortDto.getTenorUnit());
									amortSkipParam.setEmiAmount(newEmiAmount);

									amortSkipListMain = coreAmortServ.getAmort(amortSkipParam);
									Iterator it = amortListMain.iterator();
									while (it.hasNext()) {
										Amort next = (Amort) it.next();
										System.out.println(next);
										if (next.getInstallmentNo() > amortInstallmentNo - 1) {
											it.remove();
										}
									}
									for (int j = 0; j < variation.getNoOfInstallment(); j++) {
										Amort amortStepUp = amortSkipListMain.get(j);
										amortStepUp.setInstallmentNo(
												amortStepUp.getInstallmentNo() + amortInstallmentNo - 1);
										amortListMain.add(amortStepUp);

									}
								}

							}
							stepCheck = false;
						}

						if (variation.getVariationType().equalsIgnoreCase("STEPDOWN")) {
							if (variation.getVariationOption().equalsIgnoreCase("AMOUNT")) {
								newEmiAmount = Math.round(amort.getInstallmentAmount() - variation.getVariationValue());
							} else if (variation.getVariationOption().equalsIgnoreCase("PERCENTAGE")) {
								if (stepCheck) {
									upAmount = commService.numberFormatter(
											amort.getInstallmentAmount() * variation.getVariationValue() / 100);
									newEmiAmount = Math.round(amort.getInstallmentAmount() - upAmount);
								}

							}
							if (stepCheck) {
								AmortParameter amortSkipParam = new AmortParameter();
								amortSkipParam.setAmortizationMethod(amortDto.getAmortizationMethod());
								amortSkipParam.setAmortizationType(amortDto.getAmortizationType());
								amortSkipParam.setBpiHandling(amortDto.getBpiHandling());
								if ((amortInstallmentNo - 2) < 0) {
									amortSkipParam.setDtDisbursement(amortDto.getDtDisbursement());
									amortSkipParam.setLoanAmount(amortDto.getLoanAmount());

								} else {
									amortSkipParam.setDtDisbursement(
											amortListTemp.get(amortInstallmentNo - 2).getDtInstallment());
									amortSkipParam.setLoanAmount(
											amortListMain.get(amortInstallmentNo - 2).getClosingBalance());

								}
								amortSkipParam.setDtInstallmentStart(
										amortListTemp.get(amortInstallmentNo - 1).getDtInstallment());
								amortSkipParam.setEmiBasis(amortDto.getEmiBasis());
								amortSkipParam.setEmiRounding(amortDto.getEmiRounding());
								amortSkipParam.setInterestBasis(amortDto.getInterestBasis());
								amortSkipParam.setInterestRate(amortDto.getInterestRate());
								amortSkipParam.setLastRowThresholdPercentage(amortDto.getLastRowThreshold());
								amortSkipParam.setNoOfAdvanceEmi(amortDto.getNoOfAdvEmi());
								amortSkipParam.setRepaymentFrequency(amortDto.getRepaymentFreq());
								amortSkipParam.setTenor(amortDto.getTenor());
								amortSkipParam.setTenorUnit(amortDto.getTenorUnit());
								amortSkipParam.setEmiAmount(newEmiAmount);

								amortSkipListMain = coreAmortServ.getAmort(amortSkipParam);
								Iterator it = amortListMain.iterator();
								while (it.hasNext()) {
									Amort next = (Amort) it.next();
									System.out.println(next);
									if (next.getInstallmentNo() > amortInstallmentNo - 1) {
										it.remove();
									}
								}

								for (Amort amortStepDown : amortSkipListMain) {
									amortStepDown.setInstallmentNo(
											amortStepDown.getInstallmentNo() + amortInstallmentNo - 1);
									amortListMain.add(amortStepDown);
								}
							}
							stepCheck = false;
						}

						if (variation.getVariationType().equalsIgnoreCase("BALOONPAYMENT")) {
							if (variation.getVariationOption().equalsIgnoreCase("AMOUNT")) {
								amort.setClosingBalance(amort.getClosingBalance() - variation.getVariationValue());
								nextAmortRow.setOpeningBalance(amort.getClosingBalance());
								amort.setPrincipalAmount(amort.getPrincipalAmount() + variation.getVariationValue());
								amort.setInstallmentAmount(
										amort.getInstallmentAmount() + variation.getVariationValue());

							}

						}

						if (installmentNo == variation.getNoOfInstallment() && variation.getNoOfInstallment() != 0) {
							break;
						}
					}

				}

				if (variation.getVariationType().equalsIgnoreCase("SKIP")
						|| variation.getVariationType().equalsIgnoreCase("STEPUP")
						|| variation.getVariationType().equalsIgnoreCase("STEPDOWN")
						|| variation.getVariationType().equalsIgnoreCase("BALOONPAYMENT")) {
					/*
					 * for (int i = installmentNo; i < amortListMain.size(); i++) {
					 * System.out.println(amortListMain.get(i)); amortListMain.remove(i);
					 * System.out.println(amortListMain.size()); }
					 */

					Iterator i = amortListMain.iterator();
					while (i.hasNext()) {
						Amort next = (Amort) i.next();
						System.out.println(next);
						if (next.getInstallmentNo() > amortInstallmentNo) {
							i.remove();
						}
					}

					if (variation.getAdjustmentFactor().equalsIgnoreCase("INSTALLMENT")) {
						double newEmi = Math.round(
								coreAmortServ.getEMI(amortListMain.get(amortInstallmentNo - 1).getClosingBalance(),
										amortDto.getInterestRate(), amortDto.getTenor() - amortInstallmentNo,
										amortDto.getTenorUnit(), amortDto.getRepaymentFreq(),
										amortDto.getInterestBasis(), amortDto.getAmortizationMethod()));

						AmortParameter amortSkipParam = new AmortParameter();
						amortSkipParam.setAmortizationMethod(amortDto.getAmortizationMethod());
						amortSkipParam.setAmortizationType(amortDto.getAmortizationType());
						amortSkipParam.setBpiHandling(amortDto.getBpiHandling());
						amortSkipParam.setDtDisbursement(amortListTemp.get(amortInstallmentNo - 1).getDtInstallment());
						amortSkipParam.setDtInstallmentStart(amortListTemp.get(amortInstallmentNo).getDtInstallment());
						amortSkipParam.setEmiBasis(amortDto.getEmiBasis());
						amortSkipParam.setEmiRounding(amortDto.getEmiRounding());
						amortSkipParam.setInterestBasis(amortDto.getInterestBasis());
						amortSkipParam.setInterestRate(amortDto.getInterestRate());
						amortSkipParam.setLastRowThresholdPercentage(amortDto.getLastRowThreshold());
						amortSkipParam.setLoanAmount(amortListMain.get(amortInstallmentNo - 1).getClosingBalance());
						amortSkipParam.setNoOfAdvanceEmi(amortDto.getNoOfAdvEmi());
						amortSkipParam.setRepaymentFrequency(amortDto.getRepaymentFreq());
						amortSkipParam.setTenor(amortDto.getTenor() - amortInstallmentNo);
						amortSkipParam.setTenorUnit(amortDto.getTenorUnit());
						amortSkipParam.setEmiAmount(newEmi);

						amortSkipListMain = coreAmortServ.getAmort(amortSkipParam);
					} else {

						AmortParameter amortSkipParam = new AmortParameter();
						amortSkipParam.setAmortizationMethod(amortDto.getAmortizationMethod());
						amortSkipParam.setAmortizationType(amortDto.getAmortizationType());
						amortSkipParam.setBpiHandling(amortDto.getBpiHandling());
						amortSkipParam.setDtDisbursement(amortListTemp.get(amortInstallmentNo - 1).getDtInstallment());
						amortSkipParam.setDtInstallmentStart(amortListTemp.get(amortInstallmentNo).getDtInstallment());
						amortSkipParam.setEmiBasis(amortDto.getEmiBasis());
						amortSkipParam.setEmiRounding(amortDto.getEmiRounding());
						amortSkipParam.setInterestBasis(amortDto.getInterestBasis());
						amortSkipParam.setInterestRate(amortDto.getInterestRate());
						amortSkipParam.setLastRowThresholdPercentage(amortDto.getLastRowThreshold());
						amortSkipParam.setLoanAmount(amortListMain.get(amortInstallmentNo - 1).getClosingBalance());
						amortSkipParam.setNoOfAdvanceEmi(amortDto.getNoOfAdvEmi());
						amortSkipParam.setRepaymentFrequency(amortDto.getRepaymentFreq());
						amortSkipParam.setTenor(amortListMain.size() - amortInstallmentNo - 1);
						amortSkipParam.setTenorUnit(amortDto.getTenorUnit());
						amortSkipParam.setEmiAmount(0);

						amortSkipListMain = coreAmortServ.getAmort(amortSkipParam);
					}
				}

			}

			for (AmortVariationListDto variation : amortDto.getVariationList()) {
				installmentNo = 1;
				for (int i = 0; i < amortListMain.size(); i++) {
					Amort amort = amortListMain.get(i);

					if (variation.getVariationType().equalsIgnoreCase("BULLETPAYMENT")) {
						if (variation.getFromInstNo() != installmentNo) {
							installmentNo = installmentNo + 1;
							amort.setClosingBalance(amortDto.getLoanAmount());
							amort.setOpeningBalance(amortDto.getLoanAmount());
							amort.setPrincipalAmount(0);
							if (amort.getInstallmentNo() == 1) {
								amort.setInterestAmount(Math.round(interestService.getInterestAmount(amortDto.getLoanAmount(),
										amortDto.getInterestRate(), amortDto.getInterestBasis(),
										sdf.parse(amortDto.getDtDisbursement()), sdf.parse(amort.getDtInstallment()))));
							} else {
								amort.setInterestAmount(Math.round(interestService.getInterestAmount(amortDto.getLoanAmount(),
										amortDto.getInterestRate(), amortDto.getInterestBasis(),
										sdf.parse(amortListMain.get(i - 1).getDtInstallment()),
										sdf.parse(amort.getDtInstallment()))));
							}

							amort.setInstallmentAmount(amort.getInterestAmount());

						} else {
							amort.setClosingBalance(0);
							amort.setPrincipalAmount(amortDto.getLoanAmount());
							amort.setOpeningBalance(amortDto.getLoanAmount());
							if (amort.getInstallmentNo() == 1) {
								amort.setInterestAmount(Math.round(interestService.getInterestAmount(amortDto.getLoanAmount(),
										amortDto.getInterestRate(), amortDto.getInterestBasis(),
										sdf.parse(amortDto.getDtDisbursement()), sdf.parse(amort.getDtInstallment()))));
							} else {
								amort.setInterestAmount(Math.round(interestService.getInterestAmount(amortDto.getLoanAmount(),
										amortDto.getInterestRate(), amortDto.getInterestBasis(),
										sdf.parse(amortListMain.get(i - 1).getDtInstallment()),
										sdf.parse(amort.getDtInstallment()))));
							}

							amort.setInstallmentAmount(amort.getInterestAmount() + amort.getPrincipalAmount());
						}

					}

				}

			}
		} catch (Exception e) {
			throw e;
		}
		// amortListMain.addAll(installmentNo, amortSkipListMain);
		for (Amort amort : amortSkipListMain) {
			amort.setInstallmentNo(amort.getInstallmentNo() + amortInstallmentNo);
			amortListMain.add(amort);
		}
		return amortListMain; // temporary return;

	}

}
