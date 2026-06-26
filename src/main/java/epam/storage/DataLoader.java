package epam.storage;

import java.io.InputStream;
import java.util.Map;

public interface DataLoader<V, ID> {

    Map<? extends Number, V> loadData(InputStream inputStream);
    String getStorageBeanName();
}
