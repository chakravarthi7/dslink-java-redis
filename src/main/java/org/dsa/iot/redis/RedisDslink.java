package org.dsa.iot.redis;

import org.dsa.iot.dslink.DSLink;
import org.dsa.iot.dslink.DSLinkFactory;
import org.dsa.iot.dslink.DSLinkHandler;
import org.dsa.iot.redis.provider.RedisProvider;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Dslink to work with JDBC
 *
 * @author pshvets
 */
public class RedisDslink extends DSLinkHandler {

    private static final Logger LOG = LoggerFactory.getLogger(RedisDslink.class);

    @Override
    public boolean isResponder() {
    	
        return true;
    }

    @Override
    public void preInit() {
    System.out.println("In REDIS DSLINK CLASS - PREINIT METHOD");
      /*  try {
            URLClassLoader loader;
            {
                ClassLoader l = RedisDslink.class.getClassLoader();
       
                loader = (URLClassLoader) l;
            
            }
            URL[] urls = loader.getURLs();
            for (URL url : urls) {
            
                processUrl(loader, url);
            }
        } catch (Exception e) {
            String msg = "Error scanning for drivers. ";
            msg += "Relying on Java's mechanism for driver ";
            msg += "initialization.";
            LOG.error(msg, e);
        }*/
    }

   /* private void processUrl(ClassLoader loader, URL url) throws Exception {
    	
        final String driverName = Jedis.class.getName();
     
        try (ZipInputStream zis = new ZipInputStream(url.openStream())) {
      
            ZipEntry entry;
            while ((entry = zis.getNextEntry()) != null) {
                {
  
                    String name = entry.getName();
                    if (entry.isDirectory()|| !name.endsWith(driverName)) { 
                    
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
                  System.out.println("Loader " + loader);
                    System.out.println("clazzName " + clazzName);
                    registerDriver(loader, clazzName);
                }
            }
        }
    }

    private void registerDriver(ClassLoader loader, String clazzName) {
    
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
    }*/

    @Override
    public void onResponderConnected(DSLink link) {
    	System.out.println("In Main CLass - onResponderConnected Method");
        LOG.info("REDIS DSLink started");
        RedisProvider provider = new RedisProvider();
        provider.run(link);
    }

    public static void main(String[] args) {
    	
        DSLinkFactory.start(args, new RedisDslink());
    }
}
