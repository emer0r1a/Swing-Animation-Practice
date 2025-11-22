import javax.swing.*;
import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class Nodes extends JFrame {
    private NodePanel nodePanel;
    private JTextField inputField;
    private JButton addLastBtn;
    private BufferedImage nodeImage;
    private JButton resetBtn;
    private JButton removeFirstBtn;
    private JButton removeLastBtn;
    private JButton addFirstBtn;

    private final List<JLabel> nodes = new ArrayList<>();
    private final int spacing = 100;
    private final int nodeWidth = 59;
    private final int nodeHeight = 59;
    private final int step = 12;
    private final int marginX = 20;
    private final int marginY = 20;
    private final int rowSpacing = 40;
    private final int maxPerRow = 10;

    private Timer animationTimer;

    public Nodes() {
        setTitle("Linked List Draft");
        setLayout(null);
        setExtendedState(JFrame.MAXIMIZED_BOTH);
        setDefaultCloseOperation(EXIT_ON_CLOSE);

        try {
            nodeImage = ImageIO.read(getClass().getResourceAsStream("/resource/node.png"));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        nodePanel = new NodePanel(nodes, maxPerRow);
        nodePanel.setBounds(50, 100, 1200, 800);
        nodePanel.setBackground(new Color(230, 230, 230));
        add(nodePanel);

        inputField = new JTextField();
        inputField.setBounds(50, 30, 100, 30);
        add(inputField);

        addLastBtn = new JButton("Add Last");
        addLastBtn.setBounds(180, 30, 100, 30);
        addLastBtn.setFocusable(false);
        add(addLastBtn);

        addFirstBtn = new JButton("Add First");
        addFirstBtn.setBounds(290, 30, 100, 30);
        addFirstBtn.setFocusable(false);
        add(addFirstBtn);

        resetBtn = new JButton("Reset");
        resetBtn.setFocusable(false);
        resetBtn.setBounds(680, 30, 100, 30);
        add(resetBtn);

        removeFirstBtn = new JButton("Remove First");
        removeFirstBtn.setBounds(530, 30, 120, 30);
        removeFirstBtn.setFocusable(false);
        add(removeFirstBtn);

        removeLastBtn = new JButton("Remove Last");
        removeLastBtn.setBounds(400, 30, 120, 30);
        removeLastBtn.setFocusable(false);
        add(removeLastBtn);

        removeFirstBtn.addActionListener(e -> removeFirstNode());
        removeLastBtn.addActionListener(e -> removeLastNode());

        addLastBtn.addActionListener(e -> addLast(inputField.getText()));
        resetBtn.addActionListener(e -> resetNode());
        addFirstBtn.addActionListener(e -> addFirst(inputField.getText()));

        animationTimer = new Timer(20, e -> animateAllNodes());
        animationTimer.start();

        setVisible(true);
    }

    private void addFirst(String text) {
        if (text.isEmpty()) return;

        JLabel node = new JLabel(text, new ImageIcon(nodeImage), JLabel.CENTER);
        node.setHorizontalTextPosition(JLabel.CENTER);
        node.setVerticalTextPosition(JLabel.CENTER);
        node.setFont(new Font("Arial", Font.BOLD, 18));
        node.setForeground(Color.WHITE);

        node.setBounds(-nodeWidth, marginY, nodeWidth, nodeHeight);
        nodePanel.add(node);
        nodePanel.revalidate();
        nodePanel.repaint();

        nodes.addFirst(node);

        inputField.setText("");
    }

    private void removeLastNode() {
        if (!nodes.isEmpty()) {
            JLabel first = nodes.removeLast();
            nodePanel.remove(first);
            nodePanel.revalidate();
            nodePanel.repaint();
        }
    }

    private void removeFirstNode() {
        if (!nodes.isEmpty()) {
            JLabel last = nodes.removeFirst();
            nodePanel.remove(last);
            nodePanel.revalidate();
            nodePanel.repaint();
        }
    }


    private void resetNode() {
        nodes.clear();
        nodePanel.removeAll();
        nodePanel.revalidate();
        nodePanel.repaint();
    }

    private void addLast(String text) {
        if (text.isEmpty()) return;

        JLabel node = new JLabel(text, new ImageIcon(nodeImage), JLabel.CENTER);
        node.setHorizontalTextPosition(JLabel.CENTER);
        node.setVerticalTextPosition(JLabel.CENTER);
        node.setFont(new Font("Arial", Font.BOLD, 18));
        node.setForeground(Color.WHITE);

        int index = nodes.size();
        int row = index / maxPerRow;
        int col = index % maxPerRow;

        int startX = (!nodes.isEmpty()) ? marginY - col * 2*(nodeWidth-spacing) : -nodeWidth;
        int startY = marginY + row * (nodeHeight + rowSpacing);

        node.setBounds(startX+20, startY, nodeWidth, nodeHeight);
        nodePanel.add(node);
        nodePanel.revalidate();
        nodePanel.repaint();

        nodes.add(node);

        inputField.setText("");
    }

    private void animateAllNodes() {
        boolean anyMoving = false;

        for (int i = 0; i < nodes.size(); i++) {
            JLabel node = nodes.get(i);

            int row = i / maxPerRow;
            int col = i % maxPerRow;
            int targetX = marginX + col * spacing;
            int targetY = marginY + row * (nodeHeight + rowSpacing);

            int currentX = node.getX();
            int currentY = node.getY();

            if (currentY != targetY) {
                node.setLocation(targetX, targetY);
                currentX = node.getX();
                currentY = node.getY();
            }

            if (currentX < targetX) {
                node.setLocation(Math.min(currentX + step, targetX), currentY);
                anyMoving = true;
            } else if (currentX > targetX) {
                node.setLocation(Math.max(currentX - step, targetX), currentY);
                anyMoving = true;
            }
        }

        if (anyMoving) {
            nodePanel.repaint();
        }
    }

    public static void main(String[] args) {
        new Nodes();
    }
}

class NodePanel extends JPanel {
    private final List<JLabel> nodes;
    private final int maxPerRow;

    public NodePanel(List<JLabel> nodes, int maxPerRow) {
        this.nodes = nodes;
        this.maxPerRow = maxPerRow;
        setLayout(null);
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);

        g.setColor(Color.BLUE);
        Graphics2D g2 = (Graphics2D) g;
        g2.setStroke(new BasicStroke(3));

        for (int i = 0; i < nodes.size() - 1; i++) {
            int row1 = i / maxPerRow;
            int row2 = (i + 1) / maxPerRow;

            if (row1 == row2) {
                JLabel from = nodes.get(i);
                JLabel to = nodes.get(i + 1);

                int x1 = from.getX() + from.getWidth();
                int y1 = from.getY() + from.getHeight() / 2;

                int x2 = to.getX();
                int y2 = to.getY() + to.getHeight() / 2;

                g2.drawLine(x1, y1, x2, y2);

                int arrowSize = 8;
                double angle = Math.atan2(y2 - y1, x2 - x1);

                int xArrow1 = (int) (x2 - arrowSize * Math.cos(angle - Math.PI / 6));
                int yArrow1 = (int) (y2 - arrowSize * Math.sin(angle - Math.PI / 6));
                int xArrow2 = (int) (x2 - arrowSize * Math.cos(angle + Math.PI / 6));
                int yArrow2 = (int) (y2 - arrowSize * Math.sin(angle + Math.PI / 6));

                g2.drawLine(x2, y2, xArrow1, yArrow1);
                g2.drawLine(x2, y2, xArrow2, yArrow2);
            }
        }
    }
}
