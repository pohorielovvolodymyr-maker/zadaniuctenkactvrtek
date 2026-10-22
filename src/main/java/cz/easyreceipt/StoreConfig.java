package cz.easyreceipt;

import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Properties;
// jmeno, adresa, ico a mena obchodu
public class StoreConfig {
    private final String name;
    private final String address;
    private final String ico;
    private final String currency;

    private StoreConfig(String name, String address, String ico, String currency) {
        this.name = name;
        this.address = address;
        this.ico = ico;
        this.currency = currency;
    }

    public static StoreConfig load(Path path) throws IOException {
        Properties properties = new Properties();
        try (InputStream input = Files.newInputStream(path)) {
            properties.load(input);
        } //nesmi byt null
        return new StoreConfig(
                required(properties, "name"),
                required(properties, "address"),
                required(properties, "ico"),
                required(properties, "currency"));
    }

    private static String required(Properties properties, String key) {
        String value = properties.getProperty(key);
        if (value == null || value.isBlank()) {
            throw new IllegalArgumentException("V konfiguracnim souboru chybi polozka: " + key);
        }
        return value.trim();
    }

    public String getName() { return name; }
    public String getAddress() { return address; }
    public String getIco() { return ico; }
    public String getCurrency() { return currency; }
}
