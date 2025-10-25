import java.util.ArrayList;
import java.util.Scanner;

public class mall {

    static Scanner scanner = new Scanner(System.in);

    //körs när ett kort delas ut från kortleken, args[0] = kortet som delas ut (inte värdet utan själva kortet), args[1] = 0-3 ifall korter delas till spelarens olika högar, 4 ifall det delas till dealern
    public static void cardDealt(Integer[] args) {
    }

    //körs när en aktion i spelet behöver bestämmas, bör returna "s", "h", "d" eller "sp"
    //agrs innehåller [spelarens total (0), dealerns total (1), spelarens ess (2), dealerns ess (3), tillåtna aktioner (har värdet 3 ifall alla är tillåtna, 2 ifall double är tillåtet och 1 om bara hit och stand är tillåtet) (4), antal deck (5), spelarens första kort (används ifall split är tillåtet) (6), dealerns andra kort (för att läggas tillbaka i högen) (7)]
    public static String decideAction(int[] args, ArrayList<Integer> deck) {
        return "s";
    }

    //körs när det ursprungliga bettet ska bestämmas
    public static int decideBet(int[] args, ArrayList<Integer> deck) {
        return 10;
    }

    //körs ifall möjlighet för ett insurance bet finns (dvs. ifall dealern har ett ess), args är tom
    public static int decideInsuranceBet(int[] args, ArrayList<Integer> deck) {
        return 0;
    }

    public static void main(String[] args) {
        Blackjack.main(new String[]{},true, mall::decideAction, mall::decideBet, mall::decideInsuranceBet, mall::cardDealt);
    }

}
