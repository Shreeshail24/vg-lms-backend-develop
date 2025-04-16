CREATE OR REPLACE VIEW v_agr_interest_accrual_history AS select h.sMastAgrId AS smastagrid,h.sLoanId AS sloanid,h.dTranDate AS dtrandate,h.nTranId AS ntranid,h.sTranType AS strantype,h.sRemark AS sremark,(case when (h.nTranAmount > 0) then h.nTranAmount else 0 end) AS nDebitAmount,(case when (h.nTranAmount < 0) then h.nTranAmount else 0 end) AS nCreditAmount from agr_trn_sys_tran_dtl h where (h.sTranType in ('INTEREST_ACCRUAL','INTEREST_ACCRUAL_REV')) ;
#CREATE OR REPLACE VIEW v_agr_soa AS select h.dTranDate AS dtrandate,h.nTranId AS ntranid,h.sTranType AS strantype,e.sTranEvent AS sTranEvent,h.sRemark AS sremark,h.sMastAgrId AS smastagrid,e.nEventId AS nEventId,e.nTranAmount AS nTranAmount from (agr_trn_tran_header h join agr_trn_event_dtl e) where (h.nTranId = e.nTranId) order by h.nTranId,e.nEventId ;
CREATE OR REPLACE VIEW v_agr_tran_history AS select  DATE_FORMAT(h.dTranDate, '%d-%m-%Y') AS formatDate, h.dTranDate  AS dTranDate,h.nTranId AS ntranid,h.sTranType AS strantype,d.sDtlRemark AS sremark,h.sMastAgrId AS smastagrid,d.nTranDtlId AS nTranDtlId,d.sTranCategory AS sTranCategory,h.sUserId AS sUserId,(case when (d.sTranSide = 'DR') then d.nTranAmount else 0 end) AS nDebitAmount,(case when (d.sTranSide = 'CR') then d.nTranAmount else 0 end) AS nCreditAmount from ((agr_trn_tran_header h join agr_trn_tran_dtl d) join agr_trn_event_dtl e) where ((h.nTranId = e.nTranId) and (e.nEventId = d.nEventId)) ;
CREATE OR REPLACE VIEW v_agr_tran_history_od AS select DATE_FORMAT(h.dTranDate, '%d-%m-%Y') AS formatDate, h.dTranDate  AS dTranDate,d.sDtlRemark AS sremark,(case when (d.sTranSide = 'DR') then d.nTranAmount else 0 end) AS Withdrawal,(case when (d.sTranSide = 'CR') then d.nTranAmount else 0 end) AS Deposit,h.nSanctionedLimit AS nSanctionedLimit,d.nUtilizedLimit AS nUtilizedLimit,d.nAvailableLimit AS nAvailableLimit,h.nTranId AS ntranid,h.sTranType AS strantype,h.sMastAgrId AS smastagrid,d.nTranDtlId AS nTranDtlId from ((agr_trn_tran_header h join agr_trn_tran_dtl d) join agr_trn_event_dtl e) where ((h.nTranId = e.nTranId) and (e.nEventId = d.nEventId)) order by d.nTranDtlId ;
CREATE OR REPLACE VIEW v_agr_tran_loan_data AS select l.sMastAgrId AS smastagrid,l.customerId AS sCustomerID,l.sLoanId AS sloanid,l.nCycleDay AS nCycleDay,l.sLoanAdditionalStatus AS sloanadditionalstatus,l.nUnbilledPrincipal AS nunbilledprincipal,l.nInterestRate AS ninterestrate,l.nCurrentInstallmentNo AS nCurrentInstallmentNo,l.sAssetClass AS sassetclass,l.nDpd AS ndpd,l.nPenalCycleDay AS nPenalCycleDay,p.nPenalInterestRate AS nPenalInterestRate,p.sInterestBasis AS sInterestBasis,p.sPenalInterestBasis AS sPenalInterestBasis,a.nExcessAmount AS nExcessAmount,a.sAgreementStatus AS sStatus,l.sLoanType AS sLoanType from ((agr_master_agreement a join agr_loans l) join agr_product p) where ((a.sMastAgrId = l.sMastAgrId) and (l.sMastAgrId = p.sMastAgrId)) ;
CREATE OR REPLACE VIEW v_lms_agr_repay_schedule AS select agr_repay_schedule.nOpeningPrincipal AS nOpeningPrincipal,agr_repay_schedule.dtInstallmentDate AS dtInstallmentDate,agr_repay_schedule.nInstallmentNo AS nInstallmentNo,agr_repay_schedule.nPrincipalAmount AS nPrincipalAmount,agr_repay_schedule.nInterestAmount AS nInterestAmount,agr_repay_schedule.nInstallmentAmount AS nInstallmentAmount,agr_repay_schedule.nClosingPrincipal AS nClosingPrincipal,agr_repay_schedule.nBPIAmount AS nBPIAmount,agr_repay_schedule.nInterestRate AS nInterestRate,agr_repay_schedule.sEMIType AS sEMIType,agr_repay_schedule.sLoanId AS sLoanId,agr_repay_schedule.sMasterAgrId AS sMasterAgrId,agr_repay_schedule.sInterstBasis AS sInterstBasis from agr_repay_schedule ;
CREATE OR REPLACE VIEW v_mst_PayApp_Rulset AS select pr.sPayRuleID AS sPayRuleID,pr.sActive AS sActive,'Grp' AS sGroupCode,'Y' AS sGroupActive,prd.sPayHead AS sPayHead,prd.nPayGroupSeq AS nPayGroupSeq,prd.nSortSeq AS nSortSeq from (tab_mst_pay_rule pr join tab_mst_pay_rule_dtl prd) where (pr.sPayRuleID = prd.sPayRuleID) ;
CREATE OR REPLACE VIEW v_mst_payrule_manager AS select 'sDecKey1' AS sColumnName,tab_mst_pay_rule_manager.sDecKey1 AS sDecKey from tab_mst_pay_rule_manager union select 'sDecKey2' AS sColumnName,tab_mst_pay_rule_manager.sDecKey2 AS sDecKey from tab_mst_pay_rule_manager union select 'sDecKey3' AS sColumnName,tab_mst_pay_rule_manager.sDecKey3 AS sDecKey from tab_mst_pay_rule_manager union select 'sDecKey4' AS sColumnName,tab_mst_pay_rule_manager.sDecKey4 AS sDecKey from tab_mst_pay_rule_manager union select 'sDecKey5' AS sColumnName,tab_mst_pay_rule_manager.sDecKey5 AS sDecKey from tab_mst_pay_rule_manager ;
CREATE OR REPLACE VIEW v_trn_ins_batch_instruments AS select bi.nBatchInstrumentId AS nbatchinstrumentid,bi.nBatchId AS nBatchID,i.nInstrumentId AS ninstrumentid,i.sCustomerId AS sCustomerID,i.masterAgr AS sMastAgrId,i.dInstrumentDate AS dInstrumentDate,i.sInstrumentNo AS sInstrumentNo,i.nInstrumentAmount AS nInstrumentAmount,i.sIfscCode AS sIFSCCode,i.sAccountNo AS sAccountNo,i.sAccountType AS sAccountType,i.sBankName AS sBankName,i.sBankBranchName AS sBankBranchName,i.sUtrNo AS sUTRNo,i.sUpiVpa AS sUPIVPA,i.sInstrumentStatus AS sInstrumentStatus,i.sBounceReason AS sBounceReason,i.sUmrn AS sUmrn, `mcol`.`sColenderName` as `sColenderName` from (trn_ins_batch_instruments bi join trn_ins_instrument i left join tab_mst_colender mcol on i.sColenderId = mcol.nColenderID) where (bi.nInstrumentId = i.nInstrumentId) ;
CREATE OR REPLACE VIEW v_agr_trn_charges_history AS select h.nTranId AS nTranId,dd.nDueAmount AS nWaivableAmount,dd.nDueDtlId AS nDueDtlID,d.nTranDtlId AS nTranDtlId,h.sMastAgrId AS sMastAgrId,h.sTranType AS sTranType,h.dTranDate AS dTranDate,d.sLoanId AS sLoanId,d.sTranHead AS sTranHead from (((agr_trn_tran_header h join agr_trn_event_dtl e) join agr_trn_tran_dtl d) join agr_trn_due_dtl dd) where ((h.nTranId = e.nTranId) and (e.nEventId = d.nEventId) and (d.nTranDtlId = dd.nTranDtlId) and (h.sTranType = 'CHARGES_BOOKING'));
CREATE or REPLACE VIEW v_mst_provision_setup AS SELECT h.nSeqNo AS nSeqNo,h.sPortfolioCd AS sPortfolioCd,h.sActive AS sActive,d.nSrNo AS nSrNo,d.sAssetClassCd AS sAssetClassCd,d.nDPDFrom AS nDPDFrom,d.nDPDTo AS nDPDTo,d.nSecuredPer AS nSecuredPer,d.nUnSecuredPer AS nUnSecuredPer,d.sNPAFlag AS sNPAFlag FROM (tab_provision_setup_hdr h JOIN tab_provision_setup_dtl d)  WHERE ((h.nSeqNo = d.nSeqNo) AND (h.sActive = 'Y'));

CREATE OR REPLACE VIEW v_mst_gl_config AS
    SELECT
        h.nGLId,
        h.sPortfolioCode,
        h.sServBranch,
        h.sNBFC,
        h.sGLEvent,
        h.sActiveYN AS headerActiveFlag,
        d.nGLDtlId,
        d.sTranCategory,
        d.sTranHead,
        d.sCreditGLHead,
        d.sDebitGLHead,
        d.sActiveYN AS detailActiveFlag
    FROM
        tab_mst_gl_config h,
        tab_mst_gl_config_dtl d
    WHERE
        h.nGLId = d.nGLId;

CREATE OR REPLACE
VIEW v_lms_gl_tran_dtl AS
    SELECT
        m.sServBranch AS sServBranch,
        m.sHomeBranch AS sHomeBranch,
        m.sPortfolioCode AS sPortfolioCode,
        m.sProductCode AS sProductCode,
        m.sCustomerId AS sCustomerId,
        h.nGLGeneratedYN AS nGLGeneratedYN,
        DATE_FORMAT(h.dTranDate, '%d-%m-%Y') AS formatDate,
        h.dTranDate AS dTranDate,
        h.nTranId AS ntranid,
        h.sTranType AS strantype,
        d.sTranHead AS sTranHead,
        e.sTranEvent AS sTranEvent,
        d.sDtlRemark AS sremark,
        h.sMastAgrId AS smastagrid,
        d.nTranDtlId AS nTranDtlId,
        d.sTranCategory AS sTranCategory,
        h.sUserID AS sUserId,
        d.sLoanId AS sLoanId,
        ABS(d.nTranAmount) AS nTranAmount,
        i.nInstrumentAmount AS nInstrumentAmount,
        i.sInstrumentType AS sInstrumentType,
		e.nEventId AS nEventId,
        i.sColenderId AS sNBFC
    FROM
        ((((agr_trn_tran_header h
        JOIN trn_ins_instrument i)
        JOIN agr_trn_event_dtl e)
        JOIN agr_trn_tran_dtl d)
        JOIN agr_master_agreement m)
    WHERE
        ((h.nInstrumentId = i.nInstrumentId)
            AND (h.nTranId = e.nTranId)
            AND (e.nEventId = d.nEventId)
            AND (h.sMastAgrId = m.sMastAgrId));

CREATE OR REPLACE VIEW v_lms_gl_sys_tran_int_accrual AS
    SELECT
        m.sServBranch AS sServBranch,
        m.sHomeBranch AS sHomeBranch,
        m.sPortfolioCode AS sPortfolioCode,
        m.sProductCode AS sProductCode,
        m.sCustomerId AS sCustomerId,
        IFNULL(s.nGLGeneratedYN, 'N') AS nGLGeneratedYN,
        DATE_FORMAT(s.dTranDate, '%d-%m-%Y') AS formatDate,
        s.dTranDate AS dTranDate,
        s.nTranId AS ntranid,
        s.sTranType AS strantype,
        'INTEREST' AS sTranHead,
        s.sTranType AS sTranEvent,
        s.sRemark AS sremark,
        s.sMastAgrId AS smastagrid,
        s.nTranId AS nTranDtlId,
        s.sTranType AS sTranCategory,
        s.sUserID AS sUserId,
        s.sLoanId AS sLoanId,
        ABS(s.nTranAmount) AS nTranAmount,
        s.nTranId AS nEventId,
        c.sColenderCode AS sNBFC
    FROM
        agr_trn_sys_tran_dtl s,
        agr_master_agreement m,
        agr_colender_dtl c
    WHERE
        s.sMastAgrId = m.sMastAgrId
            AND m.sMastAgrId = c.sMastAgrId
            AND s.sTranType in ('INTEREST_ACCRUAL','INTEREST_ACCRUAL_REVERSAL');