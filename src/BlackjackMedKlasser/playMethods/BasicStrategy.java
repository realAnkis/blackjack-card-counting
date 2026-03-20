package BlackjackMedKlasser.playMethods;
import BlackjackMedKlasser.Card;
import BlackjackMedKlasser.Round;
import BlackjackMedKlasser.Settings;

public class BasicStrategy extends PlayMethod {

    private Settings settings;

    public BasicStrategy(Settings settings) {

        this.settings = settings;

    }

//körs när ett kort delas ut från kortleken

    @Override

    public void cardDealtMethod(Card card) {

    }

//körs när kortleken blandas

    @Override

    public void reshuffleMethod() {

    }

//körs när en aktion i spelet behöver bestämmas, bör returna "s", "h", "d" eller "sp"

//allowedActions har värdet 3 ifall alla är tillåtna, 2 ifall "h, s or d" är tillåtet och 1 om bara "h or s" är tillåtet

//handIndex är den hand som just nu spelas, bör användas i till exempel: round.getHands()[handIndex].getTotal()

    @Override

    public String actionMethod(Round round, int allowedActions, int handIndex) {

        String[][] hardHand = new String[][]{

                {"h", "h", "h", "h", "h", "h", "h", "h", "h", "h"},

                {"h", "d/h", "d/h", "d/h", "d/h", "h", "h", "h", "h", "h"},

                {"d/h", "d/h", "d/h", "d/h", "d/h", "d/h", "d/h", "d/h", "h", "h"},

                {"d/h", "d/h", "d/h", "d/h", "d/h", "d/h", "d/h", "d/h", "d/h", "d/h"},

                {"h", "h", "s", "s", "s", "h", "h", "h", "h", "h"},

                {"s", "s", "s", "s", "s", "h", "h", "h", "h", "h"},

                {"s", "s", "s", "s", "s", "h", "h", "h", "h", "h"},

                {"s", "s", "s", "s", "s", "h", "h", "h", "h", "h"},

                {"s", "s", "s", "s", "s", "h", "h", "h", "h", "h"},

                {"s", "s", "s", "s", "s", "s", "s", "s", "s", "s"}




        };

        String[][] softHand = new String[][]{

                {"h", "h", "h", "h", "d/h", "h", "h", "h", "h", "h"},

                {"h", "h", "h", "d/h", "d/h", "h", "h", "h", "h", "h"},

                {"h", "h", "h", "d/h", "d/h", "h", "h", "h", "h", "h"},

                {"h", "h", "d/h", "d/h", "d/h", "h", "h", "h", "h", "h"},

                {"h", "h", "d/h", "d/h", "d/h", "h", "h", "h", "h", "h"},

                {"h", "d/h", "d/h", "d/h", "d/h", "h", "h", "h", "h", "h"},

                {"d/s", "d/s", "d/s", "d/s", "d/s", "s", "s", "h", "h", "h"},

                {"s", "s", "s", "s", "d/s", "s", "s", "s", "s", "s"},

                {"s", "s", "s", "s", "s", "s", "s", "s", "s", "s"}

        };

        String[][] split = new String[][]{

                {"sp", "sp", "sp", "sp", "sp", "sp", "h", "h", "h", "h"},

                {"sp", "sp", "sp", "sp", "sp", "sp", "h", "h", "h", "h"},

                {"h", "h", "h", "sp", "sp", "h", "h", "h", "h", "h"},

                {"d", "d", "d", "d", "d", "d", "d", "d", "h", "h"},

                {"sp", "sp", "sp", "sp", "sp", "h", "h", "h", "h", "h"},

                {"sp", "sp", "sp", "sp", "sp", "sp", "h", "h", "h", "h"},

                {"sp", "sp", "sp", "sp", "sp", "sp", "sp", "sp", "sp", "sp"},

                {"sp", "sp", "sp", "sp", "sp", "s", "sp", "sp", "s", "s"},

                {"s", "s", "s", "s", "s", "s", "s", "s", "s", "s"},

                {"sp", "sp", "sp", "sp", "sp", "sp", "sp", "sp", "sp", "sp"}

        };

        if (allowedActions == 3)

            return split[round.getHands()[handIndex].getCards().get(0).getValue() - 2][round.getDealerCard() - 2];

        if (round.getHands()[handIndex].getAvailabelAces() > 0) {

            String action = softHand[round.getHands()[handIndex].getTotal() - 12][round.getDealerCard() - 2];

            if ("d/h".equals(action)) {

                if (allowedActions == 2) {

                    return "d";

                }

                return "h";

            }

            if ("d/s".equals(action)) {

                if (allowedActions == 2) {

                    return "d";

                }

                return "s";

            }

            return action;

        }

        if (round.getHands()[handIndex].getTotal() >= 17)

            return "s";

        if (round.getHands()[handIndex].getTotal() <= 8)

            return "h";

        String action = hardHand[round.getHands()[handIndex].getTotal() - 8][round.getDealerCard() - 2];

        if ("d/h".equals(action)) {

            if (allowedActions == 2) {

                return "d";

            }

            return "h";

        }

        return action;

    }

//körs när det ursprungliga bettet ska bestämmas

    @Override

    public int betMethod(Round round) {

        return settings.getMinBet();

    }

//körs ifall möjlighet för ett insurance bet finns (dvs. ifall dealern har ett ess)

    @Override

    public int insuranceBetMethod(Round round) {

        return 0;

    }
}
