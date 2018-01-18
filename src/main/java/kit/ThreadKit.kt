package kit

import java.util.concurrent.ThreadFactory
import java.util.concurrent.atomic.LongAdder

class ThreadKit(val prefix: String) : ThreadFactory {

    private val threadNumber = LongAdder()

    override fun newThread(r: Runnable): Thread {
        threadNumber.add(1)
        return Thread(r, this.prefix + " thread-" + threadNumber.toInt())
    }
}