package com.example.conversion.repository;

import com.example.conversion.model.entity.OutboxTable;
import com.example.conversion.model.enums.OutboxEventType;
import com.example.conversion.model.enums.OutboxStatus;
import jakarta.persistence.LockModeType;
import lombok.NonNull;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.data.jpa.repository.Query;

import java.util.UUID;

public interface OutboxRepository extends JpaRepository<OutboxTable, UUID> {
    @Lock(LockModeType.PESSIMISTIC_WRITE)
    @Query("""
    select o from OutboxTable o
    where o.status = :status
""")
    Page<@NonNull OutboxTable> findByStatus(OutboxStatus status, Pageable pageable);

    Page<@NonNull OutboxTable> findByType(OutboxEventType type,  Pageable pageable);
}
