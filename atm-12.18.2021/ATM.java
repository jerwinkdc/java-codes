import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.util.ArrayList;

//@author Jerwin

public class ATM {
    
    public static void main(String[] args) throws IOException {
        
        ATMMachine machine = new ATMMachine();
        
        machine.initComponents();
        if(machine.getCredentials()){
            System.out.println();
            machine.accessATM();
        }
        
    }
    
    public static class ATMMachine {
        private final BufferedReader atmInput = new BufferedReader(new InputStreamReader(System.in));
        private ArrayList<Members> member = new ArrayList();
        private int mainIndex;

        //MAIN
        public void initComponents(){
            loadDatabase();
        }

        private void loadDatabase(){
            member.add(new Members("John","******","50000"));
            member.add(new Members("Joan","********","25000"));
            member.add(new Members("Shelia","****","65000"));
        }

        public boolean getCredentials() throws IOException {

            String tempUsername, tempPassword, curUsername, curPassword;
            int userIndex;
            boolean userFound, userPass;

            System.out.println("[Enter an empty input to exit]");
            do{

                userIndex = 0;
                userFound = false;
                userPass = false;

                tempUsername = getATMInput("Enter your username: ");
                tempPassword = getATMInput("Enter your password: ");

                     for (int x = 0; x < member.size(); x++) { 
                         curUsername = (member.get(x).getUsername()).toLowerCase();
                         if(tempUsername.equals(curUsername)){
                             userIndex = x;
                             userFound = true;
                             break;
                         }
                     }

                    if(userFound == true){
                        curPassword = (member.get(userIndex).getPassword()).toLowerCase();
                       if(tempPassword.equals(curPassword)){
                          userPass = true;
                       }
                       else{
                           System.out.println("Invalid Password!");
                       }
                    }
                    else{
                       System.out.println("Username Not Found!");
                    }

                }while(!userPass);

             mainIndex = userIndex;
             return userPass;
        }

        public void accessATM() throws IOException{
            String logUsername = member.get(mainIndex).getUsername();
            int choice;

        do{
            choice = 0;
            System.out.println("Welcome "+logUsername+"! (ATM)");
            System.out.println("Enter [1] to Withdraw");
            System.out.println("Enter [2] to Deposit");
            System.out.println("Enter [3] to Check Balance");
            System.out.println("Enter [4] to Logout");
                switch(getATMChoice("Enter the number of operation: ", 4)){
                    case 1:
                     withdraw();
                     break;
                    case 2:
                     deposit();    
                     break;
                    case 3:
                     balance();
                     break;
                    case 4:
                     logoutUser();    
                     break; 
                }

            }while(true);

        }

        //MENUS
        private void withdraw() throws IOException{
            BigDecimal money = member.get(mainIndex).getMoney();
            BigDecimal withdrawn, newDeductedAmount;

            boolean errorOccured;
            System.out.println("\n[Enter 0 to exit]");
            do{
                errorOccured = false;
                withdrawn = getATMUserMoney("Enter the amount of money to be withdrawn:");
                newDeductedAmount = new BigDecimal("0");
                newDeductedAmount = newDeductedAmount.add(money);
                newDeductedAmount = newDeductedAmount.subtract(withdrawn);

                    if(newDeductedAmount.compareTo(BigDecimal.valueOf(100)) == -1){
                        System.out.println("Please withdraw a maintaing balance of \u20B1100!\n");
                        errorOccured = true;
                    }
                    else if(withdrawn.compareTo(BigDecimal.ZERO) == -1){
                        errorOccured = true;
                    }
                    else if(withdrawn.compareTo(BigDecimal.ZERO) == 0){
                        break;
                    }

                        if(errorOccured == false){
                            member.get(mainIndex).setMoney(String.valueOf(newDeductedAmount));
                            System.out.println(withdrawOutput(formatOutput(withdrawn)));
                            System.out.println();
                        }

                }while(errorOccured == true);

        }

        private String withdrawOutput(String withdrawn){
            return "An amount of \u20B1"+withdrawn+" has been withdrawn to your account!";
        }

        private void deposit() throws IOException{
            BigDecimal money = member.get(mainIndex).getMoney();
            BigDecimal deposited, newTotalAmount;
            BigDecimal maxValue = new BigDecimal("999999999999999999");

            boolean errorOccured;
            System.out.println("\n[Enter 0 to exit]");
            do{
                errorOccured = false;
                deposited = getATMUserMoney("Enter the amount of money to be deposited:");
                newTotalAmount = new BigDecimal("0");
                newTotalAmount = newTotalAmount.add(money);
                newTotalAmount = newTotalAmount.add(deposited);

                    if(newTotalAmount.compareTo(maxValue) == 1){
                        errorOccured = true;
                    }
                    else if(deposited.compareTo(BigDecimal.ZERO) == -1){
                        errorOccured = true;
                    }
                    else if(!realMoney(deposited)){
                        errorOccured = true;
                    }
                    else if(deposited.compareTo(BigDecimal.ZERO) == 0){
                        break;
                    }

                    if(errorOccured == false){
                        member.get(mainIndex).setMoney(String.valueOf(newTotalAmount));
                        System.out.println(depositOutput(formatOutput(deposited)));
                        System.out.println();
                    }

                }while(errorOccured == true);

        }

        private String depositOutput(String deposited){
            return "An amount of \u20B1"+deposited+" has been deposited to your account!";
        }

        private void balance(){
            BigDecimal money = member.get(mainIndex).getMoney();
            System.out.println("\nYour balance is \u20B1"+formatOutput(money)+"\n");
        }

        private void logoutUser() throws IOException{
            System.out.println();
            if(getCredentials()){
                System.out.println();
                accessATM();
            }
        }

        //INPUTS
        private String getATMInput(String display) throws IOException {
                String input;
                System.out.print(display);
                    input = atmInput.readLine();
                    input = input.toLowerCase();

                        if(input.length() == 0){
                            System.exit(0);
                        }

                return input;
        }

        private int getATMChoice(String display, int limit) throws IOException{
            boolean errorOccured;
            int input = 0;
            do{
                    System.out.print(display);
                    errorOccured = false;
                    try {

                        input = Integer.parseInt(atmInput.readLine());
                            if(input < 0 || input > limit){
                               errorOccured = true;
                            }
                    }
                    catch(NumberFormatException e){
                        errorOccured = true;
                    }
                }while(errorOccured == true);

            return input;
        }

        private BigDecimal getATMUserMoney(String display) throws IOException{
            boolean errorOccured;
            BigDecimal input = BigDecimal.valueOf(0);
            do{
                    System.out.print(display+"\n");
                    System.out.print("\u20B1");
                    errorOccured = false;
                    try {

                        input = new BigDecimal(atmInput.readLine());
                    }
                    catch(NumberFormatException e){
                        errorOccured = true;
                    }
                    System.out.println();
                }while(errorOccured == true);

            return input;
        }

        private boolean realMoney(BigDecimal input) {
            boolean condition;
            String test = String.valueOf(input);
            if(test.contains(".")){
                int startSTR = test.indexOf(".")+1;
                int endSTR = test.length();
                String decimal = test.substring(startSTR, endSTR);
                condition = (decimal.length() <= 2);
            }
            else{
                condition = true;
            }
            return condition;
        }

        public String formatOutput(BigDecimal output){
            return new DecimalFormat("#,##0.00").format(output);
        }
    
    
    }
    
    public static class Members {

        private final String name, password;
        private BigDecimal money;

        public Members(String name, String password, String money) {
             this.name = name;
             this.password = password;
             this.money = new BigDecimal(money);
        }

        public void setMoney(String amount){
            money = new BigDecimal(amount);
        }

        public String getUsername() {
            return name;
        }

        public String getPassword() {
            return password;
        }

        public BigDecimal getMoney() {
            return money;
        }

    }
    
}