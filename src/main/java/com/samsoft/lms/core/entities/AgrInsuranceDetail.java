package com.samsoft.lms.core.entities;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.samsoft.lms.customer.entities.AgrCustomer;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

import javax.persistence.*;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "agr_insurance_details")
@Builder
public class AgrInsuranceDetail {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "iInsDtlId")
    private Integer insDtlId;

    @Column(name = "sInsuranceCompany")
    private String insuranceCompany;

    @Column(name = "dInsurancePremiumAmount")
    private Double insurancePremiumAmount;

    @Column(name="sCustomerId")
    private String customerId;

    @OneToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "sMastAgrId", nullable = false)
    @JsonIgnore
    private AgrMasterAgreement masterAgreement;

}
