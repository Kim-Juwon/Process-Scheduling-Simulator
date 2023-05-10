# Process-Scheduling-Simulator-API
- 2023학년도 한국기술교육대학교 컴퓨터공학부 1학기 운영체제 팀프로젝트
- professor: 김덕수 교수님

## Service URL
- Web
  - https://process-scheduler.link
- API
  - https://api.process-scheduler.link 

## Developer

<details>

- API (algorithm)
  - Juwon Kim
- View
  - Sehyun Park
  - Seungdae Cho
  - JuYoung Oh
  
</details>

## Algorithm

<details>

- [FCFS (First-Come-First-Service)](https://ko.wikipedia.org/wiki/%EC%84%A0%EC%9E%85_%EC%84%A0%EC%B2%98%EB%A6%AC_%EC%8A%A4%EC%BC%80%EC%A4%84%EB%A7%81)
- [RR (Round-Robin)](https://ko.wikipedia.org/wiki/%EB%9D%BC%EC%9A%B4%EB%93%9C_%EB%A1%9C%EB%B9%88_%EC%8A%A4%EC%BC%80%EC%A4%84%EB%A7%81)
- [SPN (Shortest-Process-Next)](https://ko.wikipedia.org/wiki/%EC%B5%9C%EB%8B%A8_%EC%9E%91%EC%97%85_%EC%9A%B0%EC%84%A0_%EC%8A%A4%EC%BC%80%EC%A4%84%EB%A7%81)
- [SRTN (Shortest-Remaining-Time-Next)](https://ko.wikipedia.org/wiki/%EC%B5%9C%EC%86%8C_%EC%9E%94%EB%A5%98_%EC%8B%9C%EA%B0%84_%EC%9A%B0%EC%84%A0_%EC%8A%A4%EC%BC%80%EC%A4%84%EB%A7%81)
- [HRRN (High-Response-Ratio-Next)](https://ko.wikipedia.org/wiki/HRRN_%EC%8A%A4%EC%BC%80%EC%A4%84%EB%A7%81)
- [MN (MalNeon-sergeant)](https://sour-microwave-e23.notion.site/1bb774fd863d4763b9286f4b3fdb7dab)

</details>

## Algorithm Flow chart

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

<summary> MN </summary>

### MN (MalNeon-sergeant)

</details>

## API Specification

<details>

<summary> Request </summary>

### Request
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

### Response
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


## Class Diagram

<details>

![image](https://user-images.githubusercontent.com/56067949/236689528-039a3bf0-3cc1-4b5c-a3da-4695438f5bd6.png)

![domain diagram](https://github.com/Kim-Juwon/Process-Scheduling-Simulator-API/assets/56067949/8fc9aa55-769c-4b63-a1eb-255a16bb8ca9)

</details>

## Tech Stack & Infra

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
