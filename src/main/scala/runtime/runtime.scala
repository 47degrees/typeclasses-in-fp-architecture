package runtime

import algebras.datasource.NlpDataSource
import algebras.services.{Config, DefaultTagService, TagService}
import algebras.ui.Presentation
import algebras.usecases.{DefaultFetchTagsUseCase, FetchTagsUseCase}
import cats.{ApplicativeError, Functor, MonadError}
import runtime.datasource.TextRazorNlpDataSource
import runtime.services.SystemEnvConfig
import runtime.ui.ConsolePresentation

object implicits {
  implicit def presentation[F[_] : Functor : FetchTagsUseCase]: Presentation[F] =
    new ConsolePresentation

  implicit def useCase[F[_] : Functor : TagService]: FetchTagsUseCase[F] =
    new DefaultFetchTagsUseCase

  implicit def tagService[F[_] : Functor : NlpDataSource]: TagService[F] =
    new DefaultTagService

  implicit def dataSource[F[_]](implicit ME: MonadError[F, Throwable]): NlpDataSource[F] =
    new TextRazorNlpDataSource

  implicit def config[F[_]](implicit A: ApplicativeError[F, Throwable]): Config[F] =
    new SystemEnvConfig
}