package com.brightgenerous.jod;

import java.io.File;
import java.io.Serializable;
import java.lang.ref.SoftReference;
import java.net.URL;
import java.util.HashSet;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

import com.brightgenerous.jod.converter.JodUtility;

@SuppressWarnings("deprecation")
public class JodUtils implements Serializable {

    private static final long serialVersionUID = -9014302792126476795L;

    public static boolean useful() {
        return JodUtility.USEFUL;
    }

    static class InstanceKey implements Serializable {

        private static final long serialVersionUID = -5571606798438371038L;

        private final String host;

        private final Integer port;

        private final String pipe;

        public InstanceKey(String host, Integer port, String pipe) {
            this.host = host;
            this.port = port;
            this.pipe = pipe;
        }

        @Override
        public int hashCode() {
            final int multiplier = 37;
            int result = 17;
            result = (multiplier * result) + hashCodeEscapeNull(host);
            result = (multiplier * result) + hashCodeEscapeNull(port);
            result = (multiplier * result) + hashCodeEscapeNull(pipe);
            return result;
        }

        @Override
        public boolean equals(Object obj) {
            if (obj == null) {
                return false;
            }
            if (!(obj instanceof InstanceKey)) {
                return false;
            }

            InstanceKey other = (InstanceKey) obj;

            if (!equalsEscapeNull(host, other.host)) {
                return false;
            }
            if (!equalsEscapeNull(port, other.port)) {
                return false;
            }
            if (!equalsEscapeNull(pipe, other.pipe)) {
                return false;
            }
            return true;
        }

        private static int hashCodeEscapeNull(Object obj) {
            return hashCodeEscapeNull(obj, 0);
        }

        private static int hashCodeEscapeNull(Object obj, int nullValue) {
            if (obj == null) {
                return nullValue;
            }
            return obj.hashCode();
        }

        private static boolean equalsEscapeNull(Object obj0, Object obj1) {
            if (obj0 == obj1) {
                return true;
            }
            if ((obj0 == null) || (obj1 == null)) {
                return false;
            }
            return obj0.equals(obj1);
        }
    }

    private static final String DEFAULT_HOST = JodUtility.DEFAULT_HOST;

    private static final Integer DEFAULT_PORT = JodUtility.DEFAULT_PORT;

    private static final String DEFAULT_PIPE = JodUtility.DEFAULT_PIPE;

    private final String host;

    private final Integer port;

    private final String pipe;

    protected JodUtils(String host, Integer port, String pipe) {
        if (host == null) {
            if (pipe == null) {
                throw new IllegalArgumentException("The host and pipe must not be null.");
            }
        } else {
            if (port == null) {
                throw new IllegalArgumentException("The port must not be null.");
            }
            int p = port.intValue();
            if ((p < 0) || (65535 < p)) {
                throw new IllegalArgumentException("The port must not be between 0 and 65535.");
            }
        }

        this.host = host;
        this.port = port;
        this.pipe = pipe;
    }

    public static JodUtils get(String host, Integer port) {
        return getInstance((host == null) ? DEFAULT_HOST : host, (port == null) ? DEFAULT_PORT
                : port, null);
    }

    public static JodUtils get(String pipe) {
        return getInstance(null, null, (pipe == null) ? DEFAULT_PIPE : pipe);
    }

    private static volatile Map<InstanceKey, SoftReference<JodUtils>> cache;

    protected static JodUtils getInstance(String host, Integer port, String pipe) {
        if (cache == null) {
            synchronized (JodUtils.class) {
                if (cache == null) {
                    cache = new ConcurrentHashMap<>();
                }
            }
        }
        InstanceKey ik = new InstanceKey(host, port, pipe);
        SoftReference<JodUtils> sr = cache.get(ik);
        JodUtils ret;
        if (sr != null) {
            ret = sr.get();
            if (ret != null) {
                return ret;
            }
            Set<InstanceKey> dels = new HashSet<>();
            for (Entry<InstanceKey, SoftReference<JodUtils>> entry : cache.entrySet()) {
                if (entry.getValue().get() == null) {
                    dels.add(entry.getKey());
                }
            }
            for (InstanceKey del : dels) {
                cache.remove(del);
            }
        }
        ret = new JodUtils(host, port, pipe);
        cache.put(ik, new SoftReference<>(ret));
        return ret;
    }

    public IConverter getConverter(File file, Format outputFormat) {
        return getConverter(file, Format.getByExtension(file.getName()), outputFormat);
    }

    public IConverter getConverter(File file, Format inputFormat, Format outputFormat) {
        if (file == null) {
            throw new IllegalArgumentException("The file must not be null.");
        }
        if (inputFormat == null) {
            throw new IllegalArgumentException("The inputFormat must not be null.");
        }
        if (outputFormat == null) {
            throw new IllegalArgumentException("The outputFormat must not be null.");
        }

        IInputResource resource = new InputResource(file);
        IConverterStrategy strategy;
        if (host != null) {
            strategy = new ConverterStrategy(host, port, resource, inputFormat, outputFormat);
        } else {
            strategy = new ConverterStrategy(pipe, resource, inputFormat, outputFormat);
        }
        return JodUtility.getConverter(strategy);
    }

    public IConverter getConverter(String fileName, Format outputFormat) {
        return getConverter(fileName, Format.getByExtension(fileName), outputFormat);
    }

    public IConverter getConverter(String fileName, Format inputFormat, Format outputFormat) {
        if (fileName == null) {
            throw new IllegalArgumentException("The fileName must not be null.");
        }
        if (inputFormat == null) {
            throw new IllegalArgumentException("The inputFormat must not be null.");
        }
        if (outputFormat == null) {
            throw new IllegalArgumentException("The outputFormat must not be null.");
        }

        IInputResource resource = new InputResource(fileName);
        IConverterStrategy strategy;
        if (host != null) {
            strategy = new ConverterStrategy(host, port, resource, inputFormat, outputFormat);
        } else {
            strategy = new ConverterStrategy(pipe, resource, inputFormat, outputFormat);
        }
        return JodUtility.getConverter(strategy);
    }

    public IConverter getConverter(URL url, Format outputFormat) {
        return getConverter(url, Format.getByExtension(url.getPath()), outputFormat);
    }

    public IConverter getConverter(URL url, Format inputFormat, Format outputFormat) {
        if (url == null) {
            throw new IllegalArgumentException("The url must not be null.");
        }
        if (inputFormat == null) {
            throw new IllegalArgumentException("The inputFormat must not be null.");
        }
        if (outputFormat == null) {
            throw new IllegalArgumentException("The outputFormat must not be null.");
        }

        IInputResource resource = new InputResource(url);
        IConverterStrategy strategy;
        if (host != null) {
            strategy = new ConverterStrategy(host, port, resource, inputFormat, outputFormat);
        } else {
            strategy = new ConverterStrategy(pipe, resource, inputFormat, outputFormat);
        }
        return JodUtility.getConverter(strategy);
    }

    public IConverter getConverter(byte[] bytes, Format inputFormat, Format outputFormat) {
        if (bytes == null) {
            throw new IllegalArgumentException("The bytes must not be null.");
        }
        if (inputFormat == null) {
            throw new IllegalArgumentException("The inputFormat must not be null.");
        }
        if (outputFormat == null) {
            throw new IllegalArgumentException("The outputFormat must not be null.");
        }

        IInputResource resource = new InputResource(bytes);
        IConverterStrategy strategy;
        if (host != null) {
            strategy = new ConverterStrategy(host, port, resource, inputFormat, outputFormat);
        } else {
            strategy = new ConverterStrategy(pipe, resource, inputFormat, outputFormat);
        }
        return JodUtility.getConverter(strategy);
    }
}
