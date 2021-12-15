package ggozlo.bbsCommunity.domain.board.repository;

import com.querydsl.core.QueryResults;
import com.querydsl.core.Tuple;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.core.types.dsl.BooleanPath;
import com.querydsl.jpa.impl.JPAQueryFactory;
import ggozlo.bbsCommunity.domain.board.Board;
import ggozlo.bbsCommunity.domain.board.QBoard;
import ggozlo.bbsCommunity.domain.member.QMember;
import ggozlo.bbsCommunity.domain.member.authority.QAuthority;
import ggozlo.bbsCommunity.domain.post.QPost;
import ggozlo.bbsCommunity.global.dto.board.*;
import ggozlo.bbsCommunity.global.dto.post.PostListDto;
import ggozlo.bbsCommunity.global.dto.post.QPostListDto;
import ggozlo.bbsCommunity.global.exception.board.BoardDisabledException;
import ggozlo.bbsCommunity.global.exception.board.NotExistBoardException;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.time.LocalDateTime;
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
    public Optional<BoardMainDto> findBoardMain(String boardName, Pageable pageable, String  searchType, String parameter) {
        BoardMainDto boardMainDto = queryFactory
                .select(new QBoardMainDto(
                        qBoard.name.as("boardName"),
                        qBoard.describe,
                        qBoard.activation,
                        qBoard.createDate,
                        qBoard.lastModifiedDate))
                .from(qBoard)
                .where(qBoard.address.eq(boardName))
                .fetchOne();

         Page<PostListDto> postPage = getPostList(boardName, pageable, searchType, parameter );

        if (boardMainDto == null) throw new NotExistBoardException("Ex.Board.NotExist");
        boardMainDto.setPostPage(postPage);
        return Optional.ofNullable(boardMainDto);
    }

    private Page<PostListDto> getPostList(String boardName, Pageable pageable, String searchType, String parameter) {
        QueryResults<PostListDto> results = queryFactory
                .select(new QPostListDto(
                        qPost.id.as("postId"),
                        qPost.title,
                        qMember.nickname.as("authorNickname"),
                        qPost.views,
                        qPost.lastModifiedDate))
                .from(qPost)
                .join(qPost.member, qMember)
                .where(qPost.board.address.eq(boardName),
                        search(searchType, parameter))
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .orderBy(qPost.id.desc())
                .fetchResults();

        long totalPage = results.getTotal();
        List<PostListDto> postList = results.getResults();

        return new PageImpl<PostListDto>(postList, pageable, totalPage);
    }

    private BooleanExpression search(String type, String parameter) {
        if (parameter == null || type == null ) {
            return null;
        } else {
            SearchType searchType = SearchType.valueOf(type);
            if (searchType.equals(SearchType.titleAndContent)) {
                return qPost.title.contains(parameter).or(qPost.content.contains(parameter));
            } else if (searchType.equals(SearchType.title)) {
                return qPost.title.contains(parameter);
            } else if (searchType.equals(SearchType.content)) {
                return qPost.content.contains(parameter);
            } else if (searchType.equals(SearchType.author)) {
                return qPost.member.nickname.contains(parameter);
            } else {
                return null;
            }
        }
    }

    @Override
    @Transactional(readOnly = true)
    public List<Board> findActiveBoardList() {
        return queryFactory
                .select(qBoard)
                .from(qBoard)
                .join(qBoard.authorityList, qAuthority).fetchJoin()
                .join(qAuthority.member, qMember).fetchJoin()
                .where(qAuthority.role.contains("_Prime"))
                .fetch();
        // fetchjoin 때문에 Board 내용이 중복되는 row 가 Minor 매니저의 수 만큼 더 추가되는 현상이 있었다.
        // Prime 권한을 보유한 권한만 조회함으로써 문제를 해결했다
    }

    @Override
    public String findBoardNameByAddress(String boardAddress) {

        Tuple board = queryFactory.select(qBoard.name, qBoard.activation)
                .from(qBoard)
                .where(qBoard.address.eq(boardAddress))
                .fetchOne();

        if (!board.get(qBoard.activation)) {
            throw new BoardDisabledException("Ex.Board.Disabled");
        }
        String boardName = board.get(qBoard.name);

        return boardName;
    }

    @Override
    public void boardActivation(String boardAddress) {
        queryFactory
                .update(qBoard)
                .set(qBoard.activation, true)
                .where(qBoard.address.eq(boardAddress))
                .execute();
    }

    @Override
    public void boardDeactivation(String boardAddress) {
        queryFactory
                .update(qBoard)
                .set(qBoard.activation, false)
                .where(qBoard.address.eq(boardAddress))
                .execute();
    }

    @Override
    public BoardModifyDto findModifyBoard(String boardAddress) {
        return queryFactory
                .select(new QBoardModifyDto(qBoard.name, qBoard.describe))
                .from(qBoard)
                .where(qBoard.address.eq(boardAddress))
                .fetchOne();
    }

    @Override
    public void updateBoard(String boardAddress, BoardModifyDto modifyDto) {
        queryFactory
                .update(qBoard)
                .set(qBoard.name, modifyDto.getName())
                .set(qBoard.describe, modifyDto.getDescribe())
                .set(qBoard.lastModifiedDate, LocalDateTime.now())
                .where(qBoard.address.eq(boardAddress))
                .execute();
    }

    @Override
    public List<Board> findBoardSearch(String parameter) {
        return queryFactory
                .selectFrom(qBoard)
                .where(qBoard.address.contains(parameter)
                        .or(qBoard.name.contains(parameter))
                        .or(qBoard.authorityList.any().member.nickname.contains(parameter)))
                .fetch();


    }
}
