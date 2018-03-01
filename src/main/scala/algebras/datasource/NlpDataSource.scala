package algebras.datasource

import simulacrum.typeclass

case class Category(value: String)

case class Entity(value: String)

case class Topic(value: String)

case class AnalysisResponse(categories: List[Category], entities: List[Entity], topics: List[Topic])

@typeclass trait NlpDataSource[F[_]] {
  def analyze(text: String): F[AnalysisResponse]
}
