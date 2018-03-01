package algebras.services

import simulacrum.typeclass

case class NlpApiKey(value: String)

@typeclass trait Config[F[_]] {
  def nlpApiKey: F[NlpApiKey]
}