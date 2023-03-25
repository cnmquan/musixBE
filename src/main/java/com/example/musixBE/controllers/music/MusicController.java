package com.example.musixBE.controllers.music;

import com.example.musixBE.payloads.requests.music.*;
import com.example.musixBE.payloads.responses.Response;
import com.example.musixBE.payloads.responses.music.ListArtistBody;
import com.example.musixBE.payloads.responses.music.ListPlaylistBody;
import com.example.musixBE.payloads.responses.music.ListSongBody;
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

    @PutMapping("/playlist/favorite")
    public ResponseEntity<Response<ListPlaylistBody>> favoritePlaylist(@RequestBody FavoritePlaylistRequest request){
        var response = service.favoritePlaylist(request);
        return ResponseEntity.status(response.getStatus()).body(response);
    }

    @PutMapping("/playlist/dislike")
    public ResponseEntity<Response<ListPlaylistBody>> dislikePlaylist(@RequestBody DislikePlaylistRequest request){
        var response = service.dislikePlaylist(request);
        return ResponseEntity.status(response.getStatus()).body(response);
    }

    @PutMapping("/artist/favorite")
    public ResponseEntity<Response<ListArtistBody>> favoriteArtist(@RequestBody FavoriteArtistRequest request){
        var response = service.favoriteArtist(request);
        return ResponseEntity.status(response.getStatus()).body(response);
    }

    @PutMapping("/artist/dislike")
    public ResponseEntity<Response<ListArtistBody>> dislikeArtist(@RequestBody DislikeArtistRequest request){
        var response = service.dislikeArtist(request);
        return ResponseEntity.status(response.getStatus()).body(response);
    }

    @PutMapping("/song/favorite")
    public ResponseEntity<Response<ListSongBody>> favoriteSong(@RequestBody FavoriteSongRequest request){
        var response = service.favoriteSong(request);
        return ResponseEntity.status(response.getStatus()).body(response);
    }

    @PutMapping("/song/dislike")
    public ResponseEntity<Response<ListSongBody>> dislikeSong(@RequestBody DislikeSongRequest request){
        var response = service.dislikeSong(request);
        return ResponseEntity.status(response.getStatus()).body(response);
    }

    @PostMapping("/playlist/create")
    public ResponseEntity<Response<ListPlaylistBody>> createPlaylist(@RequestBody CreatePlaylistRequest request){
        var response = service.createPlaylist(request);
        return ResponseEntity.status(response.getStatus()).body(response);
    }

    @PutMapping("/playlist/changeProfile")
    public ResponseEntity<Response<ListPlaylistBody>> changeProfilePlaylist(@RequestBody ChangeProfilePlaylistRequest request){
        var response = service.changeProfilePlaylist(request);
        return ResponseEntity.status(response.getStatus()).body(response);
    }

    @PutMapping("/playlist/uploadThumbnail")
    public ResponseEntity<Response<ListPlaylistBody>> uploadPlaylistThumbnail(@ModelAttribute UploadPlaylistThumbnailRequest request){
        var response = service.uploadPlaylistThumbnail(request);
        return ResponseEntity.status(response.getStatus()).body(response);
    }

    @DeleteMapping("/playlist/remove")
    public ResponseEntity<Response<ListPlaylistBody>> removePlaylist(@RequestBody RemovePlaylistRequest request){
        var response = service.removePlaylist(request);
        return ResponseEntity.status(response.getStatus()).body(response);
    }

    @PutMapping("/playlist/uploadSong")
    public ResponseEntity<Response<ListPlaylistBody>> uploadSongPlaylist(@RequestBody UploadSongPlaylistRequest request){
        var response = service.uploadSongPlaylist(request);
        return ResponseEntity.status(response.getStatus()).body(response);
    }
}
