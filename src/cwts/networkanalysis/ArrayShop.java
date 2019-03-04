package cwts.networkanalysis;

public class ArrayShop {
    double[][] doubleArrays;
    int[][] integerArrays;
    boolean[] bookings;
    int numberOfArrays, numberOfNodes;
    ArrayBundle[] arrayBundles;

    ArrayShop(int numberOfArrays, int numberOfNodes) {
        doubleArrays = new double[numberOfArrays][numberOfNodes];
        integerArrays = new int[numberOfArrays][numberOfNodes];
        bookings = new boolean[numberOfArrays];
        this.numberOfArrays = numberOfArrays;
        this.numberOfNodes = numberOfNodes;
        arrayBundles = new ArrayBundle[numberOfArrays];
        for(int i = 0; i <numberOfArrays; i++){
            arrayBundles[i] = new ArrayBundle(doubleArrays[i],integerArrays[i], i);
        }
    }

    public ArrayBundle getArrays(){
        for(int i = 0; i <numberOfArrays; i++){
            if(!bookings[i]){
                bookings[i] = true;
                return arrayBundles[i];
            }
        }
        return null;
    }

    public void returnArrays(int arraysNumber){
        bookings[arraysNumber] = false;
    }
}