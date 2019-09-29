public class RLine{
    public Pair newL(double radius, int freq, double theta){
        double a = radius * Math.sin(2.0 * Math.PI * freq * theta);
        double b = radius * Math.cos(2.0 * Math.PI * freq * theta);
        return new Pair(a, b);
    }
    public Pair zeroL(double radius, double theta){
        double a = radius * Math.sin(2.0 * Math.PI * theta);
        double b = radius * Math.cos(2.0 * Math.PI * theta);
        return new Pair(a, b);
    }
}
