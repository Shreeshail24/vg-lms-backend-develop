package com.samsoft.lms.banking.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.Date;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "bank_payment_logs")
@Builder
public class BankPaymentLogs {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ibankpamentlogid", nullable = false, unique = true)
    private Integer bankPaymentLogId;

    @Column(name = "ipaymentid")
    private Integer paymentId;

    @Column(name = "ivapaymentid")
    private Integer vaPaymentId;

    @Column(name = "szjsondata", columnDefinition = "JSON")
    private String jsonData;

    @Column(name = "szerror")
    private String error;

    @Column(name = "dtcreateddatetime")
    private Date createdDateTime;

    @Column(name = "dtupdateddatetime")
    private Date updatedDateTime;

}
