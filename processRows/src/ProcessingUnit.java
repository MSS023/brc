import java.io.BufferedWriter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map.Entry;

public class ProcessingUnit extends Thread {
    private ArrayList<String> textData;
    private HashMap<String, ArrayList<Double>> processedCityData;
    private Double totalProcessedAvg = 0.0;
    private BufferedWriter fout;

    public ProcessingUnit(ArrayList<String> textData, BufferedWriter fout) {
        this.textData = textData;
        this.fout = fout;
    }

    private void processData() {
        Trie store = new Trie();
        int len = this.textData.size();
        for (int idx = 0; idx < len; idx += 1) {
            String[] splitData = this.textData.get(idx).split(";");
            store.insertKeyValue(splitData[0], Double.parseDouble(splitData[1]), Double.parseDouble(splitData[2]));
        }
        this.processedCityData = store.getCityMetrics();
    }

    public Double getLastTotalAvg() {
        return this.totalProcessedAvg;
    }

    public HashMap<String, ArrayList<Double>> getCityMetrics() {
        return this.processedCityData;
    }

    public String getString(Entry<String, Double> entry) {
        String result = entry.getKey() + ";" + entry.getValue();
        return result;
    }

    public synchronized void printCityAvgs() {
        HashMap<String, ArrayList<Double>> data = this.processedCityData;
        ArrayList<String> csv = new ArrayList<>();
        for (Entry<String, ArrayList<Double>> entry : data.entrySet()) {
            ArrayList<Double> value = entry.getValue();
            csv.add(entry.getKey() + ";" + value.get(0) + ";" + value.get(1));
        }
        try {
            for (int idx = 0; idx < csv.size(); idx += 1) {
                this.fout.append(csv.get(idx) + '\n');
            }
        } catch (Exception e) {
            System.out.println("Error in ProcessingUnit: printCityAvgs " + e);
        }
    }

    private void clearGarbage() {
        this.processedCityData = null;
        this.textData = null;
        this.totalProcessedAvg = null;
    }

    public void run() {
        try {
            System.out.println("The processor is running");
            this.processData();
            this.printCityAvgs();
            this.fout.flush();
            this.clearGarbage();
        } catch (Exception e) {
            System.out.println("Error in ProcessingUnit: run " + e);
        }
    }
}
