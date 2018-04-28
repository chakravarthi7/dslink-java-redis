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
		 System.out.println("In new method -HASH set query handler");
		 String Field = null,Key = null,Value = null;
		 try {
			
		   Key = event.getParameter(RedisConstants.KEY).toString();
		   Field = event.getParameter(RedisConstants.FIELD).toString();
		   Value = event.getParameter(RedisConstants.VALUE).toString();
		 } catch(Exception e){
			 System.out.println("Input Issue");
			 setStatusMessage("Input Issue", null);
		 }
		  System.out.println(Field + "    "+Key+ "  "+ Value);
		  
		  if (Key != null && Key.toString() != null && !Key.toString().isEmpty()) {
		  
		   if (Field != null && Field.toString() != null && !Field.toString().isEmpty()) {
		  
		  
	        	
	       if (Value != null && Value.toString() != null && !Value.toString().isEmpty()) {
	        		 
	        	    		
	        		 	System.out.println(config.getUrl());
	        		 	JedisPool jedisPool = new JedisPool(RedisConnectionHelper.configureDataSource(config), config.getUrl());
	        		 	Jedis jedis=jedisPool.getResource();
	        		 	Long x=jedis.hset(Key, Field, Value);
	        		 	if(x==0)
	        		 		System.out.println("value updated");
	        		 	else
	        		 		System.out.println("value inserted");
	        		 	
	        		 	setStatusMessage("Value Inserted Scussesfull", null);
	        		 	
	        		   System.out.println("Inside " + Field + "    "+Key+ "  "+ Value);
	        		//	  setStatusMessage("Set Value Sucessfully", null);

	        	 }else {
	        		 System.out.println("Value is empty");
	        		  setStatusMessage("Value is empty", null);
	        	 }
	       
	            
	        } else {
	        	System.out.println("Key is empty");
	            setStatusMessage("Key is empty", null);
	        }
	    }else {
	    	System.out.println("HName is empty");
	    	setStatusMessage("HName is empty", null);
	    }
			}
	
	 private void setStatusMessage(String message, Exception e) { 
	        if (e == null) {
	            LOG.debug(message);
	        } else {
	            LOG.warn(message, e);
	        }
	    	//System.out.println("In Set Status Message  "+  config.getNode().getChild(RedisConstants.STATUS, false));
	        config.getNode().getChild(RedisConstants.STATUS, false)
	              .setValue(new Value(message));
	    }
	   
	  
}


