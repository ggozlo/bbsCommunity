package ggozlo.bbsCommunity.domain.entity;

import lombok.*;

import javax.persistence.*;

@Entity
@SequenceGenerator(
        sequenceName = "commentSeq",  name = "commentSeq"
)
@NoArgsConstructor
@EqualsAndHashCode(of = {"id", "member", "post"})
@Getter @Setter
public class Comment {

    @Id
    @GeneratedValue(generator = "commentSeq", strategy = GenerationType.SEQUENCE)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(nullable = false)
    private Member member;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(nullable = false)
    private Post post;

    @Lob
    private String content;

    @Builder
    public Comment(Member member, Post post, String content) {
        this.member = member;
        this.post = post;
        this.content = content;
    }
}
