$(document).ready(function () {
    let websocket = null;
    function connect() {
        if('WebSocket' in window) {
            websocket = new WebSocket("ws://" + window.location.host + "/api/websocket/TPA");
        } else {
            alert('Web browser not support websocket');
            return;
        }

        websocket.onerror = () => {
            setMessageToOtherSide("WebSocket connection error");
        };

        websocket.onopen = event => {
            setMessageToOtherSide("WebSocket connection successfully");
        };

        websocket.onmessage = event => {
            console.log(event.data);
            setMessageFromAnonymous(event.data);
        };

        websocket.onclose = event => {
            setMessageToOtherSide("WebSocket connection close");
        };
    }
    connect();

    function closeWebSocket() {
        websocket.close();
    }

    function sendMsg() {
        const message = $('#text-input').val();
        websocket.send(message);
        setMessageToSelfSide(message);
    }

    function setMessageToOtherSide(message) {
        $('.body').append(`
                <div class="text-center mt-3" style="font-size: 12px;color: gray;">
                    ${message}
                </div>`);
    }

    function setMessageFromAnonymous(message) {
        $('.body').append(`
                <div class="incoming">
                    <div>
                        <span style="font-size: 10px;">
                            Anonymous
                        </span>
                    </div>
                    <div class="bubble">
                        <p>${message}</p>
                    </div>
                </div>`);
    }

    function setMessageToSelfSide(message) {
        $('.body').append(`
                <div class="outgoing">
                    <div class="bubble lower">
                        <p>${message}</p>
                    </div>
                </div>`);
    }

    $('#send-msg-btn').click(function() {
        sendMsg();
    });

    // send ping to server every 3 seconds
    // const keepAlive = function (timeout = 20000) {
    //     if (websocket.readyState === websocket.OPEN) {
    //         websocket.send('ping');
    //     }
    //
    //     setTimeout(keepAlive, timeout);
    // };

    $(window).on('beforeunload offline', event => {
        closeWebSocket();
    });
});

