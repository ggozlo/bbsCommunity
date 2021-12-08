package ggozlo.bbsCommunity.domain.post;

import ggozlo.bbsCommunity.domain.member.repository.MemberRepository;
import ggozlo.bbsCommunity.domain.member.service.MemberService;
import ggozlo.bbsCommunity.domain.post.service.PostService;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
public class postTest {

    @Autowired
    PostService postService;
    @Autowired
    MemberRepository memberRepository;

    @Value("${admin.username}")
    String adminUsername;

    @Test
    public void authorCheckMethod() {

        String authorUsername = postService.findAuthor(1L);
        Assertions.assertEquals(authorUsername, authorUsername);
    }
}
