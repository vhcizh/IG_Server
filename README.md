# 쉼터로 (Shimturo)
> 위치 기반 쉼터 정보 제공 플랫폼
- 본 저장소는 개인 개선 중심 브랜치입니다.
- 📎 배포 링크: https://shimturo.site

## 📌 프로젝트 소개
> 쉼터로는 전국의 쉼터를 조건별로 검색하거나 현재 위치 기반으로 주변 쉼터를 조회하는 서비스입니다.
>
전국 곳곳에 무더위 쉼터가 개방되어 있지만, 어디에 있는지 몰라 이용하지 못하는 경우가 많습니다.
그래서 쉼터로는 내 주변 쉼터를 쉽게 찾고 이용할 수 있도록 내 위치 정보를 기반으로 주변 쉼터를 찾을 수 있는 지도를 제공합니다.
뿐만 아니라 쉼터 유형, 지역, 휴일 및 야간 개방이나 숙박 가능 여부 등 다양한 조건을 선택하여 전국의 쉼터를 손쉽게 찾을 수 있습니다.

## 🔧 사용 기술
- **Backend**: Java, Spring Boot, Spring Security, Spring Data JPA, Querydsl, MySQL  
- **Frontend**: Thymeleaf, JavaScript, CSS  
- **Infra / DevOps**: AWS EC2, RDS, Docker, Nginx, Git, GitHub, GitHub Actions

## ✨ 주요 기능
- 내 주변 쉼터 조회 (SSR 환경에서 쿠키 활용 UX 개선)
- 조건별 쉼터 리스트 검색 (지역/휴일개방 등, 거리순 정렬)
- 관리자용 CSV 파일 업로드 기능
- 쉼터 리뷰 작성, 사용자 인증/인가
- 관리자용 쉼터 설치 추천 지도

## 🧠 핵심 개선 경험
- CSV 데이터 삽입 구조를 JDBC Batch로 개선 (43분 → 5.5초)
- Spring Security 구조 개선 (멀티 HttpSecurity 체인 분리)
- 인증번호 관리 캐시(Map → Caffeine) 개선
- JPA 순환참조 구조 개선
- 테이블 설계 비정규 구조 → 정규화 및 관계 재설계
