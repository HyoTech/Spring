package com.example.shop.Infomation;

import java.util.*;

import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;

import lombok.RequiredArgsConstructor;

import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;

import com.example.shop.Comment.CommentService;

@Controller
@RequiredArgsConstructor
public class InfoController {
    private final InfoService infoService;
    private final CommentService commentService;

    @GetMapping("/InfoWrite")
    public String InfoWrite() {
        return "InfoWrite.html";
    }

    @PostMapping("/SubmitInfo")
    public String savedInfo(@RequestParam("InfoTitle") String InfoTilte, @RequestParam("Writer") String Writer,
            @RequestParam("InfoValue") String InfoValue, @RequestParam("InfoDate") String InfoDate) {
        infoService.infoSaved(InfoTilte, Writer, InfoValue, InfoDate);
        return "redirect:/list/infopage/1";
    }

    @GetMapping("/detailInfo/{id}")
    public String detailInfo(@PathVariable("id") long id, Model model) {
        infoService.infoDetailed(id, model);
        commentService.infoCommentShow(id, model);
        return "detailInfo.html";
    }

    // 조회수 관련 API
    @PostMapping("/countClick/{id}")
    ResponseEntity<String> plusViewCount(@PathVariable("id") long id, @RequestBody Map<String, Integer> body) {
        infoService.pushView(id, body);
        return ResponseEntity.status(200).body("Count");
    }

    // 1, 글 삭제 버튼을 누르면 글 번호와 딜리트 요청을 서버로 보내줌
    // 2, 삭제 조건 : 글이 존재할 것, 자신이 작성한 글일 것
    // 3, 자신이 작성한 글인것을 판단할 조건 : 현재 로그인한 아이디와 작성자를 비교
    @DeleteMapping("/deleteInfo/{id}")
    ResponseEntity<String> delInfo(@PathVariable("id") long id,
            Authentication auth) {
        boolean deleteSuccess = infoService.infoDeleted(id, auth);

        if (deleteSuccess) {
            return ResponseEntity.status(200).body("삭제 성공");
        } else {
            return ResponseEntity.status(403).body("삭제 실패: 권한이 없거나 글이 존재하지 않습니다.");
        }
    }

    @GetMapping("/list/infopage/{id}")
    public String pageinfolist(Model model, @PathVariable("id") Integer id) {
        infoService.pageinginfo(model, id);
        return "info.html";
    }

}