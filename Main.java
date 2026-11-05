import java.util.List;
import java.util.Map;
import java.util.Scanner;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;

public class Main {
    private static AuthService authService = new AuthService();
    private static MenuService menuService = new MenuService();
    private static TableService tableService = new TableService();
    private static OrderService orderService = new OrderService();
    private static BillingService billingService = new BillingService();
    private static PaymentService paymentService = new PaymentService();
    private static ReportService reportService = new ReportService();
    private static Scanner scanner = new Scanner(System.in);

    public static void main(String[] args) {
        boolean running = true;
        while (running) {
            ConsoleUtil.printHeader("RESTAURANT MANAGEMENT SYSTEM");
            System.out.println("Welcome to Restaurant Management System\n");
            System.out.println("1. Login");
            System.out.println("2. Exit");
            
            try {
                int choice = InputUtil.readInt(scanner, "\nEnter your choice: ");
                if (choice == 1) {
                    handleLogin();
                } else if (choice == 2) {
                    System.out.println("Thank you!");
                    running = false;
                } else {
                    System.out.println("Invalid choice.");
                }
            } catch (InputUtil.CancelOperationException e) {
                // Ignore and loop
            }
        }
        scanner.close();
    }

    private static void handleLogin() {
        System.out.print("\nUsername: ");
        String username = scanner.nextLine().trim();
        System.out.print("Password: ");
        String password = scanner.nextLine().trim();

        if (authService.login(username, password)) {
            User user = authService.getCurrentUser();
            System.out.println("\nLogin successful!");
            System.out.println("\nWelcome, " + user.getUsername() + ".");
            System.out.println("Loading dashboard...");
            
            if (user.getRole() == Role.ADMIN) {
                adminDashboard();
            } else if (user.getRole() == Role.STAFF) {
                staffDashboard();
            } else {
                customerDashboard();
            }
        } else {
            System.out.println("\nInvalid username or password.");
            System.out.println("Please try again.");
        }
    }

    private static void adminDashboard() {
        boolean inMenu = true;
        while (inMenu) {
            try {
                ConsoleUtil.printHeader("ADMIN DASHBOARD");
                System.out.println("1. Manage Menu");
                System.out.println("2. Manage Tables");
                System.out.println("3. Manage Orders");
                System.out.println("4. Billing");
                System.out.println("5. Generate Report");
                System.out.println("6. Export Report to File");
                System.out.println("7. Logout");
                
                int choice = InputUtil.readInt(scanner, "\nEnter choice: ");
                switch (choice) {
                    case 1: manageMenu(); break;
                    case 2: manageTables(); break;
                    case 3: orderService.displayOrders(); break;
                    case 4: handleBilling(); break;
                    case 5: reportService.generateReport(orderService.getAllOrders(), false); break;
                    case 6: reportService.generateReport(orderService.getAllOrders(), true); break;
                    case 7: 
                        authService.logout();
                        inMenu = false;
                        break;
                    default: System.out.println("Invalid choice.");
                }
            } catch (InputUtil.CancelOperationException e) {
                System.out.println("\nGoing back...");
            }
        }
    }

    private static void staffDashboard() {
        boolean inMenu = true;
        while (inMenu) {
            try {
                ConsoleUtil.printHeader("STAFF DASHBOARD");
                System.out.println("1. View Menu");
                System.out.println("2. View Tables");
                System.out.println("3. Create Order");
                System.out.println("4. Manage Orders");
                System.out.println("5. Generate Bill");
                System.out.println("6. Process Payment");
                System.out.println("7. Logout");
                
                int choice = InputUtil.readInt(scanner, "\nEnter choice: ");
                switch (choice) {
                    case 1: menuService.displayMenu(); break;
                    case 2: tableService.displayAvailableTables(); break;
                    case 3: createOrder(); break;
                    case 4: manageOrders(); break;
                    case 5: handleBilling(); break;
                    case 6: handlePayment(); break;
                    case 7: 
                        authService.logout();
                        inMenu = false;
                        break;
                    default: System.out.println("Invalid choice.");
                }
            } catch (InputUtil.CancelOperationException e) {
                System.out.println("\nGoing back...");
            }
        }
    }

    private static void customerDashboard() {
        boolean inMenu = true;
        while (inMenu) {
            try {
                ConsoleUtil.printHeader("CUSTOMER DASHBOARD");
                System.out.println("1. View Menu");
                System.out.println("2. Order Food");
                System.out.println("3. View All Orders");
                System.out.println("4. Logout");
                
                int choice = InputUtil.readInt(scanner, "\nEnter choice: ");
                switch (choice) {
                    case 1: menuService.displayMenu(); break;
                    case 2: createOrder(); break;
                    case 3: orderService.displayOrders(); break;
                    case 4: 
                        authService.logout();
                        inMenu = false;
                        break;
                    default: System.out.println("Invalid choice.");
                }
            } catch (InputUtil.CancelOperationException e) {
                System.out.println("\nGoing back...");
            }
        }
    }

    private static void manageMenu() {
        boolean inMenu = true;
        while (inMenu) {
            try {
                ConsoleUtil.printHeader("MANAGE MENU");
                System.out.println("1. Add Food Item");
                System.out.println("2. Delete Food Item");
                System.out.println("3. View Menu");
                System.out.println("4. Back");
                int choice = InputUtil.readInt(scanner, "\nEnter choice: ");
                if (choice == 1) {
                    int id = InputUtil.readInt(scanner, "ID: ");
                    if (menuService.getFoodById(id) != null) {
                        System.out.println("\n[ALERT] Duplicate ID Detected! Food ID " + id + " already exists.");
                        continue;
                    }
                    String name = InputUtil.readString(scanner, "Name: ");
                    String category = InputUtil.readString(scanner, "Category: ");
                    double price = InputUtil.readDouble(scanner, "Price: ");
                    menuService.addFood(new FoodItem(id, name, category, price, true));
                    System.out.println("Added.");
                } else if (choice == 2) {
                    menuService.displayFullMenu();
                    int id = InputUtil.readInt(scanner, "\nEnter Food ID to delete: ");
                    if (menuService.getFoodById(id) != null) {
                        menuService.deleteFood(id);
                        System.out.println("Food item deleted successfully.");
                    } else {
                        System.out.println("Food item not found.");
                    }
                } else if (choice == 3) {
                    menuService.displayFullMenu();
                } else if (choice == 4) {
                    inMenu = false;
                } else {
                    System.out.println("Invalid choice.");
                }
            } catch (InputUtil.CancelOperationException e) {
                System.out.println("\nOperation cancelled. Going back...");
            }
        }
    }

    private static void manageTables() {
        boolean inMenu = true;
        while (inMenu) {
            try {
                ConsoleUtil.printHeader("MANAGE TABLES");
                System.out.println("1. Add Table");
                System.out.println("2. Change Table Status");
                System.out.println("3. View Tables");
                System.out.println("4. Back");
                int choice = InputUtil.readInt(scanner, "\nEnter choice: ");
                
                if (choice == 1) {
                    int tNum = InputUtil.readInt(scanner, "Table Number: ");
                    if (tableService.getTable(tNum) != null) {
                        System.out.println("\n[ALERT] Duplicate ID Detected! Table " + tNum + " already exists.");
                        continue;
                    }
                    int cap = InputUtil.readInt(scanner, "Capacity: ");
                    tableService.addTable(new RestaurantTable(tNum, cap, TableStatus.AVAILABLE));
                    System.out.println("Added.");
                } else if (choice == 2) {
                    tableService.displayTables();
                    int tNum = InputUtil.readInt(scanner, "Enter Table Number: ");
                    RestaurantTable table = tableService.getTable(tNum);
                    if (table == null) {
                        System.out.println("Table not found.");
                        continue;
                    }
                    System.out.println("1. AVAILABLE  2. OCCUPIED  3. RESERVED");
                    int stat = InputUtil.readInt(scanner, "Select Status: ");
                    if (stat == 1) table.setStatus(TableStatus.AVAILABLE);
                    else if (stat == 2) table.setStatus(TableStatus.OCCUPIED);
                    else if (stat == 3) table.setStatus(TableStatus.RESERVED);
                    else System.out.println("Invalid status choice.");
                    
                    if (stat >= 1 && stat <= 3) System.out.println("Updated.");
                } else if (choice == 3) {
                    tableService.displayTables();
                } else if (choice == 4) {
                    inMenu = false;
                }
            } catch (InputUtil.CancelOperationException e) {
                System.out.println("\nOperation cancelled. Going back...");
            }
        }
    }

    private static void createOrder() {
        ConsoleUtil.printHeader("CREATE ORDER");
        tableService.displayAvailableTables();
        
        int tNum = InputUtil.readInt(scanner, "Select table: ");
        RestaurantTable table = tableService.getTable(tNum);
        if (table == null || table.getStatus() != TableStatus.AVAILABLE) {
            System.out.println("Invalid or unavailable table.");
            return;
        }
        
        System.out.println("\nTable " + tNum + " selected.\n");
        tableService.setTableStatus(tNum, TableStatus.OCCUPIED);
        
        ConsoleUtil.printHeader("MENU");
        menuService.displayMenu();
        
        Order order = orderService.createOrder(tNum);
        boolean adding = true;
        while (adding) {
            int fId = InputUtil.readInt(scanner, "\nEnter food ID: ");
            FoodItem food = menuService.getFoodById(fId);
            if (food == null || !food.isAvailable()) {
                System.out.println("Invalid food ID.");
                continue;
            }
            int qty = InputUtil.readInt(scanner, "Enter quantity: ");
            if (qty > 0) {
                order.addItem(new OrderItem(food, qty));
                System.out.println("\nItem added successfully.");
            }
            
            System.out.print("\nAdd another item? (Y/N): ");
            if (!scanner.nextLine().trim().equalsIgnoreCase("Y")) {
                adding = false;
            }
        }
        System.out.println("\nOrder created successfully!");
        System.out.println("Order ID: " + order.getOrderId());
        System.out.println("Table: " + tNum);
    }

    private static void manageOrders() {
        orderService.displayOrders();
        String id = InputUtil.readString(scanner, "Enter Order ID to update (or 0 to go back): ");
        if (id.equals("0")) return;
        
        Order order = orderService.getOrder(id);
        if (order == null) {
            System.out.println("Order not found.");
            return;
        }
        
        System.out.println("Order ID: " + order.getOrderId());
        System.out.println("Current Status: " + order.getStatus());
        System.out.println("\n1. READY\n2. SERVED\n3. COMPLETED\n4. CANCELLED");
        int choice = InputUtil.readInt(scanner, "\nEnter choice: ");
        switch (choice) {
            case 1: order.setStatus(OrderStatus.READY); break;
            case 2: order.setStatus(OrderStatus.SERVED); break;
            case 3: 
                order.setStatus(OrderStatus.COMPLETED);
                tableService.setTableStatus(order.getTableNumber(), TableStatus.AVAILABLE);
                break;
            case 4: 
                order.setStatus(OrderStatus.CANCELLED);
                tableService.setTableStatus(order.getTableNumber(), TableStatus.AVAILABLE);
                break;
        }
        System.out.println("Updated.");
    }

    private static void handleBilling() {
        orderService.displayOrders();
        String id = InputUtil.readString(scanner, "Enter Order ID to generate bill (or 0 to go back): ");
        if (id.equals("0")) return;
        
        Order order = orderService.getOrder(id);
        if (order != null) {
            System.out.print("Export bill to file? (Y/N): ");
            boolean export = scanner.nextLine().trim().equalsIgnoreCase("Y");
            billingService.generateBill(order, export);
        } else {
            System.out.println("Order not found.");
        }
    }

    private static void handlePayment() {
        orderService.displayOrders();
        String id = InputUtil.readString(scanner, "Enter Order ID to process payment (or 0 to go back): ");
        if (id.equals("0")) return;
        
        Order order = orderService.getOrder(id);
        if (order != null) {
            if (order.getStatus() == OrderStatus.COMPLETED) {
                System.out.println("Order is already paid/completed.");
                return;
            }
            paymentService.processPayment(scanner, order);
            tableService.setTableStatus(order.getTableNumber(), TableStatus.AVAILABLE);
        } else {
            System.out.println("Order not found.");
        }
    }
}

class AuthService {
    private List<User> users;
    private User currentUser;

    public AuthService() {
        users = new ArrayList<>();
        // Default users
        users.add(new User(1, "admin", "admin123", Role.ADMIN));
        users.add(new User(2, "staff", "staff123", Role.STAFF));
        users.add(new User(3, "customer", "customer123", Role.CUSTOMER));
    }

    public boolean login(String username, String password) {
        for (User user : users) {
            if (user.getUsername().equals(username) && user.getPassword().equals(password)) {
                this.currentUser = user;
                return true;
            }
        }
        return false;
    }

    public void logout() {
        this.currentUser = null;
    }

    public User getCurrentUser() {
        return currentUser;
    }
}

class BillingService {
    public void generateBill(Order order, boolean export) {
        StringBuilder sb = new StringBuilder();
        sb.append("\n==================================================\n");
        sb.append("                    FINAL BILL                    \n");
        sb.append("==================================================\n");
        sb.append("Restaurant Name\n");
        sb.append("Restaurant Management System\n\n");
        sb.append("Order ID : ").append(order.getOrderId()).append("\n");
        sb.append("Table    : ").append(order.getTableNumber()).append("\n");
        sb.append("--------------------------------------------------\n");
        
        sb.append(String.format("%-25s %-10s %s\n", "ITEM", "QTY", "PRICE"));
        sb.append("--------------------------------------------------\n");
        for (OrderItem item : order.getItems()) {
            sb.append(String.format("%-25s %-10d Rs. %.2f\n", item.getFoodItem().getName(), item.getQuantity(), item.getSubtotal()));
        }
        sb.append("--------------------------------------------------\n");
        
        double subtotal = order.getTotal();
        double gst = subtotal * 0.05;
        double discount = 0; 
        double total = subtotal + gst - discount;
        
        sb.append(String.format("%-36s Rs. %.2f\n", "Subtotal", subtotal));
        sb.append(String.format("%-36s Rs. %.2f\n", "GST (5%)", gst));
        sb.append(String.format("%-36s Rs. %.2f\n", "Discount", discount));
        sb.append("--------------------------------------------------\n");
        sb.append(String.format("%-36s Rs. %.2f\n", "TOTAL", total));
        sb.append("--------------------------------------------------\n");
        sb.append("\nPayment Status: ").append(order.getStatus() == OrderStatus.COMPLETED ? "PAID" : "PENDING").append("\n");
        
        System.out.println(sb.toString());

        if (export) {
            String filename = "bill_" + order.getOrderId() + ".txt";
            try (FileWriter fw = new FileWriter(filename)) {
                fw.write(sb.toString());
                System.out.println("\nBill exported successfully to " + filename);
            } catch (IOException e) {
                System.out.println("\nError exporting bill: " + e.getMessage());
            }
        }
    }
}
class ConsoleUtil {
    public static void printHeader(String title) {
        System.out.println("\n==================================================");
        int padding = (50 - title.length()) / 2;
        if (padding < 0) padding = 0;
        for (int i = 0; i < padding; i++) System.out.print(" ");
        System.out.println(title);
        System.out.println("==================================================");
    }
    
    public static void printSeparator() {
        System.out.println("--------------------------------------------------");
    }
    
    public static void printQRCode(String data) {
        System.out.println();
        System.out.println("     === SCAN TO PAY ===");
        System.out.println("    +-------------------+");
        System.out.println("    | # # #       # # # |");
        System.out.println("    | #   #  ###  #   # |");
        System.out.println("    | # # #       # # # |");
        System.out.println("    |        ###        |");
        System.out.println("    |   #  #  #  #  #   |");
        System.out.println("    |  ##     #    ##   |");
        System.out.println("    |        ###        |");
        System.out.println("    | # # #    #  # # # |");
        System.out.println("    | #   #   ##  #   # |");
        System.out.println("    | # # #  #    # # # |");
        System.out.println("    +-------------------+");
        System.out.println("     UPI ID: rest@hackUPI  ");
        System.out.println();
    }
}
class FoodItem {
    private int id;
    private String name;
    private String category;
    private double price;
    private boolean available;

    public FoodItem(int id, String name, String category, double price, boolean available) {
        this.id = id;
        this.name = name;
        this.category = category;
        this.price = price;
        this.available = available;
    }

    public int getId() { return id; }
    public String getName() { return name; }
    public void setName(String name) { this.name = name; }
    public String getCategory() { return category; }
    public void setCategory(String category) { this.category = category; }
    public double getPrice() { return price; }
    public void setPrice(double price) { this.price = price; }
    public boolean isAvailable() { return available; }
    public void setAvailable(boolean available) { this.available = available; }
}
class IdGenerator {
    private static int orderCounter = 1000;
    private static int transactionCounter = 10000;

    public static String generateOrderId() {
        orderCounter++;
        return "ORD" + orderCounter;
    }

    public static String generateTransactionId() {
        transactionCounter++;
        return "TXN" + transactionCounter;
    }
}

class InputUtil {
    public static class CancelOperationException extends RuntimeException {}

    public static int readInt(Scanner scanner, String prompt) {
        while (true) {
            System.out.print(prompt);
            String input = scanner.nextLine().trim();
            if (input.equalsIgnoreCase("b")) throw new CancelOperationException();
            try {
                return Integer.parseInt(input);
            } catch (NumberFormatException e) {
                System.out.println("Invalid input. Please enter a valid number (or type 'b' to go back).");
            }
        }
    }

    public static double readDouble(Scanner scanner, String prompt) {
        while (true) {
            System.out.print(prompt);
            String input = scanner.nextLine().trim();
            if (input.equalsIgnoreCase("b")) throw new CancelOperationException();
            try {
                return Double.parseDouble(input);
            } catch (NumberFormatException e) {
                System.out.println("Invalid input. Please enter a valid decimal number (or type 'b' to go back).");
            }
        }
    }

    public static String readString(Scanner scanner, String prompt) {
        while (true) {
            System.out.print(prompt);
            String input = scanner.nextLine().trim();
            if (input.equalsIgnoreCase("b")) throw new CancelOperationException();
            if (!input.isEmpty()) {
                return input;
            }
            System.out.println("Input cannot be empty.");
        }
    }
}

class MenuService {
    private List<FoodItem> menu;

    public MenuService() {
        menu = new ArrayList<>();
        // Default items
        menu.add(new FoodItem(101, "Chicken Biriyani", "Main Course", 180, true));
        menu.add(new FoodItem(102, "Fried Rice", "Main Course", 140, true));
        menu.add(new FoodItem(103, "Paneer Butter Masala", "Main Course", 160, true));
        menu.add(new FoodItem(104, "Chicken 65", "Starter", 150, true));
        menu.add(new FoodItem(105, "French Fries", "Starter", 100, true));
        menu.add(new FoodItem(106, "Fresh Lime", "Beverage", 60, true));
        menu.add(new FoodItem(107, "Coke", "Beverage", 50, true));
    }

    public void displayMenu() {
        ConsoleUtil.printSeparator();
        System.out.printf("%-6s %-25s %-15s %s\n", "ID", "ITEM", "CATEGORY", "PRICE");
        ConsoleUtil.printSeparator();
        for (FoodItem item : menu) {
            if (item.isAvailable()) {
                String priceStr = String.format("Rs. %.2f", item.getPrice());
                System.out.printf("%-6d %-25s %-15s %s\n", item.getId(), item.getName(), item.getCategory(), priceStr);
            }
        }
        ConsoleUtil.printSeparator();
    }

    public void displayFullMenu() {
        ConsoleUtil.printSeparator();
        System.out.printf("%-6s %-25s %-15s %-12s %s\n", "ID", "ITEM", "CATEGORY", "PRICE", "AVAILABLE");
        ConsoleUtil.printSeparator();
        for (FoodItem item : menu) {
            String priceStr = String.format("Rs. %.2f", item.getPrice());
            System.out.printf("%-6d %-25s %-15s %-12s %s\n", item.getId(), item.getName(), item.getCategory(), priceStr, item.isAvailable() ? "Yes" : "No");
        }
        ConsoleUtil.printSeparator();
    }

    public FoodItem getFoodById(int id) {
        for (FoodItem item : menu) {
            if (item.getId() == id) return item;
        }
        return null;
    }

    public void addFood(FoodItem item) {
        menu.add(item);
    }

    public void updateFood(FoodItem updatedItem) {
        for (int i = 0; i < menu.size(); i++) {
            if (menu.get(i).getId() == updatedItem.getId()) {
                menu.set(i, updatedItem);
                return;
            }
        }
    }

    public void deleteFood(int id) {
        menu.removeIf(item -> item.getId() == id);
    }

    public void searchFood(String term) {
        System.out.println("\nSearch Results:");
        for (FoodItem item : menu) {
            if (item.getName().toLowerCase().contains(term.toLowerCase())) {
                System.out.println(item.getId() + ". " + item.getName());
            }
        }
    }
}

class Order {
    private String orderId;
    private int tableNumber;
    private List<OrderItem> items;
    private OrderStatus status;
    private double total;

    public Order(String orderId, int tableNumber) {
        this.orderId = orderId;
        this.tableNumber = tableNumber;
        this.items = new ArrayList<>();
        this.status = OrderStatus.PENDING;
        this.total = 0.0;
    }

    public void addItem(OrderItem item) {
        items.add(item);
        calculateTotal();
    }

    private void calculateTotal() {
        total = 0.0;
        for (OrderItem item : items) {
            total += item.getSubtotal();
        }
    }

    public String getOrderId() { return orderId; }
    public int getTableNumber() { return tableNumber; }
    public List<OrderItem> getItems() { return items; }
    public OrderStatus getStatus() { return status; }
    public void setStatus(OrderStatus status) { this.status = status; }
    public double getTotal() { return total; }
}
class OrderItem {
    private FoodItem foodItem;
    private int quantity;
    private double subtotal;

    public OrderItem(FoodItem foodItem, int quantity) {
        this.foodItem = foodItem;
        this.quantity = quantity;
        this.subtotal = foodItem.getPrice() * quantity;
    }

    public FoodItem getFoodItem() { return foodItem; }
    public int getQuantity() { return quantity; }
    public double getSubtotal() { return subtotal; }
}

class OrderService {
    private List<Order> orders;

    public OrderService() {
        orders = new ArrayList<>();
    }

    public Order createOrder(int tableNumber) {
        Order newOrder = new Order(IdGenerator.generateOrderId(), tableNumber);
        orders.add(newOrder);
        return newOrder;
    }

    public List<Order> getAllOrders() {
        return orders;
    }

    public Order getOrder(String orderId) {
        for (Order o : orders) {
            if (o.getOrderId().equalsIgnoreCase(orderId)) return o;
        }
        return null;
    }

    public void displayOrders() {
        ConsoleUtil.printHeader("ORDERS");
        System.out.printf("%-10s %-7s %-7s %-15s %s\n", "Order ID", "Table", "Items", "Total", "Status");
        ConsoleUtil.printSeparator();
        for (Order o : orders) {
            System.out.printf("%-10s %-7d %-7d Rs. %-11.2f %s\n", o.getOrderId(), o.getTableNumber(), o.getItems().size(), o.getTotal() * 1.05, o.getStatus());
        }
        ConsoleUtil.printSeparator();
    }
}
enum OrderStatus {
    PENDING,
    PREPARING,
    READY,
    SERVED,
    COMPLETED,
    CANCELLED
}
class Payment {
    private String transactionId;
    private String orderId;
    private double amount;
    private PaymentMethod method;

    public Payment(String transactionId, String orderId, double amount, PaymentMethod method) {
        this.transactionId = transactionId;
        this.orderId = orderId;
        this.amount = amount;
        this.method = method;
    }

    public String getTransactionId() { return transactionId; }
    public String getOrderId() { return orderId; }
    public double getAmount() { return amount; }
    public PaymentMethod getMethod() { return method; }
}
enum PaymentMethod {
    CASH,
    UPI,
    CARD
}

class PaymentService {
    public void processPayment(Scanner scanner, Order order) {
        double subtotal = order.getTotal();
        double total = subtotal + (subtotal * 0.05);
        
        System.out.printf("Total Amount: Rs. %.2f\n\n", total);
        System.out.println("Select Payment Method:\n");
        System.out.println("1. Cash");
        System.out.println("2. UPI");
        System.out.println("3. Card");
        
        int choice = InputUtil.readInt(scanner, "\nEnter choice: ");
        PaymentMethod method = PaymentMethod.CASH;
        switch(choice) {
            case 2: 
                method = PaymentMethod.UPI; 
                ConsoleUtil.printQRCode("");
                System.out.println("Waiting for scan and payment confirmation...");
                try { Thread.sleep(2500); } catch (Exception e) {}
                break;
            case 3: 
                method = PaymentMethod.CARD; 
                System.out.println("\nPlease tap or insert your card...");
                try { Thread.sleep(2000); } catch (Exception e) {}
                break;
            default:
                System.out.println("\nPlease collect cash from customer...");
                try { Thread.sleep(1000); } catch (Exception e) {}
        }
        
        System.out.println("\nProcessing " + method + " payment...");
        System.out.println("\nPayment successful!");
        System.out.println("\nTransaction ID: " + IdGenerator.generateTransactionId());
        
        order.setStatus(OrderStatus.COMPLETED);
        System.out.println("\nPayment Status: PAID");
    }
}

class ReportService {
    public void generateReport(List<Order> orders, boolean export) {
        StringBuilder sb = new StringBuilder();
        sb.append("\n==================================================\n");
        sb.append("                   SALES REPORT                   \n");
        sb.append("==================================================\n");
        
        int totalOrders = orders.size();
        int completed = 0;
        int cancelled = 0;
        int pending = 0;
        double totalRevenue = 0.0;
        
        Map<String, Integer> itemSales = new HashMap<>();

        for (Order o : orders) {
            if (o.getStatus() == OrderStatus.COMPLETED) {
                completed++;
                totalRevenue += (o.getTotal() * 1.05);
                for (OrderItem item : o.getItems()) {
                    String name = item.getFoodItem().getName();
                    itemSales.put(name, itemSales.getOrDefault(name, 0) + item.getQuantity());
                }
            } else if (o.getStatus() == OrderStatus.CANCELLED) {
                cancelled++;
            } else {
                pending++;
            }
        }

        sb.append("Total Orders       : ").append(totalOrders).append("\n");
        sb.append("Completed Orders   : ").append(completed).append("\n");
        sb.append("Cancelled Orders   : ").append(cancelled).append("\n");
        sb.append("Pending Orders     : ").append(pending).append("\n");
        sb.append(String.format("\nTotal Revenue      : Rs. %.2f\n\n", totalRevenue));
        
        sb.append("Top Selling Items:\n");
        List<Map.Entry<String, Integer>> list = new ArrayList<>(itemSales.entrySet());
        if (list.isEmpty()) {
            sb.append("No items sold yet.\n");
        } else {
            list.sort((a, b) -> b.getValue().compareTo(a.getValue()));
            
            int count = 1;
            for (Map.Entry<String, Integer> entry : list) {
                sb.append(count).append(". ").append(entry.getKey()).append(" (").append(entry.getValue()).append(" sold)\n");
                count++;
                if (count > 3) break;
            }
        }
        
        sb.append("--------------------------------------------------\n");
        
        System.out.println(sb.toString());
        
        if (export) {
            try (FileWriter fw = new FileWriter("sales_report.txt")) {
                fw.write(sb.toString());
                System.out.println("\nReport exported successfully to sales_report.txt");
            } catch (IOException e) {
                System.out.println("\nError exporting report: " + e.getMessage());
            }
        }
    }
}
class RestaurantTable {
    private int tableNumber;
    private int capacity;
    private TableStatus status;

    public RestaurantTable(int tableNumber, int capacity, TableStatus status) {
        this.tableNumber = tableNumber;
        this.capacity = capacity;
        this.status = status;
    }

    public int getTableNumber() { return tableNumber; }
    public int getCapacity() { return capacity; }
    public TableStatus getStatus() { return status; }
    public void setStatus(TableStatus status) { this.status = status; }
}
enum Role {
    ADMIN,
    STAFF,
    CUSTOMER
}

class TableService {
    private List<RestaurantTable> tables;

    public TableService() {
        tables = new ArrayList<>();
        // Initialize 10 tables
        for (int i = 1; i <= 2; i++) tables.add(new RestaurantTable(i, 2, TableStatus.AVAILABLE));
        for (int i = 3; i <= 6; i++) tables.add(new RestaurantTable(i, 4, TableStatus.AVAILABLE));
        for (int i = 7; i <= 10; i++) tables.add(new RestaurantTable(i, 6, TableStatus.AVAILABLE));
        
        tables.get(2).setStatus(TableStatus.OCCUPIED);
        tables.get(4).setStatus(TableStatus.RESERVED);
    }

    public void displayTables() {
        ConsoleUtil.printSeparator();
        System.out.printf("%-10s %-15s %s\n", "TABLE", "CAPACITY", "STATUS");
        ConsoleUtil.printSeparator();
        for (RestaurantTable table : tables) {
            System.out.printf("%-10d %-15d %s\n", table.getTableNumber(), table.getCapacity(), table.getStatus());
        }
        ConsoleUtil.printSeparator();
    }

    public void displayAvailableTables() {
        System.out.println("\nAvailable Tables:\n");
        for (RestaurantTable table : tables) {
            if (table.getStatus() == TableStatus.AVAILABLE) {
                System.out.println(table.getTableNumber() + " - Table " + table.getTableNumber());
            }
        }
        System.out.println();
    }

    public RestaurantTable getTable(int tableNumber) {
        for (RestaurantTable table : tables) {
            if (table.getTableNumber() == tableNumber) return table;
        }
        return null;
    }

    public void setTableStatus(int tableNumber, TableStatus status) {
        RestaurantTable table = getTable(tableNumber);
        if (table != null) table.setStatus(status);
    }
    
    public void addTable(RestaurantTable table) {
        tables.add(table);
    }
}
enum TableStatus {
    AVAILABLE,
    OCCUPIED,
    RESERVED
}
class User {
    private int id;
    private String username;
    private String password;
    private Role role;

    public User(int id, String username, String password, Role role) {
        this.id = id;
        this.username = username;
        this.password = password;
        this.role = role;
    }

    public int getId() { return id; }
    public String getUsername() { return username; }
    public String getPassword() { return password; }
    public Role getRole() { return role; }
}
