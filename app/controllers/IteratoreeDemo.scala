package controllers

import scala.concurrent._
import play.api.libs.concurrent.Execution.Implicits.defaultContext
import play.api._
import play.api.mvc.{Cookie, _}
import play.api.data._
import play.api.data.Forms._
import play.api.Play.current

import play.api.libs.Files.TemporaryFile
import play.api.libs.iteratee._
import play.api.mvc.Cookie
import play.api.mvc.MultipartFormData.FilePart

import scala.trace._
import scala.annotation.tailrec


/**
  * Created by johnreed on 7/24/16.
  */
object IteratoreeDemo {


  val consumeOneInputAndEventuallyReturnIt2: Iteratee[String, Int] = {
    Cont[String,Int](in => Done(100,Input.Empty))
  }

  val futResult2: Future[Int] = consumeOneInputAndEventuallyReturnIt2.fold[Int](folder)


  val inputLength: Iteratee[Array[Byte],Int] = {
    Iteratee.fold[Array[Byte],Int](0) { (length, bytes) => length + bytes.size }
  }

  val concatenator: Iteratee[String,String] = {
    Iteratee.fold[String,String]("") { (result, chunk) => result ++ chunk }
  }

  val printlnIteratee = Iteratee.foreach[String](s => println(s))

  val consumeOneInputAndEventuallyReturnIt: Iteratee[String, Int] = new Iteratee[String, Int] {

    def fold[B](folder: Step[String, Int] => Future[B])(implicit ec: ExecutionContext): Future[B] = {
      Debug.err("Fold")
      folder(Step.Cont {
        case Input.EOF => Done(0, Input.EOF) //Assuming 0 for default value
        case Input.Empty => System.exit(-2); this
        case Input.El(e) => System.exit(-3); Done(e.toInt, Input.EOF)
      })
    }
  }

  def folder(step: Step[String, Int]): Future[Int] = step match {
    case Step.Done(a, _) => System.exit(-1); future(a-1)

    case Step.Cont(cont) => {
      Macro.codeErr("Folding " + cont)
      cont(Input.EOF).fold({
        case Step.Done(a1, _) => Future.successful(a1 + 7) // this is what gets returned
        case _ => throw new Exception("Erroneous or diverging iteratee")
      })
    }
    case _ => throw new Exception("Erroneous iteratee")
  }

  def main(args: Array[String]) {
    val result: Future[Int] = consumeOneInputAndEventuallyReturnIt.fold[Int](folder)

    import scala.concurrent.duration._

    val myInt: Int = Await.result(result, 5.second) // scala.concurrent.duration

    // val result2 = Await.result(futResult2, 5.second)
    // Macro.checkCode(result2 == 107)

    Macro.codeErr("Done: " + myInt)
  }

}