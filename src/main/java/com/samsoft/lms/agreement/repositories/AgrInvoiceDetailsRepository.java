package com.samsoft.lms.agreement.repositories;

import org.springframework.data.jpa.repository.JpaRepository;

import com.samsoft.lms.agreement.entities.AgrInvoiceDetails;

public interface AgrInvoiceDetailsRepository extends  JpaRepository<AgrInvoiceDetails, Integer>{

	public AgrInvoiceDetails findByMasterMastAgrId(String mastAgrId);
}
