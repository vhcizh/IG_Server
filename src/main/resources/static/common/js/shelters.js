// 시 데이터를 가져오는 함수
function loadCities() {
    fetch('/common/shelters/cities') // 시 정보를 가져오는 API
        .then(response => response.json())
        .then(data => {
            const citySelect = document.getElementById('city');
            data.forEach(city => {
                const option = document.createElement('option');
                option.value = city; // 시 ID
                option.textContent = city; // 시 이름
                citySelect.appendChild(option);
            });
        });
}

// 구 데이터를 가져오는 함수
function loadGus() {
    const city = document.getElementById('city').value;
    fetch(`/common/shelters/gus?city=${city}`) // 구 정보를 가져오는 API
        .then(response => response.json())
        .then(data => {
            const guSelect = document.getElementById('gu');
            guSelect.innerHTML = '<option value="">구 선택</option>'; // 초기화
            data.forEach(gu => {
                const option = document.createElement('option');
                option.value = gu; // 구 ID
                option.textContent = gu; // 구 이름
                guSelect.appendChild(option);
            });
        });
}

// 동 데이터를 가져오는 함수
// function loadDongs() {
//     const city = document.getElementById('city').value;
//     const gu = document.getElementById('gu').value;
//     fetch(`/common/shelters/dongs?city=${city}&gu=${gu}`) // 동 정보를 가져오는 API
//         .then(response => response.json())
//         .then(data => {
//             const dongSelect = document.getElementById('dong');
//             dongSelect.innerHTML = '<option value="">동 선택</option>'; // 초기화
//             data.forEach(dong => {
//                 const option = document.createElement('option');
//                 option.value = dong; // 동 ID
//                 option.textContent = dong; // 동 이름
//                 dongSelect.appendChild(option);
//             });
//         });
// }

// 시설유형 데이터 불러오기
function loadFacilityTypes() {
    fetch('/common/shelters/facilities')
        .then(response => response.json())
        .then(data => {
            const facilityTypeSelect = document.getElementById('facilityType');
            data.forEach(type => {
                const option = document.createElement('option');
                option.value = type; // 시 ID
                option.textContent = type; // 시 이름
                facilityTypeSelect.appendChild(option);
            });
        });
}

// 페이지 로드 시 시 데이터와 쉼터유형 데이터를 불러오기
window.onload = function () {
    loadCities();
    loadFacilityTypes();
};

// 체크박스 버튼 클릭 시 색상 변화
document.addEventListener('DOMContentLoaded', function () {
    document.querySelectorAll('.checkbox-btn').forEach(function (label) {
        const checkbox = label.querySelector('input[type="checkbox"]');

        // 초기 상태 설정
        if (checkbox.checked) {
            label.classList.add('active');
        }

        // 라벨 클릭 이벤트 (체크박스 클릭 제외)
        label.addEventListener('click', function (e) {
            checkbox.checked = !checkbox.checked;
            label.classList.toggle('active');
        });
    });
});

// 상세 페이지 데이터 로드
document.addEventListener("DOMContentLoaded", function () {
    document.querySelectorAll(".shelter").forEach(link => {
        link.addEventListener("click", function () {
            const shelterId = this.getAttribute("data-shelter-id");
            const latitude = this.getAttribute("data-shelter-lat");
            const longitude = this.getAttribute("data-shelter-lon");
            loadShelterDetails(shelterId, latitude, longitude);
        });
    });
});

// 사이드 패널
function loadShelterDetails(shelterId, latitude, longitude) {
    fetch(`/common/shelters/${shelterId}`)
        .then(response => response.text())  // HTML 전체 받아오기
        .then(html => {
            document.getElementById("shelterDetails").innerHTML = html;
            // 카카오 지도 초기화 (HTML 삽입 후 실행)
            initKakaoMap(latitude, longitude);
            document.getElementById("sidePanel").style.right = "0"; // 패널 열기
        })
        .catch(error => console.error('Error:', error));
}

function closeSidePanel() {
    document.getElementById("sidePanel").style.right = "-40%"; // 패널 닫기
}

function initKakaoMap(lat, lng) {
    const mapContainer = document.getElementById('map');
    const position = new kakao.maps.LatLng(lat, lng);
    const mapOption = {
        center: position,
        level: 1
    };
    const map = new kakao.maps.Map(mapContainer, mapOption);

    // 마커 생성
    const marker = new kakao.maps.Marker({
        position: position
    });
    marker.setMap(map);
}