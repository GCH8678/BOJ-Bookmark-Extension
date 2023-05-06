package com.ch.bojbm.domain.bookmark;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

@RequestMapping("/api/bookmark")
@Controller
@RequiredArgsConstructor
public class BookmarkController {
    BookmarkService bookmarkService;
    // 북마크 추가

}
