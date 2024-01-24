package kz.java.micro.planner.users;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.cloud.netflix.eureka.EnableEurekaClient;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@SpringBootApplication
@EnableEurekaClient
@ComponentScan(basePackages = {"kz.java.micro.planner"})
@EnableJpaRepositories(basePackages = {"kz.java.micro.planner.users"})
@EntityScan(basePackages = {"kz.java.micro.planner.entity"})
public class PlannerUsersApplication {
    public static void main(String[] args) {
        SpringApplication.run(PlannerUsersApplication.class, args);
    }
}