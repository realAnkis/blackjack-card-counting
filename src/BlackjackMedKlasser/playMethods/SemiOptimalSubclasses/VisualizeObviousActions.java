package BlackjackMedKlasser.playMethods.SemiOptimalSubclasses;

import BlackjackMedKlasser.Game;
import BlackjackMedKlasser.Settings;
import BlackjackMedKlasser.playMethods.SemiOptimal;

public class VisualizeObviousActions {
    public static void main(String[] args) {
        new VisualizeObviousActions();
    }

    public VisualizeObviousActions() {
        System.out.println("hard, doubling allowed");
        print(2,0);
        System.out.println("hard, no doubling");
        print(1,0);
        System.out.println("soft, doubling allowed");
        print(2,1);
        System.out.println("soft, no doubling");
        print(1,1);
        System.out.println("pairs");
        print(3,0);
    }

    public void print(int allowedActions, int availableAces) {
        SemiOptimal so = new SemiOptimal(new Settings(), new Game());
        System.out.print("   ");
        for (int i = 2; i < 12; i++) {
            System.out.print(i + " ");
        }

        for (int p = 20; p > 3; p--) {
            if(availableAces == 1 && p == 11) break;
            if(allowedActions == 3 && p % 2 == 1) continue;
            System.out.println();
            System.out.print(p + " ");
            for (int i = 2; i < 12; i++) {
                String obviousAction = so.obviousActionsHandler.getObviousAction(p, i, allowedActions, availableAces);
                if(obviousAction.equals("none")) obviousAction = "-";
                if(obviousAction.equals("exclude_h")) obviousAction = "e";
                if(obviousAction.equals("sp")) obviousAction = "S";
                System.out.print(obviousAction + " ");
            }
        }
        System.out.println();
    }
}
