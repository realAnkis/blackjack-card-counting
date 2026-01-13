package BlackjackMedKlasser.playMethods.SemiOptimalSubclasses;

import BlackjackMedKlasser.Round;
import BlackjackMedKlasser.Settings;
import BlackjackMedKlasser.playMethods.PlayMethod;
import BlackjackMedKlasser.playMethods.SemiOptimal;

public class SimulatedRound extends SemiOptimal {
    private Settings settings;

    public SimulatedRound(Settings settings) {
        super(settings);
    }

    //körs när det ursprungliga bettet ska bestämmas
    @Override
    public int betMethod(Round round) {
        return settings.getMinBet();
    }

    /*
    @Override
    public String actionMethod(Round round, int allowedActions, int handIndex) {
        return parentPlayMethod.actionMethod(round,allowedActions,handIndex);
    }
    */
}
