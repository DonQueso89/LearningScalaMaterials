import System.getenv

val ex: Int = Integer.valueOf(getenv().get("EX"));

ex match {
  case 1 => {
    println("Exercise 1");
    val arg: String = getenv().get("ARG");
    arg match {
      case "" => { println("This one is empty") }
      case bound => { println(s"This one ==  ${bound} and is not empty ;)") }
    }
  }
  case 2 => {
    println("Exercise 2");
    val arg: Double = getenv().get("ARG").toDouble;
    arg match {
      case arg if arg > 0  => { println("greater"); }
      case arg if arg < 0 => { println("less"); }
      case _ => { println("same"); }
    }
  
  }
  case 3 => {
    println("Exercise 3");
    val arg: String = getenv().get("ARG");
    if (Seq("cyan", "magenta", "yellow").contains(arg)) {
      println(arg.toCharArray.map(c => c.toHexString))
    } else {
      println("Not a valid input")
    }
  }
  case 4 => {
    println("Exercise 4");
    for (i <- 1 until 100 by 5) {
      for (j <- i until i + 5) {
        print(s"$j, ");
      }
      print("\n");
    }
  }
  case 5 => {
    println("Exercise 5");
    for (i <- 1 to 100) {
      i match {
        case i if i % 3 == 0 && i % 5 == 0 => {
          print("typesafe, ")
        }
        case i if i % 3 == 0 => {
          print("type, ");
        }
        case i if i % 5 == 0 => {
          print("safe, ");
        }
        case i => {
          print(s"$i, ")
        }
      }
    }
  }
  case 6 => {
    println("Exercise 6");
    for (i <- 1 to 100) { if (i % 3 == 0 && i % 5 == 0) print("typesafe, ") else if (i % 3 == 0) print("type, ") else if (i % 5 == 0) print("safe, ") else print(s"$i, ") }
  }
  case _ => { println("No exercise found") };
}
