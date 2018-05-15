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

public class HashSetQueryHandler implements Handler<ActionResult> {

	private static final Logger LOG = LoggerFactory
            .getLogger(AddConnectionHandler.class);

	private RedisConfig config;
  


    public HashSetQueryHandler(RedisConfig config) {
        this.config = config;
    }
    
	@Override
	public void handle(ActionResult event) {
		// TODO Auto-generated method stub
	
		 String field = null,key = null,value = null;
		 JedisPool jedisPool =null;
		 Jedis jedis=null;
		 int dbvalue=0;
		 try {
			 key = event.getParameter(RedisConstants.KEY).toString();
			 field = event.getParameter(RedisConstants.FIELD).toString();
			 value = event.getParameter(RedisConstants.VALUE).toString();
			 dbvalue=(int) event.getParameter(RedisConstants.DATABASE).getNumber();
		 } catch(Exception e){
			 setStatusMessage("Invalid Input", null);
		 }
		 
		  
		  
		  if (key != null &&  !key.isEmpty()) {
		  
			  if (field != null  && !field.isEmpty()) {
		  
				  if (value != null && !value.isEmpty()) {
	        		 
					  try {  
		    				jedisPool = new JedisPool(RedisConnectionHelper.configureDataSource(config), config.getUrl());
		    				jedis=jedisPool.getResource();
		    				if(dbvalue < 0 || dbvalue>15 ) {
		    					setStatusMessage("DB Value must between 0 to 15", null);
		    				}else {
		    				jedis.select(dbvalue);
		    				boolean keyexist = jedis.hexists(key, field);
		    			
		    				if (keyexist != true) {
		    					jedis.hset(key, field, value);
		    					setStatusMessage("Value Inserted", null);
		    				}	    				
		        		 	else
		        		 		setStatusMessage("Key or field already exists", null);
		    				}
		    			}catch(Exception e) {
		    					setStatusMessage("Error at Jedis connection", null);}
		    			finally {
		    				if (jedis != null) {
		    					jedisPool.returnResource(jedis);
		    					}}
	        	 }else 
	        		  setStatusMessage("Value is empty", null);
	        		       	            
	        } else 
	        	setStatusMessage("Field is empty", null);
	        			  
	    }else 
	    	setStatusMessage("Key is empty", null);
	    
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


