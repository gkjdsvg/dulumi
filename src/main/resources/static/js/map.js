// 전역 변수 선언
var map;
var markers = [];
var infowindow;
var ps;
var currentLatLon = null;
var radiusOverlay = null;
const SCHOOL_LAT = 35.14274986871218;
const SCHOOL_LNG = 126.80072294969621;
const SEARCH_RADIUS = 1000; // 1km 반경
const marketCodes = ["MT1", "CS2"];
const cafeCodes = ["CE7"];
const medicineCodes = ["HP8", "PM9"];
var allCodes = [...cafeCodes, ...marketCodes, ...medicineCodes];
var mapInitialized = false;
var apiLoaded = false;

// API 로드 오류 처리
function handleApiError() {
    console.error("Kakao Maps API 로드 실패");
    document.getElementById('map').innerHTML = `
            <div class="error-message">
                <h3>지도 로드 실패</h3>
                <p>카카오 지도 API를 불러오는데 실패했습니다.</p>
                <p>인터넷 연결을 확인하거나 잠시 후 다시 시도해주세요.</p>
                <button onclick="location.reload()">새로고침</button>
            </div>
        `;
}

// API 로드 성공 시 콜백
function onApiLoaded() {
    console.log("Kakao Maps API 로드 성공");
    apiLoaded = true;
    initMapIfReady();
}

// DOM 로드 완료 시 콜백
document.addEventListener('DOMContentLoaded', function() {
    console.log("DOM 로드 완료");
    initMapIfReady();
});

// 지도 초기화 준비 확인
function initMapIfReady() {
    if (apiLoaded && !mapInitialized) {
        try {
            initMap();
            mapInitialized = true;
        } catch (error) {
            console.error("지도 초기화 중 오류 발생:", error);
            handleApiError();
        }
    }
}
    // 지도 초기화 함수
function initMap() {
    try {
    console.log("지도 초기화 시작");

    // 지도 컨테이너 요소 확인
    var mapContainer = document.getElementById('map');
    if (!mapContainer) {
    console.error("지도 컨테이너를 찾을 수 없습니다");
    return;
}

    // 로딩 메시지 제거
    var loadingMessage = document.getElementById('loading-message');
    if (loadingMessage) {
    loadingMessage.style.display = 'none';
}

    // 지도 옵션 설정
    var mapOption = {
    center: new kakao.maps.LatLng(SCHOOL_LAT, SCHOOL_LNG),
    level: 4 // 지도 확대 레벨 (1km 반경이 잘 보이도록 조정)
};

    // 지도 생성
    map = new kakao.maps.Map(mapContainer, mapOption);

    // 인포윈도우 생성
    infowindow = new kakao.maps.InfoWindow({
    zIndex: 1,
    removable: true
});

    // 장소 검색 객체 생성
    ps = new kakao.maps.services.Places();

    // 현재 위치 설정
    currentLatLon = new kakao.maps.LatLng(SCHOOL_LAT, SCHOOL_LNG);

    // 1km 반경 원 표시
    drawRadiusCircle(currentLatLon);

    console.log("지도 초기화 완료");

    // 현재 위치 가져오기
    setTimeout(function() {
    currentLocation();
}, 500);

} catch (error) {
    console.error("지도 초기화 중 오류 발생:", error);
    mapContainer.innerHTML = `
                <div class="error-message">
                    <h3>지도 초기화 실패</h3>
                    <p>오류 메시지: ${error.message}</p>
                    <button onclick="location.reload()">새로고침</button>
                </div>
            `;
}
}

    // 1km 반경 원 그리기
function drawRadiusCircle(center) {
    // 기존 반경 원 제거
    if (radiusOverlay) {
    radiusOverlay.setMap(null);
}

    // 원 생성
    var circle = new kakao.maps.Circle({
    center: center, // 원의 중심 좌표
    radius: SEARCH_RADIUS, // 원의 반경 (미터 단위)
    strokeWeight: 2, // 선의 두께
    strokeColor: '#3B82F6', // 선의 색깔
    strokeOpacity: 0.8, // 선의 불투명도
    strokeStyle: 'dashed', // 선의 스타일
    fillColor: '#3B82F6', // 채우기 색깔
    fillOpacity: 0.05 // 채우기 불투명도
});

    // 지도에 원 표시
    circle.setMap(map);
    radiusOverlay = circle;
}

    // 키워드로 장소 검색
function searchPlaces() {
    try {
    console.log("장소 검색 시작");

    if (!ps) {
    console.error("장소 검색 객체가 초기화되지 않았습니다");
    return;
}

    // 검색 옵션 설정
    const searchOptions = {
    location: currentLatLon,
    radius: SEARCH_RADIUS, // 1km 반경
    sort: kakao.maps.services.SortBy.DISTANCE // 거리순 정렬
};

    // 마커와 목록 초기화
    removeMarker();
    removeAllChildNods(document.getElementById('placesList'));

    // 로딩 메시지 표시
    var listEl = document.getElementById('placesList');
    var loadingEl = document.createElement('li');
    loadingEl.className = 'no-places';
    loadingEl.textContent = '장소를 불러오는 중입니다...';
    listEl.appendChild(loadingEl);

    // 각 카테고리별 검색 실행
    allCodes.forEach((code) => {
    ps.categorySearch(code, placesSearchCB, searchOptions);
});

} catch (error) {
    console.error("장소 검색 중 오류 발생:", error);
    var listEl = document.getElementById('placesList');
    removeAllChildNods(listEl);
    var errorEl = document.createElement('li');
    errorEl.className = 'no-places';
    errorEl.textContent = '검색 중 오류가 발생했습니다. 다시 시도해주세요.';
    listEl.appendChild(errorEl);
}
}

    // 장소검색 콜백함수
function placesSearchCB(data, status, pagination) {
    console.log("검색 상태:", status);

    // 로딩 메시지 제거
    var listEl = document.getElementById('placesList');
    var loadingEls = listEl.getElementsByClassName('no-places');
    if (loadingEls.length > 0) {
    listEl.removeChild(loadingEls[0]);
}

    if (status === kakao.maps.services.Status.OK) {
    console.log("검색 결과 수:", data.length);

    // 1km 이내 결과만 필터링
    var filteredData = filterByDistance(data, currentLatLon, SEARCH_RADIUS);
    console.log("필터링 후 결과 수:", filteredData.length);

    if (filteredData.length > 0) {
    displayPlaces(filteredData);
    displayPagination(pagination);
} else {
    var noResultEl = document.createElement('li');
    noResultEl.className = 'no-places';
    noResultEl.textContent = '1km 이내에 검색 결과가 없습니다.';
    listEl.appendChild(noResultEl);
}
} else if (status === kakao.maps.services.Status.ZERO_RESULT) {
    var noResultEl = document.createElement('li');
    noResultEl.className = 'no-places';
    noResultEl.textContent = '검색 결과가 없습니다.';
    listEl.appendChild(noResultEl);
} else if (status === kakao.maps.services.Status.ERROR) {
    console.error("검색 중 오류 발생");
    var errorEl = document.createElement('li');
    errorEl.className = 'no-places';
    errorEl.textContent = '검색 중 오류가 발생했습니다.';
    listEl.appendChild(errorEl);
}
}

    // 거리 기준으로 결과 필터링
function filterByDistance(places, center, radius) {
    return places.filter(function(place) {
    // 장소 좌표
    var placePosition = new kakao.maps.LatLng(place.y, place.x);

    // 중심점과의 거리 계산 (미터 단위)
    var distance = getDistanceFromLatLonInMeters(
    center.getLat(), center.getLng(),
    placePosition.getLat(), placePosition.getLng()
    );

    // 거리 정보 추가
    place.distance = Math.round(distance);

    // 반경 이내인지 확인
    return distance <= radius;
});
}

    // 두 지점 간의 거리 계산 (미터 단위)
function getDistanceFromLatLonInMeters(lat1, lon1, lat2, lon2) {
    var R = 6371; // 지구 반경 (km)
    var dLat = deg2rad(lat2 - lat1);
    var dLon = deg2rad(lon2 - lon1);
    var a =
    Math.sin(dLat/2) * Math.sin(dLat/2) +
    Math.cos(deg2rad(lat1)) * Math.cos(deg2rad(lat2)) *
    Math.sin(dLon/2) * Math.sin(dLon/2);
    var c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1-a));
    var d = R * c * 1000; // 미터 단위로 변환
    return d;
}

    // 각도를 라디안으로 변환
function deg2rad(deg) {
    return deg * (Math.PI/180);
}

    // 검색 결과 표시 함수
function displayPlaces(places) {
    var listEl = document.getElementById('placesList'),
    menuEl = document.getElementById('menu_wrap'),
    fragment = document.createDocumentFragment(),
    bounds = new kakao.maps.LatLngBounds();

    // 거리순으로 정렬
    places.sort(function(a, b) {
    return a.distance - b.distance;
});

    for (var i = 0; i < places.length; i++) {
    // 마커 생성 및 표시
    var placePosition = new kakao.maps.LatLng(places[i].y, places[i].x),
    marker = addMarker(placePosition, i),
    itemEl = getListItem(i, places[i]);

    // 검색 범위 확장
    bounds.extend(placePosition);

    // 마커와 목록 항목 이벤트 연결
    (function (marker, title, position) {
    kakao.maps.event.addListener(marker, 'mouseover', function () {
    displayInfowindow(marker, title);
});

    kakao.maps.event.addListener(marker, 'mouseout', function () {
    infowindow.close();
});

    kakao.maps.event.addListener(marker, 'click', function() {
    map.setCenter(position);
    displayInfowindow(marker, title);
});

    itemEl.onmouseover = function () {
    displayInfowindow(marker, title);
};

    itemEl.onmouseout = function () {
    infowindow.close();
};

    itemEl.onclick = function() {
    map.setCenter(position);
    displayInfowindow(marker, title);
};
})(marker, places[i].place_name, placePosition);

    fragment.appendChild(itemEl);
}

    // 검색결과 목록에 추가
    listEl.appendChild(fragment);
    menuEl.scrollTop = 0;

    // 검색 범위로 지도 조정
    if (!bounds.isEmpty()) {
    map.setBounds(bounds);
}
}

    // 검색결과 항목 생성
function getListItem(index, places) {
    var el = document.createElement('li');
    el.className = 'item';
    el.style.cursor = 'pointer';

    var markerEl = document.createElement('div');
    markerEl.className = 'markerbg';
    markerEl.textContent = (index + 1);

    var infoEl = document.createElement('div');
    infoEl.className = 'info';

    var titleEl = document.createElement('h5');
    titleEl.textContent = places.place_name;
    infoEl.appendChild(titleEl);

    // 거리 정보 추가
    var distanceEl = document.createElement('span');
    distanceEl.className = 'distance';
    distanceEl.textContent = '현재 위치에서 ' + places.distance + 'm';
    distanceEl.style.color = '#3B82F6';
    distanceEl.style.fontWeight = '500';
    distanceEl.style.fontSize = '0.8rem';
    infoEl.appendChild(distanceEl);

    if (places.road_address_name) {
    var roadAddressEl = document.createElement('span');
    roadAddressEl.textContent = places.road_address_name;
    infoEl.appendChild(roadAddressEl);

    var jibunEl = document.createElement('span');
    jibunEl.className = 'jibun';
    jibunEl.textContent = places.address_name;
    infoEl.appendChild(jibunEl);
} else {
    var addressEl = document.createElement('span');
    addressEl.textContent = places.address_name;
    infoEl.appendChild(addressEl);
}

    if (places.phone) {
    var phoneEl = document.createElement('span');
    phoneEl.className = 'tel';
    phoneEl.textContent = places.phone;
    infoEl.appendChild(phoneEl);
}

    el.appendChild(markerEl);
    el.appendChild(infoEl);

    return el;
}

    // 마커 생성 및 표시
function addMarker(position, idx) {
    // 커스텀 마커 생성
    var content = document.createElement('div');
    content.className = 'custom-marker';
    content.style.width = '36px';
    content.style.height = '36px';
    content.style.fontSize = '14px';
    content.style.color = 'white';
    content.style.fontWeight = 'bold';
    content.style.lineHeight = '36px';
    content.style.textAlign = 'center';
    content.style.background = '#3B82F6';
    content.style.borderRadius = '50%';
    content.style.boxShadow = '0 2px 6px rgba(0,0,0,0.3)';
    content.innerHTML = (idx + 1);

    var customOverlay = new kakao.maps.CustomOverlay({
    position: position,
    content: content,
    yAnchor: 1
});

    customOverlay.setMap(map);
    markers.push(customOverlay);

    return customOverlay;
}

    // 마커 제거
function removeMarker() {
    for (var i = 0; i < markers.length; i++) {
    markers[i].setMap(null);
}
    markers = [];
}

    // 페이지네이션 표시
function displayPagination(pagination) {
    var paginationEl = document.getElementById('pagination'),
    fragment = document.createDocumentFragment();

    // 기존 페이지번호 삭제
    while (paginationEl.hasChildNodes()) {
    paginationEl.removeChild(paginationEl.lastChild);
}

    for (var i = 1; i <= pagination.last; i++) {
    var el = document.createElement('a');
    el.href = "#";
    el.innerHTML = i;

    if (i === pagination.current) {
    el.className = 'on';
} else {
    el.onclick = (function (i) {
    return function () {
    pagination.gotoPage(i);
}
})(i);
}

    fragment.appendChild(el);
}
    paginationEl.appendChild(fragment);
}

    // 인포윈도우 표시
function displayInfowindow(marker, title) {
    var content = '<div class="custom-infowindow">' + title + '</div>';
    infowindow.setContent(content);
    infowindow.open(map, marker);
}

    // 요소의 모든 자식 노드 제거
function removeAllChildNods(el) {
    while (el.hasChildNodes()) {
    el.removeChild(el.lastChild);
}
}

    // 현재 위치 가져오기
function currentLocation() {
    console.log("현재 위치 가져오기 시작");

    if (navigator.geolocation) {
    navigator.geolocation.getCurrentPosition(function (position) {
    var lat = position.coords.latitude;
    var lon = position.coords.longitude;

    currentLatLon = new kakao.maps.LatLng(lat, lon);
    console.log("현재 위치:", lat, lon);

    // 1km 반경 원 다시 그리기
    drawRadiusCircle(currentLatLon);

    // 현재 위치 마커 표시
    displayCurrentLocationMarker(currentLatLon);

    // 지도 중심 이동
    map.setCenter(currentLatLon);

    // 주변 장소 검색
    searchPlaces();

}, function (error) {
    console.error("위치 정보를 가져오지 못했습니다.", error);
    alert("위치 정보를 가져오지 못했습니다. 기본 위치를 사용합니다.");

    // fallback: 학교 위치 사용
    fallbackToSchool();
});
} else {
    alert("이 브라우저에서는 위치 정보를 사용할 수 없습니다.");
    fallbackToSchool();
}
}

    // 학교 위치로 폴백
function fallbackToSchool() {
    currentLatLon = new kakao.maps.LatLng(SCHOOL_LAT, SCHOOL_LNG);

    // 1km 반경 원 다시 그리기
    drawRadiusCircle(currentLatLon);

    // 현재 위치 마커 표시
    displayCurrentLocationMarker(currentLatLon);

    // 지도 중심 이동
    map.setCenter(currentLatLon);

    // 주변 장소 검색
    searchPlaces();
}

    // 현재 위치 마커 표시
function displayCurrentLocationMarker(position) {
    // 현재 위치 마커 생성
    var content = document.createElement('div');
    content.className = 'custom-marker';
    content.style.backgroundColor = '#1D4ED8';
    content.style.width = '40px';
    content.style.height = '40px';
    content.innerHTML = '현위치';

    var customOverlay = new kakao.maps.CustomOverlay({
    position: position,
    content: content,
    yAnchor: 1
});

    customOverlay.setMap(map);
    markers.push(customOverlay);

    // 인포윈도우 표시
    var infoContent = '<div class="custom-infowindow">현재 위치</div>';
    infowindow.setContent(infoContent);
    infowindow.open(map, customOverlay);
}