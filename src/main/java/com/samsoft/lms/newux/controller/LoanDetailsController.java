package com.samsoft.lms.newux.controller;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.samsoft.lms.agreement.exceptions.AgreementDataNotFoundException;
import com.samsoft.lms.agreement.exceptions.AgreementInternalServerError;
import com.samsoft.lms.agreement.services.AgreementService;
import com.samsoft.lms.core.dto.AgreementAmortListDto;
import com.samsoft.lms.core.dto.AgreementRepaymentListDto;
import com.samsoft.lms.core.dto.FourFinResponse;
import com.samsoft.lms.newux.dto.response.GetLoanDetailsResDto;
import com.samsoft.lms.newux.dto.response.GetLoanOverviewResDto;
import com.samsoft.lms.newux.dto.response.getloans.AllLoanDetailsResDto;
import com.samsoft.lms.newux.dto.response.getloans.GetAllLoansResDto;
import com.samsoft.lms.newux.exceptions.LoansInternalServerError;
import com.samsoft.lms.newux.exceptions.LoansNotFoundException;
import com.samsoft.lms.newux.services.LoanDetailsService;

@RestController
@RequestMapping(value = "/loandetails")
@CrossOrigin(origins = {"https://lms.4fin.in/","http://localhost:3000", "https://qa-lms.4fin.in", "https://qa-losone.4fin.in"} ,allowedHeaders = "*")
public class LoanDetailsController {

    @Autowired
    private LoanDetailsService loanDetailsService;

    @GetMapping(value = "/getAllLoans", produces = MediaType.APPLICATION_JSON_VALUE)
    public FourFinResponse<GetAllLoansResDto> getAllLoans(@RequestParam String type) throws Exception {
        FourFinResponse<GetAllLoansResDto> response = new FourFinResponse<>();
        try {
            GetAllLoansResDto res = loanDetailsService.getAllLoans(type);
            response.setHttpStatus(HttpStatus.OK);
            response.setResponseCode(HttpStatus.OK.value());
            response.setData(res);

        } catch (LoansNotFoundException e) {

            response.setHttpStatus(HttpStatus.NOT_FOUND);
            response.setResponseCode(HttpStatus.NOT_FOUND.value());
            response.setResponseMessage(HttpStatus.NOT_FOUND.getReasonPhrase());
            response.setResponseMessage(e.getMessage());
            throw new LoansNotFoundException(e.getMessage());
        } catch (Exception e) {

            response.setHttpStatus(HttpStatus.INTERNAL_SERVER_ERROR);
            response.setResponseCode(HttpStatus.INTERNAL_SERVER_ERROR.value());
            response.setResponseMessage(HttpStatus.INTERNAL_SERVER_ERROR.getReasonPhrase());
            response.setResponseMessage(e.getMessage());
            throw new LoansInternalServerError(e.getMessage());
        }

        return response;
    }

    @GetMapping(value = "/getLoanDetails", produces = MediaType.APPLICATION_JSON_VALUE)
    public FourFinResponse<GetLoanDetailsResDto> getLoanDetails(@RequestParam String mastAgrId) throws Exception {
        FourFinResponse<GetLoanDetailsResDto> response = new FourFinResponse<>();
        try {
            GetLoanDetailsResDto res = loanDetailsService.getLoanDetails(mastAgrId);
            if(res != null) {
                response.setHttpStatus(HttpStatus.OK);
                response.setResponseCode(HttpStatus.OK.value());
                response.setData(res);
            } else {
                response.setHttpStatus(HttpStatus.NOT_FOUND);
                response.setResponseCode(HttpStatus.NOT_FOUND.value());
                response.setResponseMessage(HttpStatus.NOT_FOUND.getReasonPhrase());
                response.setData(null);
            }
        } catch (AgreementDataNotFoundException e) {

            response.setHttpStatus(HttpStatus.NOT_FOUND);
            response.setResponseCode(HttpStatus.NOT_FOUND.value());
            response.setResponseMessage(HttpStatus.NOT_FOUND.getReasonPhrase());
            response.setResponseMessage(e.getMessage());
            throw new LoansNotFoundException(e.getMessage());
        } catch (Exception e) {

            response.setHttpStatus(HttpStatus.INTERNAL_SERVER_ERROR);
            response.setResponseCode(HttpStatus.INTERNAL_SERVER_ERROR.value());
            response.setResponseMessage(HttpStatus.INTERNAL_SERVER_ERROR.getReasonPhrase());
            response.setResponseMessage(e.getMessage());
            throw new AgreementInternalServerError(e.getMessage());
        }

        return response;
    }

    @GetMapping(value = "/getLoanOverview", produces = MediaType.APPLICATION_JSON_VALUE)
    public FourFinResponse<GetLoanOverviewResDto> getLoanOverview(@RequestParam String mastAgrId) throws Exception {
        FourFinResponse<GetLoanOverviewResDto> response = new FourFinResponse<>();
        try {
            GetLoanOverviewResDto res = loanDetailsService.getLoanOverview(mastAgrId);
            if(res != null) {
                response.setHttpStatus(HttpStatus.OK);
                response.setResponseCode(HttpStatus.OK.value());
                response.setData(res);
            } else {
                response.setHttpStatus(HttpStatus.NOT_FOUND);
                response.setResponseCode(HttpStatus.NOT_FOUND.value());
                response.setResponseMessage(HttpStatus.NOT_FOUND.getReasonPhrase());
                response.setData(null);
            }
        } catch (AgreementDataNotFoundException e) {

            response.setHttpStatus(HttpStatus.NOT_FOUND);
            response.setResponseCode(HttpStatus.NOT_FOUND.value());
            response.setResponseMessage(HttpStatus.NOT_FOUND.getReasonPhrase());
            response.setResponseMessage(e.getMessage());
            throw new LoansNotFoundException(e.getMessage());
        } catch (Exception e) {

            response.setHttpStatus(HttpStatus.INTERNAL_SERVER_ERROR);
            response.setResponseCode(HttpStatus.INTERNAL_SERVER_ERROR.value());
            response.setResponseMessage(HttpStatus.INTERNAL_SERVER_ERROR.getReasonPhrase());
            response.setResponseMessage(e.getMessage());
            throw new AgreementInternalServerError(e.getMessage());
        }

        return response;
    }

    @GetMapping(value = "/getLoansByMastAgrId", produces = MediaType.APPLICATION_JSON_VALUE)
    public FourFinResponse<AllLoanDetailsResDto> getLoansByMastAgrId(@RequestParam String mastAgrId) throws Exception {
        FourFinResponse<AllLoanDetailsResDto> response = new FourFinResponse<>();
        try {
            AllLoanDetailsResDto res = loanDetailsService.getLoansByMastAgrId(mastAgrId);
            response.setHttpStatus(HttpStatus.OK);
            response.setResponseCode(HttpStatus.OK.value());
            response.setData(res);

        } catch (LoansNotFoundException e) {

            response.setHttpStatus(HttpStatus.NOT_FOUND);
            response.setResponseCode(HttpStatus.NOT_FOUND.value());
            response.setResponseMessage(HttpStatus.NOT_FOUND.getReasonPhrase());
            response.setResponseMessage(e.getMessage());
            throw new LoansNotFoundException(e.getMessage());
        } catch (Exception e) {

            response.setHttpStatus(HttpStatus.INTERNAL_SERVER_ERROR);
            response.setResponseCode(HttpStatus.INTERNAL_SERVER_ERROR.value());
            response.setResponseMessage(HttpStatus.INTERNAL_SERVER_ERROR.getReasonPhrase());
            response.setResponseMessage(e.getMessage());
            throw new LoansInternalServerError(e.getMessage());
        }

        return response;
    }
    
    @GetMapping(value = "/getagreementrepaymentlist", produces = MediaType.APPLICATION_JSON_VALUE)
	public AgreementRepaymentListDto getAgreementRepaymentList(@RequestParam String masterAgreement) throws Exception {
		AgreementRepaymentListDto amortDto = new AgreementRepaymentListDto();
		try {
			amortDto = loanDetailsService.getAgreementRepaymentList(masterAgreement);

		} catch (AgreementDataNotFoundException e) {
			throw new AgreementDataNotFoundException(e.getMessage());
		} catch (Exception e) {
			throw e;
		}

		return amortDto;
	}
    
    @GetMapping(value = "/getallagreementrepaymentlist", produces = MediaType.APPLICATION_JSON_VALUE)
	public AgreementRepaymentListDto getAllAgreementRepaymentList(@RequestParam String originationApplnNo) throws Exception {
		AgreementRepaymentListDto amortDto = new AgreementRepaymentListDto();
		try {
			amortDto = loanDetailsService.getAllAgreementRepaymentList(originationApplnNo);
		} catch (AgreementDataNotFoundException e) {
			throw new AgreementDataNotFoundException(e.getMessage());
		} catch (Exception e) {
			throw e;
		}
		return amortDto;
	}
}
