#!/usr/bin/env sbt -Dsbt.main.class=sbt.ScriptMain

/***
scalaVersion := "2.11.1"
*/
/*
* Write an executable that gets the Github commits
*/
import concurrent.ExecutionContext.Implicits.global
import collection.mutable.ListBuffer
import concurrent.Future
import java.util.Date;
import java.text.SimpleDateFormat;
import java.util.Calendar;


def getGithubCommits(params: (String, String, String)): List[(String, String, String)] = {
  /*
   * Write a function that retrieves recent commits for a given user, repo and
   * branch on Github and prints out the date, title and author for each commit.
   */
  val resultList = ListBuffer[(String, String, String)]()
  params match {
    case (user, repository, branch) => {
      val url = s"https://github.com/$user/$repository/commits/$branch/.atom"
      val rawResult = io.Source.fromURL(url).getLines.map(_.trim).mkString("").split("<entry>")
      for (entry <- rawResult.slice(1, rawResult.length)) {
        val dateRgx = """.*<updated>(.*)</updated>.*""".r
        val authorRgx = """.*<author><name>(.*)</name>.*""".r
        val titleRgx = """.*<title>(.*)</title>.*""".r

        val dateRgx(date) = entry
        val authorRgx(author) = entry
        val titleRgx(title) = entry
        resultList += ((date, author, title))
      }
    }
    case _ => println("Not enough arguments specified")
  }
  resultList toList
}

def parseResults(results: List[List[(String, String, String)]]) = {
  /*
   * Mix the commits together, sort by date and add a repo column
   */
  val dateFmt = new SimpleDateFormat("yyyy-MM-dd")
  val flatResult = results.flatten.sortWith((x, y) => { dateFmt.parse(x._1) after dateFmt.parse(y._1) })
  for (result <- flatResult) {
    val (date, author, title) = result
    println(f"Date: $date%10s Author: $author%20s Title: $title%20s")
  }
}

def parseInput(projects: String): Option[List[(String, String, String)]] = {
  util.Try[List[(String, String, String)]](projects.split(",").map(
      (project) => {
        val p = project.split("/")
        (p(0), p(1), p(2))
      }) toList) toOption

}

def getGithubCommitsConcurrent(projects: List[(String, String, String)]) = {
  /*
   * Take a list of projects, and retrieve data concurrently with Futures.
   * Future.sequence returns a 'master' Future that holds a list of the results
   * of its "sub-futures" if they are all successful or a Failure. 
   * Reminder: Failure and Success are subclasses of Try.
   */
  val possibleResults = Future sequence projects.map((p) => Future(getGithubCommits(p)))
  possibleResults onSuccess { case(x) => parseResults(x) }
  possibleResults onFailure { case(x) => println(s"Got exception: $x");  }
  Thread.sleep(5000)
}

args.toList match {
        case List(input) => {
          parseInput(input) match {
            case Some(projects) => getGithubCommitsConcurrent(projects)
            case _ => println("usage: SbtScriptChap7.scala <project1,project2,project3>")
          }
        }
        case _ => println("usage: SbtScriptChap7.scala <project1,project2,project3>")    
}
