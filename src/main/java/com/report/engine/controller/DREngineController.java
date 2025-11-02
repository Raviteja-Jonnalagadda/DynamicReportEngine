package com.report.engine.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.report.engine.services.DREUtiles;
import com.report.engine.services.GenerateReportServices;

@Controller
public class DREngineController {

	@GetMapping("/DRE")
	public String DynamicReportController() {
		String result = null;
		System.out.println(
				"[DREngineController] [DynamicReportController] Request Recived to load the ReportSelection.html ");
		result = "redirect:/html/ReportSelection.html";
		return result;
	}

	@Autowired private GenerateReportServices grs; 
	@Autowired private DREUtiles dut;
	@PostMapping("/RPT_GEN_INFO")
	@ResponseBody
	public ResponseEntity<String> reportGenerateReceiver(@RequestParam("rpt_name") String rptnm,
			@RequestParam("rpt_frdate") String rptFrDt, @RequestParam("rpt_todate") String rptToDt) {

		System.out.println("[DREngineController] [RPT_GEN_INFO] Request Received");
		String result = "";

		if (!dut.cvl(rptnm) || !dut.cvl(rptFrDt) || !dut.cvl(rptToDt)) {
			result = "FAIL~Invalid input parameters.";
			return ResponseEntity.badRequest().body(result);
		}

		try {
			result = grs.GenerateDateRangeReport(rptnm, rptFrDt, rptToDt);
			System.out.println("[DREngineController] Report Result: " + result);

			// Always return as plain text (AJAX will handle redirection)
			return ResponseEntity.ok(result);

		} catch (Exception e) {
			System.out.println("[DREngineController] Exception while generating report: " + e);
			result = "FAIL~Error generating report: " + e.getMessage();
			return ResponseEntity.internalServerError().body(result);
		}
	}

}
