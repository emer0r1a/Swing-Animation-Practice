import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

public class FlashcardFlip extends JPanel {
    private String frontText = "Hello";
    private String backText = "World";
    private boolean showingFront = true;

    private double flipProgress = 0.0;
    private boolean flipping = false;
    private Timer timer;

    private int cardWidth = 250;
    private int cardHeight = 350;
    private int cardX, cardY;
    private boolean textVisible = true;

    public FlashcardFlip() {
        setPreferredSize(new Dimension(600, 400));
        setBackground(new Color(245, 245, 245));

        addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                if (flipping) return;
                if (e.getX() >= cardX && e.getX() <= cardX + cardWidth &&
                        e.getY() >= cardY && e.getY() <= cardY + cardHeight) {
                    startFlip();
                }
            }
        });

        addMouseMotionListener(new MouseAdapter() {
            @Override
            public void mouseMoved(MouseEvent e) {
                boolean inside = e.getX() >= cardX && e.getX() <= cardX + cardWidth &&
                        e.getY() >= cardY && e.getY() <= cardY + cardHeight;
                setCursor(new Cursor(inside ? Cursor.HAND_CURSOR : Cursor.DEFAULT_CURSOR));
            }
        });
    }

    private void startFlip() {
        flipping = true;
        flipProgress = 0.0;
        textVisible = false;

        timer = new Timer(10, e -> {
            flipProgress += 0.05;
            if (flipProgress >= 1.0) {
                flipProgress = 1.0;
                flipping = false;
                showingFront = !showingFront;
                textVisible = true;
                ((Timer) e.getSource()).stop();
            }
            repaint();
        });
        timer.start();
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D g2 = (Graphics2D) g;
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

        int centerX = getWidth() / 2;
        int centerY = getHeight() / 2;
        double angle = flipProgress * Math.PI;
        double scaleX = Math.abs(Math.cos(angle));

        int w = (int) (cardWidth * scaleX);
        cardX = centerX - w / 2;
        cardY = centerY - cardHeight / 2;

        g2.setColor(Color.WHITE);
        g2.fillRoundRect(cardX, cardY, w, cardHeight, 40, 40);
        g2.setColor(Color.GRAY);
        g2.drawRoundRect(cardX, cardY, w, cardHeight, 40, 40);

        if (textVisible && scaleX >= 0.99) {
            g2.setFont(new Font("SansSerif", Font.BOLD, 16));
            g2.setColor(Color.DARK_GRAY);
            FontMetrics fm = g2.getFontMetrics();
            String text = showingFront ? frontText : backText;
            int textWidth = fm.stringWidth(text);
            g2.drawString(text, centerX - textWidth / 2, centerY + 5);
        }
    }

    public static void main(String[] args) {
        JFrame frame = new JFrame("Flashcard Flip");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.add(new FlashcardFlip());
        frame.pack();
        frame.setLocationRelativeTo(null);
        frame.setVisible(true);
    }
}
