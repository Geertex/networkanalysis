package cwts.networkanalysis;

public class ArrayBundle {
    double[] doubleArray;
    int[] integerArray;
    int entryNumber;

    public ArrayBundle(double[] doubleArray, int[] integerArray, int entryNumber) {
        this.doubleArray = doubleArray;
        this.integerArray = integerArray;
        this.entryNumber = entryNumber;
    }
}