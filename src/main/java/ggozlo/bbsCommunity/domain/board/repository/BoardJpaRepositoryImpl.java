package ggozlo.bbsCommunity.domain.board.repository;

import com.querydsl.core.QueryResults;
import com.querydsl.jpa.impl.JPAQueryFactory;
import ggozlo.bbsCommunity.domain.board.Board;
import ggozlo.bbsCommunity.domain.board.QBoard;
import ggozlo.bbsCommunity.domain.member.QMember;
import ggozlo.bbsCommunity.domain.member.authority.QAuthority;
import ggozlo.bbsCommunity.domain.post.QPost;
import ggozlo.bbsCommunity.global.dto.board.BoardMainDto;
import ggozlo.bbsCommunity.global.dto.board.QBoardMainDto;
import ggozlo.bbsCommunity.global.dto.post.PostListDto;
import ggozlo.bbsCommunity.global.dto.post.QPostListDto;
import ggozlo.bbsCommunity.global.exception.board.NotExistBoardException;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.util.List;
import java.util.Optional;

@Transactional
@RequiredArgsConstructor
public class BoardJpaRepositoryImpl implements BoardJpaRepository{

    @PersistenceContext
    EntityManager manager;

    private final JPAQueryFactory queryFactory;
    private final QBoard qBoard = QBoard.board;
    private final QPost qPost = QPost.post;
    private final QMember qMember = QMember.member;
    private final QAuthority qAuthority = QAuthority.authority;

    @Override
    public String persistBoard(Board board) {
        manager.persist(board);
        return board.getAddress();
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<BoardMainDto> findBoardMain(String boardName, Pageable pageable) {
        BoardMainDto boardMainDto = queryFactory
                .select(new QBoardMainDto(
                        qBoard.name.as("boardName"),
                        qBoard.describe))
                .from(qBoard)
                .where(qBoard.address.eq(boardName))
                .fetchOne();

         Page<PostListDto> postPage = getPostList(boardName, pageable );

        if (boardMainDto == null) throw new NotExistBoardException("Ex.Board.NotExist");
        boardMainDto.setPostPage(postPage);
        return Optional.ofNullable(boardMainDto);
    }

    private Page<PostListDto> getPostList(String boardName, Pageable pageable) {
        QueryResults<PostListDto> results = queryFactory
                .select(new QPostListDto(
                        qPost.id.as("postId"),
                        qPost.title,
                        qMember.nickname.as("authorNickname"),
                        qPost.views,
                        qPost.lastModifiedDate))
                .from(qPost)
                .join(qPost.member, qMember)
                .where(qPost.board.address.eq(boardName))
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .orderBy(qPost.id.desc())
                .fetchResults();

        long totalPage = results.getTotal();
        List<PostListDto> postList = results.getResults();

        return new PageImpl<PostListDto>(postList, pageable, totalPage);
    }

    @Override
    @Transactional(readOnly = true)
    public List<Board> findActiveBoardList() {
        return queryFactory
                .select(qBoard)
                .from(qBoard)
                .join(qBoard.authorityList, qAuthority).fetchJoin()
                .join(qAuthority.member, qMember).fetchJoin()
                .fetch();
    }


}
