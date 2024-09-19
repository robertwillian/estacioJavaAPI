package com.robert.schedules.entities;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class BadRequestResponse {
    private Boolean status;
    private String message;
}
