package com.rest.oauth2.controller;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class AuthErrorResponse {
    //
    private String errorCode;
    private String messageCode;
}
