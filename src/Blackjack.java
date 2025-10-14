
import java.util.*;

public class Blackjack {
    public static void main(String[] args) {

        int pengar = 100;
        int[] playerCards = new int[21];
        int numPlayerCards=0;
        int playerTotal=0;
        int card = 0;
        int usedCards=0;
        int[] dealerCards = new int[21];
        int numDealerCards = 0;
        int dealerTotal=0;
        int finnsEssFörPlayer = 0;
        int finnsEssFörDealer = 0;
        int antalDeck = 2;
        int bet=0;
        Scanner scanner = new Scanner(System.in);
        String stop = "";
        String action = "";

   int [] deck = new int[antalDeck*52];
        String []färg = {"Hjäter","Clöver","Ruter","Spader"};

        for (int i = 0; i < antalDeck; i++) {
            for (int j = 0; j < 52; j++) {
                int index=j+(i*52);
              if(j%13==0)deck[index]=(110);
              else
                if(j%13>9) deck[index]=100;
              else   deck[index]=((j%13+1)*10);
            }
            for (int j = 0; j < 52; j++) {
                int index= j+(i*52);
                if(j%4==0)deck[index]+=0;
                else
                if(j%4==1)deck[index]+=1;
                else
                if(j%4==2)deck[index]+=2;else
                   deck[index]+=3;
            }
        }
        Arrays.sort(deck);
// I array deck finns alla kort med sit värde i början och ettans positon visar des färg (0=hjäter, 1=clöver, 2=ruter, 3= spader),
// för värdet ta arraypositonens värde delat med 10
// för färg ta % 10
        System.out.println("===== Black Jack =====");
       while(!action.equals("stopp")){
           System.out.println("You have "+ pengar+"kr to bet");
           System.out.println("Enter your bet:");
           bet=scanner.nextInt();
           while(bet>pengar||bet<1){
               System.out.println("You can't bet that, try agian");
               bet=scanner.nextInt();
           }
           System.out.println("You have bet "+bet+"kr");

           card = (int)((Math.random()*(deck.length-usedCards))+usedCards);
           playerCards[numPlayerCards]=deck[card];
           if (deck[card]/10==11)finnsEssFörPlayer++;
           playerTotal += deck[card]/10;
           deck[card]=0;
           Arrays.sort(deck);
           usedCards++;
           numPlayerCards++;


           card = (int)((Math.random()*(deck.length-usedCards))+usedCards);
           dealerCards[numDealerCards]=deck[card];
           if (deck[card]/10==11)finnsEssFörDealer++;
           dealerTotal += deck[card]/10;
           deck[card]=0;
           Arrays.sort(deck);
           usedCards++;
           numDealerCards++;

           card = (int)((Math.random()*(deck.length-usedCards))+usedCards);
           playerCards[numPlayerCards]=deck[card];
           if (deck[card]/10==11)finnsEssFörPlayer++;
           playerTotal += deck[card]/10;
           deck[card]=0;
           Arrays.sort(deck);
           usedCards++;
           numPlayerCards++;
        action=scanner.nextLine();


        Arrays.sort(dealerCards);
        Arrays.sort(playerCards);

           while (playerTotal<21&&!action.equals("s")){
               System.out.println("dealer has : "+ dealerCards[20]/10);
               System.out.print("Player has: ");
               for (int i = 0; i < numPlayerCards; i++) {
                   System.out.print(playerCards[20-i]/10+",");
               }
               System.out.print("(total="+playerTotal +")\n");
               System.out.println("hit or stand (h/s)");
               action=scanner.nextLine();


            if(action.equals("h")){
                card = (int)((Math.random()*(deck.length-usedCards))+usedCards);
                playerCards[numPlayerCards]=deck[card];
                if (deck[card]/10==11)finnsEssFörPlayer++;
                playerTotal += deck[card]/10;
                deck[card]=0;
                Arrays.sort(deck);
                usedCards++;
                numPlayerCards++;
            }
            if(playerTotal>21&&finnsEssFörPlayer!=0){
                playerTotal-=10;
                finnsEssFörPlayer--;}
               Arrays.sort(dealerCards);
               Arrays.sort(playerCards);
       }

           if(playerTotal>21) {
               pengar-=bet;
               System.out.print("Player has: ");
               for (int i = 0; i < numPlayerCards; i++) {
                   System.out.print(playerCards[20-i]/10+",");
               }
               System.out.print("(total="+playerTotal +")\n");
               System.out.println("You bust!");
               finnsEssFörDealer =0;
               finnsEssFörPlayer=0;
               playerTotal=0;
               dealerTotal=0;
               numPlayerCards=0;
               numDealerCards=0;
               for (int i = 0; i < 21; i++) {
                   playerCards[i]=0;
               }
               for (int i = 0; i < 21; i++) {
                   dealerCards[i] = 0;
               }
               continue;}

           while (dealerTotal<17) {
               card = (int) ((Math.random() * (deck.length - usedCards)) + usedCards);
               dealerCards[numDealerCards] = deck[card];
               if (deck[card] / 10 == 11) finnsEssFörDealer++;
               dealerTotal += deck[card] / 10;
               if(dealerTotal>21&&finnsEssFörDealer!=0){
                   dealerTotal-=10;
                   finnsEssFörDealer--;
               }
               deck[card]=0;
               Arrays.sort(deck);
               usedCards++;
               numDealerCards++;
               }
           Arrays.sort(dealerCards);
           Arrays.sort(playerCards);
           System.out.print("dealer has : ");
           for (int i = 0; i < numDealerCards; i++) {
               System.out.print((dealerCards[20-i])/10+",");
           }
           System.out.print("(total="+dealerTotal +")\n");


        System.out.print("Player has: ");
           for (int i = 0; i < numPlayerCards; i++) {
               System.out.print(playerCards[20-i]/10+",");
           }
           System.out.print("(total="+playerTotal +")\n");
           if(dealerTotal<=21){
           if(dealerTotal>playerTotal){
               pengar-=bet;
               System.out.println("Dealer wins");
           }else
               if(dealerTotal<playerTotal){
            pengar+= bet;
               System.out.println("You win!");
           }else System.out.println("It's a tie");
           }else
           { System.out.println("Dealer has busted");
               System.out.println("You win");
               pengar+=bet;}

        finnsEssFörDealer =0;
           finnsEssFörPlayer=0;
           playerTotal=0;
           dealerTotal=0;
           numPlayerCards=0;
           numDealerCards=0;
           for (int i = 0; i < 21; i++) {
               playerCards[i]=0;
           }
           for (int i = 0; i < 21; i++) {
               dealerCards[i] = 0;
           }
           action = "";

       }
    }
}


