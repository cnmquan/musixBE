package com.example.musixBE.payloads.responses.social;

import com.example.musixBE.models.social.PostDTO;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class PostBody {
    PostDTO post;
}
