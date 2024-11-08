package com.example.shop;

import java.util.List;

import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import com.example.shop.Infomation.InfoRepository;
import com.example.shop.Infomation.Information;
import com.example.shop.Item.Item;
import com.example.shop.Item.ItemRepository;
import com.example.shop.Item.ItemService;

import lombok.RequiredArgsConstructor;

@Controller
@RequiredArgsConstructor
public class BasicController {
    private final ItemRepository itemRepository;
    private final ItemService itemService;
    private final InfoRepository infoRepository;

    // 최근 등록된 물품을 보여주고
    // 더 보기 버튼을 누르면 전체 아이템 페이지로 이동
    // 최근 등록된 물품 세가지 정도만
    @GetMapping("/")
    String index(Model model) {
        List<Item> rank = itemRepository.findAll(Sort.by(Sort.Direction.DESC, "id"));
        List<Information> inforank = infoRepository.findAll(Sort.by(Sort.Direction.DESC, "id"));
        model.addAttribute("rank", rank.subList(0, Math.min(5, rank.size())));
        model.addAttribute("infos", inforank.subList(0, Math.min(6, inforank.size())));
        return "index.html";
    }
}
