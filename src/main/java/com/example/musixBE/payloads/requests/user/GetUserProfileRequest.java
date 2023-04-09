package com.example.musixBE.payloads.requests.user;

import com.example.musixBE.payloads.requests.Request;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class GetUserProfileRequest extends Request {
    private String id;
}
