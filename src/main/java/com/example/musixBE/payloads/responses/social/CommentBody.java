package com.example.musixBE.payloads.responses.social;

import com.example.musixBE.models.social.CommentDTO;
import lombok.*;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CommentBody {
    CommentDTO comment;
}
