# Reflection 1

Pada modul ini, saya telah mengimplementasikan dua fitur baru pada aplikasi e-shop menggunakan Spring Boot, yaitu fitur **Edit Product** dan **Delete Product** yang masing-masing fitur berada pada branch nya masing-masing. Dari proses pengembangan fitur tersebut, saya melakukan evaluasi terhadap penerapan *clean code principles* dan *secure coding practices* yang telah saya pelajari di minggu pertama kuliah ini dan dari Module 01 - Coding Standards juga.

## Clean Code Principles yang Diterapkan

1. **Separation of Concerns (SoC)**  
Aplikasi dibangun menggunakan arsitektur MVC (Model-View-Controller) karena menggunakan Spring Boot.
    - Controller bertanggung jawab menangani HTTP request dan response nya (mirip dengan view pada Django dengan arsitektur MVT).
    - Service berisi logika bisnis aplikasi.
    - Repository menangani pengelolaan data produk.
   
Pemisahan ini membuat kode lebih terstruktur dan mudah di-maintain (dipelihara).

2. **Penamaan Kelas dan Method yang Jelas**
Nama kelas dan method seperti `ProductController`, `ProductService`, `create()`, `findAll()`, `update()`, dan `delete()` sudah mencerminkan fungsinya masing-masing sehingga mudah dipahami dan di maintain.

3. **Single Responsibility Principle**  
Setiap class hanya memiliki satu tanggung jawab utama. Contohnya: `ProductRepository` hanya bertanggung jawab terhadap pengelolaan data produk, tanpa mengandung logika tampilan atau request handling.

4. **Kode Mudah Dibaca dan Tidak Duplikatif**  
Logika bisnis tidak ditulis langsung di controller, melainkan diserahkan dulu ke service untuk ditangani. Hal ini dilakukan untuk menghindari duplikasi kode dan membuat controller tetap sederhana.

## Secure Coding Practices yang Diterapkan

1. **Penggunaan Model Binding**  
Data dari form aku ini akan di handle menggunakan `@ModelAttribute`, sehingga tidak perlu memproses parameter request secara manual. Hal ini mengurangi risiko kesalahan input handling.

2. **Tidak Mengakses Data Secara Langsung dari View**  
View (Thymeleaf) hanya menerima data dari model yang disiapkan oleh controller, sehingga tidak ada logika bisnis yang bocor ke layer tampilan.

3. **Validasi Alur Akses Data**  
Operasi edit dan delete dilakukan berdasarkan `productId`, sehingga setiap perubahan data dapat diidentifikasi dengan jelas karena tiap id itu bersifat unik.

4. **Konfirmasi Aksi Delete**  
Pada fitur delete product, ditambahkan konfirmasi sebelum penghapusan data untuk mencegah penghapusan yang tidak disengaja.

## Evaluasi dan Perbaikan yang Dapat Dilakukan

1. **Belum Ada Validasi Input**  
Saat ini belum terdapat validasi untuk memastikan bahwa `productName` tidak kosong dan `productQuantity` tidak bernilai negatif. Perbaikan dapat dilakukan dengan menambahkan validasi menggunakan annotation seperti `@NotBlank` dan `@Min`.

2. **Penyimpanan Data Masih Menggunakan In-Memory List**  
Data produk masih disimpan dalam `List` di memory, sehingga akan hilang ketika aplikasi di-restart. Ke depannya, aplikasi dapat ditingkatkan dengan menggunakan database dan Spring Data JPA.

3. **Belum Ada Penanganan Error Secara Eksplisit**  
Jika `productId` tidak ditemukan saat edit atau delete, aplikasi belum memberikan feedback khusus. Hal ini dapat diperbaiki dengan menambahkan error handling atau halaman error custom.

4. **Konfirmasi Dengan Menggunakan Modal**
Sekarang ini aku hanya baru mengimplementasikan konfirmasi dengan confirm() saja (misal di saat ingin men-delete suatu product) yang mana ini adalah built-in JavaScript dialog, tetapi kode ini masih bisa diperbagus dengan menggunakan modal untuk konfirmasinya.

## Kesimpulan

Secara keseluruhan, implementasi fitur Edit dan Delete Product telah menerapkan prinsip clean code dan dasar secure coding dengan baik. Struktur kode sudah rapi, mudah dipahami, dan mengikuti pola yang dianjurkan dalam Spring Boot dan dalam perkuliahan Adpro ini. Beberapa perbaikan masih dapat dilakukan untuk meningkatkan dan memperbagus kualitas aplikasi, terutama pada aspek yang telah aku sebutkan diatas.

# Reflection 2

## 1. Pengalaman Menulis Unit Test dan Code Coverage
- Setelah menulis unit test, aku jadi lebih yakin bahwa fitur-fitur yang aku implementasikan di proyek ini semuanya berjalan sesuai harapan.
- Menurut aku Jumlah unit test itu tidak harus banyak, yang penting itu adalah setiap fitur utama harus diuji dan testing nya itu wajib ada positive case (case kalau berhasil) dan negative case (case kalau gagal).
- Untuk melihat apakah unit test yang aku implementasikan dan jalankan ini sudah cukup atau belum adalah dengan melihat code coverage nya saja yg membantu melihat seberapa banyak kode yang diuji.
- 100% code coverage itu gk menjamin kode yang aku buat ini bebas bug, karena bisa saja ada logikanya yg salah dan gk semua edge case itu diuji.
- Jadi Intinya itu adalah unit test + code review tetap sama2 penting.

## 2. Clean Code pada Functional Test
- Menurut aku membuat banyak functional test dengan setup yg sama itu bisa menyebabkan duplikasi kode dan kode sulit di-maintain (dipelihara) gitu.
- Hal tersebut menurunkan kualitas clean code.
- Solusi yang lebih baik: Bisa gunain base class utk setup yg sama dan menghindarin copy-paste biar gk terjadi duplikasi kode.
- Dengan begitu, kode test akan jadi lebih rapi, mudah dibaca, dan mudah dikembangkan gitu.

# Reflection 3 (Module 2)

## 1. Code Quality Issues yang Diperbaiki
Sebelum saya menambahkan code quality tool, saya sudah membuat CI pipeline yang menjalankan unit test secara otomatis setiap kali ada push ke repository menggunakan GitHub Actions. 
Setelah itu, pada tutorial 2 ini saya diminta untuk menambahkan tool code scanning tambahan ke dalam proses CI/CD.
Disini saya memilih menggunakan PMD sebagai tools code analysis saya. PMD iitu digunakan untuk mendeteksi potensi masalah dalam kode seperti bad practice, unused code, kemungkinan bug, dan pelanggaran terhadap coding standard. Intinya PMD ini saya gunakan untuk meningkatkan code quality.
Setelah itu saya membuat workflow baru yang akan menjalankan PMD setiap kali ada push ke semua branch, lalu meng-commit file workflow tersebut ke branch module-2-exercise.
Setelah workflow itu dijalankan, saya melihat hasil analisis dari PMD dan menemukan adanya code quality issue yang terdeteksi (warning/error pada hasil run workflow), contohnya adalah "PMD detected 18 violations.". Dari situ saya mengetahui bahwa ada bagian kode yang tidak sesuai best practice.

Untuk memperbaikinya yang saya lakukan adalah:
- Membaca pesan error/warning dari hasil run PMD di GitHub Actions.
- Mengidentifikasi bagian kode yang bermasalah.
- Memperbaiki kode tersebut (misalnya dengan merapikan struktur atau mengikuti best practice yang direkomendasikan).
- Melakukan commit terpisah khusus untuk perbaikan tersebut.
- Menjalankan ulang workflow dan memastikan issue tersebut sudah tidak muncul lagi.
- Nahh dengan cara ini, saya memastikan bahwa kode menjadi lebih bersih dan sesuai standar kualitas.

Disini code quality issue yang aku terapkan ada 1, yaitu fix ImmutableField:
- Disini saya mendeteksi error "Field 'productData' may be declared final"
- Setelah itu saya langsung pergi ke ProductRepository dan mengubah kode yang tadinya
  private List<Product> productData = new ArrayList<>(); --> private List<Product> productData = new ArrayList<>();

## 2. Apakah Sudah Memenuhi Continuous Integration dan Continuous Deployment?
Menurut saya, implementasi yang saya buat sudah memenuhi konsep Continuous Integration (CI) dan Continuous Deployment (CD).
- Pertama, setiap kali ada push ke repository, GitHub Actions secara otomatis menjalankan unit test dan juga code analysis menggunakan PMD. Ini sudah memenuhi konsep Continuous Integration krn setiap perubahan kode langsung diuji dan dianalisis secara otomatis.
- Kedua, setelah branch module-2-exercise digabungkan ke branch main, aplikasi secara otomatis ter-deploy ke PaaS (Koyeb). Proses deployment ini berjalan tanpa perlu dilakukan secara manual setiap kali ada perubahan di branch utama, sehingga sudah memenuhi konsep Continuous Deployment.

Dengan adanya pipeline ini, proses testing, pengecekan kualitas kode, dan deployment menjadi otomatis dan lebih terstruktur.