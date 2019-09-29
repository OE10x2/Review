import javafx.animation.AnimationTimer;
import javafx.application.Application;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Line;
import javafx.stage.Stage;
import java.io.RandomAccessFile;
import java.util.ArrayList;
import java.util.Scanner;

public class Assignment3 extends Application{
    ArrayList<Double> key = new ArrayList<>(), value = new ArrayList<>();
    //The ArrayList "key" stores the x-values in the original function;
    //The ArrayList "value" stores the y-values in the original function.
    ArrayList<Double> CFX = new ArrayList<>(), CFY = new ArrayList<>();
    double sample_start = -15, sample_end = 15, increment = 0.1;
    double round = 2.0 * Math.PI, T = 0;
    double centerX = 300, centerY = 300;
    double windowX = 600, windowY = 600;
    double drawX = 300, drawY = 300;
    int freq_start = -1000, freq_end = 1000, sample_size = 0;
    Group root = new Group(), update = new Group();
    Scene scene = new Scene(root, windowX, windowY);
    static boolean loadFile;

    public static void main(String[] args){
        Scanner In = new Scanner(System.in);
        System.out.println("Get the function from file \"FUNCTION.txt\"? T/F ");
        loadFile = In.next().equals("T");
        //If the input is "T", then load the function from the file
        launch(args);
    }

    public void loadFunction(){
        //Loop through all possible values between sample_start and sample_end with an interval of increment
        double i = sample_start;
        while (i <= sample_end){
            key.add(i);
            value.add(i * i); //Original function: f(x) = x²
            i += increment;
            sample_size++;
        }
    }

    public void loadFunction(RandomAccessFile file) throws Exception{
        String line = file.readLine();
        while (line != null){
            //If the line is not empty, then add the new set of coordinates
            String[] values = line.split(",");
            /*Check "FunctionDrawer.java" line 72; the pair of coordinates are
            written into the file separated by a comma*/
            key.add(Double.parseDouble(values[0]));
            //The first value is the x-value
            value.add(Double.parseDouble(values[1]));
            //The second value is the y-value
            sample_size++;
            //Keep track of the sample size by adding one each time a new pair is recorded
            line = file.readLine();
            //Read next line
        }
    }

    public void drawFunction(){
        for (int i = 0; i < sample_size; i++){
            Circle point = new Circle(centerX+key.get(i), centerY-value.get(i), 1);
            //With the y-coordinates of the screen reversed, the y-value must be subtracted from the origin
            point.setFill(Color.BLUE);
            //The blue-colored shape on the screen displayed is the original function
            root.getChildren().add(point);
        }
    }

    @Override
    public void start(Stage primaryStage) throws Exception{
        RandomAccessFile FILE = new RandomAccessFile("FUNCTION.txt", "r");
        //Import the file as a RandomAccessFile
        if (loadFile) loadFunction(FILE);
        //Check with user input to import points from file or not
        else loadFunction();
        drawFunction();
        Line xAxis = new Line(0, centerY, windowX, centerY);
        Line yAxis = new Line(centerX, 0, centerX, windowY);
        root.getChildren().addAll(xAxis, yAxis);
        //Draw the axis for clearer representation of the drawn functions
        double delta = 1.0/(sample_size-1);
        //This is the value of ΔT used for CFX and CFY calculations
        for (int f = freq_start; f <= freq_end; f++){
            double cfx = 0, cfy = 0;
            //Reset variables
            for (int i = 0; i < sample_size; i++){
                double t = (double)i / (double)sample_size, angle = round * f * t;
                //The value of t is based on the current "i" and sample_size
                //The angle used for sin and cos is 2πft, which is round * f * t in this case (see line 19)
                cfx += (key.get(i) * Math.cos(angle) + value.get(i) * Math.sin(angle)) * delta;
                cfy += (key.get(i) * Math.sin(angle) - value.get(i) * Math.cos(angle)) * delta;
                //Calculates cfx and cfy
            }
            CFX.add(cfx);
            CFY.add(cfy);
            //Store the calculated values into ArrayLists for future use
            drawX += cfx;
            drawY += cfy;
            /*For the AnimationTimer, the time "T" starts from zero, thus the first point it draws would
            take T = 0 into the calculations. Therefore, all the angles 2πfT would equal to zero. With sin(0) = 0
            and cos(0) = 1, the x and y values at time zero would just the sums of cfx and cfy, respectively.*/
        }

        AnimationTimer timer = new AnimationTimer(){
            @Override
            public void handle(long now){
                if (T >= 1) this.stop();
                /*After drawing one cycle, the shape is already fully drawn. Thus, drawing more after T exceeds one
                is redundant, and would only cause the program to use up more resources.*/
                update.getChildren().clear();
                double X = centerX, Y = centerY;
                for (int F = 0; F < CFX.size(); F++){
                    double angle = round * (F + freq_start) * T;
                    //"F + freq_start" finds the original frequency
                    double startX = X, startY = Y;
                    X += CFX.get(F) * Math.cos(angle) - CFY.get(F) * Math.sin(angle);
                    Y += CFX.get(F) * Math.sin(angle) + CFY.get(F) * Math.cos(angle);
                    //Calculate the x and y values of the approximated function
                    Line line = new Line(startX, startY, X, Y);
                    //Connect the spinning lines
                    update.getChildren().add(line);
                }
                Line path = new Line(drawX, drawY, X, Y);
                //Traces the path of the approximated point
                path.setStroke(Color.RED);
                //The red path on the screen is the approximated function
                root.getChildren().add(path);
                drawX = X;
                drawY = Y;
                //Store the previous points of the approximated function
                T += delta;
                //Update the time
            }
        };
        timer.start();
        root.getChildren().add(update);
        primaryStage.setScene(scene);
        primaryStage.show();
    }
}
