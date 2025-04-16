package com.samsoft.lms.core.dto;

//import lombok.AllArgsConstructor;
import lombok.Data;
//import lombok.NoArgsConstructor;

@Data
//@NoArgsConstructor
//@AllArgsConstructor

public class DistinctDueHeadDto {
	
//	public DistinctDueHeadDto() {
//		super();
//		// TODO Auto-generated constructor stub
//	}

	private String dueHead;

	public String getDueHead() {
		return dueHead;
	}

	public void setDueHead(String dueHead) {
		this.dueHead = dueHead;
	}

	public String getDueCategory() {
		return dueCategory;
	}

	public void setDueCategory(String dueCategory) {
		this.dueCategory = dueCategory;
	}

	private String dueCategory;

	public DistinctDueHeadDto(String dueHead, String dueCategory) {
		super();
		this.dueHead = dueHead;
		this.dueCategory = dueCategory;
	}	

}
