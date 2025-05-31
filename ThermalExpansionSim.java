import javax.swing.*;
import javax.swing.event.*;
import java.awt.*;
import java.util.HashMap;
import javax.swing.border.EmptyBorder;

public class ThermalExpansionSim extends JFrame {
    private JPanel drawPanel;
    private JSlider tempSlider;
    private JLabel tempLabel;
    private JComboBox<String> materialBox;

    private final int initialSize = 100;
    private final int initialTemp = 20;

    private HashMap<String, Double> materialExpansionRates;

    public ThermalExpansionSim() {
        setTitle("Thermal Expansion Simulation");
        setSize(600, 500);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setLayout(new BorderLayout());

        // Define expansion rates
        materialExpansionRates = new HashMap<>();
        materialExpansionRates.put("Metal", 1.5);
        materialExpansionRates.put("Glass", 0.8);
        materialExpansionRates.put("Water", 2.0);
        materialExpansionRates.put("Harold", 5.0);

        // Drawing panel (the expanding square)
        drawPanel = new JPanel() {
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                int temp = tempSlider.getValue();
                int deltaT = temp - initialTemp;

                String material = (String) materialBox.getSelectedItem();
                double rate = materialExpansionRates.get(material);

                int size = (int) (initialSize + deltaT * rate);
                if (size < 10) size = 10;

                int x = (getWidth() - size) / 2;
                int y = (getHeight() - size) / 2;

                int red = Math.min(255, temp * 3);
                int blue = Math.max(0, 255 - temp * 3);
                g.setColor(new Color(red, 0, blue));

                g.fillRect(x, y, size, size);
            }
        };

        // Wrap draw panel in margin
        JPanel centerWrapper = new JPanel(new BorderLayout());
        centerWrapper.setBorder(new EmptyBorder(20, 20, 20, 20)); // top, left, bottom, right
        centerWrapper.add(drawPanel, BorderLayout.CENTER);

        // Temperature slider (vertical)
        tempSlider = new JSlider(JSlider.VERTICAL, 0, 100, initialTemp);
        tempSlider.setMajorTickSpacing(10);
        tempSlider.setPaintTicks(true);
        tempSlider.setPaintLabels(true);
        tempSlider.setBorder(new EmptyBorder(10, 10, 10, 10)); // margin around slider

        tempLabel = new JLabel("Temperature: " + initialTemp + "°C", JLabel.CENTER);

        tempSlider.addChangeListener(e -> {
            int temp = tempSlider.getValue();
            tempLabel.setText("Temperature: " + temp + "°C");
            drawPanel.repaint();
        });

        // Material selector
        materialBox = new JComboBox<>(new String[] { "Metal", "Glass", "Water", "Harold" });
        materialBox.addActionListener(e -> drawPanel.repaint());

        // Top panel with margin
        JPanel topPanel = new JPanel(new BorderLayout(10, 10));
        topPanel.setBorder(new EmptyBorder(10, 10, 10, 10));
        topPanel.add(new JLabel("Material: "), BorderLayout.WEST);
        topPanel.add(materialBox, BorderLayout.CENTER);
        topPanel.add(tempLabel, BorderLayout.EAST);

        add(topPanel, BorderLayout.NORTH);
        add(centerWrapper, BorderLayout.CENTER);
        add(tempSlider, BorderLayout.EAST);
    }

    public static void main(String[] args) {
        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        } catch (Exception e) {
            e.printStackTrace();
        }
        SwingUtilities.invokeLater(() -> new ThermalExpansionSim().setVisible(true));
    }
}