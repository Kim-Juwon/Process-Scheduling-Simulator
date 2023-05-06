# Process-Scheduling-Simulator-API
2023학년도 1학기 운영체제 Process Scheduling Simulator API

## developer
- KimJuwon

## Algorithm
- [FCFS (First-Come-First-Service)](https://ko.wikipedia.org/wiki/%EC%84%A0%EC%9E%85_%EC%84%A0%EC%B2%98%EB%A6%AC_%EC%8A%A4%EC%BC%80%EC%A4%84%EB%A7%81)
- [RR (Round-Robin)](https://ko.wikipedia.org/wiki/%EB%9D%BC%EC%9A%B4%EB%93%9C_%EB%A1%9C%EB%B9%88_%EC%8A%A4%EC%BC%80%EC%A4%84%EB%A7%81)
- [SPN (Shortest-Process-Next)](https://ko.wikipedia.org/wiki/%EC%B5%9C%EB%8B%A8_%EC%9E%91%EC%97%85_%EC%9A%B0%EC%84%A0_%EC%8A%A4%EC%BC%80%EC%A4%84%EB%A7%81)
- [SRTN (Shortest-Remaining-Time-Next)](https://ko.wikipedia.org/wiki/%EC%B5%9C%EC%86%8C_%EC%9E%94%EB%A5%98_%EC%8B%9C%EA%B0%84_%EC%9A%B0%EC%84%A0_%EC%8A%A4%EC%BC%80%EC%A4%84%EB%A7%81)
- [HRRN (High-Response-Ratio-Next)](https://ko.wikipedia.org/wiki/HRRN_%EC%8A%A4%EC%BC%80%EC%A4%84%EB%A7%81)
- MN

## Request
### Processes
- `1 <= processes.size() <= 99`
- 프로세스별 property 
  - **name** 
  - **arrivalTime**
  - **workload**
### Processors
- `1 <= processors.size() <= 4`
- 프로세서별 property 
  - **name**
  - **core**
 
### Algorithm
- 다음 중 택 1
  - `FCFS`
  - `RR`
  - `SPN`
  - `SRTN`
  - `HRRN`
  - `MN`
 
### Time quantum
- Round-Robin 알고리즘에서의 프로세스 실행 제한 시간

### Request JSON Example

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

## Response
- 시간 구간(n ~ n + 1초)별 상태

### from
- start time (n)

### to
- end time (n + 1)

### Pairs
- [프로세스, 프로세서] pair 리스트
  - 프로세서가 해당 프르세스에 할당되었다는 의미
- pair별 property
  - **processorName**
  - **processName**

### ProcessorPowerConsumptions
- 프로세서별 누적 전력 소비량
- 프로세서 누적 전력 소비량별 property
  - **processorName**
  - **totalPowerConsumption**

### TotalPowerConsumption
- 모든 프로세서의 누적 전력 소비량 합

### Ready queue
- 현재 ready queue 상태 (프로세스 리스트)
- 우선순위순 (앞에서부터)

### Terminated Processes
- 해당 시간에 종료된 프로세스 리스트
- 프로세스별 property
  - **name**
  - **arrivalTime**
  - **burstTime**
  - **waitingTime**
  - **turnaroundTime**
  - **normalizedTurnaroundTime**

### Response JSON Example

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
                    "totalPowerConsumption": 7.3999999999999995
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
                    "totalPowerConsumption": 21.200000000000003
                }
            ],
            "totalPowerConsumption": 21.200000000000003,
            "readyQueue": [],
            "terminatedProcesses": [
                {
                    "name": "p2",
                    "arrivalTime": 1,
                    "burstTime": 7,
                    "waitingTime": 11,
                    "turnaroundTime": 18,
                    "normalizedTurnaroundTime": 2.5714285714285716
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
                    "totalPowerConsumption": 21.200000000000003
                }
            ],
            "totalPowerConsumption": 21.200000000000003,
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

## Algorithm Flow chart
### FCFS (First-Come-First-Service)
![image](https://user-images.githubusercontent.com/56067949/234043216-a8b9fe92-1bed-49a7-bea9-2b6eafd22bcd.png)

### RR (Round-Robin)
![image](https://user-images.githubusercontent.com/56067949/234043442-f0164844-c077-415f-b9bd-7b75989e3e84.png)

### SPN (Shortest-Process-Next)
![image](https://user-images.githubusercontent.com/56067949/234043700-b01184c0-97cd-4385-91e5-b644bf54334b.png)

### SRTN (Shortest-Remaining-Time-Next)
![image](https://user-images.githubusercontent.com/56067949/234043872-daa5732b-7ed7-4af9-be96-331aa54862d6.png)

### HRRN (High-Response-Ratio-Next)
![image](https://user-images.githubusercontent.com/56067949/234043953-283287d2-499d-42c0-bc6f-d7140c91e2d2.png)

## Class Diagram
![image](https://user-images.githubusercontent.com/56067949/236477220-a80fb01f-798a-41ee-9a8c-dcabd84766c9.png)

### Scheduler (abstract class)
- 각 알고리즘에 맞는 스케줄러가 상속하는 추상 클래스
- properties
  - `readyQueue`
    - ready state process들의 ready queue
  - `runningStatus`
    - running state에 있는 process 및 그에 할당된 processor 정보
  - `notArrivedProcesses`
    - 아직 도착하지 않은(현재 시간이 arrivalTime보다 과거인 경우) process들
  - `availableProcessors`
    - 현재 running state에 있는 프로세스들에 할당되어 있지 않은 processor들
- methods
  - `schedule()`
    - 요청된 정보로 스케줄링을 진행하고 결과를 리턴

### ReadyQueue (abstract class)
- 각 알고리즘에 맞는 ready queue가 상속하는 추상 클래스
- properties
  - `readyQueue`
    - List of ready state process
- methods
  - `isEmpty()`
    - ready queue가 비어있는지 확인
  - `addArrivedProcessFrom()`
    - 현재 시간이 도착한 process들을 ready queue에 삽입
  - `getNextProcess()`
    - ready queue에서 pop한 process
  - `increaseWaitingTimeOfProcesses()`
    - ready queue에 있는 process들의 waiting time을 1씩 증가시킴
  - `peekCurrentProcesses()`
    - 현재 ready queue에 있는 process들을 priorty가 높은 순서대로 보여주는 process list 리턴
- 구현 클래스
  - `FCFSReadyQueue`
  - `RRReadyQueue`
  - `SPNReadyQueue`
  - `SRTNReadyQueue`
  - `HRRNReadyQueue`
  - `MNReadyQueue`
  
### Preemptible (interface)
- 선점 알고리즘의 ready queue가 구현해야 하는 interface
- methods
  - `addPreemptedProcesses()`
    - 선점당한 프로세스들을 ready queue에 삽입
- concrete class
  - `RRReadyQueue`
  - `SRTNReadyQueue`
  - `MNReadyQueue`

## Tech Stack & Infra
- Java 11
- Spring Boot 2.7.12
- Maven
- AWS EC2
- Nginx
