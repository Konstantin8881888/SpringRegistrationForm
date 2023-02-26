package com.example.auth.bean;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ErrorResponse extends BaseResponse
{
    private final String message;

    public ErrorResponse(String message) {
        this.message = message;
        this.ok = false;
    }
}
