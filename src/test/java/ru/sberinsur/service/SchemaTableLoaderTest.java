package ru.sberinsur.service;

import org.everit.json.schema.loader.SchemaLoader;
import org.json.JSONObject;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import ru.sberinsur.data.entity.SchemaTable;
import ru.sberinsur.data.repository.SchemaTableRepository;
import ru.sberinsur.model.SchemaUpdateResult;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
class SchemaTableLoaderTest {

    @Autowired
    private SchemaTableLoader schemaTableLoader;

    @Autowired
    private SchemaTableRepository schemaTableRepository;

    @TempDir
    public File tempDir;

    @BeforeEach
    void setUp() {
        schemaTableRepository.deleteAll();
    }

    @Test
    void updateSchemas_WhenSchemasDirectoryExists_ShouldLoadAllSchemas() throws IOException {
        // Arrange
        String schemaPath = "LifeInsPRTTerminationSchema.json";
        String schemaContent = "{\"type\":\"object\",\"properties\":{\"name\":{\"type\":\"string\"}}}";
        Path schemaFilePath = tempDir.toPath().resolve(schemaPath);
        Files.write(schemaFilePath, schemaContent.getBytes());

        SchemaTable schemaTable = new SchemaTable();
        schemaTable.setService("CreateTerminationAppLifeRq");
        schemaTable.setPath(schemaPath);
        schemaTable.setHash("e5e40dd58fc6a1a7b852d18528dd8693fe083c71a0e37533fd7b6cdd8f322cf3");
        schemaTableRepository.save(schemaTable);

        // Act
        List<SchemaUpdateResult> updatedSchemas = schemaTableLoader.updateSchemas(false);

        // Assert
        assertThat(updatedSchemas).hasSize(1);
        assertThat(updatedSchemas.get(0).getService()).isEqualTo("CreateTerminationAppLifeRq");
        assertThat(updatedSchemas.get(0).getPath()).isEqualTo(schemaPath);
        assertThat(schemaTableLoader.getSchema("CreateTerminationAppLifeRq")).isNotNull();
    }
}