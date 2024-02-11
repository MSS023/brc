import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;

public class Trie {
    private Double value = 0.0;
    private HashMap<Character, Trie> map = new HashMap<>();
    private int count = 0;

    public HashMap<Character, Trie> getMap() {
        return this.map;
    }

    public void setMap(Character key, Trie value) {
        this.map.put(key, value);
    }

    public Double getValue() {
        return this.value;
    }

    public void setValue(Double value) {
        this.value = value;
    }

    public int getCount() {
        return this.count;
    }

    public void setCount(int count) {
        this.count = count;
    }

    public void incrementCount() {
        this.count += 1;
    }

    public void incrementCount(int incrementValue) {
        this.count += incrementValue;
    }

    public void insertKeyValue(String key, Double temp, Double count) {
        int len = key.length();
        HashMap<Character, Trie> _map = this.map;
        Trie store = null;
        for (int idx = 0; idx < len; idx += 1) {
            char ch = key.charAt(idx);
            if (!_map.containsKey(ch)) {
                _map.put(ch, new Trie());
            }
            store = _map.get(ch);
            _map = store.getMap();
        }
        store.value += temp;
        store.count += count;
    }

    public HashMap<String, ArrayList<Double>> getCityMetrics() {
        return this.getCityMetrics(this, new String());
    }

    private HashMap<String, ArrayList<Double>> getCityMetrics(Trie store, String prefix) {
        HashMap<String, ArrayList<Double>> result = new HashMap<>();
        if (store.count > 0) {
            ArrayList<Double> metrics = new ArrayList<>();
            metrics.add(store.value);
            metrics.add(Double.valueOf(store.count));
            result.put(prefix, metrics);
        }
        HashMap<Character, Trie> _map = store.getMap();
        Object[] keys = _map.keySet().toArray();
        Arrays.sort(keys);
        for (int ord = 0; ord < keys.length; ord += 1) {
            Object ch = keys[ord];
            Trie temp = _map.get(ch);
            result.putAll(this.getCityMetrics(temp, prefix + ch));
        }
        return result;
    }
}
