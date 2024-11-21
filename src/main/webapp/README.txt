Eclipse 개발환경 구성 추가 항목

##########################################
####
#### 전자정부프레임워크 다운로드
####
###########################################

1. 다운로드 사이트 

주소 : http://www.egovframe.go.kr/EgovDevEnvRelease_300.jsp?menu=3&submenu=2&leftsub=2#
파일 : eGovFrameDev-3.7.0-64bit.exe[1,448,961,257 byte]

###########################################
####
#### Lombok 설치 및 사용 (getter, setter 관리)
####
###########################################

1. pom.xml에 lombok 라이브러리 추가

	<dependency>
	    <groupId>org.projectlombok</groupId>
	    <artifactId>lombok</artifactId>
	    <version>1.16.18</version>
	    <scope>provided</scope>
	</dependency>

2. 다운로드된 경로에서 cmd창에 설치 명령어 입력 (메이븐 레포지토리 경로 C:\Users\P&P04\.m2\repository\org\projectlombok\lombok\1.16.18)

	 > java -jar lombok-1.16.18.jar
 
3. installer 화면에서 'Specify location' 버튼클릭하여 eclipse 실행파일을 지정하고, 확인 버튼 클릭

4. "Install / Update" 클릭하여 설치

5. 'Quit installer' 클릭하여 종료

6. 이클립스 재실행

7. 참고사이트 
주소 : http://countryxide.tistory.com/16


###########################################
####
#### ERD 작성 ER Master 플러그인 설치
####
###########################################

1. Eclipse > Help > Install New Software 'Add' 버튼클릭

2. Name : ER Master, Location: http://justinkwony.github.io/ermaster-nhit/update-site/ 입력하여 'OK' 클릭하여 설치 진행

