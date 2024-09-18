package de.hskl.itanalyst.alwi;

import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.boot.web.servlet.support.SpringBootServletInitializer;

@Slf4j
@SpringBootApplication
public class PathfindingApplication extends SpringBootServletInitializer {

    public static void main(String[] args) {
        SpringApplication.run(PathfindingApplication.class, args);
    }

    @Override
    protected SpringApplicationBuilder configure(SpringApplicationBuilder application) {
        return application.sources(PathfindingApplication.class);
    }

//    @Bean
//    public CommandLineRunner loadInitialData(GraphBuilderService graphBuilderService) {
//        return (args) -> {
//            graphBuilderService.buildGraph();
//        };
//    }
}
