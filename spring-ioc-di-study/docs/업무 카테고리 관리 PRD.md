# PRD — 업무 카테고리 관리 (Spring IoC / DI 학습 예제)

## 문서 정보

| 항목 | 내용 |
| --- | --- |
| 문서명 | 업무 카테고리 관리 PRD |
| 버전 | v1.0 |
| 작성일 | 2026-09-08 |
| 상태 | 초안 (Draft) |
| 문서 유형 | 학습용 프로젝트 요구사항 정의서 |
| 기준 문서 | [업무 카테고리 관리.md](./업무%20카테고리%20관리.md) |
| 관련 산출물 | `sql/업무카테고리관리.sql`, `backend/`, `frontend/` |

> 본 PRD는 상용 서비스가 아닌 **교육용 예제 프로젝트**를 대상으로 합니다. 따라서 "제품 가치"는 사용자 수나 매출이 아니라 **학습자가 Spring IoC/DI를 이해하게 되는 것**으로 정의합니다.

---

## 목차

1. [배경 및 문제 정의](#1-배경-및-문제-정의)
2. [목표 및 비목표](#2-목표-및-비목표)
3. [성공 기준](#3-성공-기준)
4. [대상 사용자](#4-대상-사용자)
5. [범위](#5-범위)
6. [시스템 구성](#6-시스템-구성)
7. [기능 요구사항](#7-기능-요구사항)
8. [화면 요구사항](#8-화면-요구사항)
9. [API 명세](#9-api-명세)
10. [데이터 요구사항](#10-데이터-요구사항)
11. [학습 요구사항](#11-학습-요구사항)
12. [비기능 요구사항](#12-비기능-요구사항)
13. [기술 스택 및 개발 환경](#13-기술-스택-및-개발-환경)
14. [개발 마일스톤](#14-개발-마일스톤)
15. [제약사항 및 가정](#15-제약사항-및-가정)
16. [리스크](#16-리스크)
17. [향후 확장 후보](#17-향후-확장-후보)
18. [용어 정의](#18-용어-정의)

---

## 1. 배경 및 문제 정의

### 1.1 배경

Spring Framework의 핵심 개념인 IoC(제어의 역전)와 DI(의존성 주입)는 대부분 이론 설명이나 단일 클래스 예제로 학습된다. 그 결과 학습자는 다음과 같은 상태에 머무르는 경우가 많다.

- `DI = @Autowired` 라고 단편적으로 암기한다.
- IoC와 DI의 차이를 설명하지 못한다.
- 구현체가 2개 이상일 때 발생하는 문제(`@Primary` / `@Qualifier`)를 경험해 본 적이 없다.
- 인터페이스만 있고 구현 클래스가 없는 MyBatis Mapper가 어떻게 주입되는지 모른다.

### 1.2 문제 정의

> 학습자는 Spring이 객체를 생성하고 연결해 주는 지점을 **실행되는 코드 위에서** 확인할 수 있는 최소 규모의 전 구간(Full-stack) 예제가 필요하다.

### 1.3 해결 방식

"업무 카테고리 관리"라는 단일 CRUD 기능을 `React → Controller → Service → Mapper → MariaDB` 전 구간으로 구현한다. 기능 자체는 의도적으로 최소화하고, 대신 다음 구조를 학습자가 직접 수정·실험할 수 있도록 구성한다.

```text
CategoryController
        │ DI
        ▼
CategoryService (interface)
        ▲
CategoryServiceImpl
        │ DI
        ▼
CategoryMapper (interface, MyBatis Proxy)
```

---

## 2. 목표 및 비목표

### 2.1 목표 (Goals)

| ID | 목표 |
| --- | --- |
| G-1 | Spring IoC Container가 Bean을 생성하고 의존관계를 연결하는 과정을 코드로 확인할 수 있게 한다. |
| G-2 | IoC와 DI를 구분해서 설명할 수 있게 한다. |
| G-3 | Constructor Injection이 실무 기본 패턴임을 Field Injection과 비교해 체득하게 한다. |
| G-4 | 구현체가 복수일 때의 주입 충돌과 해결(`@Primary` / `@Qualifier`)을 직접 재현하게 한다. |
| G-5 | Frontend부터 DB까지 요청이 흐르는 경로를 하나의 기능으로 추적할 수 있게 한다. |

### 2.2 비목표 (Non-Goals)

| ID | 비목표 | 사유 |
| --- | --- | --- |
| NG-1 | 실서비스 수준의 카테고리 관리 기능 제공 | 기능 복잡도가 학습 목적을 가린다. |
| NG-2 | 인증 / 인가 / 권한 관리 | IoC·DI 학습과 무관하다. |
| NG-3 | 성능 최적화, 캐시, 트랜잭션 전략 설계 | 후속 학습 주제로 분리한다. |
| NG-4 | 배포 파이프라인, 컨테이너화, 운영 모니터링 | 로컬 실행만을 전제한다. |
| NG-5 | 반응형 / 다국어 / 접근성 대응 UI | 화면은 개념 확인용 최소 구성이다. |

---

## 3. 성공 기준

본 프로젝트는 다음 조건을 모두 만족할 때 완료로 판단한다.

| ID | 성공 기준 | 검증 방법 |
| --- | --- | --- |
| SC-1 | 로컬에서 Backend·Frontend·DB가 모두 기동되고 카테고리 목록 조회/등록/삭제가 동작한다. | 브라우저에서 CRUD 수동 실행 |
| SC-2 | 소스 전체에 `new CategoryServiceImpl()`, `new CategoryMapper()` 코드가 존재하지 않는다. | 소스 검색 |
| SC-3 | `ApplicationContext.getBean()`으로 `CategoryController`, `CategoryServiceImpl`, `CategoryMapper` Bean을 조회할 수 있다. | 실습 과제 LR-08 수행 |
| SC-4 | `CategoryService` 구현체를 2개로 만들었을 때 기동 실패를 재현하고, `@Primary`와 `@Qualifier`로 각각 해결한다. | 실습 과제 LR-05 ~ LR-07 수행 |
| SC-5 | 학습자가 IoC / DI / Bean / `@Autowired` / `@RequiredArgsConstructor`의 관계를 서술할 수 있다. | 기준 문서 7.5 용어 대응표 기준 자기 점검 |

---

## 4. 대상 사용자

| 구분 | 설명 | 요구사항에 주는 영향 |
| --- | --- | --- |
| 주 사용자 | Spring Boot를 처음 학습하는 백엔드 개발자 | 코드 양이 적고 흐름이 한눈에 보여야 한다. |
| 부 사용자 | Java 경험은 있으나 프레임워크 기반 DI가 낯선 개발자 | 순수 Java `new` 방식과의 대비가 명시되어야 한다. |
| 부 사용자 | Frontend 개발자로서 백엔드 레이어 구조를 이해하려는 개발자 | React → API → DB 흐름도가 필요하다. |

사용 환경은 **개인 로컬 개발 PC 단독 실행**을 전제한다. 동시 사용자, 멀티 테넌시는 고려하지 않는다.

---

## 5. 범위

### 5.1 범위 내 (In Scope)

- 업무 카테고리 **목록 조회**
- 업무 카테고리 **단건 조회** (API only)
- 업무 카테고리 **등록**
- 업무 카테고리 **삭제** (물리 삭제)
- 위 기능을 위한 Backend 4계층(Controller / Service / Mapper / Domain·DTO) 구현
- 위 기능을 위한 Frontend 화면 1개 (등록 폼 + 목록 테이블)
- DB 스키마 및 초기 데이터 스크립트
- IoC/DI 학습 실습 과제 8단계

### 5.2 범위 외 (Out of Scope)

기준 문서에 정의되지 않았으므로 본 버전에서는 구현하지 않는다.

| 항목 | 비고 |
| --- | --- |
| 카테고리 **수정(UPDATE)** | Mapper에 `update` 메서드 없음. → [17. 향후 확장 후보](#17-향후-확장-후보) |
| `use_yn` 사용여부 **토글 기능** | 컬럼은 존재하나 항상 `'Y'`로 고정 저장된다. |
| 논리 삭제(soft delete) | 현재 `DELETE` 물리 삭제만 정의됨. |
| 페이징 / 검색 / 정렬 옵션 | 목록은 `category_id DESC` 전체 조회로 고정. |
| 서버 측 입력 검증(`@Valid`) 및 표준 에러 응답 규격 | 현재 검증은 클라이언트 `trim()` 체크뿐. |
| 트랜잭션 처리(`@Transactional`) | 단일 DML만 존재하여 미적용. |
| 단위 / 통합 테스트 코드 | 검증은 수동 실행 및 실습 과제로 대체. |
| 인증·인가, 사용자 관리 | NG-2 |
| 배포 및 운영 | NG-4 |

---

## 6. 시스템 구성

### 6.1 구성 요소

| 구성 요소 | 실행 위치 | 포트 | 역할 |
| --- | --- | --- | --- |
| Frontend (Vite Dev Server) | 로컬 | 5173 | 화면 렌더링, 상태 관리, API 호출 |
| Backend (Spring Boot) | 로컬 | 8080 | REST API 제공, IoC Container 구동 |
| Database (MariaDB) | 로컬 | 3306 | `ioc_di_study` 스키마 |

### 6.2 요청 흐름

```text
사용자
 │ 카테고리 등록
 ▼
CategoryForm.tsx → CategoryStore → categoryApi
 │ POST /api/categories
 ▼
CategoryController → CategoryServiceImpl → CategoryMapper
 │
 ▼
CategoryMapper.xml → MariaDB
```

### 6.3 디렉터리 구조 요구사항

Backend 패키지는 계층별로 분리한다. IoC/DI 관찰이 목적이므로 **Service는 반드시 interface와 구현체를 분리**한다.

```text
backend/src/main/java/com/example/iocdi/
├─ IocDiApplication.java
├─ controller/CategoryController.java
├─ service/CategoryService.java
├─ service/CategoryServiceImpl.java
├─ mapper/CategoryMapper.java
├─ domain/Category.java
└─ dto/CategoryRequest.java
```

---

## 7. 기능 요구사항

### 7.1 요구사항 목록

| ID | 기능 | 우선순위 | 관련 API |
| --- | --- | --- | --- |
| FR-01 | 카테고리 목록 조회 | Must | `GET /api/categories` |
| FR-02 | 카테고리 단건 조회 | Should | `GET /api/categories/{categoryId}` |
| FR-03 | 카테고리 등록 | Must | `POST /api/categories` |
| FR-04 | 카테고리 삭제 | Must | `DELETE /api/categories/{categoryId}` |
| FR-05 | 등록 시 카테고리명 필수 입력 검증 (클라이언트) | Must | — |
| FR-06 | 조회 실패 시 오류 메시지 표시 | Must | — |
| FR-07 | 조회 중 로딩 상태 표시 | Should | — |
| FR-08 | 등록·삭제 후 목록 자동 갱신 | Must | — |
| FR-09 | Frontend → Backend 크로스 오리진 허용 | Must | — |

### 7.2 상세 요구사항

#### FR-01. 카테고리 목록 조회

- 화면 진입 시 카테고리 전체 목록을 자동 조회한다.
- 정렬은 `category_id DESC` (최근 등록 순) 고정이다.
- 응답 항목: `categoryId`, `categoryName`, `description`, `useYn`, `createdAt`
- **수락 기준**
  - 화면 최초 렌더링 시 조회 API가 1회 호출된다.
  - 초기 데이터 3건(SPRING / REACT / DATABASE)이 역순으로 표시된다.
  - 목록이 비어 있으면 테이블 본문에 행이 없다.

#### FR-02. 카테고리 단건 조회

- `categoryId`로 카테고리 1건을 조회한다.
- 본 버전에서는 **API만 제공**하고 화면에서 사용하지 않는다. (계층 통과 흐름 및 `parameterType` 학습 목적)
- **수락 기준**
  - 존재하는 ID 요청 시 해당 카테고리 1건이 반환된다.
  - 존재하지 않는 ID 요청 시 본문이 비어 있다. (에러 응답 규격은 범위 외)

#### FR-03. 카테고리 등록

- 입력 항목: 카테고리명(필수), 설명(선택)
- 서버는 `use_yn = 'Y'`, `created_at = NOW()`를 자동 설정한다.
- 응답 본문은 없다. (`void`)
- **수락 기준**
  - 등록 성공 후 입력 필드가 초기화된다.
  - 등록 성공 후 목록이 재조회되어 신규 항목이 최상단에 표시된다.
  - `description`을 비워도 등록이 성공한다.

#### FR-04. 카테고리 삭제

- 목록 각 행의 "삭제" 버튼으로 해당 카테고리를 삭제한다.
- 물리 삭제(`DELETE`)이며 확인(confirm) 절차는 없다.
- **수락 기준**
  - 삭제 후 목록이 재조회되어 해당 행이 사라진다.

#### FR-05. 등록 시 카테고리명 필수 입력 검증

- 카테고리명이 공백만 입력된 경우 API를 호출하지 않는다.
- `alert("카테고리명을 입력하세요.")`로 사용자에게 알린다.
- **수락 기준**
  - 빈 값 또는 공백 문자열 제출 시 네트워크 요청이 발생하지 않는다.

#### FR-06. 오류 메시지 표시

- 목록 조회 중 예외 발생 시 `"카테고리를 조회하는 중 오류가 발생했습니다."`를 화면에 표시한다.
- **수락 기준**
  - Backend를 중단한 상태로 화면을 새로고침하면 오류 메시지가 표시된다.

#### FR-07. 로딩 상태 표시

- 목록 조회 중에는 `Loading...`을 표시하고, 완료 시 제거한다.

#### FR-08. 등록·삭제 후 목록 자동 갱신

- 등록/삭제 성공 후 별도 조작 없이 목록을 재조회한다.

#### FR-09. CORS 허용

- Backend는 `http://localhost:5173` 오리진의 요청을 허용한다.
- **수락 기준**
  - 브라우저 콘솔에 CORS 오류가 발생하지 않는다.

---

## 8. 화면 요구사항

### 8.1 화면 목록

| ID | 화면 | 경로 | 구성 |
| --- | --- | --- | --- |
| UI-01 | 카테고리 관리 (단일 화면) | `/` | 헤더 + 등록 폼 + 목록 테이블 |

### 8.2 UI-01 카테고리 관리

**레이아웃**

```text
┌──────────────────────────────────────────┐
│ Spring IoC / DI 학습                     │
│ React + MobX + Spring Boot               │
├──────────────────────────────────────────┤
│ [카테고리 등록]                          │
│  카테고리명 : [____________________]     │
│  설명       : [____________________]     │
│                          ( 등록 )        │
├──────────────────────────────────────────┤
│ [카테고리 목록]                          │
│  ID │ 카테고리 │ 설명 │ 사용 │ 관리      │
│  ───┼──────────┼──────┼──────┼──────     │
│   3 │ DATABASE │ ...  │  Y   │ 삭제      │
└──────────────────────────────────────────┘
```

**구성 요소 요구사항**

| 영역 | 요구사항 |
| --- | --- |
| 헤더 | 제목 `Spring IoC / DI 학습`, 부제 `React + MobX + Spring Boot` |
| 등록 폼 | 카테고리명·설명 텍스트 입력, 등록 버튼(submit) |
| 목록 헤더 | `ID`, `카테고리`, `설명`, `사용`, `관리` 5개 컬럼 |
| 목록 행 | 각 행 우측에 삭제 버튼 (텍스트 링크형) |
| 상태 표시 | 로딩 시 `Loading...`, 오류 시 오류 메시지(빨강) |
| 스타일 | Tailwind CSS 유틸리티 클래스 사용, 카드형 컨테이너 |

**상태 관리 요구사항**

- MobX Store(`CategoryStore`)가 `categories`, `loading`, `errorMessage` 3개 상태를 보유한다.
- 컴포넌트는 `observer`로 감싸 상태 변화에 반응한다.
- 비동기 결과 반영은 `runInAction` 내부에서 수행한다.

---

## 9. API 명세

**Base URL**: `http://localhost:8080/api`
**공통 요청 헤더**: `Content-Type: application/json`

### 9.1 엔드포인트 요약

| Method | Path | 설명 | 요청 본문 | 응답 본문 |
| --- | --- | --- | --- | --- |
| GET | `/categories` | 목록 조회 | — | `Category[]` |
| GET | `/categories/{categoryId}` | 단건 조회 | — | `Category` |
| POST | `/categories` | 등록 | `CategoryRequest` | 없음 |
| DELETE | `/categories/{categoryId}` | 삭제 | — | 없음 |

### 9.2 응답 모델 — `Category`

| 필드 | 타입 | 설명 |
| --- | --- | --- |
| `categoryId` | number | 카테고리 ID |
| `categoryName` | string | 카테고리명 |
| `description` | string | 설명 |
| `useYn` | string | 사용여부 (`Y` / `N`) |
| `createdAt` | string | 등록일시 |

```json
[
  {
    "categoryId": 3,
    "categoryName": "DATABASE",
    "description": "Database 학습",
    "useYn": "Y",
    "createdAt": "2026-09-08T21:20:00"
  }
]
```

### 9.3 요청 모델 — `CategoryRequest`

| 필드 | 타입 | 필수 | 설명 |
| --- | --- | --- | --- |
| `categoryName` | string | Y | 카테고리명 (최대 100자) |
| `description` | string | N | 설명 (최대 500자) |

```json
{
  "categoryName": "SPRING",
  "description": "Spring Framework 학습"
}
```

### 9.4 규약

- 컬럼명(snake_case) ↔ 필드명(camelCase) 변환은 MyBatis `map-underscore-to-camel-case: true`로 처리한다.
- 등록/삭제 응답은 본문 없이 HTTP 200을 반환한다.
- 표준 에러 응답 규격은 본 버전 범위 외이다. ([5.2 참고](#52-범위-외-out-of-scope))

---

## 10. 데이터 요구사항

### 10.1 스키마

- 데이터베이스: `ioc_di_study`
- 문자셋 / 정렬: `utf8mb4` / `utf8mb4_unicode_ci`
- 테이블: `tb_category`

### 10.2 `tb_category` 컬럼 정의

| 컬럼 | 타입 | Null | 기본값 | 설명 |
| --- | --- | --- | --- | --- |
| `category_id` | BIGINT | N | AUTO_INCREMENT | PK |
| `category_name` | VARCHAR(100) | N | — | 카테고리명 |
| `description` | VARCHAR(500) | Y | — | 설명 |
| `use_yn` | CHAR(1) | N | `'Y'` | 사용여부 |
| `created_at` | DATETIME | N | `CURRENT_TIMESTAMP` | 등록일시 |

- PK: `category_id`
- 인덱스 추가 요구사항 없음 (데이터량 미미)

### 10.3 초기 데이터

| category_name | description | use_yn |
| --- | --- | --- |
| SPRING | Spring Framework 학습 | Y |
| REACT | React 학습 | Y |
| DATABASE | Database 학습 | Y |

### 10.4 스크립트 요구사항

- 스크립트 위치: `sql/업무카테고리관리.sql`
- 반복 실행 가능해야 한다. (`CREATE DATABASE IF NOT EXISTS`, `DROP TABLE IF EXISTS` 사용 → 실행 시 기존 데이터는 초기화된다.)

---

## 11. 학습 요구사항

본 프로젝트의 **핵심 산출물**은 동작하는 CRUD가 아니라 아래 실습의 완주다.

### 11.1 실습 과제

| ID | 과제 | 확인 포인트 |
| --- | --- | --- |
| LR-01 | `Controller → Service → Mapper` 구조로 정상 실행 | 전 구간 호출 성공 |
| LR-02 | `@RequiredArgsConstructor` 제거 후 생성자 직접 작성 | 생성자 1개면 `@Autowired` 불필요 |
| LR-03 | Field Injection(`@Autowired`) 방식으로 변경 | 동작하지만 `final` 불가 |
| LR-04 | 다시 Constructor Injection으로 복원 | 불변성·테스트 용이성 비교 |
| LR-05 | `CategoryService` 구현체 2개 생성 후 오류 확인 | 주입 대상 모호로 기동 실패 재현 |
| LR-06 | `@Primary`로 해결 | 기본 Bean 지정 |
| LR-07 | `@Qualifier`로 해결 | Bean 이름 기반 지정 |
| LR-08 | `ApplicationContext.getBean()`으로 Bean 직접 조회 | Container가 실제로 객체를 보유함을 확인 |

> LR-05 ~ LR-08을 완료하는 것이 학습 목표 달성의 핵심 조건이다. ([SC-3, SC-4](#3-성공-기준))

### 11.2 설계상 의도된 제약

학습 효과를 위해 **일반적인 최소 구현보다 일부러 복잡하게** 유지하는 항목이다. 리팩터링 대상으로 오인하지 않도록 명시한다.

| 항목 | 의도 |
| --- | --- |
| Service를 interface + 구현체로 분리 | DIP 및 `@Primary` / `@Qualifier` 실습 전제 |
| Mapper 구현 클래스를 만들지 않음 | MyBatis Proxy Bean 등록 과정을 관찰 |
| Controller가 `CategoryServiceImpl`이 아닌 `CategoryService`에 의존 | 구현체 교체 가능성 확인 |
| `getCategory` API를 화면에서 미사용 | 계층 통과 흐름만 별도 확인 |

---

## 12. 비기능 요구사항

| ID | 구분 | 요구사항 |
| --- | --- | --- |
| NFR-01 | 실행 환경 | 로컬 단독 실행. Backend 8080, Frontend 5173, MariaDB 3306 |
| NFR-02 | 가독성 | 계층별 패키지 분리, 클래스 1개당 단일 책임 유지 |
| NFR-03 | 관측성 | `com.example.iocdi.mapper` 패키지 로그 레벨 `debug` → 실행 SQL 확인 가능 |
| NFR-04 | 이식성 | DB 접속 정보(`username` / `password`)는 `application.yml`에서 변경 가능 |
| NFR-05 | 문서화 | 기준 문서에 전체 코드와 개념도가 포함되어야 한다 |
| NFR-06 | 재현성 | SQL 스크립트 재실행으로 동일 초기 상태 복원 가능 |
| NFR-07 | 성능 | 별도 목표 없음. 데이터 수십 건 규모 전제 |
| NFR-08 | 보안 | 별도 요구 없음. 로컬 학습 용도이므로 계정 정보 평문 설정 허용 |

---

## 13. 기술 스택 및 개발 환경

| 영역 | 기술 | 버전/비고 |
| --- | --- | --- |
| 언어 (BE) | Java | 17 |
| 프레임워크 | Spring Boot | 3.5.5 (`spring-boot-starter-web`) |
| 영속성 | MyBatis | `mybatis-spring-boot-starter` 3.0.4 |
| 코드 생성 | Lombok | `optional` 의존성 |
| DB | MariaDB | `mariadb-java-client` (runtime) |
| 빌드 (BE) | Maven | `spring-boot-maven-plugin` |
| 언어 (FE) | TypeScript | Vite `react-ts` 템플릿 |
| UI | React | — |
| 상태 관리 | MobX | `mobx`, `mobx-react-lite` |
| HTTP | Axios | — |
| 스타일 | Tailwind CSS | 프로젝트 Tailwind 버전에 맞춰 구성 |
| 빌드 (FE) | Vite | `npm create vite@latest frontend -- --template react-ts` |

**MyBatis 설정 요구사항**

- `mapper-locations`: `classpath:mapper/**/*.xml`
- `type-aliases-package`: `com.example.iocdi.domain`
- `map-underscore-to-camel-case`: `true`

---

## 14. 개발 마일스톤

| 단계 | 산출물 | 완료 조건 |
| --- | --- | --- |
| M1. DB 준비 | `sql/업무카테고리관리.sql` | 스키마·테이블 생성 및 초기 데이터 3건 적재 |
| M2. Backend 골격 | `pom.xml`, `application.yml`, `IocDiApplication` | 애플리케이션 정상 기동 |
| M3. 영속 계층 | `Category`, `CategoryRequest`, `CategoryMapper`, `CategoryMapper.xml` | Mapper 단위 조회 성공 |
| M4. 서비스·API 계층 | `CategoryService`, `CategoryServiceImpl`, `CategoryController` | 4개 API 응답 확인 |
| M5. Frontend | `types`, `api`, `stores`, `CategoryForm`, `App`, `main` | 화면에서 조회·등록·삭제 동작 |
| M6. 통합 확인 | — | [SC-1, SC-2](#3-성공-기준) 충족 |
| M7. IoC/DI 실습 | LR-01 ~ LR-08 수행 기록 | [SC-3 ~ SC-5](#3-성공-기준) 충족 |

---

## 15. 제약사항 및 가정

### 15.1 제약사항

- 로컬 개발 환경에서만 실행한다. 외부 접근을 고려하지 않는다.
- MariaDB가 `localhost:3306`에 기동되어 있어야 한다.
- SQL 스크립트는 `DROP TABLE`을 포함하므로 실행 시 기존 데이터가 삭제된다.
- Frontend 오리진이 `http://localhost:5173`으로 고정되어 있다. 포트가 변경되면 `@CrossOrigin` 설정도 함께 수정해야 한다.

### 15.2 현재 리포지터리 상태와의 차이

기준 문서는 목표 구조를 기술한 가이드이며, 작성일 기준 실제 `backend/` 디렉터리와 다음 차이가 있다. **M2 단계에서 문서 기준으로 정렬한다.**

| 항목 | 문서(목표) | 현재 리포지터리 |
| --- | --- | --- |
| 패키지 | `com.example.iocdi` | `com.example` |
| 시작 클래스 | `IocDiApplication` | `BackendApplication` |
| 설정 파일 | `application.yml` | `application.properties` |
| 계층 패키지 | controller / service / mapper / domain / dto | 없음 |
| Frontend | `frontend/` | 미생성 |

### 15.3 가정

- 학습자는 Java 기본 문법과 SQL 기초를 알고 있다.
- 학습자 PC에 JDK 17, Node.js, MariaDB가 설치되어 있다.
- 데이터 정합성·동시성 이슈는 발생하지 않는 수준(단일 사용자)으로 가정한다.

---

## 16. 리스크

| ID | 리스크 | 영향 | 대응 |
| --- | --- | --- | --- |
| R-1 | 기능 요구가 늘어나 IoC/DI 학습 초점이 흐려진다. | 학습 목표 미달 | [5.2 범위 외](#52-범위-외-out-of-scope)를 기준으로 신규 요구 차단 |
| R-2 | DB 접속 정보 불일치로 기동 실패한다. | M2 지연 | `application.yml` 계정 변경 안내를 문서에 명시 (NFR-04) |
| R-3 | CORS 설정 누락으로 Frontend 연동이 막힌다. | M5 지연 | FR-09를 수락 기준으로 검증 |
| R-4 | LR-05 실습 시 기동 실패를 "버그"로 오인한다. | 학습 혼선 | 의도된 실패임을 [11.2](#112-설계상-의도된-제약)에 명시 |
| R-5 | 문서 코드와 실제 코드가 어긋난다. | 학습자 혼란 | [15.2](#152-현재-리포지터리-상태와의-차이) 정렬 작업을 M2에 포함 |
| R-6 | Tailwind 버전 차이로 스타일이 적용되지 않는다. | 화면 품질 저하 | 버전에 맞춘 설정을 각자 구성 (기능 검증에는 영향 없음) |

---

## 17. 향후 확장 후보

현재 범위 외이지만, 학습 심화 단계에서 다음 순서로 추가하는 것을 권장한다.

| 순위 | 항목 | 추가 학습 주제 |
| --- | --- | --- |
| 1 | 카테고리 수정(UPDATE) 기능 | `<update>` 매핑, `@PutMapping` |
| 2 | 서버 측 검증 및 표준 에러 응답 | `@Valid`, `@RestControllerAdvice` |
| 3 | `use_yn` 토글 / 논리 삭제 전환 | 상태 컬럼 기반 조회 조건 |
| 4 | `@Transactional` 적용 | 트랜잭션 경계와 프록시 |
| 5 | 단위·통합 테스트 작성 | `@SpringBootTest`, Mock 주입 — Constructor Injection의 이점 체감 |
| 6 | Bean Scope(`singleton` / `prototype`) 실습 | Bean 생명주기 |
| 7 | 페이징 / 검색 | 동적 SQL(`<if>`, `<where>`) |

---

## 18. 용어 정의

| 용어 | 정의 |
| --- | --- |
| IoC (Inversion of Control) | 객체의 생성과 관리 책임을 개발자 코드가 아니라 `ApplicationContext`가 갖는 것. "제어의 역전". |
| DI (Dependency Injection) | IoC Container가 관리하는 객체들을 필요한 다른 객체에 연결해 주는 것. "의존성 주입". |
| Bean | Spring IoC Container가 생성하고 관리하는 객체. |
| ApplicationContext | Spring의 IoC Container 구현체. Bean 생성·주입·생명주기를 관리한다. |
| Constructor Injection | 생성자 파라미터를 통한 의존성 주입 방식. 실무 권장 패턴. |
| Field Injection | `@Autowired`를 필드에 선언하는 주입 방식. `final` 사용 불가, 테스트 불편. |
| Proxy Bean | MyBatis가 Mapper 인터페이스로부터 런타임에 생성해 Container에 등록하는 대리 객체. |
| DIP | 의존관계 역전 원칙. 구현이 아닌 추상(인터페이스)에 의존해야 한다는 원칙. |
| `@Primary` | 동일 타입 Bean이 복수일 때 기본으로 주입할 Bean을 지정하는 애노테이션. |
| `@Qualifier` | 주입할 Bean을 이름으로 명시적으로 지정하는 애노테이션. |
