package com.samsoft.lms.newux.reports.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.samsoft.lms.newux.reports.entity.CustomerwiseOutstandingPortfolio;
import com.samsoft.lms.newux.reports.repository.CustomerwiseOutstandingPortfolioRepository;

import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
public class CustomerwiseOutstandingPortfolioService {

	@Autowired
	private CustomerwiseOutstandingPortfolioRepository customerwiseOutstandingPortfolioRepository;

	public List<CustomerwiseOutstandingPortfolio> getCustomerwiseOutstandingPortfolio(String homeBranch,
			String assetClass) throws Exception {
		try {
			return customerwiseOutstandingPortfolioRepository.findByHomeBranchAndAssetClass(homeBranch, assetClass);
		} catch (Exception e) {
			log.error("Error in getCustomerwiseOutstandingPortfolio()");
			log.error(e.getMessage());
			throw e;
		}
	}
}
