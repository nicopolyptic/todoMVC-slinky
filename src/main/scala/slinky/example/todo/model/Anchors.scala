package slinky.example.todo.model

import diode.Dispatcher
import org.scalajs.dom
import org.scalajs.dom.HashChangeEvent

object Anchors {
  def init(dispatch: Dispatcher): Unit = {
    dom.window.onhashchange = (e: HashChangeEvent) => {
      dispatchFilter(dispatch)
    }
  }

  private def dispatchFilter(dispatch: Dispatcher) = {
    val hash = dom.window.location.hash
    if (hash.length() >= 1) {
      val f = TodoFilter.from(hash.substring(1))
      dispatch(SelectFilter(f))
    }
  }
}
