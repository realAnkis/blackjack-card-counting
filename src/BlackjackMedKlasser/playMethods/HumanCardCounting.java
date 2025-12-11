package BlackjackMedKlasser.playMethods;

import BlackjackMedKlasser.Card;
import BlackjackMedKlasser.Deck;
import BlackjackMedKlasser.Round;
import BlackjackMedKlasser.Settings;

public class HumanCardCounting extends PlayMethod {
    private Settings settings;
    private Deck deck;
    private double count = 0;
    private double trueCount;




    //körs när ett kort delas ut från kortleken

    public HumanCardCounting(Settings settings, Deck deck) {
        this.settings = settings;
        this.deck = deck;

    }
    @Override
    public void cardDealtMethod(Card card) {
        count += hiLo(card);
        trueCount= (count/((int)(deck.getSizeOfDeck()/52)));
    }

    //körs när kortleken blandas
    @Override
    public void reshuffleMethod() {
        count = 0;
    }

    //körs när en aktion i spelet behöver bestämmas, bör returna "s", "h", "d" eller "sp"
    //allowedActions har värdet 3 ifall alla är tillåtna, 2 ifall "h, s or d" är tillåtet och 1 om bara "h or s" är tillåtet
    //handIndex är den hand som just nu spelas, bör användas i till exempel: round.getHands()[handIndex].getTotal()
    // får bar d på första kortet
    @Override
    public String actionMethod(Round round, int allowedActions, int handIndex) {
        return basicStrategy(round, allowedActions, handIndex);
    }
    private double betVariable=trueCount;
    //körs när det ursprungliga bettet ska bestämmas
    @Override
    public int betMethod(Round round) {
        int bet;
        if (betVariable < 0) bet = settings.getMinBet();
        else if (betVariable == 1) bet = settings.getMinBet() + (settings.getMaxBet() / 10);
        else if (betVariable == 2) bet = settings.getMinBet() + (settings.getMaxBet() / 5);
        else if (betVariable == 3) bet = settings.getMinBet() + (settings.getMaxBet() / 2);
        else bet = settings.getMaxBet();

        return bet;
    }

    //körs ifall möjlighet för ett insurance bet finns (dvs. ifall dealern har ett ess)
    @Override
    public int insuranceBetMethod(Round round) {
        if (betVariable > 3) return settings.getMaxBet();
        else return 0;
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
        } else return "s";
    }
    public String test(Round round, int allowdActions,int handIndx){
        String[][] actionArr1 = {
              //{"2","3","4","5","6","7","8","9","10","11"}
                {"h","h","h","h","h","h","h","h","h","h"}, //2
                {"h","h","h","h","h","h","h","h","h","h"}, //3
                {"h","h","h","h","h","h","h","h","h","h"}, //4
                {"h","h","h","h","h","h","h","h","h","h"}, //5
                {"h","h","h","h","h","h","h","h","h","h"}, //6
                {"h","h","h","h","h","h","h","h","h","h"}, //7
                {"h","h","h","h","h","h","h","h","h","h"}, //8
                {"h","h","h","h","h","h","h","h","h","h"}, //9
                {"h","h","h","h","h","h","h","h","h","h"}, //10
                {"h","h","h","h","h","h","h","h","h","h"}, //11
                {"h","h","s","s","s","h","h","h","h","h"}, //12
                {"s","s","s","s","s","h","h","h","h","h"}, //13
                {"s","s","s","s","s","h","h","h","h","h"}, //14
                {"s","s","s","s","s","h","h","h","h","h"}, //15
                {"s","s","s","s","s","h","h","h","h","h"}, //16
                {"s","s","s","s","s","s","s","s","s","s"}, //17
                {"s","s","s","s","s","s","s","s","s","s"}, //18
                {"s","s","s","s","s","s","s","s","s","s"}, //19
                {"s","s","s","s","s","s","s","s","s","s"}  //20 (platyer total = rad, dealer card = colum)
        };
        String[][] actionArr1Soft = {
              //{"2","3","4","5","6","7","8","9","10","11"}
                {"h","h","h","h","h","h","h","h","h","h"}, //12
                {"h","h","h","h","h","h","h","h","h","h"}, //13
                {"h","h","h","h","h","h","h","h","h","h"}, //14
                {"h","h","h","h","h","h","h","h","h","h"}, //15
                {"h","h","h","h","h","h","h","h","h","h"}, //16
                {"h","h","h","h","h","h","h","h","h","h"}, //17
                {"s","s","s","s","s","s","s","h","h","h"}, //18
                {"s","s","s","s","s","s","s","s","s","s"}, //19
                {"s","s","s","s","s","s","s","s","s","s"} //20 (platyer total = rad, dealer card = colum)
        };

        String[][] actionArr2 = {
                //{"2","3","4","5","6","7","8","9","10","11"}
                {"h","h","h","h","h","h","h","h","h","h"}, //2
                {"h","h","h","h","h","h","h","h","h","h"}, //3
                {"h","h","h","h","h","h","h","h","h","h"}, //4
                {"h","h","h","h","h","h","h","h","h","h"}, //5
                {"h","h","h","h","h","h","h","h","h","h"}, //6
                {"h","h","h","h","h","h","h","h","h","h"}, //7
                {"h","h","h","h","h","h","h","h","h","h"}, //8
                {"h","d","d","d","d","h","h","h","h","h"}, //9
                {"d","d","d","d","d","d","d","d","h","h"}, //10
                {"d","d","d","d","d","d","d","d","d","d"}, //11
                {"h","h","s","s","s","h","h","h","h","h"}, //12
                {"s","s","s","s","s","h","h","h","h","h"}, //13
                {"s","s","s","s","s","h","h","h","h","h"}, //14
                {"s","s","s","s","s","h","h","h","h","h"}, //15
                {"s","s","s","s","s","h","h","h","h","h"}, //16
                {"s","s","s","s","s","s","s","s","s","s"}, //17
                {"s","s","s","s","s","s","s","s","s","s"}, //18
                {"s","s","s","s","s","s","s","s","s","s"}, //19
                {"s","s","s","s","s","s","s","s","s","s"}  //20 (platyer total = rad, dealer card = colum)
        };

        String[][] actionArr2Soft = {
              //{"2","3","4","5","6","7","8","9","10","11"}
                {"h","h","h","h","d","h","h","h","h","h"}, //12
                {"h","h","h","d","d","h","h","h","h","h"}, //13
                {"h","h","h","d","d","h","h","h","h","h"}, //14
                {"h","h","d","d","d","h","h","h","h","h"}, //15
                {"h","h","d","d","d","h","h","h","h","h"}, //16
                {"h","d","d","d","d","h","h","h","h","h"}, //17
                {"d","d","d","d","d","s","s","h","h","h"}, //18
                {"s","s","s","s","d","s","s","s","s","s"}, //19
                {"s","s","s","s","s","s","s","s","s","s"} //20 (platyer total = rad, dealer card = colum)
        };
        String[][] actionArrPair = {
                //{"2","3","4","5","6","7","8","9","10","11"}
                {"sp","sp","sp","sp","sp","sp","sp","sp","sp","sp"}, //2(AA) (platyer total = rad, dealer card = colum)
                {"sp","sp","sp","sp","sp","sp","h","h","h","h"}, //4
                {"sp","sp","sp","sp","sp","sp","h","h","h","h"}, //6
                {"h","h","h","sp","sp","h","h","h","h","h"}, //8
                {"d","d","h","d","d","d","d","d","h","h"}, //10
                {"sp","sp","sp","sp","sp","h","h","h","h","h"}, //12
                {"sp","sp","sp","sp","sp","sp","h","h","h","h"}, //14
                {"sp","sp","sp","sp","sp","sp","sp","sp","sp","sp"}, //16
                {"sp","sp","sp","sp","sp","s","sp","sp","s","s"}, //18
                {"s","s","s","s","s","s","s","s","s","s"} //20
        };
        if (allowdActions==3) return actionArrPair[(round.getHands()[handIndx].getTotal()-2)/2][round.getDealerCard()-2];
        else if (allowdActions==2&&round.getHands()[handIndx].getAvailabelAces()==0) return actionArr2[round.getHands()[handIndx].getTotal()-2][round.getDealerCard()-2];
        else if (allowdActions==2&&round.getHands()[handIndx].getAvailabelAces() >0) return actionArr2Soft[round.getHands()[handIndx].getTotal()-12][round.getDealerCard()-2];
        else if (allowdActions==1&&round.getHands()[handIndx].getAvailabelAces() ==0) return actionArr1[round.getHands()[handIndx].getTotal()-2][round.getDealerCard()-2];
        else return actionArr1Soft[round.getHands()[handIndx].getTotal()-12][round.getDealerCard()-2];
    }
}

