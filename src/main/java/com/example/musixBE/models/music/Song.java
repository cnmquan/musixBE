package com.example.musixBE.models.music;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Song {
    private String id;
    private String title;
    private String artistNames;
    private String thumbnail;
    private List<Genre> genres;
    private List<Artist> artists;
    private String audioUrl;
    private String lyricUrl;
}
