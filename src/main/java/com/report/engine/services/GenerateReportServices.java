package com.report.engine.services;

import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class GenerateReportServices {

	protected String rptname = null;
	protected String rpt_frmdate = null;
	protected String rpt_todate = null;

	@Autowired
	private DBCommunicater dbc;
	@Autowired
	public DREUtiles dut;

	public String GenerateDateRangeReport(String rtpnm, String ftdt, String todt) {
		String result = null;
		System.out.println("[GenerateReportServices] [GenerateDateRangeReport] " + rtpnm + " " + ftdt + " " + todt);
		if (!dut.cvl(rtpnm) || !dut.cvl(ftdt) || !dut.cvl(todt)) {
			result = "NULVAL~Some of the fields are missing . Cant Process Null Values";
			System.out.println("[GenerateReportServices] [GenerateDateRangeReport] Extracted Query is null -->  [ "
					+ result + " ]");
		}
		this.rptname = rtpnm;
		this.rpt_frmdate = ftdt;
		this.rpt_todate = todt;
		System.out.println("[GenerateReportServices] [GenerateDateRangeReport] globale variables  " + rptname + " "
				+ ftdt + " " + todt);
		String rpt_query = dbc.getRptQuery(rptname);
		System.out.println("[GenerateReportServices] [GenerateDateRangeReport] rpt_query  -->   " + rpt_query);
		switch (rpt_query.substring(0, 6)) {
		case "QRYNUL":
			System.out.println("[GenerateReportServices] [GenerateDateRangeReport] Extracted Query is null -->  [ "
					+ rpt_query.substring(6) + " ]");
			result = rpt_query;
			break;
		case "NOPRFD":
			System.out.println(
					"[GenerateReportServices] [GenerateDateRangeReport] No Report Record Found With the Report Name [ "
							+ rpt_query.substring(6) + " ]");
			result = rpt_query;
			break;
		case "EXGRPQ":
			System.out.println(
					"[GenerateReportServices] [GenerateDateRangeReport] Error While Getting the Report Query for the [ "
							+ rpt_query.substring(6) + " ] ");
			result = rpt_query;
			break;
		case "RPNNUL":
			System.out.println(
					"[GenerateReportServices] [GenerateDateRangeReport] Null Report Name is send to the Get Query [ "
							+ rpt_query.substring(6) + " ] ");
			result = rpt_query;
			break;
		case "SELECT":
			System.out.println("[GenerateReportServices] [GenerateDateRangeReport] Report Data Query Extracted is --> [ "+ rpt_query + " ] .");
			String val = dbc.getRptData(rptname,rpt_query, dut.dateconverter(ftdt), dut.dateconverter(todt)).toString();
			System.out.println("[GenerateReportServices] [GenerateDateRangeReport] Retreved Data from the DB is  --> [ "+ val + " ] .");
			if (val.substring(0, 4).equalsIgnoreCase("FAIL")) {
				System.out.println("[GenerateReportServices] [GenerateDateRangeReport] Error while Getting the Report Data --> [ "+ val + " ] .");
				result = "RPGDER~" + val.substring(5);
			} else {
				String data = dut.nvl(val.substring(5), "NO_DATA");
				System.out.println("[GenerateReportServices] [GenerateDateRangeReport] final json data  "+data);
				JSONObject finalrptdata = new JSONObject();
				finalrptdata.put("SIGN", "DONE");
				finalrptdata.put("Processed_By", "DRE [Dynamic Report Engine Application]");
				finalrptdata.put("TimeStamp", dut.getTimeStamp());
				finalrptdata.put("RPT_DATA",  data);
				finalrptdata.put("RPT_NAME", rptname);
				finalrptdata.put("RPT_RANG", rpt_frmdate + " - " + rpt_todate);
				System.out.println("[GenerateReportServices] [GenerateDateRangeReport] The Final JSON Arranged Report Data is -->  [ "+ finalrptdata + " ] .");
				result =  "DONE~"+finalrptdata.toString();
			}
			break;
		default:
			System.out.println(
					"[GenerateReportServices] [GenerateDateRangeReport] Unexpected Issue . Unexpected Query Type --> [ "
							+ rpt_query.substring(0, 5) + " ] .[ Full Response ] [ " + rpt_query + " ] .");
			result = "UNEISU~Unexpected Issue While Generating the report";
			break;
		}
		return result;
	}

	public static void main(String[] args) {

	}

}

