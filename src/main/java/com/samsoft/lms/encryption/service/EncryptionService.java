package com.samsoft.lms.encryption.service;

import java.util.List;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.samsoft.lms.common.utils.EncryptionUtil;
import com.samsoft.lms.customer.entities.AgrCustAddress;
import com.samsoft.lms.customer.entities.AgrCustomer;
import com.samsoft.lms.customer.repositories.AgrCustAddressRepository;
import com.samsoft.lms.customer.repositories.AgrCustomerRepository;

import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
public class EncryptionService {
	@Autowired
	private AgrCustAddressRepository addrRepo;

	@Autowired
	private AgrCustomerRepository custRepo;

	@Autowired
	private EncryptionUtil encryptionUtil;

	@Transactional
	public Boolean encryptName() throws Exception {
		try {
			List<AgrCustomer> custList = custRepo.findAll();
			for (AgrCustomer cust : custList) {
				log.info("before encrypted name ===>" + cust);
				String firstName = cust.getFirstName();
				String middleName = cust.getMiddleName();
				String lastName = cust.getLastName();
				if (firstName != null) {
					String encryptedFirstName = encryptionUtil.decodeBase64(firstName);
					cust.setFirstName(encryptedFirstName);
				}
				if (middleName != null) {
					String encryptedMiddleName = encryptionUtil.decodeBase64(middleName);
					cust.setMiddleName(encryptedMiddleName);
				}
				if (lastName != null) {
					String encryptedLastName = encryptionUtil.decodeBase64(lastName);
					cust.setLastName(encryptedLastName);
				}
				cust = custRepo.save(cust);
				log.info("saved encrypted name ===>" + cust);
			}
			return Boolean.TRUE;
		} catch (Exception e) {
			log.info("Exception Occured while encryptName====>" + e);
			throw e;
		}
	}

	@Transactional
	public Boolean encryptMobileNo() throws Exception {
		try {
			List<AgrCustomer> custList = custRepo.findAll();
			for (AgrCustomer cust : custList) {
				String mbno = cust.getMobile();
				String altMobileNo = cust.getAlternateMobile();
				String altLandline = cust.getAlternateLandline();
				String landLine = cust.getLandLine();
				if (mbno != null) {
					String encryptedMbNo = encryptionUtil.decrypt(mbno);
					cust.setMobile(encryptedMbNo);
				}
				if (altLandline != null) {
					String encryptedAltLandLine = encryptionUtil.decrypt(altLandline);
					cust.setAlternateLandline(encryptedAltLandLine);
				}
				if (altMobileNo != null) {
					String encryptedAltMobileNo = encryptionUtil.decrypt(altMobileNo);
					cust.setAlternateMobile(encryptedAltMobileNo);
				}
				if (landLine != null) {
					String encryptedLandLine = encryptionUtil.decrypt(landLine);
					cust.setLandLine(encryptedLandLine);
				}
				cust = custRepo.save(cust);
				log.info("saved encrypted mobile no" + cust);
			}
			return Boolean.TRUE;
		} catch (Exception e) {
			log.info("Excepion occured while encryptMobileNo==>" + e);
			throw e;
		}
	}

	@Transactional
	public Boolean encryptGst() throws Exception {
		try {
			List<AgrCustomer> custList = custRepo.findAll();
			for (AgrCustomer cust : custList) {
				if (cust.getGstin() != null) {
					String encryptedGst = encryptionUtil.decrypt(cust.getGstin());
					cust.setGstin(encryptedGst);
					cust = custRepo.save(cust);
					log.info("saved encrypted GST" + cust);
				}
			}
			return Boolean.TRUE;
		} catch (Exception e) {
			log.info("Exception occured in encryptGst====>" + e);
			throw e;
		}
	}
	
	@Transactional
	public Boolean encryptPan() throws Exception {
		try {
			List<AgrCustomer> custList = custRepo.findAll();
			for (AgrCustomer cust : custList) {
				if (cust.getPan() != null) {
					String encryptedPan = encryptionUtil.decrypt(cust.getPan());
					cust.setPan(encryptedPan);
					custRepo.save(cust);
					log.info("saved encrypted Pan" + cust);
				}
			}
			return Boolean.TRUE;
		} catch (Exception e) {
			log.info("Exception occured in encryptPan====>" + e);
			throw e;
		}
	}
	
	@Transactional
	public Boolean encryptAadhaar() throws Exception {
		try {
			List<AgrCustomer> custList = custRepo.findAll();
			for (AgrCustomer cust : custList) {
				if (cust.getAadharNo() != null) {
					String encryptedAadhaar = encryptionUtil.decrypt(cust.getAadharNo());
					cust.setAadharNo(encryptedAadhaar);
					custRepo.save(cust);
					log.info("saved encrypted AAdhaar" + cust);
				}
			}
			return Boolean.TRUE;
		} catch (Exception e) {
			log.info("Exception occured in encryptAAdhaar====>" + e);
			throw e;
		}
	}
	
	@Transactional
	public Boolean encryptAddress() throws Exception {
		try {
			List<AgrCustAddress> custAddrList = addrRepo.findAll();
			for (AgrCustAddress custAddr : custAddrList) {
				log.info("before Cust Address ====>"+custAddr);
				if (custAddr.getAddressLine1() != null) {
					String encryptedAddrLine1 = encryptionUtil.decrypt(custAddr.getAddressLine1());
					custAddr.setAddressLine1(encryptedAddrLine1);
//					custRepo.save(cust);
				}
				if (custAddr.getAddressLine2() != null) {
					String encryptedAddrLine2 = encryptionUtil.decrypt(custAddr.getAddressLine2());
					custAddr.setAddressLine2(encryptedAddrLine2);
//					custRepo.save(cust);
				}
				if (custAddr.getAddressLine3() != null) {
					String encryptedAddrLine3 = encryptionUtil.decrypt(custAddr.getAddressLine3());
					custAddr.setAddressLine1(encryptedAddrLine3);
//					custRepo.save(cust);
				}
				custAddr= addrRepo.save(custAddr);
				log.info("Cust Address ====>"+custAddr);
			}
			return Boolean.TRUE;
		} catch (Exception e) {
			log.info("Exception occured in encryptAddress====>" + e);
			throw e;
		}
	}
	
	@Transactional
	public Boolean encryptEmail() throws Exception {
		try {
			List<AgrCustomer> custList = custRepo.findAll();
			for (AgrCustomer cust : custList) {
				if (cust.getEmailId() != null) {
					String encryptedEmail = encryptionUtil.decrypt(cust.getEmailId());
					cust.setEmailId(encryptedEmail);
					cust = custRepo.save(cust);
					log.info("saved encrypted email" + cust);
				}
			}
			return Boolean.TRUE;
		} catch (Exception e) {
			log.info("Exception occured in encryptMail====>" + e);
			throw e;
		}
	}
}
