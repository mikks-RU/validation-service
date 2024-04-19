//package ru.sberinsur.util;
//
//import org.everit.json.schema.ValidationException;
//import org.json.JSONObject;
//import org.junit.jupiter.api.Test;
//import ru.sberinsur.model.ValidationResult;
//
//import static org.assertj.core.api.Assertions.assertThat;
//
//class FillErrorsTest {
//
//    @Test
//    void handleValidationException_ShouldReturnValidationResult() {
//        // Arrange
//        String exceptionMessage = "required key [name] not found";
//        JSONObject jsonObject = new JSONObject();
//        ValidationException validationException = new ValidationException(jsonObject, exceptionMessage, "/data", new String[] {"name"});
//
//        // Act
//        ValidationResult validationResult = FillErrors.handleValidationException(validationException);
//
//        // Assert
//        assertThat(validationResult.getFullError()).isEqualTo("required key [name] not found");
//        assertThat(validationResult.getBriefError()).isEqualTo("name");
//    }
//}