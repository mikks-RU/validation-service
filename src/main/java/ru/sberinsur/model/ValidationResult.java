package ru.sberinsur.model;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class ValidationResult {
    private String fullError;
    private String briefError;
}