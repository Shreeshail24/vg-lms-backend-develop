package com.samsoft.lms.banking.entity;

import lombok.*;

import javax.persistence.*;
import java.util.Date;

@Entity
@Table(name="bank_payment_va_verify")
@Data
@AllArgsConstructor
@NoArgsConstructor
@ToString
@Builder
public class BankPaymentVAVerify {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ivapaymentid", nullable = false, unique = true)
    private Integer vaPaymentId;

    @Column(name = "szvirtualid", length = 17)
    private String virtualId;

    @Column(name = "damount")
    private Double amount;

    @Column(name = "szvavalidationjsondata", columnDefinition = "JSON")
    private String vaValidationJsonData;

    @Column(name = "dtcreateddatetime")
    private Date createdDateTime;

    @Column(name = "dtupdateddatetime")
    private Date updatedDateTime;
}
