package com.samsoft.lms.newux.reports.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.samsoft.lms.newux.reports.entity.CustomerwiseOutstandingPortfolio;

public interface CustomerwiseOutstandingPortfolioRepository extends JpaRepository<CustomerwiseOutstandingPortfolio, String> {

	public List<CustomerwiseOutstandingPortfolio> findByHomeBranchAndAssetClass(String homeBranch, String assetClass);
}
