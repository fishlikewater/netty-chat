package protocol

import java.io.Serializable

data class Proto(var packetLen: Int = 0,
                 var headerLen: Short = 0,
                 var version: Short = 0,
                 var operation: Int = 0,
                 var seqId: Int = 0,
                 var body: ByteArray? = null) : Serializable {

    val HEADER_LENGTH: Short = 16
    val VERSION: Short = 1

}