/*
 * Chap. 5 First class functions
 * A function type is composed of its input types and it s return types:
 * e.g.: (Int, Double) => String
 *
 * Function literals
 *
 * Function literals (a.k.a. lambda's): a working function that lacks a name.
 * Essentially a parametrized expression.
 * Typically of the form: (x: Int) => x * 2 or ([<identifier>: <type>, ...]) => expression
 * A function literal can be used in any A place where a function type is expected.
 *
 * When the compiler can infer the function type, a function literal can be stripped of its types.
 * See example A below.
 * 
 * Placeholder syntax
 *
 * Placeholder syntax is a shortened form of function literals that replaces
 * named parameters with wildcard placeholders. It can be used when:
 * - the explicit type of the function is specified outside the literal (such as in example A)
 * - the parameters are only used once
 * 
 * When there are multiple parameters, the placeholders positionally replace
 * the input parameter see example B.
 *
 */

// Example A: Stripping a function literal of its types --------------------

// When invoking the higher order function
def doIt(someThing: String, someFunc: String => String) = {
  if (someThing == null) someThing else someFunc(someThing)
}
// We can invoke it like this
doIt("Hello world", (x: String) => x.reverse)

// or like this
doIt("Hello world", x => x.reverse)
// stripping the types and letting the compiler infer the types
//
// or with the placeholder syntax
doIt("Placeholder syntaxalicious", _.reverse)
//-------------------------------------------------------------------------

// Example B placeholder syntax with multiple parameters
def doItMultiple[A, B](a: A, b: A, c: A, f: (A, A, A) => B) = {
  f(a, b, c)
}

doItMultiple[String, String]("Kanye", "SnorLax", "Lindsey", String.join("\n", "Cool people:", _, _, _))
//-------------------------------------------------------------------------


object Main extends App {
  def one(threeTup: (Integer, Integer, Integer)): Unit = {
    def max(a: Integer, b: Integer): Integer = {
      if (a > b) a else b;
    }

    def hoc(threeTup: (Integer, Integer, Integer), max: (Integer, Integer) => Integer): Integer = {
      max(threeTup._1, max(threeTup._2, threeTup._3));
    }

    println(s"Max of ${threeTup} = ${hoc(threeTup, max)}");
  }

  def two(threeTup: (Integer, Integer, Integer)): Unit = {
    def max(a: Integer, b: Integer): Integer = {
      if (a > b) a else b;
    }
    def min(a: Integer, b: Integer): Integer = {
      if (a < b) a else b;
    }
    def last(a: Integer, b: Integer): Integer = {
      b
    }

    def hoc(threeTup: (Integer, Integer, Integer), max: (Integer, Integer) => Integer): Integer = {
      max(threeTup._1, max(threeTup._2, threeTup._3));
    }

    println(s"Max of ${threeTup} = ${hoc(threeTup, max)}");
    println(s"Min of ${threeTup} = ${hoc(threeTup, min)}");
    println(s"Second of ${threeTup} = ${hoc(threeTup, last)}");
  
  };

  def three(x: Integer, y: Integer): Unit = {
    def hoc(y: Integer): (Integer) => Integer = {
      (z: Integer) => x * z;
    }
    println(s"Product of $x and $y = ${hoc(x)(y)}");
  };

  def four(): Unit = {
    /*
     * fzero: Function with two parameters lists (allowing for currying)
     * fzero Takes a function taking 1 param of type A and returning nothing.
     * fzero appliess the function to the value and then returns the value.
     */
    def fzero[A](x: A)(f: A => Unit): A = { f(x); x };

    val myList: Seq[Integer] = Seq(1, 2, 3, 4, 5);

    def incrementer(a: Seq[Integer]) = {
      print(s"${a.map(x => x + 1)} == ");
      ();
    }

    println(s"${fzero[Seq[Integer]](myList)(incrementer)} incremented by one")

  };
  def five(x: Double): Unit = {
    // Function values can be stored like this
    // Note that the type must be declared, else its an invocation of square
    def square(m: Double) = m * m
    val sq:(Double) => Double = square

    // Function literals can also be assigned to an identifier
    val literalSquare:(Double) => Double = (m: Double) => m * m;

    println(s"Squaring with an assigned function literal $x == ${literalSquare(x)}")
  };

  def six(x: String): Unit = {

    def hoc[A](y: A, p: A => Boolean, f: A => A): A =  {
      if (p(y)) f(y) else y;
    }

    println(s"Result of f($x) == ${hoc[String](x, _.size > 5, _.reverse)}");

  };
  def seven(): Unit = {
    def hoc(x: Integer, p: Integer => String, f: String => Unit): Unit = {
      f(p(x))
    }
    
    def predicate(x: Integer): String = {
      var s = "";
      if (x % 3 == 0) {
        s += "type";
      }
      if (x % 5 == 0) {
        s += "safe";
      }
      if (s.length > 0) s else x.toString
    }

    for (i <- 1 to 100) {
      hoc(i, predicate, (x: String) => println(x))
    }
  };

  args(0) match {
    case "1" => one((Integer.valueOf(args(1)), Integer.valueOf(args(2)), Integer.valueOf(args(3))));
    case "2" => two((Integer.valueOf(args(1)), Integer.valueOf(args(2)), Integer.valueOf(args(3))));
    case "3" => three(Integer.valueOf(args(1)), Integer.valueOf(args(2)));
    case "4" => four();
    case "5" => five(args(1).toDouble);
    case "6" => six(args(1));
    case "7" => seven();
    case _ => println("ERROR");
  }
}
