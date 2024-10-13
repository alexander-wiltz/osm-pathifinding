package de.hskl.itanalyst.alwi.utilities;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class StringUtils {

    public String capitalizeStringFromUI(String input) {
        if (input == null || input.isEmpty()) {
            log.info("Given String is empty.");
            return input;
        }
        return input.substring(0, 1).toUpperCase() + input.substring(1).toLowerCase();
    }

}
