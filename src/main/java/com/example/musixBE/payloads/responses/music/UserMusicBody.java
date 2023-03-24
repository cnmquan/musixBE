package com.example.musixBE.payloads.responses.music;

import com.example.musixBE.models.music.MusicDTO;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class UserMusicBody {
    private MusicDTO music;
}
