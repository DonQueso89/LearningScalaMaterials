import scala.collection.mutable.ListBuffer
import io.Source.fromURL

def one(): Seq[Long] = {
  // Three ways of getting the first 20 odd numbers
  // from least expressive to most expressive
  var looped: List[Long] = List[Long]();
  for (i <- 1L to 20L) { if(i % 2 != 0) looped = i :: looped };
  val mapped: Seq[Long] = 1L to 20L map ((a: Long) => if(a % 2 != 0) a else 0L);
  val filtered: Seq[Long] = 1L to 20L filter (_ % 2 != 0);
  println(s"mapped $mapped");
  println(s"looped $looped");
  println(s"filtered $filtered");
  filtered;
}

@annotation.tailrec
def two(l: List[Long], result: List[Long]): List[Long] = {
  // Get factors recursively
  if (l.size == 0) {
    result;
  } else {
    val head: Long = l.head;
    val factors: List[Long] = (2L to (head - 1) filter (head % _ == 0)).toList
    two(l.tail, factors ++ result);
  }
}

def threeRecursive[A](items: List[A], count: Int): List[A] = {
  // base case
  if (count == 0) {
    List[A]()
  } else {
    items.head :: threeRecursive(items.tail, count - 1)
  }
}

def threeFold[A](items: List[A], count: Int): List[A] = {
  items.foldLeft[List[A]](List[A]())((acc, elem) => if (acc.size >= count) acc else acc :+ elem)
}

def threeFor[A](items: List[A], count: Int): List[A] = {
  val result = ListBuffer[A]()
  for (elem <- items) {
    if (result.size < count) {
      result += elem;
    }
  }
  result.toList;
}

def threeOneLiner[A](items: List[A], count: Int): List[A] = {
  items.slice(0, count);
}

def fourFold(l: List[String]): String = {
  l.foldLeft("")((acc, elem) => if (acc.size > elem.size) acc else elem)
}

def fourReduce(l: List[String]): String = {
  l.reduce[String]((acc, elem) => if (acc.size > elem.size) acc else elem)
}

def five(s: String): String = {
  // Reverse  a string recursively
  s match {
    case s if s.isEmpty => s
    case _ => five(s.tail) + s.head
  }
}

def sixPartition(l: List[String]): (List[String], List[String]) = {
  // Split list by palindrome and non-palindrome
  l.partition((x) => five(x) == x)
}

def sixFold(l: List[String]): (List[String], List[String]) = {
  // Split list by palindrome and non-palindrome
  def partitionFunc(acc: (List[String], List[String]), elem: String) = {
    elem match {
      case elem if five(elem) == elem => (acc._1 :+ elem, acc._2)
      case _ => (acc._1, acc._2 :+ elem)
    }
  }
  l.foldLeft((List[String](), List[String]()))(partitionFunc)
}

def getForecast(): List[String] = {
  val url = "http://api.openweathermap.org/data/2.5/forecast?mode=xml&lat=55&lon=0&APPID=8c2a663b6f67ab3b32bd07aeabead448"
  val data = fromURL(url).getLines.toList
  data
}

def seven() = {
  val data: List[String] = getForecast();
  val cityPatt = """.*<location><name>([a-zA-Z]+)</name>.*""".r;
  val cityPatt(city) = data(1);
  val countryPatt = """.*<country>([A-Z]{2})</country>.*""".r;
  val countryPatt(countryCode) = data(1);
  println(s"-----FORECAST-----\n\nCountry $countryCode\nCity: $city\n");

  val descriptionPatt = """.*<symbol.*name="([a-zA-Z ]+)".*</symbol>.*""".r;
  val startPatt = """.*from="(\d{4}-\d{2}-\d{2}T\d{2}:\d{2}:\d{2})".*""".r;
  val endPatt = """.*to="(\d{4}-\d{2}-\d{2}T\d{2}:\d{2}:\d{2})".*""".r;
  for (tag <- data(1).split("</time>")) {
    if ((tag matches descriptionPatt.toString) && (tag matches startPatt.toString) && (tag matches endPatt.toString)) {
      val descriptionPatt(description) = tag;
      val startPatt(start) = tag;
      val endPatt(end) = tag;
      println(s"\nWeather for $start to $end:\n$description\n");
    }
  }
}
