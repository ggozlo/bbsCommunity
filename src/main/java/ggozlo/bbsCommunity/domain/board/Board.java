package ggozlo.bbsCommunity.domain.board;


import ggozlo.bbsCommunity.domain.member.authority.Authority;
import ggozlo.bbsCommunity.domain.post.Post;
import ggozlo.bbsCommunity.global.entity.BaseTimeEntity;
import lombok.*;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import java.util.ArrayList;
import java.util.List;

/**
 * 게시판 테이블의 PK는 문자열인 address 로 지정하였다
 * db 성능에 이슈가 있겟지만 경험상 사용한다.
 */

@Entity
@NoArgsConstructor
@Getter @Setter
@EqualsAndHashCode(of = "address")
public class Board extends BaseTimeEntity {

    @Id
    @Column(length = 30, nullable = false)
    private String address;

    @Column(length = 50, unique = true, nullable = false)
    private String name;

    private boolean activation;

    @Column(length = 255, nullable = true)
    private String describe;

    public Board(String address) {
        this.address = address;
    }

    /**
     * 사용자가 게시판 생성을 요청할때 호출될 생성자 입니다.
     * @param address 신청된 게시판 이름 (영어만 가능)
     * @param name 게시판 별명
     * @param describe 게시판 설명 (필수사항 x)
     * @param Applicant 생성 요청자
     */
    public Board(String address, String name, String describe, Authority Applicant) {
        this.address = address;
        this.name = name;
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
