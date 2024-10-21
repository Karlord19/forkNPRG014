import java.util.concurrent.Executors

class Counter {
    static long counter = 0
}

def myPool = Executors.newFixedThreadPool(8)
final threads = (1..50).collect {
    myPool.submit {
        synchronized(Counter) {
            Counter.counter++
        }
    }
}
threads*.get()
myPool.shutdown()
myPool.awaitTermination(1, java.util.concurrent.TimeUnit.SECONDS)
println Counter.counter

//TASK Properly synchronize
//TASK Replace thread creation with a thread pool (e.g. using java.util.concurrent.Executors.newFixedThreadPool(8))
