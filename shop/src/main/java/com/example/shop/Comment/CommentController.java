package com.example.shop.Comment;

import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.stereotype.Service;

import lombok.RequiredArgsConstructor;

import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Service
@Controller
@RequiredArgsConstructor
public class CommentController {
    private final CommentService commentService;

    @PostMapping("/detail/comment")
    public String postItemComment(@RequestParam("username") String username, @RequestParam("comment") String comment,
            @RequestParam("parentid") long parentid,
            @RequestParam("parentcategory") long parentcategory) {
        commentService.itemCommentSaved(username, comment, parentid, parentcategory);
        return "redirect:/list/page/1";
    }

    // 1, parent_category라는 속성을 만듬
    // 2, 상품의 경우 1, 공지사항의 경우 2 -> 카테고리라는 테이블을 새로 만들어야할 필요?
    // 3, 댓글 작성 api에서 상품의 경우 1을 디비에 저장하고, get방식 때 1이면 아이템 코멘트 보여주는 방식으로 제작
    @PostMapping("/detailInfo/comment")
    public String postInfoComment(@RequestParam("username") String username, @RequestParam("comment") String comment,
            @RequestParam("parentid") long parentid,
            @RequestParam("parentcategory") long parentcategory) {
        commentService.infoCommentSaved(username, comment, parentid, parentcategory);
        return "redirect:/list/infopage/1";
    }

    // 댓글 삭제기능
    // 자신이 작성한 댓글만 삭제 가능하게(현재 로그인한 유저의 아이디와 작성자 비교)
    // 자신이 작성한 댓글이 아니면 삭제버튼이 안보이게 하는것도 하나의 방법
    @DeleteMapping("/delComm/{id}")
    ResponseEntity<String> delComm(@PathVariable("id") Long id, Authentication auth) {
        boolean deleteSuccess = commentService.commentDeleted(id, auth);

        if (deleteSuccess) {
            return ResponseEntity.status(200).body("삭제 성공");
        } else {
            return ResponseEntity.status(403).body("삭제 실패: 권한이 없거나 댓글이 존재하지 않습니다.");
        }
    }
}
