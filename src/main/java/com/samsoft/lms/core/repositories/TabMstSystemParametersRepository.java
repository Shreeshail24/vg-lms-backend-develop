package com.samsoft.lms.core.repositories;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.samsoft.lms.core.entities.TabMstLookups;
import com.samsoft.lms.core.entities.TabMstSystemParameters;

@Repository
public interface TabMstSystemParametersRepository  extends JpaRepository<TabMstSystemParameters, Integer> { 

	List<TabMstSystemParameters> findAllBysysParaCode(String SysParaCode);
	
	TabMstSystemParameters findBysysParaCode(String SysParaCode);
}
