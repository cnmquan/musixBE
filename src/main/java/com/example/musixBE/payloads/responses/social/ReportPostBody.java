package com.example.musixBE.payloads.responses.social;

import com.example.musixBE.models.social.ReportPostDTO;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ReportPostBody {
    ReportPostDTO reportPostDTO;
}
