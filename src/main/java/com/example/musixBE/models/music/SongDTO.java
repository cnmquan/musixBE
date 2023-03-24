package com.example.musixBE.models.music;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.*;

import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class SongDTO {
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private String id;
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private String title;
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private String artistNames;
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private String thumbnail;
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private List<GenreDTO> genres;
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private List<ArtistDTO> artists;
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private String audioUrl;
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private String lyricUrl;
}
