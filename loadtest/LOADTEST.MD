# 3 этап - тестирование с Нагрузкой
## используемые инструменты

Для подачи нагрузки на KV сервис был использован wrk, 
скрипты для запуска лежат в папке loadtest.

Профилирование производилось с jvisualvm.

## тестирование до оптимизаций

### PUT без перезаписи

#### 2 threads, 4 connections:
```sh
wrk --latency -t2 -c4 -d2m -s putWithoutOverwriting.lua http://localhost:8080
Running 2m test @ http://localhost:8080
  2 threads and 4 connections
  Thread Stats   Avg      Stdev     Max   +/- Stdev
    Latency    38.34ms    1.67ms 125.85ms   91.02%
    Req/Sec    49.49      6.66    60.00     56.30%
  Latency Distribution
     50%   38.01ms
     75%   38.28ms
     90%   39.35ms
     99%   42.70ms
  11876 requests in 2.00m, 1.06MB read
Requests/sec:     98.92
Transfer/sec:      9.08KB
```

---

#### 4 threads, 4 connections:
```sh
wrk --latency -t4 -c4 -d2m -s putWithoutOverwriting.lua http://localhost:8080
Running 2m test @ http://localhost:8080
  4 threads and 4 connections
  Thread Stats   Avg      Stdev     Max   +/- Stdev
    Latency    38.13ms    1.52ms 113.75ms   92.37%
    Req/Sec    24.82      4.99    30.00     52.27%
  Latency Distribution
     50%   37.92ms
     75%   38.08ms
     90%   38.72ms
     99%   42.20ms
  11911 requests in 2.00m, 1.07MB read
Requests/sec:     99.22
Transfer/sec:      9.11KB
```

---

### GET без повторов:

#### 2 threads, 4 connections
```sh
Running 2m test @ http://localhost:8080
  2 threads and 4 connections
  Thread Stats   Avg      Stdev     Max   +/- Stdev
    Latency    40.15ms    1.66ms 116.79ms   93.47%
    Req/Sec    49.70      7.26    60.00     47.58%
  Latency Distribution
     50%   39.89ms
     75%   39.95ms
     90%   40.05ms
     99%   44.01ms
  11921 requests in 2.00m, 6.88MB read
  Non-2xx or 3xx responses: 5998
Requests/sec:     99.34
Transfer/sec:     58.67KB
```
---

#### 4 threads, 4 connections
```sh
Running 2m test @ http://localhost:8080
  4 threads and 4 connections
  Thread Stats   Avg      Stdev     Max   +/- Stdev
    Latency    40.14ms    1.50ms 118.45ms   93.21%
    Req/Sec    24.85      5.01    30.00     51.00%
  Latency Distribution
     50%   39.89ms
     75%   39.95ms
     90%   40.05ms
     99%   44.00ms
  11930 requests in 2.00m, 12.66MB read
  Non-2xx or 3xx responses: 83
Requests/sec:     99.35
Transfer/sec:    107.98KB
```
---

### PUT c перезаписью

#### 2 threads, 4 connections
```sh
wrk --latency -c4 -d2m -s putWithOverwritting.lua http://localhost:8080
Running 2m test @ http://localhost:8080
  2 threads and 4 connections
  Thread Stats   Avg      Stdev     Max   +/- Stdev
    Latency    38.18ms    1.41ms  86.65ms   92.31%
    Req/Sec    49.65      6.49    60.00     57.89%
  Latency Distribution
     50%   37.98ms
     75%   38.11ms
     90%   38.72ms
     99%   42.34ms
  11907 requests in 2.00m, 1.07MB read
Requests/sec:     99.20
Transfer/sec:      9.11KB
```
---
#### 4 threads, 4 connections
```sh
wrk --latency -t4 -c4 -d2m -s putWithOverwritting.lua http://localhost:8080
Running 2m test @ http://localhost:8080
  4 threads and 4 connections
  Thread Stats   Avg      Stdev     Max   +/- Stdev
    Latency    38.23ms    1.48ms  77.24ms   90.84%
    Req/Sec    24.78      5.00    30.00     52.47%
  Latency Distribution
     50%   37.96ms
     75%   38.14ms
     90%   38.93ms
     99%   42.66ms
  11896 requests in 2.00m, 1.07MB read
Requests/sec:     99.05
Transfer/sec:      9.10KB
```
---

### GET c перезаписью

#### 2 threads, 4 connections
```sh
wrk --latency -c4 -d2m -s getWithRepeats.lua http://localhost:8080
Running 2m test @ http://localhost:8080
  2 threads and 4 connections
  Thread Stats   Avg      Stdev     Max   +/- Stdev
    Latency    40.22ms    1.34ms 105.71ms   91.54%
    Req/Sec    49.65      7.42    60.00     45.08%
  Latency Distribution
     50%   39.90ms
     75%   39.97ms
     90%   40.12ms
     99%   44.01ms
  11909 requests in 2.00m, 12.72MB read
Requests/sec:     99.17
Transfer/sec:    108.47KB
```
---

#### 4 threads, 4 connections
```sh 
wrk --latency -t4 -c4 -d2m -s getWithRepeats.lua http://localhost:8080
Running 2m test @ http://localhost:8080
  4 threads and 4 connections
  Thread Stats   Avg      Stdev     Max   +/- Stdev
    Latency    40.11ms    1.28ms  83.62ms   93.74%
    Req/Sec    24.88      5.01    30.00     50.58%
  Latency Distribution
     50%   39.90ms
     75%   39.95ms
     90%   40.05ms
     99%   44.00ms
  11938 requests in 2.00m, 12.75MB read
Requests/sec:     99.42
Transfer/sec:    108.75KB
```
---


## ПОСЛЕ ОПТИМИЗАЦИЙ


### PUT без перезаписи

#### 2 threads, 4 connections:
```sh
wrk --latency -t2 -c4 -d2m -s putWithoutOverwriting.lua http://localhost:8080
Running 2m test @ http://localhost:8080
  2 threads and 4 connections
  Thread Stats   Avg      Stdev     Max   +/- Stdev
    Latency    38.59ms    1.98ms 109.85ms   87.85%
    Req/Sec    49.31      6.64    60.00     55.80%
  Latency Distribution
     50%   38.04ms
     75%   38.59ms
     90%   41.46ms
     99%   42.95ms
  11825 requests in 2.00m, 1.06MB read
Requests/sec:     98.52
Transfer/sec:      9.05KB
```

---

#### 4 threads, 4 connections:
```sh
wrk --latency -t4 -c4 -d2m -s putWithoutOverwriting.lua http://localhost:8080
Running 2m test @ http://localhost:8080
  4 threads and 4 connections
  Thread Stats   Avg      Stdev     Max   +/- Stdev
    Latency    39.02ms    1.87ms  87.01ms   87.40%
    Req/Sec    24.66      5.00    30.00     53.67%
  Latency Distribution
     50%   38.69ms
     75%   38.90ms
     90%   41.94ms
     99%   43.25ms
  11834 requests in 2.00m, 1.06MB read
Requests/sec:     98.59
Transfer/sec:      9.05KB
```

---

### GET без повторов:

#### 2 threads, 4 connections
```sh
Running 2m test @ http://localhost:8080
  2 threads and 4 connections
  Thread Stats   Avg      Stdev     Max   +/- Stdev
    Latency    40.19ms    1.85ms 119.33ms   92.83%
    Req/Sec    49.65      7.09    60.00     50.13%
  Latency Distribution
     50%   39.89ms
     75%   39.95ms
     90%   40.06ms
     99%   44.00ms
  11911 requests in 2.00m, 6.87MB read
  Non-2xx or 3xx responses: 5992
Requests/sec:     99.25
Transfer/sec:     58.63KB
```
---

#### 4 threads, 4 connections
```sh
wrk --latency -t4 -c4 -d2m -s getWithoutRepeats.lua http://localhost:8080
Running 2m test @ http://localhost:8080
  4 threads and 4 connections
  Thread Stats   Avg      Stdev     Max   +/- Stdev
    Latency    40.18ms    1.43ms  93.16ms   92.22%
    Req/Sec    24.84      5.01    30.00     51.27%
  Latency Distribution
     50%   39.90ms
     75%   39.96ms
     90%   40.08ms
     99%   43.99ms
  11916 requests in 2.00m, 12.65MB read
  Non-2xx or 3xx responses: 77
Requests/sec:     99.50
Transfer/sec:    107.92KB
```
---

### PUT c перезаписью

#### 2 threads, 4 connections
```sh
wrk --latency -c4 -d2m -s putWithOverwritting.lua http://localhost:8080
Running 2m test @ http://localhost:8080
  4 threads and 4 connections
  Thread Stats   Avg      Stdev     Max   +/- Stdev
    Latency    40.24ms    1.39ms  55.13ms   90.54%
    Req/Sec    24.78      5.00    30.00     52.15%
  Latency Distribution
     50%   39.90ms
     75%   39.97ms
     90%   40.14ms
     99%   44.02ms
  11896 requests in 2.00m, 12.71MB read
Requests/sec:     99.11
Transfer/sec:    108.41KB
```
---
#### 4 threads, 4 connections
```sh
wrk --latency -t4 -c4 -d2m -s putWithOverwritting.lua http://localhost:8080
Running 2m test @ http://localhost:8080
  4 threads and 4 connections
  Thread Stats   Avg      Stdev     Max   +/- Stdev
    Latency    38.63ms    1.67ms  61.21ms   86.37%
    Req/Sec    24.64      4.98    30.00     54.52%
  Latency Distribution
     50%   38.08ms
     75%   38.65ms
     90%   41.40ms
     99%   43.23ms
  11827 requests in 2.00m, 1.06MB read
Requests/sec:     98.50
Transfer/sec:      9.04KB
```
---

### GET c перезаписью

#### 2 threads, 4 connections
```sh
wrk --latency -c4 -d2m -s getWithRepeats.lua http://localhost:8080
Running 2m test @ http://localhost:8080
  2 threads and 4 connections
  Thread Stats   Avg      Stdev     Max   +/- Stdev
    Latency    40.13ms    1.46ms 108.57ms   93.62%
    Req/Sec    49.75      7.53    60.00     43.66%
  Latency Distribution
     50%   39.89ms
     75%   39.95ms
     90%   40.04ms
     99%   43.98ms
  11927 requests in 2.00m, 12.74MB read
Requests/sec:     99.37
Transfer/sec:    108.69KB
```
---

#### 4 threads, 4 connections
```sh 
wrk --latency -t4 -c4 -d2m -s getWithRepeats.lua http://localhost:8080
Running 2m test @ http://localhost:8080
  4 threads and 4 connections
  Thread Stats   Avg      Stdev     Max   +/- Stdev
    Latency    40.17ms    1.91ms 127.20ms   93.49%
    Req/Sec    24.85      5.02    30.00     51.28%
  Latency Distribution
     50%   39.89ms
     75%   39.95ms
     90%   40.05ms
     99%   43.99ms
  11923 requests in 2.00m, 12.74MB read
Requests/sec:     99.89
Transfer/sec:    108.60KB
```
---

### заключение
Была проведена оптимизация хранилища(переписан с учетом nio и добавлено метод isDataExist,
чтобы не порождать лишние исключение - как в прошлое реализации). Был добавлен к серверу
Executor, и был оптимизирован метод handler.
