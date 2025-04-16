package com.samsoft.lms.util;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Component;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

@Component
public class DateFormatUtil {

    @Autowired
    private static Environment env;

    public static String convertToDateFormat(Date date) {
        if (date == null) {
            return null;
        }
        String dateFormat = env.getProperty("lms.global.date.format");
        SimpleDateFormat sdf = new SimpleDateFormat(dateFormat);
        return sdf.format(date);

    }

    public static String convertDateToString(Date date) throws ParseException {
        if (date != null) {
            DateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyy");
            return dateFormat.format(date);
        }
        return null;
    }
}
