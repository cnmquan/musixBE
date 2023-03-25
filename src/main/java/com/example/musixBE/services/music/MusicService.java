package com.example.musixBE.services.music;

import com.example.musixBE.models.music.*;
import com.example.musixBE.models.status.StatusList;
import com.example.musixBE.models.user.User;
import com.example.musixBE.payloads.requests.music.*;
import com.example.musixBE.payloads.responses.Response;
import com.example.musixBE.payloads.responses.music.ListArtistBody;
import com.example.musixBE.payloads.responses.music.ListPlaylistBody;
import com.example.musixBE.payloads.responses.music.ListSongBody;
import com.example.musixBE.payloads.responses.music.UserMusicBody;
import com.example.musixBE.repositories.MusicRepository;
import com.example.musixBE.repositories.UserRepository;
import com.example.musixBE.services.MusixMapper;
import com.example.musixBE.utils.DateUtils;
import com.example.musixBE.utils.FileType;
import com.example.musixBE.utils.FileUtils;
import com.example.musixBE.utils.RandomString;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.ArrayList;

@Service
@RequiredArgsConstructor
public class MusicService {
    private final MusicRepository musicRepository;

    private final UserRepository userRepository;

    private final FileUtils fileUtils;

    private final MusixMapper mapper = MusixMapper.INSTANCE;

    public Response<UserMusicBody> getUserMusic(GetUserMusicRequest request) {
        try {
             var user = userRepository.findByUsername(request.getUsername())
                     .orElseThrow(() -> new Exception(StatusList.errorUsernameNotFound.getMsg()));

            var music = musicRepository.findByUsername(request.getUsername());
            if(music.isEmpty()){
                var newMusic = Music.builder()
                        .user(User.builder()
                                .id(user.getId())
                                .username(user.getUsername())
                                .email(user.getEmail())
                                .profile(user.getProfile())
                                .build())
                        .dislikeArtists(new ArrayList<>())
                        .dislikePlaylist(new ArrayList<>())
                        .dislikeSongs(new ArrayList<>())
                        .favoriteArtists(new ArrayList<>())
                        .favoritePlaylists(new ArrayList<>())
                        .favoriteSongs(new ArrayList<>())
                        .ownPlaylists(new ArrayList<>())
                        .build();
                var saveMusic = musicRepository.save(newMusic);

                return Response.<UserMusicBody>builder()
                        .status(StatusList.successService.getStatus())
                        .msg(StatusList.successService.getMsg())
                        .data(UserMusicBody.builder()
                                .music(mapper.musicToMusicDTO(saveMusic))
                                .build())
                        .build();
            }
            return Response.<UserMusicBody>builder()
                    .status(StatusList.successService.getStatus())
                    .msg(StatusList.successService.getMsg())
                    .data(UserMusicBody.builder()
                            .music(mapper.musicToMusicDTO(music.get()))
                            .build())
                    .build();
        } catch (Exception e) {
            if(e.getMessage().equals(StatusList.errorUsernameNotFound.getMsg())){
                return Response.<UserMusicBody>builder()
                        .status(StatusList.errorUsernameNotFound.getStatus())
                        .msg(StatusList.errorUsernameNotFound.getMsg())
                        .build();
            } else {
                return Response.<UserMusicBody>builder()
                        .status(StatusList.errorService.getStatus())
                        .msg(e.getMessage())
                        .build();
            }
        }
    }

    public  Response<ListPlaylistBody> favoritePlaylist(FavoritePlaylistRequest request) {
        return playlistService(request.getPlaylist(), request.getUsername(), true);
    }

    public  Response<ListPlaylistBody> dislikePlaylist(DislikePlaylistRequest request) {
        return playlistService(request.getPlaylist(), request.getUsername(), false);
    }

    private Response<ListPlaylistBody> playlistService(PlaylistDTO playlist, String username, boolean isFavorite) {
        try {
            var music = musicRepository.findByUsername(username)
                    .orElseThrow(() -> new Exception(StatusList.errorUsernameNotFound.getMsg()));
            var playlists = isFavorite ? music.getFavoritePlaylists() : music.getDislikePlaylist();
            var isExisted = false;

            if(!playlists.isEmpty()) {
                for(var playlistItem : playlists) {
                    if(playlistItem.getId().equals(playlist.getId())){
                        playlists.remove(playlistItem);
                        isExisted = true;
                        break;
                    }
                }
            }

            if(!isExisted) {
                playlist.setType(PlaylistType.THIRD_PARTY);
                playlists.add(mapper.toPlaylist(playlist));
            }

            if(isFavorite) {
                music.setFavoritePlaylists(playlists);
            } else {
                music.setDislikePlaylist(playlists);
            }

            musicRepository.save(music);
            return Response.<ListPlaylistBody>builder()
                    .status(StatusList.successService.getStatus())
                    .msg(StatusList.successService.getMsg())
                    .data(ListPlaylistBody.builder()
                            .playlists(mapper.playlistListToPlaylistDTOList(playlists))
                            .build())
                    .build();

        } catch (Exception e) {
            if(e.getMessage().equals(StatusList.errorUsernameNotFound.getMsg())){
                return Response.<ListPlaylistBody>builder()
                        .status(StatusList.errorUsernameNotFound.getStatus())
                        .msg(StatusList.errorUsernameNotFound.getMsg())
                        .build();
            } else {
                return Response.<ListPlaylistBody>builder()
                        .status(StatusList.errorService.getStatus())
                        .msg(e.getMessage())
                        .build();
            }
        }
    }

    public  Response<ListArtistBody> favoriteArtist(FavoriteArtistRequest request) {
        return artistService(request.getArtist(), request.getUsername(), true);
    }

    public  Response<ListArtistBody> dislikeArtist(DislikeArtistRequest request) {
        return artistService(request.getArtist(), request.getUsername(), false);
    }

    private Response<ListArtistBody> artistService(ArtistDTO artist, String username, boolean isFavorite) {
        try {
            var music = musicRepository.findByUsername(username)
                    .orElseThrow(() -> new Exception(StatusList.errorUsernameNotFound.getMsg()));
            var artists = isFavorite ? music.getFavoriteArtists() : music.getDislikeArtists();
            var isExisted = false;

            if(!artists.isEmpty()){
                for(var artistItem : artists) {
                    if(artistItem.getId().equals(artist.getId())){
                        artists.remove(artistItem);
                        isExisted = true;
                        break;
                    }
                }
            }


            if(!isExisted) {
                artists.add(mapper.toArtist(artist));
            }

            if(isFavorite){
                music.setFavoriteArtists(artists);
            } else {
                music.setDislikeArtists(artists);
            }

            musicRepository.save(music);
            return Response.<ListArtistBody>builder()
                    .status(StatusList.successService.getStatus())
                    .msg(StatusList.successService.getMsg())
                    .data(ListArtistBody.builder()
                            .artists(mapper.artistListToArtistDTOList(artists))
                            .build())
                    .build();

        } catch (Exception e) {
            if(e.getMessage().equals(StatusList.errorUsernameNotFound.getMsg())){
                return Response.<ListArtistBody>builder()
                        .status(StatusList.errorUsernameNotFound.getStatus())
                        .msg(StatusList.errorUsernameNotFound.getMsg())
                        .build();
            } else {
                return Response.<ListArtistBody>builder()
                        .status(StatusList.errorService.getStatus())
                        .msg(e.getMessage())
                        .build();
            }
        }
    }

    public  Response<ListSongBody> favoriteSong(FavoriteSongRequest request) {
        return songService(request.getSong(), request.getUsername(), true);
    }

    public  Response<ListSongBody> dislikeSong(DislikeSongRequest request) {
        return songService(request.getSong(), request.getUsername(), false);
    }

    private Response<ListSongBody> songService(SongDTO song, String username, boolean isFavorite) {
        try {
            var music = musicRepository.findByUsername(username)
                    .orElseThrow(() -> new Exception(StatusList.errorUsernameNotFound.getMsg()));
            var songs = isFavorite ?  music.getFavoriteSongs() : music.getDislikeSongs();
            var isExisted = false;

            if(!songs.isEmpty()){
                for(var songItem : songs) {
                    if(songItem.getId().equals(song.getId())){
                        songs.remove(songItem);
                        isExisted = true;
                        break;
                    }
                }
            }

            if(!isExisted) {
                songs.add(mapper.toSong(song));
            }

            if(isFavorite){
                music.setFavoriteSongs(songs);
            } else {
                music.setDislikeSongs(songs);
            }

            musicRepository.save(music);
            return Response.<ListSongBody>builder()
                    .status(StatusList.successService.getStatus())
                    .msg(StatusList.successService.getMsg())
                    .data(ListSongBody.builder()
                            .songs(mapper.songListToSongDTOList(songs))
                            .build())
                    .build();

        } catch (Exception e) {
            if(e.getMessage().equals(StatusList.errorUsernameNotFound.getMsg())){
                return Response.<ListSongBody>builder()
                        .status(StatusList.errorUsernameNotFound.getStatus())
                        .msg(StatusList.errorUsernameNotFound.getMsg())
                        .build();
            } else {
                return Response.<ListSongBody>builder()
                        .status(StatusList.errorService.getStatus())
                        .msg(e.getMessage())
                        .build();
            }
        }
    }

    public Response<ListPlaylistBody> createPlaylist(CreatePlaylistRequest request) {
        try {
            var music = musicRepository.findByUsername(request.getUsername())
                    .orElseThrow(() -> new Exception(StatusList.errorUsernameNotFound.getMsg()));

            var playlists = music.getOwnPlaylists();
            for(var playlistItem : playlists){
                if(playlistItem.getTitle().equals(request.getTitle())){
                    throw new Exception(StatusList.errorPlaylistExisted.getMsg());
                }
            }

            var playlist = Playlist.builder()
                    .id(RandomString.getAlphaNumericString(24))
                    .title(request.getTitle())
                    .sortDescription(request.getSortDescription())
                    .releasedAt(DateUtils.toCurrentDateString())
                    .type(PlaylistType.OWN)
                    .songs(new ArrayList<>())
                    .countSongs(0)
                    .build();

            playlists.add(playlist);
            music.setOwnPlaylists(playlists);
            musicRepository.save(music);

            return Response.<ListPlaylistBody>builder()
                    .status(StatusList.successService.getStatus())
                    .msg(StatusList.successService.getMsg())
                    .data(ListPlaylistBody.builder()
                            .playlists(mapper.playlistListToPlaylistDTOList(playlists))
                            .build())
                    .build();
        } catch (Exception e) {
            if(e.getMessage().equals(StatusList.errorUsernameNotFound.getMsg())){
                return Response.<ListPlaylistBody>builder()
                        .status(StatusList.errorUsernameNotFound.getStatus())
                        .msg(StatusList.errorUsernameNotFound.getMsg())
                        .build();
            } else if(e.getMessage().equals(StatusList.errorPlaylistExisted.getMsg())){
                return Response.<ListPlaylistBody>builder()
                        .status(StatusList.errorPlaylistExisted.getStatus())
                        .msg(StatusList.errorPlaylistExisted.getMsg())
                        .build();
            }
            else {
                return Response.<ListPlaylistBody>builder()
                        .status(StatusList.errorService.getStatus())
                        .msg(e.getMessage())
                        .build();
            }
        }
    }

    public Response<ListPlaylistBody> changeProfilePlaylist(ChangeProfilePlaylistRequest request) {
        try {
            var music = musicRepository.findByUsername(request.getUsername())
                    .orElseThrow(() -> new Exception(StatusList.errorUsernameNotFound.getMsg()));

            var playlists = music.getOwnPlaylists();
            boolean isExisted = false;
            for(var playlistItem : playlists){
                if(playlistItem.getId().equals(request.getPlaylistId())){
                    var index = playlists.indexOf(playlistItem);
                    playlistItem.setTitle(
                            request.getTitle() != null ?
                            request.getTitle() :
                            playlistItem.getTitle()
                    );
                    playlistItem.setSortDescription(
                            request.getSortDescription() != null ?
                            request.getSortDescription() :
                            playlistItem.getSortDescription()
                    );
                    playlists.set(index, playlistItem);
                    isExisted = true;
                    break;
                }
            }
            if (!isExisted) {
                throw new Exception(StatusList.errorPlaylistNotFound.getMsg());
            } else {
                music.setOwnPlaylists(playlists);
                musicRepository.save(music);
            }


            return Response.<ListPlaylistBody>builder()
                    .status(StatusList.successService.getStatus())
                    .msg(StatusList.successService.getMsg())
                    .data(ListPlaylistBody.builder()
                            .playlists(mapper.playlistListToPlaylistDTOList(playlists))
                            .build())
                    .build();
        } catch (Exception e) {
            if(e.getMessage().equals(StatusList.errorUsernameNotFound.getMsg())){
                return Response.<ListPlaylistBody>builder()
                        .status(StatusList.errorUsernameNotFound.getStatus())
                        .msg(StatusList.errorUsernameNotFound.getMsg())
                        .build();
            } else if(e.getMessage().equals(StatusList.errorPlaylistNotFound.getMsg())){
                return Response.<ListPlaylistBody>builder()
                        .status(StatusList.errorPlaylistNotFound.getStatus())
                        .msg(StatusList.errorPlaylistNotFound.getMsg())
                        .build();
            } else {
                return Response.<ListPlaylistBody>builder()
                        .status(StatusList.errorService.getStatus())
                        .msg(e.getMessage())
                        .build();
            }
        }
    }

    public Response<ListPlaylistBody> uploadPlaylistThumbnail(UploadPlaylistThumbnailRequest request){
        try {
            var music = musicRepository.findByUsername(request.getUsername())
                    .orElseThrow(() -> new Exception(StatusList.errorUsernameNotFound.getMsg()));
            var playlists = music.getOwnPlaylists();
            Playlist playlist = null;
            var index = -1;
            for(var playlistItem : playlists){
                if(playlistItem.getId().equals(request.getPlaylistId())){
                    playlist = playlistItem;
                    index = playlists.indexOf(playlistItem);
                    break;
                }
            }
            if(playlist == null){
                throw new Exception(StatusList.errorPlaylistNotFound.getMsg());
            }

            var thumbnailId = playlist.getThumbnailId();
            if(thumbnailId == null){
                thumbnailId = music.getUser().getId() +"/playlist/" + playlist.getId() + "/thumbnail";
                playlist.setThumbnailId(thumbnailId);
            }
            var uploadUrl = fileUtils.upload(request.getThumbnail(), thumbnailId);
            playlist.setThumbnail(uploadUrl);

            playlists.set(index, playlist);
            music.setOwnPlaylists(playlists);
            musicRepository.save(music);

            return Response.<ListPlaylistBody>builder()
                    .status(StatusList.successService.getStatus())
                    .msg(StatusList.successService.getMsg())
                    .data(ListPlaylistBody.builder()
                            .playlists(mapper.playlistListToPlaylistDTOList(playlists))
                            .build())
                    .build();
        }
        catch (IOException e){
            return Response.<ListPlaylistBody>builder()
                    .status(400)
                    .msg(e.getMessage())
                    .build();
        }
        catch (Exception e) {
            if (e.getMessage().equals(StatusList.errorUsernameNotFound.getMsg())){
                return Response.<ListPlaylistBody>builder()
                        .status(StatusList.errorUsernameNotFound.getStatus())
                        .msg(StatusList.errorUsernameNotFound.getMsg())
                        .build();
            } else {
                return Response.<ListPlaylistBody>builder()
                        .status(StatusList.errorService.getStatus())
                        .msg(e.getMessage())
                        .build();
            }
        }
    }

    public Response<ListPlaylistBody> removePlaylist(RemovePlaylistRequest request){
        try {
            var music = musicRepository.findByUsername(request.getUsername())
                    .orElseThrow(() -> new Exception(StatusList.errorUsernameNotFound.getMsg()));

            var playlists = music.getOwnPlaylists();
            boolean isExisted = false;
            for(var playlistItem : playlists){
                if(playlistItem.getId().equals(request.getPlaylistId())){
                    isExisted = true;
                    if(playlistItem.getThumbnailId() != null){
                        fileUtils.destroy(playlistItem.getThumbnailId(), FileType.image);
                    }
                    playlists.remove(playlistItem);
                    break;
                }
            }
            if (!isExisted) {
                throw new Exception(StatusList.errorPlaylistNotFound.getMsg());
            } else {
                music.setOwnPlaylists(playlists);
                musicRepository.save(music);
            }

            return Response.<ListPlaylistBody>builder()
                    .status(StatusList.successService.getStatus())
                    .msg(StatusList.successService.getMsg())
                    .data(ListPlaylistBody.builder()
                            .playlists(mapper.playlistListToPlaylistDTOList(playlists))
                            .build())
                    .build();
        }
        catch (IOException e){
            return Response.<ListPlaylistBody>builder()
                    .status(400)
                    .msg(e.getMessage())
                    .build();
        }
        catch (Exception e) {
            if(e.getMessage().equals(StatusList.errorUsernameNotFound.getMsg())){
                return Response.<ListPlaylistBody>builder()
                        .status(StatusList.errorUsernameNotFound.getStatus())
                        .msg(StatusList.errorUsernameNotFound.getMsg())
                        .build();
            } else if  (e.getMessage().equals(StatusList.errorPlaylistNotFound.getMsg())) {
                return Response.<ListPlaylistBody>builder()
                        .status(StatusList.errorPlaylistNotFound.getStatus())
                        .msg(StatusList.errorPlaylistNotFound.getMsg())
                        .build();
            }else {
                return Response.<ListPlaylistBody>builder()
                        .status(StatusList.errorService.getStatus())
                        .msg(e.getMessage())
                        .build();
            }
        }
    }

    public Response<ListPlaylistBody> uploadSongPlaylist(UploadSongPlaylistRequest request) {
        try {
            var music = musicRepository.findByUsername(request.getUsername())
                    .orElseThrow(() -> new Exception(StatusList.errorUsernameNotFound.getMsg()));

            var playlists = music.getOwnPlaylists();
            var song = mapper.toSong(request.getSong());
            boolean isExisted = false;
            for(var playlistItem : playlists){
                if(playlistItem.getId().equals(request.getPlaylistId())){
                    var index = playlists.indexOf(playlistItem);
                    var songs = playlistItem.getSongs();
                    boolean isSongExisted = false;

                    if(!songs.isEmpty()){
                        for(var songItem : songs){
                            if(songItem.getId().equals(song.getId())){
                                songs.remove(songItem);
                                isSongExisted = true;
                                break;
                            }
                        }
                    }

                    if(!isSongExisted){
                        songs.add(song);
                    }

                    playlistItem.setSongs(songs);
                    playlistItem.setCountSongs(songs.size());
                    playlists.set(index, playlistItem);
                    isExisted = true;
                    break;
                }
            }
            if (!isExisted) throw new Exception(StatusList.errorPlaylistNotFound.getMsg());
            else {
                music.setOwnPlaylists(playlists);
                musicRepository.save(music);
            }


            return Response.<ListPlaylistBody>builder()
                    .status(StatusList.successService.getStatus())
                    .msg(StatusList.successService.getMsg())
                    .data(ListPlaylistBody.builder()
                            .playlists(mapper.playlistListToPlaylistDTOList(playlists))
                            .build())
                    .build();
        } catch (Exception e) {
            if(e.getMessage().equals(StatusList.errorUsernameNotFound.getMsg())){
                return Response.<ListPlaylistBody>builder()
                        .status(StatusList.errorUsernameNotFound.getStatus())
                        .msg(StatusList.errorUsernameNotFound.getMsg())
                        .build();
            }
            else if(e.getMessage().equals(StatusList.errorPlaylistNotFound.getMsg())){
                return Response.<ListPlaylistBody>builder()
                        .status(StatusList.errorPlaylistNotFound.getStatus())
                        .msg(StatusList.errorPlaylistNotFound.getMsg())
                        .build();
            } else {
                return Response.<ListPlaylistBody>builder()
                        .status(StatusList.errorService.getStatus())
                        .msg(e.getMessage())
                        .build();
            }
        }
    }
}
