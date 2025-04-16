package com.samsoft.lms.core.entities;

import java.time.LocalDate;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.ColumnResult;
import javax.persistence.ConstructorResult;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.NamedNativeQuery;
import javax.persistence.SqlResultSetMapping;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import com.samsoft.lms.core.dto.SysTranDtlDto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;


@NamedNativeQuery(name = "AgrTrnSysTranDtl.getSysTranDetails",
query = "SELECT  sum(nTranAmount) as totalPenal, nInstallmentNo as instrumentNo FROM agr_trn_sys_tran_dtl A WHERE A.sTranType= :tranType and A.sLoanId = :loanId and A.sAdjustedYn='N' and A.dTranDate < :backupDate group by nInstallmentNo  order by nInstallmentNo",
resultSetMapping = "Mapping.SysTranDtlDto")
@SqlResultSetMapping(name = "Mapping.SysTranDtlDto",
   classes = @ConstructorResult(targetClass = SysTranDtlDto.class,
                                columns = {@ColumnResult(name = "totalPenal",type = Double.class),
                                		@ColumnResult(name = "instrumentNo",type = Integer.class)}))

@Entity
@Table(name = "agr_trn_sys_tran_dtl")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class AgrTrnSysTranDtl {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "nTranId", length = 20)
	private Integer tranId;

	@Column(name = "sCustomerId", length = 20)
	private String customerId;

	@Column(name = "sMastAgrId", length = 20)
	private String mastAgrId;

	@Column(name = "sLoanId", length = 20)
	private String loanId;

	@Column(name = "dTranDate")
	@Temporal(TemporalType.DATE)
	private Date dtTranDate;

	@Column(name = "sTranType", length = 50)
	private String tranType;

	@Column(name = "nTranAmount", length = 20)
	private Double tranAmount;
	
	@Column(name = "nInstallmentNo", length = 5)
	private Integer installmentNo;

	@Column(name = "nDpd", length = 3, columnDefinition = "Integer default 0")
	private Integer dpd=0;
	
	@Column(name = "sRemark", length = 100)
	private String remark;
	
	@Column(name = "sAdjustedYn", length = 10, columnDefinition = "varchar(10) default 'N'")
	private String adjustedYn="N";

	@Column(name = "sUserID", length = 200)
	private String userId;

	@Column(name = "dLastUpdated")
	
	private LocalDate dtLastUpdated = LocalDate.now();

	@Column(name = "dUserDate")
	
	private LocalDate dtUserDate = LocalDate.now();

	@Column(name = "nGLGeneratedYN", length = 2, columnDefinition = "varchar(2) default 'N'")
	private String glGeneratedYn;

}
