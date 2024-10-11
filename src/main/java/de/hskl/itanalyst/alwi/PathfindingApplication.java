package de.hskl.itanalyst.alwi;

import de.hskl.itanalyst.alwi.services.FileHandlerService;
import de.hskl.itanalyst.alwi.services.StreetService;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.boot.web.servlet.support.SpringBootServletInitializer;
import org.springframework.cache.CacheManager;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.cache.concurrent.ConcurrentMapCacheManager;
import org.springframework.context.annotation.Bean;

@Slf4j
@EnableCaching
@SpringBootApplication
public class PathfindingApplication extends SpringBootServletInitializer {

    @Value("${app.runner.enabled}")
    private String appRunnerEnabled;

    public static void main(String[] args) {
        SpringApplication.run(PathfindingApplication.class, args);
    }

    @Override
    protected SpringApplicationBuilder configure(SpringApplicationBuilder application) {
        return application.sources(PathfindingApplication.class);
    }

    @Bean
    public ModelMapper modelMapper() {
        return new ModelMapper();
    }

    // activate cache for main entities
    @Bean
    public CacheManager cacheManager() {
        return new ConcurrentMapCacheManager("streets", "nodes", "ways");
    }

    // run just a single time if there is no data in database
    @Bean
    //@ConditionalOnProperty(name = "app.runner.enabled", havingValue = "true")
    public CommandLineRunner loadInitialData(FileHandlerService fileHandlerService, StreetService streetService) {
        return (args) -> {
            if(!appRunnerEnabled.equals("true")) {
                log.info("Command Line Runner is not enabled. Start caching from database.");
                //streetService.findAllStreets();
                log.info("Command Line Runner finished caching from database.");
            } else {
                log.info("Command Line Runner is enabled. Start persisting data to database.");
                String filename = "C:\\workspace\\osm-pathfinding\\src\\main\\resources\\osm\\export_ueberherrn+wohnstadt_nw.osm";
                fileHandlerService.saveFileDataToDatabase(filename);
            }
        };
    }
}
