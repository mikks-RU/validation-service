package ru.sberinsur.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.sberinsur.model.SchemaUpdateResult;
import ru.sberinsur.service.SchemaTableLoader;

import java.util.List;

@RestController
@RequestMapping("/update-schemas")
@RequiredArgsConstructor
public class SchemaUpdateController {
    private final SchemaTableLoader schemaLoader;

    @GetMapping
    public ResponseEntity<List<SchemaUpdateResult>> updateSchemas() {
        List<SchemaUpdateResult> updatedSchemas = schemaLoader.updateSchemas(false);
        return ResponseEntity.ok(updatedSchemas);
    }
}