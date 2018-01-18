package message

import protocol.Proto

class MessageServiceImp : MessageService {
    override fun deal(proto: Proto) {
        print(proto.toString())
    }
}