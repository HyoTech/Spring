package com.example.shop.Infomation;

import java.util.*;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
@RequiredArgsConstructor
public class InfoController {
    private final InfoRepository infoRepository;

    @GetMapping("/InfoWrite")
    public String InfoWrite() {
        return "InfoWrite.html";
    }

    @PostMapping("/SubmitInfo")
    public String postMethodName(@RequestParam("InfoTitle") String InfoTilte, @RequestParam("Writer") String Writer,
            @RequestParam("InfoValue") String InfoValue, @RequestParam("InfoDate") String InfoDate) {
        Information info = new Information();
        info.setInfo_Date(InfoDate);
        info.setInfo_Title(InfoTilte);
        info.setInfo_Value(InfoValue);
        info.setWriter(Writer);
        infoRepository.save(info);
        return "Info.html";
    }

    @GetMapping("/detailInfo/{id}")
    public String postMethodName(@PathVariable("id") long id, Model model) {
        Optional<Information> Result = infoRepository.findById(id);

        if (Result.isPresent()) {
            model.addAttribute("dInfo", Result.get());
        }
        return "detailInfo.html";
    }

}
