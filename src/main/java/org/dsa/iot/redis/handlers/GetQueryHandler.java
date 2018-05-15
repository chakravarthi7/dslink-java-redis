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
		  int dbvalue=0;
		  try {
			  key = event.getParameter(RedisConstants.KEY).toString();
			  dbvalue=(int) event.getParameter(RedisConstants.DATABASE).getNumber();
		  }catch(Exception e) {
			  setStatusMessage("Invalid Input",  null);
		  }
		  
			if(dbvalue < 0  || dbvalue>15 ) {
				setStatusMessage("Db Value is must between 0 to 15 ", null);
			}else {
		 	  
		    if (key != null && !key.isEmpty()) {
		    	
		    	
		    	try {  
    				jedisPool = new JedisPool(RedisConnectionHelper.configureDataSource(config), config.getUrl());
    				jedis=jedisPool.getResource();
    				
    				jedis.select(dbvalue);
    				boolean keyexist=jedis.exists(key);
    				if(keyexist == true) {
    				String Value1=jedis.get(key);
        		 	val=new Value(Value1);
        		 	setOutput(val, event);
    				setStatusMessage("Got Value Scussesfull", null);
    				}else
    					setStatusMessage("Key Not Exists",null);
    			}catch(Exception e) {
    				setStatusMessage("Error at Jedis connection", null);}
    			finally {
    				if (jedis != null) {
    					jedisPool.returnResource(jedis);
    					}}
       
	        	
		    
    	           
	        } else {
	            setStatusMessage("Key is empty", null);
	        }
			}
     }
	
	public void setOutput(Value val , ActionResult event) {
		
		Table table = event.getTable();
		
	 	if(val != null) {
	 		ValueType type = ValueType.STRING;
	 		Parameter p = new Parameter("Value", type);
	 		table.addColumn(p);
	 		Row row = new Row();
            row.addValue(val);
            table.addRow(row);
          }else
        	  setStatusMessage("Value is Null", null);
		
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
