package com.report.engine.services;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import com.report.engine.DTO.DREDTO;

@Component
public class DBCommunicater {
	@Value("${DRE.RPT.QUERY}")
	public String query;
	@Autowired
	public DREDTO dto;
	@Autowired
	public DREUtiles dut;

	public String getRptQuery(String Rpt_name) {
		String result = null;
		if (!dut.cvl(Rpt_name)) {
			System.out.println(
					"[DBCommunicater] [getRptQuery] Recived Null Paramater as Report Name -->  [ " + Rpt_name + " ]");
			result = "RPNNUL~Cant Process Null Pavlue";
		}
		Connection c = null;
		PreparedStatement ps = null;
		ResultSet rs = null;
		System.out.println("[DBCommunicater] [getRptQuery] Report Name Recived is -->  [ " + Rpt_name + " ]");
		System.out.println("[DBCommunicater] [getRptQuery] Report query is -->  [ " + query + " ]");
		System.out.println(
				"[DBCommunicater] [getRptQuery]  " + dto.getDburl() + " " + dto.getDbunm() + " " + dto.getDbpwd());
		try {
			c = DriverManager.getConnection(dto.getDburl(), dto.getDbunm(), dto.getDbpwd());
			ps = c.prepareStatement(query);
			ps.setString(1, Rpt_name);
			rs = ps.executeQuery();
			if (rs!=null) {
				while (rs.next()) {
					result = dut.nvl(rs.getString("RPT_QUERY"), "QRYNUL~Query Is Null");
					System.out.println(
							"[DBCommunicater] [getRptQuery] Report Query Extracted is -->  [ " + result + " ]");
				}
				close(c, ps, rs);
			} else {
				close(c, ps, rs);
				System.out.println("[DBCommunicater] [getRptQuery] No Report Record Found With the Report Name [ "
						+ Rpt_name + " ]");
				result = "NOPRFD~No Report Record Found With the Report Name [ " + Rpt_name + " ]";
			}
		} catch (Exception e) {
			close(c, ps, rs);
			System.out.println("[DBCommunicater] [getRptQuery] Error While Getting the Report Query for the [ "
					+ Rpt_name + " ] Error is [ " + e.toString() + " ]");
			result = "EXGRPQ~Error While Getting the Report Query for the [ " + Rpt_name + " ] Error is [ "
					+ e.toString() + " ]";
		}
		System.out.println("[DBCommunicater] [getRptQuery] Final Result is --> [ " + result + " ]");
		return result;
	}

	public String getRptData(String RptNm, String Rpquery, String Frmdt, String todat) {
		String result = null;
		Connection c = null;
		PreparedStatement ps = null;
		ResultSet rs = null;
		System.out.println("[DBCommunicater] [getRptData] The Final Arranged Report Data is -->  [ " + RptNm + " "+Rpquery+" "+Frmdt+" "+todat+" ] .");
		try {
			c = DriverManager.getConnection(dto.getDburl(), dto.getDbunm(), dto.getDbpwd());
			ps = c.prepareStatement(Rpquery);
			ps.setString(1, Frmdt);
			ps.setString(2, todat);
			rs = ps.executeQuery();
			String rawdata = ArrangeDBData(rs);
			System.out.println(
					"[DBCommunicater] [getRptData] The Final Arranged Report Data is -->  [ " + rawdata + " ] .");
			close(c, ps, rs);
			result = "DONE~" + rawdata;
		} catch (Exception e) {
			close(c, ps, rs);
			result = "FAIL~Error While Getting the Report Data";
			System.out
					.println("[DBCommunicater] [getRptData] Error While Gettig the Data --> [ " + e.toString() + " ]");
		}
		close(c, ps, rs);

		return result;
	}

	public String ArrangeDBData(ResultSet DbData) {
		String result = null;
		if (DbData == null) {
			System.out.println("[DBDataHandling] [ArrangeDBData] Recived ResultSet or DB DATA is null .");
			result = "DTNL~DB Data or ResultSet Is Null";
		} else {
			try {
				int clmsize = DbData.getMetaData().getColumnCount();
				LinkedHashMap<String, List<String>> maindata = new LinkedHashMap<String, List<String>>();
				for (int i = 0; i < clmsize; i++) {
					maindata.put(dut.nvl(DbData.getMetaData().getColumnName(i + 1), "NO_DATA"), new LinkedList<>());
				}
				while (DbData.next()) {
					for (int i = 0; i < clmsize; i++) {
						String value = dut.nvl(DbData.getString(i + 1), "NO_DATA");
						String colName = dut.nvl(DbData.getMetaData().getColumnName(i + 1), "NO_DATA");
						maindata.get(colName).add(value);
					}
				}
				result = maindata.toString();
			} catch (Exception e) {
				result = "FAIL~Unable to handel the DB Data ";
				System.out.println("[DBDataHandling] [ArrangeDBData] Error in Arranging the Data  --> [ " + e + " ] ");
			}
		}
		return result;
	}

	public void close(AutoCloseable... resources) {
		for (AutoCloseable resource : resources) {
			if (resource != null) {
				try {
					resource.close();
				} catch (Exception e) {
					System.out.println("[DBCommunicater] [close] Error While Closing the recource [ " + resource
							+ " ] Error is -->  [ " + e.toString() + " ]");
				}
			}
		}
	}

	public static void main(String[] args) {

	}

}
