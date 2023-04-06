package com.example.musixBE.payloads.responses.social;

import com.example.musixBE.models.social.CommentDTO;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ListCommentBody {
    List<CommentDTO> comments;
}
