package algebras.usecases

import algebras.services.{AnalysisRequest, Tag, TagService}
import cats.Functor
import simulacrum.typeclass
import cats.syntax.functor._

case class TaggedParagraph(text: String, tags: List[Tag])

@typeclass trait FetchTagsUseCase[F[_]] {
  def fetchTagsInText(text: String): F[TaggedParagraph]
}

class DefaultFetchTagsUseCase[F[_]: Functor: TagService] extends FetchTagsUseCase[F] {
  def fetchTagsInText(text: String): F[TaggedParagraph] =
    TagService[F].tag(AnalysisRequest(text)).map { tags =>
      TaggedParagraph(text, tags)
    }
}