# Process-Scheduling-Simulator
2023학년도 1학기 운영체제 Process Scheduling Simulator

## 대상 알고리즘
- FCFS (First-Come-First-Service)
- RR (Round-Robin)
- SPN (Shortest-Process-Next)
- SRTN (Shortest-Remaining-Time-Next)
- HRRN (High-Response-Ratio-Next)
- Your own algorithm

## Request
### Processes
- `1 <= processes.size() <= 99`
- property in each process
  - name 
  - arrivalTime
  - workload
### Processors
- `1 <= processors.size() <= 4`
- property in each process
  - name
  - core
#### Core
- `E Core`
  - throughput per second: `1 workload`
  - power consumption per second: `1W`
  - startup power: `0.1W`
- `P Core` 
  - throughput per second: `2 workload`
  - power consumption per second: `3W`
  - startup power: `0.5W` 
### Algorithm
- only one
- enabled
  - FCFS
  - RR
  - SPN
  - SRTN
  - HRRN
  - OSDS
### Time quantum

## Response
- For each time range
### Time
- fromTime, toTime (range)

### Pairs
- pair list
- one pair is (process, processor)
- this means, process assigned to processor

### ProcessorPowerConsumptions

### TotalPowerConsumption

### Ready queue

### Terminated Processes

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
