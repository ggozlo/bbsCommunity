package ggozlo.bbsCommunity.domain.entity;

import lombok.*;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
@Getter @Setter
@NoArgsConstructor
@SequenceGenerator(
        sequenceName = "memberSeq",
        name = "memberSeq"
)
@EqualsAndHashCode(of = {"id", "name"})
public class Member {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "memberSeq")
    private Long id;

    @Column(length = 20, unique = true, nullable = false)
    private String loginId;

    @Column(length = 18, nullable = false)
    private String password;

    @Column(length = 320, unique = true, nullable = false)
    private String email;

    @Column(length = 20, unique = true, nullable = false)
    private String name;

    @Builder
    public Member(String loginId, String password, String email, String name) {
        this.loginId = loginId;
        this.password = password;
        this.email = email;
        this.name = name;
    }

    @OneToMany(orphanRemoval = true, mappedBy = "member")
    private List<Post> postList = new ArrayList<>();

    @OneToMany(orphanRemoval = true, mappedBy = "member")
    private List<Comment> commentList = new ArrayList<>();

    @OneToMany(orphanRemoval = true, mappedBy = "member")
    private List<Authority> authorityList = new ArrayList<>();



}
