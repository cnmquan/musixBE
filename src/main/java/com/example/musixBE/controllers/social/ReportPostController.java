package com.example.musixBE.controllers.social;

import com.example.musixBE.models.social.ReportPost;
import com.example.musixBE.payloads.requests.social.post.ReportPostRequest;
import com.example.musixBE.payloads.responses.Response;
import com.example.musixBE.payloads.responses.music.ListReportPostBody;
import com.example.musixBE.payloads.responses.social.ListPostBody;
import com.example.musixBE.services.social.ReportPostService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/social/report-post")
@RequiredArgsConstructor
public class ReportPostController {
    private final ReportPostService reportPostService;

    @GetMapping("/all")
    public ResponseEntity<Response<ListReportPostBody>> getAllReports() {
        Response<ListReportPostBody> response = reportPostService.getReports();
        return ResponseEntity.status(response.getStatus()).body(response);
    }

    @PostMapping
    public ResponseEntity<Response<ReportPost>> createNewReport(@RequestBody ReportPostRequest request) {
        Response<ReportPost> response = reportPostService.createNewReport(request);
        return ResponseEntity.status(response.getStatus()).body(response);
    }

    @GetMapping()
    public ResponseEntity<Response<ListReportPostBody>> getReportsByPostId(@RequestParam String postId) {
        Response<ListReportPostBody> response = reportPostService.getReportsByPostId(postId);
        return ResponseEntity.status(response.getStatus()).body(response);
    }
}
