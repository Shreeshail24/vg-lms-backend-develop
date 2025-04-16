package com.samsoft.lms.transaction.controller;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.samsoft.lms.core.exceptions.CoreDataNotFoundException;
import com.samsoft.lms.core.services.CommonServices;
import com.samsoft.lms.core.services.DreApplicationService;
import com.samsoft.lms.transaction.dto.ChargeBookedHistDto;
import com.samsoft.lms.transaction.dto.ChargesBookingDto;
import com.samsoft.lms.transaction.dto.CreditNoteApplicationDto;
import com.samsoft.lms.transaction.dto.DebitNoteApplicationDto;
import com.samsoft.lms.transaction.dto.ForclosureReceivableListDto;
import com.samsoft.lms.transaction.dto.GlGenerateDto;
import com.samsoft.lms.transaction.dto.RecallMainGetDto;
import com.samsoft.lms.transaction.dto.RefundApplicationDto;
import com.samsoft.lms.transaction.dto.SettlementMainGetDto;
import com.samsoft.lms.transaction.dto.WriteoffMainGetDto;
import com.samsoft.lms.transaction.exceptions.TransactionDataNotFoundException;
import com.samsoft.lms.transaction.exceptions.TransactionInternalServerError;
import com.samsoft.lms.transaction.services.*;

@RestController
@RequestMapping(value = "/transaction")
//@CrossOrigin(origins = "*", maxAge = 3600)
@CrossOrigin(origins = { "https://lms.4fin.in/", "http://localhost:3000", "https://qa-lms.4fin.in",
		"https://qa-losone.4fin.in" }, allowedHeaders = "*")
public class TransactionalController {

	@Autowired
	private TransactionService transactionService;

	@Autowired
	private DebitNoteService debitNoteService;

	@Autowired
	private CreditNoteService creditNoteService;

	@Autowired
	private RefundService refundService;

	@Autowired
	private ChargesWaiverApplicationService chargesAppService;

	@Autowired
	private ForclosureApplicationService forclosureAppService;

	@Autowired
	private PartPrepaymentApplicationService partPrepaymentAppService;

	@Autowired
	private WriteoffApplicationService writeoffAppService;

	@Autowired
	private RecallApplicationService recallAppService;

	@Autowired
	private SettlementApplicationService settlementAppService;

	@Autowired
	private Environment env;

	@Autowired
	private ChargesWaiverApprovalService waiverAppService;

	@Autowired
	private RestructureApplicationService restructureAppService;

	@Autowired
	private DreApplicationService dreAppService;

	@Autowired
	private ReqStatusUpdateService reqStatusUpdateService;

	@Autowired
	private GlGenerateService glService;

	@Autowired
	private CommonServices commonServices;

	@PostMapping(value = "/chargesBooking", produces = MediaType.APPLICATION_JSON_VALUE, consumes = MediaType.APPLICATION_JSON_VALUE)
	public String applyCharges(@RequestBody ChargesBookingDto bookingDto) {
		String result = "success";
		try {
			result = transactionService.chargeBookingApply(bookingDto);
			return result;
		} catch (TransactionDataNotFoundException e) {
			throw new TransactionDataNotFoundException(e.getMessage());
		} catch (Exception e) {
			throw new TransactionInternalServerError(e.getMessage());
		}
	}

	@PostMapping(value = "/debitNoteApplication", produces = MediaType.APPLICATION_JSON_VALUE, consumes = MediaType.APPLICATION_JSON_VALUE)
	public String debitNoteApplication(@RequestBody DebitNoteApplicationDto debitDto) {
		String result = "success";
		try {
			result = debitNoteService.debitNoteApplication(debitDto);
			return result;
		} catch (TransactionDataNotFoundException e) {
			throw new TransactionDataNotFoundException(e.getMessage());
		} catch (Exception e) {
			throw new TransactionInternalServerError(e.getMessage());
		}
	}

	@PostMapping(value = "/creditNoteApplication", produces = MediaType.APPLICATION_JSON_VALUE, consumes = MediaType.APPLICATION_JSON_VALUE)
	public String creditNoteApplication(@RequestBody CreditNoteApplicationDto creditDto) {
		String result = "success";
		try {
			result = creditNoteService.creditNoteApplication(creditDto);
			return result;
		} catch (TransactionDataNotFoundException e) {
			throw new TransactionDataNotFoundException(e.getMessage());
		} catch (Exception e) {
			throw new TransactionInternalServerError(e.getMessage());
		}
	}

	@PostMapping(value = "/refundApplication", produces = MediaType.APPLICATION_JSON_VALUE, consumes = MediaType.APPLICATION_JSON_VALUE)
	public String RefundApplication(@RequestBody RefundApplicationDto refundDto) {
		String result = "success";
		try {
			result = refundService.refundApplication(refundDto);
			return result;

		} catch (TransactionDataNotFoundException e) {
			throw new TransactionDataNotFoundException(e.getMessage());
		} catch (Exception e) {
			throw new TransactionInternalServerError(e.getMessage());
		}
	}

	/*
	 * @PostMapping(value = "/chargesWaiverApplication", produces =
	 * MediaType.APPLICATION_JSON_VALUE, consumes =
	 * MediaType.APPLICATION_JSON_VALUE) public String
	 * chargesWaiverApplication(@RequestBody ChargesWaiverApplicationDto
	 * chargesWaiver) { String result = "success"; try { result =
	 * chargesAppService.chargesWaiverApplication(chargesWaiver); return result;
	 * 
	 * } catch (TransactionDataNotFoundException e) { throw new
	 * TransactionDataNotFoundException(e.getMessage()); } catch (Exception e) {
	 * throw new TransactionInternalServerError(e.getMessage()); } }
	 */

	@PostMapping(value = "/chargesWaiverApproval", produces = MediaType.APPLICATION_JSON_VALUE, consumes = MediaType.APPLICATION_JSON_VALUE)
	public String chargesWaiverApproval(@RequestParam Integer reqId, @RequestParam String mastAgrId,
			@RequestParam String userId, @RequestParam String tranDate) throws Exception {
		String result = "success";
		try {
			String dateFormat = env.getProperty("lms.global.date.format");
			SimpleDateFormat sdf = new SimpleDateFormat(dateFormat);
			Date requestedDate = sdf.parse(tranDate);
			result = waiverAppService.chargesWaiverApproval(reqId, mastAgrId, userId, requestedDate);
			return result;

		} catch (CoreDataNotFoundException e) {
			throw e;
		} catch (Exception e) {
			throw e;
		}
	}

	@PostMapping(value = "/forclosureApplication", produces = MediaType.APPLICATION_JSON_VALUE, consumes = MediaType.APPLICATION_JSON_VALUE)
	public String forclosureApplication(@RequestParam String mastAgrId, @RequestParam String tranDate,
			@RequestParam Integer reqId) {
		String result = "success";
		try {
			String dateFormat = env.getProperty("lms.global.date.format");
			SimpleDateFormat sdf = new SimpleDateFormat(dateFormat);
			Date requestedDate = sdf.parse(tranDate);
			result = forclosureAppService.forclosureApplication(mastAgrId, requestedDate, reqId);
			return result;

		} catch (TransactionDataNotFoundException e) {
			throw new TransactionDataNotFoundException(e.getMessage());
		} catch (Exception e) {
			throw new TransactionInternalServerError(e.getMessage());
		}
	}

	@PostMapping(value = "/partPrepaymentApplication", produces = MediaType.APPLICATION_JSON_VALUE, consumes = MediaType.APPLICATION_JSON_VALUE)
	public String partPrepaymentApplication(@RequestParam String mastAgrId, @RequestParam String tranDate,
			@RequestParam Integer reqId) {
		String result = "success";
		try {
			String dateFormat = env.getProperty("lms.global.date.format");
			SimpleDateFormat sdf = new SimpleDateFormat(dateFormat);
			Date requestedDate = sdf.parse(tranDate);
			result = partPrepaymentAppService.partPrepaymentApplicationAgrAutoApportion(mastAgrId, requestedDate,
					reqId);
			return result;

		} catch (TransactionDataNotFoundException e) {
			throw new TransactionDataNotFoundException(e.getMessage());
		} catch (Exception e) {
			throw new TransactionInternalServerError(e.getMessage());
		}
	}

	@GetMapping(value = "/getForclosureReceivable", produces = MediaType.APPLICATION_JSON_VALUE)
	public ForclosureReceivableListDto getForclosureReceivable(@RequestParam String mastAgrId) {
		ForclosureReceivableListDto result = new ForclosureReceivableListDto();
		try {

			result = forclosureAppService.getForclosureReceivable(mastAgrId);
			return result;

		} catch (TransactionDataNotFoundException e) {
			throw new TransactionDataNotFoundException(e.getMessage());
		} catch (Exception e) {
			throw new TransactionInternalServerError(e.getMessage());
		}
	}

	@GetMapping(value = "/getAgreementChargedBookedList", produces = MediaType.APPLICATION_JSON_VALUE)
	public List<ChargeBookedHistDto> getAgreementChargedBookedList(@RequestParam String mastAgrId) {
		List<ChargeBookedHistDto> result = new ArrayList<ChargeBookedHistDto>();
		try {

			result = transactionService.getAgreementChargedBookedList(mastAgrId);
			return result;

		} catch (TransactionDataNotFoundException e) {
			throw new TransactionDataNotFoundException(e.getMessage());
		} catch (Exception e) {
			throw new TransactionInternalServerError(e.getMessage());
		}
	}

	@PostMapping(value = "/writeoffApplication", produces = MediaType.APPLICATION_JSON_VALUE, consumes = MediaType.APPLICATION_JSON_VALUE)
	public String writeoffApplication(@RequestParam String mastAgrId, @RequestParam Integer reqId,
			@RequestParam String tranDate) {
		String result = "success";
		try {
			String dateFormat = env.getProperty("lms.global.date.format");
			SimpleDateFormat sdf = new SimpleDateFormat(dateFormat);
			Date requestedDate = sdf.parse(tranDate);
			result = writeoffAppService.writeoffApplication(mastAgrId, reqId, requestedDate);
			return result;

		} catch (Exception e) {
			throw new TransactionInternalServerError(e.getMessage());
		}
	}

	@PostMapping(value = "/recallApplication", produces = MediaType.APPLICATION_JSON_VALUE, consumes = MediaType.APPLICATION_JSON_VALUE)
	public String recallApplication(@RequestParam String mastAgrId, @RequestParam Integer reqId,
			@RequestParam String tranDate) {
		String result = "success";
		try {
			String dateFormat = env.getProperty("lms.global.date.format");
			SimpleDateFormat sdf = new SimpleDateFormat(dateFormat);
			Date requestedDate = sdf.parse(tranDate);
			result = recallAppService.recallApplication(mastAgrId, reqId, requestedDate);
			return result;

		} catch (Exception e) {
			throw new TransactionInternalServerError(e.getMessage());
		}
	}

	@PostMapping(value = "/settlementApplication", produces = MediaType.APPLICATION_JSON_VALUE, consumes = MediaType.APPLICATION_JSON_VALUE)
	public String settlementApplication(@RequestParam String mastAgrId, @RequestParam Integer reqId,
			@RequestParam String tranDate) {
		String result = "success";
		try {
			String dateFormat = env.getProperty("lms.global.date.format");
			SimpleDateFormat sdf = new SimpleDateFormat(dateFormat);
			Date requestedDate = sdf.parse(tranDate);
			result = settlementAppService.settlementApplication(mastAgrId, reqId, requestedDate);
			return result;

		} catch (Exception e) {
			throw new TransactionInternalServerError(e.getMessage());
		}
	}

	@GetMapping(value = "/getRecallTotalReceivables", produces = MediaType.APPLICATION_JSON_VALUE)
	public RecallMainGetDto getRecallTotalReceivables(@RequestParam String mastAgrId) {
		RecallMainGetDto result = new RecallMainGetDto();
		try {

			result = recallAppService.getRecallTotalReceivables(mastAgrId);
			return result;

		} catch (TransactionDataNotFoundException e) {
			throw new TransactionDataNotFoundException(e.getMessage());
		} catch (Exception e) {
			throw new TransactionInternalServerError(e.getMessage());
		}
	}

	@GetMapping(value = "/getWriteoffTotalReceivables", produces = MediaType.APPLICATION_JSON_VALUE)
	public WriteoffMainGetDto getWriteoffTotalReceivables(String mastAgrId) throws Exception {
		WriteoffMainGetDto result = new WriteoffMainGetDto();
		try {

			result = writeoffAppService.getWriteoffTotalReceivables(mastAgrId);
			return result;

		} catch (TransactionDataNotFoundException e) {
			throw new TransactionDataNotFoundException(e.getMessage());
		} catch (Exception e) {
			throw new TransactionInternalServerError(e.getMessage());
		}
	}

	@GetMapping(value = "/getSettlementTotalReceivables", produces = MediaType.APPLICATION_JSON_VALUE)
	public SettlementMainGetDto getSettlementTotalReceivables(String mastAgrId) throws Exception {
		SettlementMainGetDto result = new SettlementMainGetDto();
		try {

			result = settlementAppService.getSettlementTotalReceivables(mastAgrId);
			return result;

		} catch (TransactionDataNotFoundException e) {
			throw new TransactionDataNotFoundException(e.getMessage());
		} catch (Exception e) {
			throw new TransactionInternalServerError(e.getMessage());
		}
	}

	@PostMapping(value = "/restructureApplication", produces = MediaType.APPLICATION_JSON_VALUE, consumes = MediaType.APPLICATION_JSON_VALUE)
	public String restructureApplication(@RequestParam String mastAgrId, @RequestParam Integer reqId,
			@RequestParam String tranDate) {
		String result = "success";
		try {
			String dateFormat = env.getProperty("lms.global.date.format");
			SimpleDateFormat sdf = new SimpleDateFormat(dateFormat);
			Date requestedDate = sdf.parse(tranDate);
			result = restructureAppService.restructureApplication(mastAgrId, reqId, tranDate);
			return result;

		} catch (Exception e) {
			throw new TransactionInternalServerError(e.getMessage());
		}
	}

	@PostMapping(value = "/dreApplication", produces = MediaType.APPLICATION_JSON_VALUE, consumes = MediaType.APPLICATION_JSON_VALUE)
	public String dreApplication(@RequestParam Integer reqId, @RequestParam String mastAgrId,
			@RequestParam String tranDate, @RequestParam String userId) throws Exception {
		String result = "success";
		try {
			String dateFormat = env.getProperty("lms.global.date.format");
			SimpleDateFormat sdf = new SimpleDateFormat(dateFormat);
			Date requestedDate = sdf.parse(tranDate);
			result = dreAppService.dreApplication(reqId, mastAgrId, requestedDate, userId);
			return result;

		} catch (CoreDataNotFoundException e) {
			throw new CoreDataNotFoundException(e.getMessage());
		} catch (Exception e) {
			throw e;
		}
	}

	@PostMapping(value = "/glGeneration", produces = MediaType.APPLICATION_JSON_VALUE, consumes = MediaType.APPLICATION_JSON_VALUE)
	public String glGenerate(@RequestBody GlGenerateDto glDto) throws Exception {
		String result = "success";
		try {
			String dateFormat = env.getProperty("lms.global.date.format");
			SimpleDateFormat sdf = new SimpleDateFormat(dateFormat);
			result = glService.glGenerate(glDto);
			return result;

		} catch (CoreDataNotFoundException e) {
			throw new CoreDataNotFoundException(e.getMessage());
		} catch (Exception e) {
			throw e;
		}
	}

	@PostMapping(value = "/rejectRequest", produces = MediaType.APPLICATION_JSON_VALUE, consumes = MediaType.APPLICATION_JSON_VALUE)
	public String rejectRequest(@RequestParam Integer reqId) throws Exception {
		try {
			String res = reqStatusUpdateService.updateRequestStatus(reqId, "REJ");
			if (res != null) {
				return res;
			} else {
				return null;
			}

		} catch (Exception e) {
			e.printStackTrace();
			throw e;
		}
	}

	@GetMapping(value = "/getInterestAccruedTillDateByTranType", produces = MediaType.APPLICATION_JSON_VALUE)
	public Double getInterestAccruedTillDateByTranType(@RequestParam String mastAgrId, @RequestParam String tranType)
			throws Exception {

		try {

			return commonServices.getAddInterestAccrued(mastAgrId, tranType);

		} catch (TransactionDataNotFoundException e) {
			throw new TransactionDataNotFoundException(e.getMessage());
		} catch (Exception e) {
			throw new TransactionInternalServerError(e.getMessage());
		}
	}

}
