package com.example.musixBE.payloads.responses;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class AuthenticationFailedResponse extends AuthenticationResponse {
    private int status;
    private String msg;
}
