package com.samsoft.lms.newux.utils;

import org.springframework.stereotype.Component;

import com.samsoft.lms.newux.dto.response.InstallmentDates;

import java.util.Comparator;

@Component
public class SortCompareUtil implements Comparator<InstallmentDates> {
    @Override
    public int compare(InstallmentDates a, InstallmentDates b) {
        return a.getInstallmentDate().compareTo(b.getInstallmentDate());
    }
}
