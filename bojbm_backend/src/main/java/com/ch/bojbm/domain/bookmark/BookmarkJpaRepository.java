package com.ch.bojbm.domain.bookmark;

import com.ch.bojbm.domain.user.Users;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Collection;
import java.util.List;

public interface BookmarkJpaRepository extends JpaRepository<Bookmark,Long> {

    Bookmark findBookmarkByProblemNumAndUser(int problemNum, Users user);
    List<Bookmark> findAllByUser(Users user);

}
