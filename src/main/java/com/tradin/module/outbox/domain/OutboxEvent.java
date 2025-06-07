package com.tradin.module.outbox.domain;

import com.tradin.common.jpa.AuditTime;
import com.vladmihalcea.hibernate.type.json.JsonBinaryType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Index;
import jakarta.persistence.Table;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.Type;

@Entity
@Getter
@Table(name = "outbox_events", indexes = {
    @Index(name = "idx_outbox_events_status_created_at", columnList = "status, created_at"),
    @Index(name = "idx_outbox_events_event_id", columnList = "event_id", unique = true),
    @Index(name = "idx_outbox_events_event_type", columnList = "event_type")
})
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class OutboxEvent extends AuditTime {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private OutboxEventType eventType;

    @Column(nullable = false)
    private String eventId;

    @Type(JsonBinaryType.class)
    @Column(nullable = false, columnDefinition = "jsonb")
    private String payload;

    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private OutboxStatus status;

    @Column
    private String errorMessage;

    @Builder
    public OutboxEvent(OutboxEventType eventType, String eventId, String payload) {
        this.eventType = eventType;
        this.eventId = eventId;
        this.payload = payload;
        this.status = OutboxStatus.PENDING;
    }

    public static OutboxEvent of(OutboxEventType eventType, String eventId, String payload) {
        return OutboxEvent.builder()
            .eventType(eventType)
            .eventId(eventId)
            .payload(payload)
            .build();
    }

    public void markAsPublished() {
        this.status = OutboxStatus.PUBLISHED;
    }

    public void markAsFailed(String errorMessage) {
        this.status = OutboxStatus.FAILED;
        this.errorMessage = errorMessage;
    }

    public void markAsCompleted() {
        this.status = OutboxStatus.COMPLETED;
    }

    public String getTopic() {
        return this.eventType.getTopic();
    }
} 