package de.hskl.itanalyst.alwi;

import de.hskl.itanalyst.alwi.services.FileHandlerService;
import de.hskl.itanalyst.alwi.services.NodeService;
import de.hskl.itanalyst.alwi.services.StreetService;
import de.hskl.itanalyst.alwi.services.WayService;
import de.hskl.itanalyst.alwi.utilities.TimeTracker;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
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

    @Value("${app.cache.nodes}")
    private String cacheNodes;

    @Value("${app.cache.ways}")
    private String cacheWays;

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

    @Bean
    public CacheManager cacheManager() {
        return new ConcurrentMapCacheManager("streets", "nodes", "ways", "graph");
    }

    @Bean
    public CommandLineRunner loadInitialData(FileHandlerService fileHandlerService, StreetService streetService, TimeTracker timeTracker, NodeService nodeService, WayService wayService) {
        return (args) -> {
            if (appRunnerEnabled.equalsIgnoreCase("true")) {
                log.info("Command Line Runner is enabled. Start persisting data to database.");

                String filename = "C:\\workspace\\osm-pathfinding\\src\\main\\resources\\osm\\export_ueberherrn+wohnstadt_nw.osm";
                fileHandlerService.saveFileDataToDatabase(filename);
            }

            timeTracker.startTime();
            log.info("Data should be in the database. Start caching from database.");

            if (log.isDebugEnabled()) {
                log.debug("Start caching Streets.");
            }
            streetService.findAllStreets();

            if (cacheNodes.equalsIgnoreCase("true")) {
                if (log.isDebugEnabled()) {
                    log.debug("Cache Nodes is enabled. Start caching.");
                }
                nodeService.findAllNodes();
            }

            if (cacheWays.equalsIgnoreCase("true")) {
                if (log.isDebugEnabled()) {
                    log.debug("Cache Ways is enabled. Start caching.");
                }
                wayService.findAllWays();
            }

            timeTracker.endTime(PathfindingApplication.class.getName());
            log.info("Command Line Runner finished caching from database. Waiting for requests...");
        };
    }
}
