package com.example.conversion.repository;

import com.example.conversion.entity.InboxMessage;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
    public interface InboxRepository extends JpaRepository<InboxMessage, String> {
    boolean existsByEventId(String eventId);
    void save(InboxMessage inboxMessage);
}
