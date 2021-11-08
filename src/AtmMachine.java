import java.util.Scanner;

public class AtmMachine 
{
	public static void main(String[] args) {
		UserAccount user = new UserAccount();
		System.out.println("Welcome To Pro.Dev ATM, Choose (1 - 5)");
		System.out.println("1.Balance");
		System.out.println("2.Deposit");
		System.out.println("3.Withdrawal");
		System.out.println("4.Change ATM-Pin");
		System.out.println("5.Exit");
		try (Scanner usrinp = new Scanner(System.in)) {
			short usrch;
			usrch = usrinp.nextShort();
			switch (usrch) {
				case 1 -> acc.getBalance();
				case 2 -> acc.deposit();
				case 3 -> acc.withdraw();
				case 4 -> acc.changePin();
				case 5 -> System.exit(0);
			}
		} catch (Exception error) {
			System.out.println("Please enter a valid selection: (1 - 5)");
		}
	}
}