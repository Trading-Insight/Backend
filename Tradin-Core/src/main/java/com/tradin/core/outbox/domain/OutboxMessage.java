package com.tradin.core.outbox.domain;

import com.tradin.core.common.jpa.AuditTime;
import com.vladmihalcea.hibernate.type.json.JsonBinaryType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.persistence.Index;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.Type;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Table(name = "outbox_message", indexes = {
    @Index(name = "idx_outbox_message_type", columnList = "message_type"),
    @Index(name = "idx_outbox_message_id", columnList = "message_id"),
    @Index(name = "idx_outbox_status", columnList = "status"),
    @Index(name = "idx_outbox_type_status", columnList = "message_type, status"),
    @Index(name = "idx_outbox_created_at", columnList = "created_at"),
    @Index(name = "idx_outbox_updated_at", columnList = "updated_at")
})
public class OutboxMessage extends AuditTime {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private OutboxMessageType messageType;

    @Column(nullable = false)
    private String messageId;

    @Type(JsonBinaryType.class)
    @Column(nullable = false, columnDefinition = "jsonb")
    private String payload;

    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private OutboxStatus status;

    @Column
    private String errorMessage;

    @Builder
    public OutboxMessage(OutboxMessageType messageType, String messageId, String payload) {
        this.messageType = messageType;
        this.messageId = messageId;
        this.payload = payload;
        this.status = OutboxStatus.PENDING;
    }

    public static OutboxMessage of(OutboxMessageType messageType, String messageId, String payload) {
        return OutboxMessage.builder()
            .messageType(messageType)
            .messageId(messageId)
            .payload(payload)
            .build();
    }

    public void markAsPending() {
        this.status = OutboxStatus.PENDING;
    }


    public void markAsPublished() {
        this.status = OutboxStatus.PUBLISHED;
    }

    public void markAsPublishingFailed(String errorMessage) {
        this.status = OutboxStatus.PUBLISHING_FAILED;
        this.errorMessage = errorMessage;
    }

    public void markAsProcessingFailed(String errorMessage) {
        this.status = OutboxStatus.PROCESSING_FAILED;
        this.errorMessage = errorMessage;
    }

    public void markAsCompleted() {
        this.status = OutboxStatus.COMPLETED;
    }

    public String getTopic() {
        return this.messageType.getTopic();
    }
} 