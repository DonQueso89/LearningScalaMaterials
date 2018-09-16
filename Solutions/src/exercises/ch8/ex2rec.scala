package exercises.ch8.ex2rec;

/*
 * Create a linkedlist OOP style with recursive methods
 *
 */

abstract class BaseNode[T] {
  def value: Option[T]
  def tail: BaseNode[T]
  def head = apply(0)
  def size: Int

  // Abstract methods
  def apply(index: Int): T
  def foreach(f: T => Unit): Unit
  def map[X](f: T => X): BaseNode[X]
  def filter(f: T => Boolean): BaseNode[T]
}

class NullNode[T] extends BaseNode[T] {
  val value = Option.empty[T]
  def foreach(f: T => Unit): Unit = {}
  @throws(classOf[IndexOutOfBoundsException])
  def apply(index: Int): T =  throw new IndexOutOfBoundsException
  def filter(f: T => Boolean): BaseNode[T] = new NullNode[T]
  def map[X](f: T => X): BaseNode[X] = new NullNode[X]
  @throws(classOf[UnsupportedOperationException])
  def tail: BaseNode[T] = throw new java.lang.UnsupportedOperationException
  def size: Int = 0
  override def toString: String = s""
}

class NotNullNode[T](val value: Option[T], val tail: BaseNode[T]) extends BaseNode[T]{
  def apply(index: Int): T = {
    if (index < 1) value.get else tail(index - 1)
  }
  
  def size: Int = {
    1 + tail.size
  }

  def foreach(f: T => Unit) = {
    f(value.get)
    tail.foreach(f)
  }
  
  def map[X](f: T => X): BaseNode[X] = {
    new NotNullNode[X](Some(f(value.get)), tail.map(f))
  }

  def filter(f: T => Boolean): BaseNode[T] = {
    if (f(value.get)) {
      new NotNullNode[T](value, tail.filter(f))
    } else {
      tail.filter(f)
    }
  }

  override def toString: String = s"${value.get} " + tail.toString
}


object ListBuilder {
  /*
   *  Returns the right type of Node depending on the args passed
   *  to the constructor.
   */
  def init[T](elements: T*) = {
    var lastNode: BaseNode[T] = new NullNode[T]
    for (elem <- elements.reverse) {
      lastNode = new NotNullNode[T](Some(elem), lastNode)
    }
    lastNode
  }
}
