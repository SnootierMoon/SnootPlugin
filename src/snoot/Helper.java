package snoot;

import java.util.List;
import java.util.stream.Collectors;

public class Helper {

    public static List<String> filterPrefix(List<String> list, String prefix) {
        return list.stream().filter(s -> s.startsWith(prefix)).collect(Collectors.toList());
    }

}
