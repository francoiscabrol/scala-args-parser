package argsparser

import org.scalatest.{FeatureSpec, GivenWhenThen}

class Scenarios extends FeatureSpec with GivenWhenThen {

  val stream = new java.io.ByteArrayOutputStream()

  feature("Command line arguments parser") {
    scenario("User execute one action with parameters") {
      Given("a Simple app")
      object SimpleApp {
        val parser = new Parser()
        parser register new Action(
          cmd = "hello",
          description = "print Hello World",
          task = {
            println("Hello World.")
          }
        )

        def apply(args: Array[String]) {
          val (actions, _) = parser.parse(args)
          actions.foreach(_.execute)
        }
      }

      When("the app is executed with one argument")
      Console.withOut(stream) {
        SimpleApp.apply(Array("hello"))
      }

      Then("the action 'hello' should be executed")
      assert(stream.toString == "Hello World.\n")

    }
  }

}
