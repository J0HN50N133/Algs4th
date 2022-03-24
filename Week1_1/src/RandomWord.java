import edu.princeton.cs.algs4.StdIn;
import edu.princeton.cs.algs4.StdOut;
import edu.princeton.cs.algs4.StdRandom;

public class RandomWord {
    public static void main(String[] args) {
        String champion = "";
        int i = 1;
        while(!StdIn.isEmpty()){
            String next = StdIn.readString();
            if(StdRandom.bernoulli(1.0/(double) i)){
                champion = next;
            }
            i++;
        }
        StdOut.println(champion);
    }
}
