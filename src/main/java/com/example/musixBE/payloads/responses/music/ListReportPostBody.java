package com.example.musixBE.payloads.responses.music;

import com.example.musixBE.models.social.ReportPostDTO;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ListReportPostBody {
    List<ReportPostDTO> reportPosts;
}
