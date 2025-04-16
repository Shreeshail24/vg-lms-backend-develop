package com.samsoft.lms.banking.entity;

import lombok.*;

import javax.persistence.*;
import java.util.Date;

@Entity
@Table(name="bank_payments")
@Data
@AllArgsConstructor
@NoArgsConstructor
@ToString
@Builder
public class BankPayment {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ipaymentid", nullable = false, unique = true)
    private Integer paymentId;

    @Column(name = "szvirtualid", length = 17)
    private String virtualId;

    @Column(name = "damount")
    private Double amount;

    @Column(name = "szinstaalertjsondata", columnDefinition = "JSON")
    private String instaAlertJsonData;

    @Column(name = "szsource")
    private String source;

    @Column(name = "cpaymentstatus")
    private String paymentStatus;

    @Column(name = "szutrno")
    private String utrNo;

    @Column(name = "dtcreateddatetime")
    private Date createdDateTime;

    @Column(name = "dtupdateddatetime")
    private Date updatedDateTime;

    //Payment Gateway
    @Column(name = "szpaymentsessionid")
    private String paymentSessionId;

    @Column(name = "szcforderid")
    private String cfOrderId;

    @Column(name = "szorderstatus")
    private String orderStatus;

    @Column(name = "szpaymentgatewayjsondata", columnDefinition = "JSON")
    private String paymentGatewayJsonData;

    @Column(name = "szMastAgrId", length = 12)
    private String mastAgrId;

    @Column(name = "szcustomername")
    private String customerName;
}
