package BlackjackMedKlasser.playMethods.SemiOptimalSubclasses;

public class ObviousActionsHandler {
    String[][] hardNoDouble = new String[][]

            {
                    {"s", "s", "s", "s", "s", "s", "s", "s", "s", "s"},
                    {"s", "s", "s", "s", "s", "s", "s", "s", "s", "s"},
                    {"s", "s", "s", "s", "s", "s", "s", "s", "s", "s"},
                    {"s", "s", "s", "s", "s", "s", "none", "none", "s", "s"},
                    {"none", "none", "none", "none", "s", "none", "none", "none", "none", "h"},
                    {"none", "none", "none", "none", "none", "none", "none", "none", "none", "h"},
                    {"none", "none", "none", "none", "none", "h", "h", "h", "h", "h"},
                    {"none", "none", "none", "none", "none", "h", "h", "h", "h", "h"},
                    {"none", "none", "none", "none", "none", "h", "h", "h", "h", "h"},
                    {"h", "h", "h", "h", "h", "h", "h", "h", "h", "h"},
                    {"h", "h", "h", "h", "h", "h", "h", "h", "h", "h"},
                    {"h", "h", "h", "h", "h", "h", "h", "h", "h", "h"},
                    {"h", "h", "h", "h", "h", "h", "h", "h", "h", "h"},
                    {"h", "h", "h", "h", "h", "h", "h", "h", "h", "h"},
                    {"h", "h", "h", "h", "h", "h", "h", "h", "h", "h"},
                    {"h", "h", "h", "h", "h", "h", "h", "h", "h", "h"},
                    {"h", "h", "h", "h", "h", "h", "h", "h", "h", "h"}
            };

    String[][] hardDoubleAllowed = new String[][]{
            {"s", "s", "s", "s", "s", "s", "s", "s", "s", "s"},
            {"s", "s", "s", "s", "s", "s", "s", "s", "s", "s"},
            {"s", "s", "s", "s", "s", "s", "s", "s", "s", "s"},
            {"s", "s", "s", "s", "s", "s", "none", "none", "s", "s"},
            {"none", "none", "none", "none", "s", "none", "none", "none", "none", "h"},
            {"none", "none", "none", "none", "none", "none", "none", "none", "none", "h"},
            {"none", "none", "none", "none", "none", "h", "h", "h", "h", "h"},
            {"none", "none", "none", "none", "none", "h", "h", "h", "h", "h"},
            {"none", "none", "none", "none", "none", "h", "h", "h", "h", "h"},
            {"d", "d", "d", "d", "d", "d", "d", "d", "d", "d"},
            {"none", "none", "none", "none", "none", "none", "none", "none", "h", "h"},
            {"none", "none", "none", "none", "none", "h", "h", "h", "h", "h"},
            {"h", "h", "h", "h", "none", "h", "h", "h", "h", "h"},
            {"h", "h", "h", "h", "h", "h", "h", "h", "h", "h"},
            {"h", "h", "h", "h", "h", "h", "h", "h", "h", "h"},
            {"h", "h", "h", "h", "h", "h", "h", "h", "h", "h"},
            {"h", "h", "h", "h", "h", "h", "h", "h", "h", "h"}
    };
    String[][] softNoDouble = new String[][]{
            {"s", "s", "s", "s", "s", "s", "s", "s", "s", "s"},
            {"s", "s", "s", "s", "s", "s", "s", "s", "s", "s"},
            {"none", "none", "none", "none", "none", "s", "s", "h", "none", "none"},
            {"none", "none", "none", "none", "none", "none", "h", "h", "h", "h"},
            {"none", "none", "none", "none", "none", "none", "none", "none", "none", "none"},
            {"none", "none", "none", "none", "none", "none", "none", "none", "none", "none"},
            {"h", "none", "none", "none", "none", "h", "h", "h", "h", "h"},
            {"h", "none", "none", "none", "none", "h", "h", "h", "h", "h"},
            {"h", "none", "none", "none", "none", "h", "h", "h", "h", "h"}
    };
    String[][] softDoubleAllowed = new String[][]{
            {"s", "s", "s", "s", "s", "s", "s", "s", "s", "s"},
            {"s", "s", "s", "s", "exclude_h", "s", "s", "s", "s", "s"},
            {"none", "none", "none", "none", "none", "s", "s", "h", "none", "none"},
            {"h", "h", "h", "h", "h", "h", "h", "h", "h", "h"},
            {"h", "h", "h", "h", "h", "h", "h", "h", "h", "h"},
            {"h", "h", "h", "h", "h", "h", "h", "h", "h", "h"},
            {"h", "h", "h", "h", "h", "h", "h", "h", "h", "h"},
            {"h", "h", "h", "h", "h", "h", "h", "h", "h", "h"},
            {"h", "h", "h", "h", "h", "h", "h", "h", "h", "h"}
    };
    String[][] split = new String[][]{
            {"sp", "sp", "sp", "sp", "sp", "sp", "sp", "sp", "sp", "sp"},
            {"s", "s", "s", "s", "s", "s", "s", "s", "s", "s"},
            {"s", "s", "s", "s", "s", "s", "s", "s", "s", "s"},
            {"s", "s", "s", "s", "s", "sp", "exclude_h", "s", "s", "s"},
            {"none", "none", "none", "none", "none", "none", "h", "h", "h", "h"},
            {"none", "none", "none", "none", "none", "none", "h", "h", "h", "h"},
            {"none", "none", "none", "none", "none", "none", "none", "none", "h", "h"},
            {"none", "none", "none", "none", "none", "none", "none", "h", "h", "h"},
            {"none", "none", "none", "none", "none", "none", "none", "h", "h", "h"},
            {"none", "none", "none", "none", "none", "none", "none", "h", "h", "h"}
    };

    public String getObviousAction(int playerTotal, int dealerCard, int allowedActions, int availableAces) {
        if (allowedActions == 1) {
            if (availableAces == 1) {
                return softNoDouble[20 - playerTotal][dealerCard - 2];
            } else {
                return hardNoDouble[20 - playerTotal][dealerCard - 2];
            }
        }
        if (allowedActions == 2) {
            if (availableAces == 1) {
                return softDoubleAllowed[20 - playerTotal][dealerCard - 2];
            } else {
                return hardDoubleAllowed[20 - playerTotal][dealerCard - 2];
            }
        }
        if (availableAces == 1) return "sp";
        return split[11 - (playerTotal / 2)][dealerCard - 2];
    }

}
