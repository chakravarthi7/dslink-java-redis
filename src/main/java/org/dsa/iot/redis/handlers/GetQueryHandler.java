package org.dsa.iot.redis.handlers;

import org.dsa.iot.dslink.methods.StreamState;
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
		  System.out.println("In new method - get query handler");
		  
		  String Key = event.getParameter(RedisConstants.KEY).toString();
		  
		  Value val= null;
		 
		  	System.out.println("The Key Value " +Key );
		  
		    if (Key != null && Key.toString() != null && !Key.toString().isEmpty()) {
	        	
		    	System.out.println(config.getUrl());
    		 	JedisPool jedisPool = new JedisPool(RedisConnectionHelper.configureDataSource(config), config.getUrl());
    		 	Jedis jedis=jedisPool.getResource();
    		 	String Value1=jedis.get(Key);
    		 	val=new Value(Value1);
    		 	
    		 		
    		 	System.out.println("The output Value "+Value1);
    		 	
    		 	 config.getNode().getChild(RedisConstants.STATUS, false).setValue(val);
    		 	
                  
               //   builder.setAction(hashgetQueryAction(config));
    		 	 
    		 	 System.out.println(config.getNode().getChild(RedisConstants.VALUE, false));
    		 	 Table table = event.getTable();
    		
    		 	if(val != null) {
    		 		ValueType type = ValueType.STRING;
    		 		Parameter p = new Parameter("Value", type);
    		 		table.addColumn(p);
    		 		Row row = new Row();
    	            row.addValue(val);
    	       //     System.out.println(row.getValues());
    	            table.addRow(row);
    	          
    	        //    System.out.println(table.getRows());
    		 	//	System.out.println(val);
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
	    //	System.out.println("In Set Status Message  "+  config.getNode().getChild(RedisConstants.STATUS, false));
	        config.getNode().getChild(RedisConstants.STATUS, false)
	              .setValue(new Value(message));
	    }
	 
	   
	  
}
