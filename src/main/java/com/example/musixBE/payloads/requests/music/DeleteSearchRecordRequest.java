package com.example.musixBE.payloads.requests.music;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class DeleteSearchRecordRequest {
    private String search;
    private boolean isDeleteAll;
}
