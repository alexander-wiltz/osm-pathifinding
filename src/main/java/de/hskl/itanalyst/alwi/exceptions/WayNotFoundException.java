package de.hskl.itanalyst.alwi.exceptions;

import java.io.Serial;

public class WayNotFoundException extends Exception {
    @Serial
    private static final long serialVersionUID = -8835232566593245636L;

    public WayNotFoundException(String errorMsg) {
        super(errorMsg);
    }

    public String getErrorMessage() {
        return super.getMessage();
    }
}