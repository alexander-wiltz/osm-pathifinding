package de.hskl.itanalyst.alwi.exceptions;

import java.io.Serial;

public class WayNotComputableException extends Exception {
    @Serial
    private static final long serialVersionUID = 6706346106253832825L;

    public WayNotComputableException(String errorMsg) {
        super(errorMsg);
    }

    public String getErrorMessage() {
        return super.getMessage();
    }
}
