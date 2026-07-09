package com.example.conversion.repository;

import com.example.conversion.model.entity.OutboxTable;
import com.example.conversion.model.enums.OutboxStatus;
import lombok.NonNull;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface OutboxRepository extends JpaRepository<OutboxTable, UUID> {
    Page<@NonNull OutboxTable> findByStatus(OutboxStatus status, Pageable pageable);
}
