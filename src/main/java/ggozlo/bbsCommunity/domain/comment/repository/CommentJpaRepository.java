package ggozlo.bbsCommunity.domain.comment.repository;

import ggozlo.bbsCommunity.global.dto.comment.CommentListDto;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface CommentJpaRepository {
    Page<CommentListDto> memberCommentPage(Long memberId, Pageable pageable);
}
