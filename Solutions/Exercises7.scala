/*
 * List, Set (unordered), Map (unordered) are immutable subtypes of Iterable.
 * Data structures that are rigid and never change state are safer to use in
 * concurrent code. Data structures that are used outside of a concurrency context
 * can be mutable safely.
 * 
 * collection.immutable.List --> collection.mutable.Buffer
 * collection.immutable.Set --> collection.mutable.Set
 * collection.immutable.Map --> collection.mutable.Map
 *
 * mutable package is not automatically added to the Scala namespace.
 * 
 * Builder is a simplified form of a Buffer restricted to generating assigned collection
 * type and supporting append only operations. A Builder knows its immutable counterpart
 * (retrieved with the 'result' attribute).
 *
 * Array: a fixed size, indexed collection with mutable content. It does not extend from
 * Iterable. The REPL can print an Array but println just calls the toString method on the
 * object.
 *
 * The presence of Arrays is only for JVM compatibility and should only be used
 * for JVM code.
 * 
 * Seq is the root type of all sequences. It serves as an alias for List.
 *             |-------------Seq-----------| 
 *     |--IndexedSeq--|           |-----LinearSeq-----|  
 *  Vector            Range       |         |         |
 *                          Queue/Stack   List      Stream 
 *                                          |
 *                                         Nil
 * Similarly, IndexedSeq is an alias for Vector.
 *
 * String type is an immutable collection extending from Iterable.
 *
 * Streams build itself as the elements are accessed. Generated elements are cached
 * and ensured to generatd only once. They can be terminated with Stream.Empty or
 * List.Nil.
 *
 * Monadic collections: 
 * 
 * Option (extends Iterable), represents the presence or absence of a single value.
 * Option relies on two of its subtypes: Some and None. Functions that return an Option
 * type signal their callers that they may not be able to retrieve a return value.
 *
 * Try either returns the result or the error of the underlying function.
 * The possible returned subclasses are Success or Failure.
 * Success contains the value of the attempted function.
 * Failure contains the Exception thrown by the underlying function.
 *
 * Future: A monadic collection holding a potential return value of some task
 * that is executing in a separate thread. (and therefore a monitor of that thread).
 *
 */
import collection.mutable.Builder
import collection.mutable.Buffer
import java.io.File

@annotation.tailrec
def fibBuilder(x: Int, result: Builder[Int, List[Int]]): List[Int] = {
  /*
   * Return a list of the first x elements of the Fibonacci sequence using a Builder
   */
  if (result.result.size == 0) {
    result ++= List(1, 1)
  }
  if (result.result.size == x) {
    // base case
    result.result
  } else {
    // recursive case
    val tmp = result.result.reverse
    result += (tmp(0) + tmp(1))
    fibBuilder(x, result)
  }
}

def fibBuffer(x: Int): List[Int] = {
  /*
   * Return a list of the first x elements of the Fibonacci sequence using a Buffer
   */
  val result = Buffer[Int](1, 1);
  for (_ <- 2 until x) {
    result.prepend(result(0) + result(1))
  }
  result.reverse.toList
}

@annotation.tailrec
def fibAppendImmutable(x: Int, result: List[Int]): List[Int] = {
  /*
   * Return the list with x fibonacci numbers appended using only immutable
   * datastructures
   */
  if (x == 0) {
    result
  } else {
    val tmp = result.reverse
    fibAppendImmutable(x - 1, result :+ (tmp(0) + tmp(1)))
  }
}

def fibReport(x: Int): Unit = {
  /*
   * Use a stream to print a report of x comma separated fibonacci numbers
   * with 10 numbers on each line.
   */
  def fibStream(head: Int, last: Int): Stream[Int] = {
    if (head == 1) {
      Stream.cons(1, fibStream(head + 1, head))
    } else {
      Stream.cons(head, fibStream(head + last, head))
    }
  }

  val stream = fibStream(1, 1)
  var c = x - 1;
  for (i <- stream.take(x)) {
    print(f"$i%10d, ")
    if (c % 10 == 0) {
      print("\n")
    }
    c = c - 1
  }
}

def nextFib(x: Long): Option[Long] = {
  /*
   * Compute the next value in the fibonacci sequence and handle the latent lack
   * of a value with an option.
   */
  def boundedFibStream(head: Long, last: Long, max: Long): Stream[Long] = {
    head match {
      case head if head == max => Stream.empty[Long]
      case head if head > max => Stream.cons(-1L, boundedFibStream(max, head, max))
      case head if head == 1L => Stream.cons(1L, boundedFibStream(head + 1L, head, max))
      case _ => Stream.cons(head, boundedFibStream(head + last, head, max))
    }
  }
    val stream = boundedFibStream(1, 1, x)
    val last = stream.toList

    last(last.size - 1) match {
      case -1L => Option.empty[Long]
      case a => Some(x + a)
    }
}

def listFiles(p: Boolean=true): Array[String] = {
  /*
   * Retrieve the files from a large directory on the system and filter hidden files.
   */
  val fnames: Array[String] = new java.io.File("/Users/keesvanekeren/").listFiles().map(x => x.getName).filterNot(_.head == '.')

  for (fname <- fnames) {
    if (p) { println(s"${fname}; ") }
  }
  fnames
}

def alphabetFiles(): Unit = {
  /*
   * Take the files from the last function and print an alphabetic report like
   * A
   * Afiles
   * ANotherFile
   * B
   * Bfiles
   *
   * etc....
   */
  val fnames: Array[String] = listFiles(p=false).sortWith(_.capitalize.head < _.capitalize.head);
  var curChar = '0';
  for (fname <- fnames) {
    if (fname.capitalize.head != curChar) {
      curChar = fname.capitalize.head
      println(curChar)
    }
    println(fname)
  }
}

def product(a: String, b: String): Option[Double] = {
  /*
   * Return the product of two strings.
   * Handle errors with an Option
   */
  util.Try[Double](a.toDouble * b.toDouble) toOption
}

def productLoop(a: String, b: String): Unit = {
  /*
   * Return the product of two strings.
   * Handle errors with a for loop
   */
  def attempt(x: String , y: String): Double = { x.toDouble * y.toDouble }
  util.Try[Double](attempt(a, b)) foreach (x => println(s"answer: $x"))
}

def productMatch(a: String, b: String): Double = {
  /*
   * Return the product of two strings.
   * Handle errors with a match
   */
  def attempt(x: String , y: String): Double = { x.toDouble * y.toDouble }
  util.Try[Double](attempt(a, b)) match {
    case util.Success(x) => x;
    case util.Failure(err) => -1;
  }
}

def safeGetProperty(propName: String): String = {
  /*
   * Write a safe wrapper around System.getProperty.
   * System.getProperty returns the value of a JVM environment oroperty
   */
  util.Try(System.getProperty(propName)) toOption match {
    case Some(null) => "Unknown";
    case None => "Unknown";
    case Some(x) => x; 
  }
}
