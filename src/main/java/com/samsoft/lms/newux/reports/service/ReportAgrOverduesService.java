package com.samsoft.lms.newux.reports.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.samsoft.lms.newux.reports.entity.ReportAgrOverdues;
import com.samsoft.lms.newux.reports.repository.ReportAgrOverduesRepository;

import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
public class ReportAgrOverduesService {

	@Autowired
	private ReportAgrOverduesRepository overduesRepository;

	public List<ReportAgrOverdues> getReportAgrOverdues() throws Exception {
		try {
			return overduesRepository.findAll();
		} catch (Exception e) {
			log.error("Error in getReportAgrOverdues()");
			log.error(e.getMessage());
			throw e;
		}
	}
}
