package org.dsa.iot.redis.provider;

import org.dsa.iot.dslink.node.NodeManager;
import org.dsa.iot.dslink.node.Permission;
import org.dsa.iot.dslink.node.actions.Action;
import org.dsa.iot.dslink.node.actions.EditorType;
import org.dsa.iot.dslink.node.actions.Parameter;
import org.dsa.iot.dslink.node.actions.ResultType;
import org.dsa.iot.dslink.node.value.Value;
import org.dsa.iot.dslink.node.value.ValueType;
import org.dsa.iot.redis.driver.RedisConnectionHelper;
import org.dsa.iot.redis.handlers.*;
import org.dsa.iot.redis.model.RedisConfig;
import org.dsa.iot.redis.model.RedisConstants;
import org.dsa.iot.redis.postgres.PostgresCopyHandler;

public class ActionProvider {

    public Action getDeleteConnectionAction(NodeManager manager) {
        return new Action(Permission.READ,
                new DeleteConnectionHandler(manager));
    }

    public Action getEditConnectionAction(RedisConfig config) {
        Action action = new Action(Permission.READ, new EditConnectionHandler(
                config));
        action.addParameter(new Parameter(RedisConstants.URL, ValueType.STRING,
                new Value(config.getUrl())).setPlaceHolder("jdbc:mysql://127.0.0.1:3306"));
        action.addParameter(new Parameter(RedisConstants.USER, ValueType.STRING,
                new Value(config.getUser())));
        action.addParameter(new Parameter(RedisConstants.PASSWORD,
                ValueType.STRING).setEditorType(EditorType.PASSWORD));
        action.addParameter(new Parameter(RedisConstants.DRIVER, ValueType
                .makeEnum(RedisConnectionHelper.getRegisteredDrivers()),
                new Value(config.getDriverName())));
        action.addParameter(new Parameter(RedisConstants.DEFAULT_TIMEOUT,
                ValueType.NUMBER, new Value(config.getTimeout())));
        action.addParameter(new Parameter(RedisConstants.POOLABLE,
                ValueType.BOOL, new Value(config.isPoolable())));
        return action;
    }

    public Action getAddConnectionAction(NodeManager manager) {

        Action action = new Action(Permission.READ, new AddConnectionHandler(
                manager));
        action.addParameter(new Parameter(RedisConstants.NAME, ValueType.STRING));
        action.addParameter(new Parameter(RedisConstants.URL, ValueType.STRING).setPlaceHolder("jdbc:mysql://127.0.0.1:3306"));
        action.addParameter(new Parameter(RedisConstants.USER, ValueType.STRING));
        action.addParameter(new Parameter(RedisConstants.PASSWORD,
                ValueType.STRING).setEditorType(EditorType.PASSWORD));
        {
            String[] drivers = RedisConnectionHelper.getRegisteredDrivers();
            Value value;
            if (drivers.length > 0) {
                value = new Value(drivers[0]);
            } else {
                value = new Value((String) null);
            }
            action.addParameter(new Parameter(RedisConstants.DRIVER, ValueType
                    .makeEnum(drivers), value));
        }
        action.addParameter(new Parameter(RedisConstants.DEFAULT_TIMEOUT,
                ValueType.NUMBER));
        action.addParameter(new Parameter(RedisConstants.POOLABLE,
                ValueType.BOOL, new Value(true)));
        return action;
    }

    public Action getStreamingQueryAction(RedisConfig config) {
        Action action = new Action(Permission.READ, new StreamQueryHandler(config));
        action.addParameter(new Parameter(RedisConstants.SQL, ValueType.STRING).setEditorType(EditorType.TEXT_AREA));
        action.setResultType(ResultType.STREAM);
        return action;
    }

    public Action getQueryAction(RedisConfig config) {
        Action action = new Action(Permission.READ, new QueryHandler(config));
        action.addParameter(new Parameter(RedisConstants.SQL, ValueType.STRING).setEditorType(EditorType.TEXT_AREA));
        action.setResultType(ResultType.TABLE);
        return action;
    }

    public Action getUpdateAction(RedisConfig config) {
        Action action = new Action(Permission.WRITE, new UpdateHandler(config));
        action.addParameter(new Parameter(RedisConstants.SQL, ValueType.STRING));
        action.addResult(new Parameter(RedisConstants.ROWS_UPDATED, ValueType.NUMBER));
        return action;
    }

    public Action getCopyAction(RedisConfig config) {
        Action action = new Action(Permission.WRITE, new PostgresCopyHandler(config));
        action.addParameter(new Parameter(RedisConstants.SQL, ValueType.STRING));
        {
            Parameter p = new Parameter(RedisConstants.ROWS, ValueType.STRING);
            p.setEditorType(EditorType.TEXT_AREA);
            action.addParameter(p);
        }
        action.addResult(new Parameter(RedisConstants.ROWS_UPDATED, ValueType.NUMBER));
        return action;
    }
}
