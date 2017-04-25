package common.utils

import monix.execution.Ack.Continue
import monix.execution.{Ack, Cancelable, Scheduler}
import monix.reactive.Observable
import monix.reactive.observers.Subscriber

import scala.concurrent.Future

object RxUtils {
  implicit class RichObservable[+A](observable: Observable[A]) {

    def sub(nextFn: A => Unit): Cancelable = {

      observable.subscribe(new Subscriber[A] {
        implicit val scheduler: Scheduler = monix.execution.Scheduler.Implicits.global
        def onNext(elem: A): Continue.type = {
          nextFn(elem)
          Continue
        }
        def onComplete() = Unit
        def onError(ex: Throwable): Unit = ex.printStackTrace()
      })
    }
  }
}
