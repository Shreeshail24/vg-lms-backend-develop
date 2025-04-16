package com.samsoft.lms.request.repositories;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.samsoft.lms.request.entities.DrawdownRequest;

import java.util.List;

public interface DrawDownRequestRepository extends JpaRepository<DrawdownRequest, Integer> {

	public DrawdownRequest findByRequestId(Integer requestId);

	public List<DrawdownRequest> findByStatus(Character status);

	public List<DrawdownRequest> findByStatus(Character status, Pageable pageable);

	public DrawdownRequest findFirstByMastAgrIdOrderByRequestIdDesc(String mastAgrId);
	
	public DrawdownRequest findFirstByMastAgrIdAndStatusOrderByRequestIdDesc(String mastAgrId, Character status);
	
	public List<DrawdownRequest> findByMastAgrIdAndStatus(String mastAgrId, Character status);

	public List<DrawdownRequest> findByMastAgrId(String mastAgrId);

	@Query(value = "select count(*) from lms_t_drawdown_requests", nativeQuery = true)
	Integer getDrawdownRequestCount();

	@Query(value = "select count(*) from lms_t_drawdown_requests where cstatus = :status", nativeQuery = true)
	Integer getDrawdownRequestCountByStatus(@Param("status") Character status);

	public Page<DrawdownRequest> findAll(Pageable pageable);

}
