package ru.sberinsur.data.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.sberinsur.data.entity.SchemaTable;

public interface SchemaTableRepository extends JpaRepository<SchemaTable, Long> {

}
