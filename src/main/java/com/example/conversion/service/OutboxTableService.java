package com.example.conversion.service;

import com.example.conversion.model.entity.OutboxTable;
import com.example.conversion.model.enums.OutboxEventType;
import com.example.conversion.model.enums.OutboxStatus;
import com.example.conversion.repository.OutboxRepository;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class OutboxTableService {
    private OutboxRepository outboxRepository;

    public void save(OutboxTable outboxTable) {
        outboxRepository.save(outboxTable);
    }

    public List<OutboxTable> findAllEventToList() {
        Pageable pageable = PageRequest.of(0, 10);
        outboxRepository.findAll(pageable).forEach(or -> or.setStatus(OutboxStatus.IN_PROGRESS));
        return outboxRepository
                .findAll(pageable)
                .stream()
                .filter(or -> or.getType().equals(OutboxEventType.CONVERTER_TASK))
                .toList();

    }
}
