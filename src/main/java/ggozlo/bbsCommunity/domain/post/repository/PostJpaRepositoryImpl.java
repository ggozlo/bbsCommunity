package ggozlo.bbsCommunity.domain.post.repository;

import com.querydsl.jpa.impl.JPAQueryFactory;
import ggozlo.bbsCommunity.domain.board.QBoard;
import ggozlo.bbsCommunity.domain.member.Member;
import ggozlo.bbsCommunity.domain.member.QMember;
import ggozlo.bbsCommunity.domain.post.Post;
import ggozlo.bbsCommunity.domain.post.QPost;
import lombok.RequiredArgsConstructor;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
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

//    @Transactional(readOnly = true)
//    public String findAuthor(Long postId) {
//        return queryFactory
//                .select(qPost.member.username)
//                .from(qPost)
//                .join(qPost.member)
//                .where(qPost.id.eq(postId))
//                .fetchOne();
//    }

}
