package com.eaglesakura.json;

import java.io.IOException;

public class JsonIOException extends IOException {
    public JsonIOException(String message) {
        super(message);
    }

    public JsonIOException(String message, Throwable cause) {
        super(message, cause);
    }

    public JsonIOException(Throwable cause) {
        super(cause);
    }
}
