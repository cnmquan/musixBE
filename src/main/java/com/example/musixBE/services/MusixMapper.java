package com.example.musixBE.services;

import com.example.musixBE.models.music.*;
import com.example.musixBE.models.social.Comment;
import com.example.musixBE.models.social.CommentDTO;
import com.example.musixBE.models.social.Post;
import com.example.musixBE.models.social.PostDTO;
import com.example.musixBE.models.token.Token;
import com.example.musixBE.models.token.TokenDTO;
import com.example.musixBE.models.user.Profile;
import com.example.musixBE.models.user.ProfileDTO;
import com.example.musixBE.models.user.User;
import com.example.musixBE.models.user.UserDTO;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

import java.util.List;

@Mapper
public interface MusixMapper {

    MusixMapper INSTANCE = Mappers.getMapper(MusixMapper.class);

    ProfileDTO profileToProfileDTO(Profile profile);

    UserDTO userToUserDTO(User user);

    TokenDTO tokenToTokenDTO(Token token);

    ArtistDTO artistToArtistDTO(Artist artist);

    List<ArtistDTO> artistListToArtistDTOList(List<Artist> artists);

    Artist toArtist(ArtistDTO artistDTO);

    GenreDTO genreToGenreDTO(Genre genre);

    VideoDTO videoToVideoDTO(Video video);

    SongDTO songToSongDTO(Song song);

    List<SongDTO> songListToSongDTOList(List<Song> songs);

    Song toSong(SongDTO songDTO);

    PlaylistDTO playlistToPlaylistDTO(Playlist playlist);

    List<PlaylistDTO> playlistListToPlaylistDTOList(List<Playlist> playlists);

    Playlist toPlaylist(PlaylistDTO playlistDTO);

    MusicDTO musicToMusicDTO(Music music);

    CommentDTO commentToCommentDTO(Comment comment);

    List<CommentDTO> commentListToCommentDTOList(List<Comment> comments);

    Comment toComment(CommentDTO commentDTO);

    PostDTO postToPostDTO(Post post);

    List<PostDTO> listPostToListPostDTO(List<Post> posts);
}
