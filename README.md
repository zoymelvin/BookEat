# BookEat

BookEat adalah aplikasi Android yang dirancang untuk menjembatani pelanggan dengan restoran. Aplikasi ini memiliki dua peran utama: satu untuk pelanggan (memesan makanan dan meja) dan satu lagi untuk pemilik restoran (mengelola menu, meja, dan pesanan).

## Fitur Utama

Berdasarkan analisis file project, aplikasi ini memiliki fungsionalitas sebagai berikut:

  * **Autentikasi Pengguna:**

      * Sistem registrasi dan login yang terpisah untuk "Pelanggan" dan "Restoran".
      * Menggunakan **Firebase Authentication** untuk mengelola sesi pengguna.
      * Fitur "Lupa Password" melalui email.

  * **Fitur Sisi Pelanggan (Customer):**

      * **Membuat Pesanan:** Pelanggan dapat membuat pesanan baru.
      * **Pemilihan Meja:** Memilih meja yang tersedia di restoran.
      * **Pemilihan Menu:** Menambahkan item menu ke keranjang.
      * **Pembayaran:** Integrasi dengan payment gateway **Midtrans** untuk proses checkout.
      * **Pelacakan Pesanan:** Melihat status pesanan (diproses, selesai).
      * **Struk Digital:** Melihat detail struk setelah pesanan selesai.
      * **Manajemen Profil:** Mengedit data profil pengguna.

  * **Fitur Sisi Restoran (Resto):**

      * **Dashboard:** Tampilan utama untuk mengelola pesanan yang masuk (diproses) dan riwayat pesanan.
      * **Manajemen Menu:** Fungsionalitas CRUD (Create, Read, Update, Delete) untuk menu restoran.
      * **Manajemen Meja:** Mengelola ketersediaan dan daftar meja.
      * **Manajemen Profil:** Mengedit data profil restoran.

## Teknologi yang Digunakan

Project ini dibangun menggunakan tumpukan teknologi Android modern:

  * **Bahasa:** [Kotlin](https://kotlinlang.org/)
  * **Arsitektur & UI:**
      * **AndroidX** (AppCompat, ConstraintLayout, RecyclerView, ViewPager2)
      * **Material Design** (com.google.android.material)
      * **View Binding** (diaktifkan di `buildFeatures`)
      * **Navigation Component** (Untuk alur navigasi antar Fragment)
  * **Backend & Database (BaaS):**
      * **Firebase Platform (BOM)**
          * **Firebase Authentication** (Autentikasi email/password)
          * **Firebase Realtime Database** (Database NoSQL real-time)
          * **Firebase Storage** (Menyimpan file seperti gambar menu)
  * **Pembayaran:**
      * **Midtrans UIKIt** (Integrasi Payment Gateway)
  * **Utilities:**
      * **Glide** (Untuk memuat dan menampilkan gambar)
      * **Kotlin Coroutines** (Manajemen asynchronous)

## Prasyarat Instalasi

Sebelum Anda dapat membangun dan menjalankan project ini, pastikan Anda memiliki:

1.  **Android Studio** (Rekomendasi versi terbaru, misal: Hedgehog atau Iguana).
2.  **JDK 8** atau yang lebih baru (project dikonfigurasi untuk `jvmTarget = "1.8"`).
3.  **Akun Firebase:**
      * Project Firebase yang sudah di-setup.
      * File `google-services.json` yang valid dari project Firebase Anda, ditempatkan di direktori `app/`.
      * Layanan (Authentication, Realtime Database, Storage) harus diaktifkan.
4.  **Akun Midtrans:**
      * Kunci API (Client & Server Key) untuk mode **Sandbox** dari dashboard Midtrans.

## Susunan Project

Struktur file utama dalam project ini diatur sebagai berikut:

```
BookEat/
├── app/
│   ├── src/
│   │   ├── main/
│   │   │   ├── java/com/jif/bookeat/
│   │   │   │   ├── activity/     (Contoh: LoginPage, RegisterPage, DashboardResto, BuatPesanan)
│   │   │   │   ├── fragment/     (Contoh: HalProses, HalRiwayat, MilihMeja, MilihMenu)
│   │   │   │   ├── adapter/      (Contoh: DaftarMenuAdapter, KelolaMejaAdapter, MilihMejaAdapter)
│   │   │   │   └── model/        (Contoh: DaftarMenuItem, KMejaItem, KeranjangItem, Pesanan)
│   │   │   ├── res/
│   │   │   │   ├── layout/       (File XML untuk layout Activity & Fragment)
│   │   │   │   ├── drawable/     (Ikon dan aset gambar)
│   │   │   │   └── navigation/   (Grafik navigasi untuk Navigation Component)
│   │   │   └── AndroidManifest.xml
│   ├── build.gradle.kts          (Konfigurasi build level aplikasi)
│   └── google-services.json      (File konfigurasi Firebase - *Perlu ditambahkan manual*)
├── build.gradle.kts              (Konfigurasi build level project)
└── settings.gradle.kts           (Pengaturan project)
```

## Contoh Penggunaan (Instalasi)

Untuk menjalankan aplikasi ini secara lokal:

1.  **Clone** repositori ini:
    ```sh
    https://github.com/zoymelvin/BookEat
    ```
2.  **Buka** project di Android Studio.
3.  **Siapkan Firebase:**
      * Buka [Firebase Console](https://console.firebase.google.com/) dan buat project baru.
      * Tambahkan aplikasi Android baru dengan nama paket (applicationId) `com.jif.bookeat`.
      * Unduh file `google-services.json` yang dihasilkan.
      * Tempatkan file `google-services.json` tersebut ke dalam direktori `BookEat/app/`.
      * Aktifkan **Authentication** (Email/Password), **Realtime Database** (atur rules ke mode tes jika perlu), serta **Storage**.
4.  **Siapkan Midtrans:**
      * Login ke [Dashboard Midtrans Sandbox](https://www.google.com/search?q=https://dashboard.sandbox.midtrans.com/).
      * Dapatkan **Client Key** dan **Server Key** Anda.
      * Masukkan kunci ini di tempat yang sesuai dalam kode (kemungkinan besar di dalam file `BayarMidtrans.kt` atau file constants lainnya).
5.  **Sync Gradle** dan **Build Project**.
6.  **Run** aplikasi pada emulator atau perangkat Android fisik (Min SDK 24).
