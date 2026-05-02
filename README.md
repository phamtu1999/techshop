# Techshop - Enterprise Microservices Ecosystem

Hệ thống thương mại điện tử (E-commerce) quy mô lớn được xây dựng trên kiến trúc Microservices hiện đại, tối ưu cho khả năng mở rộng và hiệu năng cao.

## 📂 Cấu trúc Dự án (Monorepo)

```text
techshop/
├── docs/                      # Tài liệu thiết kế hệ thống, luồng nghiệp vụ & UI/UX
├── frontend/                 # Storefront & Admin (Next.js 15, TailwindCSS v4)
├── bff/                      # Backend For Frontend (Node.js/Express) - Điều phối dữ liệu
├── services/                 # Danh sách các Spring Boot Microservices
│   ├── auth-service/         # Định danh & Bảo mật (JWT)
│   ├── user-service/         # Quản lý hồ sơ người dùng
│   ├── product-service/      # Quản lý Catalog & Tìm kiếm (FTS)
│   ├── inventory-service/    # Quản lý Kho & Đồng bộ tồn kho
│   ├── order-service/        # Nghiệp vụ Đặt hàng (Saga Orchestrator)
│   ├── payment-service/      # Thanh toán (VNPay, Momo)
│   ├── notification-service/ # Thông báo (Email, Push)
│   ├── content-service/      # Quản lý CMS, Banner, Wishlist
│   └── system-service/       # Analytics & Logging hành vi
├── infrastructure/           # Cấu hình Docker, Kafka, Redis, PostgreSQL
└── scripts/                  # Các script tự động hóa khởi chạy hệ thống
```

## 🛠️ Công nghệ chủ chốt (Tech Stack)

- **Backend:** Spring Boot 3.x, Spring Cloud, Spring Security.
- **Frontend:** Next.js (App Router), TailwindCSS, Zustand, Shadcn/ui.
- **BFF:** Node.js, Express, Axios, Redis (Cache).
- **Database:** PostgreSQL (Supabase), Redis.
- **Messaging:** Apache Kafka (Event-driven).
- **DevOps:** Docker, Docker Compose.

## 🚀 Bắt đầu nhanh

Vui lòng tham khảo tài liệu hướng dẫn chi tiết tại:
👉 [Hướng dẫn Chạy Hệ thống (READ.md)](docs/READ.md)

---
© 2024 Techshop Team. All rights reserved.
