package ggozlo.bbsCommunity.domain.board.repository;

import com.querydsl.core.Tuple;
import com.querydsl.core.types.Projections;
import com.querydsl.jpa.impl.JPAQueryFactory;
import ggozlo.bbsCommunity.domain.board.Board;
import ggozlo.bbsCommunity.domain.board.QBoard;
import ggozlo.bbsCommunity.domain.member.QMember;
import ggozlo.bbsCommunity.domain.post.QPost;
import ggozlo.bbsCommunity.global.dto.board.BoardMainDto;
import ggozlo.bbsCommunity.global.dto.board.QBoardMainDto;
import ggozlo.bbsCommunity.global.dto.post.PostListDto;
import ggozlo.bbsCommunity.global.dto.post.QPostListDto;
import ggozlo.bbsCommunity.global.exception.board.NotExistBoardException;
import lombok.RequiredArgsConstructor;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.util.ArrayList;
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

    @Override
    public String persistBoard(Board board) {
        manager.persist(board);
        return board.getAddress();
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<BoardMainDto> findBoardMain(String boardName) {
        BoardMainDto boardMainDto = queryFactory
                .select(new QBoardMainDto(
                        qBoard.name.as("boardName"),
                        qBoard.describe))
                .from(qBoard)
                .where(qBoard.address.eq(boardName))
                .fetchOne();

        List<PostListDto> PostListDto = (ArrayList<PostListDto>) queryFactory
                .select(new QPostListDto(
                        qPost.id.as("postId"),
                        qPost.title,
                        qMember.nickname.as("authorNickname"),
                        qPost.views))
                .from(qPost)
                .join(qPost.member, qMember)
                .where(qPost.board.address.eq(boardName))
                .fetch();
        if (boardMainDto == null) throw new NotExistBoardException("Ex.Board.NotExist");
        boardMainDto.setPostList(PostListDto);
        return Optional.ofNullable(boardMainDto);
    }


}
