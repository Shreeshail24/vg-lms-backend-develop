package com.samsoft.lms.core.entities;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
@Data
@NoArgsConstructor
@AllArgsConstructor
public class CustAppLimitAgenciesCompositeKey implements Serializable {

    private static final long serialVersionUID = 1L;

    private Integer agencyId;
    private Integer custApplLimitId;
}
