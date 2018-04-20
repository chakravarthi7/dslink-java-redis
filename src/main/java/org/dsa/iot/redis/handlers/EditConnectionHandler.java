package org.dsa.iot.redis.handlers;

import org.dsa.iot.dslink.node.Node;
import org.dsa.iot.dslink.node.NodeBuilder;
import org.dsa.iot.dslink.node.actions.ActionResult;
import org.dsa.iot.dslink.node.value.Value;
import org.dsa.iot.dslink.util.handler.Handler;
import org.dsa.iot.dslink.util.json.JsonObject;
import org.dsa.iot.redis.driver.RedisConnectionHelper;
import org.dsa.iot.redis.model.RedisConfig;
import org.dsa.iot.redis.model.RedisConstants;
import org.dsa.iot.redis.provider.ActionProvider;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class EditConnectionHandler extends ActionProvider implements
        Handler<ActionResult> {

    private static final Logger LOG = LoggerFactory
            .getLogger(EditConnectionHandler.class);

    private RedisConfig config;

    public EditConnectionHandler(RedisConfig config) {
        this.config = config;
    }

    @Override
    public void handle(ActionResult event) {
        LOG.debug("Entering edit connection handle");

        Node status = config.getNode().getChild(RedisConstants.STATUS, false);

        Value url = event.getParameter(RedisConstants.URL, new Value(""));
        if (url.getString() == null || url.getString().isEmpty()) {
            status.setValue(new Value("url is empty"));
            return;
        }

        Value user = event.getParameter(RedisConstants.USER, new Value(""));
        Value password = event.getParameter(RedisConstants.PASSWORD);

        Value driver = event.getParameter(RedisConstants.DRIVER, new Value(""));
        if (driver.getString() == null || driver.getString().isEmpty()) {
            status.setValue(new Value("driver is empty"));
            return;
        } else {
            if ("org.postgresql.Driver".equals(driver.getString())) {
                NodeBuilder builder = config.getNode().createChild(RedisConstants.COPY, false);
              //  builder.setAction(getCopyAction(config));
                builder.setSerializable(false);
                builder.build();
            } else {
                config.getNode().removeChild(RedisConstants.COPY, false);
            }
        }

        Value timeout = event.getParameter(RedisConstants.DEFAULT_TIMEOUT,
                                           new Value(60));
        Value poolable = event.getParameter(RedisConstants.POOLABLE);

        LOG.debug("Old configuration is {}", config);
        config.setUrl(url.getString());
        config.setUser(user.getString());
        if (password != null) {
            config.setPassword(password.getString().toCharArray());
        }
        config.setDriverName(driver.getString());
        config.setPoolable(poolable.getBool());

        // create DataSource if specified
        config.setTimeout((Integer) timeout.getNumber());
        if (poolable.getBool()) {
            config.setDataSource(RedisConnectionHelper
                                         .configureDataSource(config));
        } else {
            config.setDataSource(null);
        }

        LOG.debug("New configuration is {}", config);

     /*   Node edit = event.getNode();
        edit.setAction(getEditConnectionAction(config));*/

        Node connection = config.getNode();

        JsonObject object = connection
                .getAttribute(RedisConstants.CONFIGURATION).getMap();
        object.put(RedisConstants.NAME, config.getName());
        object.put(RedisConstants.URL, config.getUrl());
        object.put(RedisConstants.USER, config.getUser());
        object.put(RedisConstants.DRIVER, config.getDriverName());
        object.put(RedisConstants.POOLABLE, config.isPoolable());
        object.put(RedisConstants.DEFAULT_TIMEOUT, config.getTimeout());
        connection.setAttribute(RedisConstants.CONFIGURATION, new Value(object));
        connection.setPassword(config.getPassword());
    }
}
