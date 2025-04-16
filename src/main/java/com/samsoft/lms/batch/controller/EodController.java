package com.samsoft.lms.batch.controller;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.samsoft.lms.batch.dto.EodStatusMainDto;
import com.samsoft.lms.batch.exceptions.EodExceptions;
import com.samsoft.lms.batch.services.EODServices;
import com.samsoft.lms.core.entities.TabOrganization;
import com.samsoft.lms.core.repositories.TabOrganizationRepository;
import com.samsoft.lms.transaction.exceptions.TransactionDataNotFoundException;
import com.samsoft.lms.transaction.exceptions.TransactionInternalServerError;

@RestController
@RequestMapping(value = "/eod")
@CrossOrigin(origins = { "https://lms.4fin.in/", "http://localhost:3000", "https://qa-lms.4fin.in",
		"https://qa-losone.4fin.in" }, allowedHeaders = "*")
public class EodController {

	@Autowired
	private EODServices eodService;
	@Autowired
	private Environment env;
	@Autowired
	private TabOrganizationRepository orgRepo;
	
	

	@PostMapping(value = "/customerEod/{customerId}", produces = MediaType.APPLICATION_JSON_VALUE)
	public String customerEod(@PathVariable("customerId") String customerId, Date businessDate) throws Exception {
		try {
			eodService.customerEod(customerId, businessDate);
		} catch (Exception e) {
			throw new Exception(e.getMessage());
		}
		return "";
	}

	@PostMapping(value = "/agreementClosureApi/{customerId}", produces = MediaType.APPLICATION_JSON_VALUE)
	public String agreementClosureApi(@PathVariable("customerId") String customerId) throws Exception {
		String result = "success";
		try {
			eodService.agreementClosureApi(customerId);
		} catch (Exception e) {
			throw new Exception(e.getMessage());
		}
		return "";
	}

	@PostMapping(value = "/mainEod")
	public ResponseEntity<String> mainEod(@RequestParam("orgId") String orgId,
			@RequestParam("businessDate") String businessDate) throws Exception {

		String dateFormat = env.getProperty("lms.global.date.format");
		String result = eodService.beforeMainEod(orgId, businessDate);

		if (!result.equals("success")) {
			throw new EodExceptions(result);
		}
		try {
			result = eodService.mainEod(orgId, businessDate);
		} catch (Exception e) {
			throw new Exception(e.getMessage());
		}
		return ResponseEntity.ok(result);
	}

	@PostMapping(value = "/mainEodFrontEnd")
	public ResponseEntity<String> mainEodFrontEnd() throws Exception {
		String dateFormat = env.getProperty("lms.global.date.format");
		SimpleDateFormat sdf = new SimpleDateFormat(dateFormat);
		TabOrganization tabOrganization = orgRepo.findAll().get(0);
		String result = eodService.beforeMainEod(tabOrganization.getOrgId(),
				sdf.format(tabOrganization.getDtBusiness()));

		if (!result.equals("success")) {
			throw new EodExceptions(result);
		}
		try {
			result = eodService.mainEod(tabOrganization.getOrgId(), sdf.format(tabOrganization.getDtBusiness()));
		} catch (Exception e) {
			e.printStackTrace();
			throw new Exception(e.getMessage());
		}
		return ResponseEntity.ok(result);
	}
	
	@GetMapping(value = "/getbusinessDate",produces = MediaType.APPLICATION_JSON_VALUE)
	public List<TabOrganization> getBusinessDate(@RequestParam String orgId){
		try {
			List<TabOrganization> result=eodService.getBusinessDate(orgId);
			return result;
			
		} catch (TransactionDataNotFoundException e) {
			throw new TransactionDataNotFoundException(e.getMessage());
		} catch (Exception e) {
			throw new TransactionInternalServerError(e.getMessage());
		}

		
	    
	}

	@PostMapping(value = "/penalCompute", produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<Boolean> penalCompute(@RequestParam("backupId") Integer backupId,
			@RequestParam("backupDate") String backupDate) {
		String dateFormat = env.getProperty("lms.global.date.format");
		// LocalDate backupDate1 = LocalDate.parse(backupDate,
		// DateTimeFormatter.ofPattern(dateFormat));
		Date backupDate1 = null;
		try {
			backupDate1 = new SimpleDateFormat(dateFormat).parse(backupDate);
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		eodService.penalCompute(backupId, backupDate1);
		return ResponseEntity.ok(true);
	}

	@PostMapping(value = "/penalReversal", produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<Boolean> penalReversal(@RequestParam("backupId") Integer backupId,
			@RequestParam("backupDate") String backupDate) {
		String dateFormat = env.getProperty("lms.global.date.format");
		// LocalDate backupDate1 = LocalDate.parse(backupDate,
		// DateTimeFormatter.ofPattern(dateFormat));
		Date backupDate1 = null;
		try {
			backupDate1 = new SimpleDateFormat(dateFormat).parse(backupDate);
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		Boolean result = eodService.penalReversal(backupId, backupDate1);
		if (!result) {
			throw new EodExceptions("EOd Exception for ");
		}
		return ResponseEntity.ok(true);
	}

	@PostMapping(value = "/penalBooking", produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<Boolean> penalBooking(@RequestParam("backupId") Integer backupId,
			@RequestParam("backupDate") String backupDate) {
		String dateFormat = env.getProperty("lms.global.date.format");
		// LocalDate backupDate1 = LocalDate.parse(backupDate,
		// DateTimeFormatter.ofPattern(dateFormat));
		Date backupDate1 = null;
		try {
			backupDate1 = new SimpleDateFormat(dateFormat).parse(backupDate);
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		eodService.penalBooking(backupId, backupDate1);
		return ResponseEntity.ok(true);
	}

	@PostMapping(value = "/interestAccrual", produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<Boolean> interestAccrual(@RequestParam("backupId") Integer backupId,
			@RequestParam("backupDate") String backupDate) {
		String dateFormat = env.getProperty("lms.global.date.format");
		// LocalDate backupDate1 = LocalDate.parse(backupDate,
		// DateTimeFormatter.ofPattern(dateFormat));
		Date backupDate1 = null;
		try {
			backupDate1 = new SimpleDateFormat(dateFormat).parse(backupDate);
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		eodService.interestAccrual(backupId, backupDate1);
		return ResponseEntity.ok(true);
	}

	@PostMapping(value = "/interestReversalAccrual", produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<Boolean> interestReversalAccrual(@RequestParam("backupId") Integer backupId,
			@RequestParam("backupDate") String backupDate) {
		String dateFormat = env.getProperty("lms.global.date.format");
		// LocalDate backupDate1 = LocalDate.parse(backupDate,
		// DateTimeFormatter.ofPattern(dateFormat));
		Date backupDate1 = null;
		try {
			backupDate1 = new SimpleDateFormat(dateFormat).parse(backupDate);
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		eodService.interestAccrualReversal(backupId, backupDate1);
		return ResponseEntity.ok(true);
	}

	@PostMapping(value = "/installmentBilling", produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<Boolean> installmentBilling(@RequestParam("backupId") Integer backupId,
			@RequestParam("backupDate") String backupDate) throws ParseException {
		String dateFormat = env.getProperty("lms.global.date.format");
		// LocalDate backupDate1 = LocalDate.parse(backupDate,
		// DateTimeFormatter.ofPattern(dateFormat));
		Date backupDate1 = null;
		try {
			backupDate1 = new SimpleDateFormat(dateFormat).parse(backupDate);
			eodService.installmentBilling(backupId, backupDate1);
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		return ResponseEntity.ok(true);
	}

	@PostMapping(value = "/settlementWriteoffApi", produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<Boolean> settlementWriteoffApi(@RequestParam("customerId") String customerId,
			@RequestParam("businessDate") String businessDate) throws ParseException {
		String dateFormat = env.getProperty("lms.global.date.format");
		// LocalDate backupDate1 = LocalDate.parse(backupDate,
		// DateTimeFormatter.ofPattern(dateFormat));
		Date backupDate1 = null;
		Boolean result = true;
		try {
			backupDate1 = new SimpleDateFormat(dateFormat).parse(businessDate);
			result = eodService.settlementWriteoffApi(customerId, backupDate1);
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		return ResponseEntity.ok(result);
	}

	@GetMapping(value = "/getEodStatus", produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<EodStatusMainDto> getEodStatus(@RequestParam("businessDate") String businessDate,
			@RequestParam("pageNo") Integer pageNo, @RequestParam("pageSize") Integer pageSize,
			@RequestParam(name = "processName", required = false) String processName) throws ParseException {
		String dateFormat = env.getProperty("lms.global.date.format");
		EodStatusMainDto eodStatus = new EodStatusMainDto();
		Date backupDate1 = null;
		try {
			backupDate1 = new SimpleDateFormat(dateFormat).parse(businessDate);
			eodStatus = eodService.getEodStatus(backupDate1, processName, pageNo, pageSize);
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		return ResponseEntity.ok(eodStatus);
	}

	@Scheduled(cron = "0 0 0 * * ?")
	public void startSchedule() {
		System.out.println("Inside Schedule");
	}

	@Scheduled(cron = "0 0 0 * * *")
	public void startMainEod() throws Exception {
		Date businessDate = orgRepo.findAll().get(0).getDtBusiness();

		String dateFormat = env.getProperty("lms.global.date.format");
		SimpleDateFormat sdf = new SimpleDateFormat(dateFormat);
		String result = eodService.beforeMainEod("1", sdf.format(businessDate));

		if (!result.equals("success")) {
			throw new EodExceptions(result);
		}
		try {
			result = eodService.mainEod("1", sdf.format(businessDate));
		} catch (Exception e) {
			throw new Exception(e.getMessage());
		}

	}

}
