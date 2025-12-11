package BlackjackMedKlasser.playMethods.SemiOptimalSubclasses;

import BlackjackMedKlasser.Round;
import BlackjackMedKlasser.Settings;
import BlackjackMedKlasser.playMethods.PlayMethod;
import BlackjackMedKlasser.playMethods.SemiOptimal;

public class SimulatedRound extends PlayMethod {
    private Settings settings;
    private SemiOptimal parentPlayMethod;


    public SimulatedRound(Settings settings, SemiOptimal parentPlayMethod) {
        this.settings = settings;
        this.parentPlayMethod = parentPlayMethod;
    }

    //körs när det ursprungliga bettet ska bestämmas
    @Override
    public int betMethod(Round round) {
        return settings.getMinBet();
    }

    //körs när en aktion i spelet behöver bestämmas, bör returna "s", "h", "d" eller "sp"
    //allowedActions har värdet 3 ifall alla är tillåtna, 2 ifall "h, s or d" är tillåtet och 1 om bara "h or s" är tillåtet
    //handIndex är den hand som just nu spelas, bör användas i till exempel: round.getHands()[handIndex].getTotal()
    @Override
    public String actionMethod(Round round, int allowedActions, int handIndex) {
        return parentPlayMethod.actionMethod(round,allowedActions,handIndex);
    }

    //körs ifall möjlighet för ett insurance bet finns (dvs. ifall dealern har ett ess)
    @Override
    public int insuranceBetMethod(Round round) {
        return 0;
    }

}
