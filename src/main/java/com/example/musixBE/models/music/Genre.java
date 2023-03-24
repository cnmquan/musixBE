package com.example.musixBE.models.music;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class Genre {
    private String id;
    private String name;
    private String tile;
    private String alias;
    private List<Genre> child;
    private Genre parent;
}
