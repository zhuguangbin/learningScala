package com.mvad.learningscala.akka

/**
 * Created by zhugb on 16-5-13.
 */

import akka.actor.{Actor, ActorSystem, Props}
import com.typesafe.config.ConfigFactory

case class AkkaMessage(message: Any)

case class Response(response: Any)


class Server extends Actor {
  override def receive: Receive = {
    //接收到的消息类型为AkkaMessage，则在前面加上response_，返回给sender
    case msg: AkkaMessage => {
      println("服务端收到消息: " + msg.message)
      sender ! Response("response_" + msg.message)
    }
    case _ => println("服务端不支持的消息类型 .. ")
  }
}

object Server {
  //创建远程Actor:ServerSystem
  def main(args: Array[String]): Unit = {

    val serverSystem = ActorSystem("Server", ConfigFactory.parseString(
      """
      akka {
       actor {
          provider = "akka.remote.RemoteActorRefProvider"
        }
        remote {
          enabled-transports = ["akka.remote.netty.tcp"]
          netty.tcp {
            hostname = "127.0.0.1"
            port = 2555
          }
        }
      }
      """))

    println("服务端启动, " + serverSystem)

    serverSystem.actorOf(Props[Server], "server")

  }
}
