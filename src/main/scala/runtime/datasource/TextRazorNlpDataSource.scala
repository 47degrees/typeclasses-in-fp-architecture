package runtime.datasource

import java.util

import algebras.datasource._
import algebras.services.{Config, NlpApiKey}
import cats.MonadError
import cats.syntax.flatMap._
import cats.syntax.functor._
import com.textrazor.TextRazor

import scala.collection.JavaConverters._
import scala.concurrent._

class TextRazorNlpDataSource[F[_] : Config](implicit errorHandler: MonadError[F, Throwable]) extends NlpDataSource[F] {

  private def client(apiKey: NlpApiKey): TextRazor = {
    val client = new TextRazor(apiKey.value)
    client.addExtractor("entities")
    client.addExtractor("topics")
    client.setClassifiers(util.Arrays.asList("textrazor_newscodes"))
    client
  }

  def analyze(text: String): F[AnalysisResponse] =
    for {
      apiKey <- Config[F].nlpApiKey
      response <- errorHandler.catchNonFatal {
        val txtRazorRes = blocking {
          client(apiKey).analyze(text).getResponse
        }
        AnalysisResponse(
          txtRazorRes.getCategories.asScala.toList.map { c => Category(c.getLabel) },
          txtRazorRes.getEntities.asScala.toList.map { e => Entity(e.getEntityId) },
          txtRazorRes.getTopics.asScala.toList.map { t => Topic(t.getLabel) }
        )
      }
    } yield response
}
