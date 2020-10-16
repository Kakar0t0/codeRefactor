
/** ***************************************************
 * File:  Record.java
 *
 *
 * Copyright (c) 2002-2003 Advanced Applications Total Applications Works.
 * (AATAW)  All Rights Reserved.
 *
 * AATAW grants you ("Licensee") a non-exclusive, royalty free, license to use,
 * modify and redistribute this software in source and binary code form,
 * provided that i) this copyright notice and license appear on all copies of
 * the software; and ii) Licensee does not utilize the software in a manner
 * which is disparaging to AATAW.
 *
 * This software is provided "AS IS," without a warranty of any kind. ALL
 * EXPRESS OR IMPLIED CONDITIONS, REPRESENTATIONS AND WARRANTIES, INCLUDING ANY
 * IMPLIED WARRANTY OF MERCHANTABILITY, FITNESS FOR A PARTICULAR PURPOSE OR
 * NON-INFRINGEMENT, ARE HEREBY EXCLUDED. AATAW AND ITS LICENSORS SHALL NOT BE
 * LIABLE FOR ANY DAMAGES SUFFERED BY LICENSEE AS A RESULT OF USING, MODIFYING
 * OR DISTRIBUTING THE SOFTWARE OR ITS DERIVATIVES. IN NO EVENT WILL AATAW OR ITS
 * LICENSORS BE LIABLE FOR ANY LOST REVENUE, PROFIT OR DATA, OR FOR DIRECT,
 * INDIRECT, SPECIAL, CONSEQUENTIAL, INCIDENTAL OR PUNITIVE DAMAGES, HOWEVER
 * CAUSED AND REGARDLESS OF THE THEORY OF LIABILITY, ARISING OUT OF THE USE OF
 * OR INABILITY TO USE SOFTWARE, EVEN IF SUN HAS BEEN ADVISED OF THE
 * POSSIBILITY OF SUCH DAMAGES.
 *
 * This software is not designed or intended for use in on-line control of
 * aircraft, air traffic, aircraft navigation or aircraft communications; or in
 * the design, construction, operation or maintenance of any nuclear
 * facility. Licensee represents and warrants that it will not use or
 * redistribute the Software for such purposes.
 ***************************************************   */

import java.io.IOException;
import java.io.RandomAccessFile;
import java.util.StringTokenizer;

public class Record {
	private int recID;
	private int quantity;
	private String toolType = "";
	private String brandName = "";
	private String toolDesc = "";
	private String partNum = "";
	private String cost = "";
	private String recordTokens[];
	private boolean myDebug = false;
	private long filePos;
	private long fileLen;

	/**
	 * @param file
	 * @throws IOException
	 */
	public void ReadRec(RandomAccessFile file) throws IOException {
		char f[] = new char[585], ch;
		StringTokenizer tokens;
		String str = "", str2 = "";
		StringBuffer buf1 = new StringBuffer("");
		int ii = 0, loopCtl = 585, len = 0;
		long remm = fileLen - filePos;

		sysPrint("ReadRec() 1a: Remaining bytes is " + remm);

		sysPrint("ReadRec() 1b: Reading ints");

		recID = file.readInt();
		sysPrint("ReadRec() 1c: recID  is " + recID);

		quantity = file.readInt();

		sysPrint("ReadRec() 2: Reading string");

		while (ii < loopCtl) {
			ch = file.readChar();
			str = str + ch;
			len = str.length();

			if (ii > 4) {
				str2 = str.substring(len - 4, len - 1);
				if (str2.equals(";;;"))
					break;
			}

			ii++;
		}

		sysPrint("ReadRec() 3a: str is " + str);

		sysPrint("ReadRec() 3b: Reading string. ii =s " + ii);

		sysPrint("ReadRec() 4: The value of str is " + str);

		tokens = new StringTokenizer(str, ";;");

		recordTokens = new String[7];

		if (tokens.countTokens() >= 4) {
			sysPrint("ReadRec() 5: The number of tokens is " + tokens.countTokens());
			ii = 2;

			while (ii < 7) {
				recordTokens[ii] = tokens.nextToken().toString();
				ii++;
			}

			toolType = new String(recordTokens[2]);
			brandName = new String(recordTokens[3]);
			partNum = new String(recordTokens[4]);
			cost = new String(recordTokens[5]);
			toolDesc = new String(recordTokens[6]);
		} else {
			sysPrint("ReadRec() 6: There are no records to read.");
		}
	}

	/**
	 * @param str
	 * @param buf
	 * @param len
	 * @return
	 */
	public StringBuffer fill(String str, StringBuffer buf, int len) {
		String strTwo = new String("                     " + "                                             ");

		if (str != null) {
			buf.setLength(len);
			buf = new StringBuffer(str + strTwo);
		} else
			buf = new StringBuffer(strTwo);

		if (len == 0) {
			buf.setLength(45);
		} else {
			buf.setLength(len);
		}

		return buf;
	}

	/**
	 * @param file
	 * @throws IOException
	 */
	public void write(RandomAccessFile file) throws IOException {
		StringBuffer buf = new StringBuffer(" ");
		String str = "", str2 = "";

		file.writeInt(recID);

		file.writeInt(quantity);

		str = str + toolType + ";;";

		str = str + brandName + ";;";
		str = str + partNum + ";;";
		str = str + cost + ";;";

		str = str + toolDesc + ";;;";

		buf = fill(str, buf.delete(0, 451), 451);

		file.writeChars(buf.toString());
		sysPrint("write(): - The value of recID is " + recID);
		sysPrint("write(): - The value of quantity is " + quantity);
		sysPrint("write(): - The value of str2 is " + str + " with a length of " + str2.length());
		sysPrint("write(): - The length of buf is " + buf.length());

	}

	/**
	 * @param file
	 * @param a
	 * @throws IOException
	 */
	public void writeInteger(RandomAccessFile file, int a) throws IOException {

		file.writeInt(a);
	}

	public int getRecID() {
		return recID;
	}

	public String getToolType() {
		return toolType.trim();
	}

	public String getToolDesc() {
		return toolDesc.trim();
	}

	public String getPartNumber() {
		return partNum.trim();
	}

	public int getQuantity() {
		return quantity;
	}

	public String getBrandName() {
		return brandName.trim();
	}

	public String getCost() {
		return cost.trim();
	}

	public void setToolType(String f) {
		toolType = f;
	}

	public void setRecID(int p) {
		recID = p;
	}

	public void setCost(String f) {
		cost = f;
	}

	public void setBrandName(String f) {
		brandName = f;
	}

	public void setToolDesc(String f) {
		toolDesc = f;
	}

	public void setPartNumber(String f) {
		partNum = f;
	}

	public void setQuantity(int q) {
		quantity = q;
	}

	public void setFilePos(long fp) {
		filePos = fp;
	}

	public void setFileLen(long fl) {
		fileLen = fl;
	}

	public void sysPrint(String str) {
		if (myDebug) {
			System.out.println(str);
		}
	}

	public static int getSize() {
		return 585;
	}
}
