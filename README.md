# MDC async

This project contains code that was shamelessly copied from 
[here](http://yanns.github.io/blog/2014/05/04/slf4j-mapped-diagnostic-context-mdc-with-play-framework/) so
that it can easily be reused as a dependency.

Some slight alterations have been done (i.e. the signatures for MDC
context are `java.util.Map[String, String]` instead of `java.util.Map[_, _]` due to recent changes in slf4j
and `MDCHttpExecutionContext` has been renamed to `MDCExecutionContext`)
but otherwise its exactly the same

## Installation

The library can be found here

```sbt
"org.mdedetrich" %% "mdc-async" % "0.1"
```
Currently its published for both Scala 2.11.8

## Usage

The main idea of the library is to be able to use slf4j MDC in asynchronous environments. This library contains two
different approaches to solving this problem, one is by using a Akka Dispatcher
and the other is by using custom execution context (which you need to use when you are doing your logging)

### Akka Dispatcher

To use the Akka dispatcher variant, you need to provide a global dispatcher. If you are using Play, then you
need to do something like the following

```conf
play {
  akka {
    actor {
      default-dispatcher = {
        type = "monitoring.MDCPropagatingDispatcherConfigurator"
      }
    }
  }
}
```

If you are not using Play, then you simple need to provide this dispatcher in context for your Akka application.
Note that as stated [here](https://github.com/jroper/thread-local-context-propagation#thread-local-context-propagation-example)
this can have performance impliciations, quoting directly from
[@jroper](https://github.com/jroper)

> I don't recommend doing this. It has performance implications, and in an asynchronous application,
> it can be very difficult to track what is done on what thread - you don't want to have to worry
> about that, you want the framework to worry about that for you, but transferring state on thread
> locals forces you to always have this implicit requirement in your mind as you code. In my opinion,
> this just isn't worth the pain. Pass state/context explicitly, then everything becomes very easy to understand,
> test, and reason about.


### MDC Execution Context
Another solution is to use a custom MDC execution context which propagates a specified `ExecutionContext`. You can find the execution
context in `scala.concurrent.MDCExecutionContext`. This execution context delegates from an already existing execution
context, so if you are using Play (for example) you would do

```scala
package scala.concurrent

import scala.concurrent.ExecutionContext

/**
 * The standard [[play.api.libs.concurrent.Execution.defaultContext]] loses the MDC context.
 *
 * This custom [[ExecutionContext]] propagates the MDC context, so that the request
 * and the correlation IDs can be logged.
 */
object Execution {

  object Implicits {
    implicit def defaultContext: ExecutionContext = Execution.defaultContext
  }

  def defaultContext: ExecutionContext = MDCExecutionContext.fromThread(play.api.libs.concurrent.Execution.defaultContext)

}
```

Otherwise you could just the default `ExecutionContext`, i.e.  `scala.concurrent.ExecutionContext.Implicits.global`


## Dependencies

Currently all of the dependencies are set as provided, so you need to provide the actual dependency yourself. The
dependencies

* [Logback](http://logback.qos.ch/) 
* [Akka](http://akka.io/) Needed for the Akka Dispatcher
* [Typesafe Config](https://github.com/typesafehub/config) Needed for reading configuration values
