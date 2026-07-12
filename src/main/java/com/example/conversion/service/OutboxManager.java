package com.example.conversion.service;

import com.example.conversion.model.entity.OutboxTable;
import com.example.conversion.model.enums.OutboxStatus;
import com.example.conversion.repository.OutboxRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Slf4j
@Transactional
@Component
@RequiredArgsConstructor
public class OutboxManager {
    private final OutboxRepository outboxRepository;

    public void save(OutboxTable outboxTable) {
        outboxRepository.save(outboxTable);
    }

    public List<OutboxTable> eventOutboxToList() {
        Pageable pageable = PageRequest.of(0, 100);
        return outboxRepository
                .findByStatus(OutboxStatus.NEW, pageable)
                .stream()
                .peek(outboxTable -> outboxTable.setStatus(OutboxStatus.IN_PROGRESS))
                .toList();
    }
}
