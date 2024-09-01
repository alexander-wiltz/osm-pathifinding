package de.hskl.itanalyst.alwi.exceptions;

import java.io.Serial;

public class NodeNotFoundException extends Exception {
    @Serial
    private static final long serialVersionUID = 98670511855313142L;

    public NodeNotFoundException(String errorMsg) {
        super(errorMsg);
    }

    public String getErrorMessage() {
        return super.getMessage();
    }
}
