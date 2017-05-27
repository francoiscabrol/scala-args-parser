package argsparser

import org.scalatest.{BeforeAndAfter, FeatureSpec, GivenWhenThen}

class Scenarios extends FeatureSpec with GivenWhenThen with BeforeAndAfter {

  val stream = new java.io.ByteArrayOutputStream()

  after {
    stream.reset()
  }

  feature("Command line arguments parser") {
    scenario("User execute one action without parameter either argument") {
      Given("a Simple app")
      object SimpleApp {
        val parser = new Parser()
        parser register new Action(cmd = "hello", description = "print Hello World", task = {
          println("Hello World.")
        })

        def apply(args: Array[String]) {
          val (actions, _) = parser.parse(args)
          actions.foreach(_.execute)
        }
      }

      When("the app is run with one action")
      Console.withOut(stream) {
        SimpleApp.apply(Array("hello"))
      }

      Then("the action 'hello' should be executed")
      assert(stream.toString == "Hello World.\n")
    }

    scenario("User execute one action with arguments") {
      Given("a Simple app with one action with one argument")
      object SimpleApp {
        val parser = new Parser()
        parser register new Action(
          cmd = "hello",
          description = "print Hello World",
          nargs = 1,
          task = (args: Array[String]) => {
            println(args(0) + " World.")
          }
        )

        def apply(args: Array[String]) {
          val (actions, _) = parser.parse(args)
          actions.foreach(_.execute)
        }
      }

      When("the app is run")
      Console.withOut(stream) {
        SimpleApp.apply(Array("hello", "Salut"))
      }

      Then("the string 'Salut World' should be printed")
      assert(stream.toString == "Salut World.\n")
    }

    scenario("User execute one action with arguments and one parameters") {
      Given("a Simple app with one action with one argument and one parameter")
      object SimpleApp {
        val parser = new Parser()
        val uppercaseParam = parser register new Param[Boolean]("Uppercase or not", "--upper|-U", false)
        parser register new Action(
          cmd = "hello",
          description = "print args(0) World",
          nargs = 1,
          task = (args: Array[String]) => {
            val str = args(0) + " World."
            println(if (uppercaseParam.value) str.toUpperCase else str)
          }
        )

        def apply(args: Array[String]) {
          val (actions, _) = parser.parse(args)
          actions.foreach(_.execute)
        }
      }

      When("the app is run")
      Console.withOut(stream) {
        SimpleApp.apply(Array("hello", "Hello", "-U"))
      }

      Then("the string 'HELLO WORLD' should be printed")
      assert(stream.toString == "HELLO WORLD.\n")
    }
  }

}
