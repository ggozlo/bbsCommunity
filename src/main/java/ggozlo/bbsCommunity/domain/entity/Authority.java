package ggozlo.bbsCommunity.domain.entity;

import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Entity
@Getter
@NoArgsConstructor
@EqualsAndHashCode(of = {"id","member", "board", "role"})
public class Authority {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(nullable = false)
    private Member member;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(nullable = true)
    private Board board;

    @Column(length = 50, nullable = false)
    private String role;

    @Builder
    public Authority(Member member, Board board, String role) {
        this.member = member;
        this.board = board;
        this.role = role;
    }


}
