# 🚀 Kurumsal Kütüphane Yönetim Sistemi (LMS)
Spring Boot + React + OAuth2 tabanlı, gerçek hayattaki dijital kütüphane süreçlerini simüle eden **full-stack** bir projedir. Basit CRUD değil; **ödünç verme yaşam döngüsü**, **ceza hesaplama**, **rezervasyon kuyruğu**, **abonelik**, **ödeme** ve **analitik** gibi kurumsal senaryolara odaklanır.

> Not: Bu repoda şu an **backend** (Spring Boot) mevcut. **Frontend (React)** ve bazı “enterprise” modüller proje ilerledikçe eklenecek/olgunlaştırılacak.

---

## Repo tarama özeti (mevcut durum)
Aşağıdaki bilgiler projede bulunan dosyalara göre otomatik tespit edilmiştir:
- Java sürümü: 21 (pom.xml -> <java.version>)
- Spring Boot sürümü: 4.0.2 (pom.xml parent)
- Uygulama giriş noktası: `com.library.LibraryManagementSystemApplication`
- `src/main/resources/application.yml` içinde:
  - Port: 8080
  - Context path: `/api/v1/library`
  - Datasource bağlantısı `spring.datasource.url` için environment property `POSTGRES_URL` kullanılıyor
  - JPA `ddl-auto: update` (uygulama ilk çalıştırmada tabloları oluşturacak)
  - Flyway: disabled (`spring.flyway.enabled: false`)
- Proje kökünde `.env.properties` dosyası olabileceği görünüyor (NOT: repo içinde bir `.env.properties` mevcut — bu dosyanın içinde gizli bilgiler bulunmamalıdır; aşağıda eylem önerisi var)
- Maven wrapper mevcut: `mvnw` / `mvnw.cmd` (wrapper versiyonu ve dağıtım bilgisi .mvn/wrapper içinde)

Bu README içeriği yukarıdaki gerçek repo durumuna göre güncellendi.

---

## 🎯 Öne Çıkan Özellikler (Hedeflenen)

### 🔐 Banka Seviyesi Güvenlik & Kimlik Doğrulama
- **Google OAuth2** ile tek tık giriş (planlanan)
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
- **Razorpay** entegrasyonu (planlanan)
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
- Recharts / Chart.js ile görselleştirme (frontend tarafı planlı)

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
- **Java**: 21
- **Spring Boot**: 4.x (projede 4.0.2 olarak tanımlı)
- **Spring Security** + JWT
- **Spring Data JPA / Hibernate**
- **Validation** (Jakarta Validation)
- **PostgreSQL** (runtime)
- **Maven** (wrapper included)
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

Aşağıdaki adımlar, repo içindeki `application.yml` ve mevcut `.env.properties` kullanımına göre düzenlenmiştir.

### 1) Ortam değişkenleri / konfigürasyon
Proje `src/main/resources/application.yml` içinde harici bir properties dosyası yükleyecek şekilde ayarlı:

```yml
spring:
  config:
    import: optional:file:.env.properties
```

Proje kökünde bir `.env.properties` oluşturun veya mevcut olanı düzenleyin. (NOT: repo kökünde bir `.env.properties` bulunabilir; içinde gerçek e-posta/parola gibi hassas veriler varsa bunları versiyon kontrolünden kaldırın.)

Örnek içeriği (gizli bilgileri buraya koymayın — sadece şablon):

```properties
# database
POSTGRES_URL=jdbc:postgresql://localhost:5432/librarydb
POSTGRES_USER=postgres
POSTGRES_PASSWORD=your_db_password

# mail (örnek)
MAIL_APP_USERNAME=your@mail.com
MAIL_APP_PASSWORD=your-mail-app-password

# initial admin (örnek)
ADMIN_EMAIL=admin@example.com
ADMIN_PASSWORD=ChangeMe123!
ADMIN_FULL_NAME=Admin User
```

Projeye yardımcı olması için kök dizine bir `.env.properties.example` eklendi — kendi kopyanızı bu şablona göre oluşturun.

### Güvenlik önerisi
- `.env.properties` içinde gerçek parolalar, API anahtarları veya e-posta şifreleri saklamayın ve kesinlikle versiyon kontrolüne (git) commit etmeyin.
- `.gitignore` içine `.env.properties` ekleyin.

### 2) Veritabanı
- PostgreSQL’de varsayılan olarak `librarydb` adında bir DB oluşturmanız beklenir (yukarıdaki `POSTGRES_URL` örneğine göre). Eğer farklı bir isim kullanıyorsanız `.env.properties` içindeki `POSTGRES_URL` değerini güncelleyin.
- Uyarı: `spring.jpa.hibernate.ddl-auto: update` ayarlı olduğu için tablolar ilk çalıştırmada otomatik oluşturulur.
- Flyway migrationlar proje içinde bulunmakla birlikte (`src/main/resources/db/migration`), `spring.flyway.enabled` şu anda `false` olarak ayarlı.

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

Alternatif: Jar paketleyip çalıştırma:

```powershell
# package (Windows)
.\mvnw.cmd -DskipTests package
# sonra
java -jar target/library-management-system-0.0.1-SNAPSHOT.jar
```

Uygulama varsayılan olarak:
- **Port**: `8080`
- **Context path**: `/api/v1/library`

Örn: `GET http://localhost:8080/api/v1/library/books`

---

## 🔌 API (Mevcut - özet)
Aşağıda ana endpoint türleri listelenmiştir; hepsi `context-path` ile birlikte kullanılmalıdır (`/api/v1/library/...`).

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

> Not: Gerçek route güvenlik kuralları `SecurityConfig` içinde tanımlıdır; bazı endpointler authentication/authorization gerektirebilir.

---

## 🔐 Güvenlik ve test notları
- JWT mekanizması için `Authorization: Bearer <token>` başlığı kullanılır.
- Proje içinde testler (`src/test`) mevcut. Basit bir context-load testi vardır.

## 🧪 Test
Projede bulunan testleri çalıştırmak için:

```bash
./mvnw test        # Linux/mac
mvnw.cmd test      # Windows PowerShell içinde .\mvnw.cmd test
```

> Bazı ortamlarda `.m2` klasör izinleri veya ağ erişimi nedeniyle test/derleme sorunları görülebilir.

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

---

## Son adımlar / öneriler
- Eğer repo içinde `.env.properties` gibi hassas bilgileri içeren bir dosya varsa, bunları git geçmişinden temizleyin ve `.gitignore` ile dışlayın.
- Bir `.env.properties.example` dosyası ile gerekli alanları gösterin (gerçek parolalar yok).
- Flyway migration'ları kullanmak isterseniz `spring.flyway.enabled: true` yapın ve `V2` vb. migration'ları ekleyin.
