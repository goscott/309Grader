package administration;

public class dbtest {

	public static void main(String[] args) {
		UserDB db = new UserDB();
		db.addUser(new User("New", "User", "" + System.currentTimeMillis(), "f0rk$@ndKNIVES33", 't'));
		db.print();
	}
}
