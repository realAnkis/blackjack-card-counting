package BlackjackMedKlasser.playMethods;

import BlackjackMedKlasser.Round;

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
}
