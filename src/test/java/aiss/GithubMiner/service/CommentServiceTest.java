package aiss.GithubMiner.service;

import aiss.GithubMiner.model.Comment.Comment;
import aiss.GithubMiner.model.gitminer.MinerComment;
import aiss.GithubMiner.transformer.CommentTransformer;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class CommentServiceTest {

    @Autowired
    CommentService commentService;

    @Test
    public void testToGitMinerComment() {
        // Simulación de un comentario mínimo
        Comment comment = new Comment();
        comment.setId(1);
        comment.setBody("Comentario de prueba");
        comment.setCreatedAt("2024-01-01T12:00:00Z");
        comment.setUpdatedAt("2024-01-01T13:00:00Z");

        MinerComment transformed = CommentTransformer.toGitMinerComment(comment);

        assertNotNull(transformed);
        assertEquals("1", transformed.getId());
        assertEquals("Comentario de prueba", transformed.getBody());
        assertEquals("2024-01-01T12:00:00Z", transformed.getCreatedAt());
        assertEquals("2024-01-01T13:00:00Z", transformed.getUpdatedAt());
    }

    @Test
    @DisplayName("Get comments from GitHub, transform them to GitMiner format and print them on the console")
    void getComments() {
        String owner = "spring-projects";
        String repo = "spring-framework";
        int issueId = 1;
        int maxPages = 1;

        List<MinerComment> comments = commentService.getComments(owner, repo, issueId, maxPages);

        assertNotNull(comments);
        assertFalse(comments.isEmpty());

        // Imprimir los comentarios transformados
        comments.forEach(commentService::printComment);
    }

    @Test
    @DisplayName("Enviar comentarios desde Github a GitMiner")
    void sendCommentsToGitMiner() {
        String owner = "spring-projects";
        String repo = "spring-framework";
        int issueId = 1;
        int maxPages = 1;

        commentService.sendCommentsToGitMiner(owner, repo, issueId, maxPages);
    }
}
