package com.tahaakocer.camunda.utils;

import org.springframework.stereotype.Component;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public class VariableUtils {
    public static List<String> StringSplit(String input) {
        if (input == null || input.isEmpty()) {
            return Collections.emptyList();
        }
        String[] parts = input.split(",");
        return Arrays.asList(parts);
    }
}
