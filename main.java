import java.util.Random;
import java.util.LinkedList;
import java.util.Scanner;

/**
 * EXPLANATION:
 * 
 * class Game is an object that instantiates integers Money and Time. A setter and getter method is created to accommodate changing/accessing data.
 * class Wheel is an object that instantiates Payoff, PayOffChance, and CurrentChance. A setter and getter method for PayOff and PayOffChance is created for any user-defined
 * modifications.
 * class MachineBot is an object that acts as the agent, wherein its objective is to have as much money in the given time being allotted.
 * 
 */

class Game {
    private int Money = 0;
    private int Time = 0;

    public int getMoney(){
        return this.Money;
    }

    public void setMoney(int n){
        this.Money = n;
    }

    public int getTime(){
        return this.Time;
    }

    public void setTime(int n){
        this.Time = n;
    }

}

class Wheel {
        private int PayOff = 0;
        private float PayOffChance = 0;
        private float CurrentChance = 0;
        Random rand = new Random();

        public int getPayOff(){ //Getter method for payoff
            return this.PayOff;
        }

        public void setPayOff(int n){ //Setter method for payoff
            this.PayOff = n;
        }

        public float getPayOffChance(){ //Getter method for chance
            return this.PayOffChance;
        }

        public void setPayOffChance(float n){ //Setter method for chance
            this.PayOffChance = n;
        }

        public void roll(Game game) { //Rolling function
            game.setMoney(game.getMoney()-1); //decreases the money for every roll
            game.setTime(game.getTime()-10); //decreases the time for every roll
            this.CurrentChance = rand.nextFloat(100); //random function (acts as an RNG element)
            if (this.CurrentChance <= PayOffChance *100){ //probability if-decision
                game.setMoney(game.getMoney()+this.PayOff); //adds payoff money to the overall money of the player.
            } 
        }
}

/**
 * ALGORITHM OF MACHINE BOT
 * 
 * This agent is Utility-based, as it focuses on having the most amount of money.
 * 
 * 1. Runs both fortune wheels if it does not have any actions for an amount of an epoch (in this case, epoch = 5), during this phase, it sets its action into, "rolling"
 * 2. Agent compares the history of both fortune wheels, and see which one has a bigger value in terms of payoff.
 * 3. If a fortune wheel has a bigger value than the other, the agent sets its action to "preferred_A" or "preferred_B"
 * 4. If both fortune wheels have an equal value, it sets its own action into "none", and the agent runs step 1 over again.
 * 
 * WHAT IS AN EPOCH?
 * An epoch is the amount of memory the agent is given, so the bot can calculate the values based on how far the epoch was set.
 * An optimal epoch value can improve the agent's performance.
 * 
 *
 * Time Complexity:
 * O(epoch)
 * 
 * Space Complexity:
 * O(n+k)
 * 
 */

class MachineBot {
    private String action = "none";
    private LinkedList<Integer> LWheelA = new LinkedList<Integer>(); //Wheel A history
    private LinkedList<Integer> LWheelB = new LinkedList<Integer>(); //Wheel B history
    private LinkedList<String> BotActions = new LinkedList<String>(); //Bot interaction history logs
    private LinkedList<Integer> Rounds = new LinkedList<Integer>(); //Rounds history logs
    private int epoch = 5; //epoch value
    private int tempA; //tempA storage value
    private int tempB; //tempB storage value

    public LinkedList<Integer> getListA() { //For debugging purposes
        return this.LWheelA;
    }

    public LinkedList<Integer> getListB() { //For debugging purposes
        return this.LWheelB;
    }

    public LinkedList<String> getBotActions() { //For debugging purposes
        return this.BotActions;
    }

    public LinkedList<Integer> getRounds() { //For debugging purposes
        return this.Rounds;
    }


    public String getAction() { //Returns the preferred wheel of the agent
        return this.action;
    }

    public void solve(Game game, Wheel wheelA, Wheel wheelB) {
        if (this.action.equals("preferred_A")) {
            BotActions.add(this.action);
            Rounds.add(game.getTime()/10);
            LWheelA.add(game.getMoney());
            wheelA.roll(game);
        } else if (this.action.equals("preferred_B")) {
            BotActions.add(this.action);
            Rounds.add(game.getTime()/10);
            LWheelB.add(game.getMoney());
            wheelB.roll(game);
        }

        if ((LWheelA.size() % epoch == 0) && (LWheelB.size() % epoch == 0) && (LWheelA.size() > 0) && (LWheelB.size() > 0)) {
            for (int j = LWheelA.size() - epoch; j < LWheelA.size(); j++) {
                this.tempA = this.tempA + LWheelA.get(j); //Precept history of wheelA
            }


            for (int k = LWheelB.size() - epoch; k < LWheelB.size(); k++) {
                this.tempB = this.tempB + LWheelB.get(k); //Precept history of wheelB
            }

            /**
             * Below this line is a comparison tree that determines the future action of the agent. If tempA is greater than tempB, then the first wheel will be accepted, if tempB is greater than tempA
             * then the second wheel will be accepted.
             * 
             * Otherwise, if both are equal, then a roll will be made again.
             * 
             */

            if (this.tempA > this.tempB){
                this.action = "preferred_A";
            } else if (tempA < tempB){
                this.action = "preferred_B";
            } else if (tempA == tempB){
                this.action = "none";
            }
        } 
         else if (this.action.equals("none")) {
            this.action = "rolling";
            BotActions.add(this.action);
            Rounds.add(game.getTime()/10);
            wheelA.roll(game);
            LWheelA.add(game.getMoney()); //Initial stage, where precept history is below the epoch value

            BotActions.add(this.action);
            Rounds.add(game.getTime()/10);
            wheelB.roll(game);
            LWheelB.add(game.getMoney()); //Initial stage, where precept history is below the epoch value
            this.action = "none";

        }
    }


}

public class main {

public static void main(String[] args){

    /**
     * EVERYTHING BELOW HERE IS JUST TO CREATE THE GAME
     * 
     * 
     */

    Scanner scan = new Scanner(System.in);
    Game game = new Game();

    System.out.print("Set initial money: \n");
    game.setMoney((int) scan.nextInt());
    System.out.print("Set initial Time: \n");
    game.setTime((int) scan.nextInt());
    int oldTime = game.getTime();
    int oldMoney = game.getMoney();
    Wheel wheelA = new Wheel();
    System.out.print("Set Wheel A's Payoff: \n");
    wheelA.setPayOff((int) scan.nextInt());
    System.out.print("Set Wheel A's Payoff chance: \n");
    wheelA.setPayOffChance((float) scan.nextInt()/100);
    Wheel wheelB = new Wheel();
    System.out.print("Set Wheel B's Payoff: \n");
    wheelB.setPayOff((int) scan.nextInt());
    System.out.print("Set Wheel B's Payoff chance: \n");
    wheelB.setPayOffChance((float) scan.nextInt()/100);

    MachineBot bot = new MachineBot();
    while (game.getMoney() > 0 && game.getTime() > 0){
        bot.solve(game, wheelA, wheelB);
    }  
    System.out.println(game.getMoney() + " Current money");
    System.out.println(game.getMoney() - oldMoney + " Earned after " + oldTime + " seconds.");

    System.out.println(bot.getAction());

    System.out.println("Would you like a detailed log of everything that transpired? y/n");
    String ans = scan.next();
    if (ans.equals("y")) {
            System.out.println("Wheel A logs: " + bot.getListA());
            System.out.println("Wheel B logs: " + bot.getListB());
            System.out.println("Rounds: " + bot.getRounds());
            System.out.println("Actions: " + bot.getBotActions());
        } else {
            //Nothing happens
        }
    }
}
