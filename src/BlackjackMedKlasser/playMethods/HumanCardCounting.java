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
        //count += hiLo(card);
        trueCount = (count / ((int) (deck.getSizeOfDeck() / 52)));
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
        return basicArrMetod(round, allowedActions, handIndex);
        // after 100 000 000 rounds
        // basicStrategy() Player edge = 1.6378918982228403%      Final balance: -21985200       Total amount bet: -1342286388
        //  test() Player edge: 2.1153516130094316%               Final balance: -32451300       Total amount bet: -1534085388
         //arrMetod() Player edge: 10.83481402087946%             Final balance: -180066850      Total amount bet: -1661928388
            //(andra test med)arrMetod()  Final balance: 4533550    Player edge: -0.3030819849528216%  Total amount bet: -1495816388
            // test av arrMetod() med 1 000 000 000 round = Player edge: -1.8157642964957765%      Final balance: 37736450      Total amount bet: -2078268092
        //vrf är det så stor variation i player edge ?????????????????
    }

    private double betVariable = trueCount;

    //körs när det ursprungliga bettet ska bestämmas
    @Override
    public int betMethod(Round round) {
        int bet;
        return 10;
       /* if (betVariable < 0) bet = settings.getMinBet();
        else if (betVariable == 1) bet = settings.getMinBet() + (settings.getMaxBet() / 10);
        else if (betVariable == 2) bet = settings.getMinBet() + (settings.getMaxBet() / 5);
        else if (betVariable == 3) bet = settings.getMinBet() + (settings.getMaxBet() / 2);
        else bet = settings.getMaxBet();

        return bet;

        */
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



    private String[][] hardTotals1 = {
            //{"2","3","4","5","6","7","8","9",10","A"}, //Dealers card
            {"h", "h", "h", "h", "h", "h", "h", "h", "h", "h"},          //3
            {"h", "h", "h", "h", "h", "h", "h", "h", "h", "h"},          //4
            {"h", "h", "h", "h", "h", "h", "h", "h", "h", "h"},          //5
            {"h", "h", "h", "h", "h", "h", "h", "h", "h", "h"},          //6
            {"h", "h", "h", "h", "h", "h", "h", "h", "h", "h"},          //7
            {"h", "h", "h", "h", "h", "h", "h", "h", "h", "h"},          //8
            {"h", "h", "h", "h", "h", "h", "h", "h", "h", "h"},          //9
            {"h", "h", "h", "h", "h", "h", "h", "h", "h", "h"},          //10
            {"h", "h", "h", "h", "h", "h", "h", "h", "h", "h"},          //11
            {"h", "h", "s", "s", "s", "h", "h", "h", "h", "h"},          //12
            {"s", "s", "s", "s", "s", "h", "h", "h", "h", "h"},          //13
            {"s", "s", "s", "s", "s", "h", "h", "h", "h", "h"},          //14
            {"s", "s", "s", "s", "s", "h", "h", "h", "h", "h"},          //15
            {"s", "s", "s", "s", "s", "h", "h", "h", "h", "h"},          //16
            {"s", "s", "s", "s", "s", "s", "s", "s", "s", "s"},          //17
            {"s", "s", "s", "s", "s", "s", "s", "s", "s", "s"},          //18
            {"s", "s", "s", "s", "s", "s", "s", "s", "s", "s"},          //19
            {"s", "s", "s", "s", "s", "s", "s", "s", "s", "s"},          //20
    };
    private String[][] hardTotals2 = {
            {"h", "h", "h", "h", "h", "h", "h", "h", "h", "h"},          //3
            {"h", "h", "h", "h", "h", "h", "h", "h", "h", "h"},          //4
            {"h", "h", "h", "h", "h", "h", "h", "h", "h", "h"},          //5
            {"h", "h", "h", "h", "h", "h", "h", "h", "h", "h"},          //6
            {"h", "h", "h", "h", "h", "h", "h", "h", "h", "h"},          //7
            {"h", "h", "h", "h", "h", "h", "h", "h", "h", "h"},          //8
            {"h", "d", "d", "d", "d", "h", "h", "h", "h", "h"},          //9
            {"d", "d", "d", "d", "d", "d", "d", "d", "h", "h"},          //10
            {"d", "d", "d", "d", "d", "d", "d", "d", "d", "d"},          //11
            {"h", "h", "s", "s", "s", "h", "h", "h", "h", "h"},          //12
            {"s", "s", "s", "s", "s", "h", "h", "h", "h", "h"},          //13
            {"s", "s", "s", "s", "s", "h", "h", "h", "h", "h"},          //14
            {"s", "s", "s", "s", "s", "h", "h", "h", "h", "h"},          //15
            {"s", "s", "s", "s", "s", "h", "h", "h", "h", "h"},          //16
            {"s", "s", "s", "s", "s", "s", "s", "s", "s", "s"},          //17
            {"s", "s", "s", "s", "s", "s", "s", "s", "s", "s"},          //18
            {"s", "s", "s", "s", "s", "s", "s", "s", "s", "s"},          //19
            {"s", "s", "s", "s", "s", "s", "s", "s", "s", "s"},          //20
    };
    private String[][] softTotals1 = {
            {"h", "h", "h", "h", "h", "h", "h", "h", "h", "h"},         //12 AA
            {"h", "h", "h", "h", "h", "h", "h", "h", "h", "h"},          //13 A2
            {"h", "h", "h", "h", "h", "h", "h", "h", "h", "h"},          //14 A3
            {"h", "h", "h", "h", "h", "h", "h", "h", "h", "h"},          //15 A4
            {"h", "h", "h", "h", "h", "h", "h", "h", "h", "h"},          //16 A5
            {"h", "h", "h", "h", "h", "h", "h", "h", "h", "h"},          //17 A6
            {"s", "s", "s", "s", "s", "s", "s", "h", "h", "h"},          //18 A7
            {"s", "s", "s", "s", "s", "s", "s", "s", "s", "s"},          //19 A8
            {"s", "s", "s", "s", "s", "s", "s", "s", "s", "s"},          //20 A9
    };
    private String[][] softTotals2 = {
            {"h", "h", "h", "h", "d", "h", "h", "h", "h", "h"},         //12 AA
            {"h", "h", "h", "d", "d", "h", "h", "h", "h", "h"},          //13 A2
            {"h", "h", "h", "d", "d", "h", "h", "h", "h", "h"},          //14 A3
            {"h", "h", "d", "d", "d", "h", "h", "h", "h", "h"},          //15 A4
            {"h", "h", "d", "d", "d", "h", "h", "h", "h", "h"},          //16 A5
            {"h", "d", "d", "d", "d", "h", "h", "h", "h", "h"},          //17 A6
            {"d", "d", "d", "d", "d", "s", "s", "h", "h", "h"},          //18 A7
            {"s", "s", "s", "s", "d", "s", "s", "s", "s", "s"},          //19 A8
            {"s", "s", "s", "s", "s", "s", "s", "s", "s", "s"},          //20 A9
    };

    private String[][] pairs = {
            {"sp", "sp", "sp", "sp", "sp", "sp", "h", "h", "h", "h"},        // 4 2 2
            {"sp", "sp", "sp", "sp", "sp", "sp", "h", "h", "h", "h"},        // 6 3 3
            {"h", "h", "h", "sp", "sp", "h", "h", "h", "h", "h"},            // 8 4 4
            {"d", "d", "d", "d", "d", "d", "d", "d", "h", "h"},              //10 5 5
            {"sp", "sp", "sp", "sp", "sp", "h", "h", "h", "h", "h"},         //12 6 6
            {"sp", "sp", "sp", "sp", "sp", "sp", "h", "h", "h", "h"},        //14 7 7
            {"sp", "sp", "sp", "sp", "sp", "sp", "sp", "sp", "sp", "sp"},    //16 8 8
            {"sp", "sp", "sp", "sp", "sp", "s", "sp", "sp", "s", "s"},       //18 9 9
            {"s", "s", "s", "s", "s", "s", "s", "s", "s", "s"},              //20 10 10
            {"sp", "sp", "sp", "sp", "sp", "sp", "sp", "sp", "sp", "sp"},    //12 AA
    };

    public String basicArrMetod(Round round, int allowdActions, int handIndx) {


        if (allowdActions == 3 && round.getHands()[handIndx].getAvailabelAces() > 0)
            return pairs[9][round.getDealerCard() - 2];
        else if (allowdActions == 3)
            return pairs[(round.getHands()[handIndx].getTotal() - 4) / 2][round.getDealerCard() - 2];
        else if (allowdActions == 2 && round.getHands()[handIndx].getAvailabelAces() > 0)
            return softTotals2[round.getHands()[handIndx].getTotal() - 12][round.getDealerCard() - 2];
        else if (allowdActions == 2 && round.getHands()[handIndx].getAvailabelAces() == 0)
            return hardTotals2[round.getHands()[handIndx].getTotal() - 3][round.getDealerCard() - 2]; // vrf error med -5 istället for -5????
        else if (allowdActions == 1 && round.getHands()[handIndx].getAvailabelAces() == 0)
            return hardTotals1[round.getHands()[handIndx].getTotal() - 3][round.getDealerCard() - 2];
        else if (allowdActions == 1 && round.getHands()[handIndx].getAvailabelAces() > 0)
            return softTotals1[round.getHands()[handIndx].getTotal() - 12][round.getDealerCard() - 2];

        return "error";
    }
}

