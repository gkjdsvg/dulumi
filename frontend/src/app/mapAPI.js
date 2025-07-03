function loadKakaoMap(mapContainerId) {
    const script = document.createElement("script");
    script.src = "//dapi.kakao.com/v2/maps/sdk.js?appkey=1f36054feba5748af4c1205c284bcef5&autoload=false";
    script.onload = () => {
        kakao.maps.load(() => {
            const container = document.getElementById(mapContainerId);
            const options = {
                center: new kakao.maps.LatLng(37.5665, 126.978),
                level: 3,
            };
            const map = new kakao.maps.Map(container, options);

            // 예시 마커
            const marker = new kakao.maps.Marker({
                position: new kakao.maps.LatLng(37.5665, 126.978),
            });
            marker.setMap(map);
        });
    };
    document.head.appendChild(script);
}

window.loadKakaoMap = loadKakaoMap;  // 함수 전역에 등록
