import java.io.BufferedWriter;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.OutputStreamWriter;
import java.util.ArrayList;
import java.util.Scanner;

public class App {

    public static void main(String[] args) throws Exception {
        String fileName = "C:\\Users\\manan\\OneDrive\\Desktop\\Practice Projects\\brc\\measurements.txt";
        String intermediateDataFileName = "C:\\Users\\manan\\OneDrive\\Desktop\\Practice Projects\\brc\\intermediate.txt";
        String outFileName = "C:\\Users\\manan\\OneDrive\\Desktop\\Practice Projects\\brc\\out.txt";
        int batchSize = 10000000;
        try {
            if (args.length < 3) {
                batchSize = Integer.parseInt(args[2]);
            }
        } catch (Exception e) {
            System.out.println("Error in App.java: main " + e);
        }
        Scanner scan = new Scanner(new FileInputStream(fileName), "UTF-8");
        BufferedWriter fInter = new BufferedWriter(
                new OutputStreamWriter(new FileOutputStream(intermediateDataFileName), "UTF-8"));
        fInter.flush();
        fInter.write("");
        ArrayList<String> dataQueue = new ArrayList<>();
        ArrayList<ProcessingUnit> units = new ArrayList<>();
        FinalProcessing fUnit = new FinalProcessing(units, intermediateDataFileName,
                outFileName, fInter);
        while (scan.hasNextLine()) {
            dataQueue.add(scan.nextLine() + ";1");
            if (dataQueue.size() == batchSize) {
                ProcessingUnit unit = new ProcessingUnit(dataQueue, fInter);
                units.add(unit);
                unit.start();
                dataQueue = new ArrayList<>();
            }
        }
        if (dataQueue.size() > 0) {
            ProcessingUnit unit = new ProcessingUnit(dataQueue, fInter);
            units.add(unit);
            unit.start();
        }
        fUnit.start();
        scan.close();
    }
}
