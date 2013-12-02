import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Timestamp;
import java.util.LinkedList;

public class Controller {
	public static void main(String[] args) {
		java.util.Date date = new java.util.Date();
		try {
			PrintWriter out = new PrintWriter(new BufferedWriter(new FileWriter("invalidMails.txt", true)));
			out.println("");
			out.println("### Protocol of mailing from "+new Timestamp(date.getTime()));
			out.close();
		} catch (IOException e) {
			System.out.println("Could not write to file");
		}

		try {
			PrintWriter out = new PrintWriter(new BufferedWriter(new FileWriter("validMails.txt", true)));
			out.println("");
			out.println("### Protocol of mailing from "+new Timestamp(date.getTime()));
			out.close();
		} catch (IOException e) {
			System.out.println("Could not write to file");
		}

		ExtractExcel mails = new ExtractExcel("/path/to/input.xls");
		prepareToSend(mails.getValidMails());
	}

	private static void prepareToSend(LinkedList<Account> Accounts) {
		String content = null;
		for (Account Account : Accounts) {
			content =
					"<html><head></head><body></body></html>";

			try {
				PrintWriter out = new PrintWriter(new BufferedWriter(new FileWriter("validMails.txt", true)));
				out.println("E-Mail sent to: id: "+Account.getId()+", E-Mail: "+Account.getMail());
				out.close();
			} catch (IOException e) {
				System.out.println("Could not write to file id: "+Account.getId()+", E-Mail: "+Account.getMail());
			}

			new SendMail(Account.getMail(), "SUBJECT", content);
		}
	}
}
