package com.brightgenerous.jod;

import java.io.IOException;
import java.io.InputStream;

@Deprecated
public interface IInputResource {

    InputStream openStream() throws IOException;
}
