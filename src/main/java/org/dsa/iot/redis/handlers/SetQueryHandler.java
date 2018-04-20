package org.dsa.iot.redis.handlers;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

import org.dsa.iot.dslink.node.actions.ActionResult;
import org.dsa.iot.dslink.node.value.Value;
import org.dsa.iot.dslink.util.handler.Handler;
import org.dsa.iot.redis.driver.RedisConnectionHelper;
import org.dsa.iot.redis.model.RedisConfig;
import org.dsa.iot.redis.model.RedisConstants;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import redis.clients.jedis.Jedis;

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
		System.out.println("In new method - Set query handler");
		
		  Value Key = event.getParameter(RedisConstants.KEY);
	        Value value = event.getParameter(RedisConstants.VALUE);
	        
	        System.out.println(Key + "   " + value);
	    
	        if (Key != null && Key.getString() != null && !Key.getString().isEmpty()) {
	        		        	
	        	 if (value != null && value.getString() != null && !value.getString().isEmpty()) {
	        		 
	        		//  Jedis Jedis=getConnection();
	        		 
	        	
	        	 }else {
	        		  setStatusMessage("Key is empty", null);
	        	 }
	       
	            
	        } else {
	            setStatusMessage("Key is empty", null);
	        }
	    }
	        
	  private Connection getConnection() throws SQLException {
	        Connection connection;
	        if (config.isPoolable()) {
	            if (config.getJedisPoolConfig() == null) {
	                config.setJedisPoolConfig(RedisConnectionHelper
	                                             .configureDataSource(config));
	            }
	            connection = config.getDataSource().getConnection();
	        } else {
	            try {
	                Class.forName(config.getDriverName());
	            } catch (ClassNotFoundException e) {
	                LOG.debug(e.getMessage());
	            }

	            connection = DriverManager.getConnection(config.getUrl(),
	                                                     config.getUser(),
	                                                     String.valueOf(config.getPassword()));
	        }
	        return connection;
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
