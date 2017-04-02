package AIAssign3;

import static AIAssign3.Datapoint.DataClass.*;
import AIAssign3.Datapoint.DataClass;
import java.util.Random;


/**
 * Created by maxwelldemelo on 3/30/2017.
 */
public class Classification {

    private final int NFold = 5;

    private Datapoint[] data;
    private double[][][] probabilityOfZeroTraining;
    private int numberOfFeatures, numberPerGroup, numberOfClasses;
    private Random random;

    public Classification(Datapoint[] data){
        this.random = new Random();
        this.data = data;
        this.numberOfFeatures = data[0].getFeatures().length;
        this.numberPerGroup = data.length/NFold;
        this.numberOfClasses = DataClass.values().length;
        probabilityOfZeroTraining = new double[NFold][numberOfClasses][numberOfFeatures];
        trainingProbabilitiesInit();
    }

    private void trainingProbabilitiesInit(){
        int[] classCount = new int[numberOfClasses];
        int classNumber = 0;
        Datapoint currDataPoint;

        for (int testingGroup = 0; testingGroup < NFold; testingGroup++) {
            for (int i = 0; i < NFold; i++) {
                if (i == testingGroup)
                    continue;
                for (int k = i * numberPerGroup; k < (i + 1) * numberPerGroup; k++) {
                    currDataPoint = data[k];
                    for (int classNum = 0; classNum < numberOfClasses; classNum++) {
                        if (currDataPoint.getDataClass() == DataClass.values()[classNum]) {
                            classNumber = classNum;
                            break;
                        }
                    }
                    for (int j = 0; j < numberOfFeatures; j++) {
                        if (!currDataPoint.getFeatures()[j])
                            probabilityOfZeroTraining[testingGroup][classNumber][j]++;
                    }
                    classCount[classNumber]++;
                }
                for(int k = 0; k < numberOfFeatures; k ++) {
                    for (int l = 0; l < classCount.length; l++) {
                        probabilityOfZeroTraining[testingGroup][l][k] = probabilityOfZeroTraining[testingGroup][l][k] / classCount[l];
                    }
                }

                for(int m = 0; m < classCount.length; m ++){
                    classCount[m] = 0;
                }
            }
        }
    }

    public DataClass independentClassification(int index, int testingGroup){
        Datapoint point= data[index];
        double[] prob = new double[numberOfClasses];

        for(int i = 0; i < numberOfClasses; i++)
            prob[i] = 1;

        for(int i = 0; i < numberOfClasses; i++){
            for(int k = 0; k < numberOfFeatures; k ++) {
                if (point.getFeatures()[k])
                    prob[i] = prob[i] * probabilityOfZeroTraining[testingGroup][i][k];
                else
                    prob[i] = prob[i] * (1 - probabilityOfZeroTraining[testingGroup][i][k]);
            }
        }

        if((prob[0] >= prob[1]) && (prob[0] >= prob[2]) && (prob[0] >= prob[3]))
            return First;
        else if((prob[1] >= prob[2]) && (prob[1] >= prob[3]))
            return Second;
        else if(prob[2] >= prob[3])
            return Third;
        else
            return Fourth;
    }

    public double[] getPorabilitiesOfZero(int testingGroup, int classNum){
        return probabilityOfZeroTraining[testingGroup][classNum];
    }

    /*// Calculates the entropy of all S based on test data
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
            return 0;
        else if(negativeExamples == positiveExamples)
            return 1;
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

        return (entropy - (Math.abs(sv0/numberPerGroup)*entropy0 + (Math.abs(numberPerGroup-sv0)/numberPerGroup)*entropy1));
    }*/
}
