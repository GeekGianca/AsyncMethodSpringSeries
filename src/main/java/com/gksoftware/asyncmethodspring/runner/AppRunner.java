package com.gksoftware.asyncmethodspring.runner;

import com.gksoftware.asyncmethodspring.model.User;
import com.gksoftware.asyncmethodspring.services.GitHubLookupService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.util.concurrent.CompletableFuture;

@Component
public class AppRunner implements CommandLineRunner {
    private static final Logger logger = LoggerFactory.getLogger(AppRunner.class);

    private final GitHubLookupService gitHubLookupService;

    public AppRunner(GitHubLookupService gitHubLookupService) {
        this.gitHubLookupService = gitHubLookupService;
    }

    @Override
    public void run(String... args) throws Exception {
        //Start the clock
        long start = System.currentTimeMillis();

        //Kick of multiple, asynchronous lookups
        CompletableFuture<User> page1 = gitHubLookupService.findUser("GeekGianca");
        CompletableFuture<User> page2 = gitHubLookupService.findUser("PivotalSoftware");
        CompletableFuture<User> page3 = gitHubLookupService.findUser("CloudFoundry");

        //Wait until they are all done
        CompletableFuture.allOf(page1, page2, page3);

        //Print results, including elapsed time
        logger.info("Elapsed time: "+(System.currentTimeMillis() - start));
        logger.info("--> "+page1.get());
        logger.info("--> "+page2.get());
        logger.info("--> "+page3.get());
    }
}
