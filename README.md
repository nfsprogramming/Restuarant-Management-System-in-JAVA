# 🍽️ Restaurant Management System

A console-based Java application that simulates the full workflow of a restaurant — from staff login and table management through order processing, billing, payment, and sales reporting.

---

## Features

| Module | What it does |
|---|---|
| **Authentication** | Role-based login (Admin / Staff / Customer); session management with logout |
| **Menu Management** | Add, delete, and view food items by ID, name, category, and price |
| **Table Management** | 10 preconfigured tables (2-, 4-, and 6-seat); add tables; set status (Available / Occupied / Reserved) |
| **Order Processing** | Create orders against available tables; add multiple items; track status (Pending → Preparing → Ready → Served → Completed / Cancelled) |
| **Billing** | Itemised bill with 5 % GST; optional export to `bill_<OrderID>.txt` |
| **Payment** | Cash, UPI (ASCII QR + UPI ID), and Card flows; auto-generates a transaction ID |
| **Sales Reporting** | Order summary (total / completed / cancelled / pending), total revenue, and top-3 selling items; optional export to `sales_report.txt` |
| **Input safety** | Type `b` at any numeric prompt to cancel and go back; duplicate ID guards on menu and table creation |

All data is held in memory — no database or external dependencies required.

---

## Tech Stack

- **Language:** Java 17+
- **Build:** plain `javac` (no build tool required)
- **Runtime:** JVM — runs on any OS with Java 17+
- **Data storage:** in-memory (`ArrayList`, `HashMap`)

---

## Prerequisites

- Java Development Kit (JDK) 17 or later

Verify with:
```bash
java --version
```

---

## Installation & Run

```bash
# 1. Clone the repository
git clone https://github.com/nfsprogramming/Restuarant-Management-System-in-JAVA.git
cd "Restuarant-Management-System-in-JAVA"

# 2. Compile
javac Main.java

# 3. Run
java Main
```

> **Windows PowerShell one-liner:** `javac Main.java; java Main`

---

## Demo Credentials

| Role | Username | Password |
|---|---|---|
| Admin | `admin` | `admin123` |
| Staff | `staff` | `staff123` |
| Customer | `customer` | `customer123` |

---

## Usage

After launching, the main menu presents **Login** or **Exit**.

### Admin Dashboard
```
1. Manage Menu      – add / delete / view items
2. Manage Tables    – add tables, change status
3. Manage Orders    – view all orders and update status
4. Billing          – generate and export itemised bills
5. Generate Report  – view sales report on screen
6. Export Report    – save sales_report.txt
7. Logout
```

### Staff Dashboard
```
1. View Menu
2. View Tables      – available tables only
3. Create Order     – pick a table, add food items
4. Manage Orders    – update order status
5. Generate Bill
6. Process Payment  – Cash / UPI / Card
7. Logout
```

### Customer Dashboard
```
1. View Menu
2. Order Food
3. View All Orders
4. Logout
```

**Navigation tip:** enter `b` at any number prompt to cancel the current action without losing your session.

---

## Project Structure

```
Restaurant Management System in JAVA/
├── Main.java          # Entire application (all classes in one file)
├── LICENSE
├── README.md
└── .gitignore
```

> Generated at runtime (not committed): `bill_*.txt`, `sales_report.txt`, `*.class`

### Classes inside `Main.java`

| Class / Enum | Responsibility |
|---|---|
| `Main` | Entry point; all dashboard and flow logic |
| `AuthService` | Login / logout; hardcoded demo users |
| `MenuService` | CRUD on `FoodItem` list |
| `TableService` | CRUD on `RestaurantTable` list |
| `OrderService` | Create / retrieve / display `Order` objects |
| `BillingService` | Build and optionally export itemised bills |
| `PaymentService` | Simulate Cash / UPI / Card payment |
| `ReportService` | Aggregate and export sales data |
| `ConsoleUtil` | Header / separator / ASCII QR printer |
| `InputUtil` | Validated `int`, `double`, `String` reads; `b`-to-cancel |
| `IdGenerator` | Sequential `ORD####` and `TXN#####` IDs |
| `FoodItem` | Model — id, name, category, price, available |
| `Order` | Model — orderId, tableNumber, items, status, total |
| `OrderItem` | Model — FoodItem + quantity + subtotal |
| `Payment` | Model — transactionId, orderId, amount, method |
| `RestaurantTable` | Model — tableNumber, capacity, status |
| `Role` | Enum — ADMIN, STAFF, CUSTOMER |
| `OrderStatus` | Enum — PENDING, PREPARING, READY, SERVED, COMPLETED, CANCELLED |
| `TableStatus` | Enum — AVAILABLE, OCCUPIED, RESERVED |
| `PaymentMethod` | Enum — CASH, UPI, CARD |

---

## Build & Release

### Compile to a runnable JAR

```bash
javac Main.java
jar cfe RestaurantMS.jar Main *.class
java -jar RestaurantMS.jar
```

Download the latest pre-built JAR from the [Releases](https://github.com/nfsprogramming/Restuarant-Management-System-in-JAVA/releases) page.

---

## Known Limitations

- **In-memory only** — all data resets when the application exits; there is no persistence layer.
- **Single file** — all classes live in `Main.java`; suitable for learning and demos.
- **Plaintext passwords** — demo credentials are hardcoded strings; not suitable for production.
- **No concurrency** — single-threaded; one user session at a time.

---

## Contributing

1. Fork the repository.
2. Create a feature branch: `git checkout -b feature/your-feature`
3. Commit your changes: `git commit -m "Add your feature"`
4. Push and open a Pull Request.

---

## License

This project is licensed under the [MIT License](LICENSE).
