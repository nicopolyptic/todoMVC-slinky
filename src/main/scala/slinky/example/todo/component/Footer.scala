package slinky.example.todo.component

import slinky.example.todo.model.TodoFilter
import slinky.example.todo.model.ClearCompleted
import diode.Dispatcher
import slinky.core.StatelessComponent
import slinky.core.annotations.react
import slinky.web.html._

import scala.scalajs.js.Dynamic.literal

@react class Footer extends StatelessComponent {

  case class Props(
      currentFilter: TodoFilter,
      activeCount: Int,
      completedCount: Int,
      dispatch: Dispatcher
  )

  def render() =
    footer(
      className := "footer",
      style := literal(display = if (props.activeCount + props.completedCount == 0) "none" else ""), // set #110
      )(
      span(
        className := "todo-count")(
        strong(props.activeCount.toString),
        s" ${if (props.activeCount == 1) "item" else "items"} left"
      ),
      ul(
        className := "filters")(
        TodoFilter.values.map(
          f => li(
            key := f.title)(
            a(className := s"${if (f == props.currentFilter) "selected" else "" }",
              href := s"#${f.title}")(
              f.title
            )
          )
        )
      ),
      if (props.completedCount > 0)
        button(
          className := "clear-completed",
          onClick := { _ => props.dispatch(ClearCompleted) })(
          "Clear completed"
        )
      else None
    )
}