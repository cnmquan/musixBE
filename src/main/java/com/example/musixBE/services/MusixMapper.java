package com.example.musixBE.services;

import com.example.musixBE.models.Token;
import com.example.musixBE.models.TokenDTO;
import com.example.musixBE.models.User;
import com.example.musixBE.models.UserDTO;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

@Mapper
public interface MusixMapper {

    MusixMapper INSTANCE = Mappers.getMapper( MusixMapper.class );

    UserDTO userToUserDTO(User user);

    TokenDTO tokenToTokenDTO(Token token);
}
