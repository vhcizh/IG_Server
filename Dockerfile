# 베이스 이미지 선택
FROM openjdk:17-jdk-slim

# 작업 디렉토리 설정
WORKDIR /app

# 빌드된 JAR 파일을 컨테이너에 복사
COPY build/libs/*.jar app.jar

# 환경 변수 설정 (SPRING_PROFILES_ACTIVE)
ENV SPRING_PROFILES_ACTIVE=prod

# 컨테이너가 실행될 때 실행할 명령어
ENTRYPOINT ["java", "-jar", "app.jar"]
