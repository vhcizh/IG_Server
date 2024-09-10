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

// 페이지 로드 시 시 데이터와 쉼터유형 데이터를 불러옵니다.
window.onload = function() {
    loadCities();
    loadFacilityTypes();
};

// 체크박스 버튼 클릭 시 색상 변화
document.querySelectorAll('.checkbox-btn').forEach(function(button) {
    button.addEventListener('click', function() {
        this.classList.toggle('active');
    });
});