package BlackjackMedKlasser.playMethods.HumanCardCountingPack;

import BlackjackMedKlasser.Card;
import BlackjackMedKlasser.Deck;
import BlackjackMedKlasser.Round;
import BlackjackMedKlasser.Settings;
import BlackjackMedKlasser.playMethods.PlayMethod;

import java.util.LinkedList;


public class HumanCardCounting extends PlayMethod {
    private Settings settings;
    private Deck deck;
    private double count = 0;
    private double trueCount;
    private double cardsPlayedCunter;

    //körs när ett kort delas ut från kortleken
    public HumanCardCounting(Settings settings, Deck deck) {
        this.settings = settings;
        this.deck = deck;

    }

    @Override
    public void cardDealtMethod(Card card) {
        cardsPlayedCunter+=1;
        count += omega2(card);
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
        return omega2Counting(round, allowedActions, handIndex);
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
        if (betVariable >=6 ) return settings.getMaxBet();
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
            {"h", "h", "h", "h", "d", "h", "h", "h", "h", "h"},          //12 AA
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

        return "sp";
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
                    else return proBJBaciStrategy(round, allowdActions, handIndx);
                }
            }
            for (int i = 11; i < 43; i++) {
                if (playerTotal == Integer.parseInt(proBJ[i][0]) && dealersCard == Integer.parseInt(proBJ[i][1]) && tC >= Integer.parseInt(proBJ[i][2])) {
                    if (tC > Integer.parseInt(proBJ[i][2])) return proBJ[i][3];
                    else if (tC == Integer.parseInt(proBJ[i][2]) && Math.random() > 0.5) return proBJ[i][3];
                    else return proBJBaciStrategy(round, allowdActions, handIndx);
                }
            }
            return proBJBaciStrategy(round, allowdActions, handIndx);
        } else if (allowdActions == 2 && playerAces > 0) {
            for (int i = 11; i < 43; i++) {
                if (playerTotal == Integer.parseInt(proBJ[i][0]) && dealersCard == Integer.parseInt(proBJ[i][1]) && tC >= Integer.parseInt(proBJ[i][2]) && proBJ[i][4].equals("t")) {
                    if (tC > Integer.parseInt(proBJ[i][2])) return proBJ[i][3];
                    else if (tC == Integer.parseInt(proBJ[i][2]) && Math.random() > 0.5) return proBJ[i][3];
                    else return proBJBaciStrategy(round, allowdActions, handIndx);
                }
            }
            return proBJBaciStrategy(round, allowdActions, handIndx);
        } else if (allowdActions == 2) {
            for (int i = 11; i < 43; i++) {
                if (playerTotal == Integer.parseInt(proBJ[i][0]) && dealersCard == Integer.parseInt(proBJ[i][1]) && tC >= Integer.parseInt(proBJ[i][2])) {
                    if (tC > Integer.parseInt(proBJ[i][2])) return proBJ[i][3];
                    else if (tC == Integer.parseInt(proBJ[i][2]) && Math.random() > 0.5) return proBJ[i][3];
                    else return proBJBaciStrategy(round, allowdActions, handIndx);
                }
            }
            return proBJBaciStrategy(round, allowdActions, handIndx);
        } else if (allowdActions == 1 && playerAces > 0) {
            for (int i = 11; i < 43; i++) {
                if (playerTotal == Integer.parseInt(proBJ[i][0]) && dealersCard == Integer.parseInt(proBJ[i][1]) && tC >= Integer.parseInt(proBJ[i][2]) && !proBJ[i][3].equals("d") && proBJ[i][4].equals("t")) {
                    if (tC > Integer.parseInt(proBJ[i][2])) return proBJ[i][3];
                    else if (tC == Integer.parseInt(proBJ[i][2]) && Math.random() > 0.5) return proBJ[i][3];
                    else return proBJBaciStrategy(round, allowdActions, handIndx);
                }
            }
            return proBJBaciStrategy(round, allowdActions, handIndx);
        } else if (allowdActions == 1) {
            for (int i = 11; i < 43; i++) {
                if (playerTotal == Integer.parseInt(proBJ[i][0]) && dealersCard == Integer.parseInt(proBJ[i][1]) && tC >= Integer.parseInt(proBJ[i][2]) && !proBJ[i][3].equals("d")) {
                    if (tC > Integer.parseInt(proBJ[i][2])) return proBJ[i][3];
                    else if (tC == Integer.parseInt(proBJ[i][2]) && Math.random() > 0.5) return proBJ[i][3];
                    else return proBJBaciStrategy(round, allowdActions, handIndx);
                }
            }
            return proBJBaciStrategy(round, allowdActions, handIndx);
        }

        return "sp";
    }

    public String[][] proHiLoBasicStategy = {
            // x axel =action vs Dealer card  + soft t/f? + pt
            // y axel =player total
            // {"2" ,"3" ,"4" ,"5" ,"6" ,"7" ,"8" ,"9" ,"10","11","t/f",player total},
            {"-", "-", "sp", "sp", "sp", "sp", "-", "-", "-", "-", "f", "4"},     /*2-2*/ // 0
            {"-", "-", "sp", "sp", "sp", "sp", "-", "-", "-", "-", "f", "6"},     /*3-3*/ //1
            {"-", "-", "-", "sp", "sp", "-", "-", "-", "-", "-", "f", "8"},     /*4-4*/ //2
            {"-", "-", "-", "-", "-", "-", "-", "-", "-", "-", "f", "10"},     /*5-5*/ //3
            {"sp", "sp", "sp", "sp", "sp", "-", "-", "-", "-", "-", "f", "12"},     /*6-6*/ //4
            {"sp", "sp", "sp", "sp", "sp", "sp", "-", "-", "-", "-", "f", "14"},     /*7-7*/ //5
            {"sp", "sp", "sp", "sp", "sp", "sp", "sp", "sp", "sp", "sp", "f", "16"},     /*8-8*/ //6
            {"sp", "sp", "sp", "sp", "sp", "-", "sp", "sp", "-", "-", "f", "18"},     /*9-9*/ //7
            {"sp", "sp", "sp", "sp", "sp", "sp", "sp", "sp", "sp", "sp", "t", "12"},     /*A-A*/ //8
            // Hard <=8   -> inte double
            {"d", "d", "d", "d", "d", "d", "d", "d", "-", "-", "f", "9"},     //9
            {"d", "d", "d", "d", "d", "d", "d", "d", "-", "-", "f", "10"},     //10
            {"d", "d", "d", "d", "d", "d", "d", "d", "d", "-", "f", "11"},     //11
            // Hard >=12  ->inte double
            // soft 12 = A-A = pair ->inte double
            {"-", "-", "-", "d", "d", "-", "-", "-", "-", "-", "t", "13"},     //12
            {"-", "-", "d", "d", "d", "-", "-", "-", "-", "-", "t", "14"},     //13
            {"-", "-", "d", "d", "d", "-", "-", "-", "-", "-", "t", "15"},     //14
            {"-", "-", "d", "d", "d", "-", "-", "-", "-", "-", "t", "16"},     //15
            {"-", "d", "d", "d", "d", "-", "-", "-", "-", "-", "t", "17"},     //16
            {"-", "d", "d", "d", "d", "-", "-", "-", "-", "-", "t", "18"},     //17
            // Soft >=19  -> inte double
            {"h", "h", "h", "h", "h", "h", "h", "h", "h", "h", "t", "12"},     //18
            {"h", "h", "h", "h", "h", "h", "h", "h", "h", "h", "t", "13"},     //19
            {"h", "h", "h", "h", "h", "h", "h", "h", "h", "h", "t", "14"},     //20
            {"h", "h", "h", "h", "h", "h", "h", "h", "h", "h", "t", "15"},     //21
            {"h", "h", "h", "h", "h", "h", "h", "h", "h", "h", "t", "16"},     //22
            {"h", "h", "h", "h", "h", "h", "h", "h", "h", "h", "t", "17"},     //23
            {"s", "s", "s", "s", "s", "s", "s", "h", "h", "h", "t", "18"},     //24
            {"s", "s", "s", "s", "s", "s", "s", "s", "s", "s", "t", "19"},     //25
            {"s", "s", "s", "s", "s", "s", "s", "s", "s", "s", "t", "20"},     //26
            // icke soft
            {"h", "h", "h", "h", "h", "h", "h", "h", "h", "h", "f", "4"},     //27
            {"h", "h", "h", "h", "h", "h", "h", "h", "h", "h", "f", "5"},     //28
            {"h", "h", "h", "h", "h", "h", "h", "h", "h", "h", "f", "6"},     //29
            {"h", "h", "h", "h", "h", "h", "h", "h", "h", "h", "f", "7"},     //30
            {"h", "h", "h", "h", "h", "h", "h", "h", "h", "h", "f", "8"},     //31
            {"h", "h", "h", "h", "h", "h", "h", "h", "h", "h", "f", "9"},     //32
            {"h", "h", "h", "h", "h", "h", "h", "h", "h", "h", "f", "10"},     //33
            {"h", "h", "h", "h", "h", "h", "h", "h", "h", "h", "f", "11"},     //34
            {"h", "h", "s", "s", "s", "h", "h", "h", "h", "h", "f", "12"},     //35
            {"s", "s", "s", "s", "s", "h", "h", "h", "h", "h", "f", "13"},     //36
            {"s", "s", "s", "s", "s", "h", "h", "h", "h", "h", "f", "14"},     //37
            {"s", "s", "s", "s", "s", "h", "h", "h", "h", "h", "f", "15"},     //38
            {"s", "s", "s", "s", "s", "h", "h", "h", "h", "h", "f", "16"},     //39
            {"s", "s", "s", "s", "s", "s", "s", "s", "s", "s", "f", "17"},     //40
            {"s", "s", "s", "s", "s", "s", "s", "s", "s", "s", "f", "18"},     //41
            {"s", "s", "s", "s", "s", "s", "s", "s", "s", "s", "f", "19"},     //42
            {"s", "s", "s", "s", "s", "s", "s", "s", "s", "s", "f", "20"},     //43
    };

    public String proBJBaciStrategy(Round round, int allowdActions, int handIndx) {
        int playerTotal = round.getHands()[handIndx].getTotal();
        int playerAces = round.getHands()[handIndx].getAvailabelAces();
        int dealersCard = round.getDealerCard();
        int action = dealersCard - 2;

        if (allowdActions == 3 && playerAces > 0) {
            return "sp";  //a soft pair is allways A-A which you allways split
        } else if (allowdActions == 3 && playerAces == 0) {
            for (int i = 0; i <= 43; i++) {
                if (!proHiLoBasicStategy[i][action].equals("-") // action is not blank
                        && Integer.parseInt(proHiLoBasicStategy[i][11]) == playerTotal // pt = pt
                        && proHiLoBasicStategy[i][10].equals("f") //has ace=?
                )
                    return proHiLoBasicStategy[i][action]; // borde alltid vara sp
            }
            return "sp"; // return is "sp" by deafult so it has the highest chance of causeing an error insted of getting stuck in a loop not doing anything
        } else if (allowdActions == 2 && playerAces > 0) {
            for (int i = 12; i <= 43; i++) { // i starts at 12 because it cant be a pair and is soft
                if (!proHiLoBasicStategy[i][action].equals("-") // action is not blank
                        && Integer.parseInt(proHiLoBasicStategy[i][11]) == playerTotal // pt = pt
                        && proHiLoBasicStategy[i][10].equals("t") //has ace=?
                )
                    return proHiLoBasicStategy[i][action];
            }
            return "sp"; // return is "sp" by deafult so it has the highest chance of causeing an error insted of getting stuck in a loop not doing anything
        } else if (allowdActions == 2 && playerAces == 0) {
            for (int i = 9; i <= 43; i++) { // i starts at 9 because it cant be a pair
                if (!proHiLoBasicStategy[i][action].equals("-") // action is not blank
                        && Integer.parseInt(proHiLoBasicStategy[i][11]) == playerTotal // pt = pt
                        && proHiLoBasicStategy[i][10].equals("f") //has ace=?
                )
                    return proHiLoBasicStategy[i][action];
            }
            return "sp"; // return is "sp" by deafult so it has the highest chance of causeing an error insted of getting stuck in a loop not doing anything
        } else if (allowdActions == 1 && playerAces > 0) {
            for (int i = 18; i <= 43; i++) { // i starts at 18 because it cant be a pair and you cant double
                if (!proHiLoBasicStategy[i][action].equals("-") // action is not blank
                        && Integer.parseInt(proHiLoBasicStategy[i][11]) == playerTotal // pt = pt
                        && proHiLoBasicStategy[i][10].equals("t") //has ace=?
                )
                    return proHiLoBasicStategy[i][action];
            }
            return "sp"; // return is "sp" by deafult so it has the highest chance of causeing an error insted of getting stuck in a loop not doing anything
        } else if (allowdActions == 1 && playerAces == 0) {
            for (int i = 27; i <= 43; i++) { // i starts at 27 because it cant be a pair and you cant double and is not soft
                if (!proHiLoBasicStategy[i][action].equals("-") // action is not blank
                        && Integer.parseInt(proHiLoBasicStategy[i][11]) == playerTotal // pt = pt
                        && proHiLoBasicStategy[i][10].equals("f") //has ace=?
                )
                    return proHiLoBasicStategy[i][action];
            }
            return "sp"; // return is "sp" by deafult so it has the highest chance of causeing an error insted of getting stuck in a loop not doing anything
        }
        return "sp";
    }

    public String[][] omega2BasicStrategy = {
       //     soft  pair
            //{"t/f",t/f,"2" ,"3" ,"4" ,"5" ,"6" ,"7" ,"8" ,"9" ,"10","11", playerTotal},
            //hard
            {"f", "f", "h", "h", "h", "h", "h", "h", "h", "h", "h", "h", "5"},    // 0
            {"f", "f", "h", "h", "h", "h", "h", "h", "h", "h", "h", "h", "6"},    // 1
            {"f", "f", "h", "h", "h", "h", "h", "h", "h", "h", "h", "h", "7"},    // 2
            {"f", "f", "h", "h", "h", "h", "h", "h", "h", "h", "h", "h", "8"},    // 3
            {"f", "f", "h", "d", "d", "d", "d", "h", "h", "h", "h", "h", "9"},    // 4
            {"f", "f", "d", "d", "d", "d", "d", "d", "d", "d", "h", "h", "10"},    // 5
            {"f", "f", "d", "d", "d", "d", "d", "d", "d", "d", "d", "h", "11"},    // 6
            {"f", "f", "h", "h", "s", "s", "s", "h", "h", "h", "h", "h", "12"},    // 7
            {"f", "f", "s", "s", "s", "s", "s", "h", "h", "h", "h", "h", "13"},    // 8
            {"f", "f", "s", "s", "s", "s", "s", "h", "h", "h", "h", "h", "14"},    // 9
            {"f", "f", "s", "s", "s", "s", "s", "h", "h", "h", "h", "h", "15"},    // 10
            {"f", "f", "s", "s", "s", "s", "s", "h", "h", "h", "h", "h", "16"},    // 11
            {"f", "f", "s", "s", "s", "s", "s", "s", "s", "s", "s", "s", "17"},    // 12
            {"f", "f", "s", "s", "s", "s", "s", "s", "s", "s", "s", "s", "18"},    // 13
            {"f", "f", "s", "s", "s", "s", "s", "s", "s", "s", "s", "s", "19"},    // 14
            {"f", "f", "s", "s", "s", "s", "s", "s", "s", "s", "s", "s", "20"},    // 15
            //soft
            {"t", "t", "sp", "sp", "sp", "sp", "sp", "sp", "sp", "sp", "sp", "sp", "12"},    // 16/* A-A  */
            {"t", "f", "h", "h", "h", "d", "d", "h", "h", "h", "h", "h", "13"},    // 17/* A-2  */
            {"t", "f", "h", "h", "h", "d", "d", "h", "h", "h", "h", "h", "14"},    // 18/* A-3  */
            {"t", "f", "h", "h", "d", "d", "d", "h", "h", "h", "h", "h", "15"},    // 19/* A-4  */
            {"t", "f", "h", "h", "d", "d", "d", "h", "h", "h", "h", "h", "16"},    // 20/* A-5  */
            {"t", "f", "h", "d", "d", "d", "d", "h", "h", "h", "h", "h", "17"},    // 21/* A-6  */
            {"t", "f", "s", "d", "d", "d", "d", "s", "s", "h", "h", "h", "18"},    // 22/* A-7  */
            {"t", "f", "s", "s", "s", "s", "s", "s", "s", "s", "s", "s", "19"},    // 23/* A-8  */
            {"t", "f", "s", "s", "s", "s", "s", "s", "s", "s", "s", "s", "20"},    // 24/* A-9  */
            //pair
            {"f", "t", "h", "h", "sp", "sp", "sp", "sp", "h", "h", "h", "h", "4"},    // 25/* 2-2  */
            {"f", "t", "h", "h", "sp", "sp", "sp", "sp", "h", "h", "h", "h", "6"},    // 26/* 3-3  */
            {"f", "t", "h", "h", "h", "h", "h", "h", "h", "h", "h", "h", "8"},    // 27/* 4-4  */
            {"f", "t", "d", "d", "d", "d", "d", "d", "d", "d", "h", "h", "10"},    // 28/* 5-5  */
            {"f", "t", "h", "sp", "sp", "sp", "sp", "h", "h", "h", "h", "h", "12"},    // 29/* 6-6  */
            {"f", "t", "sp", "sp", "sp", "sp", "sp", "sp", "h", "h", "h", "h", "14"},    // 30/* 7-7  */
            {"f", "t", "sp", "sp", "sp", "sp", "sp", "sp", "sp", "sp", "sp", "sp", "16"},    // 31/* 8-8  */
            {"f", "t", "sp", "sp", "sp", "sp", "sp", "s", "sp", "sp", "s", "s", "18"},    // 32/* 9-9  */
            {"f", "t", "s", "s", "s", "s", "s", "s", "s", "s", "s", "s", "20"},    // 33/*10-10 */
            // if not allowed to "d" than "h" exept A-7 (soft 18) stand if you cant hit
    };
    public String[][] omega2CountStrategy = {
            // some actions are replaced with tc, if action is * than its described under paragraf and should be handeld later
            //soft   pair
            //{"t/f",t/f,"2"  ,"3"  ,"4"  ,"5"  ,"6"  ,"7"  ,"8"  ,"9"  ,"10" ,"11" , playerTotal  },

            //spliting pairs : 7.5b
            {"f", "t", "-7", "-9", "-13", "-18", "sp", "sp", "15", "h", "h", "h", "4"},    // 0/* 2-2  */
            {"f", "t", "-3", "-11", "-15", "-18", "sp", "15", "h", "h", "h", "h", "6"},    // 1 /* 3-3  */
            {"f", "t", "h", "14", "7", "3", "0", "h", "h", "h", "h", "h", "8"},    // 2 /* 4-4  */
            {"f", "t", "*", "*", "*", "*", "*", "*", "*", "*", "*", "*", "10"},    // 3 /* 5-5  */
            {"f", "t", "-2", "-6", "-8", "-12", "-22", "h", "h", "h", "h", "h", "12"},    // 4 /* 6-6  */
            {"f", "t", "-15", "-18", "-21", "sp", "sp", "sp", "-1", "*", "*", "*", "14"},    // 5 /* 7-7  */
            {"f", "t", "sp", "sp", "sp", "sp", "sp", "sp", "sp", "sp", "12", "sp", "16"},    // 6 /* 8-8  */
            {"f", "t", "-3", "-7", "-10", "-11", "-13", "8", "sp", "sp", "s", "9", "18"},    // 7 /* 9-9  */
            {"f", "t", "s", "15", "12", "9", "8", "s", "s", "s", "s", "s", "20"},    // 8 /*10-10 */
            {"t", "t", "-18", "-19", "-20", "-21", "-22", "-15", "-14", "-13", "-13", "-7", "12"},    // 9/* A-A  */
             // * = see tabeles 7.1, or 7.3
            //split if tc>= index otherwise if "*" refer to other sheets
            /* for 8-8 vs 10 and 3-3 vs 7 split when tc is LESS than index */

            // doubeling soft hands: 7.4
            {"t", "f", "h", "11", "5", "0", "-3", "h", "h", "h", "h", "h", "13"},    // 10/* A-2  */
            {"t", "f", "h", "10", "2", "-4", "-8", "h", "h", "h", "h", "h", "14"},    // 11/* A-3  */
            {"t", "f", "h", "8", "0", "-7", "-11", "h", "h", "h", "h", "h", "15"},    // 12/* A-4  */
            {"t", "f", "h", "6", "-3", "-10", "-16", "h", "h", "h", "h", "h", "16"},    // 13/* A-5  */
            {"t", "f", "2", "-5", "-9", "-16", "-19", "23", "h", "h", "h", "h", "17"},    // 14/* A-6  */
            {"t", "f", "2", "-2", "-8", "-12", "-12", "s", "*", "h", "h", "*", "18"},    // 15/* A-7  */
            {"t", "f", "s", "9", "6", "3", "2", "s", "s", "s", "s", "s", "19"},    // 16/* A-8  */
            {"t", "f", "s", "16", "13", "10", "9", "s", "s", "s", "s", "s", "20"},    // 17/* A-9  */
            /* se tabel 7.2 */

            //doubeling hard hands: tabel 7.3
            {"f", "f", "h", "17", "12", "7", "5", "h", "h", "h", "h", "h", "8"},    // 18
            {"f", "f", "4", "0", "-3", "-7", "-9", "7", "17", "h", "h", "h", "9"},    //19
            {"f", "f", "-13", "-15", "-17", "-19", "-21", "-10", "-6", "-2", "9", "8", "10"},    // 20
            {"f", "f", "-17", "-18", "-19", "-21", "-23", "-12", "-9", "-6", "-6", "2", "11"},    // 21

            //standing / hitting soft hands  tabel 7.2
            {"t", "f", "h", "h", "h", "h", "h", "h", "h", "h", "h", "h", "17"},    // 22/* A-6  */
            {"t", "f", "s", "s", "s", "s", "s", "s", "-22", "h", "h", "0", "18"},    // 23/* A-7  */
            {"t", "f", "s", "s", "s", "s", "s", "s", "s", "s", "s", "s", "19"},    // 24/* A-8  */
            /* for A-7 vs A stand when tc =+2 or greater in 4+ deck games*/

            //standing / hitting hard hands 7.1
            {"f", "f", "5", "2", "0", "-2", "-2", "h", "h", "h", "h", "h", "12"},    // 25
            {"f", "f", "-1", "-3", "-5", "-8", "-8", "h", "h", "h", "h", "h", "13"},    //26
            {"f", "f", "-7", "-7", "-10", "-12", "-12", "h", "h", "h", "15", "22", "14"},    // 27
            {"f", "f", "-10", "-10", "-14", "-17", "-17", "22", "21", "15", "6", "18", "15"},    // 28
            {"f", "f", "-14", "-17", "-19", "-22", "-20", "15", "14", "7", "0", "14", "16"},    // 29
            {"f", "f", "s", "s", "s", "s", "s", "s", "s", "s", "s", "-10", "17"},    // 30
            // if your 14 cosists of 7-7 stand at +6 or greater in double deck game; +11 or greater in four- or more deck games
    };

    public String omega2BasicStrategy(Round round, int allowdActions, int handIndx) {
        int playerTotal = round.getHands()[handIndx].getTotal();
        int playerAces = round.getHands()[handIndx].getAvailabelAces();
        int action = round.getDealerCard();

        if (allowdActions == 3 && playerAces > 0) {
            return "sp";  //a soft pair is allways A-A which you allways split
        } else if (allowdActions == 3 && playerAces == 0) {// pairs
            return omega2BasicStrategy[25 + ((playerTotal - 4) / 2)][action];
        } else if (allowdActions == 2 && playerAces > 0) { // soft first 2 cards => A - (2-9)
            if(playerTotal==12)return "h";
            if (playerTotal > 20 || playerTotal < 13) return "sp";
            return omega2BasicStrategy[playerTotal + 4][action];
        } else if (allowdActions == 2 && playerAces == 0) { // hard first 2 cards
            if (playerTotal==4)return "h";
           if (playerTotal - 5 > 15 || playerTotal - 5 < 0) return "sp";
            return omega2BasicStrategy[playerTotal - 5][action];
        } else if (allowdActions == 1 && playerAces > 0) { // soft any cards
            if (playerTotal > 20 || playerTotal < 12) return "sp";
            if (playerTotal == 12) return "h";
            if (omega2BasicStrategy[playerTotal + 4][action].equals("d") && playerTotal < 18) return "h";
            if (omega2BasicStrategy[playerTotal + 4][action].equals("d") && playerTotal >= 18) return "s";
            return omega2BasicStrategy[playerTotal +4][action];
        } else if (allowdActions == 1 && playerAces == 0) {// hard any cards
            if (playerTotal - 5 > 15 || playerTotal - 5 < 0) return "sp";
            if (omega2BasicStrategy[playerTotal - 5][action].equals("d")) return "h";
            return omega2BasicStrategy[playerTotal - 5][action];
        }
        return "sp";
    }
    public String omega2Counting(Round round, int allowdActions, int handIndx) {
        int playerTotal = round.getHands()[handIndx].getTotal();
        int playerAces = round.getHands()[handIndx].getAvailabelAces();
        int dealerCard = round.getDealerCard();
       // System.out.println(cardsPlayedCunter);
        //System.out.println("player total= "+ playerTotal +" ,playerAces= " + playerAces + " ,dealerCard= "+dealerCard + " ,allowedACtopns= " + allowdActions + " ,TC= " + trueCount);
        if (allowdActions == 3 && playerAces > 0) { // soft pair
            if (playerTotal !=12) {
                System.out.println("soft pari =/= 12"); return "Loop";
            }
            if(trueCount >= Integer.parseInt(omega2CountStrategy[9][dealerCard])) return "sp";
            else return "h";
        } else  if (allowdActions == 3 && playerAces == 0) { // hard pair
            if(omega2CountStrategy[((playerTotal - 4) / 2)][dealerCard].equals("*")){
                if (playerTotal==10){
                    if(trueCount >= Integer.parseInt(omega2CountStrategy[20][dealerCard])) return "d";
                    else return "h";
                }
                if(playerTotal == 14 && dealerCard>=9) {
                    if (dealerCard==10){
                        if (deck.getNumberOfDecks()>=4) {if(trueCount >= 11) return "s";
                        else return "h";}
                        if (deck.getNumberOfDecks()<4) {if(trueCount >= 6) return "s";
                        else return "h";}
                    }
                    try{
                    if(trueCount >= Integer.parseInt(omega2CountStrategy[27][dealerCard])) return "s";
                    else return "h";} catch (NumberFormatException e) {
                        return omega2CountStrategy[27][dealerCard];
                    }
                }
            }
            if(playerTotal==16 && dealerCard==10 && omega2CountStrategy[((playerTotal - 4) / 2)][1].equals("t")){
                if(trueCount < Integer.parseInt(omega2CountStrategy[((playerTotal - 4) / 2)][dealerCard])) return "sp";
                else return "s";
            }
            if(playerTotal==6 && dealerCard==7 && omega2CountStrategy[((playerTotal - 4) / 2)][1].equals("t")){
                if(trueCount < Integer.parseInt(omega2CountStrategy[((playerTotal - 4) / 2)][dealerCard])) return "sp";
                else return "h";
            }
            try{
            if(trueCount >= Integer.parseInt(omega2CountStrategy[((playerTotal - 4) / 2)][dealerCard])) return "sp";
            else return omega2BasicStrategy(round, allowdActions, handIndx);} catch (NumberFormatException e) {
               return omega2CountStrategy[((playerTotal - 4) / 2)][dealerCard];
            }
        }else if (allowdActions == 2 && playerAces > 0) { // soft 2 cards
            if(playerTotal==12){return omega2BasicStrategy(round, allowdActions, handIndx);}
            if(deck.getNumberOfDecks()>=4) {
                if (playerTotal == 18 && dealerCard == 11) {
                    if (trueCount >= 2) return "s";
                    else return "h";
                }
            }else if(playerTotal==18 && dealerCard==11){ if(trueCount >= Integer.parseInt(omega2CountStrategy[23][dealerCard])) return "s"; else return"h";}
            if (playerTotal == 18 && dealerCard == 8) {
                if (trueCount >= Integer.parseInt(omega2CountStrategy[23][dealerCard])) return "s";
                else return "h";
            }
        try{if(trueCount >= Integer.parseInt(omega2CountStrategy[playerTotal-3][dealerCard])) return "d"; else return "h" ; }catch (
                NumberFormatException e) {
           return omega2CountStrategy[playerTotal-3][dealerCard];
        }

        } else  if (allowdActions == 2 && playerAces == 0) { // hard 2 cards
            if(playerTotal>7&& playerTotal<12) {try {if(trueCount >= Integer.parseInt(omega2CountStrategy[playerTotal+10][dealerCard])) return "d"; else return "h"; } catch (
                    NumberFormatException e) {
                return omega2CountStrategy[playerTotal+10][dealerCard];
            }}
            if(playerTotal>11&& playerTotal<18){try
            {if(trueCount >= Integer.parseInt(omega2CountStrategy[playerTotal+13][dealerCard])) return "d"; else return "h"; } catch (
                    NumberFormatException e) {
                return omega2CountStrategy[playerTotal+13][dealerCard];}
            }
            return omega2BasicStrategy(round,allowdActions,handIndx);

        }else if (allowdActions == 1 && playerAces > 0) {// soft 3+ cards
           if (playerTotal>16 && playerTotal<20){
               try{if (trueCount>=Integer.parseInt(omega2CountStrategy[playerTotal+5][dealerCard]))return "s"; else return "h";
           } catch (NumberFormatException e) {
                   if(omega2CountStrategy[playerTotal+5][dealerCard].equals("d")) System.out.println("Doubeling when not allowed");
                   return omega2CountStrategy[playerTotal+5][dealerCard];
               }
        }
            if (omega2BasicStrategy(round,allowdActions,handIndx).equals("d")) System.out.println("Doubeling when not allowed");
           return omega2BasicStrategy(round, allowdActions, handIndx);
        } else  if (allowdActions == 1 && playerAces == 0) { // hard 3+ cards
            if(playerTotal>11&& playerTotal<18){try
            {if(trueCount >= Integer.parseInt(omega2CountStrategy[playerTotal+13][dealerCard])) return "d"; else return "h"; } catch (
                    NumberFormatException e) {
                if(omega2CountStrategy[playerTotal+13][dealerCard].equals("d")) System.out.println("Doubeling when not allowed");
                return omega2CountStrategy[playerTotal+13][dealerCard];}
            }
            if (omega2BasicStrategy(round,allowdActions,handIndx).equals("d")) System.out.println("Doubeling when not allowed");
            return omega2BasicStrategy(round,allowdActions,handIndx);
        }
        return "sp";
    }

}

