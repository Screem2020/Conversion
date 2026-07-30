package com.example.conversion.repository;

import com.example.conversion.model.entity.InboxMessage;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
    public interface InboxRepository extends JpaRepository<InboxMessage, UUID> {
    boolean existsByEventId(UUID eventId);
    InboxMessage findInboxMessageByEventId(UUID eventId);
}
