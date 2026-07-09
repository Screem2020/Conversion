package com.example.conversion.repository;

import com.example.conversion.model.entity.OutboxTable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface OutboxRepository extends JpaRepository<OutboxTable, UUID> {

}
