package AIAssign3;

import java.util.Random;

/**
 * Created by Max on 3/29/2017.
 */
public class Datapoint {

    private final int[] dependencies = new int[]{-1, 0, 0, 1, 1, 2, 2, 3, 6, 6};

    private boolean[] features;
    private DataClass dataClass;
    private Random random;

    public Datapoint(DataClass dataClass){
        this.dataClass = dataClass;
        this.random = new Random();
        featuresInit();
    }

    private void featuresInit() {
        features = new boolean[10];
        int randomNum = random.nextInt(101);

        if(randomNum <= dataClass.getInitialProbability())
            features[0] = false;
        else
            features[0] = true;

        for(int i = 1; i < 10; i ++){
            randomNum = random.nextInt(101);
            if(randomNum <= dataClass.getProbability(i + 1, features[dependencies[i]]))
                features[i] = false;
            else
                features[i] = true;
        }
    }

    public boolean[] getFeatures(){
        return features;
    }

    public DataClass getDataClass(){
        return dataClass;
    }

    public String toString(){
        String result = "DataPoint: ";
        for(int i = 0; i < 10; i ++){
            if (features[i])
                result += "1";
            else
                result += "0";
            if (i < 9)
                result += ", ";
        }
        return result;
    }

    public enum DataClass {
        First(new int[] {80, 55, 45, 32, 48, 9, 43, 14, 73, 19, 2, 70, 46, 16, 59, 50, 76, 84, 48}),
        Second(new int[] {1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12, 13, 14, 15, 16, 17, 18, 19}),
        Third(new int[] {1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12, 13, 14, 15, 16, 17, 18, 19}),
        Fourth(new int[] {1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12, 13, 14, 15, 16, 17, 18, 19});

        private int[] probabilities;

        DataClass(int[] probabilities){
            this.probabilities = probabilities.clone();
        }

        public int getProbability(int feature, boolean dependency){
            if(dependency)
                return probabilities[feature*2-3];
            else
                return probabilities[feature*2-2];
        }

        public int getInitialProbability(){
            return probabilities[0];
        }
    }
}
