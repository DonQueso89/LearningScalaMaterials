def CentigradeToFahrenheit(celsius: Integer): Double = {
  return(celsius * 9 / 5 + 32);
}

val credit = 2.7255;
println(s"You owe ${credit + .01 - credit % .01}");

val result: Boolean = false;

var number: Integer = 128

var snumber = number.toString

number = snumber.toInt

var dnumber = number.toDouble

number = dnumber.toInt

var cnumber: Array[Char] = number.toString.toCharArray

number = cnumber.map(x => x.toString).mkString.toInt

val inp = "Frank,123 Main,925-555-1943,95122";

// Capturing stuff with regex
val patt = """.*(\d{3}-\d{3}-\d{4}).*""".r;
val patt(telephoneNumber) = inp;
val t = telephoneNumber.split("-").map(x => x.toInt);
val converted: (Int, Int, Int) = (t(0), t(1), t(2));
println(telephoneNumber);
