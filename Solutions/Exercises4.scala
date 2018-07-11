import System.getenv

def one(radius: Double): Double = {
  return radius * 2 * Math.PI
}

def two(radius: String): Double = {
  return radius.toDouble * 2 * Math.PI
}

@annotation.tailrec
def three(n: Integer): Unit = {
  // Tailrecursion re-uses the stack
  // The annation causes the compiler to apply this optimisation
  if (n == 50) {
    print(s"$n, ");
  } else {
    print(s"$n, ");
    three(n  + 1);
  }
}

def four(millis: Long): String = {
  val seconds = millis / 1000;
  return(s"""
    days: ${seconds / (60 * 60 * 24)}\n
    hours: ${(seconds % (60 * 60 * 24)) / 3600}\n
    minutes: ${(seconds % (60 * 60)) / 60}\n
    seconds: ${seconds % 60}\n
    """
  )
}

@annotation.tailrec
def five(result: Long, exponent: Long, n: Long): Unit = {
  if (exponent == 1) {
    println(s"result: $result");
  } else {
    five(result * n, exponent - 1, n);
  }
}

def six(x: (Integer, Integer), y: (Integer, Integer)): Unit = {
  println(s"Distance between points: ${(x._1 - y._1, x._2 - y._2)}");
}

def seven[A, B](tup: (A, B)) = {
  tup match {
    case tup if tup._1.isInstanceOf[Integer] => println((tup._1, tup._2));
    case tup if tup._2.isInstanceOf[Integer] => println((tup._2, tup._1));
    case _ => println(tup);
  }
}

def eight[A, B, C](tup: (A, B, C)): (A, String, B, String, C, String) = {
  return (tup._1, s"${tup._1}", tup._2, s"${tup._2}", tup._3, s"${tup._3}");
}
//six((Integer.valueOf(args(0)), Integer.valueOf(args(1))), (Integer.valueOf(args(2)), Integer.valueOf(args(3))));
