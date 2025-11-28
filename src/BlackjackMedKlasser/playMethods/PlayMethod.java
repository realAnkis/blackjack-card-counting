package BlackjackMedKlasser.playMethods;

import BlackjackMedKlasser.Round;
import GammaltGYA.SpelaSjälv;

import java.util.function.Function;

public class PlayMethod {
    Function<BlackjackMedKlasser.Round, String> actionMethod;
    Function<BlackjackMedKlasser.Round, String> betMethod;
    Function<BlackjackMedKlasser.Round, String> insuranceBetMethod;
    Function<BlackjackMedKlasser.Round, String> cardDealtMethod;

    public PlayMethod(Function<Round, String> actionMethod, Function<Round, String> betMethod, Function<Round, String> insuranceBetMethod, Function<Round, String> cardDealtMethod) {
        this.actionMethod = actionMethod;
        this.betMethod = betMethod;
        this.insuranceBetMethod = insuranceBetMethod;
        this.cardDealtMethod = cardDealtMethod;
    }

    public String actionMethod(BlackjackMedKlasser.Round round) {
        return actionMethod.apply(round);
    }
    public String betMethod(BlackjackMedKlasser.Round round) {
        return betMethod.apply(round);
    }
    public String insuranceBetMethod(BlackjackMedKlasser.Round round) {
        return insuranceBetMethod.apply(round);
    }
    public String cardDealtMethod(BlackjackMedKlasser.Round round) {
        return cardDealtMethod.apply(round);
    }
}
