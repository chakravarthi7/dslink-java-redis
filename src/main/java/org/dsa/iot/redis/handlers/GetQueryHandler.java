package org.dsa.iot.redis.handlers;

import org.dsa.iot.dslink.node.actions.ActionResult;
import org.dsa.iot.dslink.node.value.Value;
import org.dsa.iot.dslink.util.handler.Handler;
import org.dsa.iot.redis.model.RedisConfig;
import org.dsa.iot.redis.model.RedisConstants;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

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
      

        
	}

}
