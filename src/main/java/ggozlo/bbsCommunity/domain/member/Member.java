package ggozlo.bbsCommunity.domain.member;

import ggozlo.bbsCommunity.domain.comment.Comment;
import ggozlo.bbsCommunity.domain.member.authority.Authority;
import ggozlo.bbsCommunity.domain.post.Post;
import ggozlo.bbsCommunity.global.entity.BaseTimeEntity;
import lombok.*;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@NoArgsConstructor
@SequenceGenerator(
        sequenceName = "memberSeq",
        name = "memberSeq"
)
public class Member extends BaseTimeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "memberSeq")
    private Long id;

    @Setter
    @Column(length = 20, unique = true, nullable = false)
    private String username;

    @Setter
    @Column(length = 100, nullable = false)
    private String password;

    @Setter
    @Column(length = 320, unique = true, nullable = false)
    private String email;

    @Setter
    @Column(length = 20, unique = true, nullable = false)
    private String nickname;

    @OneToMany(orphanRemoval = true, mappedBy = "member")
    private List<Post> postList = new ArrayList<>();

    @OneToMany(orphanRemoval = true, mappedBy = "member")
    private List<Comment> commentList = new ArrayList<>();

    @OneToMany(orphanRemoval = true, mappedBy = "member")
    private List<Authority> authorityList = new ArrayList<>();




    @Builder
    public Member(String username, String password, String email, String nickname) {
        this.username = username;
        this.password = password;
        this.email = email;
        this.nickname = nickname;
    }


}
