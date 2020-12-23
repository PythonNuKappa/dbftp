package com.dbftp.engine.utils;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Calendar;
import java.util.Date;

public class StringUtils extends org.apache.commons.lang3.StringUtils {

    public static String parse (String toBeParsed) {

        if (toBeParsed.matches(".*<date>.*<date>.*")){
            return parseDate(toBeParsed);
        }
        return toBeParsed;
    }

    public static String parseDate (String toBeParsed) {
        String delimited = substringBetween(toBeParsed, "<date>", "<date>");
        LocalDate localDate = LocalDate.now();
        if (delimited.matches("[-+]\\d+.")){
            char operator = delimited.charAt(0);
            String operand = delimited.substring(1, delimited.length()-1);
            char dateOperand = delimited.charAt(delimited.length()-1);
            localDate = operateDate(localDate, operator, Integer.parseInt(operand), dateOperand);
        }

        DateTimeFormatter df = DateTimeFormatter.ofPattern("yyyyMMdd");
        String newDelimited =  df.format(localDate);
        toBeParsed = toBeParsed.replace(delimited, newDelimited);

        return toBeParsed.replace("<date>", "");
    }

    public static LocalDate operateDate(LocalDate date, char operator, int operand, char dateOperand) {
        switch(dateOperand){
            case 'D':
                switch(operator){
                    case '-':
                        return date.minusDays(operand);
                    case '+':
                        return date.plusDays(operand);
                }
            case 'M':
                switch(operator){
                    case '-':
                        return date.minusMonths(operand);
                    case '+':
                        return date.plusMonths(operand);
                }
            case 'Y':
                switch(operator){
                    case '-':
                        return date.minusYears(operand);
                    case '+':
                        return date.plusYears(operand);
                }
        }


        return null;
    }
}
