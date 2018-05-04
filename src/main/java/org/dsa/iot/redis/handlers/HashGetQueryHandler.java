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
		  System.out.println("In new method -HASH get query handler");
		  String Field = event.getParameter(RedisConstants.FIELD).toString();
		  String Key = event.getParameter(RedisConstants.KEY).toString();
		  
		  		if (Key != null && Key.toString() != null && !Key.toString().isEmpty()) {
			  
		  		if (Field != null && Field.toString() != null && !Field.toString().isEmpty()) {
			  
			  		     	        	    		
		        		 	System.out.println(config.getUrl());
		        		 	JedisPool jedisPool = new JedisPool(RedisConnectionHelper.configureDataSource(config), config.getUrl());
		        		 	Jedis jedis=jedisPool.getResource();
		        		 	String Value=jedis.hget(Key, Field);
		        		 	System.out.println(Value);
		        		 	
		        		 	setStatusMessage("Value Inserted Scussesfull", null);
		        		 	
		        		    System.out.println("Inside " + Field + "    "+Key+ "  "+ Value);
		        		//	  setStatusMessage("Set Value Sucessfully", null);
		        		    Table table = event.getTable();
		            		
		        		 	if(Value != null) {
		        		 		ValueType type = ValueType.STRING;
		        		 		Parameter p = new Parameter("Value", type);
		        		 		table.addColumn(p);
		        		 		Row row = new Row();
		        	            row.addValue(new Value(Value));
		        	       //     System.out.println(row.getValues());
		        	            table.addRow(row);
		        	          
		        	        //    System.out.println(table.getRows());
		        		 	//	System.out.println(val);
		        		 	}
		        	 	            
		        } else {
		        	System.out.println("Key is empty");
		            setStatusMessage("Key is empty", null);
		        }
		    }else {
		    	System.out.println("HName is empty");
		    	setStatusMessage("HName is empty", null);
		    }
		  
		        setStatusMessage("Get Value Sucessfully", null);
  }

	private void setStatusMessage(String message, Exception e) { 
	        if (e == null) {
	            LOG.debug(message);
	        } else {
	            LOG.warn(message, e);
	        }
	    	System.out.println("In Set Status Message  "+  config.getNode().getChild(RedisConstants.STATUS, false));
	        config.getNode().getChild(RedisConstants.STATUS, false)
	              .setValue(new Value(message));
	    }
	   
	  
}


