package com.example.musixBE.payloads.requests.music;

import com.example.musixBE.models.music.SongDTO;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class SaveSongRecordRequest {
    private String songId;
}
