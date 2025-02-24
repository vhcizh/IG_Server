function daumPostcode() {
    new daum.Postcode({
        oncomplete: function (data) {
            var addr = ''; // 주소 변수
            var extraAddr = ''; // 참고항목 변수

            // 사용자가 선택한 주소 타입에 따라 해당 주소 값을 가져온다.
            if (data.userSelectedType === 'R') {
                addr = data.roadAddress;
            } else {
                addr = data.jibunAddress;
            }

            // 도로명 주소일 경우 추가 정보 처리
            if (data.userSelectedType === 'R') {
                if (data.bname !== '' && /[동|로|가]$/g.test(data.bname)) {
                    extraAddr += data.bname;
                }
                if (data.buildingName !== '' && data.apartment === 'Y') {
                    extraAddr += (extraAddr !== '' ? ', ' + data.buildingName : data.buildingName);
                }
                if (extraAddr !== '') {
                    extraAddr = ' (' + extraAddr + ')';
                }
            } else {
                extraAddr = '';
            }

            // 우편번호와 주소 정보를 필드에 넣는다.
            document.getElementById('postcode').value = data.zonecode;
            document.getElementById('address').value = addr;
            document.getElementById("detailAddress").value= '';
            document.getElementById("detailAddress").focus();
        }
    }).open();
}

function formatPhoneNumber(input) {
    // 숫자와 하이픈만 남기고 모두 제거
    let value = input.value.replace(/[^\d-]/g, '');

    // 하이픈을 모두 제거하고 숫자만 추출
    const numbers = value.replace(/-/g, '');

    // 숫자가 11자리를 넘어가면 11자리까지만 자르기
    if (numbers.length > 11) {
        value = numbers.slice(0, 11);
    } else {
        value = numbers;
    }

    // 전화번호 형식으로 변환
    if (value.length > 7) {
        value = value.slice(0, 3) + '-' + value.slice(3, 7) + '-' + value.slice(7);
    } else if (value.length > 3) {
        value = value.slice(0, 3) + '-' + value.slice(3);
    }

    // 결과를 입력 필드에 설정
    input.value = value;
}

document.addEventListener("DOMContentLoaded", function () {
    document.getElementById("sendEmailForm").addEventListener("submit", function (event) {
        event.preventDefault();
        document.getElementById("sendEmailButton").disabled = true;
        // 폼 제출 수동 실행
        event.target.submit();
    });
});