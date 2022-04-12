import org.springframework.boot.configurationprocessor.json.JSONException;
import org.springframework.boot.configurationprocessor.json.JSONObject;
import org.springframework.http.*;
import org.springframework.web.client.RestTemplate;
import java.text.SimpleDateFormat;
import java.util.*;

public class ConsoleApplication {

    static RestTemplate restTemplate = new RestTemplate();
    static HttpHeaders header = new HttpHeaders();
    private static String token;
    static HttpEntity<String> entity;

    static Scanner in = new Scanner(System.in);

    public static void signIn() throws JSONException {
        System.out.println("\nUser name: ");
        String name = in.next();
        System.out.println("\nPassword: ");
        String password = in.next();

        JSONObject jsonObject = new JSONObject();
        jsonObject.put("userName", name);
        jsonObject.put("password", password);

        HttpEntity<String> request = new HttpEntity<>(jsonObject.toString(), header);
        String object = restTemplate.postForObject("http://localhost:8080/company/auth/signIn", request, String.class);
        if (object != null) {
            header.set("Authorization", "Bearer " + object);
            entity = new HttpEntity<>(null, header);
            token = object;
            System.out.println(token);
        }
    }

    public static void getAllGoods() {
        ResponseEntity<List> list = restTemplate.getForEntity("http://localhost:8080/company/goods", List.class);
        for (Object object : Objects.requireNonNull(list.getBody())) {
            System.out.println(object.toString());
        }
    }

    //user, admin
    private static void getAllSales() {
        ResponseEntity<List> list;
        try {
            list = restTemplate.exchange("http://localhost:8080/company/sales", HttpMethod.GET, entity, List.class);
        } catch (Exception e) {
            System.out.println("Access is denied");
            return;
        }
        for (Object object : Objects.requireNonNull(list.getBody())) {
            System.out.println(object.toString());
        }

    }

    private static void getAllGoodsFromWarehouse1() {
        ResponseEntity<List> list = restTemplate.getForEntity("http://localhost:8080/company/warehouse1", List.class);
        for (Object object : Objects.requireNonNull(list.getBody())) {
            System.out.println(object.toString());
        }
    }

    private static void getAllGoodsFromWarehouse2() {
        ResponseEntity<List> list = restTemplate.getForEntity("http://localhost:8080/company/warehouse2", List.class);
        for (Object object : Objects.requireNonNull(list.getBody())) {
            System.out.println(object.toString());
        }
    }

    //user, admin
    private static void getAllSalesAndGoods() {
        HttpEntity<String> entity = new HttpEntity<>(null, header);
        ResponseEntity<List> list = restTemplate.exchange("http://localhost:8080/company/sales/salesAndGoods", HttpMethod.GET, entity, List.class);
        for (Object object : Objects.requireNonNull(list.getBody())) {
            System.out.println(object.toString());
        }
    }

    //admin
    private static void deleteAllSales() {
        try {
            restTemplate.exchange("http://localhost:8080/company/sales/deleteAll", HttpMethod.PUT, entity, Object.class);
        } catch (Exception ex) {
            System.out.println("Access is denied");
            return;
        }
        System.out.println("\nDelete all sales\n");
    }

    //admin
    private static void deleteAllWarehouse1() {
        try {
            restTemplate.exchange("http://localhost:8080/company/warehouse1/deleteAll", HttpMethod.PUT, entity, Object.class);
        } catch (Exception e) {
            System.out.println("Access is denied");
            return;
        }
        System.out.println("\nDelete all goods from warehouse1\n");
    }

    //admin
    private static void deleteAllWarehouse2() {
        try {
            restTemplate.exchange("http://localhost:8080/company/warehouse2/deleteAll", HttpMethod.PUT, entity, Object.class);
        } catch (Exception e) {
            System.out.println("Access is denied");
            return;
        }
        System.out.println("\nDelete all goods from warehouse2\n");
    }


    //Команды с параметром

    //admin
    private static void createGood() throws JSONException {
        System.out.println("\nName: ");
        String name = in.next();
        String regex = "[a-zA-Z]+.+";
        if (!name.matches(regex)) {
            System.out.println("Invalid good's name");
            return;
        }
        System.out.println("\nPriority: ");
        String priority = in.next();
        regex = "[0-9]+\\.?[0-9]*";
        if (!priority.matches(regex)) {
            System.out.println("Invalid priority");
            return;
        }
        double p = Double.parseDouble(priority);
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("name", name);
        jsonObject.put("priority", p);

        HttpEntity<String> request = new HttpEntity<>(jsonObject.toString(), header);
        Object object;
        try {
            object = restTemplate.postForObject("http://localhost:8080/company/goods/createGood", request, Object.class);
        } catch (Exception e) {
            System.out.println("Access is denied");
            return;
        }
        System.out.println(object);
    }

    //user, admin
    private static void createSale() throws JSONException {
        System.out.println("\nGood's id: ");
        String id = in.next();
        String regex = "[0-9]+";
        if (!id.matches(regex)) {
            System.out.println("Invalid good_id");
            return;
        }

        System.out.println("\nCount: ");
        String cnt = in.next();
        if (!cnt.matches(regex)) {
            System.out.println("Invalid count");
            return;
        }

        int count = Integer.parseInt(cnt);
        int good_id = Integer.parseInt(id);
        String date = new SimpleDateFormat("dd/MM/yyyy-HH:mm").format(new Date(System.currentTimeMillis()));

        JSONObject jsonObject = new JSONObject();
        jsonObject.put("good_id", good_id);
        jsonObject.put("good_count", count);
        jsonObject.put("create_date", date);

        HttpEntity<String> request = new HttpEntity<>(jsonObject.toString(), header);
        Object object;
        try {
            object = restTemplate.postForObject("http://localhost:8080/company/sales/createSale", request, Object.class);
        } catch (Exception e) {
            System.out.println("Access is denied");
            return;
        }
        System.out.println(object);
    }

    //admin
    private static void addGoodToWarehouse1() throws JSONException {
        System.out.println("\nGood's id: ");
        String id = in.next();
        String regex = "[0-9]+";
        if (!id.matches(regex)) {
            System.out.println("Invalid good_id");
            return;
        }

        System.out.println("\nCount: ");
        String cnt = in.next();
        if (!cnt.matches(regex)) {
            System.out.println("Invalid count");
            return;
        }

        int count = Integer.parseInt(cnt);
        int good_id = Integer.parseInt(id);
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("good_id", good_id);
        jsonObject.put("good_count", count);

        HttpEntity<String> request = new HttpEntity<>(jsonObject.toString(), header);
        try {
            restTemplate.postForObject("http://localhost:8080/company/warehouse1/addGoodToWarehouse1", request, Object.class);
        } catch (Exception e) {
            System.out.println("Access is denied or good wasn't found");
            return;
        }
        System.out.println("\nGood was added\n");
    }

    //admin
    private static void addGoodToWarehouse2() throws JSONException {
        System.out.println("\nGood's id: ");
        String id = in.next();
        String regex = "[0-9]+";
        if (!id.matches(regex)) {
            System.out.println("Invalid good_id");
            return;
        }

        System.out.println("\nCount: ");
        String cnt = in.next();
        if (!cnt.matches(regex)) {
            System.out.println("Invalid count");
            return;
        }

        int count = Integer.parseInt(cnt);
        int good_id = Integer.parseInt(id);
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("good_id", good_id);
        jsonObject.put("good_count", count);

        HttpEntity<String> request = new HttpEntity<>(jsonObject.toString(), header);
        try {
            restTemplate.postForObject("http://localhost:8080/company/warehouse2/addGoodToWarehouse2", request, Object.class);
        } catch (Exception e) {
            System.out.println("Access is denied or good wasn't found");
            return;
        }
        System.out.println("\nGood was added\n");
    }

    //Goods
    public static void getGoodById() {
        System.out.println("Good's id: ");
        String id1 = in.next();
        String regex = "[0-9]+";
        if (!id1.matches(regex)) {
            System.out.println("Invalid good_id");
            return;
        }
        int id = Integer.parseInt(id1);
        Object object;
        try {
            object = restTemplate.exchange("http://localhost:8080/company/goods/byId=" + id, HttpMethod.GET, entity, Object.class);
        } catch (Exception e) {
            System.out.println("Good wasn't found");
            return;
        }
        System.out.println(object);
    }

    public static void getGoodByName() {
        String name = in.next();
        String regex = "[a-zA-Z]+.+";
        if (!name.matches(regex)) {
            System.out.println("Invalid good's name");
            return;
        }
        Object object;
        try {
            object = restTemplate.exchange("http://localhost:8080/company/goods/byName=" + name, HttpMethod.GET, entity, Object.class);
        } catch (Exception e) {
            System.out.println("Good wasn't found");
            return;
        }
        System.out.println(object);
    }


    //Sales
    //admin
    private static void getSaleById() {
        System.out.println("Sale's id: ");
        String id1 = in.next();
        String regex = "[0-9]+";
        if (!id1.matches(regex)) {
            System.out.println("Invalid sale's id");
            return;
        }
        int id = Integer.parseInt(id1);
        Object object;
        try {
            object = restTemplate.exchange("http://localhost:8080/company/sales/byId=" + id, HttpMethod.GET, entity, Object.class);
        } catch (Exception e) {
            System.out.println("Access is denied or Sale wasn't found");
            return;
        }
        System.out.println(object);
    }

    //admin
    private static void getSalesByGoodId() {
        System.out.println("Good's id: ");
        String id1 = in.next();
        String regex = "[0-9]+";
        if (!id1.matches(regex)) {
            System.out.println("Invalid sale's id");
            return;
        }
        int id = Integer.parseInt(id1);
        ResponseEntity<List> list;
        try {
            list = restTemplate.exchange("http://localhost:8080/company/sales/byGoodId=" + id, HttpMethod.GET, entity, List.class);
        } catch (Exception e) {
            System.out.println("Access is denied or Sale wasn't found");
            return;
        }
        for (Object object : Objects.requireNonNull(list.getBody())) {
            System.out.println(object.toString());
        }
    }

    //user, admin
    private static void updateSale() throws JSONException {
        System.out.println("\nGood's id: ");
        String id1 = in.next();
        String regex = "[0-9]+";
        if (!id1.matches(regex)) {
            System.out.println("Invalid good_id");
            return;
        }

        System.out.println("\nCount: ");
        String cnt = in.next();
        if (!cnt.matches(regex)) {
            System.out.println("Invalid count");
            return;
        }

        int count = Integer.parseInt(cnt);
        int id = Integer.parseInt(id1);
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("id", id);
        jsonObject.put("good_count", count);

        HttpEntity<String> request = new HttpEntity<>(jsonObject.toString(), header);
        try {
        restTemplate.postForObject("http://localhost:8080/company/sales/updateSaleCount", request, Object.class);
        } catch (Exception e) {
            System.out.println("Access is denied");
        }
    }

    //admin
    private static void deleteSaleById() {
        System.out.println("Sale's id: ");
        String id1 = in.next();
        String regex = "[0-9]+";
        if (!id1.matches(regex)) {
            System.out.println("Invalid sale's id");
            return;
        }
        int id = Integer.parseInt(id1);
        try {
        restTemplate.exchange("http://localhost:8080/company/sales/deleteById=" + id, HttpMethod.PUT, entity, Object.class);
        } catch (Exception e) {
            System.out.println("Access is denied or Sale wasn't found");
            return;
        }
        System.out.println("\nDelete  sale (id = " + id + ")\n");
    }


    //Warehouse1
    private static void getWarehouse1ByGoodId() {
        System.out.println("Good's id: ");
        String id1 = in.next();
        String regex = "[0-9]+";
        if (!id1.matches(regex)) {
            System.out.println("Invalid Good's id");
            return;
        }
        int id = Integer.parseInt(id1);
        ResponseEntity<Object> objectResponseEntity;
        try {
        objectResponseEntity = restTemplate.exchange("http://localhost:8080/company/warehouse1/byGoodId=" + id, HttpMethod.GET, entity, Object.class);
        } catch (Exception e) {
            System.out.println("Good wasn't found in warehouse1");
            return;
        }
        System.out.println(objectResponseEntity);
    }

    //admin
    private static void deleteWarehouse1ByGoodId() {
        System.out.println("Good's id: ");
        String id1 = in.next();
        String regex = "[0-9]+";
        if (!id1.matches(regex)) {
            System.out.println("Invalid Good's id");
            return;
        }
        int id = Integer.parseInt(id1);
        try {
        restTemplate.exchange("http://localhost:8080/company/warehouse1/deleteByGoodId=" + id, HttpMethod.PUT, entity, Object.class);
        } catch (Exception e) {
            System.out.println("Access is denied or Good wasn't found");
            return;
        }
        System.out.println("\nDelete  warehouse1 (id = " + id + ")\n");
    }


    //Warehouse2
    private static void getWarehouse2ByGoodId() {
        System.out.println("Good's id: ");
        String id1 = in.next();
        String regex = "[0-9]+";
        if (!id1.matches(regex)) {
            System.out.println("Invalid Good's id");
            return;
        }
        int id = Integer.parseInt(id1);
        ResponseEntity<Object> objectResponseEntity;
        try {
            objectResponseEntity = restTemplate.exchange("http://localhost:8080/company/warehouse1/byGoodId=" + id, HttpMethod.GET, entity, Object.class);
        } catch (Exception e) {
            System.out.println("Good wasn't found in warehouse1");
            return;
        }
        System.out.println(objectResponseEntity);
    }

    //admin
    private static void deleteWarehouse2ByGoodId() {
        System.out.println("Good's id: ");
        String id1 = in.next();
        String regex = "[0-9]+";
        if (!id1.matches(regex)) {
            System.out.println("Invalid Good's id");
            return;
        }
        int id = Integer.parseInt(id1);
        try {
            restTemplate.exchange("http://localhost:8080/company/warehouse2/deleteByGoodId=" + id, HttpMethod.PUT, entity, Object.class);
        } catch (Exception e) {
            System.out.println("Access is denied or Good wasn't found");
            return;
        }
        System.out.println("\nDelete  warehouse2 (id = " + id + ")\n");
    }

    public static void getCommands() {
        System.out.println("sign in");
        System.out.println("get goods");
        System.out.println("get sales");
        System.out.println("get warehouse1");
        System.out.println("get warehouse2");
        System.out.println("get sales and goods");
        System.out.println("delete all sales");
        System.out.println("delete all warehouse1");
        System.out.println("delete all warehouse2");
        System.out.println("create good");
        System.out.println("create sale");
        System.out.println("add good to warehouse1");
        System.out.println("add good to warehouse2");
        System.out.println("get good by id");
        System.out.println("get good by name");
        System.out.println("get sale by id");
        System.out.println("get sales by good id");
        System.out.println("update sale");
        System.out.println("delete sale by id");
        System.out.println("get warehouse1 by good id");
        System.out.println("get warehouse2 by good id");
        System.out.println("delete warehouse1 by good id");
        System.out.println("delete warehouse2 by good id");
    }

    public static void main(String[] args) {
        header.setContentType(MediaType.APPLICATION_JSON);
        System.out.println("Hello! If you want to see the command list, enter 'get commands'");
        String command = in.nextLine();
        while (!(command.equals("exit"))) {
            try {
                //23 метода
                switch (command) {
                    case "get commands" -> getCommands();
                    case "sign in" -> signIn();
                    case "get goods" -> getAllGoods();
                    case "get sales" -> getAllSales();
                    case "get warehouse1" -> getAllGoodsFromWarehouse1();
                    case "get warehouse2" -> getAllGoodsFromWarehouse2();
                    case "get sales and goods" -> getAllSalesAndGoods();

                    case "delete all sales" -> deleteAllSales();
                    case "delete all warehouse1" -> deleteAllWarehouse1();
                    case "delete all warehouse2" -> deleteAllWarehouse2();

                    //команды с параметром
                    case "create good" -> createGood();
                    case "create sale" -> createSale();
                    case "add good to warehouse1" -> addGoodToWarehouse1();
                    case "add good to warehouse2" -> addGoodToWarehouse2();

                    case "get good by id" -> getGoodById();
                    case "get good by name" -> getGoodByName();

                    case "get sale by id" -> getSaleById();
                    case "get sales by good id" -> getSalesByGoodId();
                    case "update sale" -> updateSale();
                    case "delete sale by id" -> deleteSaleById();

                    case "get warehouse1 by good id" -> getWarehouse1ByGoodId();
                    case "delete warehouse1 by good id" -> deleteWarehouse1ByGoodId();

                    case "get warehouse2 by good id" -> getWarehouse2ByGoodId();
                    case "delete warehouse2 by good id" -> deleteWarehouse2ByGoodId();
                    case "" -> {
                    }
                    default -> System.out.println("You entered wrong command");
                }
            } catch (JSONException ex) {
                System.out.println("Entered invalid arguments");
            } catch (Exception e) {
                System.out.println("\nSomething went wrong, please try again\n");
            }
            command = in.nextLine();
        }
    }
}
