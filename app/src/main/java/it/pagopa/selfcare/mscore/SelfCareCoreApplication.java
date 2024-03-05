package it.pagopa.selfcare.mscore;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableAsync;

@SpringBootApplication
@EnableAsync
public class SelfCareCoreApplication {

    public static void main(String[] args) {
        SpringApplication.run(SelfCareCoreApplication.class, args);
    }

}
