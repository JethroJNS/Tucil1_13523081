<h1 align="center">Tugas Kecil 1 IF2211 Strategi Algoritma</h1>
<h3 align="center">Penyelesaian IQ Puzzler Pro dengan Algoritma Brute Force</h3>

## Daftar Isi

- [Deskripsi](#deskripsi)
- [Kebutuhan Sistem](#kebutuhan-sistem)
- [Struktur](#struktur)
- [Cara Menjalankan](#cara-menjalankan)
- [Pengembang](#pengembang)

## Deskripsi

Program ini adalah solver untuk permainan IQ Puzzler Pro, di mana pengguna dapat memberikan ukuran papan permainan dan piece puzzle dalam format file, dan program akan mencoba menemukan solusi untuk menyusun piece tersebut agar memenuhi papan sepenuhnya.

Program bekerja dengan menggunakan algoritma brute force dengan proses backtracking untuk mengevaluasi semua kemungkinan penempatan piece hingga ditemukan solusi yang valid.

### Kebutuhan Sistem

* Java 8 atau versi yang lebih baru
* Sistem operasi Windows, macOS, atau Linux

### Struktur
```ssh
├── bin
│   └── IQPuzzlerPro.class
├── doc
│   └── Tucil1_K2_13523081_Jethro Jens Norbert Simatupang.pdf
├── src
│   └── IQPuzzlerPro.java
├── test
│   ├── input
│   │   ├─ input1.txt
│   │   ├─ input10.txt
│   │   ├─ input2.txt
│   │   ├─ input3.txt
│   │   ├─ input4.txt
│   │   ├─ input5.txt
│   │   ├─ input6.txt
│   │   ├─ input7.txt
│   │   ├─ input8.txt
│   │   └─ input9.txt
│   └── output
│   │   ├─ output1.txt
│   │   ├─ output2.txt
│   │   ├─ output3.txt
│   │   ├─ output4.txt
│   │   ├─ output5.txt
│   │   ├─ output6.txt
│   │   └─ output7.txt
└── README.md
```

## Cara Menjalankan

1. Clone repository ini:

```bash
git clone https://github.com/JethroJNS/Tucil1_13523081.git
```

2. Navigasi ke direktori repositori dan jalankan command berikut:

```bash
javac -d bin src/IQPuzzlerPro.java
```

```bash
java -cp bin IQPuzzlerPro
```

## Pengembang

| **NIM**  | **Nama Anggota**               | **Github** |
| -------- | ------------------------------ | ---------- |
| 13523081 | Jethro Jens Norbert Simatupang | [JethroJNS](https://github.com/JethroJNS) |