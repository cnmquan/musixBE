package com.example.musixBE.services;

import com.example.musixBE.models.token.Token;
import com.example.musixBE.models.token.TokenDTO;
import com.example.musixBE.models.user.Profile;
import com.example.musixBE.models.user.ProfileDTO;
import com.example.musixBE.models.user.User;
import com.example.musixBE.models.user.UserDTO;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

@Mapper
public interface MusixMapper {

    MusixMapper INSTANCE = Mappers.getMapper( MusixMapper.class );

    ProfileDTO profileToProfileDTO(Profile profile);

    UserDTO userToUserDTO(User user);

    TokenDTO tokenToTokenDTO(Token token);
}
