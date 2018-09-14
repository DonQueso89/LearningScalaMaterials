package exercises.ch8.ex2

/*
 * Create a linked list OOP style
 */

class Node[T](elements: T*) {
  val (value, nextNode) = if (elements.isEmpty) (Option.empty, null) else (Option(elements.head), new Node[T](elements.tail:_*))

  override def toString: String = {
    s"<Node - value: $value >"
  }

  def foreach(f: T => Unit) {
    if (!value.isEmpty) {
      f(value.get)
      nextNode.foreach(f)
    }
  }
}

/*
 * Reimplement the LinkedList as an abstract class.
 */ 

abstract class BaseNode[T] {
  def foreach(f: T => Unit): Unit
  def apply(index: Int): Option[T]
  def headOption: Option[T] = apply(0)
}

class NullNode[T] extends BaseNode[T] {
  override def foreach(f: T => Unit): Unit = {}
  override def apply(index: Int): Option[T] = None
}

class ListNode[T](elements: T*) extends BaseNode[T] {
  val value: Option[T] = elements.headOption
  val nextNode = if (elements.tail.isEmpty) new NullNode else new ListNode[T](elements.tail:_*)
  def apply(index: Int): Option[T] = {
    if (index < 1) {
      value
    } else {
      nextNode(index - 1)
    }
  }

  def foreach(f: T => Unit): Unit = {
    nextNode match {
      case node: ListNode[T] => f(value.get); nextNode.foreach(f);
      case _ => f(value.get);
    }
  }
}
