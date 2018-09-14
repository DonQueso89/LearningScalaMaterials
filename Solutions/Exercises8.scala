/*
 * Chap. 8: Classes
 *
 * Object is the root of all instances in the JVM, equivalent to the Scala root
 * type Any. AnyRef is the parent of all instantiable types.
 *
 * Class parameters (e.g.: class A(param: Int) ) are available in the class
 * global scope. By adding val to the class parameters, they
 * become fields in the class.
 *
 * Classes
 *
 * 'this' allows access to data/methods of the class
 * 'super' allows access to data/methods of the parent class.
 * An instance of a subclass can be used in place of a parent, but not the inverse.
 * The fields and methods of a superclass are expected to be a subset of the fields
 * and methods of its childclasses, therefore a parent class can never be expected
 * to support the same contracts as its children.
 *
 * See example C
 *
 * In the absence of a type parameter, the compiler will find the lowest (most specific) common
 * denomitor for all the types passed as function parameters.
 *
 * See example D
 *
 * A class is the definition of a type.
 * A nested class may access the fields and methods of the class in which it is nested..
 *
 * Abstract classes are implemented with the abstract keyword. See example A
 *
 * A less formal way to implement an abstract class is using anonymous classes.
 * Anonymous classes are nonreusable and nameless. see example B
 *
 * Apply methods can be referred to without the method name, like __call__ in
 * python. It is basically the default method of a class.
 *
 * Lazy values are values that are evaluated only when they are invoked. A lazy
 * value can be seen as a sort of cached function result. Lazy values 
 * are in between methods and values. Like methods, they are not evaluated when they
 * are defined but like values, they are only evaluated once. See example F.
 */

// Example C: a superclass' contract is a subset of the contract of its children
class Bird
class Swan extends Bird

// therefore, this is possible
val swan: Swan = new Swan()
// and this
val swan: Bird = new Swan()
// but this is not
val swan: Swan = new Bird()
//-------------------------------------------------------------------------

// Example D, the compiler finds th LCD for the types in the absence of a type parameter
//
class Stork extends Bird
class Marabou extends Stork
class BlackStork extends Stork
class Shoebill extends Stork
class Heron extends Bird

// So this will result in List[Stork]
val storks = List(new Marabou, new BlackStork, new Shoebill)

// this will result in List[Bird]
val storksAndFriends =  List(new Heron, new Marabou, new BlackStork, new Shoebill)

// and this will result in List[Object]
val storksAndAbstractFriends =  List(Int, new Heron, new Marabou, new BlackStork, new Shoebill)


abstract class Dog {
  val _type: String
  val color: String
  def bark: Unit
}

// Example A 
// Functions with empty parenthesis can be implemented with a value
class Labrador(val _type: String="Labrador", val color: String="Beige") extends Dog {
  def bark() {
    println(s"WOFO i am a $color ${_type}")
  }
}

// Example B
// (abstract) classes can be implemented with a one time use anonymous class
// the anonymous class = implemented and instantiated in one go.
val beagle = new Dog {
  val _type = "Beagle"
  val color = "Orange"
  def bark() {
    println(s"I am a $color ${_type}")
  }
}

// Example E
// Overloaded methods. Same name and retval but different input params
class Multiply {
  def multiply(l: Int, c: Int): Int = {
    l * c
  }
  
  def multiply(l: String, c: Int): Int = {
    Try(multiply(l.toInt, c)).getOrElse(0)
  }
}


// Example F
//  Lazy values

class Snorlax {
  lazy val l = { println("Init lazy val"); "lazy as a mufu" }
  val r = { println("immediately initialahlah"); "wussp mufuqz" }
}
