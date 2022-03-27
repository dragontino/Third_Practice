import kotlinx.coroutines.*
import java.util.*
import kotlin.collections.ArrayList

@ObsoleteCoroutinesApi
fun main() = runBlocking {

    val scope = CoroutineScope(newFixedThreadPoolContext(countThreads, "Andon"))

    scope.launch {
        repeat((1..countThreads).count()) {
            launch {
                if (it <= producers)
                    ProducerThread().start()
                else
                    ConsumerThread().start()
            }
        }
    }


    val input = Scanner(System.`in`)

    println("Запуск конвеера...")
    println("Чтобы остановить конвеер, нажмите 'q'")


    do {
        val stop = input.next()
    } while (stop != "q")

    finish = true

    println("Количество элементов в очереди: ${queue.itemCount()}")
}


private class ProducerThread: Thread() {
    private val random = Random()

    override fun run() {
        super.run()

        while (!finish) {
            if (queue.itemCount() >= 100) {
                sleep(timeSleep)
                continue
            }
            val value = random.nextInt(100) + 1
            queue.push(value)
            println("Количество элементов в очереди: ${queue.itemCount()}")
            sleep(600)
        }
    }
}

private class ConsumerThread: Thread() {
    override fun run() {
        super.run()

        while (!finish) {
            if (queue.isEmpty()) {
                sleep(timeSleep)
                continue
            }
            queue.pop()
            println("Количество элементов в очереди: ${queue.itemCount()}")
            sleep(300)
        }
    }
}

private val queue = Queue<Int>(200)
private const val consumers = 3
private const val producers = 2
private const val countThreads = consumers + producers

private const val timeSleep = 600L

@Volatile
private var finish = false


class Queue<E>(private val size: Int) {
    private val data = ArrayList<E>()

    @Synchronized
    fun itemCount() = data.size

    @Synchronized
    fun push(element: E) {
        if (data.size < size) data += element
    }

    @Synchronized
    fun pop() = if (itemCount() > 0)
        data.removeAt(0)
    else null

    @Synchronized
    fun isEmpty() =
        itemCount() == 0
}