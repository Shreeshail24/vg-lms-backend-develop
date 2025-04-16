package com.samsoft.lms.core.services;

import static java.time.temporal.TemporalAdjusters.firstDayOfYear;
import static java.time.temporal.TemporalAdjusters.lastDayOfYear;

import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.Period;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
import java.time.temporal.TemporalAdjusters;
import java.util.Date;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.samsoft.lms.batch.exceptions.EodExceptions;
import com.samsoft.lms.core.exceptions.CoreBadRequestException;

@Service
public class InterestService {

	@Autowired
	private Environment env;

	@Autowired
	private CommonServices commService;

	@Transactional(rollbackFor = { RuntimeException.class, Error.class, Exception.class })
	public int getInterestDays(String interestBasis, LocalDate dtStartDate, LocalDate dtEndDate) {

		int totalNoOfDays = 0, startMonthDays = 0, endMonthDays = 0, middleMonthDays = 0;
		int startDay, startMonth, startYear = 0;
		int endDay, endMonth, endYear = 0;

		SimpleDateFormat sdf = new SimpleDateFormat("dd-MMM-yyyy");
		DateTimeFormatter dateTime = DateTimeFormatter.ofPattern("dd-MMM-yyyy");

		LocalDate endDayOfStartDate = dtStartDate.with(TemporalAdjusters.lastDayOfMonth());
		LocalDate endDayOfEndDate = dtEndDate.with(TemporalAdjusters.lastDayOfMonth());

		startDay = dtStartDate.getDayOfMonth();
		startMonth = dtStartDate.getMonthValue();
		startYear = dtStartDate.getYear();

		endDay = dtEndDate.getDayOfMonth();
		endMonth = dtEndDate.getMonthValue();
		endYear = dtEndDate.getYear();

		if (startMonth == endMonth) {
			if (interestBasis.equalsIgnoreCase("30/360") || interestBasis.equalsIgnoreCase("30/365")) {
				if ((startDay != endDayOfStartDate.getDayOfMonth()) && (endDay == endDayOfEndDate.getDayOfMonth())) {
					totalNoOfDays = 30 - startDay;
				}
				if ((startDay != endDayOfStartDate.getDayOfMonth()) && (endDay != endDayOfEndDate.getDayOfMonth())) {
					totalNoOfDays = endDay - startDay + 1;
				}
				if ((startDay == endDayOfStartDate.getDayOfMonth()) && (endDay == endDayOfEndDate.getDayOfMonth())) {
					if (interestBasis.equalsIgnoreCase("30/360") && startDay == 30 && endDay == 30) {
						totalNoOfDays = 1;
					} else {
						totalNoOfDays = endDay - startDay;
					}

				}
			} else {
				totalNoOfDays = endDay - startDay + 1;
			}
		}

		if (startMonth != endMonth) {
			if (interestBasis.equalsIgnoreCase("30/360") || interestBasis.equalsIgnoreCase("30/365")) {
				if (startDay == endDayOfStartDate.getDayOfMonth()) {
					switch (endDayOfStartDate.getDayOfMonth()) {
					case 31:
						startMonthDays = 0;
						break;
					case 30:
						startMonthDays = 1;
						break;
					case 29:
						startMonthDays = 2;
						break;
					case 28:
						startMonthDays = 3;
						break;
					}
				}

				if (startDay != endDayOfStartDate.getDayOfMonth()) {
					startMonthDays = 30 - startDay + 1;
				}

				if (startDay == 1) {
					startMonthDays = 30;
				}

				if (endDay == endDayOfEndDate.getDayOfMonth()) {
					endMonthDays = 30 - 1;
				}
				if (endDay != endDayOfEndDate.getDayOfMonth()) {
					endMonthDays = endDay - 1; // removed 1 because adding additional days
				}

				// middleMonthDays = (endMonth - startMonth - 1) * 30;

				middleMonthDays = (Period.between(dtStartDate, dtEndDate).getMonths() - 1);
				if (middleMonthDays >= 0) {
					middleMonthDays = middleMonthDays * 30;
				} else {
					middleMonthDays = 0;
				}
				totalNoOfDays = startMonthDays + endMonthDays + middleMonthDays;

			} else {
				// int diff = Period.between(dtStartDate, dtEndDate).getDays();
				totalNoOfDays = (int) ChronoUnit.DAYS.between(dtStartDate, dtEndDate);
				// totalNoOfDays = diff + 1;
			}

		}

		return totalNoOfDays;
	}

	@Transactional(rollbackFor = { RuntimeException.class, Error.class, Exception.class })
	public double getInterestAmount(Double principalAmount, Float interestRate, String interestBasis, Date intStartDate,
			Date intEndDate) {
		String dateFormat = env.getProperty("lms.global.date.format");

		DateTimeFormatter formatter = DateTimeFormatter.ofPattern(dateFormat);

		LocalDate interestStartDate = LocalDate.parse(new SimpleDateFormat(dateFormat).format(intStartDate), formatter);
		LocalDate interestEndDate = LocalDate.parse(new SimpleDateFormat(dateFormat).format(intEndDate), formatter);

		int divisionFactor = 0, interestDays = 0, startYearInterestDays = 0, endYearInterestDays = 0;
		double interestAmount = 0.0, startInterestAmount = 0.0, endInterestAmount = 0.0;

		interestDays = getInterestDays(interestBasis, interestStartDate, interestEndDate);

		/*
		 * DateTimeFormatter dateTime = DateTimeFormatter.ofPattern("dd-MM-yyyy");
		 * LocalDate dtStartDate = LocalDate.parse(interestStartDate, dateTime);
		 * LocalDate dtEndDate = LocalDate.parse(interestEndDate, dateTime);
		 */

		DateTimeFormatter dateTime = DateTimeFormatter.ofPattern(dateFormat);

		SimpleDateFormat sdf = new SimpleDateFormat(dateFormat);

		LocalDate endDayOfStartDate = interestStartDate.with(TemporalAdjusters.lastDayOfMonth());
		LocalDate endDayOfEndDate = interestEndDate.with(TemporalAdjusters.lastDayOfMonth());

		int startDay = interestStartDate.getDayOfMonth();
		int startMonth = interestStartDate.getMonthValue();
		int startYear = interestStartDate.getYear();

		int endDay = interestEndDate.getDayOfMonth();
		int endMonth = interestEndDate.getMonthValue();
		int endYear = interestEndDate.getYear();

		if (startYear == endYear) {
			if ((interestBasis.equalsIgnoreCase("30/360")) || (interestBasis.equalsIgnoreCase("ACTUAL/360"))) {
				divisionFactor = 360;
			}
			if ((interestBasis.equalsIgnoreCase("ACTUAL/365"))) {
				divisionFactor = 365;
			}
			if ((interestBasis.equalsIgnoreCase("ACTUAL/ACTUAL"))) {
				if (((startYear % 4 == 0) && (startYear % 100 != 0)) || (startYear % 400 == 0)) {
					divisionFactor = 366;
				} else {
					divisionFactor = 365;
				}
			}

			interestAmount = ((principalAmount * (interestRate / 100)) / divisionFactor) * interestDays;

		}

		if (startYear != endYear) {
			if ((interestBasis.equalsIgnoreCase("30/360")) || (interestBasis.equalsIgnoreCase("ACTUAL/360"))) {
				divisionFactor = 360;
			}
			if ((interestBasis.equalsIgnoreCase("ACTUAL/365"))) {
				divisionFactor = 365;
			}
			interestAmount = ((principalAmount * (interestRate / 100)) / divisionFactor) * interestDays;

			if ((interestBasis.equalsIgnoreCase("ACTUAL/ACTUAL"))) {
				if (((startYear % 4 == 0) && (startYear % 100 != 0)) || (startYear % 400 == 0)) {
					divisionFactor = 366;
				} else {
					divisionFactor = 365;
				}
				startYearInterestDays = getInterestDays(interestBasis, interestStartDate,
						interestStartDate.with(lastDayOfYear()));
				startInterestAmount = ((principalAmount * (interestRate / 100)) / divisionFactor)
						* startYearInterestDays;

				if (((endYear % 4 == 0) && (endYear % 100 != 0)) || (endYear % 400 == 0)) {
					divisionFactor = 366;
				} else {
					divisionFactor = 365;
				}
				endYearInterestDays = getInterestDays(interestBasis, interestEndDate.with(firstDayOfYear()),
						interestEndDate);
				endInterestAmount = ((principalAmount * (interestRate / 100)) / divisionFactor) * endYearInterestDays;

				interestAmount = startInterestAmount + endInterestAmount;
			}

		}

		return commService.numberFormatter(interestAmount);
	}

}
