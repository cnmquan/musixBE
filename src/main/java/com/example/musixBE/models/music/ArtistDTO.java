package com.example.musixBE.models.music;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.*;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class ArtistDTO {
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private String id;
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private String name;
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private String alias;
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private String thumbnail;
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private String cover;
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private String biography;
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private String sortBiography;
}
