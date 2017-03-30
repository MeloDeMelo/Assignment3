package Test;

import AIAssign3.Classification;
import AIAssign3.Datapoint;
import org.junit.Before;
import org.junit.Test;

import static junit.framework.TestCase.*;

/**
 * Created by Max on 3/29/2017.
 */
public class DataPointTest {

    Datapoint dp1, dp2, dp3, dp4;
    Classification classification;
    Datapoint[] data;

    @Before
    public void setUp() throws Exception{
        dp1 = new Datapoint(Datapoint.DataClass.First);
        dp2 = new Datapoint(Datapoint.DataClass.Second);
        dp3 = new Datapoint(Datapoint.DataClass.Third);
        dp4 = new Datapoint(Datapoint.DataClass.Fourth);
        data = new Datapoint[8000];
        for(int i = 0; i < 4; i ++){
            for(int j = i*2000; j < (i+1)*2000; j ++){
                data[j] = new Datapoint(Datapoint.DataClass.values()[i]);
            }
        }
        classification = new Classification(data);
    }

    @Test
    public void dataPointInit(){
        System.out.println(dp1);
        assertTrue(dp1.getFeatures().length == 10);
        System.out.println(dp2);
        assertTrue(dp2.getFeatures().length == 10);
        System.out.println(dp3);
        assertTrue(dp3.getFeatures().length == 10);
        System.out.println(dp4);
        assertTrue(dp4.getFeatures().length == 10);
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
}
