package algebras.ui

import simulacrum.typeclass

@typeclass trait Presentation[F[_]] {
  def onUserRequestedTags(text: String): F[Unit]
}
