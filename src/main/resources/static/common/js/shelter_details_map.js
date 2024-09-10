function initMap(lat, lng) {
    var mapContainer = document.getElementById('map'),
        mapOption = {
            center: new kakao.maps.LatLng(lat, lng),
            level: 3
        };

    var map = new kakao.maps.Map(mapContainer, mapOption);

    // 마커 생성
    var markerPosition = new kakao.maps.LatLng(lat, lng);
    var marker = new kakao.maps.Marker({
        position: markerPosition
    });
    marker.setMap(map);
}

// 페이지가 로드될 때 호출
window.onload = function() {
    var lat = /*[[${shelter.latitude}]]*/ 37.5665; // 쉼터의 위도
    var lng = /*[[${shelter.longitude}]]*/ 126.978; // 쉼터의 경도
    initMap(lat, lng);
};