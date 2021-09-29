package ggozlo.bbsCommunity.db;

import ggozlo.bbsCommunity.domain.entity.*;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.transaction.Transactional;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;

@SpringBootTest
public class MemberTest {

    @PersistenceContext
    private EntityManager entityManager;

    @Test
    @Transactional
    public void memberTest() {

        // 회원 객체 저장, 검색
        Member member = createMember("hello");
        entityManager.persist(member);
        flushAndClear(entityManager);
        Member findMember = entityManager.find(Member.class, member.getId());
        Assertions.assertEquals(member, findMember);

        // 수정
        findMember.setName("changed");
        flushAndClear(entityManager);
        Member changedMember = entityManager.find(Member.class, member.getId());
        Assertions.assertEquals(findMember, changedMember);

        // 삭제
        entityManager.remove(changedMember);
        flushAndClear(entityManager);
        Member removedMember = entityManager.find(Member.class, member.getId());
        Assertions.assertNull(removedMember);
    }

    @Test
    @Transactional
    public void postTest() {
        Member member = createMember("hello");
        Board board = createBoard(new Authority(), "board");
        Post post1 = createPost(member, board, "post1");
        Post post2 = createPost(member, board, "post2");
        Post post3 = createPost(member, board, "post3");

        entityManager.persist(member);
        entityManager.persist(board);
        entityManager.persist(post1);
        entityManager.persist(post2);
        entityManager.persist(post3);
        flushAndClear(entityManager);

        Post findPost = entityManager.find(Post.class, post1.getId());
        Assertions.assertEquals(post1, findPost);

        Post findPost2 = entityManager.find(Post.class, post2.getId());
        entityManager.remove(findPost2);
        flushAndClear(entityManager);
        List<Post> postList = entityManager.createQuery("select p from Post p", Post.class).getResultList();
        Assertions.assertArrayEquals(postList.toArray(), List.of(post1, post3).toArray());

        // 회원 삭제시 post 삭제 검사
        entityManager.clear();
        Member removeMember = entityManager.find(Member.class, member.getId());
        entityManager.remove(removeMember);
        entityManager.flush();
        List<Post> removedPostList = entityManager.createQuery("select p from Post p", Post.class).getResultList();
        Assertions.assertEquals(removedPostList.size(), 0);
    }


    @Test
    @Transactional
    public void boardTest() {
        Board board = createBoard(null, "hello");
        entityManager.persist(board);

        flushAndClear(entityManager);

        Board findBoard = entityManager.find(Board.class, board.getName());
        Assertions.assertEquals(board, findBoard);
    }

    @Test
    @Transactional
    public void AuthorityTest() {
        Board board = createBoard(null, "board");
        Member member = createMember("member");
        Authority authority = new Authority(member, board, board.getName() + "_manager");
        entityManager.persist(board);
        entityManager.persist(member);
        entityManager.persist(authority);
        flushAndClear(entityManager);

        Authority findAuthority = entityManager
                .createQuery("select a from Authority a", Authority.class).getSingleResult();
        System.out.println("=============================");
        Assertions.assertEquals(findAuthority, authority);
        Assertions.assertEquals(findAuthority.getMember(), member);
        Assertions.assertEquals(findAuthority.getBoard(), board);
    }

    @Test
    @Transactional
    public void commentTest() {
        Member member = createMember("member");
        Board board = createBoard(new Authority(), "board");
        Post post = createPost(member, board, "post");
        Comment comment = new Comment(member, post, "comment");
        Comment comment2 = new Comment(member, post, "comment2");
        member.getCommentList().add(comment);
        member.getCommentList().add(comment2);

        entityManager.persist(member);
        entityManager.persist(board);
        entityManager.persist(post);
        entityManager.persist(comment);
        entityManager.persist(comment2);
        flushAndClear(entityManager);

        Comment findComment = entityManager.find(Comment.class, comment.getId());
        Member findMember = entityManager.find(Member.class, member.getId());
        Assertions.assertEquals(comment, findComment);
        Assertions.assertEquals(comment.getMember(), member);
        Assertions.assertEquals(comment.getPost(), post);
        Assertions.assertEquals(findMember.getCommentList().size(), 2);

    }



    private void flushAndClear(EntityManager entityManager) {
        entityManager.flush();
        entityManager.clear();
    }

    private Board createBoard(Authority applicant, String name) {
        Board board = new Board(name, name + " 게시판", name + " 테스트용 게시판", applicant);
        return board;
    }

    private Post createPost(Member member, Board board, String name) {
        Post post = new Post(member, board, name + " title", name + " content");
        return post;
    }

    private Member createMember(String name) {
        Member member = Member.builder()
                .loginId("loginId" + name)
                .password("password" + name)
                .name(name)
                .email(name + "email.com")
                .build();
        return member;
    }
}
