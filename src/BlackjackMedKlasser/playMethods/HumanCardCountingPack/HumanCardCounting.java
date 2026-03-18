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
    private int countInsurance = 0;
    private double trueCount;
    private double perfectBetcount;
    private double TrueperfectBetcount;
    private double perfectPlaycount;
    private IndexSort indexSort = new IndexSort();
    private final String[][] hardTotals1 = indexSort.getHardTotals1();
    private final String[][] hardTotals2 = indexSort.getHardTotals2();
    private final String[][] softTotals1 = indexSort.getSoftTotals1();
    private final String[][] softTotals2 = indexSort.getSoftTotals2();
    private final String[][] pairs = indexSort.getPairs();
    private final String[][] sortedHiLoIndexTabel = indexSort.getSortedHiLoIndexTabel();
    private final String[][] halvsTabel = indexSort.getHalvsTabel();
    private final String[][] proBJ = indexSort.getProBJ();
    private final String[][] proHiLoBasicStategy = indexSort.getProHiLoBasicStategy();
    private final String[][] omega2BasicStrategy = indexSort.getOmega2BasicStrategy();
    private final String[][] omega2CountStrategy = indexSort.getOmega2CountStrategy();
    private final String[][] omega2CSTPairs = indexSort.getOmega2CSTPairs();
    private final String[][] omega2CSTDouble_S = indexSort.getOmega2CSTDouble_S();
    private final String[][] omega2CSTDouble_H = indexSort.getOmega2CSTDouble_H();
    private final String[][] omega2CSTStand_S = indexSort.getOmega2CSTStand_S();
    private final String[][] omega2CSTStand_H = indexSort.getOmega2CSTStand_H();


    public HumanCardCounting(Settings settings, Deck deck) {
        this.settings = settings;
        this.deck = deck;

    }

    @Override
    public void cardDealtMethod(Card card) {    //körs när ett kort delas ut från kortleken
        count += omega2(card);
        countInsurance += insuranceCount(card);
        perfectBetcount += insuranceCount(card);
        perfectPlaycount += insuranceCount(card);
        int deckSize = deck.getSizeOfDeck();
        if (deckSize < 1) deckSize = 1;
        trueCount = (count / ((double) deckSize / 52));
        TrueperfectBetcount = (perfectBetcount / ((double) deckSize / 52));

    }


    //körs när kortleken blandas
    @Override
    public void reshuffleMethod() {
        count = 0;
        countInsurance = 0;
        perfectBetcount = 0;
        perfectPlaycount = 0;
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
        int betUnit =settings.getMaxBet()/20;
        int bet;
        if (betVariable <= 0) bet = settings.getMinBet();
        else if (betVariable <= 1) bet = ((settings.getMaxBet()/10 )* 2);
        else if (betVariable <= 2) bet = ((settings.getMaxBet()/10 )* 4);
        else if (betVariable <= 3) bet = ((settings.getMaxBet()/10 )* 6);
        else if (betVariable <= 4) bet = ((settings.getMaxBet()/10 )* 8);
        else if (betVariable <= 5) bet = ((settings.getMaxBet()/10 )* 10);
       /*else if (betVariable <= 1) bet = (betUnit*3);
        else if (betVariable <= 2) bet = (betUnit*6);
        else if (betVariable <= 3) bet = (betUnit*10);
        else if (betVariable <= 4) bet = (betUnit*15);
        else if (betVariable <= 5) bet = (betUnit*20);*/
        else bet = settings.getMaxBet();
        return bet;/**/
    }
    //körs ifall möjlighet för ett insurance bet finns (dvs. ifall dealern har ett ess)
    @Override
    public int insuranceBetMethod(Round round) {

        if(countInsurance > 0) return settings.getMaxBet(); // for insuranceCount
        //if (count > 2) return settings.getMaxBet(); // for hiLo
        //if (trueCount > 5) return settings.getMaxBet(); // for omega2
        else  return 0;/**/
    }
    //counting methods

    public double perfectPlay(Card card) {
        if      (card.getValue() == 2)  return 3;
        else if (card.getValue() == 3)  return 4;
        else if (card.getValue() == 4 ) return 6;
        else if (card.getValue() == 5)  return 8;
        else if (card.getValue() == 6)  return 6;
        else if (card.getValue() == 7)  return 5;
        else if (card.getValue() == 8)  return 2;
        else if (card.getValue() == 9)  return -2;
        else if (card.getValue() == 10) return -8.5;
        else if (card.getValue() == 11) return 2;
      return 0;
    };
    public double perfectBet(Card card) {
        if      (card.getValue() == 2)  return 5;
        else if (card.getValue() == 3)  return 5;
        else if (card.getValue() == 4 ) return 7;
        else if (card.getValue() == 5)  return 10;
        else if (card.getValue() == 6)  return 5;
        else if (card.getValue() == 7)  return 4;
        else if (card.getValue() == 8)  return 0;
        else if (card.getValue() == 9)  return -2;
        else if (card.getValue() == 10) return -6.5;
        else if (card.getValue() == 11) return -8;
        return 0;
    };
    public int insuranceCount(Card card) {
        if (card.getValue() == 10) return -9;
        else return 4;
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
    public int omega2(Card card) {
        if (card.getValue() == 10) return -2;
        else if (card.getValue() > 7) {
            if (card.getValue() == 9) return -1;
            else return 0;
        } else if (card.getValue() == 4 || card.getValue() == 5 || card.getValue() == 6) return +2;
        else return 1;
    }
    public double halvs(Card card) {
        if (card.getValue() > 9) return -1;
        else if (card.getValue() == 3 || card.getValue() == 4 || card.getValue() == 6) return +1;
        else if (card.getValue() == 9) return -0.5;
        else if (card.getValue() == 8) return 0;
        else if (card.getValue() == 5) return 1.5;
        else return 0.5;
    }



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
            return "sp";
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
    public String omega2CountingSplitString(Round round, int allowdActions, int handIndx){
      double playvariable = trueCount;

        int playerTotal = round.getHands()[handIndx].getTotal();
        int playerAces = round.getHands()[handIndx].getAvailabelAces();
        int dealerCard = round.getDealerCard();
        if(allowdActions==3 && playerAces>0){
            if (playerTotal==Integer.parseInt(omega2CSTPairs[9][12])
                    && omega2CSTPairs[9][1].equals("t") //is a pair
                    && omega2CSTPairs[9][0].equals("t") // is soft?
                    && playvariable>=Integer.parseInt(omega2CSTPairs[9][dealerCard])) return "sp";
            return omega2CountingSplitString(round,2,handIndx);
        }
        else if(allowdActions==3 && playerAces==0){
            for (int i = 0; i < omega2CSTPairs.length-1; i++) {
                try {if (playerTotal==Integer.parseInt(omega2CSTPairs[i][12])
                        && omega2CSTPairs[i][1].equals("t") // is a pair
                        && omega2CSTPairs[i][0].equals("f") // is soft?
                        && playvariable>=Integer.parseInt(omega2CSTPairs[i][dealerCard])
                )return "sp";
                }catch (NumberFormatException n){
                    if (omega2CSTPairs[i][dealerCard].equals("*")) {
                        if (playerTotal==10 && playvariable>=Integer.parseInt(omega2CSTDouble_H[2][dealerCard]) && playerTotal==Integer.parseInt(omega2CSTDouble_H[2][12])){ return "d";}
                        if (playerTotal==14 && dealerCard==9)return "h";
                        if (playerTotal==14 && playvariable>=Integer.parseInt(omega2CSTStand_H[2][dealerCard]) && playerTotal==Integer.parseInt(omega2CSTStand_H[2][12])){ return "s";}
                    }
                    else return omega2CSTPairs[i][dealerCard];
                }
            }
            return omega2BasicStrategy(round,allowdActions,handIndx);
        }else if(allowdActions==2 && playerAces>0){
            for (int i = 0; i < omega2CSTDouble_S.length-1; i++) {
                try {if (playerTotal==Integer.parseInt(omega2CSTDouble_S[i][12])
                        && omega2CSTDouble_S[i][1].equals("f") // is a pair
                        && omega2CSTDouble_S[i][0].equals("t") // is soft?
                        && playvariable>=Integer.parseInt(omega2CSTDouble_S[i][dealerCard])
                )return "d";
        }catch (NumberFormatException n){
                if (omega2CSTDouble_S[i][dealerCard].equals("*"))
                    return omega2CountingSplitString(round,1,handIndx);
                return omega2CSTDouble_S[i][dealerCard];
                }
            }
        return omega2BasicStrategy(round,allowdActions,handIndx);
    }else if(allowdActions==2 && playerAces==0){
            for (int i = 0; i < omega2CSTDouble_H.length-1; i++) {
                try {if (playerTotal==Integer.parseInt(omega2CSTDouble_H[i][12])
                        && omega2CSTDouble_H[i][1].equals("f") // is a pair
                        && omega2CSTDouble_H[i][0].equals("f") // is soft?
                        && playvariable>=Integer.parseInt(omega2CSTDouble_H[i][dealerCard])
                )return "d";
                }catch (NumberFormatException n){
                return omega2CSTDouble_H[i][dealerCard];
            }
        }
            return omega2BasicStrategy(round,allowdActions,handIndx);
        }else if(allowdActions==1 && playerAces>0){
                for (int i = 0; i < omega2CSTStand_S.length-1; i++) {
                    try {if (playerTotal==Integer.parseInt(omega2CSTStand_S[i][12])
                            && omega2CSTStand_S[i][1].equals("f") // is a pair
                            && omega2CSTStand_S[i][0].equals("t") // is soft?
                            && playvariable>=Integer.parseInt(omega2CSTStand_S[i][dealerCard])
                    )return "s";
                    }catch (NumberFormatException n){
                        return omega2CSTStand_S[i][dealerCard];
                    }
                }
            return omega2BasicStrategy(round,allowdActions,handIndx);
        } else if(allowdActions==1 && playerAces==0) {
            for (int i = 0; i < omega2CSTStand_H.length - 1; i++) {
                try {
                    if (playerTotal == Integer.parseInt(omega2CSTStand_H[i][12])
                            && omega2CSTStand_H[i][1].equals("f") // is a pair
                            && omega2CSTStand_H[i][0].equals("f") // is soft?
                            && playvariable >= Integer.parseInt(omega2CSTStand_H[i][dealerCard])
                    ) return "s";
                } catch (NumberFormatException n) {
                    return omega2CSTStand_H[i][dealerCard];
                }
            }
            return omega2BasicStrategy(round, allowdActions, handIndx);
        }
        return omega2BasicStrategy(round, allowdActions, handIndx);
    }
}