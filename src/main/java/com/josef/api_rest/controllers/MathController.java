package com.josef.api_rest.controllers;

import com.josef.api_rest.domain.MathOperation;
import com.josef.api_rest.exception.UnsupportedMathException;
import com.josef.api_rest.request.converters.NumberConverter;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;


@RestController
@RequestMapping("/math")
public class MathController {


    @RequestMapping("/sum/{num1}/{num2}")
    public Double sum(@PathVariable("num1") String num1, @PathVariable("num2") String num2) {
        return MathOperation.sum(NumberConverter.convertToDouble(num1), NumberConverter.convertToDouble(num2));
    }

    @RequestMapping("/subtraction/{num1}/{num2}")
    public Double subtraction(@PathVariable("num1") String num1, @PathVariable("num2") String num2){
        return MathOperation.subtraction(NumberConverter.convertToDouble(num1), NumberConverter.convertToDouble(num2));
    }

    @RequestMapping("/multiplication/{num1}/{num2}")
    public Double multiplication(@PathVariable("num1") String num1, @PathVariable("num2") String num2){
        return MathOperation.multiplication(NumberConverter.convertToDouble(num1), NumberConverter.convertToDouble(num2));
    }

    @RequestMapping("/division/{num1}/{num2}")
    public Double division(@PathVariable("num1") String num1, @PathVariable("num2") String num2) {
        return MathOperation.division(NumberConverter.convertToDouble(num1), NumberConverter.convertToDouble(num2));
    }

    @RequestMapping("/mean/{num1}/{num2}")
    private Double mean(@PathVariable("num1") String num1, @PathVariable("num2") String num2) {
        return MathOperation.mean(NumberConverter.convertToDouble(num1), NumberConverter.convertToDouble(num2));
    }

    @RequestMapping("/squareroot/{num1}")
    private Double squareRoot(@PathVariable("num1") String num1) {
        return MathOperation.squareRoot(NumberConverter.convertToDouble(num1));
    }

}
