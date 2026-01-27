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
        return hiLoCountStrat(round, allowedActions, handIndex);
    }


    private double betVariable = trueCount;

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

    /*  ---------------- Index Tabel and Sorter for it ----------------------------*
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
    public static void main(String[] args) {
        LinkedList<int[]> indexSorting = new LinkedList<>();
        for (int i = 0; i < 49; i++) {
            int[] temp = new int[2];
            temp[0] = Integer.parseInt(hiLoIndexTabel[i][2]); // the index
            temp[1] = i; // keeps track of the position in the original list
            indexSorting.add(temp);
        }
        indexSorting.sort(new IndexSort()); // This sorts based on index

        String[][] tempHiLoIndexTabel = new String[49][6];
        String[][] canSplitArr = new String[49][6];
        int canSpT = -1;
        int canSpF = 48;

        for (int i = 0; i < 49; i++) {
            for (int j = 0; j < 6; j++) {
                tempHiLoIndexTabel[i][j] = hiLoIndexTabel[indexSorting.get(i)[1]][j];
            }
            if (tempHiLoIndexTabel[i][5].equals("t")) canSpT += 1;
        }
        for (int i = 0; i < 49; i++) {
            if (tempHiLoIndexTabel[i][5].equals("t")) {
                for (int j = 0; j < 6; j++) {
                    canSplitArr[canSpT][j] = tempHiLoIndexTabel[i][j];
                }
                canSpT -= 1;
            } else if (tempHiLoIndexTabel[i][5].equals("f")) {
                for (int j = 0; j < 6; j++) {
                    canSplitArr[canSpF][j] = tempHiLoIndexTabel[i][j];
                }
                canSpF -= 1;
            }
        }
        for (int i = 0; i < 49; i++) {
            System.out.print("{" + "\"" + canSplitArr[i][0] + "\", " + "\"" + canSplitArr[i][1] + "\", " + "\"" + canSplitArr[i][2] + "\", " + "\"" + canSplitArr[i][3] + "\", " + "\"" + canSplitArr[i][4] + "\", " + "\"" + canSplitArr[i][5] + "\"},   //" + i);
            System.out.println();
        }
    }
    /*---------------- Index Tabel and Sorter for it ----------------------------   */

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
                if ((int) trueCount >= Integer.parseInt(sortedHiLoIndexTabel[i][2]) && round.getHands()[handIndx].getTotal() == Integer.parseInt(sortedHiLoIndexTabel[i][0]) && round.getDealerCard() == Integer.parseInt(sortedHiLoIndexTabel[i][1]))
                    return sortedHiLoIndexTabel[i][3];
            }
            return basicArrMetod(round, allowdActions, handIndx);
        } else if (allowdActions == 2 && round.getHands()[handIndx].getAvailabelAces() > 0) {

            for (int i = 7; i < 49; i++) {
                if ((int) trueCount >= Integer.parseInt(sortedHiLoIndexTabel[i][2]) && round.getHands()[handIndx].getTotal() == Integer.parseInt(sortedHiLoIndexTabel[i][0])
                        && round.getDealerCard() == Integer.parseInt(sortedHiLoIndexTabel[i][1]) && sortedHiLoIndexTabel[i][4].equals("t"))
                    return sortedHiLoIndexTabel[i][3];
            }
            return basicArrMetod(round, allowdActions, handIndx);
        } else if (allowdActions == 2) {
            for (int i = 7; i < 49; i++) {
                if ((int) trueCount >= Integer.parseInt(sortedHiLoIndexTabel[i][2]) && round.getHands()[handIndx].getTotal() == Integer.parseInt(sortedHiLoIndexTabel[i][0])
                        && round.getDealerCard() == Integer.parseInt(sortedHiLoIndexTabel[i][1]) && sortedHiLoIndexTabel[i][4].equals("f"))
                    return sortedHiLoIndexTabel[i][3];
            }
            return basicArrMetod(round, allowdActions, handIndx);

        } else if (allowdActions == 1 && round.getHands()[handIndx].getAvailabelAces() > 0) {

            for (int i = 7; i < 49; i++) {
                if ((int) trueCount >= Integer.parseInt(sortedHiLoIndexTabel[i][2]) && round.getHands()[handIndx].getTotal() == Integer.parseInt(sortedHiLoIndexTabel[i][0])
                        && round.getDealerCard() == Integer.parseInt(sortedHiLoIndexTabel[i][1]) && sortedHiLoIndexTabel[i][4].equals("t") && !sortedHiLoIndexTabel[i][3].equals("d"))
                    return sortedHiLoIndexTabel[i][3];
            }
            return basicArrMetod(round, allowdActions, handIndx);
        } else if (allowdActions == 1) {

            for (int i = 7; i < 49; i++) {
                if ((int) trueCount >= Integer.parseInt(sortedHiLoIndexTabel[i][2]) && round.getHands()[handIndx].getTotal() == Integer.parseInt(sortedHiLoIndexTabel[i][0])
                        && round.getDealerCard() == Integer.parseInt(sortedHiLoIndexTabel[i][1]) && sortedHiLoIndexTabel[i][4].equals("f") && !sortedHiLoIndexTabel[i][3].equals("d"))
                    return sortedHiLoIndexTabel[i][3];
            }
            return basicArrMetod(round, allowdActions, handIndx);
        }
        return basicArrMetod(round, allowdActions, handIndx);
    }

}

