package BlackjackMedKlasser.playMethods.SemiOptimalSubclasses;

import BlackjackMedKlasser.Round;
import BlackjackMedKlasser.Settings;
import BlackjackMedKlasser.playMethods.SemiOptimal;

public class SimulatedRound extends SemiOptimal {
    private Settings settings;
    private final int actionSimulationAmount = 50;
    private final int actionDepthSimulationAmount = 10;

    public SimulatedRound(Settings settings) {
        super(settings);
        this.settings = settings;
    }

    @Override
    public boolean isSimulated() {
        return true;
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
