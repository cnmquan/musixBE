package com.example.musixBE.payloads.requests.music;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class ChangeProfilePlaylistRequest {
    private String title;
    private String sortDescription;
}
