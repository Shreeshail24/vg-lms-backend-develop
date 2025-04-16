package com.samsoft.lms.newux.dto.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.util.List;

import com.samsoft.lms.customer.dto.CustomerAddressDto;
import com.samsoft.lms.customer.dto.CustomerBasicInfoDto;
import com.samsoft.lms.customer.dto.CustomerContactDto;

@Data
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class CustomerInfoResDto {

    private CustomerBasicInfoDto customerBasicInfo;
    private CustomerContactDto customerContactDetails;
    private List<CustomerAddressDto> customerAddressDetails;

}
