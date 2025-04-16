package com.samsoft.lms.request.controller;

import java.util.ArrayList;
import java.util.List;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import com.samsoft.lms.approvalSettings.services.ApprovalService;
import com.samsoft.lms.core.dto.FourFinResponse;

import com.samsoft.lms.core.exceptions.CoreBadRequestException;
import com.samsoft.lms.core.exceptions.CoreDataNotFoundException;
import com.samsoft.lms.document.dto.request.UploadExternalDocumentRequest;
import com.samsoft.lms.las.util.PaginationUtil;
import com.samsoft.lms.newux.dto.response.DebitNoteReqDetailsResponseDto;
import com.samsoft.lms.newux.dto.response.RecieptReqDetailsResponseDto;
import com.samsoft.lms.newux.exceptions.RecieptReqDetailsInternalServerError;
import com.samsoft.lms.newux.exceptions.RecieptReqDetailsNotFoundException;
import com.samsoft.lms.request.dto.ChargesReqBookingDto;
import com.samsoft.lms.request.dto.ChargesWaiverDto;
import com.samsoft.lms.request.dto.ChargesWaiverGridDto;
import com.samsoft.lms.request.dto.CreditNoteReqDto;
import com.samsoft.lms.request.dto.DebitNoteReqDto;
import com.samsoft.lms.request.dto.DreReqCashDto;
import com.samsoft.lms.request.dto.DreReqChequeDto;
import com.samsoft.lms.request.dto.DreRequestOnlineDto;
import com.samsoft.lms.request.dto.ForclosureCashRequestDto;
import com.samsoft.lms.request.dto.ForclosureChequeRequestDto;
import com.samsoft.lms.request.dto.ForclosureOnlineRequestDto;
import com.samsoft.lms.request.dto.PartPrepaymentDto;
import com.samsoft.lms.request.dto.PendingForApprovalDto;
import com.samsoft.lms.request.dto.RecallReqDto;
import com.samsoft.lms.request.dto.RefundReqDto;
import com.samsoft.lms.request.dto.RestructureReceivableListDto;
import com.samsoft.lms.request.dto.RestructureReqDtoMain;
import com.samsoft.lms.request.dto.SettlementReceivableListDto;
import com.samsoft.lms.request.dto.SettlementReqDto;
import com.samsoft.lms.request.dto.WriteoffReceivableListDto;
import com.samsoft.lms.request.dto.WriteoffReqDto;
import com.samsoft.lms.request.repositories.AgrTrnRequestHdrRepository;
import com.samsoft.lms.request.services.ChargesBookingRequestService;
import com.samsoft.lms.request.services.ChargesWaiverRequest;
import com.samsoft.lms.request.services.CreditNoteReqService;
import com.samsoft.lms.request.services.DebitNoteReqService;
import com.samsoft.lms.request.services.DreRequestService;
import com.samsoft.lms.request.services.ForclosureRequestService;
import com.samsoft.lms.request.services.PartPrepaymentReqService;
import com.samsoft.lms.request.services.RecallReqService;
import com.samsoft.lms.request.services.RefundReqService;
import com.samsoft.lms.request.services.RestructureRequestService;
import com.samsoft.lms.request.services.SettlementRequestService;
import com.samsoft.lms.request.services.WriteoffRequestService;
import com.samsoft.lms.transaction.dto.ForclosureReceivableListDto;
import com.samsoft.lms.transaction.dto.GstListDto;
import com.samsoft.lms.transaction.exceptions.TransactionDataNotFoundException;
import com.samsoft.lms.transaction.exceptions.TransactionInternalServerError;
import com.samsoft.lms.transaction.services.GstDetailsService;
import com.samsoft.lms.transaction.services.RefundService;
import com.samsoft.lms.transaction.services.TransactionService;

@RestController
@RequestMapping(value = "/request")
@CrossOrigin(origins = { "https://lms.4fin.in/", "http://localhost:3000", "https://qa-lms.4fin.in",
		"https://qa-losone.4fin.in" }, allowedHeaders = "*")
@Validated
public class TransactionRequestController {

	@Autowired
	private ChargesBookingRequestService chargesReqService;

	@Autowired
	private DreRequestService dreReqService;
	@Autowired
	private CreditNoteReqService creditNoteReqService;
	@Autowired
	private DebitNoteReqService debitNoteReqService;
	@Autowired
	private RefundReqService refundReqService;
	@Autowired
	private ChargesWaiverRequest waiverReqService;
	@Autowired
	private ForclosureRequestService forclosureService;
	@Autowired
	private PartPrepaymentReqService partPrepayService;
	@Autowired
	private AgrTrnRequestHdrRepository hdrRepo;
	@Autowired
	private TransactionService transactionService;
	@Autowired
	private GstDetailsService gstService;
	@Autowired
	private RestructureRequestService restructureService;
	@Autowired
	private SettlementRequestService settlementService;
	@Autowired
	private WriteoffRequestService writeoffService;
	@Autowired
	private RecallReqService recallReqService;
	@Autowired
	private ApprovalService approvalService;

	@PostMapping(value = "/chargesReqBooking", produces = MediaType.APPLICATION_JSON_VALUE, consumes = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<Boolean> chargesRequestBooking(@Valid @RequestBody ChargesReqBookingDto chargesReqDto)
			throws Exception {
		// AgrTrnRequestHdr requestHeader = new AgrTrnRequestHdr();
		Boolean requestHeader = true;
		try {
			requestHeader = chargesReqService.chargesReqBooking(chargesReqDto);

		} catch (TransactionDataNotFoundException e) {
			throw e;
		} catch (Exception e) {
			throw e;
		}
		return ResponseEntity.ok(requestHeader);

	}

	@PostMapping(value = "/dreReqCheque", produces = MediaType.APPLICATION_JSON_VALUE, consumes = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<String> dreReqCheque(@RequestBody DreReqChequeDto dreReqChequeDto) throws Exception { // , @ModelAttribute UploadExternalDocumentRequest uploadDocument
		// AgrTrnRequestHdr requestHeader = new AgrTrnRequestHdr();
		String requestHeader = "";
		try {
			requestHeader = dreReqService.dreReqCheque(dreReqChequeDto);

		} catch (Exception e) {
			throw new Exception(e.getMessage());
		}

		return ResponseEntity.ok(requestHeader);

	}

	@PostMapping(value = "/dreReqCash", produces = MediaType.APPLICATION_JSON_VALUE, consumes = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<String> dreReqCash(@RequestBody DreReqCashDto dreReqCashDto) throws Exception { // , @ModelAttribute UploadExternalDocumentRequest uploadDocument
		// AgrTrnRequestHdr requestHeader = new AgrTrnRequestHdr();
		String requestHeader = "";
		try {
			requestHeader = dreReqService.dreRequestCash(dreReqCashDto);

		} catch (Exception e) {
			throw new Exception(e.getMessage());
		}

		return ResponseEntity.ok(requestHeader);

	}

	@PostMapping(value = "/dreReqOnline", produces = MediaType.APPLICATION_JSON_VALUE, consumes = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<String> dreRequestOnline(@RequestBody DreRequestOnlineDto dreReqOnlineDto) throws Exception { // , @ModelAttribute UploadExternalDocumentRequest uploadDocument
		// AgrTrnRequestHdr requestHeader = new AgrTrnRequestHdr();
		String requestHeader = "";
		try {
			requestHeader = dreReqService.dreRequestOnline(dreReqOnlineDto);

		} catch (Exception e) {
			throw new Exception(e.getMessage());
		}

		return ResponseEntity.ok(requestHeader);

	}

	@PostMapping(value = "/creditNoteReq", produces = MediaType.APPLICATION_JSON_VALUE, consumes = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<Boolean> creditNoteRequest(@RequestBody CreditNoteReqDto creditNoteDto) throws Exception {
		// AgrTrnRequestHdr requestHeader = new AgrTrnRequestHdr();
		Boolean result = true;
		try {
			result = creditNoteReqService.creditNoteRequest(creditNoteDto);

		} catch (Exception e) {
			throw new Exception(e.getMessage());
		}

		return ResponseEntity.ok(result);

	}

	@PostMapping(value = "/debitNoteReq", produces = MediaType.APPLICATION_JSON_VALUE, consumes = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<Boolean> debitNoteRequest(@RequestBody DebitNoteReqDto debitNoteDto) throws Exception {
		// AgrTrnRequestHdr requestHeader = new AgrTrnRequestHdr();
		Boolean result = true;
		try {
			result = debitNoteReqService.debitNoteRequest(debitNoteDto);

		} catch (Exception e) {
			throw new Exception(e.getMessage());
		}

		return ResponseEntity.ok(result);

	}

	@PostMapping(value = "/refundReq", produces = MediaType.APPLICATION_JSON_VALUE, consumes = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<Boolean> refundRequest(@RequestBody RefundReqDto refundDto) throws Exception {
		// AgrTrnRequestHdr requestHeader = new AgrTrnRequestHdr();
		Boolean result = true;
		try {
			result = refundReqService.refundRequest(refundDto);

		} catch (Exception e) {
			throw new Exception(e.getMessage());
		}

		return ResponseEntity.ok(result);

	}

	@PostMapping(value = "/chargesWaiverReq", produces = MediaType.APPLICATION_JSON_VALUE, consumes = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<String> chargesWaiverRequest(@RequestBody ChargesWaiverDto chargesWaiverdto)
			throws Exception {
		// AgrTrnRequestHdr requestHeader = new AgrTrnRequestHdr();
		String result = "success";
		try {
			result = waiverReqService.chargesWaiverRequest(chargesWaiverdto);

		} catch (Exception e) {
			throw new Exception(e.getMessage());
		}

		return ResponseEntity.ok(result);

	}

	@PostMapping(value = "/forclosureOnlineRequest", produces = MediaType.APPLICATION_JSON_VALUE, consumes = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<String> forclosureOnlineRequest(@RequestBody ForclosureOnlineRequestDto forclosureDto)
			throws Exception {
		// AgrTrnRequestHdr requestHeader = new AgrTrnRequestHdr();
		String result = "success";
		try {
			result = forclosureService.forclosureOnlineRequest(forclosureDto);

		} catch (CoreBadRequestException e) {
			throw e;
		} catch (Exception e) {
			throw new Exception(e.getMessage());
		}

		return ResponseEntity.ok(result);

	}

	@PostMapping(value = "/forclosureChequeRequest", produces = MediaType.APPLICATION_JSON_VALUE, consumes = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<String> forclosureOnlineRequest(@RequestBody ForclosureChequeRequestDto forclosureDto)
			throws Exception {
		// AgrTrnRequestHdr requestHeader = new AgrTrnRequestHdr();
		String result = "success";
		try {
			result = forclosureService.forclosureChequeRequest(forclosureDto);

		} catch (CoreBadRequestException e) {
			throw e;
		} catch (Exception e) {
			throw new Exception(e.getMessage());
		}

		return ResponseEntity.ok(result);

	}

	@PostMapping(value = "/forclosureCashRequest", produces = MediaType.APPLICATION_JSON_VALUE, consumes = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<String> forclosureOnlineRequest(@RequestBody ForclosureCashRequestDto forclosureDto)
			throws Exception {
		// AgrTrnRequestHdr requestHeader = new AgrTrnRequestHdr();
		String result = "success";
		try {
			result = forclosureService.forclosureCashRequest(forclosureDto);

		} catch (CoreBadRequestException e) {
			throw e;
		} catch (Exception e) {
			throw new Exception(e.getMessage());
		}

		return ResponseEntity.ok(result);

	}

	@PostMapping(value = "/partPrepaymentOnlineRequest", produces = MediaType.APPLICATION_JSON_VALUE, consumes = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<String> partPrepaymentOnlineRequest(@RequestBody PartPrepaymentDto partPrepaymentDto)
			throws Exception {
		String result = "success";
		try {
			result = partPrepayService.partPrepaymentOnlineRequest(partPrepaymentDto);
		} catch (Exception e) {
			throw new Exception(e.getMessage());
		}
		return ResponseEntity.ok(result);

	}

	@GetMapping(value = "/getPrepayReceivableList", produces = MediaType.APPLICATION_JSON_VALUE)
	public ForclosureReceivableListDto getPrepayReceivableList(@RequestParam String mastAgrId,
			@RequestParam Double prepaymentAmount) {
		ForclosureReceivableListDto result = new ForclosureReceivableListDto();
		try {

			result = partPrepayService.getPrepayReceivableList(mastAgrId, prepaymentAmount);
			return result;

		} catch (TransactionDataNotFoundException e) {
			throw new TransactionDataNotFoundException(e.getMessage());
		} catch (Exception e) {
			throw new TransactionInternalServerError(e.getMessage());
		}
	}

	@GetMapping(value = "/getRestructureReceivableList", produces = MediaType.APPLICATION_JSON_VALUE)
	public List<RestructureReceivableListDto> getRestructureReceivableList(@RequestParam String mastAgrId) {
		List<RestructureReceivableListDto> result = new ArrayList<>();
		try {

			result = restructureService.getRestructureReceivableList(mastAgrId);
			return result;

		} catch (TransactionDataNotFoundException e) {
			throw new TransactionDataNotFoundException(e.getMessage());
		} catch (Exception e) {
			throw new TransactionInternalServerError(e.getMessage());
		}
	}

	@GetMapping(value = "/getSettlementReceivableList", produces = MediaType.APPLICATION_JSON_VALUE)
	public List<SettlementReceivableListDto> getSettlementReceivableList(@RequestParam String mastAgrId) {
		List<SettlementReceivableListDto> result = new ArrayList<>();
		try {

			result = settlementService.getSettlementReceivableList(mastAgrId);
			return result;

		} catch (TransactionDataNotFoundException e) {
			throw new TransactionDataNotFoundException(e.getMessage());
		} catch (Exception e) {
			throw new TransactionInternalServerError(e.getMessage());
		}
	}
	
	@GetMapping(value = "/getWriteoffReceivableList", produces = MediaType.APPLICATION_JSON_VALUE)
	public List<WriteoffReceivableListDto> getWriteoffReceivableList(@RequestParam String mastAgrId) {
		List<WriteoffReceivableListDto> result = new ArrayList<>();
		try {

			result = writeoffService.getWriteoffReceivableList(mastAgrId);
			return result;

		} catch (TransactionDataNotFoundException e) {
			throw new TransactionDataNotFoundException(e.getMessage());
		} catch (Exception e) {
			throw new TransactionInternalServerError(e.getMessage());
		}
	}

	@GetMapping(value = "/getGstList", produces = MediaType.APPLICATION_JSON_VALUE)
	public List<GstListDto> getGstList(@RequestParam String mastAgrId, @RequestParam String chargeHead,
			@RequestParam Double chargeAmount) {
		try {

			return gstService.getGstList(mastAgrId, chargeHead, chargeAmount);

		} catch (CoreDataNotFoundException e) {
			throw e;
		} catch (TransactionDataNotFoundException e) {
			throw new TransactionDataNotFoundException(e.getMessage());
		} catch (Exception e) {
			throw new TransactionInternalServerError(e.getMessage());
		}
	}

	@PostMapping(value = "/restructureRequest", produces = MediaType.APPLICATION_JSON_VALUE, consumes = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<String> restructureRequest(@RequestBody RestructureReqDtoMain restructureDto)
			throws Exception {
		String result = "success";
		try {
			result = restructureService.restructureRequest(restructureDto);
			return ResponseEntity.ok(result);

		} catch (Exception e) {
			throw new Exception(e.getMessage());
		}

	}

	@PostMapping(value = "/settlementRequest", produces = MediaType.APPLICATION_JSON_VALUE, consumes = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<String> settlementRequest(@RequestBody SettlementReqDto settlementDto) throws Exception {
		String result = "success";
		try {
			result = settlementService.settlementRequest(settlementDto);
			return ResponseEntity.ok(result);

		} catch (Exception e) {
			throw new Exception(e.getMessage());
		}

	}

	@PostMapping(value = "/writeoffRequest", produces = MediaType.APPLICATION_JSON_VALUE, consumes = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<String> writeoffRequest(@RequestBody WriteoffReqDto writeoffDto) throws Exception {
		String result = "success";
		try {
			result = writeoffService.writeoffRequest(writeoffDto);
			return ResponseEntity.ok(result);

		} catch (Exception e) {
			throw new Exception(e.getMessage());
		}

	}

	@PostMapping(value = "/recallReq", produces = MediaType.APPLICATION_JSON_VALUE, consumes = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<String> recallReq(@RequestBody RecallReqDto recallDto) throws Exception {
		String result = "success";
		try {
			result = recallReqService.recallReq(recallDto);
			return ResponseEntity.ok(result);

		} catch (Exception e) {
			throw new Exception(e.getMessage());
		}

	}

	public List<String> validateBean(BindingResult bindingResult) {
		List<String> errors = new ArrayList<>();
		for (Object object : bindingResult.getAllErrors()) {
			if (object instanceof FieldError) {
				FieldError fieldError = (FieldError) object;
				errors.add(fieldError.getDefaultMessage());

			}
		}
		return errors;
		// System.out.println(errors);
	}
	
	@GetMapping(value = "/getReceiptReqList", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<List<DreReqCashDto>> receiptReqList( @RequestParam (required = false) List<String> status,
    		@RequestParam String activityCode , 
    		@RequestParam(required = false, defaultValue = "0") Integer reqNo,
    		@RequestParam(required = false, defaultValue = "null") String date,
    		@RequestParam(required = false, defaultValue = "0") Integer pageNo,
            @RequestParam(required = false, defaultValue = "10") Integer pageSize,
            @RequestParam(required = false, defaultValue = "reqId") String sortBy,
            @RequestParam(required = false, defaultValue = "asc") String sortDir) throws Exception {
        FourFinResponse<List<DreReqCashDto>> response = new FourFinResponse<>();
        try {
        	// Create pageable object
            Sort.Direction direction = sortDir.equalsIgnoreCase("desc") ? 
                Sort.Direction.DESC : Sort.Direction.ASC;
            Pageable pageable = PageRequest.of(pageNo, pageSize, Sort.by(direction, sortBy));
        	
            Page<DreReqCashDto> pageResult = dreReqService.getReceiptReqList(status,activityCode, reqNo, date, pageable);
        	
            
        	 HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), pageResult);
             return ResponseEntity.ok().headers(headers).body(pageResult.getContent());
        	
           
        } catch (RecieptReqDetailsNotFoundException e) {

            response.setHttpStatus(HttpStatus.NOT_FOUND);
            response.setResponseCode(HttpStatus.NOT_FOUND.value());
            response.setResponseMessage(HttpStatus.NOT_FOUND.getReasonPhrase());
            response.setResponseMessage(e.getMessage());
            throw new RecieptReqDetailsNotFoundException(e.getMessage());
        } catch (Exception e) {

            response.setHttpStatus(HttpStatus.INTERNAL_SERVER_ERROR);
            response.setResponseCode(HttpStatus.INTERNAL_SERVER_ERROR.value());
            response.setResponseMessage(HttpStatus.INTERNAL_SERVER_ERROR.getReasonPhrase());
            response.setResponseMessage(e.getMessage());
            throw new RecieptReqDetailsInternalServerError(e.getMessage());
        }
    }
 
	
	 @GetMapping(value = "/getRefundReqList", produces = MediaType.APPLICATION_JSON_VALUE)
	    public ResponseEntity<List<RefundReqDto>> refundReqList( @RequestParam (required = false) List<String> status,
	    		@RequestParam String activityCode , 
	    		@RequestParam(required = false, defaultValue = "0") Integer reqNo,
	    		@RequestParam(required = false, defaultValue = "null") String date,
	    		@RequestParam(required = false, defaultValue = "0") Integer pageNo,
	            @RequestParam(required = false, defaultValue = "10") Integer pageSize,
	            @RequestParam(required = false, defaultValue = "reqId") String sortBy,
	            @RequestParam(required = false, defaultValue = "asc") String sortDir) throws Exception {
	        FourFinResponse<List<ChargesReqBookingDto>> response = new FourFinResponse<>();
	        try {
	        	// Create pageable object
	            Sort.Direction direction = sortDir.equalsIgnoreCase("desc") ? 
	                Sort.Direction.DESC : Sort.Direction.ASC;
	            Pageable pageable = PageRequest.of(pageNo, pageSize, Sort.by(direction, sortBy));
	        	
	            Page<RefundReqDto> pageResult = refundReqService.getRefundReqList(status,activityCode, reqNo, date, pageable);
	        	
	            
	        	 HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), pageResult);
	             return ResponseEntity.ok().headers(headers).body(pageResult.getContent());
	        	
	           
	        } catch (RecieptReqDetailsNotFoundException e) {

	            response.setHttpStatus(HttpStatus.NOT_FOUND);
	            response.setResponseCode(HttpStatus.NOT_FOUND.value());
	            response.setResponseMessage(HttpStatus.NOT_FOUND.getReasonPhrase());
	            response.setResponseMessage(e.getMessage());
	            throw new RecieptReqDetailsNotFoundException(e.getMessage());
	        } catch (Exception e) {

	            response.setHttpStatus(HttpStatus.INTERNAL_SERVER_ERROR);
	            response.setResponseCode(HttpStatus.INTERNAL_SERVER_ERROR.value());
	            response.setResponseMessage(HttpStatus.INTERNAL_SERVER_ERROR.getReasonPhrase());
	            response.setResponseMessage(e.getMessage());
	            throw new RecieptReqDetailsInternalServerError(e.getMessage());
	        }
	    }
	 
	
//------ API to get BookingCharges requests list by status, date and reqId
//------ There are four parameters statusList, date, reqId, activityCode(this is required) with pagination
	 @GetMapping(value = "/getBookingChargesReqList", produces = MediaType.APPLICATION_JSON_VALUE)
	    public ResponseEntity<List<ChargesReqBookingDto>> getBookingChargesReqList( @RequestParam (required = false) List<String> status,
	    		@RequestParam String activityCode , 
	    		@RequestParam(required = false, defaultValue = "0") Integer reqNo,
	    		@RequestParam (required = false, defaultValue = "null") String date,
	    		@RequestParam(required = false, defaultValue = "0") Integer pageNo,
	            @RequestParam(required = false, defaultValue = "10") Integer pageSize,
	            @RequestParam(required = false, defaultValue = "reqId") String sortBy,
	            @RequestParam(required = false, defaultValue = "asc") String sortDir) throws Exception {
	        FourFinResponse<List<ChargesReqBookingDto>> response = new FourFinResponse<>();
	        try {
	        	
	        	// Create pageable object
	            Sort.Direction direction = sortDir.equalsIgnoreCase("desc") ? 
	                Sort.Direction.DESC : Sort.Direction.ASC;
	            Pageable pageable = PageRequest.of(pageNo, pageSize, Sort.by(direction, sortBy));
	        	
	            Page<ChargesReqBookingDto> pageResult = chargesReqService.getBookingChargesReqList(status,activityCode, reqNo, date, pageable);
	        	
	            
	        	 HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), pageResult);
	             return ResponseEntity.ok().headers(headers).body(pageResult.getContent());
	        	
	           
	        } catch (RecieptReqDetailsNotFoundException e) {

	            response.setHttpStatus(HttpStatus.NOT_FOUND);
	            response.setResponseCode(HttpStatus.NOT_FOUND.value());
	            response.setResponseMessage(HttpStatus.NOT_FOUND.getReasonPhrase());
	            response.setResponseMessage(e.getMessage());
	            throw new RecieptReqDetailsNotFoundException(e.getMessage());
	        } catch (Exception e) {

	            response.setHttpStatus(HttpStatus.INTERNAL_SERVER_ERROR);
	            response.setResponseCode(HttpStatus.INTERNAL_SERVER_ERROR.value());
	            response.setResponseMessage(HttpStatus.INTERNAL_SERVER_ERROR.getReasonPhrase());
	            response.setResponseMessage(e.getMessage());
	            throw new RecieptReqDetailsInternalServerError(e.getMessage());
	        }
	    }
	 
	//------ API to get CreditNote requests list by status, date and reqId
			//------ There are four parameters statusList, date, reqId, activityCode(this is required) with pagination
				 @GetMapping(value = "/getCreditNoteReqList", produces = MediaType.APPLICATION_JSON_VALUE)
				    public ResponseEntity<List<CreditNoteReqDto>> getCreditNoteReqList( @RequestParam (required = false) List<String> status,
				    		@RequestParam String activityCode , 
				    		@RequestParam(required = false, defaultValue = "0") Integer reqNo,
				    		@RequestParam (required = false, defaultValue = "null") String date,
				    		@RequestParam(required = false, defaultValue = "0") Integer pageNo,
				            @RequestParam(required = false, defaultValue = "10") Integer pageSize,
				            @RequestParam(required = false, defaultValue = "reqId") String sortBy,
				            @RequestParam(required = false, defaultValue = "asc") String sortDir) throws Exception {
				        FourFinResponse<List<CreditNoteReqDto>> response = new FourFinResponse<>();
				        try {
				        	
				        	// Create pageable object
				            Sort.Direction direction = sortDir.equalsIgnoreCase("desc") ? 
				                Sort.Direction.DESC : Sort.Direction.ASC;
				            Pageable pageable = PageRequest.of(pageNo, pageSize, Sort.by(direction, sortBy));
				        	
				            Page<CreditNoteReqDto> pageResult = creditNoteReqService.getCreditNoteReqList(status,activityCode, reqNo, date, pageable);
				        	
				            
				        	 HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), pageResult);
				             return ResponseEntity.ok().headers(headers).body(pageResult.getContent());
				        	
				           
				        } catch (RecieptReqDetailsNotFoundException e) {

				            response.setHttpStatus(HttpStatus.NOT_FOUND);
				            response.setResponseCode(HttpStatus.NOT_FOUND.value());
				            response.setResponseMessage(HttpStatus.NOT_FOUND.getReasonPhrase());
				            response.setResponseMessage(e.getMessage());
				            throw new RecieptReqDetailsNotFoundException(e.getMessage());
				        } catch (Exception e) {

				            response.setHttpStatus(HttpStatus.INTERNAL_SERVER_ERROR);
				            response.setResponseCode(HttpStatus.INTERNAL_SERVER_ERROR.value());
				            response.setResponseMessage(HttpStatus.INTERNAL_SERVER_ERROR.getReasonPhrase());
				            response.setResponseMessage(e.getMessage());
				            throw new RecieptReqDetailsInternalServerError(e.getMessage());
				        }
				    }
				 
				//------ API to get PartPrepayment requests list by status, date and reqId
					//------ There are four parameters statusList, date, reqId, activityCode(this is required) with pagination
						 @GetMapping(value = "/getPartPrepaymentReqList", produces = MediaType.APPLICATION_JSON_VALUE)
						    public ResponseEntity<List<PartPrepaymentDto>> getPartPrepaymentReqList( @RequestParam (required = false) List<String> status,
						    		@RequestParam String activityCode , 
						    		@RequestParam(required = false, defaultValue = "0") Integer reqNo,
						    		@RequestParam (required = false, defaultValue = "null") String date,
						    		@RequestParam(required = false, defaultValue = "0") Integer pageNo,
						            @RequestParam(required = false, defaultValue = "10") Integer pageSize,
						            @RequestParam(required = false, defaultValue = "reqId") String sortBy,
						            @RequestParam(required = false, defaultValue = "asc") String sortDir) throws Exception {
						        FourFinResponse<List<PartPrepaymentDto>> response = new FourFinResponse<>();
						        try {
						        	
						        	// Create pageable object
						            Sort.Direction direction = sortDir.equalsIgnoreCase("desc") ? 
						                Sort.Direction.DESC : Sort.Direction.ASC;
						            Pageable pageable = PageRequest.of(pageNo, pageSize, Sort.by(direction, sortBy));
						        	
						            Page<PartPrepaymentDto> pageResult = partPrepayService.getPartPrepaymentReqList(status,activityCode, reqNo, date, pageable);
						        	
						            
						        	 HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), pageResult);
						             return ResponseEntity.ok().headers(headers).body(pageResult.getContent());
						        	
						           
						        } catch (RecieptReqDetailsNotFoundException e) {

						            response.setHttpStatus(HttpStatus.NOT_FOUND);
						            response.setResponseCode(HttpStatus.NOT_FOUND.value());
						            response.setResponseMessage(HttpStatus.NOT_FOUND.getReasonPhrase());
						            response.setResponseMessage(e.getMessage());
						            throw new RecieptReqDetailsNotFoundException(e.getMessage());
						        } catch (Exception e) {

						            response.setHttpStatus(HttpStatus.INTERNAL_SERVER_ERROR);
						            response.setResponseCode(HttpStatus.INTERNAL_SERVER_ERROR.value());
						            response.setResponseMessage(HttpStatus.INTERNAL_SERVER_ERROR.getReasonPhrase());
						            response.setResponseMessage(e.getMessage());
						            throw new RecieptReqDetailsInternalServerError(e.getMessage());
						        }
						    }
						 
						 
						//------ API to get Waiver Charges requests list by status, date and reqId
							//------ There are four parameters statusList, date, reqId, activityCode(this is required) with pagination
								 @GetMapping(value = "/getWaiverChargesReqList", produces = MediaType.APPLICATION_JSON_VALUE)
								    public ResponseEntity<List<ChargesWaiverDto>> getWaiverChargesReqList( @RequestParam (required = false) List<String> status,
								    		@RequestParam String activityCode , 
								    		@RequestParam(required = false, defaultValue = "0") Integer reqNo,
								    		@RequestParam (required = false, defaultValue = "null") String date,
								    		@RequestParam(required = false, defaultValue = "0") Integer pageNo,
								            @RequestParam(required = false, defaultValue = "10") Integer pageSize,
								            @RequestParam(required = false, defaultValue = "reqId") String sortBy,
								            @RequestParam(required = false, defaultValue = "asc") String sortDir) throws Exception {
								        FourFinResponse<List<ChargesWaiverDto>> response = new FourFinResponse<>();
								        try {
								        	
								        	// Create pageable object
								            Sort.Direction direction = sortDir.equalsIgnoreCase("desc") ? 
								                Sort.Direction.DESC : Sort.Direction.ASC;
								            Pageable pageable = PageRequest.of(pageNo, pageSize, Sort.by(direction, sortBy));
								        	
								            Page<ChargesWaiverDto> pageResult = waiverReqService.getWaiverChargesReqList(status,activityCode, reqNo, date, pageable);
								        	
								            
								        	 HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), pageResult);
								             return ResponseEntity.ok().headers(headers).body(pageResult.getContent());
								        	
								           
								        } catch (RecieptReqDetailsNotFoundException e) {

								            response.setHttpStatus(HttpStatus.NOT_FOUND);
								            response.setResponseCode(HttpStatus.NOT_FOUND.value());
								            response.setResponseMessage(HttpStatus.NOT_FOUND.getReasonPhrase());
								            response.setResponseMessage(e.getMessage());
								            throw new RecieptReqDetailsNotFoundException(e.getMessage());
								        } catch (Exception e) {

								            response.setHttpStatus(HttpStatus.INTERNAL_SERVER_ERROR);
								            response.setResponseCode(HttpStatus.INTERNAL_SERVER_ERROR.value());
								            response.setResponseMessage(HttpStatus.INTERNAL_SERVER_ERROR.getReasonPhrase());
								            response.setResponseMessage(e.getMessage());
								            throw new RecieptReqDetailsInternalServerError(e.getMessage());
								        }
								    }
						 
								 
								 
								//------ API to get Grid Read Only List For waiver Charges 
										 @GetMapping(value = "/getWaiverChargesReqGridList", produces = MediaType.APPLICATION_JSON_VALUE)
										    public ResponseEntity<List<ChargesWaiverGridDto>> getWaiverChargesReqGridList( 
										    		@RequestParam String mastAgrId , 
										    		@RequestParam(required = false, defaultValue = "0") Integer pageNo,
										            @RequestParam(required = false, defaultValue = "10") Integer pageSize,
										            @RequestParam(required = false, defaultValue = "mastAgrId") String sortBy,
										            @RequestParam(required = false, defaultValue = "asc") String sortDir) throws Exception {
										        FourFinResponse<List<ChargesWaiverGridDto>> response = new FourFinResponse<>();
										        try {
										        	
										        	// Create pageable object
										            Sort.Direction direction = sortDir.equalsIgnoreCase("desc") ? 
										                Sort.Direction.DESC : Sort.Direction.ASC;
										            Pageable pageable = PageRequest.of(pageNo, pageSize, Sort.by(direction, sortBy));
										        	
										            Page<ChargesWaiverGridDto> pageResult = waiverReqService.getWaiverChargesReqGridList(mastAgrId, pageable);
										        	
										            
										        	 HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), pageResult);
										             return ResponseEntity.ok().headers(headers).body(pageResult.getContent());
										        	
										           
										        } catch (RecieptReqDetailsNotFoundException e) {

										            response.setHttpStatus(HttpStatus.NOT_FOUND);
										            response.setResponseCode(HttpStatus.NOT_FOUND.value());
										            response.setResponseMessage(HttpStatus.NOT_FOUND.getReasonPhrase());
										            response.setResponseMessage(e.getMessage());
										            throw new RecieptReqDetailsNotFoundException(e.getMessage());
										        } catch (Exception e) {

										            response.setHttpStatus(HttpStatus.INTERNAL_SERVER_ERROR);
										            response.setResponseCode(HttpStatus.INTERNAL_SERVER_ERROR.value());
										            response.setResponseMessage(HttpStatus.INTERNAL_SERVER_ERROR.getReasonPhrase());
										            response.setResponseMessage(e.getMessage());
										            throw new RecieptReqDetailsInternalServerError(e.getMessage());
										        }
										    }
										 
											//------ API to get FullPrepayment requests list by status, date and reqId
											//------ There are four parameters statusList, date, reqId, activityCode(this is required) with pagination
												 @GetMapping(value = "/getFullPrepaymentReqList", produces = MediaType.APPLICATION_JSON_VALUE)
												    public ResponseEntity<List<ForclosureCashRequestDto>> getFullPrepaymentReqList( @RequestParam (required = false) List<String> status,
												    		@RequestParam String activityCode , 
												    		@RequestParam(required = false, defaultValue = "0") Integer reqNo,
												    		@RequestParam (required = false, defaultValue = "null") String date,
												    		@RequestParam(required = false, defaultValue = "0") Integer pageNo,
												            @RequestParam(required = false, defaultValue = "10") Integer pageSize,
												            @RequestParam(required = false, defaultValue = "reqId") String sortBy,
												            @RequestParam(required = false, defaultValue = "asc") String sortDir) throws Exception {
												        FourFinResponse<List<ForclosureCashRequestDto>> response = new FourFinResponse<>();
												        try {
												        	
												        	// Create pageable object
												            Sort.Direction direction = sortDir.equalsIgnoreCase("desc") ? 
												                Sort.Direction.DESC : Sort.Direction.ASC;
												            Pageable pageable = PageRequest.of(pageNo, pageSize, Sort.by(direction, sortBy));
												        	
												            Page<ForclosureCashRequestDto> pageResult = forclosureService.getFullPrepaymentReqList(status,activityCode, reqNo, date, pageable);
												        	
												            
												        	 HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), pageResult);
												             return ResponseEntity.ok().headers(headers).body(pageResult.getContent());
												        	
												           
												        } catch (RecieptReqDetailsNotFoundException e) {

												            response.setHttpStatus(HttpStatus.NOT_FOUND);
												            response.setResponseCode(HttpStatus.NOT_FOUND.value());
												            response.setResponseMessage(HttpStatus.NOT_FOUND.getReasonPhrase());
												            response.setResponseMessage(e.getMessage());
												            throw new RecieptReqDetailsNotFoundException(e.getMessage());
												        } catch (Exception e) {

												            response.setHttpStatus(HttpStatus.INTERNAL_SERVER_ERROR);
												            response.setResponseCode(HttpStatus.INTERNAL_SERVER_ERROR.value());
												            response.setResponseMessage(HttpStatus.INTERNAL_SERVER_ERROR.getReasonPhrase());
												            response.setResponseMessage(e.getMessage());
												            throw new RecieptReqDetailsInternalServerError(e.getMessage());
												        }
												    }
										 
										 
												 @GetMapping(value = "/getPendingForApprovalList", produces = MediaType.APPLICATION_JSON_VALUE)
												    public ResponseEntity<List<PendingForApprovalDto>> getPendingForApprovalList( @RequestParam (required = false) String status,  
												    		@RequestParam(required = false, defaultValue = "0") Integer reqNo,
												    		@RequestParam (required = false, defaultValue = "null") String date,
												    		@RequestParam (required = false) String userid,
												    		@RequestParam(required = false, defaultValue = "true") Boolean isAdminAllocated,
												    		@RequestParam(required = false, defaultValue = "0") Integer pageNo,
												            @RequestParam(required = false, defaultValue = "10") Integer pageSize,
												            @RequestParam(required = false, defaultValue = "dtRequestDate") String sortBy,
												            @RequestParam(required = false, defaultValue = "asc") String sortDir) throws Exception {
												        FourFinResponse<List<PendingForApprovalDto>> response = new FourFinResponse<>();
												        try {
												        	
												        	// Create pageable object
												            Sort.Direction direction = sortDir.equalsIgnoreCase("desc") ? 
												                Sort.Direction.DESC : Sort.Direction.ASC;
												            Pageable pageable = PageRequest.of(pageNo, pageSize, Sort.by(direction, sortBy));
												        	
												            Page<PendingForApprovalDto> pageResult = approvalService.getPendingForApprovalReqList(status,reqNo,date,userid,isAdminAllocated,pageable); 
												        	 HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), pageResult);
												             return ResponseEntity.ok().headers(headers).body(pageResult.getContent());
												        	
												           
												        } catch (RecieptReqDetailsNotFoundException e) {

												            response.setHttpStatus(HttpStatus.NOT_FOUND);
												            response.setResponseCode(HttpStatus.NOT_FOUND.value());
												            response.setResponseMessage(HttpStatus.NOT_FOUND.getReasonPhrase());
												            response.setResponseMessage(e.getMessage());
												            throw new RecieptReqDetailsNotFoundException(e.getMessage());
												        } catch (Exception e) {

												            response.setHttpStatus(HttpStatus.INTERNAL_SERVER_ERROR);
												            response.setResponseCode(HttpStatus.INTERNAL_SERVER_ERROR.value());
												            response.setResponseMessage(HttpStatus.INTERNAL_SERVER_ERROR.getReasonPhrase());
												            response.setResponseMessage(e.getMessage());
												            throw new RecieptReqDetailsInternalServerError(e.getMessage());
												        }
												    }
												 @GetMapping(value = "/getCountOfPendingRequest", produces = MediaType.APPLICATION_JSON_VALUE)
													public ResponseEntity<Integer> getCountOfPendingApprovalRequest(@RequestParam String reqStatus,@RequestParam String userId) {
														try {

															int count = approvalService.getPendingForApprovalRequest(reqStatus,userId);

															return ResponseEntity.ok(count);
														} catch (TransactionDataNotFoundException e) {

															throw new TransactionDataNotFoundException(e.getMessage());
														} catch (Exception e) {

															throw new TransactionInternalServerError(e.getMessage());
														}
													}
								 
						 
				
}
