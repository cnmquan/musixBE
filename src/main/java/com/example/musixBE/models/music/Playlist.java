package com.example.musixBE.models.music;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class Playlist {
    private String id;
    private String title;
    private String thumbnail;
    private String thumbnailId;
    private String artistNames;
    private String genreNames;
    private String releasedAt;
    private String sortDescription;
    private List<Song> songs;
    private int countSongs;
    private List<Genre> genres;
    private List<Artist> artists;
    private PlaylistType type;
}
