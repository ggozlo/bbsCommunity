package ggozlo.bbsCommunity.domain.post.service;

import ggozlo.bbsCommunity.domain.board.Board;
import ggozlo.bbsCommunity.domain.board.repository.BoardRepository;
import ggozlo.bbsCommunity.domain.member.Member;
import ggozlo.bbsCommunity.domain.member.repository.MemberRepository;
import ggozlo.bbsCommunity.domain.post.Post;
import ggozlo.bbsCommunity.domain.post.repository.PostRepository;
import ggozlo.bbsCommunity.global.dto.post.PostModifyFormDto;
import ggozlo.bbsCommunity.global.dto.post.PostViewDto;
import ggozlo.bbsCommunity.global.dto.post.PostWriteDto;
import ggozlo.bbsCommunity.global.exception.board.BoardDisabledException;
import ggozlo.bbsCommunity.global.exception.board.BoardException;
import ggozlo.bbsCommunity.global.exception.board.NotExistBoardException;
import ggozlo.bbsCommunity.global.exception.member.NotFoundMemberException;
import ggozlo.bbsCommunity.global.exception.post.NotFoundPostException;
import ggozlo.bbsCommunity.global.security.detailsService.SessionMember;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Arrays;
import java.util.Optional;

@Slf4j
@Service
@Transactional
@RequiredArgsConstructor
public class PostService {

    private final PostRepository postRepository;
    private final MemberRepository memberRepository;
    private final BoardRepository boardRepository;
    private final ModelMapper modelMapper;

    public Long write(PostWriteDto postDto) {
        Board board = boardRepository.findById(postDto.getBoardAddress())
                .orElseThrow(() -> new NotExistBoardException("Ex.Board.NotExist"));
        Member member = memberRepository.findById(postDto.getAuthorId())
                .orElseThrow(() -> new NotFoundMemberException("Ex.Member.NotFoundMember"));

        Post post = modelMapper.map(postDto, Post.class);
        post.setBoard(board);
        post.setMember(member);

        return postRepository.persistPost(post);
    }

    public PostViewDto view(Long postId) {
        postRepository.incrementViews(postId);
        Post post = postRepository.viewPost(postId).orElseThrow(() -> new NotFoundPostException("Ex.Post.NotFound"));
        return new PostViewDto(post);
    }

    public void deletePost(Long postId) {
        postRepository.deleteById(postId);
    }

    @Transactional(readOnly = true)
    public String findAuthor(Long postId) {
        String author = postRepository.findAuthor(postId);
        if (author == null){   throw new NotFoundMemberException("Ex.Member.NotFound");}
        return author;
    }


    @Transactional(readOnly = true)
    public PostModifyFormDto findModifyTarget(Long postId) {
        return postRepository.findModifyPost(postId).orElseThrow(() -> new NotFoundPostException("Ex.Post.NotFound"));
    }

    public void modifyPost(PostModifyFormDto modifyPost, Long postId) {
        postRepository.updatePost(postId, modifyPost);
    }

}
