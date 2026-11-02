import javax.swing.*;
import java.awt.*;
import java.util.ArrayList; // <-- TAMBAHKAN BARIS INI

public class View extends JPanel {
    int width = 360;
    int height = 640;

    private Logic logic;

    // ... sisa kode di bawahnya tetap sama ...

    public View(Logic logic) {
        this.logic = logic;
        setPreferredSize(new Dimension(width, height));
        setBackground(Color.cyan);

        setFocusable(true);
        addKeyListener(logic);
    }

    @Override
    public void paintComponent(Graphics g) {
        super.paintComponent(g);
        draw(g);
    }

    public void draw(Graphics g) {
        // Render Player (Modul 6)
        Player player = logic.getPlayer();
        if (player != null) {
            g.drawImage(player.getImage(), player.getPosX(), player.getPosY(),
                    player.getWidth(), player.getHeight(), null);
        }

        // Render Pipe List (Modul 7)
        ArrayList<Pipe> pipes = logic.getPipes();
        if (pipes != null) {
            for (int i = 0; i < pipes.size(); i++) {
                Pipe pipe = pipes.get(i);
                g.drawImage(pipe.getImage(), pipe.getPosX(), pipe.getPosY(),
                        pipe.getWidth(), pipe.getHeight(), null);
            }
        }

        // --- TUGAS PRAKTIKUM: RENDERING HUD & OVERLAY UI ---
        g.setColor(Color.white);
        g.setFont(new Font("Arial", Font.BOLD, 28));

        // TP 3: Tampilkan Skor di Atas Layar Game
        if (!logic.isMainMenu()) {
            g.drawString("Score: " + logic.getScore(), 10, 35);
        }

        // TP 1 & 2: Tampilan Saat Game Over
        if (logic.isGameOver()) {
            g.setColor(Color.red);
            g.setFont(new Font("Arial", Font.BOLD, 36));
            g.drawString("GAME OVER", 70, 300);

            g.setColor(Color.white);
            g.setFont(new Font("Arial", Font.PLAIN, 18));
            g.drawString("Tekan 'R' Untuk Restart", 85, 340);
        }
    }
}