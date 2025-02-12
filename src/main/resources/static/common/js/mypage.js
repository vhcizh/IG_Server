function openModal(shelterId, visitedShelterId) {
    document.getElementById("reviewModal").style.display = "block";
    document.getElementById("modal_shelterId").value = shelterId;
    document.getElementById("modal_visitedShelterId").value = visitedShelterId; // 추가된 부분
}


function closeModal() {
    document.getElementById("reviewModal").style.display = "none";

    // 모달 내 shelterId를 초기화
    document.getElementById("modal_shelterId").value = null;

    // 선택된 별점 초기화
    const checkedRating = document.querySelector('.star-rating input:checked');
    if (checkedRating) {
        checkedRating.checked = false;
    }
    document.querySelector('.star-value').style.width = '50%';
    document.getElementById('selected-rating').textContent = '2.5';

    // 선택된 카테고리 초기화
    document.querySelectorAll('.category-item input[type="checkbox"]').forEach(checkbox => {
        checkbox.checked = false;
        checkbox.closest('.category-item').classList.remove('active');
    });
}

document.addEventListener('DOMContentLoaded', function() {
    const categoryItems = document.querySelectorAll('.category-item');
    categoryItems.forEach(categoryItem => {
        const checkbox = categoryItem.querySelector('input[type="checkbox"]');

        // category-item 클릭 이벤트
        categoryItem.addEventListener('click', function() {
            // 체크박스 상태 토글
            checkbox.checked = !checkbox.checked;

            // active 클래스 추가/제거
            if (checkbox.checked) {
                categoryItem.classList.add('active');
            } else {
                categoryItem.classList.remove('active');
            }
        });
    });

    const starRating = document.querySelector('.star-rating');
    const starValue = starRating.querySelector('.star-value');
    const ratingInputs = starRating.querySelectorAll('input');
    const selectedRating = document.getElementById('selected-rating');
    const form = document.getElementById('reviewForm');
    const categoryCheckboxes = document.querySelectorAll('input[name="categories"]');

    // 별점 마우스 이벤트 처리
    starRating.addEventListener('mousemove', function(e) {
        let width = e.pageX - this.offsetLeft;
        let percent = Math.round((width / this.offsetWidth) * 100 / 10) * 10;
        starValue.style.width = percent + '%';
        selectedRating.textContent = percent / 20;
    });

    starRating.addEventListener('click', function(e) {
        let width = e.pageX - this.offsetLeft;
        let percent = Math.round((width / this.offsetWidth) * 100 / 10) * 10;
        let value = percent / 20;

        // 최소 0.5점 보장
        if (value < 0.5) value = 0.5;

        ratingInputs.forEach(input => {
            if (parseFloat(input.value) === value) {
                input.checked = true;
            }
        });
    });

    starRating.addEventListener('mouseout', function() {
        const checkedInput = starRating.querySelector('input:checked');
        if (checkedInput) {
            const percent = parseFloat(checkedInput.value) * 20;
            starValue.style.width = percent + '%';
            selectedRating.textContent = checkedInput.value;
        } else {
            // 기본값 2.5점 설정
            const defaultInput = starRating.querySelector('input[value="2.5"]');
            defaultInput.checked = true;
            starValue.style.width = '50%';
            selectedRating.textContent = '2.5';
        }
    });

    // 폼 제출 시 유효성 검사
    form.addEventListener('submit', function(e) {
        // 별점 검사 (0.5점 미만 불가)
        const selectedStarValue = parseFloat(form.querySelector('input[name="rating"]:checked').value);
        if (selectedStarValue < 0.5) {
            e.preventDefault();
            alert('별점을 선택해주세요 (최소 0.5점)');
            return;
        }

        // 카테고리 검사 (최소 1개)
        const selectedCategories = Array.from(categoryCheckboxes).filter(checkbox => checkbox.checked);
        if (selectedCategories.length === 0) {
            e.preventDefault();
            alert('카테고리를 최소 1개 이상 선택해주세요');
            return;
        }
    });

    // 초기 별점 0.5점 설정
    const defaultInput = starRating.querySelector('input[value="2.5"]');
    defaultInput.checked = true;
    starValue.style.width = '50%';
    selectedRating.textContent = '2.5';
});

// 준비중 모달
function showModal2() {
    document.getElementById('modal').style.display = 'block'; // 모달 보이기
}

function closeModal2() {
    document.getElementById('modal').style.display = 'none'; // 모달 숨기기
}

// 모달 외부 클릭 시 닫기
window.onclick = function(event) {
    const modal = document.getElementById('modal');
    if (event.target === modal) {
        closeModal2();
    }
}