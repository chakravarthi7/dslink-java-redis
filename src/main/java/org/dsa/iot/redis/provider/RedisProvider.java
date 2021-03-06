package org.dsa.iot.redis.provider;

import java.util.Map;
import java.util.Map.Entry;

import org.dsa.iot.dslink.DSLink;
import org.dsa.iot.dslink.node.Node;
import org.dsa.iot.dslink.node.NodeBuilder;
import org.dsa.iot.dslink.node.NodeManager;
import org.dsa.iot.dslink.node.value.Value;
import org.dsa.iot.dslink.node.value.ValueType;
import org.dsa.iot.dslink.util.json.JsonObject;
import org.dsa.iot.redis.model.RedisConfig;
import org.dsa.iot.redis.model.RedisConstants;

public class RedisProvider extends ActionProvider {

    /**
     * Starts building Node's tree
     *
     * @param link link
     */
    public void run(DSLink link) {
    
        NodeManager manager = link.getNodeManager();
  
        Node superRoot = manager.getNode("/").getNode();
  
        Node status = superRoot.createChild(RedisConstants.STATUS, false).build();
      
        status.setValueType(ValueType.STRING);
        status.setValue(new Value(RedisConstants.READY));
      
        NodeBuilder builder = superRoot
                .createChild(RedisConstants.ADD_CONNECTION_ACTION, false);
        System.out.println("builder " + builder.toString());
        builder.setAction(getAddConnectionAction(manager));
       
        builder.build();

        configureActions(superRoot, manager);
      
    }

    /**
     * Initial actions assignment
     *
     * @param superRoot root
     * @param manager   manager
     */
    private void configureActions(Node superRoot, NodeManager manager) {
        Map<String, Node> childs = superRoot.getChildren();
     
        
        
        for (Entry<String, Node> entry : childs.entrySet()) {
        	
            Node node = entry.getValue();
          
            if (node.getAttribute(RedisConstants.ACTION) != null && node.getAttribute(RedisConstants.ACTION).getBool()) {
        
                JsonObject object = node.getAttribute(RedisConstants.CONFIGURATION).getMap();
                RedisConfig config = new RedisConfig();
                config.setName((String) object.get(RedisConstants.NAME));
                config.setUrl((String) object.get(RedisConstants.URL));
                config.setUser((String) object.get(RedisConstants.USER));
                config.setPassword(node.getPassword());
                config.setPoolable((Boolean) object.get(RedisConstants.POOLABLE));
                config.setNode(node);

                NodeBuilder builder = node.createChild(RedisConstants.DELETE_CONNECTION, false);
                builder.setAction(getDeleteConnectionAction(manager));
                builder.setSerializable(false);
                builder.build();
                
                
                builder = node.createChild(RedisConstants.SET, false);
                builder.setAction(setQueryAction(config));
                builder.setSerializable(false);
                builder.build();
                
                builder = node.createChild(RedisConstants.GET, false);
                builder.setAction(getQueryAction(config));
                builder.setSerializable(false);
                builder.build();
                
                builder = node.createChild(RedisConstants.HSET, false);
                builder.setAction(hashsetQueryAction(config));
                builder.setSerializable(false);
                builder.build();
                
                builder = node.createChild(RedisConstants.HGET, false);
                builder.setAction(hashgetQueryAction(config));
                builder.setSerializable(false);
                builder.build();
                

         
                Node status = node.createChild(RedisConstants.STATUS, false).build();
                status.setValue(new Value(RedisConstants.READY));
            }
        }
    }
}
