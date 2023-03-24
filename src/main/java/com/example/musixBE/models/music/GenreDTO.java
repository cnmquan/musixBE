package com.example.musixBE.models.music;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.*;

import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class GenreDTO {
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private String id;
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private String name;
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private String tile;
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private String alias;
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private List<GenreDTO> child;
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private GenreDTO parent;
}
