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
