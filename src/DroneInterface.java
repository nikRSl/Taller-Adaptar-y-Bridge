import javax.swing.*;
import javax.swing.border.*;
import java.awt.*;
import java.awt.event.*;
import java.awt.geom.*;
import java.awt.image.BufferedImage;
import bridge.*;
import energy.*;
import adapter.*;
import legacy.*;

public class DroneInterface extends JFrame {

    // === PALETA DE COLORES ===
    private static final Color BG_DARK       = new Color(10, 14, 26);
    private static final Color BG_PANEL      = new Color(18, 24, 42);
    private static final Color BG_CARD       = new Color(26, 35, 58);
    private static final Color ACCENT_CYAN   = new Color(0, 230, 255);
    private static final Color ACCENT_PURPLE = new Color(140, 82, 255);
    private static final Color ACCENT_GREEN  = new Color(0, 255, 160);
    private static final Color ACCENT_ORANGE = new Color(255, 140, 0);
    private static final Color ACCENT_RED    = new Color(255, 60, 90);
    private static final Color TEXT_PRIMARY  = new Color(220, 230, 255);
    private static final Color TEXT_MUTED    = new Color(100, 120, 160);
    private static final Color BORDER_GLOW   = new Color(0, 230, 255, 60);

    // === FUENTE ===
    private static Font FONT_TITLE;
    private static Font FONT_LABEL;
    private static Font FONT_SMALL;
    private static Font FONT_LOG;

    private JComboBox<String> droneType;
    private JComboBox<String> energyType;
    private JTextArea log;
    private JProgressBar energyBar;
    private JLabel droneIconLabel;
    private JLabel statusLabel;
    private JLabel droneNameLabel;

    private Drone currentDrone;
    private DroneControl adaptedDrone;

    static {
        try {
            FONT_TITLE  = new Font("Segoe UI", Font.BOLD,  22);
            FONT_LABEL  = new Font("Segoe UI", Font.PLAIN, 13);
            FONT_SMALL  = new Font("Segoe UI", Font.BOLD,  11);
            FONT_LOG    = new Font("Consolas", Font.PLAIN, 12);
        } catch (Exception e) {
            FONT_TITLE = new Font("SansSerif", Font.BOLD, 22);
            FONT_LABEL = new Font("SansSerif", Font.PLAIN, 13);
            FONT_SMALL = new Font("SansSerif", Font.BOLD, 11);
            FONT_LOG   = new Font("Monospaced", Font.PLAIN, 12);
        }
    }

    public DroneInterface() {
        setTitle("⬡  DRONE CONTROL SYSTEM  v2.0");
        setSize(820, 620);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setBackground(BG_DARK);
        getContentPane().setBackground(BG_DARK);
        setLayout(new BorderLayout(10, 10));

        add(createHeader(),       BorderLayout.NORTH);
        add(createCenter(),       BorderLayout.CENTER);
        add(createLogPanel(),     BorderLayout.SOUTH);
    }

    // ─────────────────────────────────────────────────────────────
    // HEADER
    // ─────────────────────────────────────────────────────────────
    private JPanel createHeader() {
        JPanel header = new GradientPanel(
                new Color(8, 12, 28), new Color(18, 28, 60), true);
        header.setLayout(new BorderLayout());
        header.setBorder(new EmptyBorder(14, 20, 14, 20));

        // Logo + título
        JPanel left = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 0));
        left.setOpaque(false);

        JLabel icon = new JLabel(buildDroneIcon(48, ACCENT_CYAN));
        left.add(icon);

        JPanel titles = new JPanel();
        titles.setOpaque(false);
        titles.setLayout(new BoxLayout(titles, BoxLayout.Y_AXIS));

        JLabel title = new JLabel("DRONE CONTROL SYSTEM");
        title.setFont(FONT_TITLE);
        title.setForeground(TEXT_PRIMARY);

        JLabel sub = new JLabel("Bridge · Adapter · Energy Manager");
        sub.setFont(FONT_SMALL);
        sub.setForeground(ACCENT_CYAN);

        titles.add(title);
        titles.add(Box.createVerticalStrut(3));
        titles.add(sub);
        left.add(titles);

        // Estado en vivo
        statusLabel = new JLabel("● SYSTEM READY");
        statusLabel.setFont(FONT_SMALL);
        statusLabel.setForeground(ACCENT_GREEN);

        header.add(left,         BorderLayout.WEST);
        header.add(statusLabel,  BorderLayout.EAST);

        // Línea inferior decorativa
        JPanel linea = new JPanel() {
            protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g;
                GradientPaint gp = new GradientPaint(
                        0, 0, ACCENT_PURPLE,
                        getWidth() / 2f, 0, ACCENT_CYAN,
                        true);
                g2.setPaint(gp);
                g2.fillRect(0, 0, getWidth(), 2);
            }
        };
        linea.setPreferredSize(new Dimension(0, 2));
        linea.setOpaque(false);

        JPanel wrapper = new JPanel(new BorderLayout());
        wrapper.setOpaque(false);
        wrapper.add(header, BorderLayout.CENTER);
        wrapper.add(linea,  BorderLayout.SOUTH);
        return wrapper;
    }

    // ─────────────────────────────────────────────────────────────
    // CENTRO: Configuración + Controles
    // ─────────────────────────────────────────────────────────────
    private JPanel createCenter() {
        JPanel centro = new JPanel(new GridLayout(1, 2, 12, 0));
        centro.setOpaque(false);
        centro.setBorder(new EmptyBorder(4, 12, 4, 12));
        centro.add(createSettingsPanel());
        centro.add(createControlPanel());
        return centro;
    }

    private JPanel createSettingsPanel() {
        JPanel card = new CardPanel("⬡  Drone Settings", ACCENT_CYAN);
        card.setLayout(new BoxLayout(card, BoxLayout.Y_AXIS));

        // Icono de drone animado
        droneIconLabel = new JLabel(buildDroneIcon(72, ACCENT_CYAN));
        droneIconLabel.setAlignmentX(Component.CENTER_ALIGNMENT);

        droneNameLabel = new JLabel("No active drone");
        droneNameLabel.setFont(FONT_LABEL);
        droneNameLabel.setForeground(TEXT_MUTED);
        droneNameLabel.setAlignmentX(Component.CENTER_ALIGNMENT);

        card.add(Box.createVerticalStrut(10));
        card.add(droneIconLabel);
        card.add(Box.createVerticalStrut(6));
        card.add(droneNameLabel);
        card.add(Box.createVerticalStrut(16));

        // Separador
        card.add(separator());
        card.add(Box.createVerticalStrut(14));

        // Selector tipo de drone
        card.add(buildLabel("DRONE TYPE", ACCENT_CYAN));
        card.add(Box.createVerticalStrut(5));
        droneType = buildCombo(new String[]{
                "🚁  Delivery Drone",
                "🔍  Exploration Drone",
                "⚙   Legacy Drone"
        });
        card.add(droneType);
        card.add(Box.createVerticalStrut(14));

        // Selector tipo de energía
        card.add(buildLabel("ENERGY SOURCE", ACCENT_PURPLE));
        card.add(Box.createVerticalStrut(5));
        energyType = buildCombo(new String[]{
                "🔋  Lithium",
                "💧  Hydrogen",
                "☀   Solar"
        });
        card.add(energyType);
        card.add(Box.createVerticalStrut(18));

        // Botón inicializar
        JButton btn = buildButton("⬡  INITIALIZE DRONE", ACCENT_CYAN, BG_DARK);
        btn.addActionListener(e -> createDrone());
        btn.setAlignmentX(Component.CENTER_ALIGNMENT);
        btn.setMaximumSize(new Dimension(Integer.MAX_VALUE, 42));
        card.add(btn);
        card.add(Box.createVerticalStrut(10));

        return card;
    }

    private JPanel createControlPanel() {
        JPanel card = new CardPanel("⬡  Control Panel", ACCENT_PURPLE);
        card.setLayout(new BoxLayout(card, BoxLayout.Y_AXIS));

        // Barra energía
        card.add(Box.createVerticalStrut(10));
        card.add(buildLabel("ENERGY LEVEL", ACCENT_GREEN));
        card.add(Box.createVerticalStrut(6));

        energyBar = new JProgressBar(0, 100);
        energyBar.setValue(100);
        energyBar.setStringPainted(true);
        energyBar.setMaximumSize(new Dimension(Integer.MAX_VALUE, 22));
        energyBar.setForeground(ACCENT_GREEN);
        energyBar.setBackground(BG_DARK);
        energyBar.setFont(FONT_SMALL);
        energyBar.setBorderPainted(false);
        energyBar.setAlignmentX(Component.LEFT_ALIGNMENT);
        card.add(energyBar);

        card.add(Box.createVerticalStrut(18));
        card.add(separator());
        card.add(Box.createVerticalStrut(16));

        card.add(buildLabel("ACTIONS", TEXT_MUTED));
        card.add(Box.createVerticalStrut(10));

        // Botones de acción en grid 2x2
        JPanel grid = new JPanel(new GridLayout(2, 2, 10, 10));
        grid.setOpaque(false);
        grid.setMaximumSize(new Dimension(Integer.MAX_VALUE, 130));
        grid.setAlignmentX(Component.LEFT_ALIGNMENT);

        JButton encender  = buildButton("⚡ TURN ON",   ACCENT_ORANGE, BG_DARK);
        JButton volar     = buildButton("🚁 FLY",      ACCENT_CYAN,   BG_DARK);
        JButton aterrizar = buildButton("⬇ LAND",  ACCENT_RED,    BG_DARK);
        JButton energia   = buildButton("🔋 ENERGY",    ACCENT_GREEN,  BG_DARK);

        encender .addActionListener(e -> turnOnDrone());
        volar    .addActionListener(e -> flyDrone());
        aterrizar.addActionListener(e -> landDrone());
        energia  .addActionListener(e -> checkEnergy());

        grid.add(encender);
        grid.add(volar);
        grid.add(aterrizar);
        grid.add(energia);

        card.add(grid);
        card.add(Box.createVerticalStrut(10));

        return card;
    }

    // ─────────────────────────────────────────────────────────────
    // LOG
    // ─────────────────────────────────────────────────────────────
    private JPanel createLogPanel() {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setOpaque(false);
        panel.setBorder(new EmptyBorder(0, 12, 12, 12));

        JPanel card = new CardPanel("⬡  System Log", TEXT_MUTED);
        card.setLayout(new BorderLayout());

        log = new JTextArea(5, 0);
        log.setEditable(false);
        log.setBackground(new Color(8, 12, 22));
        log.setForeground(ACCENT_GREEN);
        log.setFont(FONT_LOG);
        log.setCaretColor(ACCENT_CYAN);
        log.setBorder(new EmptyBorder(8, 10, 8, 10));
        log.setText("» System initialized. Select a drone to begin.\n");

        JScrollPane scroll = new JScrollPane(log);
        scroll.setBorder(BorderFactory.createLineBorder(new Color(0, 100, 80, 80), 1));
        scroll.getViewport().setBackground(new Color(8, 12, 22));
        scroll.setPreferredSize(new Dimension(0, 110));

        card.add(scroll, BorderLayout.CENTER);
        panel.add(card, BorderLayout.CENTER);
        return panel;
    }

    // ─────────────────────────────────────────────────────────────
    // LÓGICA DE DRONES (sin cambios funcionales)
    // ─────────────────────────────────────────────────────────────
    private SistemaEnergia createEnergy() {
        String tipo = ((String) energyType.getSelectedItem())
                .replaceAll(".*\\s", "").trim();
        switch (tipo) {
            case "Lithium":     return new LithiumEnergy();
            case "Hydrogen": return new HydrogenEnergy();
            case "Solar":     return new SolarEnergy();
        }
        return new LithiumEnergy();
    }

    private void createDrone() {
        String tipo = (String) droneType.getSelectedItem();
        if (tipo.contains("Legacy")) {
            LegacyDrone viejo = new LegacyDrone();
            adaptedDrone = new DroneAdapter(viejo);
            currentDrone   = null;
            droneIconLabel.setIcon(buildDroneIcon(72, ACCENT_ORANGE));
            droneNameLabel.setText("Legacy Drone  •  Adapter");
            droneNameLabel.setForeground(ACCENT_ORANGE);
            statusLabel.setText("● ADAPTER ACTIVE");
            statusLabel.setForeground(ACCENT_ORANGE);
            appendLog("» Legacy drone connected via Adapter", ACCENT_ORANGE);
        } else {
            SistemaEnergia energy = createEnergy();
            if (droneType.getSelectedItem().toString().contains("Delivery")) {
                currentDrone = new DeliveryDrone(energy);
                droneIconLabel.setIcon(buildDroneIcon(72, ACCENT_CYAN));
                droneNameLabel.setText("Delivery Drone  •  Bridge");
                droneNameLabel.setForeground(ACCENT_CYAN);
            } else {
                currentDrone = new ExplorationDrone(energy);
                droneIconLabel.setIcon(buildDroneIcon(72, ACCENT_PURPLE));
                droneNameLabel.setText("Exploration Drone  •  Bridge");
                droneNameLabel.setForeground(ACCENT_PURPLE);
            }
            adaptedDrone = null;
            statusLabel.setText("● DRONE ACTIVE");
            statusLabel.setForeground(ACCENT_GREEN);
            appendLog("» Modern drone created using Bridge", ACCENT_CYAN);
        }
        energyBar.setValue(100);
        energyBar.setForeground(ACCENT_GREEN);
    }

    private void turnOnDrone() {
        if (adaptedDrone != null) {
            adaptedDrone.turnOn();
            appendLog("» Legacy drone motor started", ACCENT_ORANGE);
        } else if (currentDrone != null) {
            appendLog("» Energy system activated", ACCENT_GREEN);
        } else noDroneMsg();
    }

    private void flyDrone() {
        if (adaptedDrone != null) {
            adaptedDrone.fly();
            appendLog("» Legacy drone in flight", ACCENT_ORANGE);
        } else if (currentDrone != null) {
            currentDrone.fly();
            appendLog("» Modern drone flying ▲", ACCENT_CYAN);
        } else noDroneMsg();
    }

    private void checkEnergy() {
        if (adaptedDrone != null) {
            adaptedDrone.checkBattery();
            appendLog("» Fuel check completed", ACCENT_ORANGE);
        } else if (currentDrone != null) {
            currentDrone.checkBattery();
            int e = Math.max(0, energyBar.getValue() - 10);
            energyBar.setValue(e);
            energyBar.setForeground(e > 50 ? ACCENT_GREEN :
                    e > 20 ? ACCENT_ORANGE : ACCENT_RED);
            appendLog("» Energy level: " + e + "%",
                    e > 50 ? ACCENT_GREEN : e > 20 ? ACCENT_ORANGE : ACCENT_RED);
        } else noDroneMsg();
    }

    private void landDrone() {
        if (adaptedDrone != null) {
            adaptedDrone.land();
            appendLog("» Legacy drone landing ▼", ACCENT_ORANGE);
        } else if (currentDrone != null) {
            currentDrone.land();
            appendLog("» Modern drone landing ▼", ACCENT_CYAN);
        } else noDroneMsg();
    }

    private void noDroneMsg() {
        appendLog("⚠ Initialize a drone first", ACCENT_RED);
        statusLabel.setText("⚠ NO DRONE");
        statusLabel.setForeground(ACCENT_RED);
    }

    // ─────────────────────────────────────────────────────────────
    // HELPERS UI
    // ─────────────────────────────────────────────────────────────
    private void appendLog(String msg, Color color) {
        log.append(msg + "\n");
        log.setCaretPosition(log.getDocument().getLength());
    }

    private JLabel buildLabel(String text, Color color) {
        JLabel lbl = new JLabel(text);
        lbl.setFont(FONT_SMALL);
        lbl.setForeground(color);
        lbl.setAlignmentX(Component.LEFT_ALIGNMENT);
        return lbl;
    }

    private JComboBox<String> buildCombo(String[] items) {
        JComboBox<String> cb = new JComboBox<>(items);
        cb.setBackground(BG_DARK);
        cb.setForeground(TEXT_PRIMARY);
        cb.setFont(FONT_LABEL);
        cb.setMaximumSize(new Dimension(Integer.MAX_VALUE, 34));
        cb.setBorder(BorderFactory.createLineBorder(new Color(0, 150, 200, 80), 1));
        cb.setAlignmentX(Component.LEFT_ALIGNMENT);
        return cb;
    }

    private JButton buildButton(String text, Color accent, Color bg) {
        JButton btn = new JButton(text) {
            protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING,
                        RenderingHints.VALUE_ANTIALIAS_ON);
                if (getModel().isPressed()) {
                    g2.setColor(accent.darker());
                } else if (getModel().isRollover()) {
                    g2.setColor(new Color(
                            accent.getRed(), accent.getGreen(), accent.getBlue(), 40));
                } else {
                    g2.setColor(new Color(
                            accent.getRed(), accent.getGreen(), accent.getBlue(), 18));
                }
                g2.fillRoundRect(0, 0, getWidth(), getHeight(), 10, 10);
                g2.setColor(new Color(
                        accent.getRed(), accent.getGreen(), accent.getBlue(), 160));
                g2.setStroke(new BasicStroke(1.2f));
                g2.drawRoundRect(0, 0, getWidth() - 1, getHeight() - 1, 10, 10);
                g2.dispose();
                super.paintComponent(g);
            }
        };
        btn.setFont(FONT_SMALL);
        btn.setForeground(accent);
        btn.setContentAreaFilled(false);
        btn.setBorderPainted(false);
        btn.setFocusPainted(false);
        btn.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        return btn;
    }

    private JSeparator separator() {
        JSeparator sep = new JSeparator();
        sep.setForeground(new Color(50, 70, 110));
        sep.setMaximumSize(new Dimension(Integer.MAX_VALUE, 1));
        return sep;
    }

    // ─────────────────────────────────────────────────────────────
    // ICONO DE DRONE DIBUJADO
    // ─────────────────────────────────────────────────────────────
    private ImageIcon buildDroneIcon(int size, Color color) {
        BufferedImage img = new BufferedImage(size, size,
                BufferedImage.TYPE_INT_ARGB);
        Graphics2D g2 = img.createGraphics();
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING,
                RenderingHints.VALUE_ANTIALIAS_ON);

        int cx = size / 2, cy = size / 2;
        int arm = size / 2 - 4;

        // Brazos diagonales
        g2.setStroke(new BasicStroke(size > 50 ? 3f : 2f,
                BasicStroke.CAP_ROUND, BasicStroke.JOIN_ROUND));
        g2.setColor(color);
        int[][] arms = {
                {cx - arm, cy - arm, cx + arm, cy + arm},
                {cx + arm, cy - arm, cx - arm, cy + arm}
        };
        for (int[] a : arms) g2.drawLine(a[0], a[1], a[2], a[3]);

        // Hélices en las 4 esquinas
        int r = size > 50 ? 10 : 6;
        int[][] corners = {
                {cx - arm, cy - arm},
                {cx + arm, cy - arm},
                {cx - arm, cy + arm},
                {cx + arm, cy + arm}
        };
        Color glow = new Color(color.getRed(), color.getGreen(),
                color.getBlue(), 80);
        for (int[] c : corners) {
            g2.setColor(glow);
            g2.fillOval(c[0] - r - 2, c[1] - r - 2, (r + 2) * 2, (r + 2) * 2);
            g2.setColor(color);
            g2.setStroke(new BasicStroke(size > 50 ? 2.5f : 1.5f));
            g2.drawOval(c[0] - r, c[1] - r, r * 2, r * 2);
        }

        // Cuerpo central
        int bodyR = size > 50 ? 9 : 5;
        g2.setColor(new Color(color.getRed(), color.getGreen(),
                color.getBlue(), 60));
        g2.fillOval(cx - bodyR - 2, cy - bodyR - 2,
                (bodyR + 2) * 2, (bodyR + 2) * 2);
        g2.setColor(color);
        g2.setStroke(new BasicStroke(2f));
        g2.fillOval(cx - bodyR, cy - bodyR, bodyR * 2, bodyR * 2);

        g2.dispose();
        return new ImageIcon(img);
    }

    // ─────────────────────────────────────────────────────────────
    // PANELES PERSONALIZADOS
    // ─────────────────────────────────────────────────────────────
    static class GradientPanel extends JPanel {
        private Color c1, c2;
        private boolean horizontal;
        GradientPanel(Color c1, Color c2, boolean horizontal) {
            this.c1 = c1; this.c2 = c2; this.horizontal = horizontal;
            setOpaque(false);
        }
        protected void paintComponent(Graphics g) {
            Graphics2D g2 = (Graphics2D) g;
            GradientPaint gp = horizontal
                    ? new GradientPaint(0, 0, c1, getWidth(), 0, c2)
                    : new GradientPaint(0, 0, c1, 0, getHeight(), c2);
            g2.setPaint(gp);
            g2.fillRect(0, 0, getWidth(), getHeight());
            super.paintComponent(g);
        }
    }

    class CardPanel extends JPanel {
        private String title;
        private Color accent;
        CardPanel(String title, Color accent) {
            this.title  = title;
            this.accent = accent;
            setBackground(BG_CARD);
            setBorder(new CompoundBorder(
                    new LineBorder(new Color(
                            accent.getRed(), accent.getGreen(), accent.getBlue(), 70), 1, true),
                    new EmptyBorder(14, 16, 14, 16)));
        }
        protected void paintComponent(Graphics g) {
            super.paintComponent(g);
            Graphics2D g2 = (Graphics2D) g.create();
            g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING,
                    RenderingHints.VALUE_ANTIALIAS_ON);

            // Barra superior de color
            g2.setColor(new Color(
                    accent.getRed(), accent.getGreen(), accent.getBlue(), 180));
            g2.fillRoundRect(0, 0, getWidth(), 3, 3, 3);

            // Título
            g2.setFont(FONT_SMALL);
            g2.setColor(accent);
            g2.drawString(title, 16, 22);

            g2.dispose();
        }
        public Insets getInsets() {
            Insets i = super.getInsets();
            return new Insets(i.top + 14, i.left, i.bottom, i.right);
        }
    }

    // ─────────────────────────────────────────────────────────────
    public static void main(String[] args) {
        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        } catch (Exception ignored) {}

        SwingUtilities.invokeLater(() -> {
            DroneInterface ui = new DroneInterface();
            ui.setVisible(true);
        });
    }
}