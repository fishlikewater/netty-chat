package kit

import io.netty.channel.epoll.EpollEventLoopGroup
import io.netty.channel.epoll.EpollServerSocketChannel
import server.NettyServerGroup

object EpollKit {

    /** 判断是否支持epoll */
    @Throws(Exception::class)
    fun epollIsAvailable(): Boolean {
        val obj = Class.forName("io.netty.channel.epoll.Epoll").getMethod("isAvailable").invoke(null)
        return null != obj && obj.toString().toBoolean() && System.getProperty("os.name").toLowerCase().contains("linux")
    }

    fun group(boos: Int, workers: Int): NettyServerGroup {
        val bossGroup = EpollEventLoopGroup(boos, ThreadKit("epoll-boss@"))
        val workerGroup = EpollEventLoopGroup(workers, ThreadKit("epoll-worker@"))
        return NettyServerGroup(EpollServerSocketChannel::class.java, bossGroup, workerGroup);
    }

}