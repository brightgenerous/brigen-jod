package com.brightgenerous.jod.delegate;

import java.util.logging.Level;
import java.util.logging.Logger;

import com.brightgenerous.jod.IConverter;
import com.brightgenerous.jod.IConverterStrategy;

@Deprecated
public class JodUtility {

    private static final Logger log = Logger.getAnonymousLogger();

    public static final boolean USEFUL;

    private static final JodDelegater delegater;

    private static final RuntimeException rex;

    static {
        JodDelegater tmp = null;
        boolean useful = false;
        RuntimeException ex = null;
        try {
            tmp = new JodDelegaterImpl();
            useful = true;
        } catch (NoClassDefFoundError | RuntimeException e) {

            if (log.isLoggable(Level.INFO)) {
                log.log(Level.INFO, "does not resolve jod");
            }

            if (e instanceof RuntimeException) {
                Throwable th = e.getCause();
                if ((th == null) || !(th instanceof ClassNotFoundException)) {
                    throw e;
                }
                ex = (RuntimeException) e;
            } else {
                ex = new RuntimeException(e);
            }
        }
        USEFUL = useful;
        delegater = tmp;
        rex = ex;
    }

    private JodUtility() {
    }

    public static final String DEFAULT_HOST = (delegater == null) ? null : delegater
            .getDefaultHost();

    public static final Integer DEFAULT_PORT = (delegater == null) ? null : delegater
            .getDefaultPort();

    public static final String DEFAULT_PIPE = (delegater == null) ? null : delegater
            .getDefaultPipe();

    private static JodDelegater getDelegater() {
        if (delegater == null) {
            throw rex;
        }
        return delegater;
    }

    public static IConverter getConverter(IConverterStrategy strategy) {
        return getDelegater().getConverter(strategy);
    }

    public static boolean checkConnect(IConverterStrategy strategy) {
        return getDelegater().checkConnect(strategy);
    }
}
