/*
 * Objects, Case-classes and Traits.
 *
 * Object: type of class that can have only 1 instance
 * - gets instantiated automatically the first time it is accessed in
 *   a running JVM
 * - Until it is accessed, it is NOT instantiated
 * - Scalas way of decoupling static methods and fields from classes
 * - An object can extend a class, making its fields globally accesible
 *   from the same object
 * - objects can not be extended by classes
 * 
 * - object are often used for storing pure functions that access some
 *   external I/O mechanism (as opposed to classes which operate on instance data).
 * (remember that pure functions: 1- return results exclusively calculated from  he input 2- have no side-effects -3 are referentially transparent, i.e.: indistinguishable if replaced with their result.
 * 
 * the apply method on objects can be used to return the right subclass of its companion class (i.e.: as a factory method).
 * Examples are:
 *  - invoking apply() on an Option to get Some or None
 *  - invoking apply() on a Future which spawns a given function in a background thread
 * objects that are used in this way are often companion objects a class
 *, that is, a class type defined in the same file with the same name.
 *  - Companion object and classes are considered as 1 unit in terms of access management so that they can access eachothers private and protected methods and data.
 *  - An object with the same name and an apply method that has the same signature as the companion class' constructor is meant to be used as a factory for that companion class.
 *
 * Command line applications withb Objects:
 * - an object with a main method will be invoked by scala when passed as an argument. See example A for the function signature
 *
 * Case classes(example B):
 * - instantiable classes with an automatically generated companion object
 * - also have an equals method and toString based on the parameter-list
 * - great for storing and transferring data
 * - not great for inheritance (inherited data is not taken into account in the generated methods)
 * - val is assumed for case class parameters
 * - the companion objects unapply method allows destructuring a case class into a tuple of its fields for pattern matching
 *
 * Traits (Mixins)
 * 
 * - class that enables multiple inheritance
 * - classes, objects, traits and case classes can extend multiple Traits
 * - cannot take class parameters
 * - can take type parameters
 * - A parent class must always come before the trait(s): example D
 *
 *   Multiple inheritance in the JVM:
 *
 *   - In the background, the compiler applies linearization to classes using
 *   multiple inheritance. The order in which it applies this is right to left
 *   such that the rightmost trait becomes the exdending class' parent.
 *   - e.g.: 
 *   class A extends C with D with B 
 *   is seen as 
 *   class A extends B extends D extends C by the compiler 
 *
 *   Self types (Example E)
 *   - trait that asserts that it must be mixed in with a specific type or its subtype
 *   - basically a way of indirectly ensuring that a trait will extend a certain type (because of linearization in the compiler)  
 *   - A popular use of self types is to add traits to classes with input parameters. Self types of such classes can access data on the class they extend as if they were extending it directly
 *
 *   Instantiation with traits (Example F):
 *   - traits can be added to a class upon instantiation
 *   - in this way, traits extend the class
 *   - used often for dependency injection
 */
import scala.util.Try

// Example A
object Main {
  // Example B
  case class Arg(value: String, index: Int)

  trait DoubleConverter {
    def toDouble(d: String): Double = {
      d.toDouble

    }
  }

  trait IntConverter {
    def toInt(i: String): Int = {
      i.toInt
    }
    
  }

  class SafeConverter {
    def safeConvert[A, B](f: A => B, param: A): Option[B] = {
      Try(f(param)).toOption
    }
  }

  // Example D
  object Converter extends SafeConverter with DoubleConverter with IntConverter {
    def apply(s: String): Option[AnyVal] = {
      if (s.contains(".")) {
        safeConvert[String, Double](toDouble, s)
      } else {
        safeConvert[String, Int](toInt, s)
      }
    }
  }

  // Example E: self types
  trait A {
    val input: String
    val param: Double
    override def toString(): String = s"Base $input $param\n" + ("^||||\n".toList.mkString("\n"))
  }

  trait B { self: A =>
    override def toString(): String = super.toString + s"MixedB $input $param\n" + ("^||||\n".toList.mkString("\n"))
  }
  
  trait Bplus { self: A =>
    override def toString(): String = super.toString + s"MixedBplus $input $param\n"
  }
  
  trait Bminus { self: A =>
    override def toString(): String = super.toString + s"MixedBminus $input $param\n"
  }

  class C(val input: String, val param: Double) extends A with B {
    override def toString: String = super.toString + s"Child $input --> $param\n"
  }
  // NB: Linearization flattens this hierarchy into C extends B extends A

  def main(args: Array[String]) {
    val argz = args.zipWithIndex.map( { case (e, i) => Arg(e, i) } ) // pattern matching anonymous function to destructure the elem,index tuple

    for (arg <- argz) {
      println(s"${arg.index}: ${arg.value}")

      // Example C
      arg match {
        case Arg("needle in a haystack", ind) => println(s"FOUND IT AT $ind")
        case _ => 
      }

      Converter(arg.value).getOrElse(arg.value) match {
        case o: Int => println(s"Succesfully converted $o to an Int");
        case o: Double => println(s"Succesfully converted $o to a Double");
        case o => println(s"Could not convert $o to an Int or Double");
      }
    }
    val mixedInSelfType: C = new C("hello", 1.234354)
    println(mixedInSelfType)

    // Example F: different instances with different dependencies injected
    class Injectee(val input: String, val param: Double) extends A {
      override def toString(): String = super.toString + s"Injected $input $param" + ("\n^||||\n".toList.mkString("\n"))
    }

    val injected = new Injectee("input", 1.2345) with Bminus
    val injectedPlus = new Injectee("input", 1.2345) with Bplus
    println(injected)
    println(injectedPlus)
  }
}

