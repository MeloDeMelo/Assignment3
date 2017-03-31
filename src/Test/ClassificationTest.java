package Test;

import AIAssign3.Classification;
import AIAssign3.Datapoint;
import org.junit.Before;
import org.junit.Test;

/**
 * Created by Max on 31/03/2017.
 */
public class ClassificationTest {

    Classification classification;
    Datapoint[] data;

    @Before
    public void setUp() throws Exception{
        data = new Datapoint[8000];
        for(int i = 0; i < 4; i ++){
            for(int j = i*2000; j < (i+1)*2000; j ++){
                data[j] = new Datapoint(Datapoint.DataClass.values()[i]);
            }
        }
        classification = new Classification(data);
        classification.setTestingGroup(2);
    }

    @Test
    public void testFoldCrossValidation(){
        System.out.print("Probabilities for class one: ");
        for(int i = 0; i < 10; i ++){
            System.out.print(classification.getProbabilityOfZeroClassOne()[i]);
            if(i != 9)
                System.out.print(", ");
        }
        System.out.println();
        System.out.print("Probabilities for class two: ");
        for(int i = 0; i < 10; i ++){
            System.out.print(classification.getProbabilityOfZeroClassTwo()[i]);
            if(i != 9)
                System.out.print(", ");
        }
        System.out.println();
        System.out.print("Probabilities for class three: ");
        for(int i = 0; i < 10; i ++){
            System.out.print(classification.getProbabilityOfZeroClassThree()[i]);
            if(i != 9)
                System.out.print(", ");
        }
        System.out.println();
        System.out.print("Probabilities for class four: ");
        for(int i = 0; i < 10; i ++){
            System.out.print(classification.getProbabilityOfZeroClassFour()[i]);
            if(i != 9)
                System.out.print(", ");
        }
        System.out.println();
    }

    @Test
    public void testClassify(){
        int index = 7999;
        System.out.println("Datapoint " + index + " is class: " + data[index].getDataClass());
        System.out.println("The system classified it as class: " + classification.independentClassification(index));
    }
}
