package algebras.services

import algebras.datasource.NlpDataSource
import cats.Functor
import simulacrum.typeclass
import cats.syntax.functor._
import cats.syntax.apply._
import cats.instances.list._

case class AnalysisRequest(text: String)

case class Tag(value: String)

@typeclass trait TagService[F[_]] {
  def tag(request: AnalysisRequest): F[List[Tag]]
}

class DefaultTagService[F[_] : Functor : NlpDataSource] extends TagService[F] {
  def tag(request: AnalysisRequest): F[List[Tag]] =
    NlpDataSource[F].analyze(request.text).map { response =>
      (response.categories, response.entities, response.topics).mapN { case (category, entity, topic) =>
        List(Tag(category.value), Tag(entity.value), Tag(topic.value))
      }.flatten.distinct
    }
}