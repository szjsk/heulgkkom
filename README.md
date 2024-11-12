# heulgkkom

## 1. 프로젝트 소개
MSA 서비스에서 각 프로젝트간 API 통신을 위한 설정을 관리해주는 프로젝트입니다.

## 2. 기능
- 환경별 (dev, prod) 프로젝트 접근 도메인 관리
- 프로젝트 내 API 목록 관리
- 프로젝트 간 API 사용처리 요청/승인을 통한 사용처 관리
- 승인 된 API 소스를 해당 프로젝트에 자동 빌드하기 위한 Spec 제공
  (Heulgkkomball 플로그인 을 통한 자동 소스(전송코드 및 DTO) 생성)

### 3. 사용법(로컬 기준)

#### 3-1. 회원 가입
[test/UserRequest.http](test/UserRequest.http) 참조

A. 회원등록 : http://localhost:8079/register

#### 3-2. 프로젝트 구성 [test/MyApiManagerRequest.http](test/MyApiManagerRequest.http) 참조

B. 로그인 페이지 : http://localhost:8079/login

C. 프로젝트 생성 : POST http://localhost:8079/project

(프로젝트 생성시 현재 내 로그인 계정에는 해당 프로젝트 관리자 권한이 들어갑니다. 
하단 API승인 시 권한오류가 난다면 로그아웃 후 재 로그인해주세요.)

D. swagger 파일 등록 : POST http://localhost:8079/project/{{projectSeq}}
실제 프로젝트 소스 에서 추출한 OpenApi swagger 파일 등록

(ex : /v3/api-docs 를 통해 응답되는 json파일 )

_(현재 프로젝트의 API를 공유하지 않겠다면 등록하지 않아도 무방)_

E. 버전 활성화 : PUT http://localhost:8079/project/update/{{projectSeq}}/{{versionId}} 

등록한 swagger를 사용하도록 버전 업데이트

(D에서 올린 버전 및 현재 버전리스트는 GET http://localhost:8079/api/versions/{{projectSeq}} 에서 조회가능합니다.)


#### 3.3 API 요청.
A. 존재하는 API 목록 조회 :  GET http://localhost:8079/api/list/{{envType}}?projectName=&apiName= 

(프로젝트명, API명으로 검색 가능)

B. API 사용처 요청 : POST http://localhost:8079/api/request/{{my_projectSeq}}
{
"targetProjectSeq": 1 , //targetPath가 존재하는 프로젝트 seq
"targetPath": "/exmaple-url", //사용 요청할 API 경로 (API목록에 존재하는 path 기준)
"targetMethod": "POST", //사용 요청할 API 메소드
"requestReason": "test", //사용 요청 사유
"requestContact": "yourMail or phone" //사용 요청자 연락처
}


#### 3.4 API 승인
승인은 내가 해당 프로젝트에 권한이 존재해야합니다.
권한여부는 로컬 기준 http://localhost:8079/h2-console 접속 후  AUTHORITIES 테이블을 확인하면 됩니다.

A. 존재하는 API 목록 조회 :  GET http://localhost:8079/api/list/{{envType}}?projectName=&apiName=

(프로젝트명, API명으로 검색 가능)

B. API 사용 승인 : POST http://localhost:8079/api/approval/{{projectSeq}}

{
"permittedProjectApiSeqs": [1], //요청된 API의 seq
"status": "APPROVED", //승인 여부 (APPROVED, REJECTED)
"approvalReason": "test", // 승인 사유
"approvalContact": "yourMail or phone" //승인자 연락처
}

#### 3.5 정상 동작 여부 확인
API SPEC 조회 : GET http://localhost:8079/spec/api-json/{{projectName}}?envType={{envTypeSpec}}
조회 시 정상적으로 동작한다면 Heulgkkomball 플로그인을 통해 자동 생성할 수 있습니다.