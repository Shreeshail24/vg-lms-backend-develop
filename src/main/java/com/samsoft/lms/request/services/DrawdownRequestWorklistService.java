package com.samsoft.lms.request.services;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import com.samsoft.lms.core.exceptions.CoreDataNotFoundException;
import com.samsoft.lms.core.repositories.AgrMasterAgreementRepository;
import com.samsoft.lms.core.services.CommonServices;
import com.samsoft.lms.customer.entities.AgrCustomer;
import com.samsoft.lms.customer.repositories.AgrCustomerRepository;
import com.samsoft.lms.newux.dto.response.GetLoanOverviewResDto;
import com.samsoft.lms.newux.services.LoanDetailsService;
import com.samsoft.lms.request.dto.AllDrawdownRequestIdDto;
import com.samsoft.lms.request.dto.AllDrawdownRequestIdMainDto;
import com.samsoft.lms.request.entities.DrawdownRequest;
import com.samsoft.lms.request.repositories.DrawDownRequestRepository;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

@Service
@Slf4j
public class DrawdownRequestWorklistService {

	@Autowired
	private DrawDownRequestRepository drawdownReqRepo;

	@Autowired
	private AgrCustomerRepository custRepo;

	@Autowired
	private CommonServices commonService;

	@Autowired
	private Environment env;

	@Autowired
	private AgrMasterAgreementRepository agrMasterAgreementRepository;

	@Autowired
	private LoanDetailsService loanDetailsService;

	public AllDrawdownRequestIdMainDto getAllDrawdownRequestsByMasterAgrId(String mastAgrId) throws Exception{
		try{
			String dateFormat = env.getProperty("lms.global.date.format");
			SimpleDateFormat sdf = new SimpleDateFormat(dateFormat);

			AllDrawdownRequestIdMainDto mainDto = new AllDrawdownRequestIdMainDto();
			List<AllDrawdownRequestIdDto> allAgreementDtoList = new ArrayList<AllDrawdownRequestIdDto>();

			List<DrawdownRequest> reqIdList = drawdownReqRepo.findByMastAgrId(mastAgrId);

			for (DrawdownRequest req : reqIdList) {
				log.info("Drawdown Master agreement " + req.getMastAgrId());
				List<AgrCustomer> customerList = custRepo.findByMasterAgrMastAgrIdOrderByDtUserDateDesc(req.getMastAgrId());

				log.info("customerList size " + customerList.size());

				GetLoanOverviewResDto getLoanOverviewResDto = loanDetailsService.getLoanOverview(req.getMastAgrId());

				log.info("MyLoanAccountService :: getODAndSFDetails :: getLoanOverviewResDto: {}", getLoanOverviewResDto);


				AllDrawdownRequestIdDto request = new AllDrawdownRequestIdDto();
				for (AgrCustomer customer : customerList) {
					if (customer.getCustomerType().equalsIgnoreCase("B")) {
						String name = "";
						if (customer.getFirstName() != null) {
							name = customer.getFirstName();
						}
						if (customer.getMiddleName() != null) {
							name = name + " " + customer.getMiddleName();
						}
						if (customer.getLastName() != null) {
							name = name + " " + customer.getLastName();
						}
						request.setCustomerName(name);
						request.setCustomerId(customer.getCustomerId());
					}
				}

				if (getLoanOverviewResDto != null) {
					request.setUtilizedLimit(getLoanOverviewResDto.getAgreementInfo().getUtilizedLimit());
					request.setAvailableLimit(getLoanOverviewResDto.getAgreementInfo().getAvailableLimit());
				}

				request.setRequestId(req.getRequestId());
				request.setMastAgrId(req.getMastAgrId());
				request.setLimitSanctionAmount(req.getLimitSanctionAmount());
				request.setTotalDues(req.getTotalDues());
				request.setTotalOverDues(req.getTotalOverDues());
				request.setRequestedAmount(req.getRequestedAmount());
				request.setApprovedAmount(req.getApprovedAmount());
				request.setRejectReasonCode(req.getRejectReasonCode());
				request.setRemarksRequest(req.getRemarksRequest());
				request.setRemarksApproval(req.getRemarksApproval());
				request.setUseridRequest(req.getUseridRequest());
				request.setUserIdDecision(req.getUserIdDecision());
				request.setStatus(req.getStatus());

				//BeanUtils.copyProperties(req, request);
				request.setRequestedDateTime(sdf.format(req.getRequestedDateTime()));

				allAgreementDtoList.add(request);

			}

			mainDto.setTotalRows(drawdownReqRepo.findAll().size());
			mainDto.setAllRequestDto(allAgreementDtoList);

			return mainDto;
		}catch (Exception e){
			e.printStackTrace();
			log.error("In method getAllDrawdownRequestsByMasterAgrId");
			throw e;
		}
	}

	public AllDrawdownRequestIdMainDto getAllDrawdownRequestIdSearch(String mastAgrId, Character status) {

		String dateFormat = env.getProperty("lms.global.date.format");
		SimpleDateFormat sdf = new SimpleDateFormat(dateFormat);

		AllDrawdownRequestIdMainDto mainDto = new AllDrawdownRequestIdMainDto();
		List<AllDrawdownRequestIdDto> allAgreementDtoList = new ArrayList<AllDrawdownRequestIdDto>();
		
		DrawdownRequest req = drawdownReqRepo.findFirstByMastAgrIdAndStatusOrderByRequestIdDesc(mastAgrId, status);

		if(req == null) {
			throw new CoreDataNotFoundException("Master Agreement not found");
		}
		
		log.info("Drawdown Master agreement " + req.getMastAgrId());
		List<AgrCustomer> customerList = custRepo.findByMasterAgrMastAgrIdOrderByDtUserDateDesc(req.getMastAgrId());

		log.info("customerList size " + customerList.size());

		AllDrawdownRequestIdDto request = new AllDrawdownRequestIdDto();
		for (AgrCustomer customer : customerList) {
			if (customer.getCustomerType().equalsIgnoreCase("B")) {
				String name = "";
				if (customer.getFirstName() != null) {
					name = customer.getFirstName();
				}
				if (customer.getMiddleName() != null) {
					name = name + " " + customer.getMiddleName();
				}
				if (customer.getLastName() != null) {
					name = name + " " + customer.getLastName();
				}
				request.setCustomerName(name);
				request.setCustomerId(customer.getCustomerId());
			}
		}

		BeanUtils.copyProperties(req, request);
		request.setRequestedDateTime(sdf.format(req.getRequestedDateTime()));

		allAgreementDtoList.add(request);

		//mainDto.setTotalRows(drawdownReqRepo.findByStatus(status).size());
		mainDto.setAllRequestDto(allAgreementDtoList);

		return mainDto;
	}

	public AllDrawdownRequestIdMainDto getAllDrawdownRequestId(Character status, Integer pageNo, Integer pageSize) {

		String dateFormat = env.getProperty("lms.global.date.format");
		SimpleDateFormat sdf = new SimpleDateFormat(dateFormat);

		AllDrawdownRequestIdMainDto mainDto = new AllDrawdownRequestIdMainDto();
		List<AllDrawdownRequestIdDto> allAgreementDtoList = new ArrayList<AllDrawdownRequestIdDto>();
		Pageable paging = PageRequest.of(pageNo, pageSize);
		List<DrawdownRequest> reqIdList = new ArrayList<>();
		if(status.equals('C')){
			Page<DrawdownRequest> reqIdLis = drawdownReqRepo.findAll(paging);
			reqIdList = reqIdLis.getContent();
		}else{
			reqIdList = drawdownReqRepo.findByStatus(status, paging);
		}
	//	List<DrawdownRequest> reqIdList = drawdownReqRepo.findByStatus(status, paging);
		Integer allCount = 0;
		Integer pndAppCount = 0;
		Integer rejCount = 0;
		Integer pndDisbCount = 0;
		Integer disbCount = 0;

		for (DrawdownRequest req : reqIdList) {

			log.info("Drawdown Master agreement " + req.getMastAgrId());
			List<AgrCustomer> customerList = custRepo.findByMasterAgrMastAgrIdOrderByDtUserDateDesc(req.getMastAgrId());

			log.info("customerList size " + customerList.size());

			AllDrawdownRequestIdDto request = new AllDrawdownRequestIdDto();
			for (AgrCustomer customer : customerList) {
				if (customer.getCustomerType().equalsIgnoreCase("B")) {
					String name = "";
					if (customer.getFirstName() != null) {
						name = customer.getFirstName();
					}
					if (customer.getMiddleName() != null) {
						name = name + " " + customer.getMiddleName();
					}
					if (customer.getLastName() != null) {
						name = name + " " + customer.getLastName();
					}
					request.setCustomerName(name);
					request.setCustomerId(customer.getCustomerId());
				}
			}

			BeanUtils.copyProperties(req, request);
			request.setRequestedDateTime(sdf.format(req.getRequestedDateTime()));

			allAgreementDtoList.add(request);

		}

		mainDto.setTotalRows(drawdownReqRepo.findByStatus(status).size());

		allCount = drawdownReqRepo.getDrawdownRequestCount();
		mainDto.setAllCount(allCount);

		pndAppCount = drawdownReqRepo.getDrawdownRequestCountByStatus('Q');
		mainDto.setPendAppCount(pndAppCount);

		rejCount = drawdownReqRepo.getDrawdownRequestCountByStatus('R');
		mainDto.setRejCount(rejCount);

		pndDisbCount = drawdownReqRepo.getDrawdownRequestCountByStatus('A');
		mainDto.setPndDisbCount(pndDisbCount);

		disbCount = drawdownReqRepo.getDrawdownRequestCountByStatus('D');
		mainDto.setDisbCount(disbCount);
		mainDto.setAllRequestDto(allAgreementDtoList);

		return mainDto;
	}

	public  List<DrawdownRequest> getDrawdownRequestListByMasterAgrIdAndStatus(String masterAgrId, Character status) throws Exception{
		try{
			List<DrawdownRequest> reqIdList = drawdownReqRepo.findByMastAgrIdAndStatus(masterAgrId, status);
			return reqIdList;
		}catch (Exception e){
			e.printStackTrace();
			log.error("In method getDrawdownRequestList");
			throw e;
		}
	}
}
