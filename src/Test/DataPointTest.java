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

    @Before
    public void setUp() throws Exception{
        dp1 = new Datapoint(Datapoint.DataClass.First);
        dp2 = new Datapoint(Datapoint.DataClass.Second);
        dp3 = new Datapoint(Datapoint.DataClass.Third);
        dp4 = new Datapoint(Datapoint.DataClass.Fourth);
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
}
