package com.ch.bojbm.domain.bookmark;


import com.ch.bojbm.domain.bookmark.dto.BookmarkInListResponseDto;
import com.ch.bojbm.domain.bookmark.dto.BookmarkListResponseDto;
import com.ch.bojbm.domain.user.Users;
import com.ch.bojbm.domain.user.UsersRepository;
import com.ch.bojbm.domain.user.dto.UsersRequestDto;
import com.ch.bojbm.domain.user.dto.UsersResponseDto;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.userdetails.User;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Optional;

@Slf4j
@Service
@RequiredArgsConstructor
@Transactional
public class BookmarkService {
    private final BookmarkJpaRepository bookmarkJpaRepository;
    private final UsersRepository usersRepository;

    public BookmarkListResponseDto getBookmarkList(User user){
        Users currentUsers = getUsers(user.getUsername());
        List<BookmarkInListResponseDto> bookmarks = new ArrayList<>();
        Iterator<Bookmark> iterator = bookmarkJpaRepository.findAllByUser(currentUsers).iterator();
        while(iterator.hasNext()){
            Bookmark temp = iterator.next();
            BookmarkInListResponseDto dto = BookmarkInListResponseDto.builder().problemNum(temp.getProblemNum()).notificationDate(temp.getNotificationDate()).build();
            bookmarks.add(dto);
        }
        return BookmarkListResponseDto.builder()
                .bookmarkList(bookmarks)
                .build();
    }


    public void addBookmark(User user, int problemNum, int afterDay){

        Users currentUsers = getUsers(user.getUsername());
        LocalDate today = LocalDate.now();

        Bookmark savedBookmark = bookmarkJpaRepository.findBookmarkByProblemNumAndUser(problemNum,currentUsers);

        if(savedBookmark == null){
            Bookmark newBookmark = Bookmark.builder()
                    .problemNum(problemNum)
                    .notificationDate(today.plusDays(afterDay))
                    .user(currentUsers)
                    .build();
            bookmarkJpaRepository.save(newBookmark);
        }else throw new RuntimeException("이미 존재하는 북마크입니다.");
    }

    public boolean checkBookmark(User user, Integer problemNum){
        Users currentUsers = getUsers(user.getUsername());
        Bookmark savedBookmark = bookmarkJpaRepository.findBookmarkByProblemNumAndUser(problemNum,currentUsers);

        if(savedBookmark == null){
            return false;
        }else{
            return true;
        }

    }

    public void deleteBookmark(User user, Integer problemNum) {
        
        Users currentUsers = getUsers(user.getUsername());
        Bookmark savedBookmark = bookmarkJpaRepository.findBookmarkByProblemNumAndUser(problemNum,currentUsers);

        if(savedBookmark == null){
            throw new RuntimeException("이미 존재하지 않는 북마크입니다.");
        }else {
            bookmarkJpaRepository.delete(savedBookmark);
        }

    }

    private Users getUsers(String email) {
        Optional<Users> member = usersRepository.findByEmail(email);
        if (member.isPresent()) {
            return member.get();
        } else throw new EntityNotFoundException("유저를 찾을 수 없습니다.");
    }

}
