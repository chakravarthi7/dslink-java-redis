package org.dsa.iot.redis.provider;

import org.dsa.iot.dslink.node.NodeManager;
import org.dsa.iot.dslink.node.Permission;
import org.dsa.iot.dslink.node.actions.Action;
import org.dsa.iot.dslink.node.actions.EditorType;
import org.dsa.iot.dslink.node.actions.Parameter;
import org.dsa.iot.dslink.node.actions.ResultType;
import org.dsa.iot.dslink.node.value.Value;
import org.dsa.iot.dslink.node.value.ValueType;
import org.dsa.iot.redis.handlers.AddConnectionHandler;
import org.dsa.iot.redis.handlers.DeleteConnectionHandler;
import org.dsa.iot.redis.handlers.GetQueryHandler;
import org.dsa.iot.redis.handlers.HashGetQueryHandler;
import org.dsa.iot.redis.handlers.HashSetQueryHandler;
import org.dsa.iot.redis.handlers.SetQueryHandler;
import org.dsa.iot.redis.model.RedisConfig;
import org.dsa.iot.redis.model.RedisConstants;

public class ActionProvider {

	Value db=new Value(0);
    public Action getDeleteConnectionAction(NodeManager manager) {
        return new Action(Permission.READ,
                new DeleteConnectionHandler(manager));
    }


    public Action getAddConnectionAction(NodeManager manager) {
    	 
        Action action = new Action(Permission.READ, new AddConnectionHandler(manager));
        System.out.println(action.toString() );
        action.addParameter(new Parameter(RedisConstants.NAME, ValueType.STRING));
        action.addParameter(new Parameter(RedisConstants.URL, ValueType.STRING).setPlaceHolder("localhost"));
        action.addParameter(new Parameter(RedisConstants.USER, ValueType.STRING));
        action.addParameter(new Parameter(RedisConstants.PASSWORD,ValueType.STRING).setEditorType(EditorType.PASSWORD));
        action.addParameter(new Parameter(RedisConstants.POOLABLE,ValueType.BOOL, new Value(true)));
        
   
      
        return action;
       
    }
    public Action setQueryAction(RedisConfig config) {
    
        Action action = new Action(Permission.READ, new SetQueryHandler(config));
        action.addParameter(new Parameter(RedisConstants.KEY, ValueType.STRING).setEditorType(EditorType.TEXT_AREA));
	  	action.addParameter(new Parameter(RedisConstants.VALUE, ValueType.STRING).setEditorType(EditorType.TEXT_AREA));
	  	action.addParameter((new Parameter(RedisConstants.DATABASE,ValueType.NUMBER).setDefaultValue(db)));
    	
		return action;
      
    }
    public Action getQueryAction(RedisConfig config) {
    	
    	  Action action = new Action(Permission.WRITE, new GetQueryHandler(config));
    	  action.addParameter(new Parameter(RedisConstants.KEY, ValueType.STRING).setEditorType(EditorType.TEXT_AREA));
    	  action.addParameter((new Parameter(RedisConstants.DATABASE,ValueType.NUMBER).setDefaultValue(db)));
       	  action.setResultType(ResultType.TABLE);
  		return action;
      
    }
   
    public Action hashsetQueryAction(RedisConfig config) {
    	
  	  Action action = new Action(Permission.READ, new HashSetQueryHandler(config));
  	
  	  action.addParameter(new Parameter(RedisConstants.KEY, ValueType.STRING).setEditorType(EditorType.TEXT_AREA));
  	  action.addParameter(new Parameter(RedisConstants.FIELD, ValueType.STRING).setEditorType(EditorType.TEXT_AREA));
	  action.addParameter(new Parameter(RedisConstants.VALUE, ValueType.STRING).setEditorType(EditorType.TEXT_AREA));
	  action.addParameter((new Parameter(RedisConstants.DATABASE,ValueType.NUMBER).setDefaultValue(db)));
    	
	  return action;
    
  }
    
    public Action hashgetQueryAction(RedisConfig config) {
    	
    	  Action action = new Action(Permission.WRITE, new HashGetQueryHandler(config));
    	  
    	  action.addParameter(new Parameter(RedisConstants.KEY, ValueType.STRING).setEditorType(EditorType.TEXT_AREA));
    	  action.addParameter(new Parameter(RedisConstants.FIELD, ValueType.STRING).setEditorType(EditorType.TEXT_AREA));
    	  action.addParameter((new Parameter(RedisConstants.DATABASE,ValueType.NUMBER).setDefaultValue(db)));
    	  action.setResultType(ResultType.TABLE);
  	  return action;
      
    }

  
}
