package handler
import io.netty.channel.ChannelHandlerContext
import io.netty.channel.SimpleChannelInboundHandler
import message.MessageService
import org.slf4j.LoggerFactory
import protocol.Proto
import server.NettyChannelMap

class ChatServerHandler(val messageService: MessageService) : SimpleChannelInboundHandler<Proto>() {

    private val log = LoggerFactory.getLogger(ChatServerHandler::class.java)

    override fun channelRead0(ctx: ChannelHandlerContext, msg: Proto) {
        val sId = msg.seqId.toString();
        if(NettyChannelMap.get(sId) == null) NettyChannelMap.add(sId, ctx.channel())
        log.info("收到消息")
        print("收到消息")
        messageService.deal(msg)
    }

    override fun channelActive(ctx: ChannelHandlerContext) {
        log.info("连接成功")
        print("连接成功")
    }

    override fun channelInactive(ctx: ChannelHandlerContext) {
        NettyChannelMap.remove(ctx.channel())
        ctx.close();
        log.info("关闭连接")
    }

    @Suppress("OverridingDeprecatedMember")
    override fun exceptionCaught(ctx: ChannelHandlerContext, cause: Throwable) {
        log.error("异常消息", cause)
        ctx.close()
    }

}