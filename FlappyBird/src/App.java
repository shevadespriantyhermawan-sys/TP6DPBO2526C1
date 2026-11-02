import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class App {
    public static void main(String[] args) {
        JFrame frame = new JFrame("FlappyBird");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(360, 640);
        frame.setLocationRelativeTo(null);
        frame.setResizable(false);

        Logic logika = new Logic();
        View tampilan = new View(logika);
        logika.setView(tampilan);

        // --- TUGAS PRAKTIKUM 5: MAIN MENU (START & EXIT BUTTON) ---
        JPanel menuPanel = new JPanel();
        menuPanel.setLayout(new GridBagLayout());
        menuPanel.setBackground(Color.cyan);

        JLabel titleLabel = new JLabel("FLAPPY BIRD");
        titleLabel.setFont(new Font("Arial", Font.BOLD, 32));

        JButton startButton = new JButton("Start Game");
        JButton exitButton = new JButton("Exit");

        // Styling Tombol Menu
        startButton.setPreferredSize(new Dimension(140, 40));
        exitButton.setPreferredSize(new Dimension(140, 40));

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 10, 10, 10);
        gbc.gridx = 0;

        gbc.gridy = 0;
        menuPanel.add(titleLabel, gbc);
        gbc.gridy = 1;
        menuPanel.add(startButton, gbc);
        gbc.gridy = 2;
        menuPanel.add(exitButton, gbc);

        // Pasang panel Menu di awal aplikasi
        frame.add(menuPanel);
        frame.setVisible(true);

        // Aksi Tombol Start Game (TP 5)
        startButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                frame.remove(menuPanel); // Hapus panel menu utama
                frame.add(tampilan);      // Pasang tampilan game
                frame.pack();
                tampilan.requestFocus(); // Berikan fokus keyboard ke game
                logika.startGame();       // Jalankan game loop & timer
                frame.revalidate();
            }
        });

        // Aksi Tombol Exit (TP 5)
        exitButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                System.exit(0);
            }
        });
    }
}