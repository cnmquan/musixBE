package com.example.musixBE.payloads.requests.music;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class DeleteSongRecordRequest {
    private String songId;
    private boolean isDeleteAll;
}
