package org.dsa.iot.jdbc;

import org.dsa.iot.dslink.DSLink;
import org.dsa.iot.dslink.DSLinkFactory;
import org.dsa.iot.dslink.DSLinkHandler;
import org.dsa.iot.jdbc.provider.JdbcProvider;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.ByteArrayOutputStream;
import java.net.URL;
import java.net.URLClassLoader;
import java.sql.Driver;
import java.sql.DriverManager;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;

/**
 * Dslink to work with JDBC
 *
 * @author pshvets
 */
public class JdbcDslink extends DSLinkHandler {

    private static final Logger LOG = LoggerFactory.getLogger(JdbcDslink.class);

    @Override
    public boolean isResponder() {
    	System.out.println("In Main CLass - isResponder Method");
        return true;
    }

    @Override
    public void preInit() {
    	System.out.println("In Main CLass - preInit Method");
        try {
            URLClassLoader loader;
            {
                ClassLoader l = JdbcDslink.class.getClassLoader();
            	System.out.println("L " + l);
                loader = (URLClassLoader) l;
              
            }
            URL[] urls = loader.getURLs();
            for (URL url : urls) {
            	  System.out.println("URL " + url + " Loader " + loader);
                processUrl(loader, url);
            }
        } catch (Exception e) {
            String msg = "Error scanning for drivers. ";
            msg += "Relying on Java's mechanism for driver ";
            msg += "initialization.";
            LOG.error(msg, e);
        }
    }

    private void processUrl(ClassLoader loader, URL url) throws Exception {
    	System.out.println("In Main CLass - processUrl Method " + url);
        final String driverName = Driver.class.getName();
     	System.out.println("driverName " + driverName);
        try (ZipInputStream zis = new ZipInputStream(url.openStream())) {
            ZipEntry entry;
            while ((entry = zis.getNextEntry()) != null) {
                {
                    String name = entry.getName();
                    if (entry.isDirectory()
                            || !name.endsWith(driverName)) {
                        continue;
                    }
                }

                ByteArrayOutputStream baos = new ByteArrayOutputStream();
                byte[] buffer = new byte[1024];
                int read;
                while ((read = zis.read(buffer)) >= 0) {
                    baos.write(buffer, 0, read);
                }
                if (baos.size() <= 0) {
                    continue;
                }
                String[] data = baos.toString("UTF-8").split("\n");
                for (String clazzName : data) {
                    registerDriver(loader, clazzName);
                }
            }
        }
    }

    private void registerDriver(ClassLoader loader, String clazzName) {
    	System.out.println("In Main CLass - registerDriver Method");
        try {
            clazzName = clazzName.trim();
            Class<?> clazz = loader.loadClass(clazzName);
            if (Driver.class.isAssignableFrom(clazz)) {
                Object instance = clazz.newInstance();
                Driver d = (Driver) instance;
                DriverManager.registerDriver(d);

                String msg = "Registered driver: {}";
                LOG.info(msg, clazz.getName());
            }
        } catch (NoClassDefFoundError ignored) {
        } catch (Exception e) {
            String msg = "Failed to register driver: {}";
            LOG.warn(msg, clazzName, e);
        }
    }

    @Override
    public void onResponderConnected(DSLink link) {
    	System.out.println("In Main CLass - onResponderConnected Method");
        LOG.info("JDBC DSLink started");
        JdbcProvider provider = new JdbcProvider();
        provider.run(link);
    }

    public static void main(String[] args) {
    	System.out.println("In Main CLass - Main Method");
        DSLinkFactory.start(args, new JdbcDslink());
    }
}
