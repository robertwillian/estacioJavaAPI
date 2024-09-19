package com.robert.schedules.entities;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class SuccessResponse<T> {
    private T data;
    private Boolean status;
}
