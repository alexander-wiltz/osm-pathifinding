package de.hskl.itanalyst.alwi.exceptions;

import java.io.Serial;

public class StreetNotFoundException extends Exception {
    @Serial
    private static final long serialVersionUID = -8306604688247986178L;

    public StreetNotFoundException(String errorMsg) {
        super(errorMsg);
    }

    public String getErrorMessage() {
        return super.getMessage();
    }
}
