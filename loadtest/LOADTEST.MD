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
wrk --latency -c4 -d2m -s getWithoutRepeats.lua http://localhost:8080
Running 2m test @ http://localhost:8080
  2 threads and 4 connections
	  Thread Stats   Avg      Stdev     Max   +/- Stdev
    Latency    40.02ms    1.00ms  49.91ms   95.71%
    Req/Sec    49.85      7.39    60.00     45.35%
  Latency Distribution
     50%   39.88ms
     75%   39.93ms
     90%   40.00ms
     99%   43.92ms
  11957 requests in 2.00m, 12.72MB read
  Non-2xx or 3xx responses: 48
Requests/sec:     99.60
```
---

#### 4 threads, 4 connections
```sh
wrk --latency -t4 -c4 -d2m -s getWithoutRepeats.lua http://localhost:8080
Running 2m test @ http://localhost:8080
  4 threads and 4 connections
  Thread Stats   Avg      Stdev     Max   +/- Stdev
    Latency    40.05ms    1.34ms 115.65ms   95.54%
    Req/Sec    24.91      5.00    30.00     50.94%
  Latency Distribution
     50%   39.89ms
     75%   39.94ms
     90%   40.01ms
     99%   43.96ms
  11959 requests in 2.00m, 12.77MB read
Requests/sec:     99.58
Transfer/sec:    108.92KB
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
    Latency    40.06ms    1.74ms 131.16ms   95.58%
    Req/Sec    49.82      8.14    60.00     34.13%
  Latency Distribution
     50%   39.88ms
     75%   39.94ms
     90%   40.02ms
     99%   43.96ms
  11947 requests in 2.00m, 12.76MB read
Requests/sec:     99.54
Transfer/sec:    108.87KB
```
---

#### 4 threads, 4 connections
```sh 
wrk --latency -t4 -c4 -d2m -s getWithRepeats.lua http://localhost:8080
Running 2m test @ http://localhost:8080
  4 threads and 4 connections
  Thread Stats   Avg      Stdev     Max   +/- Stdev
    Latency    40.20ms    1.43ms  91.40ms   93.07%
    Req/Sec    24.83      5.02    30.00     51.29%
  Latency Distribution
     50%   39.91ms
     75%   39.97ms
     90%   40.10ms
     99%   44.03ms
  11915 requests in 2.00m, 1.09MB read
  Non-2xx or 3xx responses: 11915
Requests/sec:     99.21
Transfer/sec:      9.30KB
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
wrk --latency -c4 -d2m -s getWithoutRepeats.lua http://localhost:8080
Running 2m test @ http://localhost:8080
  2 threads and 4 connections
  Thread Stats   Avg      Stdev     Max   +/- Stdev
    Latency    40.27ms    1.96ms 127.98ms   90.90%
    Req/Sec    49.54      7.56    60.00     43.33%
  Latency Distribution
     50%   39.90ms
     75%   39.97ms
     90%   40.12ms
     99%   44.02ms
  11890 requests in 2.00m, 12.65MB read
  Non-2xx or 3xx responses: 53
Requests/sec:     99.07
Transfer/sec:    107.92KB
```
---

#### 4 threads, 4 connections
```sh
wrk --latency -t4 -c4 -d2m -s getWithoutRepeats.lua http://localhost:8080
Running 2m test @ http://localhost:8080
  4 threads and 4 connections
  Thread Stats   Avg      Stdev     Max   +/- Stdev
    Latency    40.05ms    1.34ms 115.65ms   95.54%
    Req/Sec    24.91      5.00    30.00     50.94%
  Latency Distribution
     50%   39.89ms
     75%   39.94ms
     90%   40.01ms
     99%   43.96ms
  11959 requests in 2.00m, 12.77MB read
Requests/sec:     99.58
Transfer/sec:    108.92KB
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
    Latency    40.24ms    1.64ms 125.51ms   90.55%
    Req/Sec    49.56      7.26    60.00     47.12%
  Latency Distribution
     50%   39.90ms
     75%   39.97ms
     90%   40.11ms
     99%   44.01ms
  11895 requests in 2.00m, 12.71MB read
Requests/sec:     99.11
Transfer/sec:    108.41KB
```
---

#### 4 threads, 4 connections
```sh 
wrk --latency -t4 -c4 -d2m -s getWithRepeats.lua http://localhost:8080
Running 2m test @ http://localhost:8080
  4 threads and 4 connections
^[[B^[[B^[[B  Thread Stats   Avg      Stdev     Max   +/- Stdev
    Latency    40.26ms    1.52ms  93.23ms   90.23%
    Req/Sec    24.77      4.99    30.00     52.41%
  Latency Distribution
     50%   39.91ms
     75%   39.98ms
     90%   40.23ms
     99%   44.03ms
  11894 requests in 2.00m, 12.70MB read
Requests/sec:     99.08
Transfer/sec:    108.38KB
```
---

### заключение
Была проведена оптимизация хранилища(переписан с учетом nio и добавлено метод isDataExist,
чтобы не порождать лишние исключение - как в прошлое реализации). Был добавлен к серверу
Executor, и был оптимизирован метод handler.
