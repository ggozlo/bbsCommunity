package ggozlo.bbsCommunity.domain.post.repository;

import com.querydsl.core.QueryResults;
import com.querydsl.jpa.impl.JPAQueryFactory;
import ggozlo.bbsCommunity.domain.board.QBoard;
import ggozlo.bbsCommunity.domain.member.QMember;
import ggozlo.bbsCommunity.domain.post.Post;
import ggozlo.bbsCommunity.domain.post.QPost;
import ggozlo.bbsCommunity.global.dto.member.MemberPostList;
import ggozlo.bbsCommunity.global.dto.member.QMemberPostList;
import ggozlo.bbsCommunity.global.dto.post.*;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.time.LocalDateTime;
import java.util.Optional;

@RequiredArgsConstructor
@Transactional
public class PostJpaRepositoryImpl implements PostJpaRepository {

    @PersistenceContext
    private EntityManager manager;

    private final JPAQueryFactory queryFactory;

    private final QPost qPost = QPost.post;
    private final QMember qMember = QMember.member;
    private final QBoard qBoard = QBoard.board;

    @Override
    public Long persistPost(Post post) {
        manager.persist(post);
        return post.getId();
    }

    @Transactional(readOnly = true)
    public Optional<Post> viewPost(Long postId) {
        return Optional.ofNullable(queryFactory
                .selectFrom(qPost)
                .join(qPost.member).fetchJoin()
                .join(qPost.board).fetchJoin()
                .where(qPost.id.eq(postId))
                .fetchOne());
    }

    public void incrementViews(Long postId) {
        manager
                .createQuery("update Post p set p.views = p.views + 1 where  p.id = :postId")
                .setParameter("postId", postId)
                .executeUpdate();
    }

    @Override
    public void updatePost(Long postId, PostModifyFormDto modifyPost) {
        queryFactory
                .update(qPost)
                .set(qPost.title, modifyPost.getTitle())
                .set(qPost.content, modifyPost.getContent())
                .set(qPost.lastModifiedDate, LocalDateTime.now())
                .where(qPost.id.eq(postId))
                .execute();
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<PostModifyFormDto> findModifyPost(Long postId) {
        return Optional.ofNullable(queryFactory
                .select(new QPostModifyFormDto(qBoard.name.as("boardName"), qPost.title, qPost.content))
                .from(qPost)
                .join(qPost.board, qBoard)
                .where(qPost.id.eq(postId))
                .fetchOne());
    }

    @Override
    public Page<MemberPostList> memberPostPage(Long memberId, Pageable pageable) {
        QueryResults<MemberPostList> memberPostPage = queryFactory.
                select(new QMemberPostList(
                        qPost.id.as("postId"),
                        qPost.title,
                        qPost.member.nickname.as("authorNickname"),
                        qPost.board.address.as("boardAddress"),
                        qPost.board.name.as("boardName"),
                        qPost.views,
                        qPost.lastModifiedDate))
                .from(qPost)
                .join(qPost.board)
                .join(qPost.member)
                .where(qPost.member.id.eq(memberId))
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .orderBy(qPost.id.desc())
                .fetchResults();

        return new PageImpl<MemberPostList>(memberPostPage.getResults(), pageable, memberPostPage.getTotal());
    }


}
