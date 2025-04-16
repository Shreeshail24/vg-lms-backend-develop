package com.samsoft.lms.core.services;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.samsoft.lms.agreement.entities.AgrDisbursement;
import com.samsoft.lms.agreement.repositories.AgrDisbursementRepository;
import com.samsoft.lms.core.dto.Soa;
import com.samsoft.lms.core.dto.SoaTransactionDetails;
import com.samsoft.lms.core.entities.AgrCustLimitSetup;
import com.samsoft.lms.core.entities.AgrLoans;
import com.samsoft.lms.core.entities.AgrMasterAgreement;
import com.samsoft.lms.core.entities.AgrProduct;
import com.samsoft.lms.core.entities.AgrTrnTranDetail;
import com.samsoft.lms.core.entities.VAgrSoa;
import com.samsoft.lms.core.entities.VAgrTranHistory;
import com.samsoft.lms.core.repositories.AgrCustLimitSetupRepository;
import com.samsoft.lms.core.repositories.AgrLoansRepository;
import com.samsoft.lms.core.repositories.AgrMasterAgreementRepository;
import com.samsoft.lms.core.repositories.AgrProductRepository;
import com.samsoft.lms.core.repositories.AgrTrnTranDetailsRepository;
import com.samsoft.lms.core.repositories.TabMstOrgBranchRepository;
import com.samsoft.lms.core.repositories.VAgrSoaRepository;
import com.samsoft.lms.core.repositories.VArgTranHistoryRepository;

@Service
public class SoaService {

	@Autowired
	private AgrMasterAgreementRepository masterRepo;

	@Autowired
	private AgrLoansRepository loanRepo;

	@Autowired
	private AgrProductRepository prodRepo;

	@Autowired
	private AgrCustLimitSetupRepository limitRepo;

	@Autowired
	private Environment env;

	@Autowired
	private AgrDisbursementRepository disbRepo;

	@Autowired
	private TabMstOrgBranchRepository orgRepo;

	@Autowired
	private VAgrSoaRepository soaRepo;

	@Autowired
	private VArgTranHistoryRepository tranHistRepo;
	
	@Autowired
	private AgrTrnTranDetailsRepository tranDtlRepo;

	@Autowired
	private CommonServices commonService;
	
	@Transactional(rollbackFor = { RuntimeException.class, Error.class, Exception.class })
	public Soa generateSoa(String mastAgrId) {
		String dateFormat = env.getProperty("lms.global.date.format");
		SimpleDateFormat sdf = new SimpleDateFormat(dateFormat);

		Soa soaMain = new Soa();

		AgrMasterAgreement master = masterRepo.findByMastAgrId(mastAgrId);
		AgrProduct product = prodRepo.findByMasterAgreementMastAgrId(mastAgrId);
		AgrLoans loan = loanRepo.findByMasterAgreementMastAgrId(mastAgrId).get(0);
		AgrCustLimitSetup limit = limitRepo.findByMasterAgreementMastAgrId(mastAgrId);
		AgrDisbursement disb = disbRepo.findFirstByMastAgrMastAgrIdOrderByDisbIdAsc(mastAgrId);

		BeanUtils.copyProperties(master, soaMain);
		BeanUtils.copyProperties(product, soaMain);
		BeanUtils.copyProperties(loan, soaMain);
		BeanUtils.copyProperties(limit, soaMain);

		soaMain.setDtDisbDate(sdf.format(disb.getDtDisbDate()));
		soaMain.setDtLimitExpired(sdf.format(limit.getDtLimitExpired()));
		soaMain.setDropMode(commonService.getDescription("DROPLINE_MODE", product.getDropMode()));
		soaMain.setAgreementStatus(commonService.getDescription("AGREEMENT_STATUS", master.getAgreementStatus()));
		
		
		List<VAgrSoa> soaList = soaRepo.findByMastAgrIdOrderByTranIdAsc(mastAgrId);
		List<SoaTransactionDetails> tranDtlList=new ArrayList<SoaTransactionDetails>();
		for (VAgrSoa soa : soaList) {
			if (soa.getTranType().equalsIgnoreCase("DISBURSEMENT")) {
				List<VAgrTranHistory> tranHistoryList = tranHistRepo.findByMastAgrIdAndTranId(mastAgrId,
						soa.getTranId());
				for (VAgrTranHistory tranHist : tranHistoryList) {
					SoaTransactionDetails soaTran = new SoaTransactionDetails();
					soaTran.setDtTran(sdf.format(tranHist.getDtTranDate()));
					soaTran.setCreditAmount(tranHist.getCreditAmount());
					soaTran.setDebitAmount(tranHist.getDebitAmount());
					soaTran.setDescription(tranHist.getRemark());
					soaTran.setTransactionType(tranHist.getTranType());

					tranDtlList.add(soaTran);
				}
			} else if (soa.getTranType().equalsIgnoreCase("INSTALLMENT_BILLING")) {

				SoaTransactionDetails soaTran = new SoaTransactionDetails();
				soaTran.setCreditAmount(0d);
				soaTran.setDebitAmount(soa.getTranAmount());
				soaTran.setDescription("Installment Due for " + soa.getDtTrandate());

				tranDtlList.add(soaTran);
			} else if (soa.getTranType().equalsIgnoreCase("RECEIPT")) {

				SoaTransactionDetails soaTran = new SoaTransactionDetails();
				soaTran.setCreditAmount(soa.getTranAmount());
				soaTran.setDebitAmount(0d);
				soaTran.setDescription("Payment Received against receipt #" + soa.getTranId());

				tranDtlList.add(soaTran);
			} else if (soa.getTranType().equalsIgnoreCase("RECEIPT_REVERSE")) {

				SoaTransactionDetails soaTran = new SoaTransactionDetails();
				soaTran.setCreditAmount(0d);
				soaTran.setDebitAmount(soa.getTranAmount());
				soaTran.setDescription("Payment Reversed against receipt #" + soa.getTranId());

				tranDtlList.add(soaTran);
			} else if (soa.getTranType().equalsIgnoreCase("CHARGES_BOOKING")) {

				SoaTransactionDetails soaTran = new SoaTransactionDetails();
				soaTran.setCreditAmount(0d);
				soaTran.setDebitAmount(soa.getTranAmount());
				soaTran.setDescription("Charged Booked");

				tranDtlList.add(soaTran);
			}
			
			List<AgrTrnTranDetail> tranList = tranDtlRepo.findByMasterAgrMastAgrIdAndTranCategoryOrderByTranDtlIdAsc(mastAgrId,"TAX");
			for(AgrTrnTranDetail tran : tranList) {
				SoaTransactionDetails soaTran = new SoaTransactionDetails();
				soaTran.setDtTran(sdf.format(tran.getDtDueDate()));
				soaTran.setCreditAmount(0d);
				soaTran.setDebitAmount(tran.getTranAmount());
				soaTran.setDescription(tran.getTranHead());
				soaTran.setTransactionType(tran.getTranCategory());

				tranDtlList.add(soaTran);
			}
		}
		soaMain.setTranDtl(tranDtlList);
		soaMain.setTotalRows(tranDtlList.size());
		return soaMain;
	}
}
