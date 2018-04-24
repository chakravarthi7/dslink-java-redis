package org.dsa.iot.redis.handlers;

import org.dsa.iot.dslink.node.actions.ActionResult;
import org.dsa.iot.dslink.node.value.Value;
import org.dsa.iot.dslink.util.handler.Handler;
import org.dsa.iot.redis.driver.RedisConnectionHelper;
import org.dsa.iot.redis.model.RedisConfig;
import org.dsa.iot.redis.model.RedisConstants;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;

public class GetQueryHandler implements Handler<ActionResult> {
	
	 private static final Logger LOG = LoggerFactory
	            .getLogger(AddConnectionHandler.class);

	  private RedisConfig config;

	    public GetQueryHandler(RedisConfig config) {
	        this.config = config;
	    }

	@Override
	public void handle(ActionResult event) {
		// TODO Auto-generated method stub
		  System.out.println("In new method - get query handler");
		  
		  String Key = event.getParameter(RedisConstants.KEY).toString();
		  
		  System.out.println(Key );
		  
		    if (Key != null && Key.toString() != null && !Key.toString().isEmpty()) {
	        	
		    	System.out.println(config.getUrl());
    		 	JedisPool jedisPool = new JedisPool(RedisConnectionHelper.configureDataSource(config), config.getUrl());
    		 	Jedis jedis=jedisPool.getResource();
    		 	String Value1=jedis.get(Key);
    		 	Value val=new Value(Value1);
    		 	
    		 	System.out.println(Value1);
    		 
    			System.out.println(config.getNode().getChild(RedisConstants.VALUE,false));
    		 	  setStatusMessage("Get Value Sucessfully", null);
	            
	        } else {
	            setStatusMessage("Key is empty", null);
	        }

     }

	   private void setStatusMessage(String message, Exception e) {
	        if (e == null) {
	            LOG.debug(message);
	        } else {
	            LOG.warn(message, e);
	        }
	        System.out.println(config.getNode());
	    	System.out.println(config.getNode().getChild(RedisConstants.STATUS, false));
	        config.getNode().getChild(RedisConstants.STATUS, false)
	              .setValue(new Value(message));
	    }
}
