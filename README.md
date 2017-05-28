# scala-args-parser
Command line arguments parser

**This library is a snapshot but a release is coming. Currently, I support the JVM. But I did some tests and it can be compiled for Scala-native, The only thing to do is to configure the sbt-crossproject** things.**

## Get Started

1. Add the library in the build.sbt
```
  resolvers += "Sonatype OSS Snapshots" at "https://oss.sonatype.org/content/repositories/snapshots"
  
  libraryDependencies += "io.github.francoiscabrol" %%% "scala-args-parser" % "0.1-SNAPSHOT"
```

2. Import the classes
```scala
   import argsparser._
```

3. Initialize the parser
```scala
   val parser = new Parser()
```

4. Register an action
```scala
  parser register new Action(
    cmd = "hello",
    description = "print Hello World",
    task = {
      println("Hello World.")
    }
  )
```

5. Parse your application arguments in the main method and execute the actions
```scala
    val (actions, _) = parser.parse(args)
    actions.foreach(_.execute)
```

6. Compile and run you app `sbt run hello`

## Examples

1. Simple app with a simple action (`sbt run hello`)
[See it running on Scastie](https://scastie.scala-lang.org/francoiscabrol/UtRWLlPWTOWB8GgiKmM34w/0)

```scala
object SimpleApp {
  val parser = new Parser()
  parser register new Action(
    cmd = "hello",
    description = "print Hello World",
    task = {
      println("Hello World.")
    }
  )
  
  def main(args: Array[String]) { 
    val (actions, _) = parser.parse(args)
    actions.foreach(_.execute)
  }
}
```

2. Simple app with a parameters (`sbt run doAction --word World`)
```scala
object SimpleApp {
  val parser = new Parser()
  val P1 = parser register new Param[String](
    cmd = "--word",
    description = "word to print",
    defaultValue = "Not specified"
  )
  parser register new Action(
    cmd = "doAction",
    description = "print Hello World",
    task = {
      println(s"Hello " + P1.value + ".")
    }
  )
  
  def main(args: Array[String]) { 
    val (actions, _) = parser.parse(args)
    actions.foreach(_.execute)
  }
}
```

3. The best examples are written as functional tests in the [the Scenarios.scala file](https://github.com/francoiscabrol/scala-args-parser/blob/master/src/test/scala/argsparser/Scenarios.scala)

4. See also a [good example](https://github.com/francoiscabrol/gitmaster/blob/master/src/main/scala/com/francoiscabrol/gitmaster/gmaster.scala) in the gitmaster's repository.
 
5. It can be also useful to check the Parser's [unit tests](https://github.com/francoiscabrol/scala-args-parser/blob/master/src/test/scala/argsparser/ParserTest.scala).


## Unit tests
Run the unit tests with `sbt test`
