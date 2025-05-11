package aiss.GithubMiner.service;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
@Service
public class ProjectService {

    @Autowired
    RestTemplate restTemplate;
}
