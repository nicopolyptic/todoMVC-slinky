package slinky.example.todo.component

import slinky.example.todo.model.Todo
import slinky.example.todo.model.ToggleCompleted
import slinky.example.todo.model.Update
import slinky.example.todo.model.Delete
import slinky.example.todo.model.StartEditingTodo
import slinky.example.todo.model.EditingDone
import diode.Dispatcher
import slinky.core.Component
import slinky.core.annotations.react
import slinky.web.html._
import org.scalajs.dom.Event
import org.scalajs.dom.KeyboardEvent
import org.scalajs.dom.ext.KeyCode
import org.scalajs.dom.raw.HTMLInputElement

@react class TodoView extends Component {

  case class Props(dispatch: Dispatcher, todo: Todo, editing: Boolean)
  case class State(editText: String)

  override def initialState = State(props.todo.title)
  def render() = {
    li(
      key := props.todo.id.toString,
      className := s"${if (props.editing) "editing" else ""} ${if (props.todo.isCompleted) "completed" else ""}")(
      div(
        className := "view")(
        input(
          `type` := "checkbox",
          className := "toggle",
          checked := props.todo.isCompleted,
          onChange := {_ => props.dispatch(ToggleCompleted(props.todo.id))}
        ),
        label(
          onDoubleClick := {_ => props.dispatch(StartEditingTodo(props.todo.id))})(
          props.todo.title
        ),
        button(
          className := "destroy",
          onClick := {_ => props.dispatch(Delete(props.todo.id))}
        )
      ),
      input(
        className := "edit",
        onBlur := {_ => editFieldSubmit()},
        onChange := {e => setState(State(e.target.asInstanceOf[HTMLInputElement].value))},
        onKeyDown := { editFieldKeyDown _ },
        value := state.editText
      )
    )
  }

  def editFieldSubmit() = {
    if (state.editText.trim == "")
      props.dispatch(Delete(props.todo.id))
    else
      props.dispatch(Update(props.todo.id, state.editText.trim))
      props.dispatch(EditingDone(props.todo.id))
  }

  def editFieldKeyDown(e: Event) {
    e.asInstanceOf[KeyboardEvent].keyCode match {
      case KeyCode.Escape => setState(State(props.todo.title))
                             props.dispatch(EditingDone(props.todo.id))
      case KeyCode.Enter => editFieldSubmit()
      case _ =>
    }
  }
}

