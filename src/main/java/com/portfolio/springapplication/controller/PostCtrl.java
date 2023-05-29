package com.portfolio.springapplication.controller;

import com.portfolio.springapplication.dto.post.FavReqDto;
import com.portfolio.springapplication.dto.post.PostAddReqDto;
import com.portfolio.springapplication.dto.post.PostDeleteReqDto;
import com.portfolio.springapplication.dto.post.PostUpdateReqDto;
import com.portfolio.springapplication.repository.PostRepo;
import com.portfolio.springapplication.security.auth.UserPrincipalDetail;
import com.portfolio.springapplication.security.auth.UserPrincipalDetailService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/api/post")
public class PostCtrl {

    @Autowired
    private PostRepo postRepo;
    @Autowired
    private UserPrincipalDetailService userPrincipalDetailService;

    @GetMapping("/list")
    public ResponseEntity<?> getAllPosts(@RequestParam(value = "userId", required = false)  Integer userId) {
        return ResponseEntity.ok().body(postRepo.getAllPosts(userId));
    }

    @GetMapping("/view")
    public ResponseEntity<?> getPostDtl(@RequestParam("postId") Integer postId, @RequestParam(value = "userId", required = false) Integer userId) {
        return ResponseEntity.ok().body(postRepo.getPostDetail(postId, userId));
    }

    @GetMapping("/location")
    public ResponseEntity<?> getLocPost(@RequestParam("locId") Integer locId, @RequestParam(value = "userId", required = false) Integer userId){
        return ResponseEntity.ok().body(postRepo.getLocPost(locId, userId));
    }

    @GetMapping("/user")
    public ResponseEntity<?> getUserPost(@RequestParam("puId") Integer postUserId, @RequestParam(value = "userId", required = false) Integer userId){
        return ResponseEntity.ok().body(postRepo.getUserPost(postUserId, userId));
    }

    @PostMapping("/add")
    public ResponseEntity<?> addPost(@RequestBody PostAddReqDto postAddReqDto) {
        UserPrincipalDetail userPrincipalDetail = (UserPrincipalDetail) userPrincipalDetailService.loadUserByUsername(postAddReqDto.getUsername());
        String pics = null;

        if (!postAddReqDto.getPicDatas().isEmpty()){
            pics = String.join(", ", postAddReqDto.getPicDatas());
        }

        int postId = postRepo.addPost(
                userPrincipalDetail.user().getId(),
                postAddReqDto.getContent(),
                postAddReqDto.getLocId(),
                postAddReqDto.getEvalScore(),
                pics);
        Map<String, Integer> response = new HashMap<>();
        response.put("postId", postId);

        return ResponseEntity.ok().body(response);
    }

    @PutMapping("/update")
    public ResponseEntity<?> updatePost(@RequestBody PostUpdateReqDto postUpdateReqDto){
        UserPrincipalDetail userPrincipalDetail = (UserPrincipalDetail) userPrincipalDetailService.loadUserByUsername(postUpdateReqDto.getUsername());
        String pics = null;

        if (!postUpdateReqDto.getPicDatas().isEmpty()){
            pics = String.join(", ", postUpdateReqDto.getPicDatas());
        }

        return ResponseEntity.ok().body(postRepo.updatePost(
                userPrincipalDetail.user().getId(),
                postUpdateReqDto.getEvalScore(),
                pics,
                postUpdateReqDto.getContent()
        ));
    }

    @DeleteMapping("/delete")
    public ResponseEntity<?> deletePost(@RequestBody PostDeleteReqDto postDeleteReqDto){
        UserPrincipalDetail userPrincipalDetail = (UserPrincipalDetail) userPrincipalDetailService.loadUserByUsername(postDeleteReqDto.getUsername());

        return ResponseEntity.ok().body(postRepo.deletePost(
                postDeleteReqDto.getPostId(), userPrincipalDetail.user().getId()
        ));
    }
}