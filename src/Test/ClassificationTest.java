package Test;

import AIAssign3.Classification;
import AIAssign3.Datapoint;
import org.junit.Before;
import org.junit.Test;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.Random;

/**
 * Created by Max on 31/03/2017.
 */
public class ClassificationTest {

    final int NFold = 5;
    int numberPerGroup;
    Classification classification;
    boolean wineTest = true;
    BufferedReader br;
    Datapoint[] data;
    Random random;

    @Before
    public void setUp() throws Exception{
        if(wineTest) {
            data = new Datapoint[178];
            try {
                br = new BufferedReader(new FileReader("wine.csv"));
                String line;
                int index = 0;
                boolean[] features;
                while ((line = br.readLine()) != null) {
                    String[] wine = line.split(",");
                    features = determineWineFeatures(wine);
                    data[index] = new Datapoint(features, Integer.parseInt(wine[0]) - 1);
                    index++;
                }
            }catch (FileNotFoundException e){
                System.out.println("File wasn't found");
            }catch (IOException e){
                System.out.println("IO exception");
            }
            br.close();
        }
        else{
            data = new Datapoint[8000];
            int count = 0;
            for (int k = 0; k < 5; k++) {
                for (int i = 0; i < 4; i++) {
                    for (int j = 0; j < 400; j++) {
                        data[count] = new Datapoint(Datapoint.DataClass.values()[i]);
                        count++;
                    }
                }
            }
        }
        numberPerGroup = data.length/NFold;
        classification = new Classification(data, NFold);
        random = new Random();
    }

    private boolean[] determineWineFeatures(String[] wine){
        boolean[] features = new boolean[13];
        double[] values = new double[13];
        double[] averages = new double[] {13.00, 2.33, 2.36, 19.49, 99.74, 2.29, 2.03, 0.36, 1.59, 5.06, 0.96, 2.61, 746.89};
        for(int i = 0; i < 13; i ++){
            values[i] = Double.parseDouble(wine[i+1]);
            if (values[i] >= averages[i])
                features[i] = true;
            else
                features[i] = false;
        }
        return features;
    }

    @Test
    public void testCrossFoldValidation(){
        for(int k = 0; k < NFold; k++) {
            for (int i = 0; i < 4; i++) {
                double[] probs = classification.getPorabilitiesOfZero(k, i);
                for (int l = 0; l < data[0].getFeatures().length; l ++) {
                    System.out.println("Testing set: " + k + ", Class: " + i + ", Feature: " + l + ", prob: " + probs[l]);
                }
                System.out.println();
            }
            System.out.println();
            System.out.println();
        }
    }

    @Test
    public void testIndependentClassification(){
        for(int i = 0; i < NFold; i ++){
            int index = i*numberPerGroup + random.nextInt(numberPerGroup);
            System.out.println("Datapoint " + index + " is class: " + data[index].getDataClass());
            System.out.println("Using TestingGroup " + i + " the system independently classified index " + index + " as: " + classification.independentClassification(index, i));
            if(data[index].getDataClass() == classification.independentClassification(index, i))
                System.out.println("It got it right!");
            System.out.println();
        }

        System.out.println("The system using independent classification is on average " + classification.baysianIndependenet()* 100 + "% accurate");
    }

    @Test
    public void testDependentClassification(){
        for(int i = 0; i < NFold; i ++){
            int index = i*numberPerGroup + random.nextInt(numberPerGroup);
            System.out.println("Datapoint " + index + " is class: " + data[index].getDataClass());
            System.out.println("Using TestingGroup " + i + " the system dependently classified index " + index + " as: " + classification.dependenetClassification(index, i));
            if(data[index].getDataClass() == classification.dependenetClassification(index, i))
                System.out.println("It got it right!");
            System.out.println();
        }

        System.out.println("The System using dependent classification is on average " + classification.baysianDependent() * 100 + "% accurate");
    }

    @Test
    public void testDependencytree(){
        for(Datapoint.DataClass dataClass : Datapoint.DataClass.values()){
            int[] dependencies = classification.guessDependencyTree(dataClass);
            System.out.print("The dependencies according to class " + dataClass + " are: ");
            for(int i = 0; i < dependencies.length; i ++){
                System.out.print(dependencies[i]);
                if(i < 9)
                    System.out.print(", ");
            }
            System.out.println();
        }

        System.out.print("The system decided on: ");
        for(int i = 0; i < classification.getDependencies().length; i ++){
            System.out.print(classification.getDependencies()[i]);
            if(i < 9)
                System.out.print(", ");
        }
    }
}
