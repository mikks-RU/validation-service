package ru.sberinsur.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.everit.json.schema.Schema;
import org.everit.json.schema.ValidationException;
import org.json.JSONException;
import org.json.JSONObject;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.sberinsur.model.ValidationResult;
import ru.sberinsur.util.FillErrors;
import ru.sberinsur.service.SchemaTableLoader;

import java.util.Map;

@RestController
@RequestMapping("/validate")
@RequiredArgsConstructor
@Slf4j
public class ValidationController {

    private final SchemaTableLoader schemaLoader;
    private final FillErrors fillErrors;

    @PostMapping(consumes = "application/json; charset=UTF-8", produces = "application/json; charset=UTF-8")
    public ResponseEntity<ValidationResult> validateJson(
            @RequestHeader("service") String service,
            @RequestHeader Map<String, String> headers,
            @RequestBody String json) {
        log.info("Received headers: {}", headers);
        log.info("Received JSON: {}", json);

        Schema schema = schemaLoader.getSchema(service);
        if (schema == null) {
            return ResponseEntity.badRequest().body(new ValidationResult("Schema not found", ""));
        }

        return validateJsonAgainstSchema(json, schema);
    }

    private ResponseEntity<ValidationResult> validateJsonAgainstSchema(String json, Schema schema) {
        try {
            JSONObject jsonObject = new JSONObject(json);
            schema.validate(jsonObject);
            return ResponseEntity.ok(new ValidationResult("JSON successfully validated", ""));
        } catch (JSONException e) {
            return ResponseEntity.badRequest().body(new ValidationResult("Invalid JSON: " + e.getMessage(), ""));
        } catch (ValidationException e) {
            return ResponseEntity.badRequest().body(fillErrors.handleValidationException(e));
        }
    }
}