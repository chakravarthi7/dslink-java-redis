package org.dsa.iot.redis.handlers;

import org.dsa.iot.dslink.node.Node;
import org.dsa.iot.dslink.node.NodeBuilder;
import org.dsa.iot.dslink.node.NodeManager;
import org.dsa.iot.dslink.node.actions.ActionResult;
import org.dsa.iot.dslink.node.value.Value;
import org.dsa.iot.dslink.node.value.ValueType;
import org.dsa.iot.dslink.util.handler.Handler;
import org.dsa.iot.dslink.util.json.JsonObject;
import org.dsa.iot.redis.driver.RedisConnectionHelper;
import org.dsa.iot.redis.model.RedisConfig;
import org.dsa.iot.redis.model.RedisConstants;
import org.dsa.iot.redis.provider.ActionProvider;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class AddConnectionHandler extends ActionProvider implements
        Handler<ActionResult> {

    private static final Logger LOG = LoggerFactory
            .getLogger(AddConnectionHandler.class);

    private NodeManager manager;

    public AddConnectionHandler(NodeManager manager) {
        this.manager = manager;
    }

    @Override
    public void handle(ActionResult event) {
        LOG.debug("Entering add connection handle");

        Value name = event.getParameter(RedisConstants.NAME, new Value(""));
        Node child = manager.getSuperRoot().getChild(name.getString(), false);
        Node status = manager.getSuperRoot().getChild(RedisConstants.STATUS, false);
        if (name.getString() != null && !name.getString().isEmpty()) {
            if (child != null) {
                status.setValue(new Value("connection with name "
                                                  + name.getString() + " alredy exist"));
                return;
            }
        } else {
            status.setValue(new Value("name is empty"));
            return;
        }

        Value url = event.getParameter(RedisConstants.URL, new Value(""));
        if (url.getString() == null || url.getString().isEmpty()) {
            status.setValue(new Value("url is empty"));
            return;
        }

        Value user = event.getParameter(RedisConstants.USER, new Value(""));
        Value password = event.getParameter(RedisConstants.PASSWORD, new Value(
                ""));

        Value driver = event.getParameter(RedisConstants.DRIVER, new Value(""));
        if (driver.getString() == null || driver.getString().isEmpty()) {
            status.setValue(new Value("driver is empty"));
            return;
        }

        Value timeout = event.getParameter(RedisConstants.DEFAULT_TIMEOUT,
                                           new Value(60));
        Value poolable = event.getParameter(RedisConstants.POOLABLE);

        RedisConfig config = new RedisConfig();
        config.setName(name.getString());
        config.setUrl(url.getString());
        config.setUser(user.getString());
        config.setPassword(password.getString().toCharArray());
        config.setPoolable(poolable.getBool());
        config.setTimeout((Integer) timeout.getNumber());
        config.setDriverName(driver.getString());
        LOG.debug(config.toString());

        // create DataSource if specified
        if (poolable.getBool()) {
            config.setDataSource(RedisConnectionHelper
                                         .configureDataSource(config));
        }

        JsonObject object = new JsonObject();
        object.put(RedisConstants.NAME, config.getName());
        object.put(RedisConstants.URL, config.getUrl());
        object.put(RedisConstants.USER, config.getUser());
        object.put(RedisConstants.POOLABLE, config.isPoolable());
        object.put(RedisConstants.DEFAULT_TIMEOUT, config.getTimeout());
        object.put(RedisConstants.DRIVER, config.getDriverName());

        NodeBuilder builder = manager.createRootNode(name.getString());
        builder.setAttribute(RedisConstants.ACTION, new Value(true));
        builder.setAttribute(RedisConstants.CONFIGURATION, new Value(object));
        builder.setPassword(password.getString().toCharArray());
        Node conn = builder.build();
        config.setNode(conn);

        Node connStatus = conn.createChild(RedisConstants.STATUS, false).build();
        connStatus.setValueType(ValueType.STRING);
        connStatus.setValue(new Value(RedisConstants.CREATED));

        builder = conn.createChild(RedisConstants.DELETE_CONNECTION, false);
        builder.setAction(getDeleteConnectionAction(manager));
        builder.setSerializable(false);
        builder.build();

        builder = conn.createChild(RedisConstants.EDIT_CONNECTION, false);
        builder.setAction(getEditConnectionAction(config));
        builder.setSerializable(false);
        builder.build();
        LOG.debug("Connection {} created", conn.getName());

        {
            builder = conn.createChild(RedisConstants.QUERY, false);
            builder.setAction(getQueryAction(config));
            builder.setSerializable(false);
            builder.build();
        }
        {
            builder = conn.createChild(RedisConstants.STREAMING_QUERY, false);
            builder.setAction(getStreamingQueryAction(config));
            builder.setSerializable(false);
            builder.build();
        }
        if ("org.postgresql.Driver".equals(config.getDriverName())) {
            builder = conn.createChild(RedisConstants.COPY, false);
            builder.setAction(getCopyAction(config));
            builder.setSerializable(false);
            builder.build();
        }
        {
            builder = conn.createChild(RedisConstants.UPDATE, false);
            builder.setAction(getUpdateAction(config));
            builder.setSerializable(false);
            builder.build();
        }

        status.setValue(new Value("connection created"));
    }
}
