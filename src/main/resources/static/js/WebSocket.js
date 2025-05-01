const webSocket = new SockJS('/ws');
const stomp = Stomp.over(webSocket);

stomp.connect({}, () => {
    stomp.subscribe("/topic/notices", (message) => {
        const notice = message.body;
        console.log("New notice", notice);
    });
});
