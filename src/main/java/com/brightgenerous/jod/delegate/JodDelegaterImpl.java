package com.brightgenerous.jod.delegate;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.Serializable;
import java.lang.ref.SoftReference;
import java.net.ConnectException;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import com.artofsolving.jodconverter.DefaultDocumentFormatRegistry;
import com.artofsolving.jodconverter.DocumentConverter;
import com.artofsolving.jodconverter.DocumentFormat;
import com.artofsolving.jodconverter.DocumentFormatRegistry;
import com.artofsolving.jodconverter.openoffice.connection.OpenOfficeConnection;
import com.artofsolving.jodconverter.openoffice.connection.PipeOpenOfficeConnection;
import com.artofsolving.jodconverter.openoffice.connection.SocketOpenOfficeConnection;
import com.artofsolving.jodconverter.openoffice.converter.OpenOfficeDocumentConverter;
import com.brightgenerous.jod.Format;
import com.brightgenerous.jod.IConverter;
import com.brightgenerous.jod.IConverterStrategy;

@SuppressWarnings("deprecation")
class JodDelegaterImpl implements JodDelegater {

    {
        check();
    }

    private static void check() {
        try {
            Class.forName(DefaultDocumentFormatRegistry.class.getName());
            Class.forName(DocumentConverter.class.getName());
            Class.forName(DocumentFormat.class.getName());
            Class.forName(DocumentFormatRegistry.class.getName());
            Class.forName(OpenOfficeConnection.class.getName());
            Class.forName(PipeOpenOfficeConnection.class.getName());
            Class.forName(SocketOpenOfficeConnection.class.getName());
            Class.forName(OpenOfficeDocumentConverter.class.getName());
        } catch (ClassNotFoundException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public String getDefaultHost() {
        return SocketOpenOfficeConnection.DEFAULT_HOST;
    }

    @Override
    public Integer getDefaultPort() {
        return Integer.valueOf(SocketOpenOfficeConnection.DEFAULT_PORT);
    }

    @Override
    public String getDefaultPipe() {
        return PipeOpenOfficeConnection.DEFAULT_PIPE_NAME;
    }

    @Override
    public IConverter getConverter(IConverterStrategy strategy) {
        return prv_getConverter(strategy);
    }

    @Override
    public boolean checkConnect(IConverterStrategy strategy) {
        return prv_getConverter(strategy).checkConnect();
    }

    private Converter prv_getConverter(IConverterStrategy strategy) {
        return new Converter(strategy);
    }
}

@SuppressWarnings("deprecation")
class Converter implements IConverter {

    private static final ConverterContext context = new ConverterContext();

    private final IConverterStrategy strategy;

    public Converter(IConverterStrategy strategy) {
        if (strategy == null) {
            throw new IllegalArgumentException("The strategy must not be null.");
        }

        this.strategy = strategy;
    }

    @Override
    public void convert(OutputStream outputStream) throws IOException {
        OpenOfficeConnection connection = null;
        try {
            connection = context.getConnection(strategy);
            try (InputStream inputStream = strategy.getInputResource().openStream()) {
                DocumentConverter converter = context.getDocumentConverter(connection);
                converter.convert(inputStream,
                        context.getDocumentFormat(strategy.getInputFormat()), outputStream,
                        context.getDocumentFormat(strategy.getOutputFormat()));
                outputStream.flush();
            }
        } finally {
            if (outputStream != null) {
                try {
                    outputStream.close();
                } catch (IOException e) {
                }
            }
            if (connection != null) {
                connection.disconnect();
            }
        }
    }

    public boolean checkConnect() {
        boolean ret = false;
        OpenOfficeConnection connection = null;
        try {
            connection = context.getConnection(strategy);
            connection.connect();
            ret = true;
        } catch (ConnectException e) {
        } finally {
            if (connection != null) {
                connection.disconnect();
            }
        }
        return ret;
    }
}

@SuppressWarnings("deprecation")
class ConverterContext implements Serializable {

    private static final long serialVersionUID = -1584728075429709390L;

    private final DocumentFormatRegistry documentFormatRegistry = new DefaultDocumentFormatRegistry();

    private final Map<Format, SoftReference<DocumentFormat>> cache = new ConcurrentHashMap<>();

    public OpenOfficeConnection getConnection(IConverterStrategy strategy) {
        OpenOfficeConnection ret;
        Integer port = strategy.getPort();
        if (port != null) {
            String host = strategy.getHost();
            if (host != null) {
                ret = new SocketOpenOfficeConnection(host, port.intValue());
            } else {
                ret = new SocketOpenOfficeConnection(port.intValue());
            }
        } else {
            ret = new PipeOpenOfficeConnection(strategy.getPipe());
        }
        return ret;
    }

    public DocumentConverter getDocumentConverter(OpenOfficeConnection connection) {
        return new OpenOfficeDocumentConverter(connection);
    }

    public DocumentFormat getDocumentFormat(Format format) {
        DocumentFormat ret = null;
        {
            SoftReference<DocumentFormat> sr = cache.get(format);
            if (sr != null) {
                ret = sr.get();
            }
            if (ret == null) {
                synchronized (cache) {
                    sr = cache.get(format);
                    if (sr != null) {
                        ret = sr.get();
                    }
                    if (ret == null) {
                        ret = documentFormatRegistry
                                .getFormatByFileExtension(format.getExtension());
                        cache.put(format, new SoftReference<>(ret));
                    }
                }
            }
        }
        return ret;
    }
}
