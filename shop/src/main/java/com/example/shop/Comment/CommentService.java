package com.example.shop.Comment;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;

import com.example.shop.User.MyUserDetailsService.CustomUser;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class CommentService {
    private final CommentRepository commentRepository;

    public void itemCommentSaved(@RequestParam("username") String username, @RequestParam("comment") String comment,
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
    }

    public boolean commentDeleted(Long id, Authentication auth) {
        Optional<Comment> result = commentRepository.findById(id);

        if (result.isEmpty()) {
            return false; // 댓글이 존재하지 않으면 false 반환
        }

        Comment comment = result.get();
        CustomUser customUser = (CustomUser) auth.getPrincipal();

        System.out.println("comment = " + comment.getUsername());
        // 댓글 작성자와 로그인한 사용자가 일치하는지 확인
        if (comment.getUsername().equals(customUser.getUsername())) {
            commentRepository.deleteById(id); // 댓글 삭제
            return true; // 삭제 성공
        }

        return false; // 권한이 없거나 조건을 만족하지 않으면 false 반환
    }

    public void infoCommentSaved(@RequestParam("username") String username, @RequestParam("comment") String comment,
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
    }

    public void itemCommentShow(@PathVariable("id") long id, Model model) {
        List<Comment> commentResult = commentRepository.findByParentId(id);

        if (!commentResult.isEmpty()) {
            List<Comment> filteredComments = commentResult.stream()
                    .filter(comment -> comment.getParentCategory() == 1)
                    .collect(Collectors.toList());

            if (!filteredComments.isEmpty()) {
                model.addAttribute("comment", filteredComments);
            }
        } else {
            model.addAttribute("comment", null);
        }

    }

    public void infoCommentShow(@PathVariable("id") long id, Model model) {
        List<Comment> commentResult = commentRepository.findByParentId(id);

        // 공지사항의 경우 카테고리로 공지사항인지 아닌지 판단 후 공지사항인 경우에만 R기능 수행
        // 상품의 경우도 마찬가지 category만 바꿔주면 됨(동일 테이블 사용)
        if (!commentResult.isEmpty()) {
            List<Comment> filteredComments = commentResult.stream()
                    .filter(comment -> comment.getParentCategory() == 2)
                    .collect(Collectors.toList());

            if (!filteredComments.isEmpty()) {
                model.addAttribute("comment", filteredComments);
            }
        } else {
            model.addAttribute("comment", null);
        }
    }
}
