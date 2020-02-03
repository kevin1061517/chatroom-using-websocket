package com.kevin.server;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.logging.Logger;

import javax.websocket.OnClose;
import javax.websocket.OnError;
import javax.websocket.OnMessage;
import javax.websocket.OnOpen;
import javax.websocket.Session;
import javax.websocket.server.PathParam;
import javax.websocket.server.ServerEndpoint;

import org.springframework.stereotype.Component;

@Component
@ServerEndpoint(value = "/websocket/{info}")
public class WebSocketServerController {
	private Logger logger = Logger.getLogger("Log");
    private static final String GUEST_PREFIX = "User";
//    private static final AtomicInteger connectionIds = new AtomicInteger(0);
    private static Map<WebSocketServerController,String> allUserInfo = new HashMap<>();
//    private final String nickname;
    private Session session;
    
    
    public WebSocketServerController () {
//        nickname = GUEST_PREFIX + connectionIds.getAndIncrement();
    }
 
    //最一開始連線就會進到@OnOpen
    @OnOpen
    public void start(@PathParam(value = "info") String name, Session session) {
    	logger.info("start");
        this.session = session;
        String message = String.format("%s has joined chatroom now!", name);
        allUserInfo.put(this, name);
//        String message = String.format("* %s %s", nickname, "has joined.");
        broadcast(getAllUsers());
        broadcast(message);
    }
    
    //user斷線或關掉web會進入到@OnClose
    @OnClose
    public void end() {
    	//順序要對 不然會出現Null Exception
    	String message = String.format("Unfortunately! %s has disconnected!", allUserInfo.get(this));
    	allUserInfo.remove(this);
    	broadcast(message);
        broadcast(getAllUsers());        
    }
    
    //user 在 front-end發送訊息會先到backend在把訊息broadcat到javascript的  Chat.sendMessage
    @OnMessage
    public void incoming(String message) {
//        String response = String.format("%s: %s", nickname, message);
    	broadcast(message);
    }
 
    @OnError
    public void onError(Throwable t) throws Throwable {
        logger.warning("Chat Error: " + t.toString());
    }
    
    
    //取得在線使用者名單
    private String getAllUsers() {
    	String allusers = "Online users: ";
        for (Entry<WebSocketServerController,String> e: allUserInfo.entrySet()){
        	allusers += e.getValue()+",";
        }
        return allusers.substring(0, allusers.length()-1);
    }
    //傳給所有使用者 但只要出現IOException就跳出迴圈 並結束connection 且要把斷線的使用者 在allUserInfo remove掉
    private void broadcast(String msg) {
    	WebSocketServerController delet_user = null;
    	logger.info(allUserInfo.toString());
    	for (Entry<WebSocketServerController,String> entry: allUserInfo.entrySet()){
	      try {
	          synchronized (entry) {
	        	  entry.getKey().session.getBasicRemote().sendText(msg);
	          }
	      } catch (IOException e) {
	      	  logger.warning("Chat Error: Failed to send message to client");
	      	  delet_user = entry.getKey();
	          try {
	        	  entry.getKey().session.close();
	          } catch (IOException e1) {
	              e.printStackTrace();
	          }
	          String message = String.format("Unfortunately~~ %s has disconnected!", entry.getValue());
	          broadcast(message);
	      }
	      if (delet_user != null) {
	    	  allUserInfo.remove(delet_user);
	    	  broadcast(getAllUsers());
	      }
    	}
    	
//        for (WebSocketServerController  client : connections) {
//            try {
//                synchronized (client) {
//                	
//                    client.session.getBasicRemote().sendText(msg);
//                }
//            } catch (IOException e) {
//            	  logger.warning("Chat Error: Failed to send message to client");
//                connections.remove(client);
//                allUsers.remove(name);
//                try {
//                    client.session.close();
//                } catch (IOException e1) {
//                    e.printStackTrace();
//                }
//                String message = String.format("Unfortunately! %s has disconnected!", name);
//                broadcast(message);
//            }
//        }
    }
}
