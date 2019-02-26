package cwts.networkanalysis;

public class ArrayShop {
    double[][] doubleArrays;
    int[][] integerArrays;
    boolean[] bookings;
    int numberOfArrays, numberOfNodes;

    ArrayShop(int numberOfArrays, int numberOfNodes) {
        doubleArrays = new double[numberOfArrays][numberOfNodes];
        integerArrays = new int[numberOfArrays][numberOfNodes];
        bookings = new boolean[numberOfArrays];
        this.numberOfArrays = numberOfArrays;
        this.numberOfNodes = numberOfNodes;
    }

    public Object[] getArrays(){
        for(int i = 0; i <numberOfArrays; i++){
            if(!bookings[i]){
                bookings[i] = true;
                return new Object[]{doubleArrays[i],integerArrays[i], i};
            }
        }
        return new Object[]{null, null, null};
    }

    public void returnArrays(int arraysNumber){
        bookings[arraysNumber] = false;
    }
}