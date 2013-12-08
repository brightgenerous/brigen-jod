package com.brightgenerous.jod;

import java.io.IOException;
import java.io.OutputStream;

public interface IConverter {

    void convert(OutputStream outputStream) throws IOException;
}
