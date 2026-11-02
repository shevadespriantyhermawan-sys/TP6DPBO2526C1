import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.util.ArrayList;
import javax.sound.sampled.*; // Untuk fitur Sound Effect (Bonus TP)

public class Logic implements ActionListener, KeyListener {
    // Properti Ukuran Layar Modul 6 & 7
    int frameWidth = 360;
    int frameHeight = 640;

    // Modul 6: Player
    int playerStartPosX = frameWidth / 2;
    int playerStartPosY = frameHeight / 2;
    int playerWidth = 34;
    int playerHeight = 24;

    // Modul 7: Pipe
    int pipeStartPosX = frameWidth;
    int pipeStartPosY = 0;
    int pipeWidth = 64;
    int pipeHeight = 512;
    int pipeVelocityX = -2;

    // Game Loop & Fisika Modul 6 & 7
    Timer gameLoop;
    Timer pipesCooldown;
    int gravity = 1;

    View view;
    Image birdImage;
    Player player;

    // List Pipa Modul 7
    Image lowerPipeImage;
    Image upperPipeImage;
    ArrayList<Pipe> pipes;

    // --- TUGAS PRAKTIKUM SPESIFIKASI WAJIB ---
    boolean gameOver = false;
    boolean isMainMenu = true; // TP 5: State Main Menu di awal
    int score = 0;              // TP 3 & 4: Sistem Skor

    public Logic() {
        // Load Asset Gambar (Modul 6 & Modul 7)
        birdImage = new ImageIcon(getClass().getResource("/assets/bird.png")).getImage();
        lowerPipeImage = new ImageIcon(getClass().getResource("/assets/lowerPipe.png")).getImage();
        upperPipeImage = new ImageIcon(getClass().getResource("/assets/upperPipe.png")).getImage();

        player = new Player(playerStartPosX, playerStartPosY, playerWidth, playerHeight, birdImage);
        pipes = new ArrayList<Pipe>();

        // Timer Spawning Pipa (Modul 7)
        pipesCooldown = new Timer(1500, new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent actionEvent) {
                if (!gameOver && !isMainMenu) {
                    placePipes();
                }
            }
        });

        // Game Loop (Modul 6)
        gameLoop = new Timer(1000 / 60, this);
    }

    // Method Dimulai Saat User Klik Start pada Main Menu (TP 5)
    public void startGame() {
        isMainMenu = false;
        pipesCooldown.start();
        gameLoop.start();
        playSound("/assets/jump.wav"); // Sound effect saat mulai (Bonus TP)
    }

    public void setView(View view) {
        this.view = view;
    }

    public Player getPlayer() { return player; }
    public ArrayList<Pipe> getPipes() { return pipes; }
    public boolean isGameOver() { return gameOver; }
    public boolean isMainMenu() { return isMainMenu; }
    public int getScore() { return score; }

    // Modul 7: Fungsi Penempatan Pipa Acak (Random Spawning)
    public void placePipes() {
        int randomPosY = (int) (pipeStartPosY - (pipeHeight / 4) - Math.random() * (pipeHeight / 2));
        int openingSpace = frameHeight / 4;

        Pipe upperPipe = new Pipe(pipeStartPosX, randomPosY, pipeWidth, pipeHeight, upperPipeImage);
        pipes.add(upperPipe);

        Pipe lowerPipe = new Pipe(pipeStartPosX, randomPosY + openingSpace + pipeHeight, pipeWidth, pipeHeight, lowerPipeImage);
        pipes.add(lowerPipe);
    }

    // Modul 6 & 7 + TP 1 (Deteksi Tabrakan & Jatuh)
    public void move() {
        if (gameOver || isMainMenu) return;

        // Gerak vertikal burung (Modul 6)
        player.setVelocityY(player.getVelocityY() + gravity);
        player.setPosY(player.getPosY() + player.getVelocityY());
        player.setPosY(Math.max(player.getPosY(), 0));

        // TP 1: Game Over jika jatuh melebihi batas bawah JFrame
        if (player.getPosY() + player.getHeight() >= frameHeight) {
            triggerGameOver();
        }

        // Pergerakan pipa & Penambahan Skor (Modul 7 & TP 1, 4)
        for (int i = 0; i < pipes.size(); i++) {
            Pipe pipe = pipes.get(i);
            pipe.setPosX(pipe.getPosX() + pipeVelocityX);

            // TP 4: Tambah Skor (+1) saat burung melewati pipa
            if (!pipe.isPassed() && player.getPosX() > pipe.getPosX() + pipe.getWidth()) {
                pipe.setPassed(true);
                score += 1; // Pipa dihitung sepasang (akan +1 saat melewati pipa atas/bawah)
                playSound("/assets/score.wav"); // Sound Effect (Bonus TP)
            }

            // TP 1: Deteksi Tabrakan (AABB Collision Detection)
            if (checkCollision(player, pipe)) {
                triggerGameOver();
            }
        }
    }

    // TP 1: Fungsi Deteksi Tabrakan Antara Player dan Pipe
    private boolean checkCollision(Player a, Pipe b) {
        return a.getPosX() < b.getPosX() + b.getWidth() &&
                a.getPosX() + a.getWidth() > b.getPosX() &&
                a.getPosY() < b.getPosY() + b.getHeight() &&
                a.getPosY() + a.getHeight() > b.getPosY();
    }

    private void triggerGameOver() {
        gameOver = true;
        gameLoop.stop();
        pipesCooldown.stop();
        playSound("/assets/hit.wav"); // Sound effect Game Over (Bonus TP)
    }

    // TP 2: Reset/Restart Game saat tombol 'R' ditekan
    public void restartGame() {
        player.setPosY(playerStartPosY);
        player.setVelocityY(0);
        pipes.clear();
        score = 0;
        gameOver = false;
        gameLoop.start();
        pipesCooldown.start();
    }

    // --- BONUS TUGAS PRAKTIKUM: AUDIO EFFECT ---
    private void playSound(String soundFilePath) {
        try {
            java.net.URL soundURL = getClass().getResource(soundFilePath);
            if (soundURL != null) {
                AudioInputStream audioInput = AudioSystem.getAudioInputStream(soundURL);
                Clip clip = AudioSystem.getClip();
                clip.open(audioInput);
                clip.start();
            } else {
                System.out.println("File audio tidak ditemukan: " + soundFilePath);
            }
        } catch (Exception e) {
            System.out.println("Gagal memutar audio: " + e.getMessage());
        }
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        move();
        if (view != null) {
            view.repaint();
        }
    }

    @Override
    public void keyTyped(KeyEvent e) {}

    @Override
    public void keyPressed(KeyEvent e) {
        // Kontrol Lompat (Modul 6)
        if (e.getKeyCode() == KeyEvent.VK_SPACE && !gameOver && !isMainMenu) {
            player.setVelocityY(-10);
            playSound("/assets/jump.wav");
        }

        // TP 2: Tekan R untuk Restart setelah Game Over
        if (e.getKeyCode() == KeyEvent.VK_R && gameOver) {
            restartGame();
        }
    }

    @Override
    public void keyReleased(KeyEvent e) {}
}