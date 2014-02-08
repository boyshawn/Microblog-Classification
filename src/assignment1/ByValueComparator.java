package assignment1;

import java.util.Comparator;
import java.util.Map;

/**
 * @author Jaewook Kim
 * 
 */
public class ByValueComparator implements Comparator<String> {
        Map<String, Integer> base_map;

        public ByValueComparator(Map<String, Integer> base_map) {
                this.base_map = base_map;
        }

        @Override
        public int compare(String arg0, String arg1) {
                if (!base_map.containsKey(arg0) || !base_map.containsKey(arg1)) {
                        return 0;
                }

                if (base_map.get(arg0).intValue() <= base_map.get(arg1).intValue()) {
                        return -1;
                        // } else if (base_map.get(arg0).intValue() ==
                        // base_map.get(arg1).intValue()) {
                        // return 0;
                } else {
                        return 1;
                }
        }
}
