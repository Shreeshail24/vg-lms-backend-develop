package com.samsoft.lms;

import org.apache.commons.lang3.StringUtils;
import org.springframework.boot.SpringApplication;

public class Test {

    public static void main(String[] args) {

        String str = "4FINOD2012748424X";

        if (str != null && str.length() > 0 && str.charAt(str.length() - 1) == 'X') {
            str = str.substring(0, str.length() - 1);
        }

        System.out.println(str);
    }
}
