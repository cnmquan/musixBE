package com.example.musixBE.models.music;

import lombok.*;

import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class RecordDTO {
    private List<String> searchRecord;
    private List<String> songRecord;
}
