package com.example.musixBE.payloads.requests.music;

import com.example.musixBE.models.music.PlaylistDTO;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class DislikePlaylistRequest {
    private String username;
    private PlaylistDTO playlist;
}
