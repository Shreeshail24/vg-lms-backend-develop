package com.samsoft.lms.core.entities;

import java.time.LocalDate;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import lombok.ToString;

@Entity
@Table(name = "agr_trn_event_dtl")
@Data
@AllArgsConstructor
@RequiredArgsConstructor
@ToString
public class AgrTrnEventDtl {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "nEventId", length = 20)
	private Integer eventId;

	@Column(name = "sTranEvent", length = 50)
	private String tranEvent;

	@Column(name = "nTranAmount", length = 20)
	private Double tranAmount;

	@Column(name = "nGLGeneratedYN", length = 2, columnDefinition = "varchar(2) default 'N'")
	private String glGeneratedYn;

	@Column(name = "sUserID", length = 200)
	private String userId;

	@Column(name = "dLastUpdated")
	
	private LocalDate dtLastUpdated = LocalDate.now();

	@Column(name = "dUserDate")
	
	private LocalDate dtUserDate = LocalDate.now();

	@OneToMany(cascade = CascadeType.ALL, mappedBy = "eventDtl", fetch = FetchType.LAZY)
	private List<AgrTrnTranDetail> tranDtl;

	@ManyToOne(cascade = CascadeType.ALL, fetch = FetchType.LAZY)
	@JoinColumn(name = "nTranId")
	private AgrTrnTranHeader tranHeader;

}
