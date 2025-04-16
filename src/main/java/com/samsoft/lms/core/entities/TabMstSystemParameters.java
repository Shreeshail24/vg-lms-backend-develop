package com.samsoft.lms.core.entities;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name="tab_mst_system_parameters")
@Data
@AllArgsConstructor
@NoArgsConstructor

public class TabMstSystemParameters {
	
	    @Id
	    @Column(name = "sSysParaCode", length = 200)
	    private String sysParaCode; // System Parameter Code (Primary Key)

	    @Column(name = "sSysParaDesc", length = 200)
	    private String sysParaDesc; // System Parameter Description

	    @Column(name = "sSysParaValue", length = 50)
	    private String sysParaValue; // System Parameter Value

	    @Column(name = "sUserConfigurableYN", length = 1)
	    private char userConfigurableYN; // User Configurable Y/N

	    @Column(name = "sActiveYN", length = 1)
	    private char activeYN; // Active Y/N

	    @Column(name = "sUserID", length = 10)
	    private String userID; // User ID

	    @Column(name="dUserDateTime")
		LocalDate dtUserDateTime = LocalDate.now();

	    @Column(name="dLastUpdated")
		private LocalDate dtLastUpdated = LocalDate.now();
		
}
