import java.util.ArrayList;
import java.util.Scanner;

public class Semi_optimal {

    static Scanner scanner = new Scanner(System.in);

    //körs när ett kort delas ut från kortleken
    public static void cardDealt(int card) {
    }

    //körs när en aktion i spelet behöver bestämmas
    public static String decideAction(int[] args, ArrayList<Integer> deck) {
        return scanner.nextLine();
    }

    //körs när det ursprungliga bettet ska bestämmas
    public static int decideBet(int[] args, ArrayList<Integer> deck) {
        int bet = scanner.nextInt();
        scanner.nextLine();
        return bet;
    }

    //körs ifall möjlighet för ett insurance bet finns (dvs. ifall dealern har ett ess)
    public static int decideInsuranceBet(int[] args, ArrayList<Integer> deck) {
        return decideBet(new int[0], null);
    }

    public static void main(String[] args) {
        //Blackjack.main(new String[]{}, Semi_optimal::decideAction, Semi_optimal::decideBet, Semi_optimal::decideInsuranceBet, Semi_optimal::cardDealt);
    }

}
