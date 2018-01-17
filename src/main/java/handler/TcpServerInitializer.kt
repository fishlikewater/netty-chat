package handler

import MessageService
import codec.TcpProtoCodec
import io.netty.channel.Channel
import io.netty.channel.ChannelInitializer

class TcpServerInitializer(val messageService: MessageService) : ChannelInitializer<Channel>() {


    override fun initChannel(ch: Channel) {
        ch.pipeline().addLast(TcpProtoCodec())
        ch.pipeline().addLast(ChatServerHandler(messageService))
    }

}