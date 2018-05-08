package org.dsa.iot.redis.handlers;

import org.dsa.iot.dslink.node.actions.ActionResult;
import org.dsa.iot.dslink.node.actions.Parameter;
import org.dsa.iot.dslink.node.actions.table.Row;
import org.dsa.iot.dslink.node.actions.table.Table;
import org.dsa.iot.dslink.node.value.Value;
import org.dsa.iot.dslink.node.value.ValueType;
import org.dsa.iot.dslink.util.handler.Handler;
import org.dsa.iot.redis.driver.RedisConnectionHelper;
import org.dsa.iot.redis.model.RedisConfig;
import org.dsa.iot.redis.model.RedisConstants;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;

public class HashGetQueryHandler implements Handler<ActionResult> {

	 private static final Logger LOG = LoggerFactory
	            .getLogger(AddConnectionHandler.class);

	  private RedisConfig config;
	  


	    public HashGetQueryHandler(RedisConfig config) {
	        this.config = config;
	    }
	    
	   

	@Override
	public void handle(ActionResult event) {
		// TODO Auto-generated method stub
			
		String  key =null , field=null , Value=null;
		JedisPool jedisPool =null;
		Jedis jedis=null;
		
		try {
		   field = event.getParameter(RedisConstants.FIELD).toString();
		   key = event.getParameter(RedisConstants.KEY).toString();
		} catch(Exception e) {
			setStatusMessage("Invalid Input", null);
		}
		  
		
		if (key != null  && !key.isEmpty()) {
			  
		  		if (field != null && !field.isEmpty()) {
			  
		  		  try {  
	    				jedisPool = new JedisPool(RedisConnectionHelper.configureDataSource(config), config.getUrl());
	    				jedis=jedisPool.getResource();
	    				boolean keyexist = jedis.hexists(key, field);
	    				if(keyexist == true) {
	    				Value=jedis.hget(key, field);
	    				setOutput(Value,event);
	    				setStatusMessage("Get Value Sucessfully", null);
	    				}else
	    					setStatusMessage("Key Not Exists",null);
	    			}catch(Exception e) {
	    					setStatusMessage("Error at Jedis connection", null);}
	    			finally {
	    				if (jedis != null) {
	    					jedisPool.returnResource(jedis);
	    					}} 	  
		  		 
		        	
		        	 	            
		        } else {
		        	 setStatusMessage("field is empty", null);
		        }
		    }else {
		    	setStatusMessage("Key is empty", null);
		    }
		  
		 
  }
	
	public void setOutput(String Value , ActionResult event) {
		
	    Table table = event.getTable();
		
	 	if(Value != null) {
	 		ValueType type = ValueType.STRING;
	 		Parameter p = new Parameter("Value", type);
	 		table.addColumn(p);
	 		Row row = new Row();
            row.addValue(new Value(Value));
            table.addRow(row);
       	}else {
       	 setStatusMessage("value is null", null);
       	}
		
	}

	private void setStatusMessage(String message, Exception e) { 
	        if (e == null) {
	            LOG.debug(message);
	        } else {
	            LOG.warn(message, e);
	        }
	        config.getNode().getChild(RedisConstants.STATUS, false)
	              .setValue(new Value(message));
	    }
	   
	  
}


