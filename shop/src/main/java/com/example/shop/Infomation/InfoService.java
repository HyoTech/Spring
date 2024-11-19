package com.example.shop.Infomation;

import java.util.Map;
import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;

import com.example.shop.User.UserInfo;
import com.example.shop.User.UserRepository;
import com.example.shop.User.MyUserDetailsService.CustomUser;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class InfoService {
    private final InfoRepository infoRepository;
    private final UserRepository userRepository;

    public void infoSaved(@RequestParam("InfoTitle") String InfoTilte, @RequestParam("Writer") String Writer,
            @RequestParam("InfoValue") String InfoValue, @RequestParam("InfoDate") String InfoDate) {
        Information info = new Information();

        info.setInfo_Date(InfoDate);
        info.setInfo_Title(InfoTilte);
        info.setInfo_Value(InfoValue);
        info.setWriter(Writer);
        info.setView(0);
        infoRepository.save(info);
    }

    public void infoDetailed(@PathVariable("id") long id, Model model) {
        Optional<Information> Result = infoRepository.findById(id);

        if (Result.isPresent()) {
            model.addAttribute("dInfo", Result.get());
        }
    }

    public void pushView(@PathVariable("id") long id, @RequestBody Map<String, Integer> body) {
        Optional<Information> result = infoRepository.findById(id);

        if (result.isPresent()) {
            Information info = result.get();
            Integer view = body.get("View");
            info.setView(view);
            infoRepository.save(info);
        }
    }

    public boolean infoDeleted(Long id, Authentication auth) {
        Optional<Information> result = infoRepository.findById(id);
        Optional<UserInfo> uInfo = userRepository.findByUserName(auth.getName());

        if (result.isPresent()) {
            Information info = result.get();
            CustomUser user = (CustomUser) auth.getPrincipal();

            if (info.getWriter().equals(user.getUsername()) || uInfo.get().getAuthLevel() == 1) {
                infoRepository.deleteById(id);
                return true; // 삭제 성공
            }
        }
        return false; // 삭제 실패
    }

    public void pageinginfo(Model model, @PathVariable("id") Integer id) {
        Page<Information> result = infoRepository.findPageBy(PageRequest.of(id - 1, 5));
        model.addAttribute("infos", result);
        model.addAttribute("currentPage", id);
    }
}
