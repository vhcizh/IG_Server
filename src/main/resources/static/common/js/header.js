document.addEventListener("DOMContentLoaded", function() {
    const latitude = getCookie("latitude");
    const longitude = getCookie("longitude");

    if (!latitude || !longitude) {
        navigator.geolocation.getCurrentPosition(function(position) {
            const lat = position.coords.latitude;
            const lng = position.coords.longitude;

            setCookie("latitude", lat, 1); // 1일 동안 유지
            setCookie("longitude", lng, 1);

        }, function(error) {
            console.error("위치 정보를 가져올 수 없습니다.", error);
        });
    }
});

// 쿠키 설정 함수
function setCookie(name, value, days) {
    let expires = "";
    if (days) {
        const date = new Date();
        date.setTime(date.getTime() + days * 24 * 60 * 60 * 1000);
        expires = "; expires=" + date.toUTCString();
    }
    document.cookie = name + "=" + value + "; path=/" + expires;
}

// 쿠키 가져오기 함수
function getCookie(name) {
    const nameEQ = name + "=";
    const ca = document.cookie.split(';');
    for (let i = 0; i < ca.length; i++) {
        let c = ca[i];
        while (c.charAt(0) === ' ') c = c.substring(1, c.length);
        if (c.indexOf(nameEQ) === 0) return c.substring(nameEQ.length, c.length);
    }
    return null;
}