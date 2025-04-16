package com.samsoft.lms.banking.utils;

import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Component;

@Component
@Slf4j
public class BankingUtil {

    public static final String ID_PREFIX = "4FIN";

    public static final Character ID_SUFFIX = 'X';

    public static final String VPA_ID_SUFFIX = "X.04@cmsidfc";

    public String createVirtualId(String value) {

        return ID_PREFIX + value + ID_SUFFIX;
    }

    public String createVPAId(String value) {
        return ID_PREFIX + value + VPA_ID_SUFFIX;
    }

    public Character getVirtualIdLastCharacter(String value) {
        int n = value.length();
        return value.charAt(n - 1);
    }

    public String getRemovedVirtualIdLastCharacter(String value) {
        if (value != null && value.length() > 0 && value.charAt(value.length() - 1) == ID_SUFFIX) {
            value = value.substring(0, value.length() - 1);
        }

        return value;
    }
}
