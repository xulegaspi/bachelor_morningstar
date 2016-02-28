/**
 * Created by Xurxo on 27/02/2016.
 * First WebScraper to Extract data from MorningStar.se
 */

import com.gargoylesoftware.htmlunit.FailingHttpStatusCodeException;
import com.gargoylesoftware.htmlunit.WebClient;
import com.gargoylesoftware.htmlunit.html.*;

import java.io.IOException;
import java.net.MalformedURLException;
import java.util.List;

public class WebScrapper {

    /**
     * @throws IOException
     * @throws MalformedURLException
     * @throws FailingHttpStatusCodeException
     */
    public static void main(String[] args) throws FailingHttpStatusCodeException, IOException {

        // Open the WebClient
        WebClient webClient = new WebClient();

        // Retrieve the whole web page given the URL
        HtmlPage page = webClient.getPage("http://www.morningstar.se/Funds/Quickrank.aspx?cb=on");

        Boolean stop = false;

        // Loop to iterate until there's no "next page". Using XPath to select the "next page" button
        while (page.getByXPath("/html/body/form/div[4]/div[4]/div[2]/div[9]/table/tfoot/tr/td/a[15]") != null || !stop) {

            // Get the table where the data is by Id
            HtmlTable table = page.getHtmlElementById("ctl01_cphContent_Quickrank1_ctl00");

            // Get the body of the table
            for (HtmlTableBody body : table.getBodies()) {
                List<HtmlTableRow> rows = body.getRows();

                System.out.println("NUMBER OF ROWS: " + rows.size());
                if (rows.size() < 20) {
                    stop = true;
                }

                // Loop to iterate through all the rows in each page
                for (HtmlTableRow row : rows) {

                    // Extract the desired data
                    System.out.println(row.getCell(2).getTextContent() + " || " +   // cell(2) = Namn
                            row.getCell(4).getTextContent() + " || " +              // cell(4) = i år %
                            row.getCell(5).getTextContent() + " || " +              // cell(5) = 5 år %
                            row.getCell(6).getTextContent() + " || " +              // cell(6) = Std. avvik 3 år
                            row.getCell(7).getTextContent() + " || " +              // cell(7) = Förm milj SEK
                            row.getCell(8).getTextContent());                       // cell(8) = Norman-belopp

                    System.out.println("==========================================================");
                }

            }

            // Select the button "next"
            HtmlAnchor next = (HtmlAnchor) page.getByXPath("/html/body/form/div[4]/div[4]/div[2]/div[9]/table/tfoot/tr/td/a[25]").get(0);

            // Click to get the next page
            page = next.click();

        }

        // Close the WebClient
        webClient.close();

    }

}
