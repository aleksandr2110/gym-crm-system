package epam.storage;

import epam.domain.Trainer;

import java.io.InputStream;
import java.util.Map;

public interface DataLoader<K, V> {

    Map<K, V> loadData(InputStream inputStream);
}
