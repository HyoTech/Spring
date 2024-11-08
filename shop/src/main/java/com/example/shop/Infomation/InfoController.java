package com.example.shop.Infomation;

import java.util.*;
import java.util.stream.Collectors;

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

import com.example.shop.Comment.Comment;
import com.example.shop.Comment.CommentRepository;
import com.example.shop.User.MyUserDetailsService.CustomUser;

@Controller
@RequiredArgsConstructor
public class InfoController {
    private final InfoRepository infoRepository;
    private final CommentRepository commentRepository;

    @GetMapping("/InfoWrite")
    public String InfoWrite() {
        return "InfoWrite.html";
    }

    @PostMapping("/SubmitInfo")
    public String savedInfo(@RequestParam("InfoTitle") String InfoTilte, @RequestParam("Writer") String Writer,
            @RequestParam("InfoValue") String InfoValue, @RequestParam("InfoDate") String InfoDate) {
        Information info = new Information();

        info.setInfo_Date(InfoDate);
        info.setInfo_Title(InfoTilte);
        info.setInfo_Value(InfoValue);
        info.setWriter(Writer);
        info.setView(0);
        infoRepository.save(info);
        return "redirect:/info";
    }

    @GetMapping("/detailInfo/{id}")
    public String detailInfo(@PathVariable("id") long id, Model model) {
        Optional<Information> Result = infoRepository.findById(id);
        List<Comment> commentResult = commentRepository.findByParentId(id);

        if (Result.isPresent()) {
            model.addAttribute("dInfo", Result.get());
        }

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

        return "detailInfo.html";
    }

    // 조회수 관련 API
    @PostMapping("/countClick/{id}")
    ResponseEntity<String> plusViewCount(@PathVariable("id") long id, @RequestBody Map<String, Integer> body) {
        Optional<Information> result = infoRepository.findById(id);

        if (result.isPresent()) {
            Information info = result.get();
            Integer view = body.get("View");
            info.setView(view);
            infoRepository.save(info);
        }

        return ResponseEntity.status(200).body("Count");
    }

    // 1, 글 삭제 버튼을 누르면 글 번호와 딜리트 요청을 서버로 보내줌
    // 2, 삭제 조건 : 글이 존재할 것, 자신이 작성한 글일 것
    // 3, 자신이 작성한 글인것을 판단할 조건 : 현재 로그인한 아이디와 작성자를 비교
    @DeleteMapping("/deleteInfo/{id}")
    ResponseEntity<String> delInfo(@PathVariable("id") long id,
            Authentication auth) {
        Optional<Information> result = infoRepository.findById(id);

        if (!result.isPresent()) {
            return ResponseEntity.status(404).body("Information not found");
        }

        Information info = result.get();
        CustomUser user = (CustomUser) auth.getPrincipal();

        if (info.getWriter().equals(user.getUsername())) {
            infoRepository.deleteById(id);
            return ResponseEntity.status(200).body("delete success");
        }

        return ResponseEntity.status(403).body("delete fail");
    }

}
