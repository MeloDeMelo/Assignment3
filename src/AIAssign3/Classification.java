package AIAssign3;

import static AIAssign3.Datapoint.DataClass.*;
import AIAssign3.Datapoint.DataClass;

import java.nio.file.FileStore;
import java.util.Random;


/**
 * Created by maxwelldemelo on 3/30/2017.
 */
public class Classification {

    private final int NFold = 5;

    private Datapoint[] data;
    private double[] probabilityOfZeroClassOne, probabilityOfZeroClassTwo, probabilityOfZeroClassThree, probabilityOfZeroClassFour;
    private int numberOfFeatures, testingGroup, numberPerGroup;
    private Random random;

    public Classification(Datapoint[] data){
        this.random = new Random();
        this.data = data;
        this.numberOfFeatures = data[0].getFeatures().length;
        this.numberPerGroup = data.length/NFold;
        this.testingGroup = random.nextInt(NFold);
        this.probabilityOfZeroClassOne = new double[numberOfFeatures];
        this.probabilityOfZeroClassTwo = new double[numberOfFeatures];
        this.probabilityOfZeroClassThree = new double[numberOfFeatures];
        this.probabilityOfZeroClassFour = new double[numberOfFeatures];
        nFoldCrossValidation();
    }

    private void nFoldCrossValidation(){
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

    // Calculates the entropy of all S based on test data
    public double entropy(){
        double positiveExamples = 0, negativeExamples;

        for(int i = testingGroup*numberPerGroup; i < (testingGroup + 1)*numberPerGroup; i ++){
            if(data[i].getDataClass() == independentClassification(i))
                positiveExamples ++;
        }
        negativeExamples = (numberPerGroup - positiveExamples) / numberPerGroup;
        positiveExamples = positiveExamples / numberPerGroup;

        return (-(positiveExamples) * ((Math.log10(positiveExamples))/(Math.log10(2)))
                - (negativeExamples) *((Math.log10(negativeExamples))/(Math.log10(2))));
    }

    // Calculates the entropy based on a subset of S
    public double entropy(int feature, boolean value){
        double positiveExamples = 0, negativeExamples, total = 0;

        for(int i = testingGroup*numberPerGroup; i < (testingGroup + 1)*numberPerGroup; i ++){
            if(data[i].getFeatures()[feature] == value) {
                if (data[i].getDataClass() == independentClassification(i))
                    positiveExamples++;
                total ++;
            }
        }
        negativeExamples = (total - positiveExamples) / total;
        positiveExamples = positiveExamples / total;

        if((negativeExamples == 1) || (positiveExamples == 1))
            return 1;
        else if(negativeExamples == positiveExamples)
            return 0;
        else
            return (-(positiveExamples) * ((Math.log10(positiveExamples))/(Math.log10(2))) - (negativeExamples) *((Math.log10(negativeExamples))/(Math.log10(2))));
    }

    public double gain(int feature){
        double entropy = entropy();
        double entropy0 = entropy(feature, false);
        double entropy1 = entropy(feature, true);
        double sv0 = 0;

        for(int i = testingGroup*numberPerGroup; i < (testingGroup + 1)*numberPerGroup; i ++){
            if(data[i].getFeatures()[feature] == false)
                sv0 ++;
        }

        return (entropy - ((sv0/numberPerGroup)*entropy0 + ((numberPerGroup-sv0)/numberPerGroup)*entropy1));
    }

    public DataClass independentClassification(int index){
        Datapoint point= data[index];
        double prob1, prob2, prob3, prob4;
        if(point.getFeatures()[0]){
            prob1 = probabilityOfZeroClassOne[0];
            prob2 = probabilityOfZeroClassTwo[0];
            prob3 = probabilityOfZeroClassThree[0];
            prob4 = probabilityOfZeroClassFour[0];
        }
        else{
            prob1 = (1 - probabilityOfZeroClassOne[0]);
            prob2 = (1 - probabilityOfZeroClassTwo[0]);
            prob3 = (1 - probabilityOfZeroClassThree[0]);
            prob4 = (1 - probabilityOfZeroClassFour[0]);
        }
        for(int i = 1; i < point.getFeatures().length; i ++){
            if(point.getFeatures()[i]){
                prob1 = prob1 * probabilityOfZeroClassOne[0];
                prob2 = prob2 * probabilityOfZeroClassTwo[0];
                prob3 = prob3 * probabilityOfZeroClassThree[0];
                prob4 = prob4 * probabilityOfZeroClassFour[0];
            }
            else{
                prob1 = (1 - probabilityOfZeroClassOne[0]);
                prob2 = (1 - probabilityOfZeroClassTwo[0]);
                prob3 = (1 - probabilityOfZeroClassThree[0]);
                prob4 = (1 - probabilityOfZeroClassFour[0]);
            }
        }

        if((prob1 >= prob2) && (prob1 >= prob3) && (prob1 >= prob4))
            return First;
        else if((prob2 >= prob3) && (prob2 >= prob4))
            return Second;
        else if(prob3 >= prob4)
            return Third;
        else
            return Fourth;
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
