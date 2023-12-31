#!/usr/bin/env bash

ABSPATH=$(readlink -f $0)
ABSDIR=$(dirname $ABSPATH)
source ${ABSDIR}/profile.sh

function switch_proxy() {
    IDLE_PORT=$(find_idle_port)

    echo "> 전환할 Port: $IDLE_PORT"
    echo "> Port 전환"
    echo "set \$service_url http://127.0.0.1:${IDLE_PORT};" | sudo tee /etc/nginx/conf.d/service-url.inc
    # echo "set \$service_url http://127.0.0.1:${IDLE_PORT};" : 하나의 문자을 만들어 파이프라인(|)으로 넘겨주기 위해 echo를 사용한다. 엔진엑스가 변경할 프록시 주소를 생성한다.
    # | sudo tee /etc/nginx/conf.d/service-url.inc : 앞에서 넘겨준 문장을 service-url.inc에 덮어쓴다.
    # 책에는 echo 'set \$service_url http://127.0.0.1:${IDLE_PORT};' : 이렇게 반드시 홑따옴포(')를 사용하라고 되어있지만 홑따옴표를 사용하면 변수를 찾아서 에러가 발생한다. 쌍따옴표를 사용해야 한다.
    # 쌍따옴표 (") : 변수의 실제 값을 출력 , 홀따옴표 (') : 변수명을 그대로 출력하기 때문에 쌍따옴표가 맞습니다
    # 참고 : https://github.com/jojoldu/freelec-springboot2-webservice/issues/238, https://github.com/jojoldu/freelec-springboot2-webservice/issues/51

    echo "> 엔진엑스 Reload"
    sudo service nginx reload  # 엔진엑스 설정을 다시 불러온다. restart와는 다르다. restart는 잠시 끊기는 현상이 있지만, reload는 끊김 없이 다시 불러온다. 다만, 중요한 설정들은 반영되지 않으므로 restart를 사용해야 한다. 여기선 외부의 설정 파일인 service-url을 다시 불러오는 거라 reload로 가능하다.
}