package com.example.musixBE.models.music;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class Artist {
    private String id;
    private String name;
    private String alias;
    private String thumbnail;
    private String cover;
    private String biography;
    private String sortBiography;
}
