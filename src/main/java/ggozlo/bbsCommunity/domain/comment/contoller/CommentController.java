package ggozlo.bbsCommunity.domain.comment.contoller;

import com.fasterxml.jackson.databind.ObjectMapper;
import ggozlo.bbsCommunity.domain.comment.service.CommentService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/comment")
public class CommentController {

    private final CommentService commentService;
    private final ObjectMapper objectMapper;
    private final ModelMapper modelMapper;

}
