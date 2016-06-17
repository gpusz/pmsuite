package pl.olart.pmsuite.util;

import org.supercsv.cellprocessor.ParseDate;
import org.supercsv.cellprocessor.ParseDouble;
import org.supercsv.cellprocessor.constraint.NotNull;
import org.supercsv.cellprocessor.ift.CellProcessor;
import org.supercsv.io.CsvBeanReader;
import org.supercsv.io.ICsvBeanReader;
import org.supercsv.prefs.CsvPreference;
import pl.olart.pmsuite.model.RozliczenieRFPBean;

import java.io.*;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/**
 * User: grp
 * Date: 16.06.16
 * Time: 00:14
 */
public class ParserCSV {
    public static List<RozliczenieRFPBean> parsujCSV(InputStream file) throws IOException {
        ICsvBeanReader beanReader = null;
        InputStreamReader isr = new InputStreamReader(file);
        ByteArrayInputStream stream = ustawNaglowek(isr);
        List<RozliczenieRFPBean> list = new ArrayList<RozliczenieRFPBean>();
        try {
            beanReader = new CsvBeanReader(new InputStreamReader(stream), CsvPreference.STANDARD_PREFERENCE);

            // the header elements are used to map the values to the bean (names must match)
            final String[] header = beanReader.getHeader(true);
            final CellProcessor[] processors = getProcessors();


            RozliczenieRFPBean rozliczenie;
            while( (rozliczenie = beanReader.read(RozliczenieRFPBean.class, header, processors)) != null ) {
                list.add(rozliczenie);
            }
        }
        finally {
            if( beanReader != null ) {
                beanReader.close();
            }
        }
        return list;
    }

    private static CellProcessor[] getProcessors() {


        final CellProcessor[] processors = new CellProcessor[] {
                new ParseDate("yyyy-MM-dd"), // Data dostawy/wykonania
                new NotNull(), // Typ elementu kosztu
                new NotNull(), // Wygenerowany przez
                new NotNull(), // Identyfikator zewnętrzny
                new NotNull(), // Fundusz
                new NotNull(), // Konto analityczne
                new NotNull(new ParseDouble()), // kwota netto
                new NotNull(new ParseDouble()), // kwota vat
                new NotNull(new ParseDouble()), // kwota brutto
                new NotNull(), // Opis
                new ParseDate("yyyy-MM-dd") // data zaplaty
        };

        return processors;
    }
//
//    dataDostawy;
//    typElementuKosztu;
//    wygenerowanyPrzez;
//    identyfikatorZewnetrzny;
//    fundusz;
//    kontoAnalityczne;
//    kwotaNetto;
//    kwotaVat;
//    kwotaBrutto;
//    opis;
//    dataZaplaty;

    public static ByteArrayInputStream ustawNaglowek(InputStreamReader isr) {
        try {
            // input the file content to the String "input"
            BufferedReader file = new BufferedReader(isr);
            String line;String input = "";

            boolean pierwszaLinia = true;

            while ((line = file.readLine()) != null) {
                if(pierwszaLinia) {
                    line = "dataDostawy,typElementuKosztu,wygenerowanyPrzez,identyfikatorZewnetrzny,fundusz,kontoAnalityczne,kwotaNetto,kwotaVat,kwotaBrutto,opis,dataZaplaty";
                    pierwszaLinia = false;
                }
                input += line + '\n';
            }

            file.close();

            System.out.println(input); // check that it's inputted right

            // write the new String with the replaced line OVER the same file
            ByteArrayOutputStream out = new ByteArrayOutputStream();
            out.write(input.getBytes());
            out.close();

            return new ByteArrayInputStream(out.toByteArray());

        } catch (Exception e) {
            System.out.println("Problem reading file.");
            e.printStackTrace();
        }
        return null;
    }
}
