package exercises.ch8.ex3;

/*
 * Create a directory listing class 
 */

import java.io.File
import java.io.FilenameFilter

class DirLister(val pathName: String, val filePredicate: String => Boolean) {
  val file = new File(pathName)
  val filenameFilter = new FilenameFilter() {
    override def accept(file: File, name: String): Boolean = {
      filePredicate(name)
    }
  }

  def list(): Array[File] = {
    file.listFiles(filenameFilter)
  }
}
