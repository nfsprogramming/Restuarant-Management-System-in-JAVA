# Restaurant Management System — v1.0.0

A console-based Java restaurant management application with role-based access, full order lifecycle management, billing with GST, and multi-method payment simulation.

## What's Included

- **Role-based authentication** — Admin, Staff, and Customer dashboards
- **Menu management** — add, delete, and view food items (with duplicate-ID guard)
- **Table management** — 10 preconfigured tables; add new tables; set status (Available / Occupied / Reserved)
- **Order processing** — create orders, add items, track status through Pending → Preparing → Ready → Served → Completed / Cancelled
- **Billing** — itemised bill with 5 % GST; export to `bill_<OrderID>.txt`
- **Payment simulation** — Cash, UPI (ASCII QR code), and Card flows; unique transaction IDs
- **Sales reporting** — total/completed/cancelled orders, revenue, top-3 selling items; export to `sales_report.txt`
- **Input safety** — type `b` at any numeric prompt to cancel without losing your session

## Installation

**Prerequisites:** Java 17 or later (`java --version` to verify)

```bash
# Download RestaurantMS.jar from this release, then run:
java -jar RestaurantMS.jar
```

## Demo Credentials

| Role     | Username   | Password      |
|----------|------------|---------------|
| Admin    | `admin`    | `admin123`    |
| Staff    | `staff`    | `staff123`    |
| Customer | `customer` | `customer123` |

## Checksums (SHA-256)

```
2A596B85D3249BD03764CA87C4F8AEA8B5B4FDC4DD2D2CA73339A2886497EA10  RestaurantMS.jar
```

Verify on Windows:
```powershell
(Get-FileHash RestaurantMS.jar -Algorithm SHA256).Hash
```
Verify on Linux/macOS:
```bash
sha256sum RestaurantMS.jar
```

## Known Limitations

- All data is in-memory only — resets on exit (no persistence layer)
- Single-threaded; one user session at a time
- Plaintext demo passwords — not for production use
