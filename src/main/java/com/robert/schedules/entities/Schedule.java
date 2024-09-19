package com.robert.schedules.entities;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.sql.Date;
import java.sql.Timestamp;
import java.util.UUID;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class Schedule {
    private UUID id;
    private Timestamp date;
    private String customerId;
    private String userId;
    private String service;
    private Timestamp deletedAt;
    private Timestamp createdAt;
    private Timestamp updatedAt;
}
