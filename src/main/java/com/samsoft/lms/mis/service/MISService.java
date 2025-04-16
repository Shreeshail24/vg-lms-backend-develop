package com.samsoft.lms.mis.service;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.samsoft.lms.mis.dto.CollectionDownloadDto;
import com.samsoft.lms.mis.entity.CollectionDownload;
import com.samsoft.lms.mis.repository.CollectionDownloadRepository;

import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
public class MISService {
	@Autowired
	private CollectionDownloadRepository collectionDownloadRepository;

	public List<CollectionDownloadDto> getCollecttionData() throws Exception {
		try {
			List<CollectionDownload> collectionList = collectionDownloadRepository.findAll();
			List<CollectionDownloadDto> collectionDownloadDtoList = new ArrayList<CollectionDownloadDto>();
			for (CollectionDownload collectionDownload : collectionList) {
				CollectionDownloadDto collectionDownloadDto = new CollectionDownloadDto();

				collectionDownloadDto.setMastAgrId(collectionDownload.getMastAgrId());
				collectionDownloadDto.setCustomerId(collectionDownload.getCustomerId());
				collectionDownloadDto.setCustomerName(collectionDownload.getCustomerName());
				collectionDownloadDto.setMobileNo(collectionDownload.getMobileNo());
				collectionDownloadDto.setEmailId(collectionDownload.getEmailId());
				collectionDownloadDto.setLoanAmount(collectionDownload.getLoanAmount());
				collectionDownloadDto.setAddressLine1(collectionDownload.getAddressLine1());
				collectionDownloadDto.setAddressLine2(collectionDownload.getAddressLine2());
				collectionDownloadDto.setCity(collectionDownload.getCity());
				collectionDownloadDto.setState(collectionDownload.getState());
				collectionDownloadDto.setCountry(collectionDownload.getCountry());
				collectionDownloadDto.setPincode(collectionDownload.getPincode());
				collectionDownloadDto.setNbfc(collectionDownload.getNbfc());
				collectionDownloadDto.setLoanId(collectionDownload.getLoanId());

				if (collectionDownload.getInstallmentDate() != null) {
					collectionDownloadDto.setInstallmentDate(
							new SimpleDateFormat("dd-MM-yyyy").format(collectionDownload.getInstallmentDate()));
				}

				collectionDownloadDto.setInstallmentNo(collectionDownload.getInstallmentNo());
				collectionDownloadDto.setInstallmentAmount(collectionDownload.getInstallmentAmount());

				if (collectionDownload.getPrevInstallment() != null) {
					collectionDownloadDto.setPrevInstallment(
							new SimpleDateFormat("dd-MM-yyyy").format(collectionDownload.getPrevInstallment()));
				}
				collectionDownloadDto.setIntersetRate(collectionDownload.getIntersetRate());
				collectionDownloadDto.setTenor(collectionDownload.getTenor());
				collectionDownloadDto.setBankCode(collectionDownload.getBankCode());
				collectionDownloadDto.setAccountNo("\'" + collectionDownload.getAccountNo());

				if (collectionDownload.getDisbDate() != null) {
					collectionDownloadDto.setDisbDate(
							new SimpleDateFormat("dd-MM-yyyy").format(collectionDownload.getDisbDate()));
				}
				collectionDownloadDto.setOriginationApplnNo(collectionDownload.getOriginationApplnNo());


//				BeanUtils.copyProperties(collectionDownload, collectionDownloadDto);
				collectionDownloadDtoList.add(collectionDownloadDto);
			}
			return collectionDownloadDtoList;
		} catch (Exception e) {
			log.info("Exception occured in getCollecttionData():" + e);
			throw e;
		}
	}
}
