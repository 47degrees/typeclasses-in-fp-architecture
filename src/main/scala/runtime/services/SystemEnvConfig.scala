package runtime.services

import algebras.services.{Config, NlpApiKey}
import cats.ApplicativeError

class SystemEnvConfig[F[_]](implicit AE: ApplicativeError[F, Throwable]) extends Config[F] {
  private val key = System.getenv("NLP_API_KEY")
  def nlpApiKey: F[NlpApiKey] =
    if (key == null || key == "") AE.raiseError(new IllegalStateException("Missing nlp api key"))
    else AE.pure(NlpApiKey(key))
}
