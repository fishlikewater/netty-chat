package client

import server.TcpChatServer

class MyTest {

/*    @Test
    fun test1(){
        WebSocketChatServer().start(9000);
        TestClient("127.0.0.1", 9000)
        Thread.currentThread().join()
    }

    @Test
    fun test2(){
        TestClient("127.0.0.1", 9000)
    }*/

}

fun main(args: Array<String>) {
    TcpChatServer().start(9000);
}