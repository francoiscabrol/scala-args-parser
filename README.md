# scala-args-parser
Command line arguments parser

**WIP**

## Syntax

Set the actions
```
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

## Examples
See a [good example](https://github.com/francoiscabrol/gitmaster/blob/master/src/main/scala/com/francoiscabrol/gitmaster/gmaster.scala) in the gitmaster's repository and in the [unit tests](https://github.com/francoiscabrol/scala-args-parser/blob/master/src/test/scala/argsparser/ParserTest.scala).
