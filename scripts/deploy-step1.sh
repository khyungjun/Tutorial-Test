# p.300 step1 deploy.sh

#!/bin/bash

REPOSITORY=/home/ec2-user/app/step1 # 프로젝트 디렉토리 주소는 스크립트 내에서 자주 사용하는 값이기 때문에 이를 변수로 저장한다. 마찬가지로 PROJECT_NAME=Tutorial-Test도 동일하게 변수로 저장한다. 쉘에서는 타입 없이 선언하여 저장한다. 쉘에서는 $변수명으로 변수를 사용할 수 있다.
PROJECT_NAME=Tutorial-Test 

cd $REPOSITORY/$PROJECT_NAME/ # 제일 처음 git clone 받았던 디렉토리로 이동한다. 바로 위의 쉘 변수 설명을 따라 /home/ec2-user/app/step1/Tutorial-Test 주소로 이동한다.

echo "> Git Pull"

git pull # 디렉토리 이동 후, master 브랜치의 최신 내용을 받는다.

echo "> 프로젝트 Build 시작"

./gradlew build # 프로젝트 내부의 gradlew로 build를 수행한다.

echo "> step1 디렉토리로 이동"

cd $REPOSITORY

echo "> Build 파일 복사"

cp $REPOSITORY/$PROJECT_NAME/build/libs/*.jar $REPOSITORY/ # build의 결과물인 jar 파일을 복사해 jar 파일을 모아둔 위치로 복사한다.

echo "> 현재 구동중인 애플리케이션 pid 확인"

CURRENT_PID=$(pgrep -f ${PROJECT_NAME}*.jar) # 기존에 수행 중이던 스프링 부트 어플리케이션을 종료한다. pgrep은 process id만 추출하는 명령어이다. -f 옵션은 프로세스 이름으로 찾는다.

echo "> 현재 구동중인 애플리케이션 pid: $CURRENT_PID"

if [ -z "$CURRENT_PID" ]; then # 현재 구동 중인 프로세스가 있는지 없는지를 판단해서 기능을 수행한다. process id 값을 보고 프로세스가 있으면 해당 프로세스를 종료한다.
	echo "> 현재 구동 중인 애플리케이션이 없으므로 종료하지 않습니다."
else
	echo "> kill -15 $CURRENT_PID"
	kill -15 $CURRENT_PID
	sleep 5
fi

echo "> 새 어플리케이션 배포"

JAR_NAME=$(ls -tr $REPOSITORY/ | grep *.jar | tail -n 1) # 새로 실행할 jar 파일명을 찾는다. 여러 jar 파일이 생기기 때문에 tail -n로 가장 나중의 jar 파일(최신 파일)을 변수에 저장한다.

echo "> JAR Name: $JAR_NAME"


# 1차 (맨 처음)
nohup java -jar $REPOSITORY/$JAR_NAME 2>&1 & 
# 찾은 jar 파일명으로 해당 jar 파일을 nohup으로 실행한다. 스프링 부트의 장점으로 특별히 외장 톰캣을 설치할 필요가 없다. 내장 톰캣을 사용해서 jar 파일만 있으면 바로 웹 어플리케이션 서버를 실행할 수 있다. 일반적으로 자바를 실행할 때는 java -jar라는 명령어를 사용하지만, 이렇게 하면 사용자가 터미널 접속을 끊을 때 어플리케이션도 같이 종료된다. 애플리케이션 실행자가 터미널을 종료해도 애플리케이션은 계속 구동될 수 있도록 nohup 명령어를 사용한다.


# 2차 (application-oauth.yml 추가 후 적용)
nohup java -jar \  <-- 역슬래쉬(\)를 했을 때 다음 칸을 띄지 않도록 한다. 역슬래시(\)가 있어야 다음 라인도 같은 줄로 인식을 하는데 노란색으로 나와야한다. 한 칸 띄면 흰색으로 나오며 인식하지 못한다.
-Dspring.config.location=classpath:/application.yml,/home/ec2-user/app/application-oauth.yml \
$REPOSITORY/$JAR_NAME 2>&1 &
# Dspring.config.location : 스프링 설정 파일 위치를 지정한다. 기본 옵션들을 담고 있는 application.yml과 OAuth 설정들을 담고 있는 applcation-oauth.yml의 위치를 지정한다. classpath가 붙으면 jar 안에 있는 resources 디렉토리를 기준으로 경로가 생성된다. applcation-oauth.yml은 절대경로를 사용한다. 외부에 파일이 있기 때문이다.


# 3차 (application-real.yml, application-real-db.yml 추가 후 적용) 
# (책에 classpath:/application-real.yml 빠져있음. 참고 : https://github.com/jojoldu/freelec-springboot2-webservice/issues/316)
nohup java -jar \
-Dspring.config.location=classpath:/application.yml,classpath:/application-real.yml,/home/ec2-user/app/application-oauth.yml,/home/ec2-user/app/application-real-db.yml \
-Dspring.profiles.active=real \
$REPOSITORY/$JAR_NAME 2>&1 &
# Dspring.profiles.active=real : applcation-real.yml을 활성화시킨다. applcation-real.yml의 spring.profiles.include=oauth,real-db 옵션 때문에 rea-db 역시 함께 활성화에 포함된다.