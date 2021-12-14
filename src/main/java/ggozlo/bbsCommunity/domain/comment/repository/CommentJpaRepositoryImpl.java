package ggozlo.bbsCommunity.domain.comment.repository;

import com.querydsl.core.QueryResults;
import com.querydsl.jpa.impl.JPAQueryFactory;
import ggozlo.bbsCommunity.domain.comment.QComment;
import ggozlo.bbsCommunity.domain.member.QMember;
import ggozlo.bbsCommunity.domain.post.QPost;
import ggozlo.bbsCommunity.global.dto.comment.CommentListDto;
import ggozlo.bbsCommunity.global.dto.comment.QCommentListDto;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.transaction.annotation.Transactional;

@Transactional
@RequiredArgsConstructor
public class CommentJpaRepositoryImpl implements CommentJpaRepository{

    private final JPAQueryFactory queryFactory;

    private QComment qComment = QComment.comment;
    private QPost qPost = QPost.post;
    private QMember qMember = QMember.member;

    @Override
    public Page<CommentListDto> memberCommentPage(Long memberId, Pageable pageable) {
        QueryResults<CommentListDto> fetchResults = queryFactory
                .select(new QCommentListDto(
                        qComment.post.id.as("postId"),
                        qComment.post.title.as("postTitle"),
                        qComment.post.board.address.as("boardAddress"),
                        qComment.content,
                        qComment.createDate))
                .from(qComment)
                .join(qComment.post)
                .join(qComment.post.board)
                .where(qComment.member.id.eq(memberId))
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .orderBy(qComment.id.desc())
                .fetchResults();
        return new PageImpl<CommentListDto>(fetchResults.getResults(), pageable, fetchResults.getTotal());
    }
}
