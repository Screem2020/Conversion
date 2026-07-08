package com.example.conversion.repository;

import com.example.conversion.model.entity.OutboxTable;
import org.springframework.data.jpa.repository.JpaRepository;

public interface OutboxRepository extends JpaRepository<OutboxTable, String> {

}
