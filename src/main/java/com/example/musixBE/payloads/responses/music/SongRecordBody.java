package com.example.musixBE.payloads.responses.music;

import com.example.musixBE.models.music.SongDTO;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class SongRecordBody {
    private String song;
}
