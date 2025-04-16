package com.samsoft.lms.instrument.repositories;

import java.util.Date;
import java.util.List;

import javax.transaction.Transactional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.samsoft.lms.batch.dto.GetSumAmountDto;
import com.samsoft.lms.instrument.entities.TrnInsBatchHdr;
import com.samsoft.lms.instrument.entities.TrnInsInstrument;
import com.samsoft.lms.request.entities.AgrTrnReqInstrument;
import com.samsoft.lms.request.entities.AgrTrnRequestHdr;

@Repository
public interface TrnInsInstrumentRepository extends JpaRepository<TrnInsInstrument, Integer> {

	@Transactional
	List<TrnInsInstrument> findByDepositBankIfscAndInstrumentTypeAndInstrumentStatusAndDtInstrumentDateLessThanOrderByInstrumentId(
			String depositBankIfsc, String instrumenType, String instrumentStatus, Date businessDate);
	
	List<TrnInsInstrument> findByDepositBankIfscAndInstrumentTypeAndInstrumentStatusAndDtInstrumentDateLessThanAndColenderIdOrderByInstrumentId(
			String depositBankIfsc, String instrumenType, String instrumentStatus, Date businessDate, String colenderId);

	TrnInsInstrument findByMasterAgrAndInstrumentNo(String masterAgreement, String instrumentNo);

	TrnInsInstrument findByMasterAgrAndInstrumentIdAndInstrumentStatus(String masterAgreement,
			Integer instrumentId, String instrumentStatus);

	TrnInsInstrument findAllByInstrumentNo(String instrumentNo);

	TrnInsInstrument findByInstrumentId(Integer instrumentId);

	List<TrnInsInstrument> findByCustomerIdAndPayAppliedYnAndPayBounceYnAndDtStatusUpdateAndInstrumentStatusIn(
			String customerId, String payAppliedYn, String payBounceYn, Date dtStatus, List<String> instrumentStatus);

	List<TrnInsInstrument> findByMasterAgrAndDtInstrumentDateGreaterThan(String masterAgreement,
			Date businessDate);	

	@Modifying
	@Transactional
	void deleteByMasterAgrAndDtInstrumentDateGreaterThan(String masterAgreement,
			Date businessDate);
		
	TrnInsInstrument findByUmrnAndInstrumentNo(String umrn, String instrumentNo);
	
	TrnInsInstrument findByUmrnAndInstrumentNoAndMasterAgr(String umrn, String instrumentNo, String mastAgrId);
	
	TrnInsInstrument findByUmrnAndInstrumentNoAndMasterAgrAndInstrumentStatusNotIn(String umrn, String instrumentNo, String mastAgrId, List<String> instrumentStatus);
	
	TrnInsInstrument findByInstrumentNoAndMasterAgrAndInstrumentStatusNotIn(String instrumentNo, String mastAgrId, List<String> instrumentStatus);

	
	TrnInsInstrument findByUmrnAndInstrumentId(String umrn, Integer instrumentId);
	
	List<TrnInsInstrument> findByMasterAgrAndInstrumentIdIn(String mastAgrId,List<Integer> instrumentIds);
	
	List<TrnInsInstrument> findByMasterAgrAndInstrumentStatusIn(String mastAgrId,List<String> instrumentStatus);

	TrnInsInstrument findByUmrnAndDtInstrumentDateAndMasterAgrAndInstrumentStatusNotIn(String umrn, Date instrumentDate, String mastAgrId, List<String> instrumentStatus);

	TrnInsInstrument findByDtInstrumentDateAndMasterAgrAndInstrumentStatusNotIn(Date instrumentDate, String mastAgrId, List<String> instrumentStatus);

	@Query(value="select new com.samsoft.lms.batch.dto.GetSumAmountDto( sum(instrumentAmount) as sumAmount) from TrnInsInstrument where masterAgr = :mastAgrId and payType = :payType and instrumentStatus = :instrumentStatus")
	GetSumAmountDto getSumAmount(@Param("mastAgrId") String mastAgrId, @Param("payType") String payType, @Param("instrumentStatus") String instrumentStatus);

	List<TrnInsInstrument> findBydtInstrumentDateBetween(Date fromDate, Date toDate);
	

	
}
