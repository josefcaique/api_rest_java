package com.josef.api_rest.request.converters;

import com.josef.api_rest.exception.UnsupportedMathException;

public class NumberConverter {

    public static void checkTwoNumbers(String num1, String num2) {
        if (!isNumeric(num1) || !isNumeric(num2)) throw new UnsupportedMathException("Please set a numeric value");
    }

    public static boolean isNumeric(String strNum){
        if (strNum == null || strNum.isEmpty()) return false;
        String number = strNum.replace(",", ".");
        return number.matches("[-+]?[0-9]*\\.?[0-9]+");
    }

    public static Double convertToDouble(String numStr){
        if (!isNumeric(numStr)) throw new UnsupportedMathException("Please set a numeric value!");
        String num = numStr.replace(",", ".");
        return Double.parseDouble(num);
    }
}
