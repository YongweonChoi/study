아래는 **Postman에서 Category CRUD 테스트**할 때 사용할 수 있는 요청 예시입니다.

기본 URL은 현재 컨트롤러 기준으로 다음과 같습니다.

```plain text
http://localhost:8080/api/categories
```


---

## 1. 전체 카테고리 조회 - READ List

### Method

```plain text
GET
```


### URL

```plain text
http://localhost:8080/api/categories
```


### Body

없음

### 예상 응답 예시

```json
[
  {
    "categoryId": 1,
    "categoryName": "업무",
    "description": "업무 관련 카테고리",
    "useYn": "Y",
    "createdAt": "2026-09-09T10:30:00"
  },
  {
    "categoryId": 2,
    "categoryName": "개인",
    "description": "개인 일정 카테고리",
    "useYn": "Y",
    "createdAt": "2026-09-09T11:00:00"
  }
]
```


---

## 2. 단일 카테고리 조회 - READ Detail

### Method

```plain text
GET
```


### URL

```plain text
http://localhost:8080/api/categories/1
```


`1` 자리에 조회할 `categoryId`를 넣으면 됩니다.

### Body

없음

### 예상 응답 예시

```json
{
  "categoryId": 1,
  "categoryName": "업무",
  "description": "업무 관련 카테고리",
  "useYn": "Y",
  "createdAt": "2026-09-09T10:30:00"
}
```


---

## 3. 카테고리 등록 - CREATE

### Method

```plain text
POST
```


### URL

```plain text
http://localhost:8080/api/categories
```


### Headers

```plain text
Content-Type: application/json
```


### Body - raw - JSON

```json
{
  "categoryName": "운동",
  "description": "운동 및 건강 관리 카테고리"
}
```


현재 요청 DTO 기준으로 등록 시 필요한 JSON 필드는 다음 2개입니다.

```json
{
  "categoryName": "카테고리명",
  "description": "카테고리 설명"
}
```


---

## 4. 카테고리 수정 - UPDATE

### Method

```plain text
PUT
```


### URL

```plain text
http://localhost:8080/api/categories/1
```


`1` 자리에 수정할 `categoryId`를 넣으면 됩니다.

### Headers

```plain text
Content-Type: application/json
```


### Body - raw - JSON

```json
{
  "categoryName": "운동 수정",
  "description": "운동 및 헬스 관리 카테고리"
}
```


---

## 5. 카테고리 삭제 - DELETE

### Method

```plain text
DELETE
```


### URL

```plain text
http://localhost:8080/api/categories/1
```


`1` 자리에 삭제할 `categoryId`를 넣으면 됩니다.

### Body

없음

---

## Postman 테스트 순서 추천

1. **POST** `/api/categories`  
   카테고리 등록

2. **GET** `/api/categories`  
   등록된 목록 확인

3. **GET** `/api/categories/{categoryId}`  
   특정 카테고리 상세 조회

4. **PUT** `/api/categories/{categoryId}`  
   카테고리 수정

5. **GET** `/api/categories/{categoryId}`  
   수정 결과 확인

6. **DELETE** `/api/categories/{categoryId}`  
   카테고리 삭제

7. **GET** `/api/categories`  
   삭제 결과 확인

---

## Postman Body 설정 방법

POST 또는 PUT 요청에서 아래처럼 설정하세요.

```plain text
Body → raw → JSON
```


그리고 JSON 입력:

```json
{
  "categoryName": "공부",
  "description": "프로그래밍 공부 관련 카테고리"
}
```
