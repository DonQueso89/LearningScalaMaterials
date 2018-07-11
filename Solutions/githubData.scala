#!/usr/bin/env sbt -Dsbt.main.class=sbt.ScriptMain

/***
 scalaVersion := "2.11.1"
 */

/*
 * Script that reports recent commits for a Github branch
 */

def retrieveData(repo: String, branch: String, user: String): Unit = {
  val url = s"https://github.com/$user/$repo/commits/$branch.atom"
  val result = io.Source.fromURL(url)
}

