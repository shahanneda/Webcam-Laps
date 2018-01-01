package sample;

import com.github.sarxos.webcam.Webcam;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.VBox;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.stage.Stage;
import javafx.util.Duration;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;
public class Main extends Application {
    static Boolean shouldClose = false;
     ScheduledExecutorService executor;
    int lastr;
    int lastg;
    int lastb;
    int selectedr;  int selectedg;  int selectedb;
    long lastSubmitTime = System.currentTimeMillis();
    long minTimeDeley = 1000;
    MediaPlayer mediaPlayer;
    @Override
    public void start(Stage primaryStage) throws Exception{


        primaryStage.setTitle("Hello World!");
        Button btn = new Button();

        Label testlabel  = new Label();
        testlabel.setText("Hello");

        Label bluecount = new Label();
        bluecount.setText("0");

        btn.setText("Say 'Hello World'");
        //Used for setting the correct color, is the last frames color average
        Rectangle r2 = new Rectangle();
        r2.setX(500);
        r2.setY(0);
        r2.setWidth(500);
        r2.setHeight(500);
        r2.setArcWidth(0);
        r2.setArcHeight(0);
        r2.setFill(Color.RED);

        btn.setOnAction(new EventHandler<ActionEvent>() {

            @Override
            public void handle(ActionEvent event) {
                System.out.println("Hello World!");
                testlabel.setText("Color = Red:" + lastr+ " G:" + lastg + " b:"+lastb );
                selectedr = lastr;
                selectedb = lastb;
                selectedg = lastg;

                float newr = selectedr/255f;
                float newb = selectedb/255f;
                float newg = selectedg/255f;
//                Platform.runLater(new Runnable() {
//                    @Override
//                    public void run() {
                        r2.setFill(new Color(newr,newg,newb,1));
                System.out.println(selectedr/255);
//                    }
//                });

            }
        });

        Rectangle r = new Rectangle();
        r.setX(0);
        r.setY(0);
        r.setWidth(500);
        r.setHeight(500);
        r.setArcWidth(0);
        r.setArcHeight(0);



        VBox root = new VBox();
        root.getChildren().add(btn);
        root.getChildren().add(testlabel);
        root.getChildren().add(r);
        root.getChildren().add(bluecount);
        root.getChildren().add(r2);

        primaryStage.setScene(new Scene(root, 300, 250));
        primaryStage.show();
        String musicFile = "beep.mp3";     // For example

        Media sound = new Media(new File(musicFile).toURI().toString());
         mediaPlayer = new MediaPlayer(sound);
        mediaPlayer.play();
        shouldClose = false;
         executor =
                Executors.newSingleThreadScheduledExecutor();

        Runnable periodicTask = new Runnable() {
            public void run() {
                // Invoke method(s) to do the work

            Webcam webcam = Webcam.getDefault();
            webcam.open();

            BufferedImage image = webcam.getImage();
            int width = image.getWidth();
            int height = image.getHeight();
            BufferedImage croppedImage = image.getSubimage(width/2, height/2, width/3, height/3);


            BufferedImage bi = croppedImage;
            int x0 = 0;
            int y0 = 0;
            int w = croppedImage.getWidth();
            int h = croppedImage.getHeight();

//                int x1 = x0 + w;
//                int y1 = y0 + h;
//                long sumr = 0, sumg = 0, sumb = 0;
//              //  for (int x = x0; x < x1; x++) {
//                    for (int y = y0; y < y1; y++) {
//                        Color pixel = new Color(bi.getRGB(0, y));
//                        sumr += pixel.getRed();
//                        sumg += pixel.getGreen();
//                        sumb += pixel.getBlue();
//                    }
//               // }
//                int num = w * h;
//               Color c= new Color(sumr / num, sumg / num, sumb / num);
                float sumr = 0, sumg = 0, sumb = 0;

                for(int x = 0; x < croppedImage.getWidth(); x++){
                    for (int y = 0 ; y<croppedImage.getHeight(); y++){
                        java.awt.Color pixel = new java.awt.Color(bi.getRGB(x, y));
                        sumr += pixel.getRed();
                        sumg += pixel.getGreen();
                        sumb += pixel.getBlue();

                    }

                }
                float ar = sumr/(croppedImage.getWidth()*croppedImage.getHeight());
                float ag = sumg/(croppedImage.getWidth()*croppedImage.getHeight());
                float ab = sumb/(croppedImage.getWidth()*croppedImage.getHeight());
               // System.out.println("Red: "+ar + ", Green: " + ag + ", Blue: " + ab);
                Color finalAverageColor = new Color(ar/255,ag/255,ab/255,1);

                lastr = (int)ar;
                lastg = (int)ag;
                lastb = (int)ab;
                boolean isSimtocolor = Helper.isSimilar((int)ar,(int)ag,(int)ab,selectedr,selectedg,selectedb);
                Platform.runLater(new Runnable() {
                    @Override
                    public void run() {
                        //Update the text of label here

                        //testlabel.setText("Color = Red:" + finalAverageColor.getRed()+ " G:" + finalAverageColor.getGreen() + " b:"+finalAverageColor.getBlue() );

                        int bcount = Integer.parseInt(bluecount.getText());
                        System.out.println(System.currentTimeMillis() - lastSubmitTime);
                        if (isSimtocolor && System.currentTimeMillis() - lastSubmitTime > minTimeDeley){
                            //&& lastSubmitTime + minTimeDeley > System.currentTimeMillis()

                            lastSubmitTime = System.currentTimeMillis();
                            testlabel.setText("Is Simeler :)");
                            bcount++;
                            bluecount.setText(Integer.toString(bcount));
                            mediaPlayer.seek(Duration.millis(0));
                            mediaPlayer.play();
                        }else{
                            testlabel.setText("Not Simler :(");
                        }
                        r.setFill(finalAverageColor);


                    }
                });


                java.awt.Color c = new java.awt.Color(0,0,1);
              //System.out.println(c.getBlue());
            // save image to PNG file

            }
        };

        executor.scheduleAtFixedRate(periodicTask, 0, 100, TimeUnit.MILLISECONDS);
//        while(!shouldClose){
//
////
//////        // get image
////
////
////            BufferedImage image = webcam.getImage();
////
////            // save image to PNG file
////            ImageIO.write(image, "PNG", new File("test.png"));
//        }
    }


    public static void main(String[] args) throws IOException {


        launch(args);



    }
    @Override
    public void stop(){
        System.out.println("Stage is closing");
        executor.shutdown();
        shouldClose = true;
        // Save file
    }

    /*
 * Where bi is your image, (x0,y0) is your upper left coordinate, and (w,h)
 * are your width and height respectively
 */

}

