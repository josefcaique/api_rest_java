package com.josef.api_rest.controllers;

import com.josef.api_rest.exception.UnsupportedMathException;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import static java.lang.Double.parseDouble;


@RestController
@RequestMapping("/math")
public class MathController {

    @RequestMapping("/sum/{num1}/{num2}")
    public Double sum(@PathVariable("num1") String num1, @PathVariable("num2") String num2) {
        checkTwoNumbers(num1,num2);
        return convertToDouble(num1) + convertToDouble(num2);
    }

    @RequestMapping("/subtraction/{num1}/{num2}")
    public Double subtraction(@PathVariable("num1") String num1, @PathVariable("num2") String num2){
        checkTwoNumbers(num1,num2);
        return convertToDouble(num1) - convertToDouble(num2);
    }

    @RequestMapping("/division/{num1}/{num2}")
    public Double division(@PathVariable("num1") String num1, @PathVariable("num2") String num2) {
        checkTwoNumbers(num1,num2);
        return convertToDouble(num1) / convertToDouble(num2);
    }

    @RequestMapping("/mean/{num1}/{num2}")
    private Double mean(@PathVariable("num1") String num1, @PathVariable("num2") String num2) {
        checkTwoNumbers(num1,num2);
        return (convertToDouble(num1) + convertToDouble(num2)) / 2;
    }

    @RequestMapping("/squareroot/{num1}")
    private Double square(@PathVariable("num1") String num1) {
        if (!isNumeric(num1)) throw new UnsupportedMathException("Please set a numeric value");
        return Math.sqrt(convertToDouble(num1));
    }


    private void checkTwoNumbers(String num1, String num2) {
        if (!isNumeric(num1) || !isNumeric(num2)) throw new UnsupportedMathException("Please set a numeric value");
    }

    private boolean isNumeric(String strNum){
        if (strNum == null || strNum.isEmpty()) return false;
        String number = strNum.replace(",", ".");
        return number.matches("[-+]?[0-9]*\\.?[0-9]+");
    }

    private Double convertToDouble(String numStr){
        if (!isNumeric(numStr)) throw new UnsupportedMathException("Please set a numeric value!");
        String num = numStr.replace(",", ".");
        return Double.parseDouble(num);
    }

}
