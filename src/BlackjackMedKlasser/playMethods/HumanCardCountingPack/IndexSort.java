package BlackjackMedKlasser.playMethods.HumanCardCountingPack;

import java.util.Comparator;

public class IndexSort implements Comparator<int[]> {
    @Override
    public int compare(int[] o1, int[] o2) {
        return o1[0]-o2[0];
    }
    // Used temporaroly to sort HiLoIndexTabel, left if needed again later
}
