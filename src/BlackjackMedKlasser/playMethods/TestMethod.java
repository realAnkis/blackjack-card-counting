package BlackjackMedKlasser.playMethods;

import BlackjackMedKlasser.Card;
import BlackjackMedKlasser.Round;

import java.util.Scanner;

public class TestMethod extends PlayMethod {


    //körs när ett kort delas ut från kortleken
    @Override
    public void cardDealtMethod(Card card) {
    }

    //körs när en aktion i spelet behöver bestämmas, bör returna "s", "h", "d" eller "sp"
    //allowedActions har värdet 3 ifall alla är tillåtna, 2 ifall "h, s or d" är tillåtet och 1 om bara "h or s" är tillåtet
    //handIndex är den hand som just nu spelas, bör användas i till exempel: round.getHands()[handIndex].getTotal()
    @Override
    public String actionMethod(Round round, int allowedActions, int handIndex) {
        if(round.getHands()[handIndex].getTotal() <= 10) return "h";
        return "s";
    }

    //körs när det ursprungliga bettet ska bestämmas
    @Override
    public int betMethod(Round round) {
        return 10;
    }

    //körs ifall möjlighet för ett insurance bet finns (dvs. ifall dealern har ett ess)
    @Override
    public int insuranceBetMethod(Round round) {
        return 0;
    }
}
