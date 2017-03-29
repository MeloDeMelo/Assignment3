package Test;

import AIAssign3.Datapoint;
import org.junit.Before;
import org.junit.Test;

/**
 * Created by Max on 3/29/2017.
 */
public class DataPointTest {

    Datapoint dp1;

    @Before
    public void setUp() throws Exception{
        dp1 = new Datapoint(Datapoint.DataClass.First);
    }

    @Test
    public void dataPointInit(){
        System.out.println(dp1);
    }
}
