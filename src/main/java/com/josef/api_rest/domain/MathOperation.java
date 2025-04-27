package com.josef.api_rest.domain;

import com.josef.api_rest.exception.UnsupportedMathException;
import com.josef.api_rest.request.converters.NumberConverter;

public class MathOperation {

    public MathOperation(){}

    public static Double sum(Double num1, Double num2) {
        return num1 + num2;
    }

    public static Double subtraction(Double num1, Double num2) {
        return num1 - num2;
    }

    public static Double multiplication(Double num1, Double num2) {
        return num1 * num2;
    }

    public static Double division(Double num1, Double num2) {
        return num1 / num2;
    }

    public static Double mean(Double num1, Double num2) {
        return (num1 * num2) / 2;
    }

    public static Double squareRoot(Double num1) {
        return Math.sqrt(num1);
    }

}
