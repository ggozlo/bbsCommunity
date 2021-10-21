package ggozlo.bbsCommunity.domain.board;


import ggozlo.bbsCommunity.domain.member.authority.Authority;
import ggozlo.bbsCommunity.domain.post.Post;
import lombok.*;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import java.util.ArrayList;
import java.util.List;

@Entity
@NoArgsConstructor
@EqualsAndHashCode(of ={"nickname", "nickName"})
@Getter
public class Board {

    @Id
    @Column(length = 30, nullable = false)
    private String name;

    @Column(length = 50, unique = true, nullable = false)
    private String nickName;

    private boolean activation;

    @Column(length = 255, nullable = true)
    private String describe;

    public Board(String name) {
        this.name = name;
    }

    /**
     * 사용자가 게시판 생성을 요청할때 호출될 생성자 입니다.
     * @param name 신청된 게시판 이름 (영어만 가능)
     * @param nickName 게시판 별명
     * @param describe 게시판 설명 (필수사항 x)
     * @param Applicant 생성 요청자
     */
    public Board(String name, String nickName, String describe, Authority Applicant) {
        this.name = name;
        this.nickName = nickName;
        this.describe = describe;
        activation = false;
        authorityList.add(Applicant);
    }

    //
    @OneToMany(mappedBy = "board", orphanRemoval = true)
    private List<Post> postList = new ArrayList<>();

    @OneToMany(orphanRemoval = true, mappedBy = "board")
    private List<Authority> authorityList = new ArrayList<>();

}
