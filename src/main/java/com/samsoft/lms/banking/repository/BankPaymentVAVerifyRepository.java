package com.samsoft.lms.banking.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.samsoft.lms.banking.entity.BankPaymentVAVerify;

@Repository
public interface BankPaymentVAVerifyRepository extends JpaRepository<BankPaymentVAVerify, Integer> {
}
