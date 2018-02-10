package slinky.example

import slinky.hot
import slinky.web.ReactDOM
import org.scalajs.dom
import slinky.example.todo.model.{Anchors, AppCircuit}
import slinky.example.todo.component.TodoList
import scala.scalajs.js.annotation.{JSExportTopLevel, JSImport}
import scala.scalajs.{LinkingInfo, js}


@JSImport("resources/todomvc-app.css", JSImport.Default)
@js.native
object IndexCSS extends js.Object

object Main {
  val css = IndexCSS

  @JSExportTopLevel("entrypoint.main")
  def main(): Unit = {
    if (LinkingInfo.developmentMode) {
      hot.initialize()
    }
    val model = AppCircuit.zoom(_.todos)

    val todos = TodoList(model, AppCircuit)

    val container = dom.document.getElementById("todoapp")
    ReactDOM.render(todos, container)

    Anchors.init(AppCircuit)

    AppCircuit.subscribe(model)(root => {
      ReactDOM.render(todos, container)
    })
  }
}