package com.samsoft.lms.core.services;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Service;

import com.samsoft.lms.agreement.entities.AgrColenderDtl;
import com.samsoft.lms.agreement.repositories.AgrColenderDtlRepository;
import com.samsoft.lms.core.exceptions.CoreDataNotFoundException;
import com.samsoft.lms.core.repositories.AgrMasterAgreementRepository;
import com.samsoft.lms.instrument.entities.TrnInsInstrument;
import com.samsoft.lms.instrument.entities.TrnInsInstrumentAlloc;
import com.samsoft.lms.instrument.repositories.TrnInsInstrumentAllocRepository;
import com.samsoft.lms.instrument.repositories.TrnInsInstrumentRepository;
import com.samsoft.lms.request.entities.AgrTrnReqInstrument;
import com.samsoft.lms.request.entities.AgrTrnReqInstrumentAllocDtl;
import com.samsoft.lms.request.repositories.AgrTrnReqInstrumentAllocDtlRepository;
import com.samsoft.lms.request.repositories.AgrTrnReqInstrumentRepository;
import com.samsoft.lms.transaction.services.ReqStatusUpdateService;

@Service
@Slf4j
public class DreApplicationService {

	@Autowired
	private AgrTrnReqInstrumentRepository instReqRepo;
	@Autowired
	private AgrTrnReqInstrumentAllocDtlRepository instReqAlloRepo;
	@Autowired
	private ReqStatusUpdateService reqStatusUpdateService;
	@Autowired
	private Environment env;
	@Autowired
	private CommonServices commonService;
	@Autowired
	private TrnInsInstrumentAllocRepository instAllocRepo;
	@Autowired
	private TrnInsInstrumentRepository instRepo;
	@Autowired
	private AgrMasterAgreementRepository masterRepo;
	@Autowired
	private PaymentAutoApportionmentService autoApportionment;

	@Autowired
	private AgrColenderDtlRepository agrColenderDtlRepository;

	public String dreApplication(Integer reqId, String mastAgrId, Date tranDate, String userId)
			throws CoreDataNotFoundException, Exception {

		String result = "Payment Successful";
		try {
			String dateFormat = env.getProperty("lms.global.date.format");
			SimpleDateFormat sdf = new SimpleDateFormat(dateFormat);

			AgrTrnReqInstrument reqInst = instReqRepo.findByRequestHdrReqId(reqId);

			if (reqInst == null) {
				throw new CoreDataNotFoundException("Source Id not found");
			}

			List<AgrTrnReqInstrumentAllocDtl> instReqAllocList = instReqAlloRepo
					.findByInstrumentInstrumetSrNo(reqInst.getInstrumetSrNo());

			//
			AgrColenderDtl agrColenderDtl = agrColenderDtlRepository.findByMasterAgrMastAgrIdAndInstrumentPresenterYn(mastAgrId, "Y");

			if (reqInst.getInstrumentType().equalsIgnoreCase("CH")) {
				TrnInsInstrument inst = new TrnInsInstrument();
				inst.setDtInstrumentDate(reqInst.getDtInstrumentDate());
				inst.setPayType(reqInst.getPayType());
				inst.setPayMode(reqInst.getPayMode());
				inst.setInstrumentType("CH");
				inst.setAccountNo(reqInst.getAccountNo());
				inst.setAccountType(reqInst.getAccountType());
				inst.setInstrumentNo(reqInst.getInstrumentNo());
				inst.setBankName(reqInst.getBankCode());
				inst.setBankBranchName(reqInst.getBankBranchCode());
				inst.setMicrCode(reqInst.getMicrCode());
				inst.setClearingLocation(reqInst.getClearingLocation());
				inst.setInstrumentAmount(commonService.numberFormatter(reqInst.getInstrumentAmount()));
				inst.setInstrumentStatus("NEW");
				inst.setDepositBankName(reqInst.getDepositBank());
				inst.setUtrNo(reqInst.getUtrNo());
				inst.setIfscCode(reqInst.getIfscCode());
				inst.setDtReceipt(reqInst.getDtReceipt());
				inst.setDepositRefNo(reqInst.getDepositRefNo());
				inst.setCardHolderName(reqInst.getCardHolderName());
				inst.setIssuingBank(reqInst.getIssuingBank());
				inst.setUpiVpa(reqInst.getUpiVpa());
				inst.setCollectedBy(reqInst.getCollectedBy());
				inst.setCollectionAgency(reqInst.getCollectionAgency());
				inst.setProvisionalReceipt(reqInst.getProvisionalReceipt());
				inst.setProcLoc(reqInst.getProcLoc());
				inst.setCardType(reqInst.getCardType());
				inst.setNclStatus("N");
				inst.setUserId(reqInst.getUserId());
				inst.setMasterAgr(mastAgrId);
				inst.setDepositBank("ICICI_HL_AC");
				inst.setDepositBankBranch("PUNE");
				inst.setDepositBankIfsc("ICICI000001");
				inst.setCustomerId(masterRepo.findByMastAgrId(mastAgrId).getCustomerId());
				inst.setColenderId(agrColenderDtl.getColenderCode());
				inst.setTdsAmount(reqInst.getTdsAmount());

				List<TrnInsInstrumentAlloc> instAllocList = new ArrayList<TrnInsInstrumentAlloc>();
				for (AgrTrnReqInstrumentAllocDtl dreAllocation : instReqAllocList) {
					if (dreAllocation.getAmout() > 0) {

						TrnInsInstrumentAlloc instAl = new TrnInsInstrumentAlloc();
						instAl.setLoanId(dreAllocation.getLoanId());
						instAl.setTranCategory(dreAllocation.getTranCategory());
						instAl.setTranHead(dreAllocation.getTranHead());
						instAl.setApportionAmount(commonService.numberFormatter(dreAllocation.getAmout()));
						instAl.setUserId(userId);
						instAl.setInstrument(inst);
						instAllocList.add(instAl);
					}

				}

				instAllocRepo.saveAll(instAllocList);
				instRepo.save(inst);
			} else if (reqInst.getInstrumentType().equalsIgnoreCase("CA")) {
				TrnInsInstrument insInstrument = new TrnInsInstrument();
				insInstrument.setMasterAgr(mastAgrId);
				insInstrument.setDtInstrumentDate(reqInst.getDtInstrumentDate());
				insInstrument.setPayType(reqInst.getPayType());
				insInstrument.setPayMode(reqInst.getPayMode());
				insInstrument.setInstrumentType(reqInst.getInstrumentType());
				insInstrument.setInstrumentAmount(commonService.numberFormatter(reqInst.getInstrumentAmount()));
				insInstrument.setInstrumentStatus("CLR");
				insInstrument.setDtReceipt(reqInst.getDtReceipt());
				insInstrument.setDepositRefNo(reqInst.getDepositRefNo());
				insInstrument.setNclStatus("N");
				insInstrument.setUserId(reqInst.getUserId());
				insInstrument.setColenderId(agrColenderDtl.getColenderCode());
				insInstrument.setTdsAmount(reqInst.getTdsAmount());

				List<TrnInsInstrumentAlloc> listInstAlloc = new ArrayList<TrnInsInstrumentAlloc>();

				for (AgrTrnReqInstrumentAllocDtl dreAllocation : instReqAllocList) {
					if (dreAllocation.getAmout() > 0) {
						TrnInsInstrumentAlloc inst = new TrnInsInstrumentAlloc();
						inst.setLoanId(dreAllocation.getLoanId());
						inst.setTranCategory(dreAllocation.getTranCategory());
						inst.setTranHead(dreAllocation.getTranHead());
						inst.setApportionAmount(commonService.numberFormatter(dreAllocation.getAmout()));
						inst.setUserId(userId);
						inst.setInstrument(insInstrument);
						listInstAlloc.add(inst);
					}
				}
				List<TrnInsInstrumentAlloc> savedInstAlloc = instAllocRepo.saveAll(listInstAlloc);
				TrnInsInstrument saveInstrument = instRepo.save(insInstrument);

				autoApportionment.agreementAutoApportionmentPaymentApplication(mastAgrId,
						saveInstrument.getInstrumentId(), reqInst.getDtReceipt());

			} else {

				TrnInsInstrument insInstrument = new TrnInsInstrument();
				insInstrument.setMasterAgr(mastAgrId);
				insInstrument.setDtInstrumentDate(reqInst.getDtInstrumentDate());
				insInstrument.setPayType(reqInst.getPayType());
				insInstrument.setPayMode(reqInst.getPayMode());
				insInstrument.setInstrumentType(reqInst.getInstrumentType());
				insInstrument.setInstrumentAmount(commonService.numberFormatter(reqInst.getInstrumentAmount()));
				insInstrument.setInstrumentStatus("CLR");
				insInstrument.setDtReceipt(reqInst.getDtReceipt());
				insInstrument.setDepositRefNo(reqInst.getDepositRefNo());
				insInstrument.setNclStatus("N");
				insInstrument.setBankName(reqInst.getBankCode());
				insInstrument.setBankBranchName(reqInst.getBankBranchCode());
				insInstrument.setIfscCode(reqInst.getIfscCode());
				insInstrument.setUtrNo(reqInst.getUtrNo());
				insInstrument.setUserId(reqInst.getUserId());
				insInstrument.setColenderId(agrColenderDtl.getColenderCode());
				insInstrument.setTdsAmount(reqInst.getTdsAmount());
        
				List<TrnInsInstrumentAlloc> listInstAlloc = new ArrayList<TrnInsInstrumentAlloc>();

				for (AgrTrnReqInstrumentAllocDtl dreAllocation : instReqAllocList) {
					if (dreAllocation.getAmout() > 0) {
						TrnInsInstrumentAlloc inst = new TrnInsInstrumentAlloc();
						inst.setLoanId(dreAllocation.getLoanId());
						inst.setTranCategory(dreAllocation.getTranCategory());
						inst.setTranHead(dreAllocation.getTranHead());
						inst.setApportionAmount(commonService.numberFormatter(dreAllocation.getAmout()));
						inst.setInstrument(insInstrument);
						inst.setUserId(userId);
						listInstAlloc.add(inst);
					}
				}
				List<TrnInsInstrumentAlloc> savedInstAlloc = instAllocRepo.saveAll(listInstAlloc);

				TrnInsInstrument saveInstrument = instRepo.save(insInstrument);
				
				autoApportionment.agreementAutoApportionmentPaymentApplication(mastAgrId,
						saveInstrument.getInstrumentId(), tranDate);

			}

			reqStatusUpdateService.updateRequestStatus(reqId, "APR");
		} catch (Exception e) {
			e.printStackTrace();
			log.error("Error: " + e);
			throw e;
		}
		return result;

	}
}
