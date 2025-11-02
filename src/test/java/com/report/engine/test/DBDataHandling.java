package com.report.engine.test;

import java.sql.*;
import java.text.SimpleDateFormat;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;

import com.report.engine.services.DREUtiles;

public class DBDataHandling {

	protected static String url = "jdbc:oracle:thin:@localhost:1521:xe";
	protected static String unm = "system";
	protected static String pwd = "system";

	public static String ArrangeDBData(ResultSet DbData) {
		String result = null;
		if (DbData == null) {
			System.out.println("[DBDataHandling] [ArrangeDBData] Recived ResultSet or DB DATA is null .");
			result = "DTNL~DB Data or ResultSet Is Null";
		} else {
			 DREUtiles dut = new DREUtiles();
			try {
				int clmsize = DbData.getMetaData().getColumnCount();
				System.out.println("[DBDataHandling] [ArrangeDBData] Coloums Size is -->  " + clmsize);
				LinkedHashMap<String, List<String>> maindata = new LinkedHashMap<String, List<String>>();
				for (int i = 0; i < clmsize; i++) {
					maindata.put(dut.nvl(DbData.getMetaData().getColumnName(i + 1), "NO_DATA"), new LinkedList<>());
				}
				System.out.println("[DBDataHandling] [ArrangeDBData] Half Filled Main HashMap --> [ " + maindata + " ] .");
				while (DbData.next()) {
					for (int i = 0; i < clmsize; i++) {
						String value = dut.nvl(DbData.getString(i + 1), "NO_DATA");
						String colName = dut.nvl(DbData.getMetaData().getColumnName(i + 1), "NO_DATA");
						maindata.get(colName).add(value);
					}
				}
				result = "DONE~" + maindata.toString();
			} catch (Exception e) {
				result = "FAIL~Unable to handel the DB Data ";
				System.out.println("[DBDataHandling] [ArrangeDBData] Error in Arranging the Data  --> [ " + e + " ] ");
			}
		}
		return result;
	}

	public static String DBDatagetter(String Query) {
		String result = null;

		try (Connection cn = DriverManager.getConnection(url, unm, pwd);
				PreparedStatement ps = cn.prepareStatement(Query);
				ResultSet rs = ps.executeQuery();) {

			String dbdata = ArrangeDBData(rs);
			System.out.println("\n\n DB DATA IS \n\n" + dbdata);
			result = dbdata;
		} catch (Exception e) {
			System.out.println(
					"[DBDataHandling] [DBDatagetter] Error in executing the Query or getting the data -->  " + e);
		}
		return result;
	}

	public static void main(String[] args) {
		String query = "select CARD_PRD as Card_Product,count(*) as Number_Of_Cards from DRE_TEST_CARDS where TRUNC(TO_DATE(MAKER_DTTM)) BETWEEN TRUNC(TO_DATE('05-Jun-25','DD-MON-YY')) AND TRUNC(TO_DATE('05-Nov-25','DD-MON-YY')) group by CARD_PRD";
		DBDatagetter(query);
		SimpleDateFormat sdf = new SimpleDateFormat("dd-MMM-yyyy hh:MM:ss");
		Date d = new Date(System.currentTimeMillis());
		String a =sdf.format(d);
		System.out.println(a);
		String date = new Date(System.currentTimeMillis()).toString();
		System.out.println(date);
	}

}