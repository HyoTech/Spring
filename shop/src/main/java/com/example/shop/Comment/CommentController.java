package com.example.shop.Comment;

import java.util.Optional;

import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.stereotype.Service;

import lombok.RequiredArgsConstructor;

import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.example.shop.User.MyUserDetailsService.CustomUser;

@Service
@Controller
@RequiredArgsConstructor
public class CommentController {
    private final CommentRepository commentRepository;

    @PostMapping("/detail/comment")
    public String postItemComment(@RequestParam("username") String username, @RequestParam("comment") String comment,
            @RequestParam("parentid") long parentid,
            @RequestParam("parentcategory") long parentcategory) {
        if (comment != null) {
            Comment newComment = new Comment();
            newComment.setUsername(username);
            newComment.setContent(comment);
            newComment.setParentId(parentid);
            newComment.setParentCategory(parentcategory);
            commentRepository.save(newComment);
        }
        return "redirect:/list/page/1";
    }

    // 1, parent_category라는 속성을 만듬
    // 2, 상품의 경우 1, 공지사항의 경우 2 -> 카테고리라는 테이블을 새로 만들어야할 필요?
    // 3, 댓글 작성 api에서 상품의 경우 1을 디비에 저장하고, get방식 때 1이면 아이템 코멘트 보여주는 방식으로 제작
    @PostMapping("/detailInfo/comment")
    public String postInfoComment(@RequestParam("username") String username, @RequestParam("comment") String comment,
            @RequestParam("parentid") long parentid,
            @RequestParam("parentcategory") long parentcategory) {
        if (comment != null) {
            Comment newComment = new Comment();
            newComment.setUsername(username);
            newComment.setContent(comment);
            newComment.setParentId(parentid);
            newComment.setParentCategory(parentcategory);
            commentRepository.save(newComment);
        }
        return "redirect:/list/page/1";
    }

    // 댓글 삭제기능
    // 자신이 작성한 댓글만 삭제 가능하게(현재 로그인한 유저의 아이디와 작성자 비교)
    // 자신이 작성한 댓글이 아니면 삭제버튼이 안보이게 하는것도 하나의 방법
    @DeleteMapping("/delComm/{id}")
    ResponseEntity<String> delComm(@PathVariable("id") Long id, Authentication auth) {
        Optional<Comment> result = commentRepository.findById(id);

        if (!result.isPresent()) {
            return ResponseEntity.status(400).body("Comment not found");
        }

        Comment comment = result.get();
        CustomUser customUser = (CustomUser) auth.getPrincipal();

        if (comment.getUsername().equals(customUser.getUsername())) {
            commentRepository.deleteById(id);
            return ResponseEntity.status(200).body("delete success");
        }

        return ResponseEntity.status(200).body("del success");
    }
}
