package com.example.musixBE.services.social;

import com.example.musixBE.models.social.Post;
import com.example.musixBE.models.social.ReportPost;
import com.example.musixBE.models.status.StatusList;
import com.example.musixBE.models.user.User;
import com.example.musixBE.payloads.requests.social.post.ReportPostRequest;
import com.example.musixBE.payloads.responses.Response;
import com.example.musixBE.payloads.responses.social.ReportPostBody;
import com.example.musixBE.repositories.PostRepository;
import com.example.musixBE.repositories.ReportPostRepository;
import com.example.musixBE.repositories.UserRepository;
import com.example.musixBE.services.MusixMapper;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import javax.swing.text.html.Option;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class ReportPostService {
    final ReportPostRepository reportPostRepository;
    final PostRepository postRepository;
    final UserRepository userRepository;
    final MusixMapper musixMapper = MusixMapper.INSTANCE;

    public Response<ReportPost> createNewReport(ReportPostRequest request) {
        final Optional<User> user = userRepository.findById(request.getUserId());
        final Optional<Post> post = postRepository.findById(request.getPostId());
        if (user.isEmpty()) {
            return Response.<ReportPost>builder()
                    .status(StatusList.errorUserIdNotFound.getStatus())
                    .msg(StatusList.errorUserIdNotFound.getMsg())
                    .build();
        }
        if (post.isEmpty()) {
            return Response.<ReportPost>builder()
                    .status(StatusList.errorPostNotFound.getStatus())
                    .msg(StatusList.errorPostNotFound.getMsg())
                    .build();
        }
        ReportPost reportPost = ReportPost.builder()
                .post(post.get())
                .user(user.get())
                .reason(request.getReason())
                .dateCreated(System.currentTimeMillis()).build();
        final var result = reportPostRepository.save(reportPost);
        return Response.<ReportPost>builder()
                .status(StatusList.successService.getStatus())
                .msg(StatusList.successService.getMsg())
                .data(result)
                .build();
    }
}
