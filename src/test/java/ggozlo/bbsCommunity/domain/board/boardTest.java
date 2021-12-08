package ggozlo.bbsCommunity.domain.board;

import com.querydsl.jpa.impl.JPAQueryFactory;
import ggozlo.bbsCommunity.domain.member.QMember;
import ggozlo.bbsCommunity.domain.member.authority.Authority;
import ggozlo.bbsCommunity.domain.member.authority.QAuthority;
import ggozlo.bbsCommunity.domain.post.QPost;
import ggozlo.bbsCommunity.global.dto.board.BoardDto;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@SpringBootTest
public class boardTest {

    @Autowired
    JPAQueryFactory queryFactory;

    private final QBoard qBoard = QBoard.board;
    private final QPost qPost = QPost.post;
    private final QMember qMember = QMember.member;
    private final QAuthority qAuthority = QAuthority.authority;

    @Test
    @Transactional
    public void boardPrimeManagerJoinTest() {


    }
}
