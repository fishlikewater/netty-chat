package client

import codec.TcpProtoCodec
import io.netty.bootstrap.Bootstrap
import io.netty.channel.ChannelInitializer
import io.netty.channel.nio.NioEventLoopGroup
import io.netty.channel.socket.SocketChannel
import io.netty.channel.socket.nio.NioSocketChannel
import protocol.Proto

class TestClient(val host: String,
                 val port: Int) {

    private lateinit var group: NioEventLoopGroup;
    @Throws(Exception::class)
    fun start() {
        group = NioEventLoopGroup()

        try {
            val b = Bootstrap()
                    .group(group)
                    .channel(NioSocketChannel::class.java)
                    .handler(object : ChannelInitializer<SocketChannel>() {
                        @Throws(Exception::class)
                        override fun initChannel(ch: SocketChannel) {
                            ch.pipeline().addLast(TcpProtoCodec())
                        }
                    })

            val f = b.connect(host, port).sync()
            var proto = Proto()
            proto.version = 1.toShort()
            proto.operation = 0
            proto.body = "aaaa".toByteArray()
            f.channel().writeAndFlush(proto)
        } finally {
            val shutdownThread = Thread(Runnable { this.shotdown() })
            shutdownThread.name = "shutdown@thread"
            Runtime.getRuntime().addShutdownHook(shutdownThread)
        }
    }

    fun shotdown() {
        group.shutdownGracefully()

    }
}

fun main(args: Array<String>) {
    TestClient("localhost", 9000)
}