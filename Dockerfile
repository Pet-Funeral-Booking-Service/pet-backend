# base image
FROM --platform=linux/amd64 openjdk:21-jdk-slim
# 작업 디렉토리 설정
WORKDIR /app

# 빌드된 JAR 파일을 컨테이너로 복사
COPY build/libs/pet-funeral-0.0.1-SNAPSHOT.jar app.jar

# 포트 열기
EXPOSE 8080

# 환경변수를 사용하도록 설정
ENV SPRING_CONFIG_LOCATION=classpath:/application.yml

# 애플리케이션 실행
ENTRYPOINT ["java", "-jar", "app.jar"]