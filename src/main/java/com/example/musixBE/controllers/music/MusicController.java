package com.example.musixBE.controllers.music;

import com.example.musixBE.payloads.requests.music.*;
import com.example.musixBE.payloads.responses.Response;
import com.example.musixBE.payloads.responses.music.*;
import com.example.musixBE.services.music.MusicService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/music")
@RequiredArgsConstructor
public class MusicController {
    private final MusicService service;

    @GetMapping("/{username}")
    public ResponseEntity<Response<UserMusicBody>> getOtherUserMusic(@PathVariable("username") String username) {
        var response = service.getUserMusic(username);
        return ResponseEntity.status(response.getStatus()).body(response);
    }

    @GetMapping
    public ResponseEntity<Response<UserMusicBody>> getUserMusic(@RequestHeader("Authorization") String bearerToken) {
        var response = service.getUserMusicByToken(bearerToken);
        return ResponseEntity.status(response.getStatus()).body(response);
    }

    @PutMapping("/playlist/favorite")
    public ResponseEntity<Response<ListPlaylistBody>> favoritePlaylist(@RequestBody FavoritePlaylistRequest request,
                                                                       @RequestHeader("Authorization") String bearerToken) {
        var response = service.favoritePlaylist(request, bearerToken);
        return ResponseEntity.status(response.getStatus()).body(response);
    }

    @PutMapping("/playlist/dislike")
    public ResponseEntity<Response<ListPlaylistBody>> dislikePlaylist(@RequestBody DislikePlaylistRequest request,
                                                                      @RequestHeader("Authorization") String bearerToken) {
        var response = service.dislikePlaylist(request, bearerToken);
        return ResponseEntity.status(response.getStatus()).body(response);
    }

    @PutMapping("/artist/favorite")
    public ResponseEntity<Response<ListArtistBody>> favoriteArtist(@RequestBody FavoriteArtistRequest request,
                                                                   @RequestHeader("Authorization") String bearerToken) {
        var response = service.favoriteArtist(request, bearerToken);
        return ResponseEntity.status(response.getStatus()).body(response);
    }

    @PutMapping("/artist/dislike")
    public ResponseEntity<Response<ListArtistBody>> dislikeArtist(@RequestBody DislikeArtistRequest request,
                                                                  @RequestHeader("Authorization") String bearerToken) {
        var response = service.dislikeArtist(request, bearerToken);
        return ResponseEntity.status(response.getStatus()).body(response);
    }

    @PutMapping("/song/favorite")
    public ResponseEntity<Response<ListSongBody>> favoriteSong(@RequestBody FavoriteSongRequest request,
                                                               @RequestHeader("Authorization") String bearerToken) {
        var response = service.favoriteSong(request, bearerToken);
        return ResponseEntity.status(response.getStatus()).body(response);
    }

    @PutMapping("/song/dislike")
    public ResponseEntity<Response<ListSongBody>> dislikeSong(@RequestBody DislikeSongRequest request,
                                                              @RequestHeader("Authorization") String bearerToken) {
        var response = service.dislikeSong(request, bearerToken);
        return ResponseEntity.status(response.getStatus()).body(response);
    }

    @PostMapping("/ownPlaylist")
    public ResponseEntity<Response<ListPlaylistBody>> createPlaylist(@RequestBody CreatePlaylistRequest request,
                                                                     @RequestHeader("Authorization") String bearerToken) {
        var response = service.createPlaylist(request, bearerToken);
        return ResponseEntity.status(response.getStatus()).body(response);
    }

    @PutMapping("/ownPlaylist/{playlistId}/changeProfile")
    public ResponseEntity<Response<ListPlaylistBody>> changeProfilePlaylist(
            @PathVariable("playlistId") String playlistId,
            @RequestBody ChangeProfilePlaylistRequest request,
            @RequestHeader("Authorization") String bearerToken) {
        var response = service.changeProfilePlaylist(playlistId, request, bearerToken);
        return ResponseEntity.status(response.getStatus()).body(response);
    }

    @PutMapping("/ownPlaylist/{playlistId}/uploadThumbnail")
    public ResponseEntity<Response<ListPlaylistBody>> uploadPlaylistThumbnail(
            @PathVariable("playlistId") String playlistId,
            @ModelAttribute UploadPlaylistThumbnailRequest request,
            @RequestHeader("Authorization") String bearerToken) {
        var response = service.uploadPlaylistThumbnail(playlistId, request, bearerToken);
        return ResponseEntity.status(response.getStatus()).body(response);
    }

    @DeleteMapping("/ownPlaylist/{playlistId}")
    public ResponseEntity<Response<ListPlaylistBody>> removePlaylist(
            @PathVariable("playlistId") String playlistId,
            @RequestHeader("Authorization") String bearerToken) {
        var response = service.removePlaylist(playlistId, bearerToken);
        return ResponseEntity.status(response.getStatus()).body(response);
    }

    @PutMapping("/ownPlaylist/{playlistId}/uploadSong")
    public ResponseEntity<Response<ListPlaylistBody>> uploadSongPlaylist(
            @PathVariable("playlistId") String playlistId,
            @RequestBody UploadSongPlaylistRequest request,
            @RequestHeader("Authorization") String bearerToken) {
        var response = service.uploadSongPlaylist(playlistId, request, bearerToken);
        return ResponseEntity.status(response.getStatus()).body(response);
    }

    @GetMapping("/record")
    public ResponseEntity<Response<UserRecordBody>> getUserRecord(@RequestHeader("Authorization") String bearerToken) {
        var response = service.getUserRecord( bearerToken);
        return ResponseEntity.status(response.getStatus()).body(response);
    }

    @PutMapping("/search-song/history")
    public ResponseEntity<Response<SearchRecordBody>> saveSearchRecord(@RequestBody SaveSearchRecordRequest request,
                                                                       @RequestHeader("Authorization") String bearerToken) {
        var response = service.saveSearchRecord(request.getSearch(), bearerToken);
        return ResponseEntity.status(response.getStatus()).body(response);
    }

    @PutMapping("/recent-song/history")
    public ResponseEntity<Response<SongRecordBody>> saveSongRecord(@RequestBody SaveSongRecordRequest request,
                                                                       @RequestHeader("Authorization") String bearerToken) {
        var response = service.saveSongRecord(request.getSongId(), bearerToken);
        return ResponseEntity.status(response.getStatus()).body(response);
    }

    @DeleteMapping("/search-song/history")
    public ResponseEntity<Response<Boolean>> deleteSearchRecord(@RequestBody DeleteSearchRecordRequest request,
                                                                       @RequestHeader("Authorization") String bearerToken) {
        var response = service.deleteSearchRecord(request, bearerToken);
        return ResponseEntity.status(response.getStatus()).body(response);
    }

    @DeleteMapping("/recent-song/history")
    public ResponseEntity<Response<Boolean>> deleteSongRecord(@RequestBody DeleteSongRecordRequest request,
                                                                   @RequestHeader("Authorization") String bearerToken) {
        var response = service.deleteSongRecord(request, bearerToken);
        return ResponseEntity.status(response.getStatus()).body(response);
    }
}
