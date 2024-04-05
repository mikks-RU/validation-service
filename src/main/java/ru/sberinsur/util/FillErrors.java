package ru.sberinsur.util;

import lombok.extern.slf4j.Slf4j;
import org.everit.json.schema.ValidationException;
import org.springframework.stereotype.Component;
import ru.sberinsur.model.ValidationResult;

import java.util.HashSet;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

@Slf4j
@Component
public class FillErrors {

    private static final Pattern UNIQUE_ERROR_PATTERN = Pattern.compile("\\[([a-zA-Z]+)\\]");

    public static ValidationResult handleValidationException(ValidationException ex) {
        Set<String> errors = new HashSet<>();
        errors.addAll(ex.getAllMessages().stream()
                .map(FillErrors::formatErrorMessage)
                .collect(Collectors.toSet()));

        if (ex.getCausingExceptions() != null && !ex.getCausingExceptions().isEmpty()) {
            ex.getCausingExceptions().forEach(failure ->
                    errors.add(formatErrorMessage(failure.getMessage())));
        }

        String fullError = String.join(", ", errors);
        log.error("Full validation error: {}", fullError);
        Set<String> uniqueErrors = getUniqueErrors(fullError);
        String briefError = String.join(", ", uniqueErrors);

        return new ValidationResult(fullError, briefError);
    }

    private static String formatErrorMessage(String message) {
        return message.replaceAll("#: ", "").replaceAll(" #", "").replaceAll("\\s+", " ");
    }

    private static Set<String> getUniqueErrors(String validationResult) {
        Set<String> uniqueErrors = new HashSet<>();
        Matcher matcher = UNIQUE_ERROR_PATTERN.matcher(validationResult);

        while (matcher.find()) {
            String match = matcher.group(1);
            uniqueErrors.add(match);
        }

        return uniqueErrors;
    }
}