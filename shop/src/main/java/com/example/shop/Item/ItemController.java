package com.example.shop.Item;

import java.util.Map;

import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.stereotype.Service;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import org.springframework.security.core.Authentication;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.example.shop.Comment.CommentService;

@Service
@Controller
@RequiredArgsConstructor
public class ItemController {
    private final ItemService itemService;
    private final S3Service s3Service;
    private final CommentService commentService;

    // 공지사항 API
    @GetMapping("/info")
    public String showInfo(Model model) {
        itemService.showInfoList(model);
        return "info.html";
    }

    // 상품입력 폼으로 이동
    @GetMapping("/write")
    String write() {
        return "write.html";
    }

    // 상품입력 폼에서 작성한 상품 정보들을 서버로 보내주어 검사 후 DB에 저장
    @PostMapping("/add")
    String postWrite(@RequestParam("title") String title, @RequestParam("price") Integer price,
            @RequestParam("writer") String writer,
            @RequestParam("image") String image,
            Authentication auth) {
        if (auth.isAuthenticated()) {
            itemService.addItem(title, price, writer, image, auth);
        }
        return "redirect:/list/page/1";
    }

    // 상품상세페이지, 아이템 테이블의 ID컬럼을 이용하여 몇번째 상품인지 확인
    // URL ID를 이용해서 items의 id에 맞게 상세페이지를 보여주기
    // 코멘트 테이블을 공지사항, 물품리스트 두 테이블이 같이 사용하고 있기때문에 구분 코드 추가(ParentID 겹칠것을 우려)
    @GetMapping("/list/page/detail/{id}")
    String detail(@PathVariable("id") long id, Model model) {
        commentService.itemCommentShow(id, model);
        itemService.showDetail(id, model);
        return "detail.html";
    }

    // 상품 수정 관련 API
    @GetMapping("/list/page/modify/{id}")
    public String modify(@PathVariable("id") long id, Model model) {
        itemService.modInfo(id, model);
        return "modify.html";
    }

    @PostMapping("/mod/{id}")
    String postModify(@PathVariable("id") long id, @RequestParam("title") String title,
            @RequestParam("price") Integer price,
            @RequestParam("image") String image,
            @RequestParam("oldImageUrl") String oldImageUrl) {
        itemService.modItem(id, title, price, image, oldImageUrl);
        return "redirect:/list/page/1";
    }

    // 상품 삭제 API
    @DeleteMapping("/del/{id}")
    ResponseEntity<String> deleItem(@PathVariable("id") Long id, @RequestBody Map<String, String> body,
            Authentication auth) {
        String objectKey = body.get("ObjectKey");

        boolean deleteSuccess = itemService.DeItem(id, auth);

        if (objectKey.isEmpty()) {
            objectKey = "";
        }

        if (!deleteSuccess) {
            return ResponseEntity.status(403).body("삭제 실패: 권한이 없거나 아이템이 존재하지 않습니다.");
        }

        try {
            s3Service.deleteFileFromS3(objectKey);
            return ResponseEntity.status(200).body("삭제 완료");
        } catch (Exception e) {
            return ResponseEntity.status(500).body("S3 삭제 중 오류 발생: " + e.getMessage());
        }
    }

    // DB에 있는 상품명과 가격을 카드형태로 보여줌, 페이징 기능
    @GetMapping("/list/page/{id}")
    String pagelist(Model model, @PathVariable("id") Integer id) {
        itemService.paging(model, id);
        return "list.html";
    }

    // presigned-url 요청을 받으면 사용자 쪽으로 url을 전달해주는 API
    @GetMapping("presigned-url")
    @ResponseBody
    String getURL(@RequestParam("filename") String filename) {
        var result = s3Service.createPresignedUrl("test/" + filename);
        return result;
    }

    @GetMapping("/search")
    String getSearch(Model model, @RequestParam("searchText") String searchText,
            @RequestParam(name = "page", defaultValue = "0") int page) {
        itemService.searchItem(model, searchText, page);
        return "search.html";
    }
}