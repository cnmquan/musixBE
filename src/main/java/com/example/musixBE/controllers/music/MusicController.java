package com.example.musixBE.controllers.music;

import com.example.musixBE.payloads.requests.music.*;
import com.example.musixBE.payloads.responses.Response;
import com.example.musixBE.payloads.responses.music.UserMusicBody;
import com.example.musixBE.services.music.MusicService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/music")
@RequiredArgsConstructor
public class MusicController {
    private final MusicService service;

    @GetMapping
    public ResponseEntity<Response<UserMusicBody>> getUserMusic(@RequestBody GetUserMusicRequest request){
        var response = service.getUserMusic(request);
        return ResponseEntity.status(response.getStatus()).body(response);
    }

    @PostMapping("/playlist/favorite")
    public ResponseEntity<Response<UserMusicBody>> favoritePlaylist(@RequestBody FavoritePlaylistRequest request){
        var response = service.favoritePlaylist(request);
        return ResponseEntity.status(response.getStatus()).body(response);
    }

    @PostMapping("/playlist/dislike")
    public ResponseEntity<Response<UserMusicBody>> dislikePlaylist(@RequestBody DislikePlaylistRequest request){
        var response = service.dislikePlaylist(request);
        return ResponseEntity.status(response.getStatus()).body(response);
    }

    @PostMapping("/artist/favorite")
    public ResponseEntity<Response<UserMusicBody>> favoriteArtist(@RequestBody FavoriteArtistRequest request){
        var response = service.favoriteArtist(request);
        return ResponseEntity.status(response.getStatus()).body(response);
    }

    @PostMapping("/artist/dislike")
    public ResponseEntity<Response<UserMusicBody>> dislikeArtist(@RequestBody DislikeArtistRequest request){
        var response = service.dislikeArtist(request);
        return ResponseEntity.status(response.getStatus()).body(response);
    }

    @PostMapping("/song/favorite")
    public ResponseEntity<Response<UserMusicBody>> favoriteSong(@RequestBody FavoriteSongRequest request){
        var response = service.favoriteSong(request);
        return ResponseEntity.status(response.getStatus()).body(response);
    }

    @PostMapping("/song/dislike")
    public ResponseEntity<Response<UserMusicBody>> dislikeSong(@RequestBody DislikeSongRequest request){
        var response = service.dislikeSong(request);
        return ResponseEntity.status(response.getStatus()).body(response);
    }

    @PostMapping("/playlist/create")
    public ResponseEntity<Response<UserMusicBody>> createPlaylist(@RequestBody CreatePlaylistRequest request){
        var response = service.createPlaylist(request);
        return ResponseEntity.status(response.getStatus()).body(response);
    }

    @PostMapping("/playlist/changeProfile")
    public ResponseEntity<Response<UserMusicBody>> changeProfilePlaylist(@RequestBody ChangeProfilePlaylistRequest request){
        var response = service.changeProfilePlaylist(request);
        return ResponseEntity.status(response.getStatus()).body(response);
    }

    @PostMapping("/playlist/uploadThumbnail")
    public ResponseEntity<Response<UserMusicBody>> uploadPlaylistThumbnail(@ModelAttribute UploadPlaylistThumbnailRequest request){
        var response = service.uploadPlaylistThumbnail(request);
        return ResponseEntity.status(response.getStatus()).body(response);
    }

    @PostMapping("/playlist/remove")
    public ResponseEntity<Response<UserMusicBody>> removePlaylist(@RequestBody RemovePlaylistRequest request){
        var response = service.removePlaylist(request);
        return ResponseEntity.status(response.getStatus()).body(response);
    }

    @PostMapping("/playlist/uploadSong")
    public ResponseEntity<Response<UserMusicBody>> uploadSongPlaylist(@RequestBody UploadSongPlaylistRequest request){
        var response = service.uploadSongPlaylist(request);
        return ResponseEntity.status(response.getStatus()).body(response);
    }
}
