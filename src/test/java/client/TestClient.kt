package client

import codec.WebSocketProtoCodec
import com.google.protobuf.ByteString
import io.netty.bootstrap.Bootstrap
import io.netty.channel.ChannelInitializer
import io.netty.channel.nio.NioEventLoopGroup
import io.netty.channel.socket.SocketChannel
import io.netty.channel.socket.nio.NioSocketChannel
import pb.Auth
import pb.Message
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
                            ch.pipeline().addLast(WebSocketProtoCodec())
                        }
                    })

            val f = b.connect(host, port).sync()
            val authReq = Auth.AuthReq.newBuilder()
                    .setUid(1)
                    .setToken("test")
                    .build()
            var proto = Proto()
            proto.version = 1.toShort()
            proto.operation=0
            proto.body=authReq.toByteArray()
            f.channel().writeAndFlush(proto)

            val msgData = Message.MsgData.newBuilder()
                    .setTo(1)
                    .setType(Message.MsgType.SINGLE_TEXT)
                    .setData(ByteString.copyFromUtf8("TEST"))
                    .build()
            proto = Proto()
            proto.version = 1.toShort()
            proto.operation = 5
            proto.body = msgData.toByteArray()
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
    TestClient("127.0.0.1", 9000)
}