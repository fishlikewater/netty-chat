package server
import handler.WebSocketServerInitializer
import io.netty.bootstrap.ServerBootstrap
import io.netty.channel.ChannelFuture
import io.netty.channel.ChannelOption
import io.netty.channel.EventLoopGroup
import io.netty.channel.epoll.EpollChannelOption
import io.netty.channel.nio.NioEventLoopGroup
import io.netty.channel.socket.nio.NioServerSocketChannel
import io.netty.handler.logging.LogLevel
import io.netty.handler.logging.LoggingHandler
import kit.EpollKit
import message.MessageService
import message.MessageServiceImp
import org.slf4j.LoggerFactory

class WebSocketChatServer : ChatServer {

    private val log = LoggerFactory.getLogger(WebSocketChatServer::class.java)
    private lateinit var workGroup: EventLoopGroup;
    private lateinit var boosGroup: EventLoopGroup;
    private lateinit var chananel: ChannelFuture
    private lateinit var messageService: MessageService;

    override fun start(port: Int) {
        messageService = MessageServiceImp();
        val startTime: Long = System.currentTimeMillis();
        val server: ServerBootstrap = ServerBootstrap();
        /** liunx 服务器有更高效的方式*/
        if (EpollKit.epollIsAvailable()) {
            log.info("⬢ Use EpollEventLoopGroup")
            val nettyGroup: NettyServerGroup = EpollKit.group(0, 0);
            workGroup = nettyGroup.workerGroup;
            boosGroup = nettyGroup.boosGroup;
            server.option(EpollChannelOption.SO_REUSEPORT, true)
            server.channel(nettyGroup.socketChannel);
        } else {
            log.info("⬢ Use NioEventLoopGroup")
            workGroup = NioEventLoopGroup();
            boosGroup = NioEventLoopGroup();
            server.channel(NioServerSocketChannel::class.java)
        }
        try {
            server.option(ChannelOption.SO_BACKLOG, 8196)
            server.option(ChannelOption.SO_REUSEADDR, true)
            server.childOption(ChannelOption.SO_REUSEADDR, true)
            server.group(workGroup, boosGroup)
                    .handler(LoggingHandler(LogLevel.DEBUG))
                    .childHandler(WebSocketServerInitializer(messageService))
            chananel = server.bind(port).sync();
            log.info("⬢ initialize successfully, Time elapsed: {} ms", System.currentTimeMillis() - startTime)
        } finally {
            val shutdownThread = Thread(Runnable { this.shotdown() })
            shutdownThread.name = "shutdown@thread"
            Runtime.getRuntime().addShutdownHook(shutdownThread)
        }
    }

    override fun shotdown() {
        chananel.channel().close().syncUninterruptibly()
        boosGroup.shutdownGracefully()
        workGroup.shutdownGracefully()
    }

    override fun restart(port: Int) {
        shotdown();
        start(port);
    }
}