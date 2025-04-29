# 1. Java 17 기반 이미지 사용
FROM openjdk:21-jdk-slim

# 2. 빌드된 jar 파일을 컨테이너 안에 복사
COPY build/libs/*.jar app.jar

# 3. 8080 포트 열기
EXPOSE 8080

# 4. 앱 실행 명령
ENTRYPOINT ["java", "-jar", "app.jar"]
