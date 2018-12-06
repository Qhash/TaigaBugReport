package de.techfunder.taigabugreport.pojo.events;

public class ErrorUnauthorized {
    private String error;

    public ErrorUnauthorized(String errorMessage) {
        error = errorMessage;
    }

    public String getError() {
        return error;
    }
}
