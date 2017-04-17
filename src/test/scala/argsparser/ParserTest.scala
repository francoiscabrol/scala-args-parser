package argsparser

import org.scalatest.{FlatSpec, GivenWhenThen}

class ParserTest extends FlatSpec with GivenWhenThen {
  val action1 = new Action(
    cmd = "action1",
    description = "Action 1",
    task = {
      println("Action 1 executed.")
    }
  )

  val action2 = new Action(
    cmd = "action2",
    description = "Action 2",
    task = {
      println("Action 2 executed.")
    }
  )

  val param1 = new Param[String]("Param 1", "--param1", "default value")

  "Parser" should "return the help by default" in {
    val parser = new Parser(help = true)
    parser register action1
    val (actions, _) = parser.parse(Array())
    assert(actions.head.description == "Show this help.")
  }

  it should "return the default action by default" in {
    val parser = new Parser(help = false)
    parser register action1
    val (actions, _) = parser.parse(Array(), action1)
    assert(actions.contains(action1))
  }

  it should "return the action1" in {
    val parser = new Parser(help = false)
    parser register action1
    parser register action2
    When("no default action")
    val (actions, _) = parser.parse(Array("action1"))
    assert(actions.contains(action1))
    And("with default action")
    val (actions2, _) = parser.parse(Array("action2"), action1)
    assert(actions2.contains(action2))
  }

  it should "return the param 1" in {
    val parser = new Parser(help = false)
    parser register action1
    parser register param1
    val (_, params) = parser.parse(Array("--param1", "value1"))
    assert(params.contains(param1))
  }

  it should "throw an error if the param value is not passed" in {
    val parser = new Parser(help = false)
    parser register action1
    parser register param1
    val thrown = intercept[Exception] {
      parser.parse(Array("--param1"))
    }
    assert(thrown.getMessage === "The param '--param1' require one value.")
  }
}