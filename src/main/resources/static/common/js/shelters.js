// 구 데이터를 가져오는 함수
let selectedGu = document.getElementById('selectedGu').value; // 기존에 선택된 구 값 저장
let firstLoad = true;
function loadGus() {
    const selectedCity = document.getElementById('city').value; // 현재 선택된 시 값 저장
    // 시가 선택되었다면 구 데이터 로드
    if(selectedCity) {
        fetch(`/common/shelters/gus?city=${selectedCity}`) // 구 정보를 가져오는 API
            .then(response => response.json())
            .then(data => {
                const guSelect = document.getElementById('gu');
                guSelect.innerHTML = '<option value="">시/군/구 선택</option>'; // 초기화
                data.forEach(gu => {
                    const option = document.createElement('option');
                    option.value = gu; // 구 ID
                    option.textContent = gu; // 구 이름

                    // 기존 선택값과 같으면 selected 설정
                    if (firstLoad === true && gu === selectedGu) {
                        option.selected = true;
                        firstLoad = false; // 처음 로드했을때만 기존 선택된 값을 자동으로 선택되도록 하고 그 외에는 리스트만 불러오고 선택되지 않도록 한다.
                    }

                    guSelect.appendChild(option);
                });
            });
    } else {
        // '시군구선택' 버튼 선택시 초기화
        const guSelect = document.getElementById('gu');
        guSelect.innerHTML = '<option value="">시/군/구 선택</option>'; // 초기화
        selectedGu = null; // 초기화
    }
}

// 시설유형 데이터 불러오기
function loadFacilityTypes() {
    const selectedType = document.getElementById('facilityType').value; // 현재 선택된 시설유형 값 저장

    fetch('/common/shelters/facilities')
        .then(response => response.json())
        .then(data => {
            const facilityTypeSelect = document.getElementById('facilityType');
            facilityTypeSelect.innerHTML = '<option value="">시설 유형 선택</option>'; // 초기화
            data.forEach(type => {
                const option = document.createElement('option');
                option.value = type; // 시 ID
                option.textContent = type; // 시 이름
                if(type === selectedType) {  // 기존 선택값과 같으면 selected 설정
                    option.selected = true;
                }
                facilityTypeSelect.appendChild(option);
            });
        });
}

// 체크박스 버튼 클릭 시 색상 변화
function checkboxColorChange() {
    document.querySelectorAll('.checkbox-btn')
        .forEach(function (label) {
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
}

// 상세 페이지 데이터 로드
function addClickEventEachShelterList() {
    document.querySelectorAll(".shelter").forEach(link => {
        link.addEventListener("click", function () {
            const shelterId = this.getAttribute("data-shelter-id");
            const latitude = this.getAttribute("data-shelter-lat");
            const longitude = this.getAttribute("data-shelter-lon");
            openPanelAndLoadShelterDetails(shelterId, latitude, longitude);
        });
    });
}

// 사이드 패널
function openPanelAndLoadShelterDetails(shelterId, latitude, longitude) {
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

// 페이지 로드 완료 후 실행
document.addEventListener('DOMContentLoaded', function () {
    loadGus();
    loadFacilityTypes();
    checkboxColorChange();
    addClickEventEachShelterList();
});