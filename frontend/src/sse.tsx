import { useEffect } from "react";

export default function SSEConnect() {
    useEffect(() => {
        const token = localStorage.getItem("access_token");

        if (!token) {
            console.log("❌ 토큰 없음. SSE 연결하지 않음.");
            return;
        }

        const eventSource = new EventSource(`http://localhost:8087/api/notification/subscribe?token=${token}`);

        eventSource.onmessage = (event) => {
            console.log("✅ 받은 메시지:", event.data);
        };

        eventSource.onerror = (error) => {
            console.error("❌ SSE 오류 발생:", error);
            eventSource.close();
        };

        return () => {
            eventSource.close();
        };
    }, []);

    return null;
}
