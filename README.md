# ChatRoom_by_Socket
I developed a simple web-based chatroom using Spring Boot, which is an open source Java-based framework and finally deplyed it to Heroku, which is a cloud platform as a service (PaaS) supporting several programming languages. The purpose of this programming practice is mainly to not only learn the websocket fundamental but also be regarded as homework assigned by my advisor. Most of all, it's important to familiar with JAVA, Spring Boot, Websocket, JavaScript and so on.

## Introduction

## Synopsis
### Back-end code
####   receive front-end websocket
``` Java
@Component
@ServerEndpoint(value = "/websocket/{info}")
public class WebSocketServerController {
    ...
    ...
    ...
@OnOpen
    public void start(@PathParam(value = "info") String name, Session session) {
    	logger.info("start");
        this.session = session;
        this.name = name;
        connections.add(this);
        String message = String.format("%s has joined chatroom now!", name);
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
    	broadcast(message);
    }
 
    @OnError
    public void onError(Throwable t) throws Throwable {
        logger.info("Chat Error: " + t.toString());
    }
``` 
####   broadcast message to all online users
``` Java
    private void broadcast(String msg) {
    	logger.info(connections.toString());
        for (WebSocketServerController  client : connections) {
            try {
                synchronized (client) {
                    client.session.getBasicRemote().sendText(msg);
                }
            } catch (IOException e) {
            	logger.warning("Chat Error: Failed to send message to client");
                connections.remove(client);
                try {
                    client.session.close();
                } catch (IOException e1) {
                    e.printStackTrace();
                }
                String message = String.format("Unfortunately! %s has disconnected!", name);
                broadcast(message);
            }
        }
    }
``` 

## Deploying to Heroku

## Required language, package and tool
* Java JDK
* JAVA Spring Boot
* javax.websocket package
* HTML
* JavaScript
* Heroku
* Git

## Demo Screenshot(gif)
![image](https://i.imgur.com/Mk1AXkP.gif)

## Link
[ChatRoom Demo](https://webforchatroom.herokuapp.com/)

## Reference
* https://blog.csdn.net/qq_35017509/article/details/86243089 
* https://www.callicoder.com/deploy-host-spring-boot-apps-on-heroku/
* https://blog.csdn.net/u013716535/article/details/86180412 
* https://blog.csdn.net/u011072139/article/details/60768663
* https://blog.csdn.net/softwave/article/details/51994253
* https://blog.csdn.net/qq_33696345/article/details/79989880
* https://blog.xuite.net/hs19890622/job/388606028-%E5%9F%B7%E8%A1%8CSpring+Boot+%E5%B0%88%E6%A1%88%E7%9A%84%E6%96%B9%E5%BC%8F 
