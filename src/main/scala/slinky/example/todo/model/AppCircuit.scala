package slinky.example.todo.model

import diode._

object AppCircuit extends Circuit[AppModel] {

  def initialModel = AppModel(Todos(Seq(), None, TodoFilter.All))

  override val actionHandler = foldHandlers(
    new TodoHandler(zoomTo(_.todos.todoList)),
    new EditTodoHandler(zoomTo(_.todos.editingTodo)),
    new FilterHandler(zoomTo(_.todos.filter)),
  )
}

class FilterHandler[M](modelRW: ModelRW[M, TodoFilter]) extends ActionHandler(modelRW) {
  override protected def handle  = {
    case SelectFilter(f) => updated(f)
    case ClearCompleted => updated(TodoFilter.All)
  }
}

class EditTodoHandler[M](modelRW: ModelRW[M, Option[Todo.Id]]) extends ActionHandler(modelRW) {
  override protected def handle  = {
    case StartEditingTodo(id) => updated(Some(id))
    case EditingDone(id) => updated(None)
  }
}

class TodoHandler[M](modelRW: ModelRW[M, Seq[Todo]]) extends ActionHandler(modelRW) {

  def updateOne(Id: Todo.Id)(f: Todo => Todo): Seq[Todo] =
    value.map {
      case found @ Todo(Id, _, _) => f(found)
      case other                  => other
    }

  override def handle = {
    case InitTodos =>
      updated(List(Todo(Todo.randomId, "Test your code!", false)))
    case AddTodo(title) =>
      updated(value :+ Todo(Todo.randomId, title, false))
    case ToggleAll(checked) =>
      updated(value.map(_.copy(isCompleted = checked)))
    case ToggleCompleted(id) =>
      updated(updateOne(id)(old => old.copy(isCompleted = !old.isCompleted)))
    case Update(id, title) =>
      updated(updateOne(id)(_.copy(title = title)))
    case Delete(id) =>
      updated(value.filterNot(_.id == id))
    case ClearCompleted =>
      updated(value.filterNot(_.isCompleted))
  }
}
