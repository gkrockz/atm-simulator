import java.sql.*;
import java.util.Scanner;

public class UserAccount {

	Connection concobj;
	PreparedStatement st, wst;
	ResultSet rs;
	Scanner usrinp = new Scanner(System.in);
	private int cusid, withAmt, depAmt, pin, count, npin1, npin2;
	private double totamt, bal;
	private boolean status;

	public void getBalance() {
		try {
			connect();
			cusid = fetchUser();
			String Query1 = "select balance from users where id=(?)";
			st = concobj.prepareStatement(Query1);
			st.setInt(1, cusid);
			rs = st.executeQuery();
			while (rs.next()) {
				totamt = rs.getDouble(1);
				System.out.println("Available balance: " + totamt + "Rs");
			}
		} catch (SQLException error) {
			System.out.println("Error occured during fetching balance: " + error);
		} finally {
			disconnect();
		}

	}

	public void deposit() {
		try {
			connect();
			cusid = fetchUser();
			System.out.println("Enter deposit amount: ");
			depAmt = usrinp.nextInt();
			String Query2 = "update users set balance=balance+(?) where id=(?)";
			st = concobj.prepareStatement(Query2);
			st.setDouble(1, depAmt);
			st.setInt(2, cusid);
			count = st.executeUpdate();
			System.out.println(depAmt + " Rs deposited into your account sucessfully");
		} catch (SQLException error) {
			System.out.println("Error occured during deposit : " + error);
		} finally {
			disconnect();
		}

	}

	public void withdraw() throws Exception {
		connect();
		cusid = fetchUser();
		totamt = checkbalance(cusid);
		System.out.println("Enter withdrawal amount : ");
		withAmt = usrinp.nextInt();
		try {
			if (totamt < 1000) {
				throw new MinimumBalanceException("Balance for this account can't go below 1000 Rs");
			} else {
				totamt -= withAmt;
				String Query3 = "update users set balance = (?) where id=(?)";
				wst = concobj.prepareStatement(Query3);
				wst.setDouble(1, totamt);
				wst.setInt(2, cusid);
				count = wst.executeUpdate();
				System.out.println("Total money: " + withAmt + " Rs");
				System.out.println("Balance after withdrawal: " + totamt + " Rs");
			}
		} catch (MinimumBalanceException ex) {
			System.out.println(ex.getMessage());
		} finally {
			disconnect();
		}
	}

	public double checkbalance(int cusid) {
		try {
			connect();
			String Query5 = "select balance from users where id=(?)";
			st = concobj.prepareStatement(Query5);
			st.setInt(1, cusid);
			rs = st.executeQuery();
			while (rs.next()) {
				bal = rs.getDouble(1);
			}
		} catch (SQLException error) {
			System.out.println("Error occured while fetching balance: " + error);
		}
		return bal;
	}

	public boolean validateUser(int CusId) {
		try {
			connect();
			String Query6 = "select id,pin from users where id = (?)";
			st = concobj.prepareStatement(Query6);
			st.setInt(1, CusId);
			rs = st.executeQuery();
			while (rs.next()) {
				if (pin == Integer.parseInt(rs.getString(2)) && CusId == rs.getInt(1)) {
					status = true;
				} else {
					status = false;
				}
			}
		} catch (SQLException error) {
			System.out.println("Error occured while validating user: " + error);
		}
		return status;
	}

	public void changePin() {
		try {
			connect();
			cusid = fetchUser();
			System.out.println("Enter your old pin:");
			pin = usrinp.nextInt();
			String Query7 = "select pin from users where id = (?);";
			st = concobj.prepareStatement(Query7);
			st.setInt(1, cusid);
			rs = st.executeQuery();
			while (rs.next()) {
				if (pin == Integer.parseInt(rs.getString(1))) {
					System.out.println("Enter your new 4-digit pin:");
					npin1 = usrinp.nextInt();
					System.out.println("Re Enter your new pin");
					npin2 = usrinp.nextInt();
					if (npin1 == npin2) {
						setpin(cusid, npin1);
					} else {
						System.out.println("OOPS, pin doesn't match with your previous entry");
					}
				} else {
					System.out.println("Sorry, you have entered incorrect pin");
				}
			}
		} catch (SQLException error) {
			System.out.println("Error occured while updating pin: " + error);
		}
	}

	public void setpin(int cusid, int pin) {
		try {
			String Query8 = "update users set pin = (?) where id=(?)";
			st = concobj.prepareStatement(Query8);
			st.setInt(1, pin);
			st.setInt(2, cusid);
			count = st.executeUpdate();
			System.out.println("ATM pin changed sucessfully :)");

		} catch (SQLException error) {
			System.out.println("Error occured while updating pin: " + error);
		}
	}

	public int fetchUser() {
		try {
			System.out.println("Enter your customer Id: ");
			cusid = usrinp.nextInt();
			System.out.println("Enter your 4-digit pin:");
			pin = usrinp.nextInt();
			if (validateUser(cusid)) {
				String Query4 = "select name from users where id=(?)";
				st = concobj.prepareStatement(Query4);
				st.setInt(1, cusid);
				rs = st.executeQuery();
				while (rs.next()) {
					System.out.println("Welcome " + rs.getString(1) + " :)");
					System.out.println("===============================");
				}
			} else {
				System.out.println("Incorrect user credentials");
				System.exit(count);
			}
		} catch (SQLException error) {
			System.out.println("Error occured while fetching user details: " + error);
		}
		return cusid;
	}

	public void connect() {
		try {
			String url = "jdbc:postgresql://localhost:5432/test";
			String username = "postgres";
			String password = "secret";
			Class.forName("org.postgresql.Driver");
			concobj = DriverManager.getConnection(url, username, password);
		} catch (Exception error) {
			System.out.println("Error occured while establishing connection with DB: " + error);
		}
	}

	public void disconnect() {
		try {
			concobj.close();
			st.close();
		} catch (SQLException error) {
			System.out.println("Error occured while closing the connection with DB: " + error);
		}
	}
}