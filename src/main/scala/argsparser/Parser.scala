package argsparser

import scala.collection.mutable.ListBuffer

case class Param[T](description: String, cmd: String, defaultValue: T) {

  private var _value: Option[T] = None

  def value: T = _value.getOrElse(defaultValue)
  def value_=(nval:T): Unit = _value = Some(nval)
}

case class Action(description: String, cmd: String, nargs: Int = 0, task: (Array[String]) => Unit) {

  var args:Array[String] = Array()

  def this(description: String, cmd: String, task: => Unit) = this(description, cmd, 0, (args: Array[String]) => task)

  def contract: Unit = {
    require(args.size == nargs, s"The action $cmd require $nargs arguments. See help.")
  }

  def execute: Unit = task(args)
}

object Action {
  def apply(description: String, cmd: String, task: => Unit) = new Action(description, cmd, 0, (args: Array[String]) => task)
}

class Parser(help: Boolean = false) {

  private val _actions = ListBuffer[Action]()
  private val _params = ListBuffer[Param[_]]()

  val helpAction = new Action(
    cmd = "--help",
    description = "Show this help.",
    task = (args: Array[String]) => {
      println("Actions")
      println(actions.map(action => {
        println(action.cmd + " " + action.description)
      }))
      println("Params")
      println(params.map(param => {
        println(param.cmd + " " + param.description)
      }))
    }
  )
  val undefinedAction = new Action(
    cmd = "undefined",
    description = "undefined",
    task = (args: Array[String]) => {
      println("No action by default.")
    }
  )

  def actions = _actions.toList
  def params = _params.toList
  def register(obj: Action): Action = {
    _actions += obj
    obj
  }
  def register[T](obj: Param[T]): Param[T] = {
    _params += obj
    obj
  }

  private def defaultAction: Action = if (help == true) helpAction else undefinedAction

  def parse(args: Array[String], default: Action = defaultAction): (Set[Action], Set[Param[_]]) = {
    parseArgs(args, default)
  }

  private def parseArgs(args: Array[String], defaultAction: Action, actions: Set[Action] = Set(), options: Set[Param[_]] = Set()): (Set[Action], Set[Param[_]]) = {
    def addParam[T](param: Param[T]) = {
      param match {
        case p: Param[T] if p.defaultValue.isInstanceOf[String] => {
          val param = p.asInstanceOf[Param[String]]
          if (args.size < 2) throw new IllegalArgumentException(s"The param '${param.cmd}' require one value.")
          param.value = args(1)
          parseArgs(args.drop(2), defaultAction, actions, options + param)
        }
        case p:Param[T] if p.defaultValue.isInstanceOf[Boolean] => {
          val param = p.asInstanceOf[Param[Boolean]]
          param.value = !param.defaultValue
          parseArgs(args.drop(1), defaultAction, actions, options + param)
        }
      }
    }
    def addAction(action: Action) = {
      val actionArguments = args.take(action.nargs + 1).drop(1)
      action.args = actionArguments
      action.contract
      parseArgs(args.drop(1 + action.nargs), defaultAction, actions + action, options)
    }

    args match {
      case args if args.isEmpty => actions match {
        case actions if actions.isEmpty => (Set(defaultAction), options)
        case _ => (actions, options)
      }
      case _ => _params.find(_.cmd == args(0)) match {
        case Some(param) => addParam(param)
        case None => _actions.find(_.cmd == args(0)) match {
          case Some(action) => addAction(action)
          case None => throw new IllegalArgumentException(s"Action or param '${args(0)}' is not valid. See help.")
        }
      }
    }
  }
}

