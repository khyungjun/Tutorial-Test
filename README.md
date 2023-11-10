### 스프링 부트와 AWS로 혼자 구현하는 웹 서비스

## GitHub Action와 AWS S3 연동하기

<br>
Page 327 ~ 332
책에 나와 있는 내용과 거의 흡사합니다. 변경된 내용이 없다고 생각하셔도 좋습니다.

간단하게 설명하면 깃허브 액션을 사용하면 다음과 같은 구조로 진행이 됩니다. 

![image](https://user-images.githubusercontent.com/104341003/177384544-2e29c7df-4103-4406-9f78-88fc0207397a.png)

<br>
<br>
AWS S3는 파일 서버로, 이미지 파일 / 정적 파일 등을 관리하는 기능입니다.
실제 배포는AWS CodeDeploy에서 진행되는데, AWS CodeDeploy는 저장 기능이 없기 때문에, AWS S3와 연동하여  .jar 파일을 받아 배포할 수 있도록 진행됩니다. 

> AWS CodeDeploy에서도 빌드와 배포 모두 가능하지만
> 해당 실습에서는 AWS CodeDeploy는 배포를, 빌드는 GitHub Action에서 진행됩니다. 
> 빌드없이 배포만 하는경우 AWS CodeDeploy 하나로 진행하는 경우 대응하기 어렵기 때문입니다.
