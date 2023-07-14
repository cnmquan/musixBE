package com.example.musixBE.controllers.social;

import com.example.musixBE.models.social.ReportPost;
import com.example.musixBE.payloads.requests.social.post.ReportPostRequest;
import com.example.musixBE.payloads.responses.Response;
import com.example.musixBE.payloads.responses.social.ReportPostBody;
import com.example.musixBE.services.social.ReportPostService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/social/report_post")
@RequiredArgsConstructor
public class ReportPostController {
    private final ReportPostService reportPostService;

    @PostMapping
    public ResponseEntity<Response<ReportPost>> createNewReport(@RequestBody ReportPostRequest request) {
        Response<ReportPost> response = reportPostService.createNewReport(request);
        return ResponseEntity.status(response.getStatus()).body(response);
    }
}
