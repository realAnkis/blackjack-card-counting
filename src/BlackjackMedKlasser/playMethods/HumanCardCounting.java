package BlackjackMedKlasser.playMethods;

import BlackjackMedKlasser.Card;
import BlackjackMedKlasser.Round;

public class HumanCardCounting extends PlayMethod {

    public double count = 0;

    //körs när ett kort delas ut från kortleken
    @Override
    public void cardDealtMethod(Card card) {
        count += hiLo(card);

    }

    //körs när en aktion i spelet behöver bestämmas, bör returna "s", "h", "d" eller "sp"
    //allowedActions har värdet 3 ifall alla är tillåtna, 2 ifall "h, s or d" är tillåtet och 1 om bara "h or s" är tillåtet
    //handIndex är den hand som just nu spelas, bör användas i till exempel: round.getHands()[handIndex].getTotal()
    // får bar d på första kortet
    @Override
    public String actionMethod(Round round, int allowedActions, int handIndex) {

        return basicStrategy(round,allowedActions,handIndex);


    }

    //körs när det ursprungliga bettet ska bestämmas
    @Override
    public int betMethod(Round round) {
        int maxBet = 100;
        int minBet = 10;
        int bet;
        if (count < 0) bet = minBet;
        else if (count == 1) bet = minBet + (maxBet / 10);
        else if (count == 2) bet = minBet + (maxBet / 5);
        else bet = maxBet;

        return bet;
    }

    //körs ifall möjlighet för ett insurance bet finns (dvs. ifall dealern har ett ess)
    @Override
    public int insuranceBetMethod(Round round) {
        return 0;
    }


    public int hiLo(Card card) {
        if (card.getValue() > 9) return -1;
        else if (card.getValue() < 7) return 1;
        else return 0;
    }


    public int hiOpt1(Card card) {
        if (card.getValue() == 10) return -1;
        else if (card.getValue() < 7 && card.getValue() != 2) return 1;
        else return 0;
    }


    public int hiOpt2(Card card) {
        if (card.getValue() == 10) return -2;
        else if (card.getValue() < 8) {
            if (card.getValue() == 5 || card.getValue() == 4) return 2;
            else
                return 1;
        } else return 0;
    }

    public int kO(Card card) {
        if (card.getValue() > 9) return -1;
        else if (card.getValue() < 8) return 1;
        else return 0;
    }

    public int omega2(Card card) {
        if (card.getValue() == 10) return -2;
        else if (card.getValue() > 7) {
            if (card.getValue() == 9) return -1;
            else return 0;
        } else if (card.getValue() == 4 || card.getValue() == 5 || card.getValue() == 6) return +2;
        else return 1;
    }

    public int red7(Card card) {
        if (card.getValue() > 9) return -1;
        else if (card.getValue() < 7) return 1;
        else if (card.getValue() == 7 && card.getSuit() < 2) return 1;
            // givet att Suit 0 och 1 är hjäter och ruter
        else return 0;
    }

    public double halvs(Card card) {
        if (card.getValue() > 9) return -1;
        else if (card.getValue() == 3 || card.getValue() == 4 || card.getValue() == 6) return +1;
        else if (card.getValue() == 9) return -0.5;
        else if (card.getValue() == 8) return 0;
        else if (card.getValue() == 5) return 1.5;
        else return 0.5;
    }

    public int zenCount(Card card) {
        if (card.getValue() == 10) return -2;
        else if (card.getValue() < 8) {
            if (card.getValue() < 4 || card.getValue() == 7) return 1;
            else return 2;
        } else if (card.getValue() < 10) return 0;
        else return -1;
    }

    public int tenCount(Card card) {
        if (card.getValue() == 10) return -2;
        else return 1;
    }

    //allowedActions har värdet 3 ifall alla är tillåtna, 2 ifall "h, s or d" är tillåtet och 1 om bara "h or s" är tillåtet
    public String basicStrategy(Round round, int allowedActions, int handIndx) {

        if (allowedActions == 1) {
            if (round.getHands()[handIndx].getAvailabelAces() < 1) {
                if (round.getHands()[handIndx].getTotal() >= 17) return "s";
                else if (round.getHands()[handIndx].getTotal() > 12) {
                    if (round.getDealerCard() > 6) return "h";
                    else return "s";
                } else if (round.getHands()[handIndx].getTotal() == 12) {
                    if (round.getDealerCard() > 3 && round.getDealerCard() < 7) return "s";
                    else return "h";
                } else return "h";
            } else {
                if (round.getHands()[handIndx].getTotal() > 18) return "s";
                else if (round.getHands()[handIndx].getTotal() == 18 || round.getDealerCard() < 9) return "s";
                else return "h";
            }
        } else if (allowedActions == 2) {
            if (round.getHands()[handIndx].getAvailabelAces() < 1) {
                if (round.getHands()[handIndx].getTotal() >= 17) return "s";
                else if (round.getHands()[handIndx].getTotal() > 12) {
                    if (round.getDealerCard() > 6) return "h";
                    else return "s";
                } else if (round.getHands()[handIndx].getTotal() == 12) {
                    if (round.getDealerCard() > 3 && round.getDealerCard() < 7) return "s";
                    else return "h";
                } else if (round.getHands()[handIndx].getTotal() == 11) return "d";
                else if (round.getHands()[handIndx].getTotal() == 10) {
                    if (round.getDealerCard() > 10) return "h";
                    else return "d";
                } else if (round.getHands()[handIndx].getTotal() == 9) {
                    if (round.getDealerCard() > 2 && round.getDealerCard() < 7) return "d";
                    else return "h";
                } else return "h";
            } else {
                if (round.getHands()[handIndx].getTotal() == 20) return "s";
                else if (round.getHands()[handIndx].getTotal() == 19) {
                    if (round.getDealerCard() == 6) return "d";
                    else return "s";
                } else if (round.getHands()[handIndx].getTotal() == 18) {
                    if (round.getDealerCard() < 7) return "d";
                    else if (round.getDealerCard() > 9) return "h";
                    else return "s";
                } else if (round.getHands()[handIndx].getTotal() == 17) {
                    if (round.getDealerCard() > 6 || round.getDealerCard() == 2) return "h";
                    else return "d";
                } else if (round.getHands()[handIndx].getTotal() > 14) {
                    if (round.getDealerCard() > 6 || round.getDealerCard() < 4) return "h";
                    else return "d";
                } else if (round.getHands()[handIndx].getTotal() > 12) {
                    if (round.getDealerCard() > 6 || round.getDealerCard() < 5) return "h";
                    else return "d";
                } else if (round.getDealerCard() == 6) return "d";
                else return "h";
            }
        } else if (allowedActions == 3) {
            // alla är tillåtna (inklusive split (sp)) -> betyder att man har ett par
            if (round.getHands()[handIndx].getTotal() == 12) return "sp";
            else if (round.getHands()[handIndx].getTotal() == 20) return "s";
            else if (round.getHands()[handIndx].getTotal() == 18) {
                if (round.getDealerCard() > 9 || round.getDealerCard() == 7) return "s";
                else return "sp";
            } else if (round.getHands()[handIndx].getTotal() == 16) return "sp";
            else if (round.getHands()[handIndx].getTotal() == 14) {
                if (round.getDealerCard() > 7) return "h";
                else return "sp";
            } else if (round.getHands()[handIndx].getTotal() == 12) {
                if (round.getDealerCard() > 6) return "h";
                else return "sp";
            } else if (round.getHands()[handIndx].getTotal() == 10) {
                if (round.getDealerCard() > 9) return "h";
                else return "d";
            } else if (round.getHands()[handIndx].getTotal() == 8) {
                if (round.getDealerCard() == 5 || round.getDealerCard() == 6) return "sp";
                else return "h";
            } else if (round.getDealerCard() > 7) return "h";
            else return "sp";
        }
        else return "s";
    }
}

