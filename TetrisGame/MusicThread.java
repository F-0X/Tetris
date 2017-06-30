package TetrisGame;

import javax.sound.midi.MidiSystem;
import javax.sound.midi.Sequencer;
import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;

class MusicThread extends Thread {

    private Sequencer seq = null;
    private InputStream musicFile = null;

    void muteMusic(){
        seq.stop();
    }

    void unmuteMusic(){
        //try {
        //    seq.setSequence(musicFile);
        //} catch (Exception e) {
        //    e.printStackTrace();
        //}
        //seq.setLoopCount(100);
        seq.start();
    }

    @Override
    public void run() {
        try{
            seq = MidiSystem.getSequencer();
            seq.open();
            musicFile = new BufferedInputStream(new FileInputStream(new File("ThemeA.mid")));
            seq.setSequence(musicFile);
        } catch (Exception e){
            e.printStackTrace();
        }

        seq.setLoopCount(100);
        seq.start();
    }
}


//try {
//    seq = MidiSystem.getSequencer();
//} catch (MidiUnavailableException e) {
//    e.printStackTrace();
//}

//try {
//    seq.open();
//} catch (MidiUnavailableException e) {
//    e.printStackTrace();
//}

//InputStream musicFile = null;
//try {
//    musicFile = new BufferedInputStream(new FileInputStream(new File("ThemeA.mid")));
//} catch (FileNotFoundException e) {
//    e.printStackTrace();
//}

//try {
//    seq.setSequence(musicFile);
//} catch (IOException | InvalidMidiDataException e) {
//    e.printStackTrace();
//}
