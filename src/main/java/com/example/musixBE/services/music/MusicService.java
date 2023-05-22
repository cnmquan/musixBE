package com.example.musixBE.services.music;

import com.example.musixBE.models.music.*;
import com.example.musixBE.models.status.StatusList;
import com.example.musixBE.models.user.User;
import com.example.musixBE.payloads.requests.music.*;
import com.example.musixBE.payloads.responses.Response;
import com.example.musixBE.payloads.responses.music.*;
import com.example.musixBE.repositories.MusicRepository;
import com.example.musixBE.repositories.UserRepository;
import com.example.musixBE.services.JwtService;
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

    private final JwtService jwtService;

    private final MusixMapper mapper = MusixMapper.INSTANCE;

    public Response<UserMusicBody> getUserMusicByToken(String bearerToken) {
        final String username = jwtService.extractUsername(bearerToken.substring(7));
        return getUserMusic(username);
    }

    public Response<UserMusicBody> getUserMusic(String username) {
        try {
            var user = userRepository.findByUsername(username)
                    .orElseThrow(() -> new Exception(StatusList.errorUsernameNotFound.getMsg()));

            var music = musicRepository.findByUsername(username);
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
                        .searchRecord(new ArrayList<>())
                        .songRecord(new ArrayList<>())
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

    public  Response<ListPlaylistBody> favoritePlaylist(FavoritePlaylistRequest request, String bearerToken) {
        return playlistService(request.getPlaylist(),true, bearerToken);
    }

    public  Response<ListPlaylistBody> dislikePlaylist(DislikePlaylistRequest request, String bearerToken) {
        return playlistService(request.getPlaylist(),  false, bearerToken);
    }

    private Response<ListPlaylistBody> playlistService(PlaylistDTO playlist, boolean isFavorite, String bearerToken) {
        try {
            final String username = jwtService.extractUsername(bearerToken.substring(7));
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

    public  Response<ListArtistBody> favoriteArtist(FavoriteArtistRequest request, String bearerToken) {
        return artistService(request.getArtist(), true, bearerToken);
    }

    public  Response<ListArtistBody> dislikeArtist(DislikeArtistRequest request, String bearerToken) {
        return artistService(request.getArtist(), false, bearerToken);
    }

    private Response<ListArtistBody> artistService(ArtistDTO artist, boolean isFavorite, String bearerToken) {
        try {
            final String username = jwtService.extractUsername(bearerToken.substring(7));
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

    public  Response<ListSongBody> favoriteSong(FavoriteSongRequest request, String bearerToken) {
        return songService(request.getSong(), true, bearerToken);
    }

    public  Response<ListSongBody> dislikeSong(DislikeSongRequest request, String bearerToken) {
        return songService(request.getSong(),false, bearerToken);
    }

    private Response<ListSongBody> songService(SongDTO song, boolean isFavorite, String bearerToken) {
        try {
            final String username = jwtService.extractUsername(bearerToken.substring(7));
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

    public Response<ListPlaylistBody> createPlaylist(CreatePlaylistRequest request, String bearerToken) {
        try {
            final String username = jwtService.extractUsername(bearerToken.substring(7));
            var music = musicRepository.findByUsername(username)
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

    public Response<ListPlaylistBody> changeProfilePlaylist(String playlistId, ChangeProfilePlaylistRequest request, String bearerToken) {
        try {
            final String username = jwtService.extractUsername(bearerToken.substring(7));
            var music = musicRepository.findByUsername(username)
                    .orElseThrow(() -> new Exception(StatusList.errorUsernameNotFound.getMsg()));

            var playlists = music.getOwnPlaylists();
            boolean isExisted = false;
            for(var playlistItem : playlists){
                if(playlistItem.getId().equals(playlistId)){
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

    public Response<ListPlaylistBody> uploadPlaylistThumbnail(String playlistId, UploadPlaylistThumbnailRequest request, String bearerToken){
        try {
            final String username = jwtService.extractUsername(bearerToken.substring(7));
            var music = musicRepository.findByUsername(username)
                    .orElseThrow(() -> new Exception(StatusList.errorUsernameNotFound.getMsg()));
            var playlists = music.getOwnPlaylists();
            Playlist playlist = null;
            var index = -1;
            for(var playlistItem : playlists){
                if(playlistItem.getId().equals(playlistId)){
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

    public Response<ListPlaylistBody> removePlaylist(String playlistId, String bearerToken){
        try {
            final String username = jwtService.extractUsername(bearerToken.substring(7));
            var music = musicRepository.findByUsername(username)
                    .orElseThrow(() -> new Exception(StatusList.errorUsernameNotFound.getMsg()));

            var playlists = music.getOwnPlaylists();
            boolean isExisted = false;
            for(var playlistItem : playlists){
                if(playlistItem.getId().equals(playlistId)){
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

    public Response<ListPlaylistBody> uploadSongPlaylist(String playlistId, UploadSongPlaylistRequest request, String bearerToken) {
        try {
            final String username = jwtService.extractUsername(bearerToken.substring(7));
            var music = musicRepository.findByUsername(username)
                    .orElseThrow(() -> new Exception(StatusList.errorUsernameNotFound.getMsg()));

            var playlists = music.getOwnPlaylists();
            var song = mapper.toSong(request.getSong());
            boolean isExisted = false;
            for(var playlistItem : playlists){
                if(playlistItem.getId().equals(playlistId)){
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

    public Response<UserRecordBody> getUserRecord(String bearerToken) {
        try {
            final String username = jwtService.extractUsername(bearerToken.substring(7));

            var music = musicRepository.findByUsername(username).orElseThrow(() -> new Exception(StatusList.errorUsernameNotFound.getMsg()));
            if(music.getSearchRecord() == null && music.getSongRecord() == null){
                music.setSearchRecord(new ArrayList<>());
                music.setSongRecord(new ArrayList<>());
                musicRepository.save(music);
            }
            return Response.<UserRecordBody>builder()
                    .status(StatusList.successService.getStatus())
                    .msg(StatusList.successService.getMsg())
                    .data(UserRecordBody.builder()
                            .record(mapper.musicToRecordDTO(music))
                            .build())
                    .build();
        } catch (Exception e) {
            if(e.getMessage().equals(StatusList.errorUsernameNotFound.getMsg())){
                return Response.<UserRecordBody>builder()
                        .status(StatusList.errorUsernameNotFound.getStatus())
                        .msg(StatusList.errorUsernameNotFound.getMsg())
                        .build();
            } else {
                return Response.<UserRecordBody>builder()
                        .status(StatusList.errorService.getStatus())
                        .msg(e.getMessage())
                        .build();
            }
        }
    }

    public Response<SearchRecordBody> saveSearchRecord(String search, String bearerToken) {
        try {
            final String username = jwtService.extractUsername(bearerToken.substring(7));
            var music = musicRepository.findByUsername(username)
                    .orElseThrow(() -> new Exception(StatusList.errorUsernameNotFound.getMsg()));
            var searchRecord = music.getSearchRecord();
            if(searchRecord.isEmpty() || !searchRecord.contains(search)){
                searchRecord.add(search);
                music.setSearchRecord(searchRecord);
                musicRepository.save(music);
            }
            return Response.<SearchRecordBody>builder()
                    .status(StatusList.successService.getStatus())
                    .msg(StatusList.successService.getMsg())
                    .data(SearchRecordBody.builder()
                            .search(search)
                            .build())
                    .build();

        } catch (Exception e) {
            if(e.getMessage().equals(StatusList.errorUsernameNotFound.getMsg())){
                return Response.<SearchRecordBody>builder()
                        .status(StatusList.errorUsernameNotFound.getStatus())
                        .msg(StatusList.errorUsernameNotFound.getMsg())
                        .build();
            } else {
                return Response.<SearchRecordBody>builder()
                        .status(StatusList.errorService.getStatus())
                        .msg(e.getMessage())
                        .build();
            }
        }
    }

    public Response<Boolean> deleteSearchRecord(DeleteSearchRecordRequest request, String bearerToken) {
        try {
            final String username = jwtService.extractUsername(bearerToken.substring(7));
            var music = musicRepository.findByUsername(username)
                    .orElseThrow(() -> new Exception(StatusList.errorUsernameNotFound.getMsg()));
            var searchRecord = music.getSearchRecord();
            if(request.isDeleteAll()){
                searchRecord.clear();
            } else {
                searchRecord.remove(request.getSearch());
            }
            music.setSearchRecord(searchRecord);
            musicRepository.save(music);
            return Response.<Boolean>builder()
                    .status(StatusList.successService.getStatus())
                    .msg(StatusList.successService.getMsg())
                    .data(true)
                    .build();

        } catch (Exception e) {
            if(e.getMessage().equals(StatusList.errorUsernameNotFound.getMsg())){
                return Response.<Boolean>builder()
                        .status(StatusList.errorUsernameNotFound.getStatus())
                        .msg(StatusList.errorUsernameNotFound.getMsg())
                        .build();
            } else {
                return Response.<Boolean>builder()
                        .status(StatusList.errorService.getStatus())
                        .msg(e.getMessage())
                        .build();
            }
        }
    }

    public Response<SongRecordBody> saveSongRecord(String songId, String bearerToken) {
        try {
            final String username = jwtService.extractUsername(bearerToken.substring(7));
            var music = musicRepository.findByUsername(username)
                    .orElseThrow(() -> new Exception(StatusList.errorUsernameNotFound.getMsg()));
            var songRecord = music.getSongRecord();
            if(songRecord.isEmpty() || !songRecord.contains(songId)){
                songRecord.add(songId);
                music.setSongRecord(songRecord);
                musicRepository.save(music);
            }
            return Response.<SongRecordBody>builder()
                    .status(StatusList.successService.getStatus())
                    .msg(StatusList.successService.getMsg())
                    .data(SongRecordBody.builder()
                            .song(songId)
                            .build())
                    .build();

        } catch (Exception e) {
            if(e.getMessage().equals(StatusList.errorUsernameNotFound.getMsg())){
                return Response.<SongRecordBody>builder()
                        .status(StatusList.errorUsernameNotFound.getStatus())
                        .msg(StatusList.errorUsernameNotFound.getMsg())
                        .build();
            } else {
                return Response.<SongRecordBody>builder()
                        .status(StatusList.errorService.getStatus())
                        .msg(e.getMessage())
                        .build();
            }
        }
    }

    public Response<Boolean> deleteSongRecord(DeleteSongRecordRequest request, String bearerToken) {
        try {
            final String username = jwtService.extractUsername(bearerToken.substring(7));
            var music = musicRepository.findByUsername(username)
                    .orElseThrow(() -> new Exception(StatusList.errorUsernameNotFound.getMsg()));
            var songRecord = music.getSongRecord();
            if(request.isDeleteAll()){
                songRecord.clear();
            } else {
                songRecord.remove(request.getSongId());
            }
            music.setSongRecord(songRecord);
            musicRepository.save(music);
            return Response.<Boolean>builder()
                    .status(StatusList.successService.getStatus())
                    .msg(StatusList.successService.getMsg())
                    .data(true)
                    .build();

        } catch (Exception e) {
            if(e.getMessage().equals(StatusList.errorUsernameNotFound.getMsg())){
                return Response.<Boolean>builder()
                        .status(StatusList.errorUsernameNotFound.getStatus())
                        .msg(StatusList.errorUsernameNotFound.getMsg())
                        .build();
            } else {
                return Response.<Boolean>builder()
                        .status(StatusList.errorService.getStatus())
                        .msg(e.getMessage())
                        .build();
            }
        }
    }
}
