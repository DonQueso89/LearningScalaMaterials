/*
 * Create a console class
 */

package exercises.ch8.ex1
import java.util.Date

object MediaType {
  final val mediaTypes: Map[Int, String] = Map(
    1 -> "floppy",
    2 -> "CDROM",
    3 -> "DVD"
  )

  def apply(t: Int): String = {
    mediaTypes.get(t).getOrElse("Unknown")
  }
}

class Konsole(val model: String, val make: String, val debutDate: Date,  val mediaFormats: Seq[String], val wifiType: String="N/A", val mediaType: Int, val maxRes: Int) {
  override def toString() = s"< Console: $model make: $make - $debutDate - wifi: $wifiType - mediaType: ${MediaType(mediaType)} - maxRes: $maxRes >";
  
}

class Game(val name: String, val maker: String, val supported: Seq[Konsole]) {
  def isSupported(konsole: Konsole): Boolean = supported.contains(konsole)
  override def toString() = s"< $name - $maker - $supported >"
}

object Exercise {
  /*
   * - generate list of games and test isSupported
   * - convert list of games to lookup table of consoles and supported games 
   * - write a function that prints a list of games sorted by maker/name
   */

  val konsoles = Map[String, Konsole](
    "xbox" -> new Konsole("xbox", "1", new Date, Seq("a", "b"), "sometype", 3, 500),
    "ps1" -> new Konsole("ps1", "1", new Date, Seq("a", "b"), "sometype", 2, 600),
    "nintendo" -> new Konsole("nintendo", "1", new Date, Seq("a", "b"), "sometype", 1, 300),
    "PC" -> new Konsole("PC", "1", new Date, Seq("a", "b"), "sometype", 3, 9320)
  )

  val games = Seq[Game](
    new Game("spyro", "supergames", Seq(konsoles("ps1"), konsoles("PC"))),
    new Game("crashbandicoot", "supergames", Seq(konsoles("ps1"), konsoles("xbox"), konsoles("PC"))),
    new Game("diablo", "blizzard", Seq(konsoles("PC"))),
    new Game("dawnofWar", "hellgames", Seq(konsoles("PC"))),
    new Game("mario", "nintendo", Seq(konsoles("nintendo"), konsoles("PC")))
    
  )
  
  @annotation.tailrec
  def toLookupTable(games: Seq[Game], result: Map[Konsole, Set[Game]]): Map[Konsole, Set[Game]] = {
    /*
     * Take a list of games and add it to all consoles that support it
     */ 
    if (games.isEmpty) {
      result 
    }
    else {
      val currGame = games.head;
      val updatedResult = currGame
        .supported
        .map((k) => (k, Set(currGame))) // Make it a tuple of (Konsole, Set[Game])
        .foldLeft[Map[Konsole, Set[Game]]](result)((r, elem) => 
            r ++ Map(elem._1 -> (r.get(elem._1).getOrElse(Set[Game]()) ++ elem._2))
        ) // Make it a Map of Konsole -> Set[Games]

      toLookupTable(
        games.tail,
        updatedResult
      )
    }
  }

  def main() {
    // test isSupported
    val g = games(0)
    val k = konsoles("xbox")
    println(s"${g.name} is ${if (g.isSupported(k)) "" else "not "}supported by ${k.model}" )

    // Convert to console --> supported games lookup
    val lookuptable: Map[Konsole, Set[Game]] = toLookupTable(games, Map[Konsole, Set[Game]]())
    lookuptable.foreach((e) => println(s"\nConsole: ${e._1} supports ${e._2.map(_.name) mkString("\n")}"))

    // Sort by maker and then by gamename
    val sorted = games.sortWith((a , b) => if (a.maker == b.maker) a.name <= b.name else a.maker <= b.maker)
    println("\nSorted games\n" + (sorted.map((s) => (s.maker, s.name)) mkString("\n")))
  }
}
