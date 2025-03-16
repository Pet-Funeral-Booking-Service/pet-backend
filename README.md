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

### ⚒️ 프로젝트 로컬 실행 방법

----

1. git clone
```
https://github.com/Pet-Funeral-Booking-Service/pet-backend.git
```

2. .env 파일 생성 && 편집
```
//env 파일 생성 명령어
touch .env 
```

```
//env 파일 편집 명령어
nano .env
```

env 파일의 값을 .env.example 를 참고해 설정한다. DB 는 MYSQL 은 8.3.0 를 사용한다.
```
DB_URL=jdbc:mysql://localhost:3306/{DB 스키가 이름}?serverTimezone=Asia/Seoul
DB_USERNAME=root
DB_PASSWORD=비밀번호
```

3.docker-compose 실행

```
docker-compose up -d 
```