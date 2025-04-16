package com.samsoft.lms.core.controllers;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.samsoft.lms.core.dto.HeadWiseDueDto;
import com.samsoft.lms.core.entities.TabMstLookups;
import com.samsoft.lms.core.entities.TabMstSystemParameters;
import com.samsoft.lms.core.services.CommonServices;

@RestController
@RequestMapping(value = "/commonServices")
@CrossOrigin(origins = {"https://lms.4fin.in/","http://localhost:3000", "https://qa-lms.4fin.in", "https://qa-losone.4fin.in", "http://localhost:4200"} ,allowedHeaders = "*")
public class CommonServicesController {

	@Autowired
	private Environment env;

	@Autowired
	private CommonServices commonService;

	@GetMapping(value = "/getBalanceTenor", produces = MediaType.APPLICATION_JSON_VALUE)
	public Integer getBalanceTenor(@RequestParam String loanId, @RequestParam String businessDate) throws Exception {
		String dateFormat = env.getProperty("lms.global.date.format");
		Integer result = 0;
		try {
			
			Date bussDate = new SimpleDateFormat(dateFormat).parse(businessDate);
			result = commonService.getBalanceTenor(loanId, bussDate);
		} catch (Exception e) {
			throw e;
		}
		return result;

	}

	@GetMapping(value = "/getMasterAgrUnbilledPrincipal", produces = MediaType.APPLICATION_JSON_VALUE)
	public Double getMasterAgrUnbilledPrincipal(@RequestParam String masterAgreement) throws Exception {
		Double result = 0.0;
		try {
			result = commonService.getMasterAgrUnbilledPrincipal(masterAgreement);
		} catch (Exception e) {
			throw e;
		}
		return result;

	}

	@GetMapping(value = "/getMasterAgrExcessAmount", produces = MediaType.APPLICATION_JSON_VALUE)
	public Double getMasterAgrExcessAmount(@RequestParam String masterAgreement) throws Exception {
		Double result = 0.0;
		try {
			result = commonService.getMasterAgrExcessAmount(masterAgreement);
		} catch (Exception e) {
			throw e;
		}
		return result;

	}

	@GetMapping(value = "/getLoanUnbilledPrincipal", produces = MediaType.APPLICATION_JSON_VALUE)
	public Double getLoanUnbilledPrincipal(@RequestParam String loanId) throws Exception {
		Double result = 0.0;
		try {
			result = commonService.getLoanUnbilledPrincipal(loanId);
		} catch (Exception e) {
			throw e;
		}
		return result;

	}

	@GetMapping(value = "/getLoanTotalDues", produces = MediaType.APPLICATION_JSON_VALUE)
	public Double getLoanTotalDues(@RequestParam String loanId) throws Exception {
		Double result = 0.0;
		try {
			result = commonService.getLoanTotalDues(loanId);
		} catch (Exception e) {
			throw e;
		}
		return result;

	}

	@GetMapping(value = "/getMasterTotalDues", produces = MediaType.APPLICATION_JSON_VALUE)
	public Double getMasterTotalDues(@RequestParam String masterAgreement) throws Exception {
		Double result = 0.0;
		try {
			result = commonService.getMasterTotalDues(masterAgreement);
		} catch (Exception e) {
			throw e;
		}
		return result;

	}

	@GetMapping(value = "/getAllChargeDuesOfMasterAgr", produces = MediaType.APPLICATION_JSON_VALUE)
	public Double getAllChargeDuesOfMasterAgr(@RequestParam String masterAgreement) throws Exception {
		Double result = 0.0;
		try {
			result = commonService.getAllChargeDuesOfMasterAgr(masterAgreement);
		} catch (Exception e) {
			throw e;
		}
		return result;

	}

	@GetMapping(value = "/getAllPenalChargesOfMasterAgr", produces = MediaType.APPLICATION_JSON_VALUE)
	public Double getAllPenalChargesOfMasterAgr(@RequestParam String masterAgreement) throws Exception {
		Double result = 0.0;
		try {
			result = commonService.getAllPenalChargesOfMasterAgr(masterAgreement);
		} catch (Exception e) {
			throw e;
		}
		return result;

	}

	@GetMapping(value = "/getAllChargesExceptPenalOfMasterAgr", produces = MediaType.APPLICATION_JSON_VALUE)
	public Double getAllChargesExceptPenalOfMasterAgr(@RequestParam String masterAgreement) throws Exception {
		Double result = 0.0;
		try {
			result = commonService.getAllChargesExceptPenalOfMasterAgr(masterAgreement);
		} catch (Exception e) {
			throw e;
		}
		return result;

	}

	@GetMapping(value = "/getHeadwiseDuesForMasterAgr", produces = MediaType.APPLICATION_JSON_VALUE)
	public Double getHeadwiseDuesForMasterAgr(@RequestParam String masterAgreement, @RequestParam String dueHead)
			throws Exception {
		Double result = 0.0;
		try {
			result = commonService.getHeadwiseDuesForMasterAgr(masterAgreement, dueHead);
		} catch (Exception e) {
			throw e;
		}
		return result;

	}

	@GetMapping(value = "/getHeadwiseDuesForLoan", produces = MediaType.APPLICATION_JSON_VALUE)
	public Double getHeadwiseDuesForLoan(@RequestParam String loanId, @RequestParam String dueHead) throws Exception {
		Double result = 0.0;
		try {
			result = commonService.getHeadwiseDuesForLoan(loanId, dueHead);
		} catch (Exception e) {
			throw e;
		}
		return result;

	}

	@GetMapping(value = "/getMasterAgrStatus", produces = MediaType.APPLICATION_JSON_VALUE)
	public String getMasterAgrStatus(@RequestParam String masterAgreement) throws Exception {
		String result;
		try {
			result = commonService.getMasterAgrStatus(masterAgreement);
		} catch (Exception e) {
			throw e;
		}
		return result;

	}

	@GetMapping(value = "/getLoanStatus", produces = MediaType.APPLICATION_JSON_VALUE)
	public String getLoanStatus(@RequestParam String loanId) throws Exception {
		String result;
		try {
			result = commonService.getLoanStatus(loanId);
		} catch (Exception e) {
			throw e;
		}
		return result;

	}

	@GetMapping(value = "/getMasterAgrDpd", produces = MediaType.APPLICATION_JSON_VALUE)
	public Integer getMasterAgrDpd(@RequestParam String masterAgreement) throws Exception {
		Integer result = 0;
		try {
			result = commonService.getMasterAgrDpd(masterAgreement);
		} catch (Exception e) {
			throw e;
		}
		return result;

	}

	@GetMapping(value = "/getMasterAgrAssetClass", produces = MediaType.APPLICATION_JSON_VALUE)
	public String getMasterAgrAssetClass(@RequestParam String masterAgreement) throws Exception {
		String result;
		try {
			result = commonService.getMasterAgrAssetClass(masterAgreement);
		} catch (Exception e) {
			throw e;
		}
		return result;

	}

	@GetMapping(value = "/getMasterAgrNpaStatus", produces = MediaType.APPLICATION_JSON_VALUE)
	public String getMasterAgrNpaStatus(@RequestParam String masterAgreement) throws Exception {
		String result;
		try {
			result = commonService.getMasterAgrNpaStatus(masterAgreement);
		} catch (Exception e) {
			throw e;
		}
		return result;

	}

	@GetMapping(value = "/getLoanAssetClass", produces = MediaType.APPLICATION_JSON_VALUE)
	public String getLoanAssetClass(@RequestParam String loanId) throws Exception {
		String result;
		try {
			result = commonService.getLoanAssetClass(loanId);
		} catch (Exception e) {
			throw e;
		}
		return result;

	}

	@GetMapping(value = "/getLoanDpd", produces = MediaType.APPLICATION_JSON_VALUE)
	public Integer getLoanDpd(@RequestParam String loanId) throws Exception {
		Integer result = 0;
		try {
			result = commonService.getLoanDpd(loanId);
		} catch (Exception e) {
			throw e;
		}
		return result;

	}

	@GetMapping(value = "/getLoanOutstandingAmount", produces = MediaType.APPLICATION_JSON_VALUE)
	public Double getLoanOutstandingAmount(@RequestParam String loanId) throws Exception {
		Double result = 0.0;
		try {
			result = commonService.getLoanOutstandingAmount(loanId);
		} catch (Exception e) {
			throw e;
		}
		return result;

	}

	@GetMapping(value = "/getmasterAgrOutstandingAmount", produces = MediaType.APPLICATION_JSON_VALUE)
	public Double getmasterAgrOutstandingAmount(@RequestParam String masterAgreement) throws Exception {
		Double result = 0.0;
		try {
			result = commonService.getmasterAgrOutstandingAmount(masterAgreement);
		} catch (Exception e) {
			throw e;
		}
		return result;

	}
	
	@GetMapping(value = "/getBusinessDateInDate", produces = MediaType.APPLICATION_JSON_VALUE)
	public Date getBusinessDateInDate() throws Exception {
		try {
			return commonService.getBusinessDateInDate();
		} catch (Exception e) {
			throw e;
		}
	}
	
	@GetMapping(value = "/getBusinessDateInString", produces = MediaType.APPLICATION_JSON_VALUE)
	public String getBusinessDateInString() throws Exception {
		try {
			return commonService.getBusinessDateInString();
		} catch (Exception e) {
			throw e;
		}
	}
	
	@GetMapping(value = "/getLookupValues", produces = MediaType.APPLICATION_JSON_VALUE)
	public List<TabMstLookups> getLookupValues(@RequestParam String lookupType) throws Exception {
		try {
			return commonService.getLookupValues(lookupType);
		} catch (Exception e) {
			throw e;
		}
	}
	
	@GetMapping(value = "/getMasterHeadwiseDueList", produces = MediaType.APPLICATION_JSON_VALUE)
	public List<HeadWiseDueDto> getMasterHeadwiseDueList(@RequestParam String mastAgrId) throws Exception {
		try {
			return commonService.getMasterHeadwiseDueList(mastAgrId);
		} catch (Exception e) {
			throw e;
		}
	}
	
	
	@GetMapping(value = "/getSystemParameterValues", produces = MediaType.APPLICATION_JSON_VALUE)
	public List<TabMstSystemParameters> getSystemParameterValues(@RequestParam String SysParaCode) throws Exception {
		try {
			return commonService.getSystemParameterValues(SysParaCode);
		} catch (Exception e) {
			throw e;
		}
	}
}
