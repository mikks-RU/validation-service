# JSON Validation Service

## Description

This service is designed to validate JSON data against specific schemas. The service provides an HTTP endpoint to accept JSON data and returns the validation result.

## Features

1. **Service Interface**:
  - The service accepts JSON data through an HTTP request.
  - The request header must contain the "service" field, which determines the validation schema for the request.
  - Upon successful validation, the service returns a 200 status code and a confirmation of successful validation.
  - In case of validation errors, the service returns the appropriate error code (e.g., 400 Bad Request) and error information in JSON format.

2. **Schema Storage**:
  - The JSON validation schemas are stored in the file system.
  - The database stores information about the mapping between services and schema files, as well as the hashes of the schema files.
  - At application startup and periodically, the service checks the hashes of the schema files and updates the corresponding records in the database.

3. **Request Processing**:
  - Upon receiving a validation request, the service extracts the "service" value from the request header.
  - It then finds the corresponding schema in memory based on the "service" value.
  - Next, the service performs the validation of the received JSON message against the found schema.
  - The service returns the validation result: a 200 status code for successful validation or an error code and error information in JSON format for unsuccessful validation.

4. **Additional Features**:
  - Handling of incorrect JSON format.
  - Handling the case when the schema for the given "service" is not found.
  - Providing both detailed and concise error information.


# JSON Validation Service

## Описание

Данный сервис предназначен для валидации JSON-данных на соответствие определенным схемам. Сервис предоставляет HTTP-эндпоинт для принятия JSON-данных и возвращает результат валидации.

## Возможности

1. **Интерфейс сервиса**:
  - Сервис принимает JSON-данные через HTTP-запрос.
  - В заголовке запроса должно быть указано поле "service", определяющее схему валидации для запроса.
  - При успешной валидации возвращается код 200 и подтверждение успешной валидации.
  - При ошибке валидации возвращается соответствующий код ошибки (например, 400 Bad Request) и информация об ошибке в формате JSON.

2. **Хранение схем**:
  - Схемы для валидации JSON-данных хранятся в файловой системе.
  - В базе данных хранится информация о соответствии между сервисами и файлами схем, а также хэши файлов схем.
  - При старте приложения и периодически сервис проверяет хэши файлов схем и обновляет соответствующие записи в базе данных.

3. **Обработка запросов**:
  - При получении запроса на валидацию, сервис извлекает значение поля "service" из заголовка запроса.
  - Находит соответствующую схему в памяти по значению "service".
  - Проводит валидацию полученного JSON-сообщения по найденной схеме.
  - Возвращает результат валидации: код состояния 200 при успешной валидации или код ошибки и информацию об ошибке в формате JSON при неуспешной валидации.

4. **Дополнительные возможности**:
  - Обработка некорректного формата JSON.
  - Обработка ситуации, когда схема по заданному "service" не найдена.
  - Предоставление как полной информации об ошибке, так и краткой.
