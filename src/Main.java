import java.io.File;
import java.io.IOException;
import java.sql.SQLException;
import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartUtils;
import org.jfree.data.category.DefaultCategoryDataset;
import java.util.Map;
import java.util.TreeMap;

public class Main {

    private static SQLHandler sql;

    public static void main(String[] args) {
        try {
            var countries = CSV.parse();
            sql = new SQLHandler();

            // Добавление стран в базу данных
            for (var country : countries) {
                sql.addCountry(country);
            }

            // Выполнение заданных SQL-запросов
            sql1();
            sql2();
            sql3();

        } catch (SQLException e) {
            handleException(e);
        }
    }

    // Метод для формирования графика по показателю экономики и сохранения его в файл
    private static void sql1() {
        try {
            DefaultCategoryDataset dataset = new DefaultCategoryDataset();
            var countryByEconomy = sql.getCountriesEconomy();

            Map<String, Double> sortedCountryByEconomy = new TreeMap<>(countryByEconomy);

            for (Map.Entry<String, Double> entry : sortedCountryByEconomy.entrySet()) {
                dataset.addValue(entry.getValue(), entry.getKey(), "");
            }

            var graph = ChartFactory.createBarChart("График по показателю экономики", "Страна", "ВВП", dataset);
            ChartUtils.saveChartAsPNG(new File("graph.png"), graph, 1920, 1080);
            System.out.println("\n№1. График по показателю экономики сформирован в файл graph.png");

        } catch (SQLException | IOException e) {
            handleException(e);
        }
    }

    // Метод для получения страны с самым высоким показателем экономики
    private static void sql2() {
        try {
            System.out.println("\n№2. Страна с самым высоким показателем экономики среди \"Latin America and Caribbean\" и \"Eastern Asia\": "
                    + sql.getCountryWithHigherEconomy());

        } catch (SQLException e) {
            handleException(e);
        }
    }

    // Метод для получения страны с самыми средними показателями
    private static void sql3() {
        try {
            System.out.println("\n№3. Страна с \"самыми средними показателями\" среди \"Western Europe\" и \"North America\": "
                    + sql.getCountryWithAvg());

        } catch (SQLException e) {
            handleException(e);
        }
    }

    // Обработка исключений
    private static void handleException(Exception e) {
        System.err.println("Ошибка выполнения программы: " + e.getMessage());
        e.printStackTrace();
    }
}
