package handler

import codec.TcpProtoCodec
import io.netty.channel.ChannelHandler
import io.netty.channel.ChannelInitializer
import io.netty.channel.socket.nio.NioSocketChannel
import io.netty.handler.codec.http.HttpObjectAggregator
import io.netty.handler.codec.http.HttpServerCodec
import io.netty.handler.codec.http.websocketx.WebSocketServerProtocolHandler
import io.netty.handler.stream.ChunkedWriteHandler
import message.MessageService

@ChannelHandler.Sharable
class WebSocketServerInitializer(val messageService: MessageService) : ChannelInitializer<NioSocketChannel>() {

    @Throws(Exception::class)
    override fun initChannel(ch: NioSocketChannel) {
        val pipeline = ch.pipeline()
        // 编解码 http 请求
        pipeline.addLast(HttpServerCodec())
        // 写文件内容
        pipeline.addLast(ChunkedWriteHandler())
        // 聚合解码 HttpRequest/HttpContent/LastHttpContent 到 FullHttpRequest
        // 保证接收的 Http 请求的完整性
        pipeline.addLast(HttpObjectAggregator(64 * 1024))
        // 处理其他的 WebSocketFrame
        pipeline.addLast(WebSocketServerProtocolHandler("/chat"))
        // 处理 TextWebSocketFrame
        pipeline.addLast(TcpProtoCodec())
        pipeline.addLast(ChatServerHandler(messageService))
    }
}