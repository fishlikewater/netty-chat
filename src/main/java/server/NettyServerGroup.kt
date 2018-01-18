package server

import io.netty.channel.MultithreadEventLoopGroup
import io.netty.channel.socket.ServerSocketChannel

class NettyServerGroup(val socketChannel: Class<out ServerSocketChannel>,
                       val boosGroup: MultithreadEventLoopGroup,
                       val workerGroup: MultithreadEventLoopGroup) {



}