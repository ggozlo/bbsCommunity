package ggozlo.bbsCommunity.domain.comment.service;

import ggozlo.bbsCommunity.domain.comment.Comment;
import ggozlo.bbsCommunity.domain.comment.repository.CommentRepository;
import ggozlo.bbsCommunity.domain.member.Member;
import ggozlo.bbsCommunity.domain.member.repository.MemberRepository;
import ggozlo.bbsCommunity.domain.post.Post;
import ggozlo.bbsCommunity.domain.post.repository.PostRepository;
import ggozlo.bbsCommunity.global.dto.comment.CommentDto;
import ggozlo.bbsCommunity.global.exception.member.NotFoundMemberException;
import ggozlo.bbsCommunity.global.exception.post.NotFoundPostException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
public class CommentService {

    private final CommentRepository commentRepository;
    private final MemberRepository memberRepository;
    private  final PostRepository postRepository;

    @Transactional
    public void saveComment(Long memberId, Long postId, String comment) {

        Member member = memberRepository.findById(memberId)
                .orElseThrow(() -> new NotFoundMemberException("Ex.Member.NotFoundMember"));
        Post post = postRepository.findById(postId).orElseThrow(() -> new NotFoundPostException("Ex.Post.NotFound"));
        Comment newComment = new Comment(member, post, comment);
        commentRepository.save(newComment);
    }

    @Transactional(readOnly = true)
    public List<CommentDto> postComment(Long postId, String memberNickname) {
        List<Comment> commentList = commentRepository.findByPostId(postId);
        List<CommentDto> dtoList = commentList.stream()
                .map(entity -> {
                    CommentDto commentDto = new CommentDto(entity.getId(), entity.getMember().getNickname(), entity.getContent());
                    if (memberNickname.equals(commentDto.getAuthorNickname())) {
                        commentDto.setAuthor(true);
                    }
                    return commentDto;
                })
                .collect(Collectors.toList());

        return dtoList;
    }

    public void deleteComment(Long commentId) {
        commentRepository.deleteById(commentId);
    }
}
