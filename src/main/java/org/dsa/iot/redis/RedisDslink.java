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
    public void onResponderConnected(DSLink link) {
        LOG.info("REDIS DSLink started");
        RedisProvider provider = new RedisProvider();
        provider.run(link);
    }

    public static void main(String[] args) {
    	
        DSLinkFactory.start(args, new RedisDslink());
    }
}
