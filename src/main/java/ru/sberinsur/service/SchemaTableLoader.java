package ru.sberinsur.service;

import lombok.extern.slf4j.Slf4j;
import org.apache.commons.codec.digest.DigestUtils;
import org.everit.json.schema.Schema;
import org.everit.json.schema.loader.SchemaLoader;
import org.json.JSONObject;
import org.json.JSONTokener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import ru.sberinsur.data.entity.SchemaTable;
import ru.sberinsur.data.repository.SchemaTableRepository;
import ru.sberinsur.model.SchemaUpdateResult;

import javax.annotation.PostConstruct;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Component
@Slf4j
public class SchemaTableLoader {

    private final SchemaTableRepository schemaTableRepository;
    private final Map<String, Schema> schemaMap = new HashMap<>();

    @Value("${schemas.dir}")
    private String schemasDir;

    @Autowired
    public SchemaTableLoader(SchemaTableRepository schemaTableRepository) {
        this.schemaTableRepository = schemaTableRepository;
    }

    @PostConstruct
    public void init() {
        updateSchemas();
    }

    public List<SchemaUpdateResult> updateSchemas() {
        List<SchemaUpdateResult> updatedSchemas = new ArrayList<>();
        List<SchemaTable> schemaTables = schemaTableRepository.findAll();
        for (SchemaTable schemaTable : schemaTables) {
            String schemaPath = schemasDir + File.separator + schemaTable.getPath();
            Path path = Paths.get(schemaPath);
            try {
                byte[] fileBytes = Files.readAllBytes(path);
                String currentHash = DigestUtils.sha256Hex(fileBytes);
//                if (!currentHash.equals(schemaTable.getHash())) {
                    JSONTokener tokener = new JSONTokener(new String(fileBytes));
                    JSONObject schemaJson = new JSONObject(tokener);
                    Schema schema = SchemaLoader.load(schemaJson);
                    schemaMap.put(schemaTable.getService(), schema);
                    schemaTable.setHash(currentHash);
                    schemaTableRepository.save(schemaTable);
                    updatedSchemas.add(new SchemaUpdateResult(schemaTable.getService(), schemaTable.getPath()));
//                }
            } catch (Exception e) {
                log.error("Ошибка загрузки или обновления схемы: {}", schemaPath, e);
            }
        }
        return updatedSchemas;
    }

    public Schema getSchema(String service) {
        return schemaMap.get(service);
    }
}