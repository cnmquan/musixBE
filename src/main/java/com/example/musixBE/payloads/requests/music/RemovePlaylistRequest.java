package com.example.musixBE.payloads.requests.music;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class RemovePlaylistRequest {
    private String username;
    private String playlistId;
}
