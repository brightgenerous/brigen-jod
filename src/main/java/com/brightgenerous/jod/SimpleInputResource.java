package com.brightgenerous.jod;

import java.io.IOException;
import java.io.InputStream;

@SuppressWarnings("deprecation")
class SimpleInputResource implements IInputResource {

    private final InputStream inputStream;

    public SimpleInputResource(InputStream inputStream) {
        this.inputStream = inputStream;
    }

    @Override
    public InputStream openStream() throws IOException {
        return inputStream;
    }
}
