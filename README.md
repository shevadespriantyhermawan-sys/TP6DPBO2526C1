# 🐤 Flappy Bird - Java Swing GUI Game

> **Tugas Praktikum Modul 6 & Modul 7**  
> **Mata Kuliah:** Desain dan Pemrograman Berorientasi Objek (DPBO)  
> **Program Studi:** Ilmu Komputer - FPMIPA Universitas Pendidikan Indonesia (UPI)  
> **Angkatan:** 2024 / Tahun Ajaran 2025/2026

---

## Janji

Saya Sheva Desprianty Hermawan dengan NIM [2511645] mengerjakan Tugas Praktikum 6 pada Mata Kuliah Desain dan Pemrograman Berorientasi Objek (DPBO) untuk keberkahan-Nya maka saya tidak melakukan kecurangan seperti yang telah dispesifikasikan. Aamiin


## 📌 Deskripsi Projek

Projek ini adalah rekreasi dari game populer **Flappy Bird** berbasis desktop yang dibangun menggunakan **Java Swing GUI**. Projek ini dibuat sebagai pemenuhan Tugas Praktikum yang menggabungkan konsep dasar *Game Development* (Game Loop, Rendering Grafis, Event Driven Programming, Fisika Permainan) dengan arsitektur **Object-Oriented Programming (OOP)**.

---

## 🛠️ Spesifikasi Fitur

### 📋 Fitur Wajib (Modul 6, Modul 7 & Spesifikasi TP)
- [x] **Inisialisasi Window Game (`JFrame`)**: Menampilkan jendela utama berukuran $360 \times 640$ piksel.
- [x] **Animasi & Physics (Game Loop)**: Pergerakan halus $60\text{ FPS}$ dengan kalkulasi posisi bertahap.
- [x] **Simulasi Gravitasi**: Pemain (`Player`) akan otomatis jatuh jika tidak melompat.
- [x] **Obstacle Spawning**: Rintangan pipa (`Pipe`) muncul otomatis secara acak (*random height*) setiap 1.5 detik.
- [x] **Dynamic Collision Detection**: Deteksi benturan (*AABB Bounding Box*) antara karakter pemain dengan pipa atas, pipa bawah, maupun batas bawah layar.
- [x] **Game Over State**: Permainan otomatis berhenti (*freeze*) ketika terjadi tabrakan atau pemain jatuh.
- [x] **Restart System**: Fitur untuk mengulang permainan dari awal dengan menekan tombol **`R`** pada keyboard setelah *Game Over*.
- [x] **Score Counter**: Menampilkan skor secara real-time di layar utama yang bertambah `+1` setiap kali pemain berhasil melewati rintangan pipa.
- [x] **Main Menu Interface**: Layar pembuka awal berupa GUI yang berisi tombol **Start Game** dan tombol **Exit**.

### 🎁 Fitur Bonus (+20)
- [x] **Audio & Sound Effects**: Integrasi efek suara (*Sound Effects*) menggunakan library `javax.sound.sampled`:
  - 🔊 Suara melompat (`jump.wav`) saat tombol `SPACE` ditekan.
  - 🔔 Suara perolehan skor (`score.wav`) saat berhasil melewati pipa.
  - 💥 Suara benturan (`hit.wav`) saat terjadi *Game Over*.

---

## 🏗️ Desain & Arsitektur Program

Program ini dirancang menggunakan arsitektur **Model-View-Controller (MVC)** untuk memisahkan antara logika permainan, antarmuka pengguna, dan data entitas.

```text
[App.java]  --> (Entry Point & Main Menu GUI)
    │
    ├──> [Logic.java] <─── (Controller / Game Engine & KeyListener)
    │        │
    │        ├──> [Player.java]  (Model: Entitas Burung)
    │        ├──> [Pipe.java]    (Model: Entitas Pipa)
    │        └──> Sound System   (Audio Loader)
    │
    └──> [View.java]  <─── (View / Canvas JPanel Rendering)
```

### 🧩 Penjelasan Detail Setiap Class

| Nama Class | Tipe / Pattern | Tanggung Jawab & Deskripsi Fungsi |
| :--- | :--- | :--- |
| **`App.java`** | Main Entry | Berisi method `main()`. Mengatur pembentukan `JFrame` dan membangun UI **Main Menu** (tombol *Start Game* dan *Exit*). |
| **`Player.java`** | Model | Kelas entitas yang menyimpan data pemain: posisi (`posX`, `posY`), dimensi (`width`, `height`), gambar sprite, dan kecepatan jatuh/melompat (`velocityY`). |
| **`Pipe.java`** | Model | Kelas entitas rintangan pipa: menyimpan posisi, ukuran, sprite pipa (atas/bawah), kecepatan pergerakan horizontal (`velocityX`), serta *flag* `passed` untuk penanda skor. |
| **`Logic.java`** | Controller / Engine | Mengelola seluruh aturan game: *game loop* ($60\text{ FPS}$ `Timer`), *spawner* pipa, kalkulasi fisika gravitasi, deteksi tabrakan AABB, penanganan input keyboard (`KeyListener`), penambahan skor, serta efek suara audio. |
| **`View.java`** | View / UI Canvas | Subclass dari `JPanel` yang merender objek visual ke layar via `paintComponent()`: menggambar sprite pemain, daftar rintangan pipa, teks *Score Counter*, dan overlay *Game Over*. |

---

## 🔄 Penjelasan Alur Permainan (Flowchart Concept)

```text
       ┌────────────────────────┐
       │   Jalankan App.java    │
       └───────────┬────────────┘
                   ▼
       ┌────────────────────────┐
       │       MAIN MENU        │
       │  [Start Game] [Exit]   │
       └───────────┬────────────┘
                   │ Klik "Start Game"
                   ▼
┌──────────────────────────────────────────────┐
│                  GAME LOOP                   │
├──────────────────────────────────────────────┤
│ 1. Player jatuh akibat Gravitasi             │
│ 2. Tekan SPACE  --> Player Melompat (-10 Y)  │
│ 3. Pipa bergerak dari kanan ke kiri          │
│ 4. Lewati Pipa  --> Skor +1                  │
└──────────────────┬───────────────────────────┘
                   │
         Bentur Pipa / Fall Out?
                   │
        ┌──────────┴──────────┐
        │                     │
      ( YA )                ( TIDAK )
        │                     │
        ▼                     └───> [Kembali ke Game Loop]
┌────────────────────────┐
│       GAME OVER        │
│  - Hentikan Timer      │
│  - Mainkan Sound Hit   │
│  - Tampilkan Teks UI   │
└───────────┬────────────┘
            │
  Tekan 'R' pada Keyboard
            │
            ▼
┌────────────────────────┐
│      RESTART GAME      │
│  - Reset Posisi Player │
│  - Reset Skor = 0      │
│  - Bersihkan Pipa      │
└───────────┬────────────┘
            │
            └───> [Kembali ke Game Loop]
```

---

## ⌨️ Kontrol Keyboard

| Tombol | Kondisi | Aksi / Fungsi |
| :---: | :---: | :--- |
| **`SPACE`** | Game Berjalan | Membuat karakter burung melompat ke atas. |
| **`R`** | Game Over | Mereset permainan dan memulainya kembali dari awal. |
| **Mouse Click** | Main Menu | Mengklik tombol **Start Game** atau tombol **Exit**. |

---

## 📁 Struktur Repositori

```text
TP6DPBO2526C1/
├── Dokumentasi/                    # Folder media pengujian & dokumentasi
│   ├── demo_gameplay.mp4           # Video rekaman jalannya game lengkap
│   ├── screenshot_menu.png         # Screenshot tampilan Main Menu
│   ├── screenshot_gameplay.png     # Screenshot tampilan saat bermain
│   └── screenshot_gameover.png     # Screenshot tampilan saat Game Over
│
├── FlappyBird/                     # Root folder project Java
│   ├── src/
│   │   ├── assets/                 # Folder package resource asset
│   │   │   ├── background.png      # Gambar background game
│   │   │   ├── bird.png            # Sprite burung (player)
│   │   │   ├── lowerPipe.png       # Sprite pipa bagian bawah
│   │   │   ├── upperPipe.png       # Sprite pipa bagian atas
│   │   │   ├── jump.wav            # Efek suara melompat (Bonus)
│   │   │   ├── score.wav           # Efek suara perolehan skor (Bonus)
│   │   │   └── hit.wav             # Efek suara benturan/Game Over (Bonus)
│   │   │
│   │   ├── App.java                # Entry point & Main Menu GUI
│   │   ├── Logic.java              # Controller & Game Logic
│   │   ├── Pipe.java               # Model Class Pipe
│   │   ├── Player.java             # Model Class Player
│   │   └── View.java               # View Canvas JPanel
│   └── FlappyBird.iml              # File konfigurasi IntelliJ IDEA
│
└── README.md                       # Dokumentasi resmi projek
```

---

## 📸 Dokumentasi Program

Seluruh dokumentasi visual dan rekaman pengujian aplikasi disimpan secara lengkap pada folder **`Dokumentasi/`**:

1. **Layar Main Menu**: Menampilkan judul game beserta tombol interaktif *Start Game* dan *Exit*.
2. **Gameplay & Running Game**: Menunjukkan pergerakan burung, pergerakan pipa acak, serta perhitungan skor di pojok kiri atas.
3. **Kondisi Game Over & Restart**: Menunjukkan saat terjadi benturan, berhentinya pergerakan game, serta eksekusi tombol `R` untuk me-restart permainan.

---

## 🚀 Cara Menjalankan Project

1. **Clone Repositori**:
   ```bash
   git clone https://github.com/USERNAME_KAMU/TP6DPBO2526C1.git
   ```
2. **Buka Project**:
   * Buka **IntelliJ IDEA** (atau IDE pilihan kamu).
   * Pilih menu **File > Open**, lalu arahkan ke folder `FlappyBird`.
3. **Pastikan Resource Asset Terbaca**:
   * Pastikan folder `src/assets` memuat file `.png` dan `.wav` secara lengkap.
4. **Jalankan Aplikasi**:
   * Buka file `src/App.java`.
   * Klik tombol **Run** (`Shift + F10`) atau klik kanan `App.java` $\rightarrow$ **Run 'App.main()'**.