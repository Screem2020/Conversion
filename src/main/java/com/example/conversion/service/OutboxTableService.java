package com.example.conversion.service;

import com.example.conversion.model.entity.OutboxTable;
import com.example.conversion.model.enums.OutboxEventType;
import com.example.conversion.model.enums.OutboxStatus;
import com.example.conversion.repository.OutboxRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

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
        Page<OutboxTable> events = outboxRepository.findAll(pageable);
        List<OutboxTable> content = events.getContent();
        content.forEach(or -> or.setStatus(OutboxStatus.IN_PROGRESS));
        return content.stream()
                .filter(or -> or.getType() == (OutboxEventType.CONVERTER_TASK))
                .toList();

    }
}
