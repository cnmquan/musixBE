package com.example.musixBE.models.music;

import com.example.musixBE.models.user.User;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.List;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Document("musics")
public class Music {
    private String id;
    private User user;
    private List<Artist> favoriteArtists;
    private List<Artist> dislikeArtists;
    private List<Song> favoriteSongs;
    private List<Song> dislikeSongs;
    private List<Playlist> favoritePlaylists;
    private List<Playlist> dislikePlaylist;
    private List<Playlist> ownPlaylists;
    private List<String> searchRecord;
    private List<String> songRecord;
}
