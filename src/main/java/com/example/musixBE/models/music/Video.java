package com.example.musixBE.models.music;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Video {
    private String id;
    private String title;
    private String artistNames;
    private String thumbnail;
    private List<Artist> artists;
    private int duration;
    private String videoUrl;
}
