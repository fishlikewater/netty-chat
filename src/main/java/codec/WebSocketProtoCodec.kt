package codec

import io.netty.buffer.ByteBuf
import io.netty.buffer.ByteBufAllocator
import io.netty.channel.ChannelHandlerContext
import io.netty.handler.codec.MessageToMessageCodec
import org.slf4j.LoggerFactory
import protocol.Proto

class WebSocketProtoCodec : MessageToMessageCodec<ByteBuf, Proto>() {
    private val logger = LoggerFactory.getLogger(WebSocketProtoCodec::class.java)

    @Throws(Exception::class)
    override fun encode(channelHandlerContext: ChannelHandlerContext, proto: Proto, list: MutableList<Any>) {
        val byteBuf = ByteBufAllocator.DEFAULT.buffer()
        val body = proto.body;
        if (body != null) {
            byteBuf.writeInt(proto.HEADER_LENGTH + body.size)
            byteBuf.writeShort(proto.HEADER_LENGTH.toInt())
            byteBuf.writeShort(proto.VERSION.toInt())
            byteBuf.writeInt(proto.operation)
            byteBuf.writeInt(proto.seqId)
            byteBuf.writeBytes(proto.body)
        } else {
            byteBuf.writeInt(proto.HEADER_LENGTH.toInt())
            byteBuf.writeShort(proto.HEADER_LENGTH.toInt())
            byteBuf.writeShort(proto.VERSION.toInt())
            byteBuf.writeInt(proto.operation)
            byteBuf.writeInt(proto.seqId)
        }
        list.add(byteBuf)

        logger.debug("encode: {}", proto)
    }

    @Throws(Exception::class)
    override fun decode(channelHandlerContext: ChannelHandlerContext, byteBuf: ByteBuf, list: MutableList<Any>) {
        val proto = Proto(byteBuf.readInt(),byteBuf.readShort(),byteBuf.readShort(),byteBuf.readInt(),byteBuf.readInt())
        if (proto.packetLen > proto.headerLen) {
            val bytes = ByteArray(proto.packetLen - proto.headerLen)
            byteBuf.readBytes(bytes)
            proto.body = bytes
        }

        list.add(proto)

        logger.debug("decode: {}", proto)
    }
}