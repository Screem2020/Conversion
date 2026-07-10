package com.example.conversion.service;

import com.example.conversion.model.entity.OutboxTable;
import com.example.conversion.model.enums.OutboxStatus;
import com.example.conversion.repository.OutboxRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Slf4j
@Transactional
@Service
@RequiredArgsConstructor
public class OutboxTableService {
    private final OutboxRepository outboxRepository;

    public void save(OutboxTable outboxTable) {
        outboxRepository.save(outboxTable);
    }

    public List<OutboxTable> eventOutboxToList() {
        Pageable pageable = PageRequest.of(0, 10);
        var events = outboxRepository.findByStatus(OutboxStatus.NEW, pageable);
        List<OutboxTable> content = events.getContent();
        return content.stream()
                .peek(or -> or.setStatus(OutboxStatus.IN_PROGRESS))
                .toList();

    }
}
