package Test;

import AIAssign3.Datapoint;
import org.junit.Before;
import org.junit.Test;

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
        System.out.println(dp2);
        System.out.println(dp3);
        System.out.println(dp4);
    }
}
