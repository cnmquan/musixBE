package com.example.musixBE.payloads.responses.user;

import com.example.musixBE.models.user.UserDTO;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ListProfileBody {
    private List<UserDTO> users;
}
