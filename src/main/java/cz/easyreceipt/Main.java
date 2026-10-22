package cz.easyreceipt;

import java.math.BigDecimal;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;
//v teto tride se spousti program
public class Main {
    public static void main(String[] args) {
        System.out.println("EasyReceipt 1.0.0");
        try {
            StoreConfig store = StoreConfig.load(Path.of("store.properties"));
            List<Product> products = readProducts();
            ReceiptService service = new ReceiptService();
            Receipt receipt = service.createReceipt("0001", products);
            System.out.println();
            System.out.print(new ReceiptPrinter().print(receipt, store));
        } catch (Exception exception) {
            System.err.println("Chyba: " + exception.getMessage());
            System.err.println("Zkontrolujte vstupni udaje a soubor store.properties.");
            System.exit(1);
        }
    }

    private static List<Product> readProducts() {
        List<Product> products = new ArrayList<>();
        try (Scanner scanner = new Scanner(System.in)) {
            System.out.println("Zadejte nakup. Pro ukonceni zadavani napiste 'konec'.");
            while (true) {
                System.out.print("Nazev produktu: ");
                String name = scanner.nextLine().trim();
                if (name.equalsIgnoreCase("konec")) break;
                if (name.isBlank()) {
                    System.out.println("Nazev nesmi byt prazdny.");
                    continue;
                }

                BigDecimal price;
                while (true) {
                    System.out.print("Cena za kus (napr. 25.90): ");
                    String input = scanner.nextLine().trim().replace(',', '.');
                    try {
                        price = new BigDecimal(input);
                        if (price.compareTo(BigDecimal.ZERO) < 0) {
                            System.out.println("Cena nesmi byt zaporna.");
                            continue;
                        }
                        break;
                    } catch (NumberFormatException ex) {
                        System.out.println("Zadejte platnou cenu, napr. 25.90.");
                    }
                }

                int quantity;
                while (true) {
                    System.out.print("Pocet kusu: ");
                    try {
                        quantity = Integer.parseInt(scanner.nextLine().trim());
                        if (quantity <= 0) {
                            System.out.println("Pocet musi byt vetsi nez nula.");
                            continue;
                        }
                        break;
                    } catch (NumberFormatException ex) {
                        System.out.println("Zadejte cele kladne cislo.");
                    }
                }

                products.add(new Product(name, price, quantity));
                System.out.println("Produkt pridan.\n");
            }
        }
        if (products.isEmpty()) {
            throw new IllegalArgumentException("Nebyl zadan zadny produkt.");
        }
        return products;
    }
}
