package ru.sberinsur.controller;

import org.everit.json.schema.Schema;
import org.everit.json.schema.ValidationException;
import org.everit.json.schema.loader.SchemaLoader;
import org.json.JSONObject;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import ru.sberinsur.model.ValidationResult;
import ru.sberinsur.service.SchemaTableLoader;
import ru.sberinsur.util.FillErrors;

import static org.mockito.Mockito.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(ValidationController.class)
class ValidationControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private SchemaTableLoader schemaTableLoader;

    @MockBean
    private FillErrors fillErrors;

    @Test
    void validateJson_WhenSchemaNotFound_ShouldReturnBadRequest() throws Exception {
        when(schemaTableLoader.getSchema(any())).thenReturn(null);

        mockMvc.perform(post("/validate")
                        .header("service", "test-service")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"key\":\"value\"}"))
                .andExpect(status().isBadRequest())
                .andExpect(content().json("{\"fullError\":\"Схема не найдена\",\"briefError\":\"\"}"));
    }

    @Test
    void validateJson_WhenJsonInvalid_ShouldReturnBadRequest() throws Exception {
        when(schemaTableLoader.getSchema(any())).thenReturn(null);
        when(fillErrors.handleValidationException(any(ValidationException.class)))
                .thenReturn(new ValidationResult("Некорректный JSON", "Некорректный JSON"));

        mockMvc.perform(post("/validate")
                        .header("service", "test-service")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"key\":\"value}"))
                .andExpect(status().isBadRequest())
                .andExpect(content().json("{\"fullError\":\"Некорректный JSON\",\"briefError\":\"Некорректный JSON\"}"));
    }

    @Test
    void validateJson_WhenJsonValid_ShouldReturnOk() throws Exception {
        String service = "SrvLifeInsPINTermination";
        String json = "{\"key\":\"value\"}";
        Schema schema = SchemaLoader.load(new JSONObject("{\"type\":\"object\",\"properties\":{\"key\":{\"type\":\"string\"}}}"));
        when(schemaTableLoader.getSchema(service)).thenReturn(schema);

        mockMvc.perform(post("/validate")
                        .header("service", service)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json))
                .andExpect(status().isOk())
                .andExpect(content().json("{\"fullError\":\"JSON успешно прошел валидацию\",\"briefError\":\"\"}"));
    }
}