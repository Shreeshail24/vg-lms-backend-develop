package com.samsoft.lms.core.entities;

import java.time.LocalDate;
import java.util.Date;
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

import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

import com.fasterxml.jackson.annotation.JsonIgnore;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "agr_trn_tran_header")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class AgrTrnTranHeader {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "nTranId", length = 20)
	private Integer tranId;

	@Column(name = "dTranDate")
	@Temporal(TemporalType.DATE)
	private Date tranDate;

	@Column(name = "sTranType", length = 20)
	private String tranType;

	@Column(name = "sRemark", length = 100)
	private String remark;

	@Column(name = "sReason", length = 100)
	private String reason;

	@Column(name = "sSource", length = 20)
	private String source;

	@Column(name = "sReqId", length = 10)
	private String reqId;

	@Column(name = "nRefTranID", length = 5)
	private Integer refTranId;

	@Column(name = "nInstrumentId", length = 3)
	private Integer intrumentId;

	@Column(name = "nSanctionedLimit", length = 20) // Added by DeepakG
	private Double sanctionedLimit;

	@Column(name = "sUserID", length = 200)
	private String userID;

	@Column(name = "dLastUpdated")
	private LocalDate dtLastUpdated = LocalDate.now();

	@Column(name = "dUserDate")
	private LocalDate dtUserDate = LocalDate.now();

	/*
	 * @OneToMany(cascade = CascadeType.ALL, mappedBy = "tranHeader", fetch =
	 * FetchType.LAZY) private List<AgrTrnTranDetail> tranDetails;
	 */

	@OneToMany(cascade = CascadeType.ALL, mappedBy = "tranHeader", fetch = FetchType.LAZY)
	private List<AgrTrnEventDtl> eventDetails;

	/*
	 * @ManyToOne(cascade = CascadeType.ALL, fetch = FetchType.LAZY)
	 * 
	 * @JoinColumn(name="sMastAgrId") private AgrMasterAgreement masterAgr;
	 */

	@ManyToOne(fetch = FetchType.LAZY, optional = false)
	@JoinColumn(name = "sMastAgrId", nullable = false)
	@OnDelete(action = OnDeleteAction.CASCADE)
	@JsonIgnore
	private AgrMasterAgreement masterAgr;
	
	@Column(name = "nGLGeneratedYN", length = 20)
	private String nGLGeneratedYN;

}
