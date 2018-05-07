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
	
		Jedis jedis=null;
		JedisPool jedisPool=null;
		String key=null , value = null;
		
		try {
			 key = event.getParameter(RedisConstants.KEY).toString();
	         value = event.getParameter(RedisConstants.VALUE).toString();
		}catch(Exception e) {
			setStatusMessage("Invalid Input",  null);
		}

	    
	    if (key != null &&  !key.isEmpty()) {
	        		        	
	    	if (value != null  && !value.isEmpty()) {
	        		 
	    			try {  
	    				jedisPool = new JedisPool(RedisConnectionHelper.configureDataSource(config), config.getUrl());
	    				jedis=jedisPool.getResource();
	    				
	    				boolean keyexist=jedis.exists(key);
	    				if(keyexist != true) {
	    					jedis.set(key, value); 
		    				setStatusMessage("Value Inserted Scussesfull", null);
	    				}else
	    					setStatusMessage("Key Already Exists", null);
	    				
	    			}catch(Exception e) {
	    				setStatusMessage("Error at Jedis connection", null);}
	    			finally {
	    				if (jedis != null) {
	    					jedisPool.returnResource(jedis);
	    					}}
	       
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
