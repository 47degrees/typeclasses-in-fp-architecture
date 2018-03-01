autoscale: true
build-lists: true
slidenumbers: true
footer: @raulraja @47deg

# A Tour of Functional Typeclasses #

An introduction to FP & typeclasses ilustrating
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

```scala
def transform[B](list : List[Int])(transformation : Int => B) =
