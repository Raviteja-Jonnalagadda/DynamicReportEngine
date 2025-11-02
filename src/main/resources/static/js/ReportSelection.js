const jsn = "[ReportSelection] ";

$(document).ready(function () {
    console.log(jsn + "Document Ready");

    $('#rpt_sub_btn').off('click').on('click', function () {
        console.log(jsn + "Generate Report Clicked");

        const rptnm = $("#rpt_name").val().trim();
        const rpfrd = $("#rpt_frdate").val().trim();
        const rptod = $("#rpt_todate").val().trim();

        if (!rptnm || !rpfrd || !rptod) {
            alert("Please enter all values to generate the report.");
            return;
        }

        console.log(jsn + "Submitting AJAX request to /RPT_GEN_INFO");
        $('#rpt_sub_btn').prop('disabled', true);

        $.ajax({
            url: window.location.origin + '/RPT_GEN_INFO',
            type: 'POST',
            data: {
                rpt_name: rptnm,
                rpt_frdate: rpfrd,
                rpt_todate: rptod
            },
            success: function (response) {
                console.log(jsn + "Response received: " + response);

                if (response.startsWith("FAIL~")) {
                    const errMsg = response.substring(5).trim();
                    alert("Error: " + errMsg);
                    $('#rpt_sub_btn').prop('disabled', false);
                    return;
                }

                if (response.startsWith("DONE~")) {
                    const reportData = response.substring(5).trim();
                    sessionStorage.setItem("dre_report_data", reportData);
                    sessionStorage.setItem("dre_report_name", rptnm);

                    console.log(jsn + "Report data stored in sessionStorage.");
                    window.location.href = "DRE_main.html";
                } else {
                    alert("Unexpected response format: " + response);
                    $('#rpt_sub_btn').prop('disabled', false);
                }
            },
            error: function (xhr, status, error) {
                console.error(jsn + "AJAX Error:", status, error);
                alert("Failed to generate report: " + error);
                $('#rpt_sub_btn').prop('disabled', false);
            }
        });
    });
});
