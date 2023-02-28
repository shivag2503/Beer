package com.barclays.practice.springmvc.domain;

import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.UUID;

@Builder
@Data
public class Customer {

    private UUID id;
    private String name;
    private Integer version;
    private LocalDateTime createdTime;
    private LocalDateTime lastModifiedTime;
}
