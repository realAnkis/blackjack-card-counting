import java.util.ArrayList;
import java.util.Scanner;

public class SpelaSjälv {

    static Scanner scanner = new Scanner(System.in);

    //körs när ett kort delas ut från kortleken, args[0] = kortet som delas ut (inte värdet utan själva kortet), args[1] = 0 ifall korter delas till spelaren, 1 ifall det delas till dealern, 2 ifall det delas till split
    public static void cardDealt(Integer[] args) {
    }

    //körs när en aktion i spelet behöver bestämmas, bör returna "s", "h", "d" eller "sp"
    //agrs innehåller [spelarens total, dealerns total, spelarens ess, dealerns ess, tillåtna aktioner (3 ifall alla är tillåtna, 2 ifall double är tillåtet och 1 om bara hit och stand är tillåtet), antal deck, spelarens första kort (används ifall split är tillåtet)]
    public static String decideAction(int[] args, ArrayList<Integer> deck) {
        return scanner.nextLine();
    }

    //körs när det ursprungliga bettet ska bestämmas
    public static int decideBet(int[] args, ArrayList<Integer> deck) {
        System.out.println("Enter your bet:");
        int bet = scanner.nextInt();
        scanner.nextLine();
        System.out.println("You have bet " + bet + "kr");
        return bet;
    }

    //körs ifall möjlighet för ett insurance bet finns (dvs. ifall dealern har ett ess), args är tom
    public static int decideInsuranceBet(int[] args, ArrayList<Integer> deck) {
        System.out.println("Insurance bet?");
        return decideBet(new int[0], null);
    }

    public static void main(String[] args) {
        Blackjack.main(new String[]{},true, SpelaSjälv::decideAction, SpelaSjälv::decideBet, SpelaSjälv::decideInsuranceBet, SpelaSjälv::cardDealt);
    }

}
