package app

import algebras.ui.Presentation
import cats.implicits._
import runtime.implicits._

object Main extends App {

  val text =
    """|
       | And now here is my secret, a very simple secret:
       | It is only with the heart that one can see rightly;
       | what is essential is invisible to the eye.
       | ― Antoine de Saint-Exupéry, The Little Prince
    """.stripMargin

  import scala.util.Try
  println(Presentation[Try].onUserRequestedTags(text))

}
