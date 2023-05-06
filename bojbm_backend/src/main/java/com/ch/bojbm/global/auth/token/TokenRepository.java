package com.ch.bojbm.global.auth.token;


import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

// @Repository 생략 가능 -> 프록시 구현체 만들때 Spring Data JPA가 컴포넌트 스캔을 자동으로 처리해줌
public interface TokenRepository extends JpaRepository<Token,Long> {
    @Query("""
select t from Token t inner join Users u
on t.user.id = u.id\s
where u.id = :id and (t.expired=false or t.revoked=false)\s
""")
    List<Token> findAllValidTokenByUsers(Long id);

    Optional<Token> findByToken(String token);
}
