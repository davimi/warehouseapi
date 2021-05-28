package com.warehouse

import akka.http.scaladsl.Http
import akka.actor.typed.ActorSystem
import akka.actor.typed.scaladsl.Behaviors
import com.warehouse.api.WarehouseApiImplementaion
import com.warehouse.data.RedisService
import com.warehouse.service.WarehouseServiceImplementation

import scala.concurrent.ExecutionContextExecutor
import scala.io.StdIn

object Main extends App {

  val app = new WarehouseApiImplementaion(new WarehouseServiceImplementation(new RedisService))

  implicit val system: ActorSystem[Nothing] = ActorSystem(Behaviors.empty, "warehouse-actor-system")

  implicit val executionContext: ExecutionContextExecutor = system.executionContext

  val httpServer = Http().newServerAt("localhost", 8080).bind(app.totalRoute)

  StdIn.readLine() // let it run until user presses return
  httpServer
    .flatMap(_.unbind()) // trigger unbinding from the port
    .onComplete(_ => system.terminate()) // and shutdown when done
}
