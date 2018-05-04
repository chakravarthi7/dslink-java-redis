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

public class SetQueryHandler implements Handler<ActionResult> {
	  private static final Logger LOG = LoggerFactory
	            .getLogger(AddConnectionHandler.class);

	  private RedisConfig config;
	

	    public SetQueryHandler(RedisConfig config) {
	        this.config = config;
	    }

	@Override
	public void handle(ActionResult event) {
		// TODO Auto-generated method stub
		//System.out.println("In new method - Set query handler");
		
		String Key=null , value = null;
		try {
			 Key = event.getParameter(RedisConstants.KEY).toString();
	         value = event.getParameter(RedisConstants.VALUE).toString();
		}catch(Exception e) {
			setStatusMessage("Invalid Input",  e);
		}
		
	        System.out.println(Key + "   " + value);
	    
	    if (Key != null &&  !Key.isEmpty()) {
	        		        	
	    	if (value != null  && !value.isEmpty()) {
	        		 
	    		
	    		try {  
	    			JedisPool jedisPool = new JedisPool(RedisConnectionHelper.configureDataSource(config), config.getUrl());
	      		 	Jedis jedis=jedisPool.getResource();
	       		 	jedis.set(Key, value); 
	       		 	setStatusMessage("Value Inserted Scussesfull", null);
	       	    }catch(Exception e) {
	        		  	        	                    }
	    		finally{
	    		
	    	        	}
	       
	    	}else {
	           setStatusMessage("Value is empty", null);
	              }
	       
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
	        config.getNode().getChild(RedisConstants.STATUS, false)
	              .setValue(new Value(message));
	    }

}
