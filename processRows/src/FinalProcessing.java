import java.io.BufferedWriter;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.OutputStreamWriter;
import java.util.ArrayList;
import java.util.Scanner;

public class FinalProcessing extends Thread {
    private ArrayList<ProcessingUnit> threads;
    private String intermediateFileName;
    private String outputFileName;

    public FinalProcessing(ArrayList<ProcessingUnit> threads, String intermediateFileName, String outputFileName,
            BufferedWriter fInter) {
        this.threads = threads;
        this.intermediateFileName = intermediateFileName;
        this.outputFileName = outputFileName;
    }

    private void doProcessing() {
        try {
            Scanner fin = new Scanner(new FileInputStream(this.intermediateFileName), "UTF-8");
            BufferedWriter fout = new BufferedWriter(
                    new OutputStreamWriter(new FileOutputStream(this.outputFileName), "UTF-8"));
            ArrayList<String> data = new ArrayList<>();
            while (fin.hasNextLine()) {
                data.add(fin.nextLine());
            }
            fin.close();
            ProcessingUnit unit = new ProcessingUnit(data, fout);
            unit.start();
        } catch (Exception e) {
            System.out.println("Error in Final Processing: doProcessing" + e);
        }
    }

    public void run() {
        try {
            ArrayList<ProcessingUnit> units = this.threads;

            boolean isSomeAlive = true;

            while (isSomeAlive) {
                if (isSomeAlive) {
                    System.out.println("Waiting for intermediate file to complete");
                }

                if (units.size() == 0) {
                    Thread.sleep(5000);
                    continue;
                }

                int len = units.size();

                isSomeAlive = false;

                for (int idx = 0; idx < len; idx += 1) {
                    if (units.get(idx).isAlive()) {
                        isSomeAlive = true;
                        break;
                    }
                }
                Thread.sleep(5000);
            }
            doProcessing();
        } catch (Exception e) {
            System.out.println("Error in Final Processing " + e);
        }
    }
}
