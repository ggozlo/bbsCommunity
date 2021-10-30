package ggozlo.bbsCommunity.domain.post;

import ggozlo.bbsCommunity.domain.board.Board;
import ggozlo.bbsCommunity.domain.comment.Comment;
import ggozlo.bbsCommunity.domain.member.Member;
import ggozlo.bbsCommunity.global.entity.BaseTimeEntity;
import lombok.*;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
@Getter @Setter
@NoArgsConstructor
@SequenceGenerator(
        sequenceName = "postSeq", name = "postSeq"
)
@EqualsAndHashCode(of = {"id", "member", "board", "title"})
public class Post extends BaseTimeEntity {

    @Id
    @GeneratedValue(generator = "postSeq", strategy = GenerationType.SEQUENCE)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(nullable = false)
    private Member member;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(nullable = false)
    private Board board;

    @Column(length = 50, nullable = false)
    private String title;

    @Lob
    private String content;


    private Integer views;

    public Post(Member member, Board board, String title, String content) {
        this.member = member;
        this.board = board;
        this.title = title;
        this.content = content;
    }


    //
    @OneToMany(mappedBy = "post", orphanRemoval = true)
    private List<Comment> commentList = new ArrayList<>();
}
