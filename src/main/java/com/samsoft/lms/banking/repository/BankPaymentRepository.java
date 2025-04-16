package com.samsoft.lms.banking.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.samsoft.lms.banking.entity.BankPayment;

@Repository
public interface BankPaymentRepository extends JpaRepository<BankPayment, Integer> {

    @Query(value = "select bp from BankPayment bp where virtualId = :virtualId and paymentStatus = :paymentStatus and SUBSTR(createdDateTime, 1, 10) = CURDATE()")
    public BankPayment findByVirtualIdAndPaymentStatus(String virtualId, String paymentStatus);

    public BankPayment findByVirtualIdAndPaymentStatusAndPaymentSessionId(String virtualId, String paymentStatus, String paymentSessionId);

}
