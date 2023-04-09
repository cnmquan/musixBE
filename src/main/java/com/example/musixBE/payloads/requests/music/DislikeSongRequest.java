package com.example.musixBE.payloads.requests.music;


import com.example.musixBE.models.music.SongDTO;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class DislikeSongRequest {
    private SongDTO song;
}
