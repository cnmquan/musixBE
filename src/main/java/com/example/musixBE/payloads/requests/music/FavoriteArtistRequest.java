package com.example.musixBE.payloads.requests.music;


import com.example.musixBE.models.music.ArtistDTO;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class FavoriteArtistRequest {
    private ArtistDTO artist;
    private String username;
}
