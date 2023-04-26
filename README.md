# Process-Scheduling-Simulator-API
2023학년도 1학기 운영체제 Process Scheduling Simulator API

## developer
- KimJuwon

## 대상 알고리즘
- [FCFS (First-Come-First-Service)](https://ko.wikipedia.org/wiki/%EC%84%A0%EC%9E%85_%EC%84%A0%EC%B2%98%EB%A6%AC_%EC%8A%A4%EC%BC%80%EC%A4%84%EB%A7%81)
- [RR (Round-Robin)](https://ko.wikipedia.org/wiki/%EB%9D%BC%EC%9A%B4%EB%93%9C_%EB%A1%9C%EB%B9%88_%EC%8A%A4%EC%BC%80%EC%A4%84%EB%A7%81)
- [SPN (Shortest-Process-Next)](https://ko.wikipedia.org/wiki/%EC%B5%9C%EB%8B%A8_%EC%9E%91%EC%97%85_%EC%9A%B0%EC%84%A0_%EC%8A%A4%EC%BC%80%EC%A4%84%EB%A7%81)
- [SRTN (Shortest-Remaining-Time-Next)](https://ko.wikipedia.org/wiki/%EC%B5%9C%EC%86%8C_%EC%9E%94%EB%A5%98_%EC%8B%9C%EA%B0%84_%EC%9A%B0%EC%84%A0_%EC%8A%A4%EC%BC%80%EC%A4%84%EB%A7%81)
- [HRRN (High-Response-Ratio-Next)](https://ko.wikipedia.org/wiki/HRRN_%EC%8A%A4%EC%BC%80%EC%A4%84%EB%A7%81)
- Your own algorithm

## Request
### Processes
- `1 <= processes.size() <= 99`
- properties per processes 
  - **name** 
  - **arrivalTime**
  - **workload**
### Processors
- `1 <= processors.size() <= 4`
- properties per processors 
  - **name**
  - **core**
 
### Algorithm
- Allow only one of the following
  - `FCFS`
  - `RR`
  - `SPN`
  - `SRTN`
  - `HRRN`
  - `OSDS`
 
### Time quantum
- For Round Robin algorithm

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
        },
        {
            "name": "Core2",
            "core": "P"
        }
    ],
    "algorithm": "RR",
    "timeQuantum": 2
}
```

## Response
- List of information by time slice

### from
- start time

### to
- end time

### Pairs
- List of (Pair of process and processor)
  - Processor is assigned to process, meaning it is performing work.
- properties per pairs
  - **processorName**
  - **processName**

### ProcessorPowerConsumptions
- List of total power consumption by processor
- properties per pairs
  - **processorName**
  - **totalPowerConsumption**

### TotalPowerConsumption
- Sum of the total power consumption of all processors

### Ready queue
- Current state of the ready queue
- List of process
- Prioritized from front to back

### Terminated Processes
- List of terminated process
- properties per process
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
                },
                {
                    "processorName": "Core2",
                    "processName": null
                }
            ],
            "processorPowerConsumptions": [
                {
                    "processorName": "Core1",
                    "totalPowerConsumption": 1.1
                },
                {
                    "processorName": "Core2",
                    "totalPowerConsumption": 0.0
                }
            ],
            "totalPowerConsumption": 1.1,
            "readyQueue": [],
            "terminatedProcesses": []
        },
        {
            "from": 1,
            "to": 2,
            "pairs": [
                {
                    "processorName": "Core1",
                    "processName": "p1"
                },
                {
                    "processorName": "Core2",
                    "processName": "p2"
                }
            ],
            "processorPowerConsumptions": [
                {
                    "processorName": "Core1",
                    "totalPowerConsumption": 2.1
                },
                {
                    "processorName": "Core2",
                    "totalPowerConsumption": 3.5
                }
            ],
            "totalPowerConsumption": 5.6,
            "readyQueue": [],
            "terminatedProcesses": []
        },
        
        ... 
        ...
        skip
        ...
        ...
        
        {
            "from": 17,
            "to": 18,
            "pairs": [
                {
                    "processorName": "Core1",
                    "processName": "p5"
                },
                {
                    "processorName": "Core2",
                    "processName": null
                }
            ],
            "processorPowerConsumptions": [
                {
                    "processorName": "Core1",
                    "totalPowerConsumption": 18.9
                },
                {
                    "processorName": "Core2",
                    "totalPowerConsumption": 52.5
                }
            ],
            "totalPowerConsumption": 71.4,
            "readyQueue": [],
            "terminatedProcesses": [
                {
                    "name": "p3",
                    "arrivalTime": 3,
                    "burstTime": 8,
                    "waitingTime": 6,
                    "turnaroundTime": 14,
                    "normalizedTurnaroundTime": 1.75
                }
            ]
        },
        {
            "from": 18,
            "to": 19,
            "pairs": [
                {
                    "processorName": "Core1",
                    "processName": null
                },
                {
                    "processorName": "Core2",
                    "processName": null
                }
            ],
            "processorPowerConsumptions": [
                {
                    "processorName": "Core1",
                    "totalPowerConsumption": 18.9
                },
                {
                    "processorName": "Core2",
                    "totalPowerConsumption": 52.5
                }
            ],
            "totalPowerConsumption": 71.4,
            "readyQueue": [],
            "terminatedProcesses": [
                {
                    "name": "p5",
                    "arrivalTime": 5,
                    "burstTime": 8,
                    "waitingTime": 5,
                    "turnaroundTime": 13,
                    "normalizedTurnaroundTime": 1.625
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

## Tech Stack & Infra
- Java 11
- Spring Boot 2.7.12
- Maven
- AWS EC2
- Nginx
