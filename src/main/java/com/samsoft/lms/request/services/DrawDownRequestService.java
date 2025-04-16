package com.samsoft.lms.request.services;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import com.samsoft.lms.agreement.entities.AgrDisbursement;
import com.samsoft.lms.agreement.repositories.AgrDisbursementRepository;
import com.samsoft.lms.agreement.services.AgreementService;
import com.samsoft.lms.agreement.services.DisbursementService;
import com.samsoft.lms.common.enums.ExceptionMessageEnum;
import com.samsoft.lms.core.dto.AgreementLimitSetupListDto;
import com.samsoft.lms.core.exceptions.InvalidInputException;
import com.samsoft.lms.core.repositories.*;
import com.samsoft.lms.customer.dto.CustomerBasicInfoDto;
import com.samsoft.lms.customer.services.CustomerServices;
import com.samsoft.lms.las.repositories.AgrCollateralSharesRepository;
import com.samsoft.lms.request.dto.*;
import com.samsoft.lms.request.entities.DrawdownRequest;
import com.samsoft.lms.request.repositories.AgrTrnRequestHdrRepository;
import com.samsoft.lms.request.repositories.AgrTrnRequestStatusRepository;
import com.samsoft.lms.request.repositories.DrawDownRequestRepository;
import com.samsoft.lms.util.PlatformUtils;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

@Service
@Slf4j
public class DrawDownRequestService {

	@Autowired
	private DrawDownRequestRepository downRequestRepository;

	@Autowired
	private AgreementService agrService;

	@Autowired
	private CustomerServices custService;

	@Autowired
	private DisbursementService disbursementService;

	@Autowired
	private AgrDisbursementRepository disbRepo;

	@Autowired
	private AgrMasterAgreementRepository agreementRepo;

	@Autowired
	private AgrTrnRequestHdrRepository reqHdrRepo;

	@Autowired
	private Environment env;

	@Autowired
	private AgrCustLimitSetupRepository agrCustLimitSetupRepository;

	@Autowired
	private AgrCollateralSharesRepository agrCollateralSharesRepository;

	@Autowired
	private AgrTrnRequestStatusRepository agrTrnRequestStatusRepository;

	@Autowired
	private AgrMasterAgreementRepository masterRepo;

	@Autowired
	private AgrTrnTranHeaderRepository agrTrnTranHeaderRepository;

	@Autowired
	private AgrLoansRepository agrLoansRepository;

	@Autowired
	private AgrTrnEventDtlRepository agrTrnEventDtlRepository;

	@Autowired
	private AgrTrnTranDetailsRepository agrTrnTranDetailsRepository;

	@Autowired
	private DrawDownRequestRepository drawdownRepo;

	@Autowired
	private PlatformUtils platFormUtils;

	public Boolean createDrawdownRequest(DrawDownRequestDto downRequestDto) throws Exception {
		try {

			if (downRequestDto == null) {
				return Boolean.FALSE;
			}

			DrawdownRequest drawdownRequest = new DrawdownRequest(downRequestDto.getMasterAggrId(),
					downRequestDto.getLimitSanAmount(), downRequestDto.getUtilizedLimit(),
					downRequestDto.getAvailableLimit(), downRequestDto.getTotalDues(),
					downRequestDto.getTotalOverDues(), downRequestDto.getRequestedAmount(),
					downRequestDto.getRemarksRequest(), new Date(), downRequestDto.getUserIdRequest(), 'Q',
					downRequestDto.getEndUse());

			drawdownRequest = downRequestRepository.save(drawdownRequest);

			log.info("DrawDownRequest saved ===> " + drawdownRequest);

		} catch (Exception e) {
			log.error("Method name : createDrawdownRequest()");
			log.error("Request Object : " + downRequestDto);
			e.printStackTrace();
			throw e;
		}
		return Boolean.TRUE;
	}

//    public Boolean createLasDrawdownRequest(DrawDownRequestDto downRequestDto) throws Exception {
//        try {
//
//            if (downRequestDto == null) {
//                return Boolean.FALSE;
//            }
//
//            DrawdownRequest drawdownRequest = new DrawdownRequest(downRequestDto.getMasterAggrId(),
//                    downRequestDto.getLimitSanAmount(), downRequestDto.getUtilizedLimit(),
//                    downRequestDto.getAvailableLimit(), downRequestDto.getTotalDues(),
//                    downRequestDto.getTotalOverDues(), downRequestDto.getRequestedAmount(),
//                    downRequestDto.getRemarksRequest(), new Date(), downRequestDto.getUserIdRequest(), 'Q',
//                    downRequestDto.getEndUse());
//
//            drawdownRequest = downRequestRepository.save(drawdownRequest);
//
//            log.info("DrawDownRequest saved ===> " + drawdownRequest);
//
//        } catch (Exception e) {
//            log.error("Method name : createDrawdownRequest()");
//            log.error("Request Object : " + downRequestDto);
//            e.printStackTrace();
//            throw e;
//        }
//        return Boolean.TRUE;
//    }

	public DrawDownGetRequestDto getDrawDownRequestDetail(DrawDownGetDto drawDownGetDto) throws Exception {

		DrawDownGetRequestDto downGetRequestDto = null;

		try {

			List<AgreementLimitSetupListDto> agreementLimitSetupListDto = agrService
					.getAgreementLimitList(drawDownGetDto.getMasterAgreement());

			if (agreementLimitSetupListDto == null) {
				return null;
			}

			CustomerBasicInfoDto customerList = custService.getCustomerByCustomerId(drawDownGetDto.getCustomerId());
			if (customerList == null) {
				return null;
			}

			DrawDownResponseDto downResponseDto = getDrawDownRequest(drawDownGetDto.getMasterAgreement());
			for (AgreementLimitSetupListDto agreementLimitSetupDto : agreementLimitSetupListDto) {
				downGetRequestDto = new DrawDownGetRequestDto(agreementLimitSetupDto, customerList, downResponseDto);

			}
		} catch (Exception e) {
			log.error("Method name : getDrawDownRequestDetail()");
			log.error("Request Object : " + drawDownGetDto);
			e.printStackTrace();
			throw e;
		}
		return downGetRequestDto;
	}

	public DrawDownResponseDto getDrawDownRequest(String masterAggrId) throws Exception {
		DrawDownResponseDto downResponseDto = null;
		try {
			String dateFormat = env.getProperty("lms.global.date.format");
			SimpleDateFormat sdf = new SimpleDateFormat(dateFormat);

			DrawdownRequest drawdownRequest = downRequestRepository
					.findFirstByMastAgrIdOrderByRequestIdDesc(masterAggrId);

			// log.info("drawdownRequest " + drawdownRequest.toString());
			if (drawdownRequest == null) {
				return null;
			}
			String decisiondate = "", requestdate = "";
			if (drawdownRequest.getRequestedDateTime() != null) {
				log.info("getRequestedDateTime " + drawdownRequest.getRequestedDateTime());
				requestdate = new SimpleDateFormat("dd-MM-yyyy HH:mm:ss")
						.format(drawdownRequest.getRequestedDateTime());
			}
			if (drawdownRequest.getDecisionDateTime() != null) {
				log.info("getDecisionDateTime " + drawdownRequest.getDecisionDateTime());

				decisiondate = new SimpleDateFormat("dd-MM-yyyy HH:mm:ss")
						.format(drawdownRequest.getDecisionDateTime());
			}

			log.info("Before Const ");
			if (drawdownRequest.getDtPrinStart() == null) {
				downResponseDto = new DrawDownResponseDto(drawdownRequest.getRequestId(),
						drawdownRequest.getMastAgrId(), drawdownRequest.getLimitSanctionAmount(),
						drawdownRequest.getUtilizedLimit(), drawdownRequest.getAvailableLimit(),
						drawdownRequest.getTotalDues(), drawdownRequest.getTotalOverDues(),
						drawdownRequest.getRequestedAmount(), drawdownRequest.getApprovedAmount(),
						drawdownRequest.getRejectReasonCode(), drawdownRequest.getRemarksRequest(),
						drawdownRequest.getRemarksApproval(), requestdate, decisiondate,
						drawdownRequest.getUseridRequest(), drawdownRequest.getUserIdDecision(),
						drawdownRequest.getStatus());
			} else {
				downResponseDto = new DrawDownResponseDto(drawdownRequest.getRequestId(),
						drawdownRequest.getMastAgrId(), drawdownRequest.getLimitSanctionAmount(),
						drawdownRequest.getUtilizedLimit(), drawdownRequest.getAvailableLimit(),
						drawdownRequest.getTotalDues(), drawdownRequest.getTotalOverDues(),
						drawdownRequest.getRequestedAmount(), drawdownRequest.getApprovedAmount(),
						drawdownRequest.getRejectReasonCode(), drawdownRequest.getRemarksRequest(),
						drawdownRequest.getRemarksApproval(), requestdate, decisiondate,
						drawdownRequest.getUseridRequest(), drawdownRequest.getUserIdDecision(),
						drawdownRequest.getStatus(), sdf.format(drawdownRequest
								.getDtPrinStart())/*
													 * , drawdownRequest.getIfsc(), drawdownRequest.getDisbAccNo(),
													 * drawdownRequest.getRepayFreq()
													 */);
			}

			log.info("DrawDownRequest Data : " + downResponseDto);
		} catch (Exception e) {
			log.error("Method name : getDrawDownRequest()");
			log.error("Request masterAggrId : " + masterAggrId);
			e.printStackTrace();
			throw e;
		}
		return downResponseDto;
	}

	public String updateDrawDownRequest(DrawDownUpdateDto downUpdateDto) throws Exception {
		String result = "success";
		try {
			String dateFormat = env.getProperty("lms.global.date.format");
			SimpleDateFormat sdf = new SimpleDateFormat(dateFormat);
			log.info("downUpdateDto===>" + downUpdateDto);
			if (downUpdateDto == null) {
				throw new InvalidInputException(ExceptionMessageEnum.INPUT_EMPTY.getMessage(), HttpStatus.BAD_REQUEST);
			}

			DrawdownRequest drawdownRequest = downRequestRepository.findByRequestId(downUpdateDto.getRequestId());
			if (drawdownRequest == null) {
				throw new InvalidInputException(ExceptionMessageEnum.DATA_NOT_FOUND.getMessage(),
						HttpStatus.BAD_REQUEST);
			}
			drawdownRequest.setRejectReasonCode(downUpdateDto.getRejectReasonCode());
			drawdownRequest.setRemarksApproval(downUpdateDto.getRemarksApproval());
			drawdownRequest.setUserIdDecision(downUpdateDto.getUserIdDecision());
			drawdownRequest.setStatus(downUpdateDto.getStatus());
			drawdownRequest.setDecisionDateTime(new Date());

			if (downUpdateDto.getStatus() == 'A') {
				Date parsedDate = sdf.parse(downUpdateDto.getDtPrinStart());
				log.info("parsedDate===>" + parsedDate);
				drawdownRequest.setDtPrinStart(parsedDate);
			}

			drawdownRequest.setIfsc(downUpdateDto.getDisbifscCode());
			drawdownRequest.setDisbAccNo(downUpdateDto.getDisbaccountNo());
			drawdownRequest.setRepayFreq(downUpdateDto.getRepayFreq());
			drawdownRequest.setApprovedAmount(downUpdateDto.getApprovedAmount());

			downRequestRepository.save(drawdownRequest);

			// Commented because disbursement is getting called on UTR update screen

			/*
			 * if (downUpdateDto.getStatus() == 'A') { if (approvedDrawdownRequest == null)
			 * { log.info("Ready for OD first disburstment..."); LoanDisbursementOdFirstDto
			 * loanDisbursementOdFirstDto = new LoanDisbursementOdFirstDto(
			 * downUpdateDto.getRepayFreq(), downUpdateDto.getUserIdDecision(), "DRAWDOWN");
			 *
			 * DisbursementOdFirstDto odFirstDto = new
			 * DisbursementOdFirstDto(downUpdateDto.getDtDisbDate(),
			 * downUpdateDto.getDtPrinStart(), downUpdateDto.getDisbAmount(),
			 * downUpdateDto.getDisbifscCode(), downUpdateDto.getDisbaccountNo(),
			 * downUpdateDto.getPaymentMode(), downUpdateDto.getUtrNo());
			 *
			 * DisbursementBoardingOdFirstDto boardingOdFirstDto = new
			 * DisbursementBoardingOdFirstDto( downUpdateDto.getMastAgrId(),
			 * loanDisbursementOdFirstDto, odFirstDto);
			 *
			 * result = disbursementService.disbursementOdFirstApi(boardingOdFirstDto); }
			 * else { log.info("Ready for OD subsequent disburstment...");
			 * LoanDisbursementOdSubsequentDto disbursementOdSubsequentDto = new
			 * LoanDisbursementOdSubsequentDto( downUpdateDto.getUserIdDecision());
			 * DisbursementOdSubsequentDto subsequentDto = new DisbursementOdSubsequentDto(
			 * downUpdateDto.getDtDisbDate(), downUpdateDto.getDisbAmount(),
			 * downUpdateDto.getDisbifscCode(), downUpdateDto.getDisbaccountNo(),
			 * downUpdateDto.getPaymentMode(), downUpdateDto.getUtrNo());
			 *
			 * DisbursementBoardingOdSubsequentDto boardingOdSubsequentDto = new
			 * DisbursementBoardingOdSubsequentDto( downUpdateDto.getMastAgrId(),
			 * disbursementOdSubsequentDto, subsequentDto); result =
			 * disbursementService.disbursementOdSubsequentApi(boardingOdSubsequentDto); } }
			 * else { log.info("Request rejected..."); }
			 */
			log.info("updateDrawDownRequest saved");
		} catch (Exception e) {
			log.error("Method name : updateDrawDownRequest()");
			log.error("Request Object : " + downUpdateDto);
			e.printStackTrace();
			throw e;
		}
		return result;
	}

	public String updateUtrNoAfterApproval(String mastAgrId, String utrNo) {
		String result = "success";
		try {
			log.info("Master Agreement {} and utrNo {} ", mastAgrId, utrNo);
			AgrDisbursement maxDisbDtl = disbRepo.findFirstByMastAgrMastAgrIdOrderByDisbIdDesc(mastAgrId);
			log.info("Max Disb Sr no is {} ", maxDisbDtl.getDisbId());

			maxDisbDtl.setUtrNo(utrNo);
			log.info("Before Save");

			disbRepo.save(maxDisbDtl);
			log.info("After Save");

		} catch (Exception e) {
			log.error(" Error updating utrNo {} ", e.getMessage());
			throw e;
		}
		return result;
	}

	public Double getAllPendingRequestsAmount(String masterAggrId) throws Exception {
		try {
			Double allPendingRequestsAmount = 0.0;
			List<DrawdownRequest> reqIdList = downRequestRepository.findByMastAgrIdAndStatus(masterAggrId, 'Q');
			for (DrawdownRequest obj : reqIdList) {
				allPendingRequestsAmount += obj.getRequestedAmount();
			}
			return allPendingRequestsAmount;
		} catch (Exception e) {
			log.error("Method name : getAllPendingRequestsAmount()");
			log.error("Request Object masterAggrId : {}", masterAggrId);
			e.printStackTrace();
			throw e;
		}
	}

//    @Transactional
//    public String updateLasDrawDownRequest(DrawDownUpdateDto downUpdateDto) throws Exception {
//        String result = "success";
//        try {
//            String dateFormat = env.getProperty("lms.global.date.format");
//            SimpleDateFormat sdf = new SimpleDateFormat(dateFormat);
//            log.info("downUpdateDto===>" + downUpdateDto);
//            if (downUpdateDto == null) {
//                throw new InvalidInputException(ExceptionMessageEnum.INPUT_EMPTY.getMessage(), HttpStatus.BAD_REQUEST);
//            }
//
//            DrawdownRequest drawdownRequest = downRequestRepository.findByRequestId(downUpdateDto.getRequestId());
//            if (drawdownRequest == null) {
//                throw new InvalidInputException(ExceptionMessageEnum.DATA_NOT_FOUND.getMessage(),
//                        HttpStatus.BAD_REQUEST);
//            }
//            drawdownRequest.setRejectReasonCode(downUpdateDto.getRejectReasonCode());
//            drawdownRequest.setRemarksApproval(downUpdateDto.getRemarksApproval());
//            drawdownRequest.setUserIdDecision(downUpdateDto.getUserIdDecision());
//            drawdownRequest.setStatus(downUpdateDto.getStatus());
//            drawdownRequest.setDecisionDateTime(new Date());
//
//            if (downUpdateDto.getStatus() == 'A') {
//                Date parsedDate = sdf.parse(downUpdateDto.getDtPrinStart());
//                log.info("parsedDate===>" + parsedDate);
//                drawdownRequest.setDtPrinStart(parsedDate);
//            }
//
//            drawdownRequest.setIfsc(downUpdateDto.getDisbifscCode());
//            drawdownRequest.setDisbAccNo(downUpdateDto.getDisbaccountNo());
//            drawdownRequest.setRepayFreq(downUpdateDto.getRepayFreq());
//            drawdownRequest.setApprovedAmount(downUpdateDto.getApprovedAmount());
//
//            downRequestRepository.save(drawdownRequest);
//
////            LasDrawDownRequestDto downRequestDto = new LasDrawDownRequestDto();
////            downRequestDto.setMasterAggrId(drawdownRequest.getMastAgrId());
////            downRequestDto.setRequestedAmount(drawdownRequest.getRequestedAmount());
////            downRequestDto.setRequestDate(downUpdateDto.getDtPrinStart());
////            this.updateLasDrawdownRequest(downRequestDto);
//
//        } catch (Exception e) {
//            log.error("Method name : updateDrawDownRequest()");
//            log.error("Request Object : " + downUpdateDto);
//            e.printStackTrace();
//            throw e;
//        }
//        return result;
//    }

//    @Transactional
//    public Boolean updateLasDrawDownUtr(LasDrawDownRequestDto downRequestDto) throws Exception {
//        try {
//
//            Double avlLimit = 0.0;
//            Double utilizedLimit = 0.0;
//
//            if (downRequestDto == null) {
//                return Boolean.FALSE;
//            }
//
//            String dateFormat = env.getProperty("lms.global.date.format");
//
//            Date requestDate = new SimpleDateFormat(dateFormat).parse(downRequestDto.getRequestDate());
//            SimpleDateFormat sdf = new SimpleDateFormat(dateFormat);
//
//            //
//            AgrCustLimitSetup agrCustLimitSetup = agrCustLimitSetupRepository.findByMasterAgreementMastAgrId(downRequestDto.getMasterAggrId());
//
//            if (agrCustLimitSetup != null) {
//
//                AgrCollateralShares agrCollateralShares = agrCollateralSharesRepository.findByMastAgrId(downRequestDto.getMasterAggrId());
//
//                if (agrCollateralShares != null) {
//
//                    if (agrCustLimitSetup.getUtilizedLimit() > agrCustLimitSetup.getAvailableLimit() && downRequestDto.getRequestedAmount() > agrCustLimitSetup.getAvailableLimit()) {
//                        //Trigger
//                        log.info("Utilized Limit > Avl Limit");
//                    } else {
//
//                        if (agrCollateralShares.getDrawingPower() > agrCustLimitSetup.getLimitSanctionAmount()) {
//
//                            //Update DrawingPower => LimitSanctionAmount
//                            agrCollateralShares.setDrawingPower(agrCustLimitSetup.getLimitSanctionAmount());
//                            agrCollateralShares = agrCollateralSharesRepository.save(agrCollateralShares);
//                            log.info("agrCollateralShares===> " + agrCollateralShares);
//
//                        }
//
//                        avlLimit = agrCustLimitSetup.getAvailableLimit() - downRequestDto.getRequestedAmount();
//                        utilizedLimit = downRequestDto.getRequestedAmount();
//
//                        log.info("avlLimit===> "+ avlLimit);
//                        log.info("utilizedLimit===> " + utilizedLimit);
//
//                        agrCustLimitSetup.setAvailableLimit(avlLimit);
//                        agrCustLimitSetup.setUtilizedLimit(utilizedLimit);
//                        agrCustLimitSetup = agrCustLimitSetupRepository.save(agrCustLimitSetup);
//                        log.info("agrCustLimitSetup===> " + agrCustLimitSetup);
//
//                        if(avlLimit == 0) {
//                            //Trigger
//                            platFormUtils.sendLasMail("aniket.jadhav@4fin.in", "Your Over Draft account has insufficient balance, For adding balance kindly click on the Proceed Button", "Insufficient Balance.", downRequestDto.getMasterAggrId());
//                        }
//                    }
//
//                }
//            }
//
//            AgrMasterAgreement master = masterRepo.findByMastAgrId(downRequestDto.getMasterAggrId());
//            if (master == null) {
//                throw new AgreementDataNotFoundException("Master Agreement not available in LMS.");
//
//            }
//
//            AgrTrnTranHeader hdr = new AgrTrnTranHeader();
//            hdr.setMasterAgr(master);
//            hdr.setTranDate(sdf.parse(downRequestDto.getRequestDate()));
//            hdr.setTranType("DISBURSEMENT");
//            hdr.setRemark("Drawdown Done.");
//            hdr.setSanctionedLimit(agrCustLimitSetup.getLimitSanctionAmount());
//            hdr.setSource("INTERFACE");
//            hdr.setUserID(agrCustLimitSetup.getUserId());
//            hdr = agrTrnTranHeaderRepository.save(hdr);
//
//            AgrTrnEventDtl event = new AgrTrnEventDtl();
//            event.setTranHeader(hdr);
//            event.setTranEvent("DISBURSEMENT");
//            event.setTranAmount(downRequestDto.getRequestedAmount());
//            event.setUserId(agrCustLimitSetup.getUserId());
//            agrTrnEventDtlRepository.save(event);
//
//            AgrTrnTranDetail detail = new AgrTrnTranDetail();
//            detail.setEventDtl(event);
//            detail.setMasterAgr(master);
//            List<AgrLoans> agrLoansList = agrLoansRepository.findByMasterAgreementMastAgrId(downRequestDto.getMasterAggrId());
//            for (AgrLoans loan : agrLoansList) {
//                detail.setLoan(loan);
//            }
//            detail.setTranCategory("DISBURSEMENT");
//            detail.setTranHead("DISB_AMT");
//            detail.setTranAmount(downRequestDto.getRequestedAmount());
//            detail.setTranSide("DR");
//            detail.setDtlRemark("Withdrawal - NEFT");
//            detail.setAvailableLimit(avlLimit);
//            detail.setUtilizedLimit(utilizedLimit);
//            agrTrnTranDetailsRepository.save(detail);
//
//            List<DrawdownRequest> drawDownList = drawdownRepo.findByMastAgrIdAndStatus(downRequestDto.getMasterAggrId(),
//                    'A');
//            for (DrawdownRequest drawDown : drawDownList) {
//                drawDown.setStatus('D');
//                drawdownRepo.save(drawDown);
//            }
//
//        } catch (Exception e) {
//            log.error("Method name : createDrawdownRequest()");
//            log.error("Request Object : " + downRequestDto);
//            e.printStackTrace();
//            throw e;
//        }
//        return Boolean.TRUE;
//    }

}
