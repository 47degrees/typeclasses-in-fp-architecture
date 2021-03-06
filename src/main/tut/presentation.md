autoscale: true
build-lists: true
slidenumbers: true
footer: @raulraja @47deg

# [fit] Type classes & FP Λrchitecture

![filtered](romanesco.jpg)

---

# Typeclasses in FP Architecture #

An introduction to FP & type classes illustrating
the power of coding to abstractions and Tagless Final Architectures

[@raulraja](https://twitter.com/raulraja)
[@47deg](https://twitter.com/47deg)
[Interactive](http://github.com/47deg/typeclasses-tour)
[Presentation](https://speakerdeck.com/raulraja/typeclasses-tour)

---

## What is Functional Programming ##

> In computer science, functional programming
> is a programming paradigm.

> A style of building the structure and elements
> of computer programs that treats computation
> as the evaluation of mathematical functions
> and avoids changing-state and mutable data.

-- Wikipedia

---

## Common traits of Functional Programming ##

- Higher-order functions
- Immutable data
- Referential transparency
- Lazy evaluation
- Recursion
- Abstractions

---

### Higher Order Functions ##

When a functions takes another function as argument
or returns a function as return type:

```tut:silent
def transform[B](list : List[Int])(transformation : Int => B) =
  list map transformation

transform(List(1, 2, 4))(x => x * 10)
```

---

## Inmutable data ##

Once a value is instantiated it can't be mutated in place.
How can we change it's content then?

```tut:silent
case class Conference(name : String)
```

---

## Referential Transparency ##

When a computation returns the same value
each time is invoked

Transparent :

```tut:silent
def pureAdd(x : Int, y : Int) = x + y
```

Opaque :

```tut:silent
var x = 0
def impureAdd(y : Int) = x += y; x
```

---

## Lazy Evaluation ##

When a computation is evaluated
only if needed

```tut:silent
import scala.util.Try

def boom = throw new RuntimeException

def strictEval(f : Any) = Try(f)

def lazyEval(f : => Any) = Try(f)
```

---

## Recursion ##

Recursion is favored over iteration

```tut:silent
def reduceIterative(list : List[Int]) : Int = {
  var acc = 0
  for (i <- list) acc = acc + i
  acc
}
```

---

## Recursion ##

Recursion is favored over iteration

```tut:silent
def reduceRecursive(list : List[Int], acc : Int = 0) : Int =
  list match {
    case Nil => acc
    case head :: tail => reduceRecursive(tail, head + acc)
  }
```

---

## Abstractions ##

> Each significant piece of functionality in a program
> should be implemented in just one place in the source code.

-- Benjamin C. Pierce in Types and Programming Languages (2002)

---

## What is a Typeclass ##

A typeclass is an interface/protocol that provides
a behavior for a given data type.

This is also known as **Ad-hoc Polymorphism**

We will learn typeclasses by example...

---

## Typeclasses ##

[ ] **Monoid** : Combine values of the same type
[ ] **Functor** : Transform values inside contexts

---

## Monoid ##

A `Monoid` expresses the ability of a value of a
type to `combine` itself with other values of the same type
in addition it provides an `empty` value.

```tut:silent
import simulacrum._

@typeclass trait Monoid[A] {
  def combine(x : A, y : A) : A
  def empty : A
}
```

---

## Monoid ##

```tut:silent
implicit val IntAddMonoid = new Monoid[Int] {
  def combine(x : Int, y : Int) : Int = ???
  def empty = ???
}
```

---

## Monoid ##

```tut:silent
implicit val IntAddMonoid = new Monoid[Int] {
  def combine(x : Int, y : Int) : Int = x + y
  def empty = 0
}
```

---

## Monoid ##

```tut:silent
implicit val StringConcatMonoid = new Monoid[String] {
  def combine(x : String, y : String) : String = x + y
  def empty = ""
}
```

---

## Monoid ##

```tut:silent
implicit def ListConcatMonoid[A] = new Monoid[List[A]] {
  def combine(x : List[A], y : List[A]) : List[A] = x ++ y
  def empty = Nil
}
```

---

## Monoid ##

We can code to abstractions instead of coding to concrete
types.

```tut:silent
def uberCombine[A : Monoid](x : A, y : A) : A =
  Monoid[A].combine(x, y)

uberCombine(10, 10)
```

---

## Typeclasses ##

[x] **Monoid** : Combine values of the same type
[ ] **Functor** : Transform values inside contexts

---

## Functor ##

A `Functor` expresses the ability of a container
to transform its content given a function

```tut:silent
@typeclass trait Functor[F[_]] {
  def map[A, B](fa : F[A])(f : A => B) : F[B]
}
```

---

## Functor ##

Most containers transformations can be expressed as
Functors.

```tut:silent
implicit def ListFunctor = new Functor[List] {
  def map[A, B](fa : List[A])(f : A => B) = fa map f
}
```

---

## Functor ##

Most containers transformations can be expressed as
Functors.

```tut:silent
implicit def OptionFunctor = new Functor[Option] {
  def map[A, B](fa : Option[A])(f : A => B) = fa map f
}
```

---

## Functor ##

Most containers transformations can be expressed as
Functors.

```tut:silent
import scala.concurrent.{Future, Await}
import scala.concurrent.duration._
import scala.concurrent.ExecutionContext.Implicits.global

implicit def FutureFunctor = new Functor[Future] {
  def map[A, B](fa : Future[A])(f : A => B) = fa map f
}
```

---

## Functor ##

We can code to abstractions instead of coding to concrete
types.

```tut:silent
def uberMap[F[_] : Functor, A, B](fa : F[A])(f : A => B) : F[B] =
  Functor[F].map(fa)(f)

uberMap(List(1, 2, 3))(x => x * 2)
```

---

## Typeclasses ##

[x] **Monoid** : Combine values of the same type
[x] **Functor** : Transform values inside contexts

---

## Typeclasses ##

Can we combine multiple
abstractions & behaviors?

---

# What are our application layers?

```
├── algebras
│   ├── datasource
│   │   └── NlpDataSource.scala
│   ├── services
│   │   ├── Config.scala
│   │   └── TagService.scala
│   ├── ui
│   │   └── Presentation.scala
│   └── usecases
│       └── FetchTagsUseCase.scala
├── app
│   └── main.scala
└── runtime
    ├── datasource
    │   └── TextRazorNlpDataSource.scala
    ├── runtime.scala
    ├── services
    │   └── SystemEnvConfig.scala
    └── ui
        └── ConsolePresentation.scala
```

---

# What are our application layers?

Presentation

```tut:silent
import simulacrum._
@typeclass trait Presentation[F[_]] {
  def onUserRequestedTags(text: String): F[Unit]
}
```

---

# What are our application layers?

Use Case

```tut:silent
case class Tag(value: String)
case class TaggedParagraph(text: String, tags: List[Tag])

@typeclass trait FetchTagsUseCase[F[_]] {
  def fetchTagsInText(text: String): F[TaggedParagraph]
}
```

---

# What are our application layers?

Services

```tut:silent
case class AnalysisRequest(text: String)

@typeclass trait TagService[F[_]] {
  def tag(request: AnalysisRequest): F[List[Tag]]
}
```

---

# What are our application layers?

Data Source

```tut:silent
case class Category(value: String)
case class Entity(value: String)
case class Topic(value: String)
case class AnalysisResponse(
  categories: List[Category],
  entities: List[Entity],
  topics: List[Topic]
)

@typeclass trait NlpDataSource[F[_]] {
  def analyze(text: String): F[AnalysisResponse]
}
```

---

# What are our application layers?

Configuration

```tut:silent
case class NlpApiKey(value: String)

@typeclass trait Config[F[_]] {
  def nlpApiKey: F[NlpApiKey]
}
```

---

# Implementation

Presentation

```tut:silent
import cats._
import cats.Functor
import cats.implicits._

class ConsolePresentation[F[_]: Functor: FetchTagsUseCase] extends Presentation[F] {
  def onUserRequestedTags(text: String): F[Unit] =
    FetchTagsUseCase[F].fetchTagsInText(text).map { paragraph =>
      println(paragraph.tags.mkString(", "))
    }
}
```

---

# Implementation?

Use case

```tut:silent
class DefaultFetchTagsUseCase[F[_]: Functor: TagService] extends FetchTagsUseCase[F] {
  def fetchTagsInText(text: String): F[TaggedParagraph] =
    TagService[F].tag(AnalysisRequest(text)).map { tags =>
      TaggedParagraph(text, tags)
    }
}
```

---

# Implementation?

Tag Service

```tut:silent
class DefaultTagService[F[_]: Functor: NlpDataSource] extends TagService[F] {
  def tag(request: AnalysisRequest): F[List[Tag]] =
    NlpDataSource[F].analyze(request.text).map { r =>
      (r.categories, r.entities, r.topics).mapN { case (category, entity, topic) =>
        List(Tag(category.value), Tag(entity.value), Tag(topic.value))
      }.flatten.distinct
    }
}
```

---

# Implementation?

Config

```tut:silent
class SystemEnvConfig[F[_]](implicit AE: ApplicativeError[F, Throwable]) extends Config[F] {
  val key = System.getenv("NLP_API_KEY")
  def nlpApiKey: F[NlpApiKey] =
    if (key == null || key == "")
      AE.raiseError(new IllegalStateException("Missing nlp api key"))
    else
      AE.pure(NlpApiKey(key))
}
```

---

# Implementation?

Data Source

```tut:silent
import collection.JavaConverters._
import scala.concurrent._
import java.util.Arrays
import com.textrazor.TextRazor

object TextRazorClient {
  def client(apiKey: NlpApiKey) : TextRazor = {
    val c = new TextRazor(apiKey.value)
    c.addExtractor("entities")
    c.addExtractor("topics")
    c.setClassifiers(Arrays.asList("textrazor_newscodes"))
    c
  }
}
```

---

# Implementation?

Data Source

```tut:silent
class TextRazorNlpDataSource[F[_]: Config](implicit ME: MonadError[F, Throwable]) extends NlpDataSource[F] {
   def analyze(text: String): F[AnalysisResponse] =
    for {
      apiKey <- Config[F].nlpApiKey
      response <- ME.catchNonFatal {
         val r = blocking {
           TextRazorClient.client(apiKey).analyze(text).getResponse
         }
         AnalysisResponse(
           r.getCategories.asScala.toList.map { c => Category(c.getLabel) },
           r.getEntities.asScala.toList.map { e => Entity(e.getEntityId) },
           r.getTopics.asScala.toList.map { t => Topic(t.getLabel) }
         )
      }
    } yield response
}
```

---

# Implementation?

Runtime Module

```tut:silent
object runtime {
  implicit def presentation[F[_]: Functor: FetchTagsUseCase]: Presentation[F] =
    new ConsolePresentation
  implicit def useCase[F[_]: Functor: TagService] : FetchTagsUseCase[F] =
    new DefaultFetchTagsUseCase
  implicit def tagService[F[_]: Functor: NlpDataSource] : TagService[F] =
    new DefaultTagService
  implicit def dataSource[F[_]: Config]
    (implicit ME: MonadError[F, Throwable]) : NlpDataSource[F] =
      new TextRazorNlpDataSource
  implicit def config[F[_]](implicit A: ApplicativeError[F, Throwable]): Config[F] =
    new SystemEnvConfig
}
```

---

# Implementation?

Application

```tut:silent
import runtime._
import scala.util.Try

val text =
    """|
       | And now here is my secret, a very simple secret:
       | It is only with the heart that one can see rightly;
       | what is essential is invisible to the eye.
       | ― Antoine de Saint-Exupéry, The Little Prince
       """.stripMargin

Presentation[Try].onUserRequestedTags(text)
```

---

## Pros ##

- Pure Applications and Libraries
- Testing flexibility
- Controlled Effects at the edge
- Separation of concerns on steroids
- A unified model to create components and compose them
- A unified API to rule all data types when using just type classes.
- Restricted to thoroughly tested lawful declarations with a base on mathematics

---

## Cons ##

- Requires understanding type classes and higher kinds
- Potential peer and status quo push-back

---

## Questions? & Thanks! ##

@raulraja
@47deg
http://github.com/47deg/typeclasses-in-fp-architecture
https://speakerdeck.com/raulraja/typeclasses-in-fp-architecture