package com.samsoft.lms.approvalSettings.repositories;

import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.samsoft.lms.approvalSettings.entities.ApprovalSetting;

@Repository
public interface ApprovalSettingRepository extends JpaRepository<ApprovalSetting, Integer> {

   public ApprovalSetting findByApprovalSettingId(Integer approvalSettingId);
   
   public List <ApprovalSetting> findByRequestType(String RequestType);
   
   public List <ApprovalSetting> findAll ();
   
    @Modifying
	@Transactional
	Integer deleteByApprovalSettingId(Integer approvalSettingId);
	

   
}
