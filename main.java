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

class Game { //OOP class for Game, this is so it's modular and easy to update.
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

        public float getPayOffChance(){ //Getter method for chance, this is for debugging purposes.
            return this.PayOffChance;
        }

        public void setPayOffChance(float n){ //Setter method for chance
            this.PayOffChance = n;
        }

        public void roll(Game game) { //Rolling function
            game.setMoney(game.getMoney()-1); //decreases the money for every roll
            game.setTime(game.getTime()-10); //decreases the time for every roll
            this.CurrentChance = rand.nextFloat(100); //random function (acts as an RNG element)
            this.PayOff = rand.nextInt(1,3); //random function that determines the payoff of the wheel (1, 5, 100 coins)
            switch (this.PayOff) {
                case 1:
                    this.PayOff = 1;
                    break;
                case 2:
                    this.PayOff = 5;
                    break;
                case 3:
                    this.PayOff = 100;
                    break;
                default:
                    break;
            }
            if (this.CurrentChance <= PayOffChance *100){ //probability if-decision
                game.setMoney(game.getMoney()+this.PayOff); //adds payoff money to the overall money of the player.
            } 
        }
}

/**
 * ALGORITHM OF MACHINE BOT
 * 
 * This agent is Utility-based learning agent, as it focuses on having the most amount of money.
 * 
 * 1. Runs both fortune wheels if it does not have any actions for an amount of an epoch (in this case, epoch = 15), during this phase, it sets its action into, "rolling"
 * 2. Agent compares the history of both fortune wheels, and see which one has a bigger value in terms of payoff divided by the index of epoch * payoff.
 * 3. If a fortune wheel has a bigger value than the other, the agent sets its action to "preferred_A" or "preferred_B"
 * 4. If both fortune wheels have an equal value, it sets its own action into "none", and the agent runs step 1 over again.
 * 
 * WHAT IS AN EPOCH?
 * An epoch is the amount of memory the agent is given, so the bot can calculate the values based on how far the epoch was set.
 * An optimal epoch value can improve the agent's performance.
 * 
 * Generally, a higher epoch value means that the bot would be very accurate in optimizing the most amount of money. But it means that there should be more rounds.
 * 
 * PLEASE NOTE: the amount of rounds should never equal or be lesser than the epoch value. Hence, the optimization of the epoch value is a must.
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
    private int epoch = 15; //epoch value
    private int index; //Index variable to address the proportion problem
    private float tempA; //tempA storage value
    private float tempB; //tempB storage value

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
        if (this.action.equals("preferred_A")) { //Runs this commmand if it prefers wheel A
            BotActions.add(this.action);
            Rounds.add(game.getTime()/10);
            LWheelA.add(game.getMoney());
            wheelA.roll(game);
        } else if (this.action.equals("preferred_B")) { //Runs this commmand if it prefers wheel B
            BotActions.add(this.action);
            Rounds.add(game.getTime()/10);
            LWheelB.add(game.getMoney());
            wheelB.roll(game);
        }

        if ((Rounds.size() % epoch == 0) && (!LWheelA.isEmpty() && !LWheelB.isEmpty())) {
            index = epoch * 100;
            for (int j = LWheelA.size() - epoch; j < LWheelA.size(); j++) {
                this.tempA = this.tempA + LWheelA.get(j); //Precept history of wheelA
            }

            this.tempA = this.tempA / (index);


            for (int k = LWheelB.size() - epoch; k < LWheelB.size(); k++) {
                this.tempB = this.tempB + LWheelB.get(k); //Precept history of wheelB
            }

            this.tempB = this.tempB / (index);
            
            System.out.println(this.tempA + " " + this.tempB);
            /**
             * Below this line is a comparison tree that determines the future action of the agent. If tempA is greater than tempB, then the first wheel will be accepted, if tempB is greater than tempA
             * then the second wheel will be accepted.
             * 
             * Otherwise, if both are equal, then a roll will be made again.
             * 
             */

            if (this.tempA > this.tempB){
                this.action = "preferred_A";
                Rounds.add(game.getTime()/10+1); //considers this step as a half of a round.
            } else if (this.tempA < this.tempB){
                this.action = "preferred_B";
                Rounds.add(game.getTime()/10+1); //considers this step as a half of a round.
            } else if (this.tempA == this.tempB){
                this.action = "none";
                Rounds.add(game.getTime()/10+1); //considers this step as a half of a round.
            } 

        } 
         else if (this.action.equals("none")) {
            this.action = "rolling";
            BotActions.add(this.action);
            Rounds.add(game.getTime()/10);
            wheelA.roll(game);
            LWheelA.add(game.getMoney()); //Initial stage, where precept history is below the epoch value, and the stage where the agent 're-learns' the environment.

            BotActions.add(this.action);
            Rounds.add(game.getTime()/10);
            wheelB.roll(game);
            LWheelB.add(game.getMoney()); //Initial stage, where precept history is below the epoch value, and the stage where the agent 're-learns' the environment.
            this.action = "none";

        }
    }


}

public class main {

public static void main(String[] args){

    /**
     * EVERYTHING BELOW HERE IS JUST TO CREATE THE GAME
     * Everything that was instantiated here is for the sake of demonstration only. This section of the code represents the user-defined variables the users
     * will be placing.
     * 
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
    System.out.print("Set Wheel A's Payoff chance: \n");
    wheelA.setPayOffChance((float) scan.nextInt()/100);
    Wheel wheelB = new Wheel();
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
