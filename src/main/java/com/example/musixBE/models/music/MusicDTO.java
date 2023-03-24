package com.example.musixBE.models.music;

import com.example.musixBE.models.user.User;
import lombok.*;

import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class MusicDTO {
    private String id;
    private User user;
    private List<ArtistDTO> favoriteArtists;
    private List<ArtistDTO> dislikeArtists;
    private List<SongDTO> favoriteSongs;
    private List<SongDTO> dislikeSongs;
    private List<PlaylistDTO> favoritePlaylists;
    private List<PlaylistDTO> dislikePlaylist;
    private List<PlaylistDTO> ownPlaylists;
}
