package com.brightgenerous.jod;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.Serializable;
import java.net.URL;

@SuppressWarnings("deprecation")
public class InputResource implements IInputResource, Serializable {

    private static final long serialVersionUID = 319356142238190647L;

    private final File file;

    private final String fileName;

    private final URL url;

    private final byte[] bytes;

    public InputResource(File file) {
        if (file == null) {
            throw new IllegalArgumentException("The file must not be null.");
        }

        this.file = file;
        fileName = null;
        url = null;
        bytes = null;
    }

    public InputResource(String fileName) {
        if (fileName == null) {
            throw new IllegalArgumentException("The fileName must not be null.");
        }

        file = null;
        this.fileName = fileName;
        url = null;
        bytes = null;
    }

    public InputResource(URL url) {
        if (url == null) {
            throw new IllegalArgumentException("The url must not be null.");
        }

        file = null;
        fileName = null;
        this.url = url;
        bytes = null;
    }

    public InputResource(byte[] bytes) {
        if (bytes == null) {
            throw new IllegalArgumentException("The bytes must not be null.");
        }

        file = null;
        fileName = null;
        url = null;
        this.bytes = bytes;
    }

    @SuppressWarnings("resource")
    @Override
    public InputStream openStream() throws IOException {
        InputStream ret;
        if (file != null) {
            ret = new FileInputStream(file);
        } else if (fileName != null) {
            ret = new FileInputStream(fileName);
        } else if (url != null) {
            ret = url.openStream();
        } else if (bytes != null) {
            ret = new ByteArrayInputStream(bytes);
        } else {
            throw new IllegalStateException();
        }
        return ret;
    }
}
