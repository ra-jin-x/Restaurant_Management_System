import com.restaurant.CLI.tableCLI;
import com.restaurant.service.tableServices;

public class main {
    public static void main(String[] args) {
        tableServices serv = new tableServices();
        tableCLI cli = new tableCLI(serv);
        cli.start();
    }
}