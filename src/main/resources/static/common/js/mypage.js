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
    document.querySelector('.star-value').style.width = '0';
    document.getElementById('selected-rating').textContent = '0';

    // 선택된 카테고리 초기화
    document.querySelectorAll('.category-item input[type="checkbox"]').forEach(checkbox => {
        checkbox.checked = false;
        checkbox.closest('.category-item').classList.remove('active');
    });
}

window.onclick = function(event) {
    const modal = document.getElementById("reviewModal");
    if (event.target === modal) {
        modal.style.display = "none";
    }
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
});



document.addEventListener('DOMContentLoaded', function() {
    const starRating = document.querySelector('.star-rating');
    const starValue = starRating.querySelector('.star-value');
    const ratingInputs = starRating.querySelectorAll('input');
    const selectedRating = document.getElementById('selected-rating');

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
            starValue.style.width = '0';
            selectedRating.textContent = '0';
        }
    });
});