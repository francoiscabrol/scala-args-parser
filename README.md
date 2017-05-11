# scala-args-parser
Command line arguments parser

**WIP**

## Syntax

Simple app with a simple action (`sbt run doAction`)
```scala
object SimpleApp {
  val parser = new Parser()
  parser register new Action(
    cmd = "doAction",
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

Simple app with a parameters (`sbt run doAction --word World`)
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

## Examples
See a [good example](https://github.com/francoiscabrol/gitmaster/blob/master/src/main/scala/com/francoiscabrol/gitmaster/gmaster.scala) in the gitmaster's repository and in the [unit tests](https://github.com/francoiscabrol/scala-args-parser/blob/master/src/test/scala/argsparser/ParserTest.scala).
