package com.message.auth_service.dto;

import lombok.Data;

@Data
public class EmailDto {

    String to;
    String subject;
    String body;
}