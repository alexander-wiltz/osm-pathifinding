package de.hskl.itanalyst.alwi;

import de.hskl.itanalyst.alwi.services.FileHandlerService;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.boot.web.servlet.support.SpringBootServletInitializer;
import org.springframework.context.annotation.Bean;

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

    @Bean
    public ModelMapper modelMapper() {
        return new ModelMapper();
    }

    // run just a single time if there is no data in database
    @Bean
    @ConditionalOnProperty(name = "app.runner.enabled", havingValue = "true")
    public CommandLineRunner loadInitialData(FileHandlerService fileHandlerService) {
        log.info("Command Line Runner is active");
        return (args) -> {
            String filename = "C:\\workspace\\osm-pathifinding\\src\\main\\resources\\osm\\export_ueberherrn+wohnstadt_nw.osm";
            fileHandlerService.saveFileDataToDatabase(filename);
        };
    }
}
