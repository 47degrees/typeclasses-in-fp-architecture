package runtime.ui

import algebras.ui.Presentation
import algebras.usecases.FetchTagsUseCase
import cats._
import cats.implicits._

class ConsolePresentation[F[_]: Functor: FetchTagsUseCase] extends Presentation[F] {
  def onUserRequestedTags(text: String): F[Unit] =
    FetchTagsUseCase[F].fetchTagsInText(text).map { paragraph =>
      println(paragraph.tags.mkString(", "))
    }
}
