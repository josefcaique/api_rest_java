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
    public Double sum(@PathVariable("num1") String num1,
                      @PathVariable("num2") String num2)
    {
        if (!isNumeric(num1) || !isNumeric(num2)) throw new UnsupportedMathException("Please set a numeric value!");
        return convertToDouble(num1) + convertToDouble(num2);
    }

    private boolean isNumeric(String strNum){
        if (strNum == null || strNum.isEmpty()) return false;
        String number = strNum.replace(",", ".");
        return number.matches("[-+]?[0-9]*\\.?[0-9]+");
    }

    private Double convertToDouble(String num){
        if (!isNumeric(num)) throw new UnsupportedMathException("Please set a numeric value!");
        return Double.parseDouble(num);
    }
}
