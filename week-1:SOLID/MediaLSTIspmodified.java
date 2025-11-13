import java.util.*;

interface Playable{
   void play(String source);
   void pause();
}
interface Recordable {
   void record(String destination);
}

interface Streamable {
   void streamLive(String url);
}

interface Downloadable {
    void download(String sourceUrl);
}

class AudioPlayer implements Playable,Downloadable{
  private boolean playing = false;
  @Override
  public void play(String source){
    playing=true;
  }
  @Override
  public void pause(){
    playing=false;
  }
  @Override
   public void download(String sourceUrl){
     
   }
   public boolean isPlaying(){
     return playing;
   }
}

class CameraStreamPlayer implements Playable,Recordable,Streamable{
    private boolean liveStarted = false;
    private boolean playing = false;

    @Override 
     public void play(String source) {
        if (!liveStarted) {
            System.out.println("[WARN] playing without live stream started.");
        }
        playing = true;
    }
    @Override 
    public void pause(){ 
      playing = false; 
    }
      
    @Override 
    public void record(String destination) {
    }
    @Override 
    public void streamLive(String url) {
      liveStarted = true;
      }
    

    public boolean isPlaying(){ 
      return playing; 
    }
    
    public boolean isLive(){ 
    return liveStarted; 
    }
       
}


public class MediaLSTIspmodified{
  public static void main(String[] args){
       AudioPlayer ap = new AudioPlayer();
        ap.play("song.mp3");
        System.out.println("Audio playing: " + ap.isPlaying());
        ap.pause();

        CameraStreamPlayer cam = new CameraStreamPlayer();
        cam.play("rtsp://camera");       
        cam.streamLive("rtsp://camera"); 
        cam.play("rtsp://camera");
  }
}
