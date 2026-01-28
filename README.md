# 🚀 Kurumsal Kütüphane Yönetim Sistemi (LMS)
Spring Boot + React + OAuth2 tabanlı, gerçek hayattaki dijital kütüphane süreçlerini simüle eden **full-stack** bir projedir. Basit CRUD değil; **ödünç verme yaşam döngüsü**, **ceza hesaplama**, **rezervasyon kuyruğu**, **abonelik**, **ödeme** ve **analitik** gibi kurumsal senaryolara odaklanır.

> Not: Bu repoda şu an **backend** (Spring Boot) mevcut. **Frontend (React)** ve bazı “enterprise” modüller proje ilerledikçe eklenecek/olgunlaştırılacak.

---

## 🎯 Öne Çıkan Özellikler (Hedeflenen)

### 🔐 Banka Seviyesi Güvenlik & Kimlik Doğrulama
- **Google OAuth2** ile tek tık giriş
- **JWT** ile stateless oturum yönetimi
- **RBAC (Role Based Access Control)**:
  - **Admin Panel**: kitaplar, kullanıcılar, finans
  - **User Panel**: profil, ödeme/geçmiş
- **Route Protection**: React tarafında Private Routes

### 📚 Akıllı Kitap & Envanter Yönetimi
- **Bulk Upload / Batch Processing** ile toplu kitap ekleme
- **Kopya yönetimi**: Total Copies vs Available Copies
- **Soft Delete**: kayıtlar DB’den silinmez, pasiflenir
- **ISBN doğrulama** ve tekrar kayıtların engellenmesi

### 💳 Ödeme & Finans Modülü
- **Razorpay** entegrasyonu (test anahtarları ile)
- **Ceza ödemeleri**: gecikme cezası (örn. 5/gün) ve ödeme bitmeden yeni ödünç engeli
- **Üyelik abonelikleri**: Silver/Gold planları, plan bitişi/yenileme
- **Transaction History**: başarı/başarısız/tekrar deneme kayıtları

### ⏳ Rezervasyon & Kuyruk Sistemi
- Stok yoksa **waiting queue**
- İade olduğunda **sıradaki kullanıcıya öncelik**
- **Concurrency handling**: aynı anda son kopyayı kapma problemlerinin önlenmesi

### 🔄 Ödünç Verme Yaşam Döngüsü Motoru
- Checkout validasyonları: plan, limit, borç/ceza vb.
- **Scheduler** ile otomatik “Overdue” işaretleme
- Kuyruk varsa **yenileme kısıtı**

### 📊 Analitik Dashboard
- Gelir grafikleri, kullanıcı büyümesi, popüler türler
- Recharts / Chart.js ile görselleştirme

---

## 🧱 Mimari (Modüler Monolith)
Backend, modüler monolith olarak düzenlenmiştir:

```
com.library
├── shared/          # Ortak config/security/exception/response vb.
└── module/
    ├── auth/        # kimlik doğrulama (AuthService, UserDetailsService, AuthResponse)
    ├── user/        # kullanıcı domain (User, UserRepository, UserDTO)
    ├── book/        # kitap domain (CRUD, search, stats)
    └── genre/       # tür domain (CRUD, hiyerarşi)
```

---

## 🛠️ Teknoloji Yığını

### Backend
- **Java**: 21 (projede `pom.xml` ile)
- **Spring Boot**: 4.x (projede `pom.xml` ile)
- **Spring Security** + JWT
- **Spring Data JPA / Hibernate**
- **Validation** (Jakarta Validation)
- **PostgreSQL** (runtime)
- **Maven**
- **Lombok**

### Frontend (Planlanan)
- **React (Vite)**
- **TailwindCSS + MUI**
- **Redux Toolkit**
- **Axios (Interceptors)**

---

## ✅ Gereksinimler
- **Java 21+**
- **Maven** (veya repo içindeki `mvnw` / `mvnw.cmd`)
- **PostgreSQL**

---

## ⚙️ Kurulum (Backend)

### 1) Ortam değişkenleri
Bu proje `src/main/resources/application.yml` içinde `.env.properties` import edecek şekilde ayarlı:

```yml
spring:
  config:
    import: optional:file:.env.properties
```

Proje kök dizinine `.env.properties` oluşturun:

```properties
POSTGRES_URL=jdbc:postgresql://localhost:5432/library
POSTGRES_USER=postgres
POSTGRES_PASSWORD=postgres
```

> İsterseniz doğrudan `application.yml` içine de yazabilirsiniz; ancak `.env.properties` tavsiye edilir.

### 2) Veritabanı
- PostgreSQL’de `library` adında bir DB oluşturun.
- `ddl-auto: update` açık olduğu için tablolar ilk çalıştırmada otomatik oluşturulur.

### 3) Çalıştırma
Windows (PowerShell):

```powershell
cd "d:\spring-boot-projects\library-management-system"
.\mvnw.cmd spring-boot:run
```

Mac/Linux:

```bash
./mvnw spring-boot:run
```

Uygulama varsayılan olarak:
- **Port**: `8080`
- **Context path**: `/api/v1/library`

Örn: `GET /api/v1/library/books`

---

## 🔌 API (Mevcut)

### Books
- `POST /books`
- `POST /books/bulk`
- `GET /books/{id}`
- `GET /books/{id}/isbn`
- `PUT /books/{id}`
- `DELETE /books/{id}` (soft delete)
- `DELETE /books/{id}/permanent` (hard delete)
- `GET /books/search`
- `GET /books/stats`

### Genres
- `POST /genres`
- `GET /genres`
- `GET /genres/{id}`
- `PUT /genres/{id}`
- `DELETE /genres/{id}` (soft delete)
- `DELETE /genres/{id}/hard` (hard delete)
- `GET /genres/top-level`
- `GET /genres/{id}/book-count`

> Not: `application.yml` context-path nedeniyle çağrılar `/api/v1/library/...` ile başlar.

---

## 🔐 Güvenlik Notları
- JWT mekanizması **Authorization: Bearer <token>** başlığıyla çalışır.
- `SecurityConfig` içinde route kuralları tanımlıdır.

---

## 🧪 Test
```bash
mvn test
```

> Bazı ortamlarda `.m2` erişim izinleri nedeniyle test çalıştırırken yetki hatası görülebilir.

---

## 🗺️ Yol Haritası (Kısa)
- OAuth2 (Google) login akışı
- Razorpay ödeme entegrasyonu
- Abonelik planları ve plan yenileme
- Rezervasyon kuyruğu + concurrency kontrolü
- Scheduler: overdue işaretleme + otomatik ceza
- React admin/user panelleri + dashboard grafikleri

---

## 📄 Lisans
Bu proje bir portföy/bitirme projesi olarak tasarlanmıştır. Lisans/dağıtım koşulları eklenebilir.

