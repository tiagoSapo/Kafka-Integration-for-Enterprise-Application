
package com.exemplo.ismeta3resttest;

import com.exemplo.dto.ClientDto;
import com.exemplo.dto.CurrencyDto;
import com.exemplo.dto.ManagerDto;
import com.exemplo.ismeta3resttest.utils.ClientUtils;
import static com.exemplo.ismeta3resttest.utils.ClientUtils.readJsonFromUrl;
import static com.exemplo.ismeta3resttest.utils.ClientUtils.readJsonFromUrlArray;
import java.io.IOException;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Scanner;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.apache.commons.lang3.StringUtils;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class Main {
    
    public static void main(String[] args) {
        
        StringBuilder sb = new StringBuilder();
        
        sb.append("------ Testar camada REST ------\n");
        sb.append("1) GET Clients\n");
        sb.append("2) GET Client by ID\n");
        sb.append("3) ADD Client\n\n");
        sb.append("4) GET Managers\n");
        sb.append("5) GET Managers by ID\n");
        sb.append("6) ADD Managers\n\n");
        sb.append("7) GET Currencies\n");
        sb.append("8) GET Currency by ID\n");
        sb.append("9) ADD Currency\n\n");
        sb.append("10) GET Credits per Client\n");
        sb.append("11) GET Payments per Client\n");
        sb.append("12) GET Balance per Client\n\n");
        sb.append("13) GET all Clients Credits\n");
        sb.append("14) GET all Clients Payments\n");
        sb.append("15) GET all Clients Balances\n\n");
        sb.append("16) Client highest Debt\n");
        sb.append("17) Manager highest Revenue\n\n");
        sb.append("18) GET 1 month Bill per Client\n");
        sb.append("0) EXIT!\n> ");
        
        String menu = sb.toString();
        Scanner sc = new Scanner(System.in);
        
        loop: while (true) {
            
            System.out.print(menu);
            String input = sc.next();
            
            switch (input) {
                case "1":
                    getClients();
                    break;
                case "2":
                    getClientsById(sc);
                    break;
                case "3":
                    addClient(sc);
                    break;
                case "4":
                    getManagers();
                    break;
                case "5":
                    getManagersById(sc);
                    break;
                case "6":
                    addManager(sc);
                    break;
                case "7":
                    getCurrencies();
                    break;
                case "8":
                    getCurrenciesById(sc);
                    break;
                case "9":
                    addCurrency(sc);
                    break;
                case "10":
                    getCreditsPerClient(sc);
                    break;
                case "11":
                    getPaymentsPerClient(sc);
                    break;
                case "12":
                    getBalancesPerClient(sc);
                    break;
                case "13":
                    getAllCredits();
                    break;
                case "14":
                    getAllPayments();
                    break;
                case "15":
                    getAllBalances();
                    break;
                case "16":
                    getHighestDebt();
                    break;
                case "17":
                    getHighestRevenue();
                    break;
                case "18":
                    getBillsPerClient(sc);
                    break;
                default:
                    sc.close();
                    break loop;
            }
        }
    }

    private static void getClients() {
        try {
            JSONArray json = readJsonFromUrlArray("http://127.0.0.1:8080/clients");
            System.out.println(json.toString());
        } catch (IOException | JSONException ex) {
            Logger.getLogger(Main.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    private static void getClientsById(Scanner sc) {
        
        int id;
        String input;
        String msg = "Cliente id:\n> ";
        
        System.out.print(msg);
        input = sc.next();
        
        try {
            id = Integer.valueOf(input);
            JSONObject json = readJsonFromUrl("http://127.0.0.1:8080/clients/" + String.valueOf(id));
            System.out.println(json.toString());
        } catch (IOException | JSONException ex) {
            System.err.println(ex);
        }
    }

    private static void getManagers() {
        try {
            JSONArray json = readJsonFromUrlArray("http://127.0.0.1:8080/managers");
            System.out.println(json.toString());
        } catch (IOException | JSONException ex) {
            Logger.getLogger(Main.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    private static void getManagersById(Scanner sc) {
        int id;
        String input;
        String msg = "Manager id:\n> ";
        
        System.out.print(msg);
        input = sc.next();
        try {
            id = Integer.valueOf(input);
            JSONObject json = readJsonFromUrl("http://127.0.0.1:8080/managers/" + String.valueOf(id));
            System.out.println(json.toString());
        } catch (IOException | JSONException ex) {
            System.err.println(ex);
        }
    }

    private static void getCurrencies() {
        try {
            JSONArray json = readJsonFromUrlArray("http://127.0.0.1:8080/currencies");
            System.out.println(json.toString());
        } catch (IOException | JSONException ex) {
            Logger.getLogger(Main.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    private static void getCurrenciesById(Scanner sc) {
        int id;
        String input;
        String msg = "Cliente id:\n> ";
        
        System.out.print(msg);
        input = sc.next();
        try {
            id = Integer.valueOf(input);
            JSONObject json = readJsonFromUrl("http://127.0.0.1:8080/currencies/" + String.valueOf(id));
            System.out.println(json.toString());
        } catch (IOException | JSONException ex) {
            System.err.println(ex);
        }
    }

    private static void addManager(Scanner sc) {
        
        String input;
        String msg;
        
        msg = "Nome do manager:\n> ";
        System.out.print(msg);
        
        input = sc.next();
        if (!StringUtils.isEmpty(input)) {
            ManagerDto manager = new ManagerDto(input);
            try {
                ClientUtils.sendJson(manager, "http://localhost:8080/managers");
            } catch (IOException ex) {
                System.err.println(ex);
            }
        }
    }
    
    private static void addClient(Scanner sc) {
        
        String nome, id;
        String msg;
        
        msg = "Nome do cliente:\n> ";
        System.out.print(msg);
        nome = sc.next();
        
        msg = "Id do Manager do cliente a registar:\n> ";
        System.out.print(msg);
        id = sc.next();
        
        if (!StringUtils.isEmpty(nome) && !StringUtils.isEmpty(id)) {
            try {
                ClientDto cli = new ClientDto(nome, Integer.valueOf(id));
                ClientUtils.sendJson(cli, "http://localhost:8080/clients");
            } 
            catch (NumberFormatException | IOException ex) {
                System.err.println(ex);
            }
            
        }
    }
    
    private static void addCurrency(Scanner sc) {
        
        String nome, rate;
        String msg;
        
        msg = "Nome da moeda:\n> ";
        System.out.print(msg);
        nome = sc.next();
        
        msg = "Rate of exchange:\n> ";
        System.out.print(msg);
        rate = sc.next();
        
        if (!StringUtils.isEmpty(nome) && !StringUtils.isEmpty(rate)) {
            
            try {
                double rateValue = Double.valueOf(rate);
                CurrencyDto currency = new CurrencyDto(nome, rateValue);
                ClientUtils.sendJson(currency, "http://localhost:8080/currencies");
            } 
            catch (NumberFormatException | IOException ex) {
                System.err.println(ex);
            }
            
        }
    }

    private static void getCreditsPerClient(Scanner sc) {
        
        int id;
        String input;
        String msg = "Cliente id:\n> ";
        
        System.out.print(msg);
        input = sc.next();
        
        try {
            id = Integer.valueOf(input);
            JSONObject json = readJsonFromUrl("http://127.0.0.1:8080/clients/" + String.valueOf(id) + "/credits");
            System.out.println(json.toString());
        } catch (IOException | JSONException ex) {
            System.err.println(ex);
        }
    }

    private static void getPaymentsPerClient(Scanner sc) {
        
        int id;
        String input;
        String msg = "Cliente id:\n> ";
        
        System.out.print(msg);
        input = sc.next();
        
        try {
            id = Integer.valueOf(input);
            JSONObject json = readJsonFromUrl("http://127.0.0.1:8080/clients/" + String.valueOf(id) + "/payments");
            System.out.println(json.toString());
        } catch (IOException | JSONException ex) {
            System.err.println(ex);
        }
    }

    private static void getBalancesPerClient(Scanner sc) {
        
        int id;
        String input;
        String msg = "Cliente id:\n> ";
        
        System.out.print(msg);
        input = sc.next();
        
        try {
            id = Integer.valueOf(input);
            JSONObject json = readJsonFromUrl("http://127.0.0.1:8080/clients/" + String.valueOf(id) + "/balance");
            System.out.println(json.toString());
        } catch (IOException | JSONException ex) {
            System.err.println(ex);
        }
    }

    private static void getAllCredits() {
        
        try {
            JSONArray json = readJsonFromUrlArray("http://127.0.0.1:8080/clients/credits");
            System.out.println(json.toString());
        } catch (IOException | JSONException ex) {
            System.err.println(ex);
        }
    }

    private static void getAllPayments() {
        
        try {
            JSONArray json = readJsonFromUrlArray("http://127.0.0.1:8080/clients/payments");
            System.out.println(json.toString());
        } catch (IOException | JSONException ex) {
            System.err.println(ex);
        }
    }

    private static void getAllBalances() {
        try {
            JSONArray json = readJsonFromUrlArray("http://127.0.0.1:8080/clients/balances");
            System.out.println(json.toString());
        } catch (IOException | JSONException ex) {
            System.err.println(ex);
        }
    }

    private static void getHighestDebt() {
        
        try {
            JSONObject json = readJsonFromUrl("http://127.0.0.1:8080/clients/max-credits");
            System.out.println(json.toString());
        } catch (IOException | JSONException ex) {
            System.err.println(ex);
        }
    }

    private static void getHighestRevenue() {
        try {
            JSONObject json = readJsonFromUrl("http://127.0.0.1:8080/managers/best-manager");
            System.out.println(json.toString());
        } catch (IOException | JSONException ex) {
            System.err.println(ex);
        }
    }

    private static void getBillsPerClient(Scanner sc) {
        
        int id;
        String input;
        String msg = "Cliente id:\n> ";
        
        System.out.print(msg);
        input = sc.next();
        
        try {
            id = Integer.valueOf(input);
            JSONObject json = readJsonFromUrl("http://127.0.0.1:8080/clients/" + String.valueOf(id) + "/bill");
            System.out.println(json.toString());
        } catch (IOException | JSONException ex) {
            System.err.println(ex);
        }
    }
}
