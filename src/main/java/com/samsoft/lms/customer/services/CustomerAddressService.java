package com.samsoft.lms.customer.services;

import java.util.ArrayList;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.samsoft.lms.core.services.CommonServices;
import com.samsoft.lms.customer.dto.CustomerAddressDto;
import com.samsoft.lms.customer.dto.CustomerAddressTypeDto;
import com.samsoft.lms.customer.entities.AgrCustAddress;
import com.samsoft.lms.customer.exceptions.CustomerDataNotFoundException;
import com.samsoft.lms.customer.repositories.AgrCustAddressRepository;

@Service
public class CustomerAddressService {

	@Autowired
	private AgrCustAddressRepository addrRepo;
	
	@Autowired
	private CommonServices commService;

	@Transactional(rollbackFor = { RuntimeException.class, Error.class, Exception.class })
	public Set<CustomerAddressTypeDto> getCustomersAddressTypeList(String customerId) {
		Set<CustomerAddressTypeDto> setOfAddr = new LinkedHashSet<CustomerAddressTypeDto>();
		try {
			List<AgrCustAddress> listOfAddress = addrRepo.findByCustomerId(customerId);
			if (listOfAddress.size() <= 0) {
				throw new CustomerDataNotFoundException(
						"No address type information is available for customer id " + customerId);
			}

			
			for (AgrCustAddress address : listOfAddress) {
				CustomerAddressTypeDto addr = new CustomerAddressTypeDto();
				BeanUtils.copyProperties(address, addr);
				addr.setCustomer(address.getCustomer().getCustomerId());
				addr.setAddrType(address.getAddrType());
				addr.setAddrTypeDesc(commService.getDescription("ADDRESS_TYPE",address.getAddrType()));
				setOfAddr.add(addr);
			}
		} catch (CustomerDataNotFoundException e) {
			throw new CustomerDataNotFoundException(e.getMessage());
		} catch (Exception e) {
			throw e;
		}
		return setOfAddr;
	}

	@Transactional(rollbackFor = { RuntimeException.class, Error.class, Exception.class })
	public CustomerAddressDto getCustomerAddress(String customerId, String addrType) {
		CustomerAddressDto addr = new CustomerAddressDto();
		try {
			List<AgrCustAddress> addressList = addrRepo.findByCustomerIdAndAddrType(customerId, addrType);
			if (addressList.size() == 0) {
				throw new CustomerDataNotFoundException("No address information is available for customer id "
						+ customerId + " and address type " + addrType);
			}

			List<AgrCustAddress> distinctRowList = addressList.stream().distinct().collect(Collectors.toList());
			AgrCustAddress address = distinctRowList.get(0);
			BeanUtils.copyProperties(address, addr);
			addr.setAddrType(commService.getDescription("ADDR_TYPE",address.getAddrType()));
			addr.setCustomerId(address.getCustomer().getCustomerId());
		} catch (CustomerDataNotFoundException e) {
			throw new CustomerDataNotFoundException(e.getMessage());
		} catch (Exception e) {
			throw e;
		}
		return addr;
	}

	@Transactional(rollbackFor = { RuntimeException.class, Error.class, Exception.class })
	public List<CustomerAddressDto> getCustomerAddressByCustInternalId(Integer custInternalId) {
		List<CustomerAddressDto> addrList = new ArrayList<>();
		CustomerAddressDto addr = null;
		try {
			List<AgrCustAddress> addressList = addrRepo.findByCustomerCustInternalId(custInternalId);
			if (addressList.size() == 0) {
				throw new CustomerDataNotFoundException("No address information is available for customer id "
						+ custInternalId);
			}

			List<AgrCustAddress> distinctRowList = addressList.stream().distinct().collect(Collectors.toList());
			for (AgrCustAddress address : distinctRowList) {
				addr = new CustomerAddressDto();
				BeanUtils.copyProperties(address, addr);
				addr.setAddrType(commService.getDescription("ADDR_TYPE", address.getAddrType()));
				addr.setCustomerId(address.getCustomer().getCustomerId());
				addrList.add(addr);
			}
		} catch (CustomerDataNotFoundException e) {
			throw new CustomerDataNotFoundException(e.getMessage());
		} catch (Exception e) {
			throw e;
		}
		return addrList;
	}

}
