package BlackjackMedKlasser.playMethods.HumanCardCountingPack;

import BlackjackMedKlasser.Card;
import BlackjackMedKlasser.Deck;
import BlackjackMedKlasser.Round;
import BlackjackMedKlasser.Settings;
import BlackjackMedKlasser.playMethods.PlayMethod;

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
        int deckSize = deck.getSizeOfDeck();
        if (deckSize < 1) deckSize = 1;
        trueCount = (count / ((double) deckSize / 52));

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
    /*===========================================================================================*/
    @Override
    public String actionMethod(Round round, int allowedActions, int handIndex) {
        return hiLoProBj(round, allowedActions, handIndex);
    }
    /*===========================================================================================*/

    private final double betVariable = trueCount;

    //körs när det ursprungliga bettet ska bestämmas
    @Override
    public int betMethod(Round round) {
        int bet;
        if (betVariable <= 1) bet = settings.getMinBet();
        else if (betVariable <= 2) bet = (settings.getMaxBet() / 10 * 2);
        else if (betVariable <= 3) bet = (settings.getMaxBet() / 10 * 4);
        else if (betVariable <= 4) bet = (settings.getMaxBet() / 10 * 6);
        else if (betVariable <= 5) bet = (settings.getMaxBet() / 10 * 8);
        else bet = settings.getMaxBet();
        return bet;
    }

    //körs ifall möjlighet för ett insurance bet finns (dvs. ifall dealern har ett ess)
    @Override
    public int insuranceBetMethod(Round round) {
        if (betVariable > 3) return settings.getMaxBet();
        else return 0;
    }

    //counting methods
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
            return hardTotals2[round.getHands()[handIndx].getTotal() - 3][round.getDealerCard() - 2];
        else if (allowdActions == 1 && round.getHands()[handIndx].getAvailabelAces() == 0)
            return hardTotals1[round.getHands()[handIndx].getTotal() - 3][round.getDealerCard() - 2];
        else if (allowdActions == 1 && round.getHands()[handIndx].getAvailabelAces() > 0)
            return softTotals1[round.getHands()[handIndx].getTotal() - 12][round.getDealerCard() - 2];

        return "error";
    }

    /*  ---------------- Index Tabel  ----------------------------*
    // from https://www.casinocenter.com/master-class-the-hi-lo-card-counting-system/
    private static String[][] hiLoIndexTabel = {
            //    {player toal, dealer card, index, action, Soft?(t,f),pair?(t,f)}
            {"16", "10", "1", "s", "f", "f"},   // 0
            {"12", "3", "3", "s", "f", "f"},   // 1
            {"15", "4", "4", "s", "f", "f"},   // 2
            {"20", "5", "5", "sp", "f", "t"},  // 3
            {"20", "6", "5", "sp", "f", "t"},  // 4
            {"12", "4", "1", "s", "f", "f"},   // 5
            {"12", "2", "5", "s", "f", "f"},   // 6
            {"8", "6", "2", "d", "f", "f"},   // 7
            {"13", "2", "0", "s", "f", "f"},   //8
            {"9", "3", "3", "d", "f", "f"},    // 9
            {"10", "11", "3", "d", "f", "f"},  // 10
            {"11", "11", "0", "d", "f", "f"},  // 11
            {"8", "5", "4", "d", "f", "f"},    // 12
            {"19", "6", "1", "d", "t", "f"},    // 13
            {"12", "6", "0", "s", "f", "f"},   // 14
            {"19", "5", "1", "d", "t", "f"},  // 15
            {"12", "5", "-1", "s", "f", "f"},  // 16
            {"16", "9", "5", "s", "f", "f"},   // 17
            {"20", "4", "7", "sp", "f", "t"},   // 18
            {"13", "3", "-1", "s", "f", "f"},  // 19
            {"9", "2", "1", "d", "f", "f"},    // 20
            {"10", "10", "7", "d", "f", "f"},   // 21
            {"14", "4", "1", "d", "t", "f"},   // 22
            {"8", "6", "2", "d", "f", "t"},   // 23
            {"13", "4", "-3", "s", "f", "f"},  // 24
            {"19", "4", "3", "d", "t", "f"},    // 25
            {"14", "2", "-3", "s", "f", "f"},  // 26
            {"18", "2", "0", "d", "t", "f"},    // 27
            {"10", "9", "-2", "d", "f", "f"},  // 28
            {"13", "4", "3", "d", "t", "f"},   // 29
            {"9", "3", "-1", "d", "f", "f"},   // 30
            {"11", "10", "-5", "d", "f", "f"},  // 31
            {"8", "5", "4", "d", "f", "t"},    // 32
            {"20", "6", "5", "d", "t", "f"},   // 33
            {"20", "5", "5", "d", "t", "f"},    // 34
            {"8", "4", "6", "d", "f", "f"},    // 35
            {"15", "9", "8", "s", "f", "f"},   // 36
            {"16", "11", "8", "s", "f", "f"},   // 37
            {"20", "3", "9", "sp", "f", "t"},  // 38
            {"13", "5", "-4", "s", "f", "f"},  // 39
            {"14", "3", "-5", "s", "f", "f"},   // 40
            {"19", "3", "5", "d", "t", "f"},  // 41
            {"13", "5", "-1", "d", "t", "f"},  // 42
            {"13", "6", "-4", "s", "f", "f"},   // 43
            {"16", "8", "9", "s", "f", "f"},   // 44
            {"12", "2", "1", "sp", "f", "t"},   // 45
            {"9", "4", "-3", "d", "f", "f"},  // 46
            {"15", "2", "-6", "s", "f", "f"},  // 47
            {"18", "11", "-1", "s", "t", "f"},  // 48
    };
    */

    private final String[][] sortedHiLoIndexTabel = {
            // sorted first after index so largest indexs is first, then moved up all that can split
            //{player toal [0], dealer card[1], index[2], action[3], Soft?(t,f)[4], pair?(t,f)[5]}
            {"20", "3", "9", "sp", "f", "t"},   //0
            {"20", "4", "7", "sp", "f", "t"},   //1
            {"20", "6", "5", "sp", "f", "t"},   //2
            {"20", "5", "5", "sp", "f", "t"},   //3
            {"8", "5", "4", "d", "f", "t"},   //4
            {"8", "6", "2", "d", "f", "t"},   //5
            {"12", "2", "1", "sp", "f", "t"},   //6 ------------------- last pair=true --------------------------
            {"16", "8", "9", "s", "f", "f"},   //7
            {"16", "11", "8", "s", "f", "f"},   //8
            {"15", "9", "8", "s", "f", "f"},   //9
            {"10", "10", "7", "d", "f", "f"},   //10
            {"8", "4", "6", "d", "f", "f"},   //11
            {"19", "3", "5", "d", "t", "f"},   //12
            {"20", "5", "5", "d", "t", "f"},   //13
            {"20", "6", "5", "d", "t", "f"},   //14
            {"16", "9", "5", "s", "f", "f"},   //15
            {"12", "2", "5", "s", "f", "f"},   //16
            {"8", "5", "4", "d", "f", "f"},   //17
            {"15", "4", "4", "s", "f", "f"},   //18
            {"13", "4", "3", "d", "t", "f"},   //19
            {"19", "4", "3", "d", "t", "f"},   //20
            {"10", "11", "3", "d", "f", "f"},   //21
            {"9", "3", "3", "d", "f", "f"},   //22
            {"12", "3", "3", "s", "f", "f"},   //23
            {"8", "6", "2", "d", "f", "f"},   //24
            {"14", "4", "1", "d", "t", "f"},   //25
            {"9", "2", "1", "d", "f", "f"},   //26
            {"19", "5", "1", "d", "t", "f"},   //27
            {"19", "6", "1", "d", "t", "f"},   //28
            {"12", "4", "1", "s", "f", "f"},   //29
            {"16", "10", "1", "s", "f", "f"},   //30
            {"18", "2", "0", "d", "t", "f"},   //31
            {"12", "6", "0", "s", "f", "f"},   //32
            {"11", "11", "0", "d", "f", "f"},   //33
            {"13", "2", "0", "s", "f", "f"},   //34
            {"18", "11", "-1", "s", "t", "f"},   //35
            {"13", "5", "-1", "d", "t", "f"},   //36
            {"9", "3", "-1", "d", "f", "f"},   //37
            {"13", "3", "-1", "s", "f", "f"},   //38
            {"12", "5", "-1", "s", "f", "f"},   //39
            {"10", "9", "-2", "d", "f", "f"},   //40
            {"9", "4", "-3", "d", "f", "f"},   //41
            {"14", "2", "-3", "s", "f", "f"},   //42
            {"13", "4", "-3", "s", "f", "f"},   //43
            {"13", "6", "-4", "s", "f", "f"},   //44
            {"13", "5", "-4", "s", "f", "f"},   //45
            {"14", "3", "-5", "s", "f", "f"},   //46
            {"11", "10", "-5", "d", "f", "f"},   //47
            {"15", "2", "-6", "s", "f", "f"},   //48
    };

    public String hiLoCountStrat(Round round, int allowdActions, int handIndx) {
        if (allowdActions == 3) {
            for (int i = 0; i < 7; i++) {
                if ((int) trueCount == Integer.parseInt(sortedHiLoIndexTabel[i][2]) && round.getHands()[handIndx].getTotal() == Integer.parseInt(sortedHiLoIndexTabel[i][0]) && round.getDealerCard() == Integer.parseInt(sortedHiLoIndexTabel[i][1]))
                    return sortedHiLoIndexTabel[i][3];
            }
            return basicArrMetod(round, allowdActions, handIndx);
        } else if (allowdActions == 2 && round.getHands()[handIndx].getAvailabelAces() > 0) {
            for (int i = 7; i < 49; i++) {
                if ((int) trueCount == Integer.parseInt(sortedHiLoIndexTabel[i][2]) && round.getHands()[handIndx].getTotal() == Integer.parseInt(sortedHiLoIndexTabel[i][0])
                        && round.getDealerCard() == Integer.parseInt(sortedHiLoIndexTabel[i][1]) && sortedHiLoIndexTabel[i][4].equals("t"))
                    return sortedHiLoIndexTabel[i][3];
            }
            return basicArrMetod(round, allowdActions, handIndx);
        } else if (allowdActions == 2) {
            for (int i = 7; i < 49; i++) {
                if ((int) trueCount == Integer.parseInt(sortedHiLoIndexTabel[i][2]) && round.getHands()[handIndx].getTotal() == Integer.parseInt(sortedHiLoIndexTabel[i][0])
                        && round.getDealerCard() == Integer.parseInt(sortedHiLoIndexTabel[i][1]) && sortedHiLoIndexTabel[i][4].equals("f"))
                    return sortedHiLoIndexTabel[i][3];
            }
            return basicArrMetod(round, allowdActions, handIndx);
        } else if (allowdActions == 1 && round.getHands()[handIndx].getAvailabelAces() > 0) {
            for (int i = 7; i < 49; i++) {
                if ((int) trueCount == Integer.parseInt(sortedHiLoIndexTabel[i][2]) && round.getHands()[handIndx].getTotal() == Integer.parseInt(sortedHiLoIndexTabel[i][0])
                        && round.getDealerCard() == Integer.parseInt(sortedHiLoIndexTabel[i][1]) && sortedHiLoIndexTabel[i][4].equals("t") && !sortedHiLoIndexTabel[i][3].equals("d"))
                    return sortedHiLoIndexTabel[i][3];
            }
            return basicArrMetod(round, allowdActions, handIndx);
        } else if (allowdActions == 1) {
            for (int i = 7; i < 49; i++) {
                if ((int) trueCount == Integer.parseInt(sortedHiLoIndexTabel[i][2]) && round.getHands()[handIndx].getTotal() == Integer.parseInt(sortedHiLoIndexTabel[i][0])
                        && round.getDealerCard() == Integer.parseInt(sortedHiLoIndexTabel[i][1]) && sortedHiLoIndexTabel[i][4].equals("f") && !sortedHiLoIndexTabel[i][3].equals("d"))
                    return sortedHiLoIndexTabel[i][3];
            }
            return basicArrMetod(round, allowdActions, handIndx);
        }

        return "sp";

    }


    private final String[][] halvsTabel = {
            //{player toal [0], dealer card[1], index[2], action[3], Soft?(t,f)[4], pair?(t,f)[5]}
            // =====================
            // HARD TOTALS (Hit / Stand)
            // =====================
            {"16", "10", "0", "s", "f", "f"}, //0
            {"16", "9", "5", "s", "f", "f"}, //1
            {"16", "11", "3", "s", "f", "f"}, //2
            {"15", "10", "4", "s", "f", "f"}, //3
            {"15", "9", "5", "s", "f", "f"}, //4
            {"15", "11", "5", "s", "f", "f"}, //5
            {"14", "10", "3", "s", "f", "f"}, //6
            {"14", "9", "6", "s", "f", "f"}, //7
            {"14", "2", "1", "s", "f", "f"}, //8
            {"13", "2", "-1", "s", "f", "f"}, //9
            {"13", "3", "-2", "s", "f", "f"}, //10
            {"13", "4", "-1", "s", "f", "f"}, //11
            {"12", "2", "3", "s", "f", "f"}, //12
            {"12", "3", "2", "s", "f", "f"}, //13
            {"12", "4", "0", "s", "f", "f"}, //14
            {"12", "5", "-2", "s", "f", "f"}, //15
            {"12", "6", "-1", "s", "f", "f"}, //16
            {"12", "11", "1", "s", "f", "f"}, //17
            // =====================
            // HARD TOTALS (Double)
            // =====================
            {"11", "11", "1", "d", "f", "f"}, //18
            {"11", "10", "0", "d", "f", "f"}, //19
            {"10", "10", "4", "d", "f", "f"}, //20
            {"10", "11", "4", "d", "f", "f"}, //21
            {"10", "9", "1", "d", "f", "f"}, //22
            {"9", "2", "1", "d", "f", "f"}, //23
            {"9", "7", "4", "d", "f", "f"}, //24
            {"8", "6", "2", "d", "f", "f"}, //25
            // =====================
            // SOFT HANDS (Double / Stand)
            // =====================
            {"18", "2", "1", "d", "t", "f"}, //26
            {"18", "3", "0", "d", "t", "f"}, //27
            {"18", "4", "-1", "d", "t", "f"}, //28
            {"18", "5", "-2", "d", "t", "f"}, //29
            {"18", "6", "-3", "d", "t", "f"}, //30
            {"18", "9", "5", "s", "t", "f"}, //31
            {"18", "10", "4", "s", "t", "f"}, //32
            {"18", "11", "2", "s", "t", "f"}, //33
            {"17", "2", "1", "d", "t", "f"}, //34
            {"17", "3", "-1", "d", "t", "f"}, //35
            {"17", "4", "-2", "d", "t", "f"}, //36
            {"16", "3", "2", "d", "t", "f"}, //37
            {"16", "4", "0", "d", "t", "f"}, //38
            {"15", "4", "3", "d", "t", "f"}, //39
            {"15", "5", "1", "d", "t", "f"}, //40
            {"14", "4", "4", "d", "t", "f"}, //41
            {"14", "5", "2", "d", "t", "f"}, //42
            {"13", "5", "3", "d", "t", "f"}, //43
            // =====================
            // PAIRS (Splits)
            // =====================
            {"20", "5", "5", "sp", "f", "t"}, //44
            {"20", "6", "4", "sp", "f", "t"}, //45
            {"20", "4", "6", "sp", "f", "t"}, //46
            {"18", "11", "3", "sp", "f", "t"}, //47
            {"18", "7", "4", "sp", "f", "t"}, //48
            {"16", "10", "4", "sp", "f", "t"}, //49
            {"16", "11", "5", "sp", "f", "t"}, //50
            {"14", "10", "6", "sp", "f", "t"}, //51
            {"12", "2", "3", "sp", "f", "t"}, //52
            {"8", "4", "2", "sp", "f", "t"}, //53
            {"8", "5", "1", "sp", "f", "t"}  //54

    };

    public String halvsCountStrat(Round round, int allowdActions, int handIndx) {
        int playerTotal = round.getHands()[handIndx].getTotal();
        int dealersCard = round.getDealerCard();
        int tC = (int) Math.floor(trueCount);

        if (allowdActions == 3) {
            for (int i = 44; i < 55; i++) { /* Pairs */
                if (playerTotal == Integer.parseInt(halvsTabel[i][0]) && dealersCard == Integer.parseInt(halvsTabel[i][1]) && tC >= Integer.parseInt(halvsTabel[i][2]))
                    return halvsTabel[i][3];
            }
            for (int i = 18; i < 26; i++) { /* Hard doubles or Stand */
                if (playerTotal == Integer.parseInt(halvsTabel[i][0]) && dealersCard == Integer.parseInt(halvsTabel[i][1]) && tC >= Integer.parseInt(halvsTabel[i][2])
                        && halvsTabel[i][4].equals("f")) return halvsTabel[i][3];
            }
            for (int i = 26; i < 44; i++) { /* Soft doubles or Stand */
                if (playerTotal == Integer.parseInt(halvsTabel[i][0]) && dealersCard == Integer.parseInt(halvsTabel[i][1]) && tC >= Integer.parseInt(halvsTabel[i][2])
                        && halvsTabel[i][4].equals("t")) return halvsTabel[i][3];
            }
            for (int i = 0; i < 18; i++) { /*Hit or Stand */
                if (playerTotal == Integer.parseInt(halvsTabel[i][0]) && dealersCard == Integer.parseInt(halvsTabel[i][1]) && tC >= Integer.parseInt(halvsTabel[i][2]))
                    return halvsTabel[i][3];
            }
            return basicArrMetod(round, allowdActions, handIndx);
        } else if (allowdActions == 2 && round.getHands()[handIndx].getAvailabelAces() > 0) {/*===========================================================================================*/
            for (int i = 26; i < 44; i++) { /* Soft doubles or Stand */
                if (playerTotal == Integer.parseInt(halvsTabel[i][0]) && dealersCard == Integer.parseInt(halvsTabel[i][1]) && tC >= Integer.parseInt(halvsTabel[i][2])
                        && halvsTabel[i][4].equals("t")) return halvsTabel[i][3];
            }
            for (int i = 0; i < 18; i++) { /*Hit or Stand */
                if (playerTotal == Integer.parseInt(halvsTabel[i][0]) && dealersCard == Integer.parseInt(halvsTabel[i][1]) && tC >= Integer.parseInt(halvsTabel[i][2]))
                    return halvsTabel[i][3];
            }
            return basicArrMetod(round, allowdActions, handIndx);
        } else if (allowdActions == 2) { /*=====================================================================================================================================*/
            for (int i = 18; i < 26; i++) { /* Hard doubles or Stand */
                if (playerTotal == Integer.parseInt(halvsTabel[i][0]) && dealersCard == Integer.parseInt(halvsTabel[i][1]) && tC >= Integer.parseInt(halvsTabel[i][2])
                        && halvsTabel[i][4].equals("f")) return halvsTabel[i][3];
            }
            for (int i = 0; i < 18; i++) { /*Hit or Stand */
                if (playerTotal == Integer.parseInt(halvsTabel[i][0]) && dealersCard == Integer.parseInt(halvsTabel[i][1]) && tC >= Integer.parseInt(halvsTabel[i][2]))
                    return halvsTabel[i][3];
            }
            return basicArrMetod(round, allowdActions, handIndx);
        } else if (allowdActions == 1 && round.getHands()[handIndx].getAvailabelAces() > 0) { /*===========================================================================================*/
            for (int i = 26; i < 44; i++) { /* Soft doubles or Stand */
                if (playerTotal == Integer.parseInt(halvsTabel[i][0]) && dealersCard == Integer.parseInt(halvsTabel[i][1]) && tC >= Integer.parseInt(halvsTabel[i][2])
                        && halvsTabel[i][4].equals("t") && !halvsTabel[i][3].equals("d")) return halvsTabel[i][3];
            }
            for (int i = 0; i < 18; i++) { /*Hit or Stand */
                if (playerTotal == Integer.parseInt(halvsTabel[i][0]) && dealersCard == Integer.parseInt(halvsTabel[i][1]) && tC >= Integer.parseInt(halvsTabel[i][2]))
                    return halvsTabel[i][3];
            }
            return basicArrMetod(round, allowdActions, handIndx);
        } else if (allowdActions == 1) {/*====================================================================================================================================================*/
            for (int i = 0; i < 18; i++) { /*Hit or Stand */
                if (playerTotal == Integer.parseInt(halvsTabel[i][0]) && dealersCard == Integer.parseInt(halvsTabel[i][1]) && tC >= Integer.parseInt(halvsTabel[i][2]))
                    return halvsTabel[i][3];
            }
            return basicArrMetod(round, allowdActions, handIndx);
        }
        return "sp";
    }

    private final String[][] proBJ = {
            //{player toal [0], dealer card[1], index[2], action[3], Soft?(t,f)[4], pair?(t,f)[5]}
            // follows tabel 23 in Professional blackjack by Wong, Stanford
            {"20", "4", "6", "sp", "f", "t"}, // 0
            {"20", "5", "5", "sp", "f", "t"}, // 1
            {"20", "6", "4", "sp", "f", "t"}, // 2
            {"18", "7", "3", "sp", "f", "t"}, // 3
            {"18", "11", "3", "sp", "f", "t"},// 4
            {"14", "8", "5", "sp", "f", "t"}, // 5
            {"8", "3", "6", "sp", "f", "t"}, // 6
            {"8", "4", "1", "sp", "f", "t"}, // 7
            {"6", "2", "0", "sp", "f", "t"}, // 8
            {"6", "8", "4", "sp", "f", "t"}, // 9
            {"4", "8", "5", "sp", "f", "t"}, // 10  last pair

            {"11", "11", "1", "d", "f", "f"}, // 11
            {"10", "10", "4", "d", "f", "f"}, // 12
            {"10", "11", "4", "d", "f", "f"}, // 13
            {"9", "2", "1", "d", "f", "f"}, // 14
            {"9", "3", "-1", "d", "f", "f"}, // 15
            {"9", "7", "3", "d", "f", "f"}, // 16
            {"8", "4", "5", "d", "f", "f"}, // 17
            {"8", "5", "3", "d", "f", "f"}, // 18
            {"8", "6", "1", "d", "f", "f"}, // 19
            {"20", "4", "6", "d", "t", "f"}, // 20  --soft doubleing start
            {"20", "5", "5", "d", "t", "f"}, // 21
            {"20", "6", "4", "d", "t", "f"}, // 22
            {"19", "3", "5", "d", "t", "f"}, // 23
            {"19", "4", "3", "d", "t", "f"}, // 24
            {"19", "5", "1", "d", "t", "f"}, // 25
            {"19", "6", "1", "d", "t", "f"}, // 26
            {"18", "2", "0", "d", "t", "f"}, // 27
            {"17", "2", "1", "d", "t", "f"}, // 28
            {"16", "3", "4", "d", "t", "f"}, // 29
            {"15", "4", "-1", "d", "t", "f"}, // 30
            {"14", "4", "1", "d", "t", "f"}, // 31
            {"13", "4", "3", "d", "t", "f"}, // 32
            {"13", "5", "0", "d", "t", "f"}, // 33 --- soft doubleing end

            {"18", "11", "1", "s", "t", "f"}, // 34 ---- soft standing (soft doubeling takesw precednece)

            {"16", "9", "5", "s", "f", "f"}, // 35 --- hard standing
            {"16", "10", "0", "s", "f", "f"}, // 36
            {"15", "10", "4", "s", "f", "f"}, // 37
            {"13", "2", "-1", "s", "f", "f"}, // 38
            {"12", "2", "3", "s", "f", "f"}, // 39
            {"12", "3", "2", "s", "f", "f"}, // 40
            {"12", "4", "0", "s", "f", "f"}, // 41
            {"12", "6", "-1", "s", "f", "f"}, // 42
    };

    public String hiLoProBj(Round round, int allowdActions, int handIndx) {
        int playerTotal = round.getHands()[handIndx].getTotal();
        int playerAces = round.getHands()[handIndx].getAvailabelAces();
        int dealersCard = round.getDealerCard();
        int tC = (int) Math.floor(trueCount);
        if (allowdActions == 3) {
            for (int i = 0; i < 11; i++) {
                if (playerTotal == Integer.parseInt(proBJ[i][0]) && dealersCard == Integer.parseInt(proBJ[i][1]) && tC >= Integer.parseInt(proBJ[i][2])) {
                    if (tC > Integer.parseInt(proBJ[i][2])) return proBJ[i][3];
                    else if (tC == Integer.parseInt(proBJ[i][2]) && Math.random() > 0.5) return proBJ[i][3];
                    else return basicArrMetod(round, allowdActions, handIndx);
                }
            }return basicArrMetod(round, allowdActions, handIndx);
        } else if (allowdActions == 2 && playerAces > 0) {
            for (int i = 11; i < 43; i++) {
                if (playerTotal == Integer.parseInt(proBJ[i][0]) && dealersCard == Integer.parseInt(proBJ[i][1]) && tC >= Integer.parseInt(proBJ[i][2]) && proBJ[i][4].equals("t")){
                    if (tC > Integer.parseInt(proBJ[i][2])) return proBJ[i][3];
                    else if (tC == Integer.parseInt(proBJ[i][2]) && Math.random() > 0.5) return proBJ[i][3];
                    else return basicArrMetod(round, allowdActions, handIndx);
                }
            } return basicArrMetod(round, allowdActions, handIndx);
        } else if (allowdActions == 2) {
            for (int i = 11; i < 43; i++) {
                if (playerTotal == Integer.parseInt(proBJ[i][0]) && dealersCard == Integer.parseInt(proBJ[i][1]) && tC >= Integer.parseInt(proBJ[i][2])){
                    if (tC > Integer.parseInt(proBJ[i][2])) return proBJ[i][3];
                    else if (tC == Integer.parseInt(proBJ[i][2]) && Math.random() > 0.5) return proBJ[i][3];
                    else return basicArrMetod(round, allowdActions, handIndx);
                }
            } return basicArrMetod(round, allowdActions, handIndx);
        } else if (allowdActions == 1 && playerAces > 0) {
            for (int i = 11; i < 43; i++) {
                if (playerTotal == Integer.parseInt(proBJ[i][0]) && dealersCard == Integer.parseInt(proBJ[i][1]) && tC >= Integer.parseInt(proBJ[i][2]) && !proBJ[i][3].equals("d") && proBJ[i][4].equals("t")){
                    if (tC > Integer.parseInt(proBJ[i][2])) return proBJ[i][3];
                    else if (tC == Integer.parseInt(proBJ[i][2]) && Math.random() > 0.5) return proBJ[i][3];
                    else return basicArrMetod(round, allowdActions, handIndx);
                }
            } return basicArrMetod(round, allowdActions, handIndx);
        } else if (allowdActions == 1) {
            for (int i = 11; i < 43; i++) {
                if (playerTotal == Integer.parseInt(proBJ[i][0]) && dealersCard == Integer.parseInt(proBJ[i][1]) && tC >= Integer.parseInt(proBJ[i][2]) && !proBJ[i][3].equals("d")){
                    if (tC > Integer.parseInt(proBJ[i][2])) return proBJ[i][3];
                    else if (tC == Integer.parseInt(proBJ[i][2]) && Math.random() > 0.5) return proBJ[i][3];
                    else return basicArrMetod(round, allowdActions, handIndx);
                }
            } return basicArrMetod(round, allowdActions, handIndx);
        }

        return "sp";
    }
}

