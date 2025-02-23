import java.io.*;
import java.util.*;

public class IQPuzzlerPro {
    static int N, M, P;
    static char[][] board;
    static List<boolean[][]> pieces = new ArrayList<>();
    static List<Character> pieceSymbols = new ArrayList<>();
    static long iterations = 0;
    static long startTime;

    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);
        try {
            readInput(sc); // Membaca input file
            startTime = System.currentTimeMillis(); // Memulai timer
            boolean foundSolution = solve(0); // Mencari solusi puzzle
            long elapsedTime = System.currentTimeMillis() - startTime; // Menghentikan timer

            // Menyusun output
            StringBuilder output = new StringBuilder();
            if (foundSolution) {
                output.append(printBoard());
            } else {
                output.append("Tidak ada solusi!\n");
            }
            output.append("\nWaktu pencarian: ").append(elapsedTime).append(" ms\n");
            output.append("\nBanyak kasus yang ditinjau: ").append(iterations).append("\n");

            // Menampilkan output
            System.out.println(output);

            // Menyimpan solusi
            System.out.print("Apakah anda ingin menyimpan solusi? (ya/tidak): ");
            String response = sc.nextLine().trim().toLowerCase();
            if (response.equals("ya")) {
                saveToFile(output.toString());
            }

        } catch (IOException e) {
            System.out.println(e.getMessage());
        } finally {
            sc.close();
        }
    }

    private static void saveToFile(String content) {
        Scanner scanner = new Scanner(System.in);
        System.out.print("Masukkan nama file output: ");
        String fileName = scanner.nextLine();
        String outputPath = "test/output/" + fileName;
        
        try (FileWriter writer = new FileWriter(outputPath)) {
            // Menyimpan papan tanpa warna
            writer.write(printBoardWithoutColors());
            
            // Menyimpan waktu dan jumlah kasus
            writer.write("\nWaktu pencarian: " + (System.currentTimeMillis() - startTime) + " ms\n");
            writer.write("\nBanyak kasus yang ditinjau: " + iterations + "\n");
            
            System.out.println("Solusi berhasil disimpan dalam " + fileName);
        } catch (IOException e) {
            System.out.println("Gagal menyimpan solusi: " + e.getMessage());
        }
    }

    static void readInput(Scanner sc) throws IOException {
        System.out.print("Masukkan nama file test case: ");
        String fileName = sc.nextLine(); // Meminta input nama file
        String inputPath = "test/input/" + fileName;

        BufferedReader br;
        try {
            br = new BufferedReader(new FileReader(inputPath));
        } catch (FileNotFoundException e) {
            throw new IOException("File tidak ditemukan!");
        }

        // Membaca baris pertama untuk mendapatkan ukuran papan dan jumlah piece
        String firstLine = br.readLine();
        if (firstLine == null) {
            throw new IOException("Format file input tidak valid!");
        }
        String[] dims = firstLine.split(" ");
        if (dims.length < 3) {
            throw new IOException("Format file input tidak valid!");
        }
        N = Integer.parseInt(dims[0]);
        M = Integer.parseInt(dims[1]);
        P = Integer.parseInt(dims[2]);

        // Membaca baris "DEFAULT"
        String defaultLine = br.readLine();
        if (defaultLine == null || !defaultLine.equals("DEFAULT")) {
            throw new IOException("Format file input tidak valid!");
        }

        // Inisialisasi papan
        board = new char[N][M];
        for (char[] row : board) Arrays.fill(row, '.');

        // Membaca piece-piece
        char previousSymbol = '\0'; // Menyimpan simbol piece sebelumnya
        List<String> shapeLines = new ArrayList<>(); // Menyimpan bentuk piece saat ini
        String line;
        while ((line = br.readLine()) != null) {
            // Mencari simbol piece pada baris saat ini
            char currentSymbol = '\0';
            for (char ch : line.toCharArray()) {
                if (ch != ' ') {
                    currentSymbol = ch;
                    break;
                }
            }
            // Jika simbol berubah dan shapeLines tidak kosong, piece sebelumnya diproses
            if (currentSymbol != previousSymbol && !shapeLines.isEmpty()) {
                processPiece(shapeLines, previousSymbol);
                shapeLines.clear(); // Mengosongkan shapeLines untuk piece berikutnya
            }
            // Menambahkan baris ke shapeLines
            shapeLines.add(line);
            previousSymbol = currentSymbol; // Memperbarui label sebelumnya
        }

        // Memproses piece terakhir
        if (!shapeLines.isEmpty()) {
            processPiece(shapeLines, previousSymbol);
        }

        br.close();

        // Memvalidasi jumlah piece
        if (pieces.size() > P) {
            throw new IOException("Piece berlebih!");
        } else if (pieces.size() < P) {
            throw new IOException("Piece kurang!");
        }

        // Memvalidasi total ukuran piece
        int totalSize = 0;
        for (boolean[][] piece : pieces) {
            for (boolean[] row : piece) {
                for (boolean cell : row) {
                    if (cell) totalSize++;
                }
            }
        }
        if (totalSize != N * M) {
            throw new IOException("Ukuran piece tidak sesuai!");
        }
    }

    static void processPiece(List<String> shapeLines, char label) throws IOException {
        if (shapeLines.isEmpty()) { // Memastikan piece tidak kosong
            throw new IOException("Potongan puzzle tidak memiliki bentuk!");
        }

        pieceSymbols.add(label); // Menyimpan simbol piece

        // Menentukan ukuran maksimum baris dan kolom dari bentuk piece
        int shapeRows = shapeLines.size();
        int shapeCols = 0;
        for (String s : shapeLines) {
            shapeCols = Math.max(shapeCols, s.length());
        }

        // Inisialisasi array boolean untuk merepresentasikan bentuk piece
        boolean[][] shape = new boolean[shapeRows][shapeCols];

        // Mengisi array boolean berdasarkan karakter dalam shapeLines
        for (int r = 0; r < shapeRows; r++) {
            String row = shapeLines.get(r);
            for (int c = 0; c < row.length(); c++) {
                if (row.charAt(c) == label) {
                    shape[r][c] = true;
                }
            }
        }
        pieces.add(shape); // Menambahkan bentuk piece ke daftar pieces
    }

    static boolean solve(int index) {
        if (index == P) return true; // Jika semua piece telah ditempatkan, solusi ditemukan
        iterations++;
        char label = pieceSymbols.get(index); // Mendapatkan simbol untuk piece saat ini
        
        for (boolean[][] shape : generateTransformations(pieces.get(index))) { // Mencoba semua transformasi untuk piece saat ini
            for (int r = 0; r <= N - shape.length; r++) {
                for (int c = 0; c <= M - shape[0].length; c++) {
                    if (canPlace(shape, r, c)) { // Mencoba semua posisi untuk transformasi saat ini
                        placePiece(shape, r, c, label);
                        if (solve(index + 1)) return true; // Mencoba piece berikutnya
                        removePiece(shape, r, c); // Backtracking
                    }
                }
            }
        }
        return false;
    }

    static boolean canPlace(boolean[][] shape, int row, int col) {
        // Iterasi melalui semua sel dalam bentuk piece
        for (int r = 0; r < shape.length; r++) {
            for (int c = 0; c < shape[r].length; c++) {
                if (shape[r][c] && board[row + r][col + c] != '.') { // Jika posisi papan sudah terisi, maka tidak bisa ditempatkan
                    return false;
                }
            }
        }
        return true;
    }

    static void placePiece(boolean[][] shape, int row, int col, char label) {
        // Iterasi melalui semua sel dalam bentuk piece
        for (int r = 0; r < shape.length; r++) {
            for (int c = 0; c < shape[r].length; c++) {
                if (shape[r][c]) { // Jika sel dalam shape bernilai true, piece ditempatkan pada posisi yang sesuai
                    board[row + r][col + c] = label;
                }
            }
        }
    }

    static void removePiece(boolean[][] shape, int row, int col) {
        // Iterasi melalui semua sel dalam bentuk piece
        for (int r = 0; r < shape.length; r++) {
            for (int c = 0; c < shape[r].length; c++) {
                if (shape[r][c]) { // Jika sel dalam shape bernilai true, hapus dari papan dengan mengganti kembali ke '.'
                    board[row + r][col + c] = '.';
                }
            }
        }
    }

    static List<boolean[][]> generateTransformations(boolean[][] shape) {
        // Membuat daftar untuk menyimpan semua transformasi (rotasi dan flip)
        List<boolean[][]> transformations = new ArrayList<>();
        for (int i = 0; i < 4; i++) {
            shape = rotate90(shape);
            transformations.add(shape);
            transformations.add(flip(shape));
        }
        return transformations;
    }

    static boolean[][] rotate90(boolean[][] shape) {
        // Memutar piece 90 derajat searah jarum jam
        int rows = shape.length, cols = shape[0].length;
        boolean[][] rotated = new boolean[cols][rows];
        for (int i = 0; i < rows; i++) {
            for (int j = 0; j < cols; j++) {
                rotated[j][rows - i - 1] = shape[i][j];
            }
        }
        return rotated;
    }

    static boolean[][] flip(boolean[][] shape) {
        // Membalik/merefleksi piece
        int rows = shape.length, cols = shape[0].length;
        boolean[][] flipped = new boolean[rows][cols];
        for (int i = 0; i < rows; i++) {
            for (int j = 0; j < cols; j++) {
                flipped[i][cols - j - 1] = shape[i][j];
            }
        }
        return flipped;
    }

    static String printBoard() {
        // Daftar warna ANSI untuk setiap piece
        String[] colors = {
            "\u001B[31m", // Merah
            "\u001B[32m", // Hijau
            "\u001B[33m", // Kuning
            "\u001B[34m", // Biru
            "\u001B[35m", // Magenta
            "\u001B[36m", // Cyan
            "\u001B[37m", // Putih
            "\u001B[91m", // Merah Terang
            "\u001B[92m", // Hijau Terang
            "\u001B[93m", // Kuning Terang
            "\u001B[94m", // Biru Terang
            "\u001B[95m", // Magenta Terang
            "\u001B[96m"  // Cyan Terang
        };

        // Reset warna ke default setelah mencetak
        String resetColor = "\u001B[0m";

        // Map untuk menyimpan warna berdasarkan label piece
        Map<Character, String> colorMap = new HashMap<>();
        for (int i = 0; i < pieceSymbols.size(); i++) {
            colorMap.put(pieceSymbols.get(i), colors[i % colors.length]);
        }

        // Menyusun string papan
        StringBuilder sb = new StringBuilder();

        // Mencetak papan dengan warna
        for (char[] row : board) {
            for (char c : row) {
                if (c != '.') {
                    sb.append(colorMap.get(c)).append(c).append(" ").append(resetColor);
                } else {
                    sb.append(". ");
                }
            }
            sb.append("\n");
        }

        return sb.toString();
    }

    static String printBoardWithoutColors() {
        // Menyusun string papan tanpa warna (untuk save file)
        StringBuilder sb = new StringBuilder();
        for (char[] row : board) {
            for (char c : row) {
                sb.append(c).append(" ");
            }
            sb.append("\n");
        }
    
        return sb.toString();
    }

}