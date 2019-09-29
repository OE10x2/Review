import javafx.animation.AnimationTimer;
import javafx.application.Application;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Line;
import javafx.stage.Stage;

import java.util.ArrayList;

public class Assignment2 extends Application{
    public static void main(String[] args){
        launch(args);
    }

    RLine RLine = new RLine();
    Group root = new Group(), update = new Group();
    Scene scene = new Scene(root);
    ArrayList<Double> angles = new ArrayList<>();
    ArrayList<Integer> speeds = new ArrayList<>();
    ArrayList<Double> radii = new ArrayList<>();
    double prevX = 0, prevY = 0;
    boolean prev = false;

    @Override
    public void start(Stage primaryStage) throws Exception{
        Line x_axis = new Line(0, 300, 600, 300);
        Line y_axis = new Line(300, 0, 300, 600);
        Circle origin = new Circle(300, 300, 5);
        root.getChildren().addAll(x_axis, y_axis, origin);
        for (int i = 0; i < 10; i++){
            angles.add(Math.random()*360);
            speeds.add((int)(Math.random()*5));
            radii.add(Math.random()*50+50);
        }

        AnimationTimer timer = new AnimationTimer(){
            long cur = 0;
            @Override
            public void handle(long now){
                update.getChildren().clear();
                double totalX = 300.0, totalY = 300.0;
                for (int i = 0; i < 5; i++){
                    Pair AB;
                    if (speeds.get(i) == 0) AB = RLine.zeroL(radii.get(i), angles.get(i));
                    else{
                        angles.set(i, (angles.get(i) + 1.0/60.0) % 360.0);
                        AB = RLine.newL(radii.get(i), speeds.get(i), angles.get(i));
                    }
                    Line connect = new Line(totalX, totalY, AB.a+totalX, AB.b+totalY);
                    totalX += AB.a;
                    totalY += AB.b;
                    update.getChildren().add(connect);
                }
                if (prev){
                    Line curve = new Line(prevX, prevY, totalX, totalY);
                    root.getChildren().add(curve);
                }
                else prev = true;
                prevX = totalX;
                prevY = totalY;
                cur = now;
            }
        };
        timer.start();
        root.getChildren().add(update);
        primaryStage.setScene(scene);
        primaryStage.show();
    }
}