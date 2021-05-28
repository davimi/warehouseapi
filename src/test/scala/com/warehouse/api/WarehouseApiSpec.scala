package com.warehouse.api

import akka.http.scaladsl.model.StatusCodes._
import akka.http.scaladsl.testkit.{RouteTestTimeout, ScalatestRouteTest}
import com.warehouse.domain._
import com.warehouse.service.WarehouseService
import org.scalatest.flatspec.AnyFlatSpec
import org.scalatest.matchers.should.Matchers

import java.util.concurrent.TimeUnit
import scala.concurrent.duration.Duration
import scala.util.{Success, Try}


class WarehouseApiSpec extends AnyFlatSpec with Matchers with ScalatestRouteTest {

  class TestWarehouseService extends WarehouseService {

    private val chair = Product("nice chair", List(Article("1", 1)))

    override def getAllProducts: Seq[Product] = List(chair)

    override def sellProduct(productId: Int, quantity: Int): Try[Product] = Success(chair)

    override def getInventory: Inventory = Inventory(Seq.empty)
  }

  val api = new WarehouseApiImplementaion(new TestWarehouseService)

  private val duration = Duration(3, TimeUnit.SECONDS)
  implicit val routeTestTimeout = RouteTestTimeout(duration)


  "The API" should "provide an endpoints of all products" in {
      import scala.concurrent.ExecutionContext.global
      Get("/warehouse/products") ~> api.allProductsRoute ~> check {
        status shouldBe OK
      }
    }


  "The API" should "provide an endpoint to sell products" in {
      Post("/warehouse/product/123") ~> api.productSellRoute ~> check {
        status shouldBe OK
      }
  }

}
