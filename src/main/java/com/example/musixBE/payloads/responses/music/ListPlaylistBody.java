package com.example.musixBE.payloads.responses.music;

import com.example.musixBE.models.music.PlaylistDTO;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ListPlaylistBody {
    private List<PlaylistDTO> playlists;
}
