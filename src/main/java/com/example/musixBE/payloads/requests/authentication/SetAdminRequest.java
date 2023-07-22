package com.example.musixBE.payloads.requests.authentication;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class SetAdminRequest {
    private String username;
    private String password;
    private String name;
}
