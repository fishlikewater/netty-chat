package server

interface ChatServer {

    fun start(port: Int);

    fun shotdown();

    fun restart(port: Int);

}