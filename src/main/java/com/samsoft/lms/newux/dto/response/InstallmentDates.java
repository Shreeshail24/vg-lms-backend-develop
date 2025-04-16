package com.samsoft.lms.newux.dto.response;

import lombok.Data;

@Data
public class InstallmentDates {

    private String installmentDate;

    public InstallmentDates(String installmentDate) {
        this.installmentDate = installmentDate;
    }
}
