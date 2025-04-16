package com.samsoft.lms.core.entities;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.ColumnResult;
import javax.persistence.ConstructorResult;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.NamedNativeQuery;
import javax.persistence.SqlResultSetMapping;
import javax.persistence.Table;

import org.hibernate.annotations.Immutable;
import org.hibernate.annotations.Subselect;

import com.samsoft.lms.core.dto.RuleDetailsDto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@NamedNativeQuery(name = "VMstPayAppRuleSet.getRuleDetails", query = "SELECT IFNULL(prr.NPAYGROUPSEQ, 999) AS npaGroupSeq, IFNULL(prr.NSORTSEQ, 999) AS sortSeq, dd1.SDUECATEGORY AS dueCategory, dd1.SPAYHEAD AS payHead, dd1.NDUEDTLID AS dueDtlId, dd1.NINSTALLMENTNO AS installmentNo, dd1.NDUEAMOUNT as dueAmount, dd1.DDUEDATE as dueDate, mlr.TOTALDUE as totalDue, dd1.sLoanId as loanId,dd1.nTranDtlId as tranDtlId FROM (SELECT IFNULL(rs.sPayHead, CASE WHEN dd.sduecategory = 'INSTALLMENT' THEN 'DEFAULT_INSTALLMENT' WHEN 'FEE' THEN 'DEFAULT_CHARGES' WHEN 'TAX' THEN 'DEFAULT_TAXES' ELSE dd.sduecategory END) AS sPayHead, dd.sduecategory, dd.sduehead, dd.ndueamount, dd.dduedate, dd.ninstallmentno, dd.smastagrid, dd.nduedtlid, dd.sLoanId, dd.nTranDtlId FROM v_mst_PayApp_Rulset rs LEFT JOIN agr_trn_due_dtl dd ON rs.sPayHead = dd.sduehead WHERE dd.smastagrid = :masterAgreement AND rs.sPayRuleID = :ruleId) dd1, (SELECT  rs1.nPayGroupSeq, rs1.nSortSeq, rs1.sPayHead FROM v_mst_PayApp_Rulset rs1 WHERE rs1.sPayRuleID = :ruleId) prr, (SELECT  dd2.dduedate, dd2.sduehead, SUM(dd2.ndueamount) AS Totaldue FROM agr_trn_due_dtl dd2 WHERE dd2.smastagrid = :masterAgreement GROUP BY dd2.dduedate , dd2.sduehead) mlr WHERE dd1.SDUEHEAD = prr.sPayHead  AND dd1.SMASTAGRID = :masterAgreement AND dd1.DDUEDATE = mlr.DDUEDATE AND dd1.SDUEHEAD = mlr.SDUEHEAD AND dd1.sduecategory = (CASE WHEN (:paymentFor = 'ALL_DUES') THEN dd1.sduecategory ELSE :paymentFor END) ORDER BY prr.NPAYGROUPSEQ , dd1.DDUEDATE , prr.NSORTSEQ , dd1.SDUEHEAD", resultSetMapping = "Mapping.RuleDetailsDto")
@NamedNativeQuery(name = "VMstPayAppRuleSet.getRuleDetailsLoanManual", query = "SELECT IFNULL(prr.NPAYGROUPSEQ, 999) AS npaGroupSeq, IFNULL(prr.NSORTSEQ, 999) AS sortSeq, dd1.SDUECATEGORY AS dueCategory, dd1.SPAYHEAD AS payHead, dd1.NDUEDTLID AS dueDtlId, dd1.NINSTALLMENTNO AS installmentNo, dd1.NDUEAMOUNT as dueAmount, dd1.DDUEDATE as dueDate, mlr.TOTALDUE as totalDue, dd1.sLoanId as loanId,dd1.nTranDtlId as tranDtlId FROM (SELECT IFNULL(rs.sPayHead, CASE WHEN dd.sduecategory = 'INSTALLMENT' THEN 'DEFAULT_INSTALLMENT' WHEN 'FEE' THEN 'DEFAULT_CHARGES' WHEN 'TAX' THEN 'DEFAULT_TAXES' ELSE dd.sduecategory END) AS sPayHead, dd.sduecategory, dd.sduehead, dd.ndueamount, dd.dduedate, dd.ninstallmentno, dd.smastagrid, dd.nduedtlid, dd.sLoanId, dd.nTranDtlId FROM v_mst_PayApp_Rulset rs LEFT JOIN agr_trn_due_dtl dd ON rs.sPayHead = dd.sduehead WHERE dd.smastagrid = :masterAgreement AND rs.sPayRuleID = :ruleId) dd1, (SELECT  rs1.nPayGroupSeq, rs1.nSortSeq, rs1.sPayHead FROM v_mst_PayApp_Rulset rs1 WHERE rs1.sPayRuleID = :ruleId) prr, (SELECT  dd2.dduedate, dd2.sduehead, SUM(dd2.ndueamount) AS Totaldue FROM agr_trn_due_dtl dd2 WHERE dd2.smastagrid = :masterAgreement GROUP BY dd2.dduedate , dd2.sduehead) mlr WHERE dd1.SDUEHEAD = prr.sPayHead  AND dd1.SMASTAGRID = :masterAgreement AND dd1.DDUEDATE = mlr.DDUEDATE AND dd1.SDUEHEAD = mlr.SDUEHEAD AND dd1.sduecategory = :paymentFor AND dd2.sLoanID= :loanId ORDER BY prr.NPAYGROUPSEQ , dd1.DDUEDATE , prr.NSORTSEQ , dd1.SDUEHEAD", resultSetMapping = "Mapping.RuleDetailsDto")
@NamedNativeQuery(name = "VMstPayAppRuleSet.getRuleDetailsChargesManual", query = "SELECT IFNULL(prr.NPAYGROUPSEQ, 999) AS npaGroupSeq, IFNULL(prr.NSORTSEQ, 999) AS sortSeq, dd1.SDUECATEGORY AS dueCategory, dd1.SPAYHEAD AS payHead, dd1.NDUEDTLID AS dueDtlId, dd1.NINSTALLMENTNO AS installmentNo, dd1.NDUEAMOUNT as dueAmount, dd1.DDUEDATE as dueDate, mlr.TOTALDUE as totalDue, dd1.sLoanId as loanId,dd1.nTranDtlId as tranDtlId FROM (SELECT IFNULL(rs.sPayHead, CASE WHEN dd.sduecategory = 'INSTALLMENT' THEN 'DEFAULT_INSTALLMENT' WHEN 'FEE' THEN 'DEFAULT_CHARGES' WHEN 'TAX' THEN 'DEFAULT_TAXES' ELSE dd.sduecategory END) AS sPayHead, dd.sduecategory, dd.sduehead, dd.ndueamount, dd.dduedate, dd.ninstallmentno, dd.smastagrid, dd.nduedtlid, dd.sLoanId, dd.nTranDtlId FROM v_mst_PayApp_Rulset rs LEFT JOIN agr_trn_due_dtl dd ON rs.sPayHead = dd.sduehead WHERE dd.smastagrid = :masterAgreement AND rs.sPayRuleID = :ruleId) dd1, (SELECT  rs1.nPayGroupSeq, rs1.nSortSeq, rs1.sPayHead FROM v_mst_PayApp_Rulset rs1 WHERE rs1.sPayRuleID = :ruleId) prr, (SELECT  dd2.dduedate, dd2.sduehead, SUM(dd2.ndueamount) AS Totaldue FROM agr_trn_due_dtl dd2 WHERE dd2.smastagrid = :masterAgreement GROUP BY dd2.dduedate , dd2.sduehead) mlr WHERE dd1.SDUEHEAD = prr.sPayHead  AND dd1.SMASTAGRID = :masterAgreement AND dd1.DDUEDATE = mlr.DDUEDATE AND dd1.SDUEHEAD = mlr.SDUEHEAD AND dd1.sduecategory = :paymentFor AND dd1.sDueCategory= :tranCategory and  dd1.sDueHead = :tranHead ORDER BY prr.NPAYGROUPSEQ , dd1.DDUEDATE , prr.NSORTSEQ , dd1.SDUEHEAD", resultSetMapping = "Mapping.RuleDetailsDto")

@SqlResultSetMapping(name = "Mapping.RuleDetailsDto", classes = @ConstructorResult(targetClass = RuleDetailsDto.class, columns = {
		@ColumnResult(name = "npaGroupSeq", type = String.class),
		@ColumnResult(name = "sortSeq", type = Integer.class),
		@ColumnResult(name = "dueCategory", type = String.class),
		@ColumnResult(name = "payHead", type = String.class),
		@ColumnResult(name = "dueDtlId", type = Integer.class),
		@ColumnResult(name = "installmentNo", type = Integer.class),		
		@ColumnResult(name = "dueAmount", type = Double.class),
		@ColumnResult(name = "dueDate", type = Date.class),
		@ColumnResult(name = "totalDue", type = Double.class),
		@ColumnResult(name = "loanId", type = String.class),
		@ColumnResult(name = "tranDtlId", type = Integer.class)}))

@Entity
@Immutable
@Data
@AllArgsConstructor
@NoArgsConstructor
//@Table(name = "v_mst_payapp_rulset")
@Subselect("select * from v_mst_payapp_rulset")
public class VMstPayAppRuleSet {

	@Id
	@Column(name = "sPayRuleID", length = 20)
	private String payRuleId;

	@Column(name = "sActive", length = 10)
	private String active;

	@Column(name = "sGroupCode", length = 20)
	private String groupCode;

	@Column(name = "sGroupActive", length = 20)
	private String groupActive;

	@Column(name = "sPayHead", length = 20)
	private String payHead;

	@Column(name = "nPayGroupSeq", length = 10)
	private Integer payGroupSeq;

	@Column(name = "nSortSeq", length = 10)
	private Integer sortSeq;

}
