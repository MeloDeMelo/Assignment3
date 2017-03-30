package AIAssign3;

import java.util.Random;

/**
 * Created by maxwelldemelo on 3/30/2017.
 */
public class Classification {

    private final int NFold = 5;

    private Datapoint[] data;
    private double[] probabilityOfZeroClassOne, probabilityOfZeroClassTwo, probabilityOfZeroClassThree, probabilityOfZeroClassFour;
    private int numberOfFeatures, testingGroup;
    private Random random;

    public Classification(Datapoint[] data){
        this.random = new Random();
        this.data = data;
        this.numberOfFeatures = data[0].getFeatures().length;
        this.testingGroup = random.nextInt(NFold);
        this.probabilityOfZeroClassOne = new double[numberOfFeatures];
        this.probabilityOfZeroClassTwo = new double[numberOfFeatures];
        this.probabilityOfZeroClassThree = new double[numberOfFeatures];
        this.probabilityOfZeroClassFour = new double[numberOfFeatures];
        nFoldCrossValidation();
    }

    private void nFoldCrossValidation(){
        int numberPerGroup = data.length/NFold;
        int classOneCount = 0, classTwoCount = 0, classThreeCount = 0, classFourCount = 0;
        Datapoint currDataPoint;

        for (int i = 0; i < NFold; i++){
            if(i == testingGroup)
                continue;
            for(int k = i*numberPerGroup; k < (i+1)*numberPerGroup; k ++) {
                currDataPoint = data[k];
                switch (currDataPoint.getDataClass()) {
                    case First:
                        for (int j = 0; j < currDataPoint.getFeatures().length; j++) {
                            if (!currDataPoint.getFeatures()[j])
                                probabilityOfZeroClassOne[j]++;
                        }
                        classOneCount++;
                        break;
                    case Second:
                        for (int j = 0; j < currDataPoint.getFeatures().length; j++) {
                            if (!currDataPoint.getFeatures()[j])
                                probabilityOfZeroClassTwo[j]++;
                        }
                        classTwoCount++;
                        break;
                    case Third:
                        for (int j = 0; j < currDataPoint.getFeatures().length; j++) {
                            if (!currDataPoint.getFeatures()[j])
                                probabilityOfZeroClassThree[j]++;
                        }
                        classThreeCount++;
                        break;
                    case Fourth:
                        for (int j = 0; j < currDataPoint.getFeatures().length; j++) {
                            if (!currDataPoint.getFeatures()[j])
                                probabilityOfZeroClassFour[j]++;
                        }
                        classFourCount++;
                        break;
                }
            }
        }

        for(int i = 0; i < numberOfFeatures; i ++){
            probabilityOfZeroClassOne[i] = probabilityOfZeroClassOne[i]/classOneCount;
            probabilityOfZeroClassTwo[i] = probabilityOfZeroClassTwo[i]/classTwoCount;
            probabilityOfZeroClassThree[i] = probabilityOfZeroClassThree[i]/classThreeCount;
            probabilityOfZeroClassFour[i] = probabilityOfZeroClassFour[i]/classFourCount;
        }
    }

    public double[] getProbabilityOfZeroClassOne(){
        return probabilityOfZeroClassOne;
    }

    public double[] getProbabilityOfZeroClassTwo(){
        return probabilityOfZeroClassTwo;
    }

    public double[] getProbabilityOfZeroClassThree(){
        return probabilityOfZeroClassThree;
    }

    public double[] getProbabilityOfZeroClassFour(){
        return probabilityOfZeroClassFour;
    }

    public int getTestingGroup(){
        return testingGroup;
    }

    public void setTestingGroup(int testingGroup){
        this.testingGroup = testingGroup;
        nFoldCrossValidation();
    }
}
