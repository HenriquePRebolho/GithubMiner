package aiss.GithubMiner.service;

import aiss.GithubMiner.model.Author;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.List;

@Service
public class GithubMinerService {

    @Autowired
    RestTemplate restTemplate;

    public List<Author> findAllUsers(String owner, String repo) {
        List<Author> authors = null;


    }


}
