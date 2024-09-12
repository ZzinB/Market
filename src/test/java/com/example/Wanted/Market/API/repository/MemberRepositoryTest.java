package com.example.Wanted.Market.API.repository;

import com.example.Wanted.Market.API.domain.Member;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
class MemberRepositoryTest {
    @Autowired
    private MemberRepository memberRepository;

    @Test
    @DisplayName("회원검색 - 이메일 찾기")
    public void testFindByEmail() {
        // Given
        Member member = new Member();
        member.setEmail("test@example.com");
        memberRepository.save(member);

        // When
        Member foundMember = memberRepository.findByEmail("test@example.com").orElse(null);

        // Then
        assertThat(foundMember).isNotNull();
        assertThat(foundMember.getEmail()).isEqualTo("test@example.com");
    }
}
