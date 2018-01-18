package server

import io.netty.channel.Channel
import java.util.concurrent.ConcurrentHashMap

object NettyChannelMap {

    private val map = ConcurrentHashMap<String, Channel>()

    fun add(clientId: String, channel: Channel) {
        map.put(clientId, channel)
    }

    fun get(clientId: String): Channel? {
        return map[clientId]
    }

    fun remove(channel: Channel) {
        for ((key, value) in map) {
            if (value === channel) {
                map.remove(key)
            }
        }
    }
}