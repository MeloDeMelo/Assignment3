package Test;

import AIAssign3.Classification;
import AIAssign3.Datapoint;
import org.junit.Before;
import org.junit.Test;

import java.util.Random;

/**
 * Created by Max on 31/03/2017.
 */
public class ClassificationTest {

    Classification classification;
    Datapoint[] data;
    Random random;

    @Before
    public void setUp() throws Exception{
        data = new Datapoint[8000];
        int count = 0;
        for(int k = 0; k < 5; k ++) {
            for (int i = 0; i < 4; i++) {
                for (int j = 0; j < 400; j++) {
                    data[count] = new Datapoint(Datapoint.DataClass.values()[i]);
                    count ++;
                }
            }
        }
        classification = new Classification(data);
        random = new Random();
    }

    @Test
    public void testCrossFoldValidation(){
        for(int k = 0; k < 5; k++) {
            for (int i = 0; i < 4; i++) {
                double[] probs = classification.getPorabilitiesOfZero(k, i);
                for (int l = 0; l < 10; l ++) {
                    System.out.println("Testing set: " + k + ", Class: " + i + ", Feature: " + l + ", prob: " + probs[l]);
                }
                System.out.println();
            }
            System.out.println();
            System.out.println();
        }
    }

    @Test
    public void testClassify(){
        for(int i = 0; i < 5; i ++){
            int index = i*1600 + random.nextInt(1600);
            System.out.println("Datapoint " + index + " is class: " + data[index].getDataClass());
            System.out.println("Using TestingGroup " + i + " the system classified index " + index + " as: " + classification.independentClassification(index, i));
            if(data[index].getDataClass() == classification.independentClassification(index, i))
                System.out.println("It got it right!");
            System.out.println();
        }
    }

    @Test
    public void testDependencytree(){
        for(Datapoint.DataClass dataClass : Datapoint.DataClass.values()){
            int[] dependencies = classification.guessDependencyTree(dataClass);
            System.out.print("The dependencies according to class " + dataClass + " are: ");
            for(int i = 0; i < dependencies.length; i ++){
                System.out.print(i + " depends on " + dependencies[i]);
                if(i < 9)
                    System.out.print(", ");
            }
            System.out.println();
        }
    }
}
