package com.example.musixBE.payloads.responses.user;

import com.example.musixBE.models.user.UserDataDTO;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class UserDataBody {
    private UserDataDTO user;
}
