package aiss.GithubMiner.service;

import aiss.GithubMiner.model.Comment.User;
import aiss.GithubMiner.model.gitminer.MinerUser;
import aiss.GithubMiner.transformer.UserTransformer;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import static org.junit.jupiter.api.Assertions.*;
@SpringBootTest
class UserServiceTest {
    @Autowired
    UserService userService;

    private final String USERNAME = "enr"; // OJOO Aqui hay que crear un usuario en bitbucket altassian
    // y crear un token que se asocia a tu usuario
    private final String APP_PASSWORD = "ATBBsb7A"; // aqui ponemos el token , en postman (BASIC AUTH)

    @Test
    public void testToGitMinerUser() {
        // Simulación de un usuario básico
        User githubUser = new User();
        githubUser.setId(123);
        githubUser.setLogin("testuser");
        githubUser.setHtmlUrl("https://github.com/testuser");
        githubUser.setType("User");

        MinerUser result = UserTransformer.toGitMinerUser(githubUser);

        assertNotNull(result);
        assertEquals("123", result.getId());
        assertEquals("testuser", result.getUsername());
        assertEquals("https://github.com/testuser", result.getWebUrl());
    }

    @Test
    @DisplayName("Obtener usuario autenticado desde Github")
    public void getAuthenticatedUserTest() {
        MinerUser user = userService.getAuthenticatedUser(USERNAME, APP_PASSWORD);

        assertNotNull(user);
        assertNotNull(user.getId());
        assertNotNull(user.getUsername());

        userService.printUser(user);
    }

    @Test
    @DisplayName("Enviar usuario a GitMiner")
    public void sendUserToGitMinerTest() {
        MinerUser user = userService.getAuthenticatedUser(USERNAME, APP_PASSWORD);
        assertNotNull(user);
// para ver el usuario que estamos enviando
        System.out.println("Usuario a enviar:");
        System.out.println(user);

        boolean enviado = userService.sendUserToGitMiner(user);
        assertTrue(enviado);
    }

}
