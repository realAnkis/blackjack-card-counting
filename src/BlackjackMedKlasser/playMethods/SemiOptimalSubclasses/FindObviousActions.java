package BlackjackMedKlasser.playMethods.SemiOptimalSubclasses;

import BlackjackMedKlasser.Settings;
import BlackjackMedKlasser.playMethods.SemiOptimal;

public class FindObviousActions {
    String[][] hardNoDouble = new String[10][17];
    String[][] hardDoubleAllowed = new String[10][17];
    String[][] softNoDouble = new String[10][9];
    String[][] softDoubleAllowed = new String[10][9];
    String[][] split = new String[10][10];

    public FindObviousActions() {
        for (int i = 0; i < 10; i++) {
            for (int j = 0; j < 17; j++) {
                hardNoDouble[i][j] = "O";
                hardDoubleAllowed[i][j] = "O";

            }
        }
        for (int i = 0; i < 10; i++) {
            for (int j = 0; j < 9; j++) {
                softNoDouble[i][j] = "O";
                softDoubleAllowed[i][j] = "O";
            }
        }
        for (int i = 0; i < 10; i++) {
            for (int j = 0; j < 10; j++) {
                split[i][j] = "O";
            }
        }
    }

    public void gatherResult(int total, int dealerCard, int allowedActions, boolean soft, String result) {
        if (allowedActions == 1) {
            if (soft) addResult(softNoDouble, dealerCard - 2, total - 12, result);
            else addResult(hardNoDouble, dealerCard - 2, total - 4, result);
        } else if (allowedActions == 2) {
            if (soft) addResult(softDoubleAllowed, dealerCard - 2, total - 12, result);
            else addResult(hardDoubleAllowed, dealerCard - 2, total - 4, result);
        }  else if (allowedActions == 3) {
            if(soft) addResult(split, dealerCard - 2, 9, result);
            else addResult(split, dealerCard - 2, total/2 - 2, result);
        }
    }

    public void addResult(String[][] array, int dealerIndex, int playerIndex, String result) {
        String currentValue = array[dealerIndex][playerIndex];
        String newValue = getNewValue(currentValue, result);
        if (newValue.equals("same")) return;
        array[dealerIndex][playerIndex] = newValue;
        printAll();
    }

    public String getNewValue(String currentValue, String newValue) {
        if (currentValue.equals("X")) return "same";
        if (currentValue.equals("E") && !newValue.equals("h")) return "same";
        if (currentValue.equals(newValue)) return "same";
        if (currentValue.equals("O")) return newValue;
        if(!newValue.equals("h")) return "E";
        return "X";
    }

    public void printAll() {
        System.out.println("hard, doubling allowed");
        print(4, hardDoubleAllowed);
        System.out.println("hard, no doubling");
        print(4, hardNoDouble);
        System.out.println("soft, doubling allowed");
        print(12, softDoubleAllowed);
        System.out.println("soft, no doubling");
        print(12, softNoDouble);
        System.out.println("pairs");
        print(2, split);
    }
    public void print(int playerIndexOffset, String[][] array) {
        System.out.print("   ");
        for (int i = 2; i < 12; i++) {
            System.out.print(i + " ");
        }

        for (int p = playerIndexOffset + array[0].length - 1; p >= playerIndexOffset; p--) {
            System.out.println();
            System.out.print(p + " ");
            for (int i = 2; i < 12; i++) {
                String obviousAction = array[i-2][p-playerIndexOffset];
                System.out.print(obviousAction + " ");
            }
        }
        System.out.println();
    }
}
