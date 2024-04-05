package ru.sberinsur.controller;

import lombok.RequiredArgsConstructor;
import org.everit.json.schema.Schema;
import org.everit.json.schema.ValidationException;
import org.json.JSONException;
import org.json.JSONObject;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.sberinsur.model.ValidationResult;
import ru.sberinsur.util.FillErrors;
import ru.sberinsur.service.SchemaTableLoader;

@RestController
@RequestMapping("/validate")
@RequiredArgsConstructor
public class ValidationController {
    private final SchemaTableLoader schemaLoader;
    private final FillErrors fillErrors;

    @PostMapping
    public ResponseEntity<ValidationResult> validateJson(
            @RequestHeader("service") String service,
            @RequestBody String json) {
        Schema schema = schemaLoader.getSchema(service);
        if (schema == null) {
            return ResponseEntity.badRequest().body(new ValidationResult("Схема не найдена", ""));
        }

        try {
            JSONObject jsonObject = new JSONObject(json);
            schema.validate(jsonObject);
            return ResponseEntity.ok(new ValidationResult("JSON успешно прошел валидацию", ""));
        } catch (JSONException e) {
            return ResponseEntity.badRequest().body(new ValidationResult("Некорректный JSON:" + e.getMessage(), ""));
        } catch (ValidationException e) {
            return ResponseEntity.badRequest().body(fillErrors.handleValidationException(e));
        }
    }

}