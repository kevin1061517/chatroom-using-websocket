package com.kevin.server;

import java.io.IOException;
import java.util.Set;
import java.util.concurrent.CopyOnWriteArraySet;
import java.util.concurrent.atomic.AtomicInteger;

import javax.websocket.OnClose;
import javax.websocket.OnError;
import javax.websocket.OnMessage;
import javax.websocket.OnOpen;
import javax.websocket.Session;
import javax.websocket.server.PathParam;
import javax.websocket.server.ServerEndpoint;

import org.springframework.stereotype.Component;
import java.util.logging.*;

@Component
@ServerEndpoint(value = "/websocket/{info}")
public class WebSocketServerController {
	private Logger logger = Logger.getLogger("Log");
    private static final String GUEST_PREFIX = "User";
    private static final AtomicInteger connectionIds = new AtomicInteger(0);
    private static final Set<WebSocketServerController> connections =
            new CopyOnWriteArraySet<>();
    
    private String name;
//    private final String nickname;
    private Session session;
 
    public WebSocketServerController () {
//        nickname = GUEST_PREFIX + connectionIds.getAndIncrement();
    }
 

    @OnOpen
    public void start(@PathParam(value = "info") String name, Session session) {
//    	System.out.println("start");
    	logger.info("start");
        this.session = session;
        this.name = name;
        connections.add(this);
        String message = String.format("%s has joined chatroom now!", name);
//        String message = String.format("* %s %s", nickname, "has joined.");
        broadcast(message);
    }
 
    @OnClose
    public void end() {
        connections.remove(this);
        String message = String.format("Unfortunately! %s has disconnected!", name);
        broadcast(message);
    }
 
    @OnMessage
    public void incoming(String message) {
//        String response = String.format("%s: %s", nickname, message);
        //user input 會先到這
//        System.out.println("client: "+response);
    	broadcast(message);
    }
 
    @OnError
    public void onError(Throwable t) throws Throwable {
        System.out.println("Chat Error: " + t.toString());
    }
 
    private void broadcast(String msg) {
    	logger.info(connections.toString());
        for (WebSocketServerController  client : connections) {
            try {
                synchronized (client) {
                	
                    client.session.getBasicRemote().sendText(msg);
                }
            } catch (IOException e) {
            	System.out.println("Chat Error: Failed to send message to client");
                connections.remove(client);
                try {
                    client.session.close();
                } catch (IOException e1) {
                    // Ignore
                }
                String message = String.format("Unfortunately! %s has disconnected!", name);
                broadcast(message);
            }
        }
    }
}
