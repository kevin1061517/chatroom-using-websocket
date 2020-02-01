# ChatRoom_by_Socket
## Introduction
I developed a simple web-based chatroom using Spring Boot, which is an open source Java-based framework and finally deplyed it to Heroku, which is a cloud platform as a service (PaaS) supporting several programming languages. The purpose of this programming practice is mainly to not only learn the websocket fundamental but also be regarded as homework assigned by my advisor. Most of all, it's important to familiar with JAVA, Spring Boot, Websocket, JavaScript and so on.

## Synopsis
``` Java
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
``` 

``` Java
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
``` 
## Deploying to Heroku

## Requirements & Dependencies

## Demo Screenshot(gif)
![image](https://i.imgur.com/Mk1AXkP.gif)

## URL
[ChatRoom Demo](https://webforchatroom.herokuapp.com/)

## Reference
https://blog.csdn.net/qq_35017509/article/details/86243089
https://www.callicoder.com/deploy-host-spring-boot-apps-on-heroku/https://blog.csdn.net/u013716535/article/details/86180412
https://blog.xuite.net/hs19890622/job/388606028-%E5%9F%B7%E8%A1%8CSpring+Boot+%E5%B0%88%E6%A1%88%E7%9A%84%E6%96%B9%E5%BC%8F
