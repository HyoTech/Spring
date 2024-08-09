package com.example.shop.Item;

import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.stereotype.Service;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Service
@Controller
@RequiredArgsConstructor
public class ItemController {
    private final ItemService itemService;

    // DB에 있는 상품명과 가격을 카드형태로 보여줌
    @GetMapping("/list")
    String list(Model model) {
        itemService.showList(model);
        return "list.html";
    }

    // 상품입력 폼으로 이동
    @GetMapping("/write")
    String write() {
        return "write.html";
    }

    // 상품입력 폼에서 작성한 상품 정보들을 서버로 보내주어 검사 후 DB에 저장
    @PostMapping("/add")
    String postWrite(@RequestParam String title, @RequestParam Integer price) {
        itemService.addItem(title, price);
        return "redirect:/list";

    }

    // 상품상세페이지, 아이템 테이블의 ID컬럼을 이용하여 몇번째 상품인지 확인
    // URL ID를 이용해서 items의 id에 맞게 상세페이지를 보여주기
    @GetMapping("/detail/{id}")
    String detail(@PathVariable long id, Model model) {
        itemService.showDetail(id, model);
        return "detail.html";
    }

    @GetMapping("/modify/{id}")
    public String modify(@PathVariable long id, Model model) {
        itemService.modInfo(id, model);
        return "modify.html";
    }

    @PostMapping("/mod/{id}")
    String postModify(@PathVariable long id, @RequestParam String title, @RequestParam Integer price) {
        itemService.modItem(id, title, price);
        return "redirect:/list";
    }

    @DeleteMapping("/del/{id}")
    ResponseEntity<String> deleItem(@PathVariable Long id) {
        itemService.DeItem(id);
        return ResponseEntity.status(200).body("삭제완료");
    }
}
