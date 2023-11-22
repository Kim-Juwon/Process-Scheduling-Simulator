# Process-Scheduling-Simulator-API
- 한국기술교육대학교 컴퓨터공학부 23-1학기 [운영체제(김덕수 교수님)](https://www.youtube.com/playlist?list=PLBrGAFAIyf5rby7QylRc6JxU5lzQ9c4tN) 팀프로젝트 API 

<img width="500" alt="image" src="https://github.com/Kim-Juwon/Process-Scheduling-Simulator-API/assets/56067949/08adf662-157f-4f11-8302-17e776adf1b7">

## 시연 영상

<details>

### Youtube
- [Process Scheduling Simulator 사용법](https://youtu.be/T0AJoeZl174)

### GIF
![Screen-Recording-2023-05-12-at-11 49 29-PM](https://github.com/Kim-Juwon/Process-Scheduling-Simulator-API/assets/56067949/51e43751-9746-4d50-a40a-c114196d1e05)

</details>
  
## '프로세스 스케줄링' 이란?

<details>

- Process 스케줄링은 multi-tasking system에서 여러 프로세스들에 프로세서 할당(dispatch) 순서를 결정하는 작업입니다.
- 목적은 성능 향상에 있습니다.
- 성능 지표는 굉장히 많은 종류가 있으며, 대표적인 지표는 다음과 같습니다.
    - mean response time (평균 응답 시간)
    - throughput (단위 시간당 처리량)
    - resource utilization (단위 시간당 자원 활용도)

</details>

## 기본 알고리즘
FCFS, RR, SPN, SRTN, HRRN

<details>

### [FCFS (First-Come-First-Service)](https://ko.wikipedia.org/wiki/%EC%84%A0%EC%9E%85_%EC%84%A0%EC%B2%98%EB%A6%AC_%EC%8A%A4%EC%BC%80%EC%A4%84%EB%A7%81)
- 도착한 순서대로 프로세스를 dispatch
- Non-preemptive 알고리즘

<img width="500" alt="image" src="https://github.com/Kim-Juwon/Process-Scheduling-Simulator-API/assets/56067949/fa866ce9-0043-4706-ae74-97a8756f940d">

- Batch system에 적합
  - 빠른 응답시간보다는 작업 처리에 대한 성능이 더 중요하기 때문
- time-sharing(interactive) system에 부적합
- 장점
  - resource utilization이 높음
    - 불필요한 스케줄링(context switching)이 이루어지지 않아 프로세서가 지속적으로 작업을 수행할 수 있기 때문
- 단점
  - convoy effect가 발생
    - burst time이 긴 프로세스에 의해 다른 프로세스들의 대기시간이 길어지는 현상
  - 평균 respone time이 김
    - convoy effect가 원인   

### [RR (Round-Robin)](https://ko.wikipedia.org/wiki/%EB%9D%BC%EC%9A%B4%EB%93%9C_%EB%A1%9C%EB%B9%88_%EC%8A%A4%EC%BC%80%EC%A4%84%EB%A7%81)
- 도착한 순서대로 프로세스를 dispatch 하되, **프로세서 사용 제한 시간(time quantum)** 이 존재
- Preemptive 알고리즘

<img width="500" alt="image" src="https://github.com/Kim-Juwon/Process-Scheduling-Simulator-API/assets/56067949/75b4137b-7bb9-4743-98cc-8400f43a2dce">

- running 상태의 프로세스중 time quantum이 만료된 프로세스가 있고, ready 상태의 프로세스가 있다면 선점
- 장점
  - time-sharing(interavtive) system에 적합
  - 특정 프로세스들의 자원 독점을 방지
- 단점
  - 잦은 context switching으로 인해 overhead가 큼
- time quantum이 시스템 성능을 결정 짓는 핵심 요소
  - very large(infinite) time quantum -> **FCFS**
  - very small time quantum -> processor sharing
    - 사용자는 모든 프로세스가 각각의 프로세서 위에서 실행되는 것처럼 느끼게 됨    
    - (프로세서의 작업 수행 체감 속도) = (프로세서의 실제 작업 속도 * 프로세서의 개수)

### [SPN (Shortest-Process-Next)](https://ko.wikipedia.org/wiki/%EC%B5%9C%EB%8B%A8_%EC%9E%91%EC%97%85_%EC%9A%B0%EC%84%A0_%EC%8A%A4%EC%BC%80%EC%A4%84%EB%A7%81)
- burst time이 가장 작은 프로세스를 dispatch
- Non-preemptive 알고리즘

<img width="500" alt="image" src="https://github.com/Kim-Juwon/Process-Scheduling-Simulator-API/assets/56067949/1516050d-6b0e-4b7b-9055-37698828b8a9">

- 장점
  - 프로세스들의 평균 waiting time과 response time이 짧음
  - 시스템 내 프로세스들의 수를 최소화
    - 스케줄링 overhead가 감소하고, 메모리 절약을 할 수 있어 시스템의 효율을 향상시킴
- 단점
  - burst time이 상대적으로 긴 프로세스는 **starvation** 현상이 발생할 수 있음
  - burst time을 예측하기 어려움
    - 예측하기 위한 기법이 필요 

### [SRTN (Shortest-Remaining-Time-Next)](https://ko.wikipedia.org/wiki/%EC%B5%9C%EC%86%8C_%EC%9E%94%EB%A5%98_%EC%8B%9C%EA%B0%84_%EC%9A%B0%EC%84%A0_%EC%8A%A4%EC%BC%80%EC%A4%84%EB%A7%81)
- SPN을 preemptive 방식으로 변형한 알고리즘
- 잔여 burst time이 running 프로세스보다 더 적은 ready 상태의 프로세스가 있다면 선점

<img width="500" alt="image" src="https://github.com/Kim-Juwon/Process-Scheduling-Simulator-API/assets/56067949/0a9280f3-d9b5-48e3-a6dd-92b63b578ddd">

- 장점
  - SPN의 장점을 극대화
- 단점
  - 잔여 burst time을 계속해서 추적해야 하는 overhead
  - 잦은 context switching으로 인한 overhead
- 위 단점으로 인해 구현 및 사용이 비현실적 

### [HRRN (High-Response-Ratio-Next)](https://ko.wikipedia.org/wiki/HRRN_%EC%8A%A4%EC%BC%80%EC%A4%84%EB%A7%81)
- SPN + **Aging** concept을 적용한 알고리즘
- Non-preemptive 알고리즘
- Aging concept
  - 프로세스의 waiting time을 고려하여 우선순위 설정
  - response ratio가 가장 높은 프로세스가 가장 우선순위가 높음
    - response ratio: `(WT + BT) / BT`

<img width="500" alt="image" src="https://github.com/Kim-Juwon/Process-Scheduling-Simulator-API/assets/56067949/03632d5a-5954-40a4-9f3c-cf3ad34e420c">

- 장점
  - SPN의 장점을 취하면서도 starvation을 방지함
- 단점
  - ready 상태 프로세스들의 우선순위를 지속적으로 업데이트 필요   
  - burst time 예측 기법 필요   

</details>

## 알고리즘 구현 Flow chart

<details>

<summary> FCFS </summary>

### FCFS (First-Come-First-Service)
![image](https://user-images.githubusercontent.com/56067949/234043216-a8b9fe92-1bed-49a7-bea9-2b6eafd22bcd.png)

</details>

<details>

<summary> RR </summary>

### RR (Round-Robin)
![image](https://user-images.githubusercontent.com/56067949/234043442-f0164844-c077-415f-b9bd-7b75989e3e84.png)

</details>

<details>

<summary> SPN </summary>

### SPN (Shortest-Process-Next)
![image](https://user-images.githubusercontent.com/56067949/234043700-b01184c0-97cd-4385-91e5-b644bf54334b.png)

</details>

<details>

<summary> SRTN </summary>

### SRTN (Shortest-Remaining-Time-Next)
![image](https://github.com/Kim-Juwon/Process-Scheduling-Simulator-API/assets/56067949/581564c9-c491-438e-ad91-8334eae56956)


</details>

<details>

<summary> HRRN </summary>

### HRRN (High-Response-Ratio-Next)
![image](https://user-images.githubusercontent.com/56067949/234043953-283287d2-499d-42c0-bc6f-d7140c91e2d2.png)

</details>

<details>

<summary> 말년병장 </summary>

### 말년병장
![image](https://github.com/Kim-Juwon/Process-Scheduling-Simulator-API/assets/56067949/cc3b1672-5b68-4192-920b-e32b86fb1b59)

</details>

## API 명세

<details>

<summary> Request </summary>

## `POST /schedule`
  
### HTTP Body
#### Processes
- `1 <= processes.size() <= 99`
- 프로세스별 property 
  - **name** 
  - **arrivalTime**
  - **workload**
#### Processors
- `1 <= processors.size() <= 15`
- 프로세서별 property 
  - **name**
  - **core**
 
#### Algorithm
- 다음 중 택 1
  - `FCFS`
  - `RR`
  - `SPN`
  - `SRTN`
  - `HRRN`
  - `MN`
 
#### Time quantum
- Round-Robin 알고리즘에서의 프로세스 실행 제한 시간

#### Request JSON Example

```json
{
    "processes": [
        {
            "name": "p1",
            "arrivalTime": 0,
            "workload": 9
        },
        {
            "name": "p2",
            "arrivalTime": 1,
            "workload": 8
        },
        {
            "name": "p3",
            "arrivalTime": 3,
            "workload": 11
        },
        {
            "name": "p4",
            "arrivalTime": 4,
            "workload": 7
        },
        {
            "name": "p5",
            "arrivalTime": 5,
            "workload": 12
        }
    ],
    "processors": [
        {
            "name": "Core1",
            "core": "E"
        }
    ],
    "algorithm": "RR",
    "timeQuantum": 2
}
```

</details>

<details>
<summary> Response </summary>

### HTTP Body
- 시간 구간(n ~ n + 1초)별 상태

#### from
- start time (n)

#### to
- end time (n + 1)

#### Pairs
- [프로세스, 프로세서] pair 리스트
  - 프로세서가 해당 프르세스에 할당되었다는 의미
- pair별 property
  - **processorName**
  - **processName**

#### ProcessorPowerConsumptions
- 프로세서별 누적 전력 소비량
- 프로세서 누적 전력 소비량별 property
  - **processorName**
  - **totalPowerConsumption**

#### TotalPowerConsumption
- 모든 프로세서의 누적 전력 소비량 합

#### Ready queue
- 현재 ready queue 상태 (프로세스 리스트)
- 우선순위순 (앞에서부터)

#### Terminated Processes
- 해당 시간에 종료된 프로세스 리스트
- 프로세스별 property
  - **name**
  - **arrivalTime**
  - **burstTime**
  - **waitingTime**
  - **turnaroundTime**
  - **normalizedTurnaroundTime**

#### Response JSON Example

```json
{
    "statuses": [
        {
            "from": 0,
            "to": 1,
            "pairs": [
                {
                    "processorName": "Core1",
                    "processName": "p1"
                }
            ],
            "processorPowerConsumptions": [
                {
                    "processorName": "Core1",
                    "totalPowerConsumption": 1.1
                }
            ],
            "totalPowerConsumption": 1.1,
            "readyQueue": [],
            "terminatedProcesses": []
        },
        
        ...
        skip
        ...
        
        {
            "from": 6,
            "to": 7,
            "pairs": [
                {
                    "processorName": "Core1",
                    "processName": "p3"
                }
            ],
            "processorPowerConsumptions": [
                {
                    "processorName": "Core1",
                    "totalPowerConsumption": 7.4
                }
            ],
            "totalPowerConsumption": 7.4,
            "readyQueue": [
                "p2",
                "p4",
                "p5"
            ],
            "terminatedProcesses": []
        },
        
        ...
        skip
        ...
        
        {
            "from": 19,
            "to": 20,
            "pairs": [
                {
                    "processorName": "Core1",
                    "processName": "p4"
                }
            ],
            "processorPowerConsumptions": [
                {
                    "processorName": "Core1",
                    "totalPowerConsumption": 21.2
                }
            ],
            "totalPowerConsumption": 21.2
            "readyQueue": [],
            "terminatedProcesses": [
                {
                    "name": "p2",
                    "arrivalTime": 1,
                    "burstTime": 7,
                    "waitingTime": 11,
                    "turnaroundTime": 18,
                    "normalizedTurnaroundTime": 2.57
                }
            ]
        },
        {
            "from": 20,
            "to": 21,
            "pairs": [
                {
                    "processorName": "Core1",
                    "processName": null
                }
            ],
            "processorPowerConsumptions": [
                {
                    "processorName": "Core1",
                    "totalPowerConsumption": 21.2
                }
            ],
            "totalPowerConsumption": 21.2,
            "readyQueue": [],
            "terminatedProcesses": [
                {
                    "name": "p4",
                    "arrivalTime": 5,
                    "burstTime": 5,
                    "waitingTime": 10,
                    "turnaroundTime": 15,
                    "normalizedTurnaroundTime": 3.0
                }
            ]
        }
    ]
}
```

</details>


## 클래스 다이어그램

<details>

![image](https://user-images.githubusercontent.com/56067949/236689528-039a3bf0-3cc1-4b5c-a3da-4695438f5bd6.png)

![domain diagram](https://github.com/Kim-Juwon/Process-Scheduling-Simulator-API/assets/56067949/8fc9aa55-769c-4b63-a1eb-255a16bb8ca9)

</details>

## 사용 기술

<details>

- Language 
  - Java 11
- Framework
  - Spring Boot 2.7.12
- Build tool 
  - Maven
- Infra 
  - AWS EC2
  - Nginx
