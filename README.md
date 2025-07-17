# AI Tutor Server

**AI 튜터 백엔드 서버**


## 🛠 기술 스택

- **Framework**: Spring Boot 3.4.7
- **Language**: Java 17
- **Database**: H2 (In-Memory)
- **ORM**: Spring Data JPA
- **AI API**: Google Gemini 1.5 Flash
- **Build Tool**: Gradle

## 📋 주요 기능

### 1. 멤버십 관리
- B2C/B2B 멤버십 생성 및 관리
- 사용자별 멤버십 할당 및 추적
- 대화 횟수 제한 및 만료 처리
- 멤버십 CRUD 작업

### 2. AI 채팅 시스템
- Google Gemini API 연동
- 실시간 AI 응답 생성
- 멤버십 기반 사용량 차감
- 영어 학습 맞춤형 프롬프트

### 3. 결제 처리
- 모의 결제 시스템 구현
- 결제 검증 및 멤버십 활성화
- 결제 내역 관리

## 🏗 프로젝트 구조

```
src/main/java/com/min/ringleaitutorserver/
├── config/          # 설정 클래스
├── controller/       # REST API 컨트롤러
├── dto/             # 데이터 전송 객체
├── entity/          # JPA 엔티티
├── exception/       # 예외 처리
├── repository/      # 데이터 접근 계층
├── service/         # 비즈니스 로직
└── util/           # 유틸리티 클래스
```

## ⚙️ 설정 및 실행

### 1. 환경 설정

**필수: Google Gemini API 키 설정**

1. [Google AI Studio](https://aistudio.google.com/)에 접속
2. API 키 생성
3. `src/main/resources/application.properties` 파일에서 다음 줄 수정
   ```properties
   app.gemini.api-key=YOUR_ACTUAL_GEMINI_API_KEY
   ```

### 2. 실행 방법

```bash
# 프로젝트 클론
git clone https://github.com/Gdm0714/ai-tutor-server.git
cd ai-tutor-server

# 빌드 및 실행
./gradlew bootRun
```

### 3. 초기 데이터
- **Basic (B2C)**: 10회 대화, 30일, 10,000원
- **Premium (B2C)**: 50회 대화, 30일, 30,000원
- **Enterprise (B2B)**: 무제한 대화, 365일, 500,000원
- **Trial (B2C)**: 3회 대화, 7일, 무료

기본 사용자(ID: 1)에게 Trial 멤버십이 자동 할당됩니다.

## 📡 API 엔드포인트

### 멤버십 관리
- `GET /api/membership/user/{userId}` - 사용자 멤버십 조회
- `GET /api/membership` - 전체 멤버십 목록
- `GET /api/membership/type/{type}` - 타입별 멤버십 조회
- `POST /api/membership` - 멤버십 생성
- `DELETE /api/membership/{id}` - 멤버십 삭제
- `POST /api/membership/assign` - 사용자에게 멤버십 할당
- `POST /api/membership/payment` - 결제 처리

### AI 채팅
- `POST /api/chat/start` - AI 채팅 시작

### 데이터베이스 콘솔
- `http://localhost:8080/h2-console` - H2 데이터베이스 웹 콘솔

## 🔧 개발 도구

### H2 데이터베이스 접속
- URL: `jdbc:h2:mem:ringledb`
- 사용자명: `dongmin`
- 비밀번호: `1234`


