package slinky.example.todo.component

import slinky.example.todo.model._
import diode.Dispatcher
import diode.ModelRO
import slinky.core.Component
import slinky.core.annotations.react
import slinky.core.facade.ReactElement
import slinky.web.html._
import org.scalajs.dom.Event
import org.scalajs.dom.KeyboardEvent
import org.scalajs.dom.ext.KeyCode
import org.scalajs.dom.raw.HTMLInputElement

@react class TodoList extends Component {

  case class Props(todos: ModelRO[Todos], dispatch: Dispatcher)
  case class State(editing: Option[Todo.Id])

  override def initialState = State(None)

  def render () = {
    val todos                        = props.todos.value.todoList
    val filteredTodos                = todos filter props.todos.value.filter.accepts
    val activeCount                  = todos count TodoFilter.Active.accepts
    val completedCount               = todos.length - activeCount

    div(
      h1("todos"),
      header(
        className := "header")(
          input(
            className := "new-todo",
            placeholder := "What needs to be done?",
            onKeyDown := { handleNewTodoKeyDown _}
          )
      ),
      todoList(),
      footer(activeCount, completedCount)
    )
  }

  def todoList() : ReactElement = {
    section(
      className := "main")(
      input(
        `type` := "checkbox",
        className := "toggle-all",
        onChange := { e => props.dispatch(ToggleAll(e.target.asInstanceOf[HTMLInputElement].checked)) }
      ),
      ul(
        className := "todo-list")(
        props.todos.value.todoList.filter(props.todos.value.filter.accepts).map(
          todo => {
            val editing = props.todos.value.editingTodo.exists(_ == todo.id)
            div(key := todo.id.toString)(TodoView(props.dispatch, todo, editing ))
          }
        )
      )
    )
  }

  def handleNewTodoKeyDown(e : Event)  {
    val title = e.target.asInstanceOf[HTMLInputElement].value.trim
    if (e.asInstanceOf[KeyboardEvent].keyCode == KeyCode.Enter && title.nonEmpty) {
      e.target.asInstanceOf[HTMLInputElement].value = ""
      this.props.dispatch(AddTodo(title))
    }
  }

  def footer(activeCount: Int, completedCount: Int) =
    Footer(
        props.todos.value.filter,
        activeCount,
        completedCount,
        props.dispatch
    )
}
