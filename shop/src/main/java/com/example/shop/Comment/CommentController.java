package com.example.shop.Comment;

import org.springframework.stereotype.Controller;
import org.springframework.stereotype.Service;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Service
@Controller
@RequiredArgsConstructor
public class CommentController {
    private final CommentRepository commentRepository;

    @PostMapping("/detail/comment")
    public String postMethodName(@RequestParam String username, String comment, long parentid) {
        Comment newComment = new Comment();
        newComment.setUsername(username);
        newComment.setContent(comment);
        newComment.setparentId(parentid);
        commentRepository.save(newComment);
        return "redirect:/list/page/1";
    }

}
