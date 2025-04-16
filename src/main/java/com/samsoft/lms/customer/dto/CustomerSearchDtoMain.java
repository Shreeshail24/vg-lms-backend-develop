package com.samsoft.lms.customer.dto;

import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CustomerSearchDtoMain {

	private Integer totalRows;
	private List<CustomerSearchDto> customerList;
//	public Integer getTotalRows() {
//		return totalRows;
//	}
//	public void setTotalRows(Integer totalRows) {
//		this.totalRows = totalRows;
//	}
//	public List<CustomerSearchDto> getCustomerList() {
//		return customerList;
//	}
//	public void setCustomerList(List<CustomerSearchDto> customerList) {
//		this.customerList = customerList;
//	}
}
