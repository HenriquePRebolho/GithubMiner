package aiss.GithubMiner.service;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
@Service
public class CommitService {

    @Autowired
    RestTemplate restTemplate;
}
