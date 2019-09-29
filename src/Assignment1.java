import javafx.animation.AnimationTimer;
import javafx.application.Application;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Line;
import javafx.stage.Stage;

public class Assignment1 extends Application{
    Group root = new Group(), disroot = new Group();
    Scene scene = new Scene(root, 600, 600);
    double theta, a, f;
    TextField radius, freq, t;
    Button radiusB, freqB, tB;

    @Override
    public void start(Stage primaryStage) throws Exception{
        a = 100.0;
        theta = 0.0;
        f = 1.0;
        radius = new TextField("100");
        radius.setTranslateX(500);
        radius.setTranslateY(500);
        radiusB = new Button("Confirm");
        radiusB.setTranslateX(500);
        radiusB.setTranslateY(550);
        radiusB.setOnAction(event -> a = Double.parseDouble(radius.getText()));

        freq = new TextField("1");
        freq.setTranslateX(500);
        freq.setTranslateY(400);
        freqB = new Button("Confirm");
        freqB.setTranslateX(500);
        freqB.setTranslateY(450);
        freqB.setOnAction(event -> f = Double.parseDouble(freq.getText()));

        t = new TextField("1");
        t.setTranslateX(500);
        t.setTranslateY(100);
        tB = new Button("Confirm");
        tB.setTranslateX(500);
        tB.setTranslateY(150);
        tB.setOnAction(event -> theta = Double.parseDouble(t.getText()));
        root.getChildren().addAll(radius, radiusB, freq, freqB, t, tB);

        AnimationTimer timer = new AnimationTimer(){
            long cur = 0;
            @Override
            public void handle(long time){
                disroot.getChildren().clear();
                theta = (theta + (time - cur)/1e9);
                update();
                cur = time;
            }
        };
        timer.start();
        root.getChildren().add(disroot);
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    public double sin(double a, double f, double t){
        return a * Math.sin(Math.toRadians(360.0 * f * t));
    }

    public double cos(double a, double f, double t){
        return a * Math.cos(Math.toRadians(360.0 * f * t));
    }

    public void update(){
        Line x_axis = new Line(0, 300, 600, 300);
        Line y_axis = new Line(300, 0, 300, 600);
        Circle origin = new Circle(300, 300, 5);
        Circle rotate = new Circle(sin(a, f, theta)+300, cos(a, f, theta)+300, 5);
        Line connect = new Line(300, 300, sin(a, f, theta)+300, cos(a, f, theta)+300);
        disroot.getChildren().addAll(x_axis, y_axis, origin, rotate, connect);
    }
}
