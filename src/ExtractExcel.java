import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.poifs.filesystem.POIFSFileSystem;
import org.apache.poi.ss.usermodel.Cell;

import java.io.*;
import java.util.LinkedList;

public class ExtractExcel {
	private String file;
	private LinkedList<Account> validMails = new LinkedList<Account>();
	private LinkedList<Account> invalidMails = new LinkedList<Account>();

	public ExtractExcel(String file) {
		this.file = file;
		readExcel();
	}

	private void readExcel() {
		try {
			int rows; // No of rows
			int cols = 0; // No of columns
			POIFSFileSystem fs = new POIFSFileSystem(new FileInputStream(file));
			HSSFWorkbook wb = new HSSFWorkbook(fs);
			HSSFSheet sheet = wb.getSheetAt(0);
			HSSFRow row;
			HSSFRow idRow;
			HSSFRow salutationRow;
			HSSFCell cell;

			String id = "0";
			String salutation = "";

			rows = sheet.getPhysicalNumberOfRows();
			cols = countCols(sheet, rows);

			int mailIndex = findMailsInSheet(sheet, rows, cols);

			for (int r = 0; r < rows; r++) {
				row = sheet.getRow(r);
				if (row != null) {
					cell = row.getCell(mailIndex);
					if (cell != null && !(cell.toString().equalsIgnoreCase("e-mail") || cell.toString().equalsIgnoreCase("email") || cell.toString().equalsIgnoreCase("mail"))) {

						idRow = sheet.getRow(r);
						id = idRow.getCell(mailIndex-2).toString();

						// Salutation must be placed one field left of mailIndex
						salutationRow = sheet.getRow(r);
						salutation = salutationRow.getCell(mailIndex-1).toString();

						checkMail(id, salutation, cell.toString());
					}
				}
			}
		} catch(Exception ioe) {
			ioe.printStackTrace();
		}
	}

	/**
	 * Find current row with mails
	 */
	private int findMailsInSheet(HSSFSheet sheet, int rows, int cols) {
		String colName;
		HSSFCell cell;
		HSSFRow row;

		for(int r = 0; r < rows; r++) {
			row = sheet.getRow(r);
			if(row != null) {
				for(int c = 0; c < cols; c++) {
					cell = row.getCell(c);
					if (cell != null) {
						cell.setCellType(Cell.CELL_TYPE_STRING);
						colName = cell.getRichStringCellValue().toString();
						if (colName.equalsIgnoreCase("e-mail") || colName.equalsIgnoreCase("email") || colName.equalsIgnoreCase("mail")) {
							return c;
						}
					}
				}
			}
		}
		return -1;
	}

	// This trick ensures that we get the data properly even if it doesn't start from first few rows
	private int countCols(HSSFSheet sheet, int rows) {
		HSSFRow row;
		int tmp;
		int cols = 0;

		for(int i = 0; i < 10 || i < rows; i++) {
			row = sheet.getRow(i);
			if(row != null) {
				tmp = sheet.getRow(i).getPhysicalNumberOfCells();
				if(tmp > cols) cols = tmp;
			}
		}
		return cols;
	}

	private void checkMail(String id, String salutation, String mail) {
		if (MailValidator.evaluate(mail)) {
			validMails.add(new Account(id, salutation, mail));
		} else if (id.length() != 0 && salutation.length() != 0 && mail.length() != 0) {
			try {
				PrintWriter out = new PrintWriter(new BufferedWriter(new FileWriter("invalidMails.txt", true)));
				out.println("Ungültiger Eintrag: id: "+id+", E-Mail: "+mail);
				out.close();
			} catch (IOException e) {
				System.out.println("Could not write to file: Ungültiger Eintrag: id: "+id+", E-Mail: "+mail);
			}
		}
	}

	public LinkedList<Account> getValidMails() {
		return validMails;
	}

	public LinkedList<Account> getInvalidMails() {
		return invalidMails;
	}
}
