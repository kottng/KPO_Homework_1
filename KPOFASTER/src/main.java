import java.util.Scanner;


public class main {
    public static boolean is_it_singleplayer = true;
    public static void main(String[] args) {
        System.out.println("\tHello! This is the game REVERSI.\nUse this terminal to choose the quantity of players and to make a move back.\nTo start a game print \"1\".\n");
        Scanner in = new Scanner(System.in);
        int num = in.nextInt();
        if (num == 1) {
            System.out.print("Input a number of players: ");
            int num_of_players = in.nextInt();
            if (num_of_players == 2) {
                is_it_singleplayer = false;
            }
            new REVERSI(is_it_singleplayer);
        }
    }
}