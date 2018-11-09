package com.gksoftware.asyncmethodspring.services;

import com.gksoftware.asyncmethodspring.model.User;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.concurrent.CompletableFuture;

/**
 * This class uses Spring's RestTemplate to invoke
 * a remote REST point (url), and then convert the answer into a User object
 * or Object. Spring boot automatically provides a RestTemplateBuilder
 * that customizes the defaults with any auto-configuration bits(MessageConverter).
 *
 * The class is marked with the @Service annotation,
 * making it a candidate for Spring's component scanning to detect it and add it
 * to the application context.
 */
@Service
public class GitHubLookupService {

    private static final Logger logger = LoggerFactory.getLogger(GitHubLookupService.class);

    private final RestTemplate restTemplate;

    public GitHubLookupService(RestTemplateBuilder restTemplateBuilder) {
        this.restTemplate = restTemplateBuilder.build();
    }

    /**
     * The findUser method is flagged with Spring's @Async annotation,
     * indicating it will run on a separate thread. The method's return type is
     * <code>CompletableFuture<User></code> instead of User, a requirement for any
     * asynchronuous service. This code uses the completedFuture method
     * to return a CompletableFuture instance which is already completed with
     * result of the GitHub query
     * @param user name of profile GitHub
     * @return CompletableFuture
     * @throws InterruptedException
     */
    @Async
    public CompletableFuture<User> findUser(String user) throws InterruptedException {
        logger.info("Looking up "+user);
        String url = String.format("https://api.github.com/users/%s", user);
        User result = restTemplate.getForObject(url, User.class);
        //Artificial delay of 1sec for demostration purposes
        Thread.sleep(1000L);
        return CompletableFuture.completedFuture(result);
    }

}
