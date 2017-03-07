package net.sf.javaanpr;

/**
 * Created by Athinodoros on 3/7/2017.
 */

import net.sf.javaanpr.imageanalysis.CarSnapshot;
import net.sf.javaanpr.intelligence.Intelligence;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;
import org.xml.sax.SAXException;

import javax.xml.parsers.ParserConfigurationException;
import java.io.*;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Properties;

import static com.sun.javafx.fxml.expression.Expression.greaterThan;
import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.notNullValue;
import static org.hamcrest.MatcherAssert.assertThat;



@RunWith(Parameterized.class)
public class testNubrerPlates {

    private File platesIn;
    private String expected;
    private CarSnapshot carPlatePic;

    public testNubrerPlates(File plate, String expected) {
        this.platesIn = plate;
        this.expected = expected;
    }

    @Before
    public void init() throws IOException {
        carPlatePic = new CarSnapshot(new FileInputStream(platesIn));
    }

   @Parameterized.Parameters
    public static Collection<Object[]> data() {
        Properties properties = new Properties();
        String snapshotDirPath = "src/test/resources/snapshots";
        String resultsPath = "src/test/resources/results.properties";
        try {
            InputStream resultsStream = new FileInputStream(new File(resultsPath));
            properties.load(resultsStream);
            resultsStream.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        File snapshotDir = new File(snapshotDirPath);
        File[] snapshots = snapshotDir.listFiles();

        Collection<Object[]> dataForOneImage= new ArrayList();
        for (File file : snapshots) {
            String name = file.getName();
            String plateExpected = properties.getProperty(name);
            dataForOneImage.add(new Object[]{file, plateExpected });
        }
        return dataForOneImage;
    }

       @Test
    public void testAllPlate() {
        Intelligence intel;
        try {
            intel = new Intelligence();
            String plateCorrect = expected;
            String numberPlate = intel.recognize(carPlatePic, false);
            System.out.println("numberPlate = " + numberPlate);
            assertThat(numberPlate, is(equalTo(plateCorrect)));
        } catch (ParserConfigurationException e) {
            e.printStackTrace();
        } catch (SAXException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

}
