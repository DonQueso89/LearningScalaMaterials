package exercises.ch8.ex4;

/*
 * Write a wrapper around the JVMs builtin Synthesizer 
 */

import javax.sound.midi.MidiSystem

class SynthBlaster(val volume: Int) {
  lazy val synth = MidiSystem.getSynthesizer
  final val intervalMillis = 250
  
  def playNote(note: Int) {
    synth.open()
    val channel = synth.getChannels.head
    channel.noteOn(note, volume)
    Thread.sleep(intervalMillis)
    channel.noteOff(note, volume)
  }

  def play(notes: List[Int]): Unit = {
    synth.open()
    for (n <- notes) {
      playNote(n)
    }
    synth.close()
  }

  def play(notes: Range): Unit = {
    /*
     * e.g.: play(40 to 60 by 5)
     */
    play(notes.toList)
  }
}
