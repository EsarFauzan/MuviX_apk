# MuviX - Android Java XML MVC Starter

Starter project aplikasi nonton film dengan:
- Java untuk logic
- XML untuk UI
- MVC: Model, View, Controller
- Glide untuk load gambar
- Room Database untuk history dan subscribe
- Material Components untuk UI modern
- HttpURLConnection untuk ambil data API tanpa Retrofit

## API
Endpoint default:
https://68ff8dfbe02b16d1753e765d.mockapi.io/film

Kalau field API berbeda, ubah mapping di:
`app/src/main/java/com/muvix/app/model/MovieApiClient.java`

## Cara pakai
1. Extract ZIP ini.
2. Buka folder `MuviX_MVC_Starter` di Android Studio.
3. Tunggu Gradle sync.
4. Jalankan app.
5. Kalau API kosong/error, app otomatis pakai SampleData dulu.

## Struktur MVC
- Model: `model/`
  - Movie.java
  - MovieApiClient.java
  - Room Database, DAO
- Controller: `controller/`
  - MovieController.java
- View: `view/`
  - Activity
  - Adapter
  - XML Layout

## Dokumentasi Arsitektur MVC Aplikasi MuviX

### 1. Deskripsi Aplikasi
MuviX adalah aplikasi streaming film berbasis Android yang dibuat menggunakan Java dan XML. Aplikasi ini menyediakan fitur daftar film, detail film, simulasi pemutar film, riwayat menonton, subscribe film, jadwal tayang, pencarian film, dan halaman profil pengguna.

Aplikasi ini menggunakan arsitektur MVC agar struktur kode lebih rapi dan mudah dikembangkan.

### 2. Teknologi yang Digunakan
- Java sebagai bahasa pemrograman utama.
- XML untuk membuat desain antarmuka aplikasi.
- Glide untuk menampilkan gambar poster dan banner film.
- Room Database untuk menyimpan data history dan subscribe secara lokal.
- Material Components untuk membuat tampilan UI lebih modern.
- HttpURLConnection untuk mengambil data film dari REST API MockAPI.
- RecyclerView untuk menampilkan daftar film dalam bentuk list dan grid.

### 3. Arsitektur MVC
#### A. Model
Model bertugas mengelola data aplikasi, baik dari API maupun database lokal.

File yang termasuk Model:
- `Movie.java`
- `MovieDao.java`
- `AppDatabase.java`
- `MovieApiClient.java`
- `SampleData.java`

Fungsi Model:
- Menyimpan struktur data film.
- Mengambil data film dari REST API.
- Menyimpan riwayat menonton ke Room Database.
- Menyimpan data subscribe film.
- Mengambil data history dan subscribe.

#### B. View
View bertugas menampilkan antarmuka aplikasi kepada pengguna.

File yang termasuk View:
- `MainActivity.java`
- `DetailActivity.java`
- `PlayerActivity.java`
- `ScheduleActivity.java`
- `HistoryActivity.java`
- `SubscribeActivity.java`
- `ProfileActivity.java`
- Semua file layout XML di folder `res/layout`

Fungsi View:
- Menampilkan halaman beranda.
- Menampilkan detail film.
- Menampilkan simulasi player.
- Menampilkan jadwal tayang.
- Menampilkan history.
- Menampilkan subscribe.
- Menampilkan profile pengguna.

#### C. Controller
Controller bertugas menghubungkan View dengan Model.

File Controller:
- `MovieController.java`

Fungsi Controller:
- Mengambil data film dari API melalui `MovieApiClient`.
- Mengirim data film ke Activity.
- Menyimpan film ke history.
- Menyimpan dan menghapus subscribe.
- Mengambil data history dari database.
- Mengambil data subscribe dari database.

### 4. Alur Kerja Aplikasi
1. User membuka aplikasi MuviX.
2. `MainActivity` meminta data film ke `MovieController`.
3. `MovieController` mengambil data dari `MovieApiClient`.
4. Data film ditampilkan di halaman Home menggunakan RecyclerView.
5. User memilih film dan masuk ke `DetailActivity`.
6. Jika user menekan tombol Tonton, aplikasi membuka `PlayerActivity`.
7. Film yang ditonton disimpan ke Room Database sebagai history.
8. Jika user menekan Subscribe, film disimpan ke database sebagai film subscribe.
9. Halaman History dan Subscribe mengambil data dari Room Database.
10. Halaman Profile menampilkan total history, subscribe, dan aktivitas terakhir.

### 5. Fitur Aplikasi
- Menampilkan daftar film dari REST API.
- Search film berdasarkan judul, genre, atau episode.
- Detail film.
- Simulasi player film.
- Menyimpan riwayat menonton.
- Menyimpan film subscribe.
- Jadwal tayang film.
- Profile dengan statistik pengguna.
- Empty state saat data kosong.

### 6. Kesimpulan
Aplikasi MuviX menerapkan arsitektur MVC dengan pemisahan antara Model, View, dan Controller. Struktur ini membuat aplikasi lebih mudah dipahami, dirawat, dan dikembangkan. Dengan dukungan Room Database, Glide, Material Components, RecyclerView, dan REST API MockAPI, aplikasi ini dapat menampilkan data film secara dinamis dan menyimpan aktivitas pengguna secara lokal.

## Checklist Testing Final

Home:
- [ ] Film dari MockAPI muncul
- [ ] Search film berfungsi
- [ ] Klik film masuk Detail

Detail:
- [ ] Banner muncul
- [ ] Tombol Tonton masuk Player
- [ ] Tombol Subscribe berubah jadi Subscribed

Player:
- [ ] Poster/banner muncul
- [ ] Progress berjalan
- [ ] Film masuk History

History:
- [ ] Film yang ditonton muncul
- [ ] Klik item masuk Player
- [ ] Hapus semua history berfungsi
- [ ] Subscribe tidak ikut hilang

Subscribe:
- [ ] Film subscribe muncul
- [ ] Klik item masuk Detail
- [ ] Unsubscribe berfungsi

Jadwal:
- [ ] Filter Hari Ini berfungsi
- [ ] Filter Besok berfungsi
- [ ] Filter Minggu Ini berfungsi
- [ ] Klik film masuk Detail

Profile:
- [ ] Total history berubah
- [ ] Total subscribe berubah
- [ ] Aktivitas terakhir muncul
- [ ] Klik aktivitas masuk Player
