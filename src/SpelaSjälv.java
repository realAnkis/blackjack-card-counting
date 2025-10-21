import java.util.ArrayList;
import java.util.Scanner;

public class SpelaSjälv {

    static Scanner scanner = new Scanner(System.in);

    public static void cardDealt(int card) {}

    public static String decideAction(int[] args, ArrayList<Integer> deck) {
        return scanner.nextLine();
    }
    public static int decideBet(int[] args, ArrayList<Integer> deck) {
        int bet = scanner.nextInt();
        scanner.nextLine();
        return bet;
    }

    public static void main(String[] args) {
        Blackjack.main(new String[]{}, SpelaSjälv::decideAction, SpelaSjälv::decideBet, SpelaSjälv::cardDealt);
    }

}
