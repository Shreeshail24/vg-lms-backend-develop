package com.samsoft.lms.newux.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import com.samsoft.lms.core.dto.FourFinResponse;
import com.samsoft.lms.las.util.PaginationUtil;
import com.samsoft.lms.newux.dto.response.ChargesReqBookingReqDetailsResponseDto;
import com.samsoft.lms.newux.dto.response.ChargesWaverReqDetailsResponseDto;
import com.samsoft.lms.newux.dto.response.CreditNoteReqDetailsResponseDto;
import com.samsoft.lms.newux.dto.response.DebitNoteReqDetailsResponseDto;
import com.samsoft.lms.newux.dto.response.DrawDownDisbUpdateReqDetailsResponseDto;
import com.samsoft.lms.newux.dto.response.DrawDownReqDetailsResponseDto;
import com.samsoft.lms.newux.dto.response.DrawDownUpdateReqDetailsResponseDto;
import com.samsoft.lms.newux.dto.response.ForClosureOnlineReqDetailsResponseDto;
import com.samsoft.lms.newux.dto.response.PartPaymentDetailsResponseDto;
import com.samsoft.lms.newux.dto.response.RecieptReqDetailsResponseDto;
import com.samsoft.lms.newux.dto.response.RefundReqDetailsResponseDto;
import com.samsoft.lms.newux.dto.response.getloans.AllSRWithActCodeAndStatusResDto;
import com.samsoft.lms.newux.dto.response.getloans.AllSRWithActCodeResDto;
import com.samsoft.lms.newux.dto.response.getloans.SrPendingCountResDto;
import com.samsoft.lms.newux.exceptions.ChargesReqBookingReqDetailsInternalServerError;
import com.samsoft.lms.newux.exceptions.ChargesReqBookingReqDetailsNotFoundException;
import com.samsoft.lms.newux.exceptions.ChargesWaverReqDetailsInternalServerError;
import com.samsoft.lms.newux.exceptions.ChargesWaverReqDetailsNotFoundException;
import com.samsoft.lms.newux.exceptions.CreditNoteReqDetailsInternalServerError;
import com.samsoft.lms.newux.exceptions.CreditNoteReqDetailsNotFoundException;
import com.samsoft.lms.newux.exceptions.DebitNoteReqDetailsInternalServerError;
import com.samsoft.lms.newux.exceptions.DebitNoteReqDetailsNotFoundException;
import com.samsoft.lms.newux.exceptions.DrawDownDisbUpdateReqDetailsInternalServerError;
import com.samsoft.lms.newux.exceptions.DrawDownDisbUpdateReqDetailsNotFoundException;
import com.samsoft.lms.newux.exceptions.DrawDownReqDetailsInternalServerError;
import com.samsoft.lms.newux.exceptions.DrawDownReqDetailsNotFoundException;
import com.samsoft.lms.newux.exceptions.DrawDownUpdateReqDetailsInternalServerError;
import com.samsoft.lms.newux.exceptions.DrawDownUpdateReqDetailsNotFoundException;
import com.samsoft.lms.newux.exceptions.ForClosureReqDetailsInternalServerError;
import com.samsoft.lms.newux.exceptions.ForClosureReqDetailsNotFoundException;
import com.samsoft.lms.newux.exceptions.RecieptReqDetailsInternalServerError;
import com.samsoft.lms.newux.exceptions.RecieptReqDetailsNotFoundException;
import com.samsoft.lms.newux.exceptions.RefundReqDetailsInternalServerError;
import com.samsoft.lms.newux.exceptions.RefundReqDetailsNotFoundException;
import com.samsoft.lms.newux.exceptions.SrPendingCountInternalServerError;
import com.samsoft.lms.newux.exceptions.SrPendingCountNotFoundException;
import com.samsoft.lms.newux.exceptions.SrWorklistInternalServerError;
import com.samsoft.lms.newux.exceptions.SrWorklistNotFoundException;
import com.samsoft.lms.newux.services.SRService;
import com.samsoft.lms.request.dto.ChargesReqBookingDto;
import com.samsoft.lms.newux.dto.response.getloans.RequestedStatusCountDto;

@RestController
@RequestMapping(value = "/servicerequest")
@CrossOrigin(origins = {"https://lms.4fin.in/","http://localhost:4200", "https://qa-lms.4fin.in", "https://qa-losone.4fin.in"} ,allowedHeaders = "*")

public class SRController {

	@Autowired
	private SRService srService;
	
	 @GetMapping(value = "/getsrpendingcount", produces = MediaType.APPLICATION_JSON_VALUE)
	    public FourFinResponse<SrPendingCountResDto> getSRPendingCount() throws Exception {
	        FourFinResponse<SrPendingCountResDto> response = new FourFinResponse<>();
	        try {
	        	SrPendingCountResDto srPendingCountResDto = srService.getSRPendingCount();
	            if(srPendingCountResDto != null) {
	                response.setHttpStatus(HttpStatus.OK);
	                response.setResponseCode(HttpStatus.OK.value());
	                response.setData(srPendingCountResDto);
	            } else {
	                response.setHttpStatus(HttpStatus.NOT_FOUND);
	                response.setResponseCode(HttpStatus.NOT_FOUND.value());
	                response.setResponseMessage(HttpStatus.NOT_FOUND.getReasonPhrase());
	                response.setData(null);
	            }
	        } catch (SrPendingCountNotFoundException e) {

	            response.setHttpStatus(HttpStatus.NOT_FOUND);
	            response.setResponseCode(HttpStatus.NOT_FOUND.value());
	            response.setResponseMessage(HttpStatus.NOT_FOUND.getReasonPhrase());
	            response.setResponseMessage(e.getMessage());
	            throw new SrPendingCountNotFoundException(e.getMessage());
	        } catch (Exception e) {

	            response.setHttpStatus(HttpStatus.INTERNAL_SERVER_ERROR);
	            response.setResponseCode(HttpStatus.INTERNAL_SERVER_ERROR.value());
	            response.setResponseMessage(HttpStatus.INTERNAL_SERVER_ERROR.getReasonPhrase());
	            response.setResponseMessage(e.getMessage());
	            throw new SrPendingCountInternalServerError(e.getMessage());
	        }

	        return response;
	    }
	 
	
	 @GetMapping(value = "/getallsrbyactivitycode", produces = MediaType.APPLICATION_JSON_VALUE)
	    public FourFinResponse<AllSRWithActCodeResDto> getAllSRByActivityCode(@RequestParam String activityCode) throws Exception {
	        FourFinResponse<AllSRWithActCodeResDto> response = new FourFinResponse<>();
	        try {
	        	AllSRWithActCodeResDto allSRResDto = srService.getAllSRByActivityCode(activityCode);
	            if(allSRResDto != null) {
	                response.setHttpStatus(HttpStatus.OK);
	                response.setResponseCode(HttpStatus.OK.value());
	                response.setData(allSRResDto);
	            } else {
	                response.setHttpStatus(HttpStatus.NOT_FOUND);
	                response.setResponseCode(HttpStatus.NOT_FOUND.value());
	                response.setResponseMessage(HttpStatus.NOT_FOUND.getReasonPhrase());
	                response.setData(null);
	            }
	        } catch (SrWorklistNotFoundException e) {

	            response.setHttpStatus(HttpStatus.NOT_FOUND);
	            response.setResponseCode(HttpStatus.NOT_FOUND.value());
	            response.setResponseMessage(HttpStatus.NOT_FOUND.getReasonPhrase());
	            response.setResponseMessage(e.getMessage());
	            throw new SrWorklistNotFoundException(e.getMessage());
	        } catch (Exception e) {

	            response.setHttpStatus(HttpStatus.INTERNAL_SERVER_ERROR);
	            response.setResponseCode(HttpStatus.INTERNAL_SERVER_ERROR.value());
	            response.setResponseMessage(HttpStatus.INTERNAL_SERVER_ERROR.getReasonPhrase());
	            response.setResponseMessage(e.getMessage());
	            throw new SrWorklistInternalServerError(e.getMessage());
	        }

	        return response;
	    }
	 
	 @GetMapping(value = "/getallsrbyactivitycodeandstatus", produces = MediaType.APPLICATION_JSON_VALUE)
	    public FourFinResponse<AllSRWithActCodeAndStatusResDto> getAllSRByActivityCodeAndStatus(@RequestParam String activityCode, @RequestParam String status) throws Exception {
	        FourFinResponse<AllSRWithActCodeAndStatusResDto> response = new FourFinResponse<>();
	        try {
	        	AllSRWithActCodeAndStatusResDto allSRResDto = srService.getAllSRByActivityCodeAndStatus(activityCode, status);
	            if(allSRResDto != null) {
	                response.setHttpStatus(HttpStatus.OK);
	                response.setResponseCode(HttpStatus.OK.value());
	                response.setData(allSRResDto);
	            } else {
	                response.setHttpStatus(HttpStatus.NOT_FOUND);
	                response.setResponseCode(HttpStatus.NOT_FOUND.value());
	                response.setResponseMessage(HttpStatus.NOT_FOUND.getReasonPhrase());
	                response.setData(null);
	            }
	        } catch (SrWorklistNotFoundException e) {

	            response.setHttpStatus(HttpStatus.NOT_FOUND);
	            response.setResponseCode(HttpStatus.NOT_FOUND.value());
	            response.setResponseMessage(HttpStatus.NOT_FOUND.getReasonPhrase());
	            response.setResponseMessage(e.getMessage());
	            throw new SrWorklistNotFoundException(e.getMessage());
	        } catch (Exception e) {

	            response.setHttpStatus(HttpStatus.INTERNAL_SERVER_ERROR);
	            response.setResponseCode(HttpStatus.INTERNAL_SERVER_ERROR.value());
	            response.setResponseMessage(HttpStatus.INTERNAL_SERVER_ERROR.getReasonPhrase());
	            response.setResponseMessage(e.getMessage());
	            throw new SrWorklistInternalServerError(e.getMessage());
	        }
	        return response;
	    }
	
	 @GetMapping(value = "/getrecieptreqdetails", produces = MediaType.APPLICATION_JSON_VALUE)
	    public FourFinResponse<RecieptReqDetailsResponseDto> getRecieptReqDetails(@RequestParam String customerId, @RequestParam String mastAgrId, @RequestParam String reqId) throws Exception {
	        FourFinResponse<RecieptReqDetailsResponseDto> response = new FourFinResponse<>();
	        try {
	        	RecieptReqDetailsResponseDto recieptReqDetailsResponseDto = srService.getRecieptReqDetails(customerId, mastAgrId, reqId);
	            if(recieptReqDetailsResponseDto != null) {
	                response.setHttpStatus(HttpStatus.OK);
	                response.setResponseCode(HttpStatus.OK.value());
	                response.setData(recieptReqDetailsResponseDto);
	            } else {
	                response.setHttpStatus(HttpStatus.NOT_FOUND);
	                response.setResponseCode(HttpStatus.NOT_FOUND.value());
	                response.setResponseMessage(HttpStatus.NOT_FOUND.getReasonPhrase());
	                response.setData(null);
	            }
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

	        return response;
	    }
	  
	 
	 @GetMapping(value = "/getrefundreqdetails", produces = MediaType.APPLICATION_JSON_VALUE)
	 public FourFinResponse<RefundReqDetailsResponseDto> getRefundReqDetails(@RequestParam String customerId, @RequestParam String mastAgrId, @RequestParam String reqId) throws Exception {
	        FourFinResponse<RefundReqDetailsResponseDto> response = new FourFinResponse<>();
	        try {
	        	RefundReqDetailsResponseDto refundReqDetailsResponseDto = srService.getRefundReqDetails(customerId, mastAgrId, reqId);
	            if(refundReqDetailsResponseDto != null) {
	                response.setHttpStatus(HttpStatus.OK);
	                response.setResponseCode(HttpStatus.OK.value());
	                response.setData(refundReqDetailsResponseDto);
	            } else {
	                response.setHttpStatus(HttpStatus.NOT_FOUND);
	                response.setResponseCode(HttpStatus.NOT_FOUND.value());
	                response.setResponseMessage(HttpStatus.NOT_FOUND.getReasonPhrase());
	                response.setData(null);
	            }
	        } catch (RefundReqDetailsNotFoundException e) {

	            response.setHttpStatus(HttpStatus.NOT_FOUND);
	            response.setResponseCode(HttpStatus.NOT_FOUND.value());
	            response.setResponseMessage(HttpStatus.NOT_FOUND.getReasonPhrase());
	            response.setResponseMessage(e.getMessage());
	            throw new RefundReqDetailsNotFoundException(e.getMessage());
	        } catch (Exception e) {

	            response.setHttpStatus(HttpStatus.INTERNAL_SERVER_ERROR);
	            response.setResponseCode(HttpStatus.INTERNAL_SERVER_ERROR.value());
	            response.setResponseMessage(HttpStatus.INTERNAL_SERVER_ERROR.getReasonPhrase());
	            response.setResponseMessage(e.getMessage());
	            throw new RefundReqDetailsInternalServerError(e.getMessage());
	        }

	        return response;
	    }
	
	 
	 @GetMapping(value = "/getdebitnotereqdetails", produces = MediaType.APPLICATION_JSON_VALUE)
	 public FourFinResponse<DebitNoteReqDetailsResponseDto> getDebitNoteReqDetails(@RequestParam String customerId, @RequestParam String mastAgrId, @RequestParam String reqId) throws Exception {
	        FourFinResponse<DebitNoteReqDetailsResponseDto> response = new FourFinResponse<>();
	        try {
	        	DebitNoteReqDetailsResponseDto debitNoteReqDetailsResponseDto = srService.getDebitNoteReqDetails(customerId, mastAgrId, reqId);
	            if(debitNoteReqDetailsResponseDto != null) {
	                response.setHttpStatus(HttpStatus.OK);
	                response.setResponseCode(HttpStatus.OK.value());
	                response.setData(debitNoteReqDetailsResponseDto);
	            } else {
	                response.setHttpStatus(HttpStatus.NOT_FOUND);
	                response.setResponseCode(HttpStatus.NOT_FOUND.value());
	                response.setResponseMessage(HttpStatus.NOT_FOUND.getReasonPhrase());
	                response.setData(null);
	            }
	        } catch (DebitNoteReqDetailsNotFoundException e) {

	            response.setHttpStatus(HttpStatus.NOT_FOUND);
	            response.setResponseCode(HttpStatus.NOT_FOUND.value());
	            response.setResponseMessage(HttpStatus.NOT_FOUND.getReasonPhrase());
	            response.setResponseMessage(e.getMessage());
	            throw new DebitNoteReqDetailsNotFoundException(e.getMessage());
	        } catch (Exception e) {

	            response.setHttpStatus(HttpStatus.INTERNAL_SERVER_ERROR);
	            response.setResponseCode(HttpStatus.INTERNAL_SERVER_ERROR.value());
	            response.setResponseMessage(HttpStatus.INTERNAL_SERVER_ERROR.getReasonPhrase());
	            response.setResponseMessage(e.getMessage());
	            throw new DebitNoteReqDetailsInternalServerError(e.getMessage());
	        }

	        return response;
	    }


	 @GetMapping(value = "/getcreditnotereqdetails", produces = MediaType.APPLICATION_JSON_VALUE)
		 public FourFinResponse<CreditNoteReqDetailsResponseDto> getCreditNoteReqDetails(@RequestParam String customerId, @RequestParam String mastAgrId, @RequestParam String reqId) throws Exception {
		        FourFinResponse<CreditNoteReqDetailsResponseDto> response = new FourFinResponse<>();
		        try {
		        	CreditNoteReqDetailsResponseDto creditNoteReqDetailsResponseDto = srService.getCreditNoteReqDetails(customerId, mastAgrId, reqId);
		            if(creditNoteReqDetailsResponseDto != null) {
		                response.setHttpStatus(HttpStatus.OK);
		                response.setResponseCode(HttpStatus.OK.value());
		                response.setData(creditNoteReqDetailsResponseDto);
		            } else {
		                response.setHttpStatus(HttpStatus.NOT_FOUND);
		                response.setResponseCode(HttpStatus.NOT_FOUND.value());
		                response.setResponseMessage(HttpStatus.NOT_FOUND.getReasonPhrase());
		                response.setData(null);
		            }
		        } catch (CreditNoteReqDetailsNotFoundException e) {

		            response.setHttpStatus(HttpStatus.NOT_FOUND);
		            response.setResponseCode(HttpStatus.NOT_FOUND.value());
		            response.setResponseMessage(HttpStatus.NOT_FOUND.getReasonPhrase());
		            response.setResponseMessage(e.getMessage());
		            throw new CreditNoteReqDetailsNotFoundException(e.getMessage());
		        } catch (Exception e) {

		            response.setHttpStatus(HttpStatus.INTERNAL_SERVER_ERROR);
		            response.setResponseCode(HttpStatus.INTERNAL_SERVER_ERROR.value());
		            response.setResponseMessage(HttpStatus.INTERNAL_SERVER_ERROR.getReasonPhrase());
		            response.setResponseMessage(e.getMessage());
		            throw new CreditNoteReqDetailsInternalServerError(e.getMessage());
		        }

		        return response;
		    }

	 
	 @GetMapping(value = "/getchargesreqbookingreqdetails", produces = MediaType.APPLICATION_JSON_VALUE)
	    public FourFinResponse<ChargesReqBookingReqDetailsResponseDto> getChargesReqBookingReqDetails(@RequestParam String customerId, @RequestParam String mastAgrId, @RequestParam String reqId) throws Exception {
	        FourFinResponse<ChargesReqBookingReqDetailsResponseDto> response = new FourFinResponse<>();
	        try {
	        	ChargesReqBookingReqDetailsResponseDto chargesReqBookingReqDetailsResponseDto = srService.getChargesReqBookingReqDetails(customerId, mastAgrId, reqId);
	            if(chargesReqBookingReqDetailsResponseDto != null) {
	                response.setHttpStatus(HttpStatus.OK);
	                response.setResponseCode(HttpStatus.OK.value());
	                response.setData(chargesReqBookingReqDetailsResponseDto);
	            } else {
	                response.setHttpStatus(HttpStatus.NOT_FOUND);
	                response.setResponseCode(HttpStatus.NOT_FOUND.value());
	                response.setResponseMessage(HttpStatus.NOT_FOUND.getReasonPhrase());
	                response.setData(null);
	            }
	        } catch (ChargesReqBookingReqDetailsNotFoundException e) {

	            response.setHttpStatus(HttpStatus.NOT_FOUND);
	            response.setResponseCode(HttpStatus.NOT_FOUND.value());
	            response.setResponseMessage(HttpStatus.NOT_FOUND.getReasonPhrase());
	            response.setResponseMessage(e.getMessage());
	            throw new ChargesReqBookingReqDetailsNotFoundException(e.getMessage());
	        } catch (Exception e) {

	            response.setHttpStatus(HttpStatus.INTERNAL_SERVER_ERROR);
	            response.setResponseCode(HttpStatus.INTERNAL_SERVER_ERROR.value());
	            response.setResponseMessage(HttpStatus.INTERNAL_SERVER_ERROR.getReasonPhrase());
	            response.setResponseMessage(e.getMessage());
	            throw new ChargesReqBookingReqDetailsInternalServerError(e.getMessage());
	        }

	        return response;
	    }
	 
	 @GetMapping(value = "/getchargeswaverreqdetails", produces = MediaType.APPLICATION_JSON_VALUE)
	    public FourFinResponse<ChargesWaverReqDetailsResponseDto> getChargesWaverReqDetails(@RequestParam String customerId, @RequestParam String mastAgrId, @RequestParam String reqId) throws Exception {
	        FourFinResponse<ChargesWaverReqDetailsResponseDto> response = new FourFinResponse<>();
	        try {
	        	ChargesWaverReqDetailsResponseDto chargesWaverReqDetailsResponseDto = srService.getChargesWaverReqDetails(customerId, mastAgrId, reqId);
	            if(chargesWaverReqDetailsResponseDto != null) {
	                response.setHttpStatus(HttpStatus.OK);
	                response.setResponseCode(HttpStatus.OK.value());
	                response.setData(chargesWaverReqDetailsResponseDto);
	            } else {
	                response.setHttpStatus(HttpStatus.NOT_FOUND);
	                response.setResponseCode(HttpStatus.NOT_FOUND.value());
	                response.setResponseMessage(HttpStatus.NOT_FOUND.getReasonPhrase());
	                response.setData(null);
	            }
	        } catch (ChargesWaverReqDetailsNotFoundException e) {

	            response.setHttpStatus(HttpStatus.NOT_FOUND);
	            response.setResponseCode(HttpStatus.NOT_FOUND.value());
	            response.setResponseMessage(HttpStatus.NOT_FOUND.getReasonPhrase());
	            response.setResponseMessage(e.getMessage());
	            throw new ChargesWaverReqDetailsNotFoundException(e.getMessage());
	        } catch (Exception e) {

	            response.setHttpStatus(HttpStatus.INTERNAL_SERVER_ERROR);
	            response.setResponseCode(HttpStatus.INTERNAL_SERVER_ERROR.value());
	            response.setResponseMessage(HttpStatus.INTERNAL_SERVER_ERROR.getReasonPhrase());
	            response.setResponseMessage(e.getMessage());
	            throw new ChargesWaverReqDetailsInternalServerError(e.getMessage());
	        }

	        return response;
	    }
	 
	 
	 @GetMapping(value = "/getforclosureonlinereqdetails", produces = MediaType.APPLICATION_JSON_VALUE)
	    public FourFinResponse<ForClosureOnlineReqDetailsResponseDto> getForClosureOnlineReqDetails(@RequestParam String customerId, @RequestParam String mastAgrId, @RequestParam String reqId) throws Exception {
	        FourFinResponse<ForClosureOnlineReqDetailsResponseDto> response = new FourFinResponse<>();
	        try {
	        	ForClosureOnlineReqDetailsResponseDto forClosureOnlineReqDetailsResponseDto = srService.getForClosureOnlineReqDetails(customerId, mastAgrId, reqId);
	            if(forClosureOnlineReqDetailsResponseDto != null) {
	                response.setHttpStatus(HttpStatus.OK);
	                response.setResponseCode(HttpStatus.OK.value());
	                response.setData(forClosureOnlineReqDetailsResponseDto);
	            } else {
	                response.setHttpStatus(HttpStatus.NOT_FOUND);
	                response.setResponseCode(HttpStatus.NOT_FOUND.value());
	                response.setResponseMessage(HttpStatus.NOT_FOUND.getReasonPhrase());
	                response.setData(null);
	            }
	        } catch (ForClosureReqDetailsNotFoundException e) {

	            response.setHttpStatus(HttpStatus.NOT_FOUND);
	            response.setResponseCode(HttpStatus.NOT_FOUND.value());
	            response.setResponseMessage(HttpStatus.NOT_FOUND.getReasonPhrase());
	            response.setResponseMessage(e.getMessage());
	            throw new ForClosureReqDetailsNotFoundException(e.getMessage());
	        } catch (Exception e) {

	            response.setHttpStatus(HttpStatus.INTERNAL_SERVER_ERROR);
	            response.setResponseCode(HttpStatus.INTERNAL_SERVER_ERROR.value());
	            response.setResponseMessage(HttpStatus.INTERNAL_SERVER_ERROR.getReasonPhrase());
	            response.setResponseMessage(e.getMessage());
	            throw new ForClosureReqDetailsInternalServerError(e.getMessage());
	        }

	        return response;
	    }
	 
	 @GetMapping(value = "/getdrawdownreqdetails", produces = MediaType.APPLICATION_JSON_VALUE)
	    public FourFinResponse<DrawDownReqDetailsResponseDto> getDrawDownReqDetails(@RequestParam String customerId, @RequestParam String mastAgrId, @RequestParam String reqId) throws Exception {
	        FourFinResponse<DrawDownReqDetailsResponseDto> response = new FourFinResponse<>();
	        try {
	        	DrawDownReqDetailsResponseDto drawDownReqDetailsResponseDto = srService.getDrawDownReqDetails(customerId, mastAgrId, reqId);
	            if(drawDownReqDetailsResponseDto != null) {
	                response.setHttpStatus(HttpStatus.OK);
	                response.setResponseCode(HttpStatus.OK.value());
	                response.setData(drawDownReqDetailsResponseDto);
	            } else {
	                response.setHttpStatus(HttpStatus.NOT_FOUND);
	                response.setResponseCode(HttpStatus.NOT_FOUND.value());
	                response.setResponseMessage(HttpStatus.NOT_FOUND.getReasonPhrase());
	                response.setData(null);
	            }
	        } catch (DrawDownReqDetailsNotFoundException e) {

	            response.setHttpStatus(HttpStatus.NOT_FOUND);
	            response.setResponseCode(HttpStatus.NOT_FOUND.value());
	            response.setResponseMessage(HttpStatus.NOT_FOUND.getReasonPhrase());
	            response.setResponseMessage(e.getMessage());
	            throw new DrawDownReqDetailsNotFoundException(e.getMessage());
	        } catch (Exception e) {

	            response.setHttpStatus(HttpStatus.INTERNAL_SERVER_ERROR);
	            response.setResponseCode(HttpStatus.INTERNAL_SERVER_ERROR.value());
	            response.setResponseMessage(HttpStatus.INTERNAL_SERVER_ERROR.getReasonPhrase());
	            response.setResponseMessage(e.getMessage());
	            throw new DrawDownReqDetailsInternalServerError(e.getMessage());
	        }

	        return response;
	    }

	 @GetMapping(value = "/getdrawdownupdatereqdetails", produces = MediaType.APPLICATION_JSON_VALUE)
	    public FourFinResponse<DrawDownUpdateReqDetailsResponseDto> getDrawDownUpdateReqDetails(@RequestParam String customerId, @RequestParam String mastAgrId, @RequestParam String reqId) throws Exception {
	        FourFinResponse<DrawDownUpdateReqDetailsResponseDto> response = new FourFinResponse<>();
	        try {
	        	DrawDownUpdateReqDetailsResponseDto drawDownUpdateReqDetailsResponseDto = srService.getDrawDownUpdateReqDetails(customerId, mastAgrId, reqId);
	            if(drawDownUpdateReqDetailsResponseDto != null) {
	                response.setHttpStatus(HttpStatus.OK);
	                response.setResponseCode(HttpStatus.OK.value());
	                response.setData(drawDownUpdateReqDetailsResponseDto);
	            } else {
	                response.setHttpStatus(HttpStatus.NOT_FOUND);
	                response.setResponseCode(HttpStatus.NOT_FOUND.value());
	                response.setResponseMessage(HttpStatus.NOT_FOUND.getReasonPhrase());
	                response.setData(null);
	            }
	        } catch (DrawDownUpdateReqDetailsNotFoundException e) {

	            response.setHttpStatus(HttpStatus.NOT_FOUND);
	            response.setResponseCode(HttpStatus.NOT_FOUND.value());
	            response.setResponseMessage(HttpStatus.NOT_FOUND.getReasonPhrase());
	            response.setResponseMessage(e.getMessage());
	            throw new DrawDownUpdateReqDetailsNotFoundException(e.getMessage());
	        } catch (Exception e) {

	            response.setHttpStatus(HttpStatus.INTERNAL_SERVER_ERROR);
	            response.setResponseCode(HttpStatus.INTERNAL_SERVER_ERROR.value());
	            response.setResponseMessage(HttpStatus.INTERNAL_SERVER_ERROR.getReasonPhrase());
	            response.setResponseMessage(e.getMessage());
	            throw new DrawDownUpdateReqDetailsInternalServerError(e.getMessage());
	        }

	        return response;
	    }
	  
	 @GetMapping(value = "/getdrawdowndisbupdatereqdetails", produces = MediaType.APPLICATION_JSON_VALUE)
	    public FourFinResponse<DrawDownDisbUpdateReqDetailsResponseDto> getDrawDownDisbUpdateReqDetails(@RequestParam String customerId, @RequestParam String mastAgrId, @RequestParam String reqId) throws Exception {
	        FourFinResponse<DrawDownDisbUpdateReqDetailsResponseDto> response = new FourFinResponse<>();
	        try {
	        	DrawDownDisbUpdateReqDetailsResponseDto drawDownDisbUpdateReqDetailsResponseDto = srService.getDrawDownDisbUpdateReqDetails(customerId, mastAgrId, reqId);
	            if(drawDownDisbUpdateReqDetailsResponseDto != null) {
	                response.setHttpStatus(HttpStatus.OK);
	                response.setResponseCode(HttpStatus.OK.value());
	                response.setData(drawDownDisbUpdateReqDetailsResponseDto);
	            } else {
	                response.setHttpStatus(HttpStatus.NOT_FOUND);
	                response.setResponseCode(HttpStatus.NOT_FOUND.value());
	                response.setResponseMessage(HttpStatus.NOT_FOUND.getReasonPhrase());
	                response.setData(null);
	            }
	        } catch (DrawDownDisbUpdateReqDetailsNotFoundException e) {

	            response.setHttpStatus(HttpStatus.NOT_FOUND);
	            response.setResponseCode(HttpStatus.NOT_FOUND.value());
	            response.setResponseMessage(HttpStatus.NOT_FOUND.getReasonPhrase());
	            response.setResponseMessage(e.getMessage());
	            throw new DrawDownDisbUpdateReqDetailsNotFoundException(e.getMessage());
	        } catch (Exception e) {

	            response.setHttpStatus(HttpStatus.INTERNAL_SERVER_ERROR);
	            response.setResponseCode(HttpStatus.INTERNAL_SERVER_ERROR.value());
	            response.setResponseMessage(HttpStatus.INTERNAL_SERVER_ERROR.getReasonPhrase());
	            response.setResponseMessage(e.getMessage());
	            throw new DrawDownDisbUpdateReqDetailsInternalServerError(e.getMessage());
	        }

	        return response;
	    }

		@GetMapping(value = "/getPartPaymentOnlineReqDetails", produces = MediaType.APPLICATION_JSON_VALUE)
	    public FourFinResponse<PartPaymentDetailsResponseDto> getPartPaymentOnlineReqDetails(@RequestParam Integer reqId) throws Exception {
	        FourFinResponse<PartPaymentDetailsResponseDto> response = new FourFinResponse<>();
	        try {
	        	PartPaymentDetailsResponseDto partPaymentDetailsResponseDto = srService.getPartPaymentOnlineReqDetails(reqId);
	            if(partPaymentDetailsResponseDto != null) {
	                response.setHttpStatus(HttpStatus.OK);
	                response.setResponseCode(HttpStatus.OK.value());
	                response.setData(partPaymentDetailsResponseDto);
	            } else {
	                response.setHttpStatus(HttpStatus.NOT_FOUND);
	                response.setResponseCode(HttpStatus.NOT_FOUND.value());
	                response.setResponseMessage(HttpStatus.NOT_FOUND.getReasonPhrase());
	                response.setData(null);
	            }
	        } catch (DrawDownUpdateReqDetailsNotFoundException e) {

	            response.setHttpStatus(HttpStatus.NOT_FOUND);
	            response.setResponseCode(HttpStatus.NOT_FOUND.value());
	            response.setResponseMessage(HttpStatus.NOT_FOUND.getReasonPhrase());
	            response.setResponseMessage(e.getMessage());
	            throw new DrawDownUpdateReqDetailsNotFoundException(e.getMessage());
	        } catch (Exception e) {

	            response.setHttpStatus(HttpStatus.INTERNAL_SERVER_ERROR);
	            response.setResponseCode(HttpStatus.INTERNAL_SERVER_ERROR.value());
	            response.setResponseMessage(HttpStatus.INTERNAL_SERVER_ERROR.getReasonPhrase());
	            response.setResponseMessage(e.getMessage());
	            throw new DrawDownUpdateReqDetailsInternalServerError(e.getMessage());
	        }
	        return response;
	    }
	  
		 @GetMapping(value = "/getallTransactionsRequestByActivityCode", produces = MediaType.APPLICATION_JSON_VALUE)
		    public FourFinResponse<RequestedStatusCountDto> getAllTransactionsByActivityCode(@RequestParam String activityCode) throws Exception {
		        FourFinResponse<RequestedStatusCountDto> response = new FourFinResponse<>();
		        try {
		        	RequestedStatusCountDto allSRResDto = srService.getAllTransactionsByActivityCode(activityCode);
		            if(allSRResDto != null) {
		                response.setHttpStatus(HttpStatus.OK);
		                response.setResponseCode(HttpStatus.OK.value());
		                response.setData(allSRResDto);
		            } else {
		                response.setHttpStatus(HttpStatus.NOT_FOUND);
		                response.setResponseCode(HttpStatus.NOT_FOUND.value());
		                response.setResponseMessage(HttpStatus.NOT_FOUND.getReasonPhrase());
		                response.setData(null);
		            }
		        } catch (SrWorklistNotFoundException e) {

		            response.setHttpStatus(HttpStatus.NOT_FOUND);
		            response.setResponseCode(HttpStatus.NOT_FOUND.value());
		            response.setResponseMessage(HttpStatus.NOT_FOUND.getReasonPhrase());
		            response.setResponseMessage(e.getMessage());
		            throw new SrWorklistNotFoundException(e.getMessage());
		        } catch (Exception e) {

		            response.setHttpStatus(HttpStatus.INTERNAL_SERVER_ERROR);
		            response.setResponseCode(HttpStatus.INTERNAL_SERVER_ERROR.value());
		            response.setResponseMessage(HttpStatus.INTERNAL_SERVER_ERROR.getReasonPhrase());
		            response.setResponseMessage(e.getMessage());
		            throw new SrWorklistInternalServerError(e.getMessage());
		        }

		        return response;
		    }
		 
		//------ API to get DebitNot requests list by status, date and reqId
		//------ There are four parameters statusList, date, reqId, activityCode(this is required) with pagination
			 @GetMapping(value = "/getDebitNoteReqList", produces = MediaType.APPLICATION_JSON_VALUE)
			    public ResponseEntity<List<DebitNoteReqDetailsResponseDto>> getDebitNoteReqList( @RequestParam (required = false) List<String> status,
			    		@RequestParam String activityCode , 
			    		@RequestParam(required = false, defaultValue = "0") Integer reqNo,
			    		@RequestParam (required = false, defaultValue = "null") String date,
			    		@RequestParam(required = false, defaultValue = "0") Integer pageNo,
			            @RequestParam(required = false, defaultValue = "10") Integer pageSize,
			            @RequestParam(required = false, defaultValue = "reqId") String sortBy,
			            @RequestParam(required = false, defaultValue = "asc") String sortDir) throws Exception {
			        FourFinResponse<List<DebitNoteReqDetailsResponseDto>> response = new FourFinResponse<>();
			        try {
			        	
			        	// Create pageable object
			            Sort.Direction direction = sortDir.equalsIgnoreCase("desc") ? 
			                Sort.Direction.DESC : Sort.Direction.ASC;
			            Pageable pageable = PageRequest.of(pageNo, pageSize, Sort.by(direction, sortBy));
			        	
			            Page<DebitNoteReqDetailsResponseDto> pageResult = srService.getDebitNoteReqList(status,activityCode, reqNo, date, pageable);
			        	
			            
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
}
