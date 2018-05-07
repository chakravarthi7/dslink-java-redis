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
		 
		  Value val= null;
		  String key=null;
		  JedisPool jedisPool =null;
		  Jedis jedis=null;
		  
		  try {
			  key = event.getParameter(RedisConstants.KEY).toString();
		  }catch(Exception e) {
			  setStatusMessage("Invalid Input",  null);
		  }
		  
		 
		 	  
		    if (key != null && !key.isEmpty()) {
		    	
		    	
		    	try {  
    				jedisPool = new JedisPool(RedisConnectionHelper.configureDataSource(config), config.getUrl());
    				jedis=jedisPool.getResource();
    				String Value1=jedis.get(key);
        		 	val=new Value(Value1);
    				setStatusMessage("Got Value Scussesfull", null);
    			}catch(Exception e) {
    				setStatusMessage("Error at Jedis connection", null);}
    			finally {
    				if (jedis != null) {
    					jedisPool.returnResource(jedis);
    					}}
       
	        	
		    	Table table = event.getTable();
    		
    		 	if(val != null) {
    		 		ValueType type = ValueType.STRING;
    		 		Parameter p = new Parameter("Value", type);
    		 		table.addColumn(p);
    		 		Row row = new Row();
    	            row.addValue(val);
    	            table.addRow(row);
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
