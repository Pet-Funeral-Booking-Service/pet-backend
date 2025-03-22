## 반려묘 장례식장 검색 서비스
---


### 📝 프로젝트 소개

----

```
반려묘의 합법적인 장례식장을 쉽고 빠르게 찾을 수 있도록 도와주는 서비스입니다.  
사용자는 지역별 합법 장례식장의 가격을 비교하여, 합리적인 비용으로 반려묘와 마지막 순간을 함께할 수 있습니다.
```

### ⚒️ 기술 스택

----

**Backend** : `Spring Boot`, `JPA`, `QueryDSL`  
**Database** : `MySQL`, `Redis`    
**Devops** : `Docker`, `Jenkis`, `Aws Ec2`, `Aws RDS`, `Aws S3`

### ⚒️ 프로젝트 실행 방법

----

1. git clone
```
https://github.com/Pet-Funeral-Booking-Service/pet-backend.git
```

2. .env 파일 생성 && 편집  
.env 파일은 1번에서 클론한 프로젝트 루트디렉토리에 위치해야 한다.
```
//env 파일 생성 명령어
touch .env 
```

```
//env 파일 편집 명령어
nano .env
```

env 파일의 값을 .env.example 를 참고해 설정한다.
```
DB_URL=jdbc:mysql://db:3306/pet_service?serverTimezone=Asia/Seoul
DB_USERNAME=MYSQL이름
DB_PASSWORD=MYSQL비번
...

```
3.docker-compose 실행 (서버 실행)

```
docker-compose up
```

4.docker-compose 종료 (서버 종료)
```
docker-compose down -v 
```

⚠️ 선행 조건
1. .env 파일을 클론받은 프로젝트와 동일한 위치에 존재해야함
2. docker 설치 및 로그인 