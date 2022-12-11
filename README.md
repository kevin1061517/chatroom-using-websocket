## ChatRoom_by_Socket
I built a simple chatroom web application using spring boot framework in the JAVA programming language and finally deplyed it to Heroku, which is a cloud platform as a service (PaaS) supporting several programming languages. The purpose of this programming practice is mainly to not only learn the websocket fundamental but also be regarded as homework assigned by my advisor. Most of all, it's important to familiar with JAVA, Spring Boot, Websocket, JavaScript and so on.

### How to close a websocket when client's network suddenly turn offline
1. Using the js offline event handle

   If we would like to detect if the user went offline we simply add websocket close function into offline event function.
    #### front-end
    ``` Javascript
        function closeWebSocket() {
            websocket.close();
        }
        
        window.on('beforeunload offline', event => {
            closeWebSocket();
        });
    ``` 
    #### back-end (WebSocketServer)
    ``` Java
        @OnClose
        public void onClose(Session session) {
            Client client = CURRENT_CLIENTS.get(session.getId());
            log.info("onClose. client name: {}", client.getTeamName());
            CURRENT_CLIENTS.remove(session.getId());
        }
    ```
2. Using Ping interval on the client-side and decrease the websocket session timeout on server-side
   #### front-end
    ``` Javascript
        // send ping to server every 3 seconds
        const keepAlive = function (timeout = 20000) {
            if (websocket.readyState === websocket.OPEN) {
                websocket.send('ping');
            }
    
            setTimeout(keepAlive, timeout);
        };
    ``` 
    #### back-end (WebSocketConfig)
    ``` Java
        @Bean
        public ServletServerContainerFactoryBean createWebSocketContainer() {
            ServletServerContainerFactoryBean container = new ServletServerContainerFactoryBean();
            container.setMaxSessionIdleTimeout(5000L);
    
            return container;
        }
    ```

## Demo Screenshot(gif)
![image](https://i.imgur.com/4BdjD1V.gif)

## Link
[ChatRoom Demo](https://webforchatroom.herokuapp.com/)

## Reference
* https://blog.csdn.net/qq_35017509/article/details/86243089 
* https://www.callicoder.com/deploy-host-spring-boot-apps-on-heroku/
* https://blog.csdn.net/u013716535/article/details/86180412 
* https://www.cnblogs.com/wqsbk/p/8661141.html
* https://elements.heroku.com/buttons/juliuskrah/spring-profiles
