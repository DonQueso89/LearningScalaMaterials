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
  def size: Int
  def headOption: Option[T] = apply(0)
  def _filter(f: T => Boolean): Seq[T]
  def _map[X](f: T => X): Seq[X]
  def head: T = {
    val value = headOption
    if (value.isEmpty) {
      throw new java.util.NoSuchElementException
    }
    value.get
  }
  def tail: BaseNode[T]
}

class NullNode[T] extends BaseNode[T] {
  override def foreach(f: T => Unit): Unit = {}
  override def apply(index: Int): Option[T] = None
  override def _filter(f: T => Boolean): Seq[T] = Seq[T]()
  override def _map[X](f: T => X): Seq[X] = Seq[X]()
  override def tail: BaseNode[T] = throw new java.lang.UnsupportedOperationException
  def size: Int = 0
  override def toString: String = s""
}

class ListNode[T](elements: T*) extends BaseNode[T] {
  val value: Option[T] = elements.headOption
  val nextNode: BaseNode[T] = if (elements.tail.isEmpty) new NullNode else new ListNode[T](elements.tail:_*)

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

  override def tail(): BaseNode[T] = {
    nextNode
  }
  
  def size: Int = { 
    1 + nextNode.size
  }

  override def _filter(f: T => Boolean): Seq[T] = {
    /*
     * Ugly helper func for filter method to construct filtered Sequence
     */
    ( if (f(value.get)) Seq[T](value.get) else Seq[T]() ) ++ nextNode._filter(f)
  }

  def filter(f: T => Boolean): ListNode[T] = {
    val result: Seq[T] = ( if (f(value.get)) Seq[T](value.get) else Seq[T]() ) ++ nextNode._filter(f)
    new ListNode[T](result:_*)
  }

  override def _map[X](f: T => X): Seq[X] = {
    /*
     * Ugly helper function for map method to construct mapped Sequence
     */
    Seq[X](f(value.get)) ++ nextNode._map(f)
  }
  

  def map[X](f: T => X): ListNode[X] = {
    /*
     * Construct mapped Sequence from Sequence obtained from helper meth
     */
    new ListNode[X](_map(f):_*)
  }

  override def toString: String = {
    s"${value.get} " + nextNode.toString
  }
}
