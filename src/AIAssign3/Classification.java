package AIAssign3;

import static AIAssign3.Datapoint.DataClass.*;
import AIAssign3.Datapoint.DataClass;


/**
 * Created by maxwelldemelo on 3/30/2017.
 */
public class Classification {

    private final int NFold = 5;

    private Datapoint[] data;
    private double[][][] probabilityOfZeroTraining;
    private double[][][] probabilityOfZeroTrainingDependent;
    private int numberOfFeatures, numberPerGroup, numberOfClasses;
    private int[] dependencies;

    public Classification(Datapoint[] data){
        this.data = data;
        this.numberOfFeatures = data[0].getFeatures().length;
        this.numberPerGroup = data.length/NFold;
        this.numberOfClasses = DataClass.values().length;
        this.dependencies = new int[numberOfFeatures];
        probabilityOfZeroTraining = new double[NFold][numberOfClasses][numberOfFeatures];
        trainingProbabilitiesInit();
        determineDependencyTree();
        trainingProbabilitiesDependentInit();
    }

    private void trainingProbabilitiesDependentInit(){
        int numberOfProbs = 0;
        for(int i = 0; i < dependencies.length; i ++){
            if(dependencies[i] != -1)
                numberOfProbs += 2;
            else
                numberOfProbs ++;
        }

        probabilityOfZeroTrainingDependent = new double[NFold][numberOfClasses][numberOfProbs];
        int[] classCount = new int[numberOfClasses];
        int[][] dependencyOccurenceZero = new int[numberOfClasses][numberOfFeatures];
        int classNumber;
        Datapoint currDataPoint;

        for(int testingGroup = 0; testingGroup < NFold; testingGroup++){
            for(int i = 0; i < NFold; i ++){
                if(i == testingGroup)
                    continue;
                for(int k = i * numberPerGroup; k < (i + 1) * numberPerGroup; k++) {
                    currDataPoint = data[k];
                    classNumber = determineClassNumber(k);
                    for (int feature = 0; feature < numberOfFeatures; feature ++) {
                        if(dependencies[feature] == -1){
                            if (!currDataPoint.getFeatures()[feature])
                                probabilityOfZeroTrainingDependent[testingGroup][classNumber][0]++;
                        }
                        else{
                            if(currDataPoint.getFeatures()[dependencies[feature]]) {
                                if (!currDataPoint.getFeatures()[feature])
                                    probabilityOfZeroTrainingDependent[testingGroup][classNumber][(feature+1) * 2 - 3]++;
                            }
                            else {
                                if (!currDataPoint.getFeatures()[feature]) {
                                    probabilityOfZeroTrainingDependent[testingGroup][classNumber][(feature + 1) * 2 - 2]++;
                                }
                                dependencyOccurenceZero[classNumber][feature]++;
                            }
                        }
                    }
                    classCount[classNumber]++;
                }
            }
            for(int feature = 0; feature < numberOfFeatures; feature++) {
                for (int classNum = 0; classNum < numberOfClasses; classNum++) {
                    if (dependencies[feature] == -1) {
                        probabilityOfZeroTrainingDependent[testingGroup][classNum][0] = probabilityOfZeroTrainingDependent[testingGroup][classNum][0] / classCount[classNum];
                    } else {
                        probabilityOfZeroTrainingDependent[testingGroup][classNum][(feature + 1) * 2 - 3] = probabilityOfZeroTrainingDependent[testingGroup][classNum][(feature + 1) * 2 - 3] / (classCount[classNum] - dependencyOccurenceZero[classNum][feature]);
                        probabilityOfZeroTrainingDependent[testingGroup][classNum][(feature + 1) * 2 - 2] = probabilityOfZeroTrainingDependent[testingGroup][classNum][(feature + 1) * 2 - 2] / dependencyOccurenceZero[classNum][feature];
                    }
                }
            }
            classCount = new int[numberOfClasses];
            dependencyOccurenceZero = new int[numberOfClasses][numberOfFeatures];
        }
    }

    public DataClass dependenetClassification(int index, int testingGroup){
        Datapoint point= data[index];
        double[] prob = new double[numberOfClasses];

        for(int i = 0; i < numberOfClasses; i++)
            prob[i] = 1;

        for(int i = 0; i < numberOfClasses; i++){
            for(int k = 0; k < numberOfFeatures; k ++) {
                if(dependencies[k] == -1) {
                    if (!point.getFeatures()[k])
                        prob[i] = prob[i] * probabilityOfZeroTrainingDependent[testingGroup][i][k];
                    else
                        prob[i] = prob[i] * (1 - probabilityOfZeroTrainingDependent[testingGroup][i][k]);
                }
                else{
                    if(point.getFeatures()[dependencies[k]]){
                        if (!point.getFeatures()[k])
                            prob[i] = prob[i] * probabilityOfZeroTrainingDependent[testingGroup][i][(k+1)*2-3];
                        else
                            prob[i] = prob[i] * (1 - probabilityOfZeroTrainingDependent[testingGroup][i][(k+1)*2-3]);
                    }
                    else{
                        if (!point.getFeatures()[k])
                            prob[i] = prob[i] * probabilityOfZeroTrainingDependent[testingGroup][i][(k+1)*2-2];
                        else
                            prob[i] = prob[i] * (1 - probabilityOfZeroTrainingDependent[testingGroup][i][(k+1)*2-2]);
                    }
                }
            }
        }

        return pickMaxProb(prob);
    }

    private DataClass pickMaxProb(double[] prob){
        double max = prob[0];
        int index = 0;
        for(int i = 1; i < prob.length; i ++){
            if(prob[i] > max){
                max = prob[i];
                index = i;
            }
        }
        return DataClass.values()[index];
    }

    private int determineClassNumber(int index){
        for (int classNum = 0; classNum < numberOfClasses; classNum++) {
            if (data[index].getDataClass() == DataClass.values()[classNum]) {
                return classNum;
            }
        }
        return -1;
    }

    private void trainingProbabilitiesInit(){
        int[] classCount = new int[numberOfClasses];
        int classNumber;
        Datapoint currDataPoint;

        for (int testingGroup = 0; testingGroup < NFold; testingGroup++) {
            for (int i = 0; i < NFold; i++) {
                if (i == testingGroup)
                    continue;
                for (int k = i * numberPerGroup; k < (i + 1) * numberPerGroup; k++) {
                    currDataPoint = data[k];
                    classNumber = determineClassNumber(k);
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
                if (!point.getFeatures()[k])
                    prob[i] = prob[i] * probabilityOfZeroTraining[testingGroup][i][k];
                else
                    prob[i] = prob[i] * (1 - probabilityOfZeroTraining[testingGroup][i][k]);
            }
        }

        return pickMaxProb(prob);
    }

    public double[] getPorabilitiesOfZero(int testingGroup, int classNum){
        return probabilityOfZeroTraining[testingGroup][classNum];
    }

    public double baysianIndependenet(){
        double accurary = 0;
        double[] classAccuracy = new double[NFold];

        for(int i = 0; i < NFold; i ++){
            for(int k = i*numberPerGroup; k < (i+1)*numberPerGroup; k ++){
                if(data[k].getDataClass() == independentClassification(k, i))
                    classAccuracy[i] ++;
            }
            classAccuracy[i] = classAccuracy[i]/numberPerGroup;
            accurary += classAccuracy[i];
        }

        return accurary/NFold;
    }

    public double baysianDependent(){
        double accurary = 0;
        double[] classAccuracy = new double[NFold];

        for(int i = 0; i < NFold; i ++){
            for(int k = i*numberPerGroup; k < (i+1)*numberPerGroup; k ++){
                if(data[k].getDataClass() == dependenetClassification(k, i))
                    classAccuracy[i] ++;
            }
            classAccuracy[i] = classAccuracy[i]/numberPerGroup;
            accurary += classAccuracy[i];
        }

        return accurary/NFold;
    }

    public int[] guessDependencyTree(DataClass dataClass){
        double[][] weights = new double[numberOfFeatures-1][];
        int[] dependencies = new int[numberOfFeatures];

        for(int i = 0; i < numberOfFeatures-1; i ++)
            weights[i] = new double[i + 1];

        for(int i = 0; i < numberOfFeatures-1; i ++){
            for(int k = 0; k < weights[i].length; k ++){
                weights[i][k] = weight(k, weights[i].length, dataClass);
            }
        }

        dependencies[0] = -1;

        double maxValue;
        double maxIndex;
        for(int i = 0; i < numberOfFeatures-1; i ++){
            maxIndex = 0;
            maxValue = weights[i][0];
            for(int k = 1; k < weights[i].length; k ++){
                if(weights[i][k] > maxValue) {
                    maxIndex = k;
                    maxValue = weights[i][k];
                }
            }
            dependencies[i+1] = (int)maxIndex;
        }

        return dependencies;
    }

    public void determineDependencyTree(){
        int[][] dependencyTrees = new int[numberOfClasses][];
        double max = 0;
        int index = 0;
        for(int i = 0; i < numberOfClasses; i ++){
            dependencyTrees[i] = guessDependencyTree(DataClass.values()[i]);
            if(max < determineAccuracyOfTree(dependencyTrees[i])){
                max = determineAccuracyOfTree(dependencyTrees[i]);
                index = i;
            }
        }

        dependencies = dependencyTrees[index];
    }

    private double determineAccuracyOfTree(int[] dependencies){
        int count = 0;
        for(int i = 0; i < numberOfFeatures; i ++){
            if(dependencies[i] == data[0].getDependencies()[i])
                count ++;
        }
        return count/numberOfFeatures;
    }

    private double weight(int feature1, int feature2, DataClass dataClass){
        double trueTrue = weightCalulation(feature1, feature2, true, true, dataClass);
        double trueFalse = weightCalulation(feature1, feature2, true, false, dataClass);
        double falseTrue = weightCalulation(feature1, feature2, false, true, dataClass);
        double falseFalse = weightCalulation(feature1, feature2, false, false, dataClass);
        return (trueTrue + trueFalse + falseFalse + falseTrue);
    }

    private double weightCalulation(int feature1, int feature2, boolean feature1Value, boolean feature2Value, DataClass dataClass){
        int feature1Count = 0, feature2Count = 0, feature1And2Count = 0;
        double numOfData = data.length;
        for(int i = 0; i < numOfData; i ++){
            if(data[i].getDataClass() == dataClass) {
                if (data[i].getFeatures()[feature1] == feature1Value)
                    feature1Count++;
                if (data[i].getFeatures()[feature2] == feature2Value)
                    feature2Count++;
                if ((data[i].getFeatures()[feature1] == feature1Value) && (data[i].getFeatures()[feature2] == feature2Value))
                    feature1And2Count++;
            }
        }
        double vrBoth = feature1And2Count/numOfData;
        double vr1 = feature1Count/numOfData;
        double vr2 = feature2Count/numOfData;
        if ((vr1 == 0) || (vr2 == 0))
            return 0;
        return(vrBoth * Math.log(vrBoth/(vr1 * vr2)));
    }

    public int[] getDependencies(){
        return dependencies;
    }
}
