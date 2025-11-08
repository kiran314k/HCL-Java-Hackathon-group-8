package com.hcl.audit.impl;



import com.fasterxml.jackson.databind.ObjectMapper;
import com.hcl.audit.dto.AuditEventRequest;
import com.hcl.audit.dto.AuditEventResponse;
import com.hcl.audit.entity.AuditEventEntity;
import com.hcl.audit.entity.repository.AuditEventRepository;
import com.hcl.audit.service.AuditEventService;

import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class AuditEventServiceImpl implements AuditEventService {

    private final AuditEventRepository repository;
    private final ObjectMapper mapper;

    public AuditEventServiceImpl(AuditEventRepository repository, ObjectMapper mapper) {
        this.repository = repository;
        this.mapper = mapper;
    }

    @Override
    public AuditEventResponse createEvent(AuditEventRequest request) {
        try {
            String jsonPayload = mapper.writeValueAsString(request.getPayload());
            AuditEventEntity entity = new AuditEventEntity(
                    request.getSource(),
                    request.getType(),
                    jsonPayload,
                    LocalDateTime.now(),
                    request.getPerformedBy()
            );
            AuditEventEntity saved = repository.save(entity);
            return new AuditEventResponse(
                    saved.getId(),
                    saved.getSource(),
                    saved.getType(),
                    mapper.readTree(saved.getPayload()),
                    saved.getTimestamp(),
                    saved.getPerformedBy()
            );
        } catch (Exception e) {
            throw new RuntimeException("Failed to save audit event", e);
        }
    }

    @Override
    public List<AuditEventResponse> getEvents(String source, String type,String performedBy) {
        List<AuditEventEntity> entities;
        if (source != null && type != null) {
            entities = repository.findAll().stream()
                    .filter(e -> e.getSource().equals(source) && e.getType().equals(type))
                    .toList();
        } else if (source != null) {
            entities = repository.findBySource(source);
        } else if (type != null) {
            entities = repository.findByType(type);
        } else if(performedBy !=null) {
        	entities = repository.findByPerformedBy(performedBy);
        }else{
            entities = repository.findAll();
        }

        return entities.stream()
                .map(e -> {
                    try {
                        return new AuditEventResponse(
                                e.getId(),
                                e.getSource(),
                                e.getType(),
                                mapper.readTree(e.getPayload()),
                                e.getTimestamp(),
                                e.getPerformedBy()
                        );
                    } catch (Exception ex) {
                        throw new RuntimeException(ex);
                    }
                })
                .collect(Collectors.toList());
    }
}
