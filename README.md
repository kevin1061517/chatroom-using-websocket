# ChatRoom_by_Socket
I built a simple chatroom web application using spring boot framework in the JAVA programming language and finally deplyed it to Heroku, which is a cloud platform as a service (PaaS) supporting several programming languages. The purpose of this programming practice is mainly to not only learn the websocket fundamental but also be regarded as homework assigned by my advisor. Most of all, it's important to familiar with JAVA, Spring Boot, Websocket, JavaScript and so on.

## Introduction

## Synopsis
``` Javascript
        var Chat = {};
   	 
        Chat.socket = null;
        Chat.connect = (function(host) {
            if ('WebSocket' in window) {
                Chat.socket = new WebSocket(host);
            } else if ('MozWebSocket' in window) {
                Chat.socket = new MozWebSocket(host);
            } else {
                Console.log('Error: WebSocket is not supported by this browser.');
                return;
            }

            Chat.socket.onopen = function () {
                Console.log("Hint: You are joining the chatroom now!")
                document.getElementById('chat').onkeydown = function(event) {
                    if (event.keyCode == 13) {
                        Chat.sendMessage();
                    }
                };
            };
 
            Chat.socket.onclose = function () {
                document.getElementById('chat').onkeydown = null;
                Console.log('Info: WebSocket closed');
            };
 
            Chat.socket.onmessage = function (message) {
            	console.log(message.data);
            	if(message.data.match('Online users:')!=null){
            		document.getElementById('box').innerText = message.data;
            	}
            	else{
            		Console.log(message.data);
            	}
                
            };
        });
 
        Chat.initialize = function() {
        	var roomid = 0;
        	console.log('ws://' + window.location.host + '/websocket/'+name);
            if (window.location.protocol == 'http:') {
                Chat.connect('ws://' + window.location.host + '/websocket/'+name);
            } else {
                Chat.connect('wss://' + window.location.host + '/websocket/'+name);
            }

        };
``` 

### WebSocketServerController
####   receive front-end websocket
``` Java
@Component
@ServerEndpoint(value = "/websocket/{info}")
public class WebSocketServerController {
    ...
    ...
    ...
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
### WebSocketConfig
``` Java
..
@Configuration
@ConditionalOnWebApplication
public class WebSocketConfig {
	@Bean
    public ServerEndpointExporter serverEndpointExporter() {
        return new ServerEndpointExporter();
    }
}
``` 

## Deploying to Heroku
``` 
mvn clean package
``` 
## Required language, package and tool
* JAVA Spring Boot
* javax.websocket package
* HTML
* JavaScript
* Heroku
* Git

## Demo Screenshot(gif)
![image](https://i.imgur.com/4BdjD1V.gif)

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
* https://www.cnblogs.com/wqsbk/p/8661141.html
* https://elements.heroku.com/buttons/juliuskrah/spring-profiles
