package com.alibaba.dchain.inner.extension;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URL;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.ServiceConfigurationError;
import java.util.concurrent.ConcurrentHashMap;

import com.alibaba.dchain.inner.exception.ErrorEnum;
import com.alibaba.dchain.inner.exception.OpenApiException;
import com.alibaba.dchain.inner.utils.MapUtil;

/**
 * @author 开帆
 * @date 2022/03/14
 */
public class ExtensionLoader<T> {

    private Map<String, Class<?>> cachedClasses;

    private Map<String, T> cachedInstances = new ConcurrentHashMap<>();

    private final Class<T> type;

    private final ClassLoader classLoader;

    private static final String PREFIX = "META-INF/services/";

    public ExtensionLoader(Class<T> type) {
        this.type = type;
        this.classLoader = ExtensionLoader.class.getClassLoader();
    }

    public ExtensionLoader(Class<T> type, ClassLoader classLoader) {
        this.type = type;
        this.classLoader = classLoader;
    }

    public List<T> getExtensionObject() throws OpenApiException {
        Map<String, Class<?>> extensionClasses = getExtensionClasses();
        if (MapUtil.isEmpty(extensionClasses)) {
            return Collections.emptyList();
        }
        List<T> result = new ArrayList<>();
        for (Entry<String, Class<?>> entry : extensionClasses.entrySet()) {
            if (cachedInstances.get(entry.getKey()) == null) {
                synchronized (this) {
                    if (cachedInstances.get(entry.getKey()) == null) {
                        T instance = newInstance(entry.getValue());
                        cachedInstances.put(entry.getKey(), instance);
                    }
                }
            }
            result.add(cachedInstances.get(entry.getKey()));
        }
        return result;
    }

    private Map<String, Class<?>> getExtensionClasses() throws OpenApiException {
        if (cachedClasses == null) {
            synchronized (this) {
                if (cachedClasses == null) {
                    cachedClasses = loadExtensionClasses();
                }
            }
        }
        if (cachedClasses != null) {
            return new HashMap<>(cachedClasses);
        }
        return Collections.emptyMap();
    }

    @SuppressWarnings("unchecked")
    private <T> T newInstance(Class<?> cls) throws OpenApiException {
        ClassLoader tccl = Thread.currentThread().getContextClassLoader();
        try {
            Thread.currentThread().setContextClassLoader(cls.getClassLoader());
            return (T)cls.newInstance();
        } catch (Exception ex) {
            throw new OpenApiException(ErrorEnum.INSTANTIATE_INSTANCE_ERROR, ex);
        } finally {
            Thread.currentThread().setContextClassLoader(tccl);
        }
    }

    private Map<String, Class<?>> loadExtensionClasses() throws OpenApiException {
        String fullName = PREFIX + type.getName();
        Map<String, Class<?>> extensionClasses = new ConcurrentHashMap<>(8);
        try {
            Enumeration<URL> resources = classLoader.getResources(fullName);
            while (resources.hasMoreElements()) {
                Iterator<String> classNamesIterator = parse(type, resources.nextElement(), extensionClasses);

                if (classNamesIterator.hasNext()) {
                    String className = classNamesIterator.next();
                    Class<?> cls = null;
                    try {
                        cls = Class.forName(className, false, classLoader);
                    } catch (ClassNotFoundException x) {
                        fail(type, "Provider " + className + " not found");
                    }
                    if (!type.isAssignableFrom(cls)) {
                        fail(type, "Provider " + className + " not a subtype");
                    }
                    extensionClasses.put(className, cls);
                }
            }
        } catch (Exception e) {
            throw new OpenApiException(ErrorEnum.LOAD_SPI_ERROR, e);
        }
        return extensionClasses;
    }

    private Iterator<String> parse(Class<?> service, URL u, Map<String, Class<?>> extensionClasses)
        throws ServiceConfigurationError {
        InputStream in = null;
        BufferedReader r = null;
        ArrayList<String> names = new ArrayList<>();
        try {
            in = u.openStream();
            r = new BufferedReader(new InputStreamReader(in, "utf-8"));
            int lc = 1;
            while ((lc = parseLine(service, u, r, lc, names, extensionClasses)) >= 0) {}
        } catch (IOException x) {
            fail(service, "Error reading configuration file", x);
        } finally {
            try {
                if (r != null) { r.close(); }
                if (in != null) { in.close(); }
            } catch (IOException y) {
                fail(service, "Error closing configuration file", y);
            }
        }
        return names.iterator();
    }

    private int parseLine(Class<?> service, URL u, BufferedReader r, int lc, List<String> names,
        Map<String, Class<?>> extensionClasses)
        throws IOException, ServiceConfigurationError {
        String ln = r.readLine();
        if (ln == null) {
            return -1;
        }
        int ci = ln.indexOf('#');
        if (ci >= 0) { ln = ln.substring(0, ci); }
        ln = ln.trim();
        int n = ln.length();
        if (n != 0) {
            if ((ln.indexOf(' ') >= 0) || (ln.indexOf('\t') >= 0)) {
                fail(service, u, lc, "Illegal configuration-file syntax");
            }
            int cp = ln.codePointAt(0);
            if (!Character.isJavaIdentifierStart(cp)) { fail(service, u, lc, "Illegal provider-class name: " + ln); }
            for (int i = Character.charCount(cp); i < n; i += Character.charCount(cp)) {
                cp = ln.codePointAt(i);
                if (!Character.isJavaIdentifierPart(cp) && (cp != '.')) {
                    fail(service, u, lc, "Illegal provider-class name: " + ln);
                }
            }
            if (!extensionClasses.containsKey(ln) && !names.contains(ln)) { names.add(ln); }
        }
        return lc + 1;
    }

    private void fail(Class<?> service, String msg, Throwable cause) throws ServiceConfigurationError {
        throw new ServiceConfigurationError(service.getName() + ": " + msg, cause);
    }

    private void fail(Class<?> service, String msg) throws ServiceConfigurationError {
        throw new ServiceConfigurationError(service.getName() + ": " + msg);
    }

    private void fail(Class<?> service, URL u, int line, String msg) throws ServiceConfigurationError {
        fail(service, u + ":" + line + ": " + msg);
    }
}
