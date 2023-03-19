package com.example.musixBE.payloads.requests;

import lombok.*;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class RegisterRequest extends Request {
    private String username;
    private String fullName;
    private String email;
    private String password;
    private String birthday;
    private String phoneNumber;
}
