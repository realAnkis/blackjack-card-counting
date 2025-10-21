import java.lang.reflect.Method;
import java.util.function.Function;

public class SpelaSjälv {

    public static String decideAction() {
        return "s";
    }

    public static void main(String[] args) {
        Blackjack.main(new String[]{}, SpelaSjälv::decideAction);
    }
}
