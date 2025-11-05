import javax.swing.*;
import javax.swing.border.*;
import java.awt.*;
import java.awt.event.*;
import java.text.DecimalFormat;

/**
 * Interfaz gráfica principal del simulador de suspensión
 */
public class SuspensionSimulatorGUI extends JFrame {
    // Modelo y motor de simulación
    private SuspensionModel model;
    private SimulationEngine engine;
    
    // Paneles de gráficos
    private ChartPanel positionChart;
    private ChartPanel velocityChart;
    private ChartPanel accelerationChart;
    private ChartPanel frequencyChart;
    
    // Controles de parámetros
    private JSlider massSlider;
    private JSlider dampingSlider;
    private JSlider stiffnessSlider;
    private JSlider k3Slider;
    private JSlider freqSlider;
    private JSlider ampSlider;
    
    // Etiquetas de valores
    private JLabel massLabel;
    private JLabel dampingLabel;
    private JLabel stiffnessLabel;
    private JLabel k3Label;
    private JLabel freqLabel;
    private JLabel ampLabel;
    
    // Panel de estadísticas
    private JTextArea statsArea;
    
    // Formato de números
    private DecimalFormat df = new DecimalFormat("#0.00");
    
    public SuspensionSimulatorGUI() {
        setTitle("Simulador de Suspensión - Sistema Masa-Resorte-Amortiguador");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new BorderLayout(10, 10));
        
        // Inicializar modelo con valores por defecto
        model = new SuspensionModel(100.0, 200.0, 10000.0);
        engine = new SimulationEngine(model, 0.001, 5.0);
        
        // Crear componentes de la interfaz
        createControlPanel();
        createChartPanel();
        createStatsPanel();
        
        // Ejecutar simulación inicial
        runSimulation();
        
        pack();
        setLocationRelativeTo(null);
        setVisible(true);
    }
    
    /**
     * Crea el panel de controles de parámetros
     */
    private void createControlPanel() {
        JPanel controlPanel = new JPanel();
        controlPanel.setLayout(new BoxLayout(controlPanel, BoxLayout.Y_AXIS));
        controlPanel.setBorder(new TitledBorder("Parámetros del Sistema"));
        controlPanel.setPreferredSize(new Dimension(300, 600));
        
        // Masa
        JPanel massPanel = createParameterPanel("Masa (kg):", 10, 500, 100);
        massSlider = (JSlider) massPanel.getComponent(1);
        massLabel = (JLabel) massPanel.getComponent(2);
        controlPanel.add(massPanel);
        
        // Amortiguamiento
        JPanel dampingPanel = createParameterPanel("Amortiguamiento (N·s/m):", 10, 1000, 200);
        dampingSlider = (JSlider) dampingPanel.getComponent(1);
        dampingLabel = (JLabel) dampingPanel.getComponent(2);
        controlPanel.add(dampingPanel);
        
        // Rigidez
        JPanel stiffnessPanel = createParameterPanel("Rigidez (N/m):", 1000, 50000, 10000);
        stiffnessSlider = (JSlider) stiffnessPanel.getComponent(1);
        stiffnessLabel = (JLabel) stiffnessPanel.getComponent(2);
        controlPanel.add(stiffnessPanel);
        
        // Rigidez no lineal k3
        JPanel k3Panel = createParameterPanel("k₃ No-lineal (×10³):", 0, 100, 0);
        k3Slider = (JSlider) k3Panel.getComponent(1);
        k3Label = (JLabel) k3Panel.getComponent(2);
        controlPanel.add(k3Panel);
        
        // Frecuencia de excitación
        JPanel freqPanel = createParameterPanel("Frecuencia (Hz):", 1, 50, 10);
        freqSlider = (JSlider) freqPanel.getComponent(1);
        freqLabel = (JLabel) freqPanel.getComponent(2);
        controlPanel.add(freqPanel);
        
        // Amplitud de excitación
        JPanel ampPanel = createParameterPanel("Amplitud (N):", 10, 500, 100);
        ampSlider = (JSlider) ampPanel.getComponent(1);
        ampLabel = (JLabel) ampPanel.getComponent(2);
        controlPanel.add(ampPanel);
        
        // Botón de simulación
        JButton simulateButton = new JButton("Ejecutar Simulación");
        simulateButton.setFont(new Font("Arial", Font.BOLD, 14));
        simulateButton.setAlignmentX(Component.CENTER_ALIGNMENT);
        simulateButton.addActionListener(e -> runSimulation());
        
        controlPanel.add(Box.createVerticalStrut(20));
        controlPanel.add(simulateButton);
        
        // Botón de reset
        JButton resetButton = new JButton("Restablecer Valores");
        resetButton.setAlignmentX(Component.CENTER_ALIGNMENT);
        resetButton.addActionListener(e -> resetParameters());
        
        controlPanel.add(Box.createVerticalStrut(10));
        controlPanel.add(resetButton);
        
        // Información del sistema
        JPanel infoPanel = new JPanel();
        infoPanel.setLayout(new BoxLayout(infoPanel, BoxLayout.Y_AXIS));
        infoPanel.setBorder(new TitledBorder("Propiedades del Sistema"));
        
        JTextArea infoArea = new JTextArea(5, 20);
        infoArea.setEditable(false);
        infoArea.setFont(new Font("Monospaced", Font.PLAIN, 11));
        infoPanel.add(new JScrollPane(infoArea));
        
        controlPanel.add(Box.createVerticalStrut(20));
        controlPanel.add(infoPanel);
        
        // Actualizar info cuando cambian los parámetros
        ActionListener updateInfo = e -> {
            double wn = model.getNaturalFrequency();
            double zeta = model.getDampingRatio();
            String damping = zeta < 1 ? "Sub-amortiguado" : (zeta == 1 ? "Crítico" : "Sobre-amortiguado");
            
            infoArea.setText(String.format(
                "ωₙ = %.2f rad/s\n" +
                "fₙ = %.2f Hz\n" +
                "ζ = %.3f\n" +
                "Tipo: %s",
                wn, wn / (2 * Math.PI), zeta, damping
            ));
        };
        
        massSlider.addChangeListener(e -> updateInfo.actionPerformed(null));
        dampingSlider.addChangeListener(e -> updateInfo.actionPerformed(null));
        stiffnessSlider.addChangeListener(e -> updateInfo.actionPerformed(null));
        
        add(controlPanel, BorderLayout.WEST);
    }
    
    /**
     * Crea un panel de parámetro con slider y etiqueta
     */
    private JPanel createParameterPanel(String label, int min, int max, int initial) {
        JPanel panel = new JPanel(new BorderLayout(5, 5));
        panel.setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));
        
        JLabel nameLabel = new JLabel(label);
        nameLabel.setPreferredSize(new Dimension(200, 20));
        
        JSlider slider = new JSlider(min, max, initial);
        slider.setMajorTickSpacing((max - min) / 4);
        slider.setPaintTicks(true);
        
        JLabel valueLabel = new JLabel(String.valueOf(initial));
        valueLabel.setPreferredSize(new Dimension(60, 20));
        valueLabel.setHorizontalAlignment(SwingConstants.RIGHT);
        
        slider.addChangeListener(e -> {
            valueLabel.setText(String.valueOf(slider.getValue()));
        });
        
        panel.add(nameLabel, BorderLayout.NORTH);
        panel.add(slider, BorderLayout.CENTER);
        panel.add(valueLabel, BorderLayout.EAST);
        
        return panel;
    }
    
    /**
     * Crea el panel de gráficos
     */
    private void createChartPanel() {
        JPanel chartPanel = new JPanel(new GridLayout(2, 2, 10, 10));
        chartPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        
        positionChart = new ChartPanel("Desplazamiento vs Tiempo", "Tiempo (s)", "Posición (m)");
        positionChart.setLineColor(new Color(0, 120, 215));
        
        velocityChart = new ChartPanel("Velocidad vs Tiempo", "Tiempo (s)", "Velocidad (m/s)");
        velocityChart.setLineColor(new Color(0, 180, 80));
        
        accelerationChart = new ChartPanel("Aceleración vs Tiempo", "Tiempo (s)", "Aceleración (m/s²)");
        accelerationChart.setLineColor(new Color(220, 50, 50));
        
        frequencyChart = new ChartPanel("Respuesta en Frecuencia", "Frecuencia (Hz)", "Magnitud (dB)");
        frequencyChart.setLineColor(new Color(150, 50, 200));
        
        chartPanel.add(positionChart);
        chartPanel.add(velocityChart);
        chartPanel.add(accelerationChart);
        chartPanel.add(frequencyChart);
        
        add(chartPanel, BorderLayout.CENTER);
    }
    
    /**
     * Crea el panel de estadísticas
     */
    private void createStatsPanel() {
        JPanel statsPanel = new JPanel(new BorderLayout());
        statsPanel.setBorder(new TitledBorder("Estadísticas de la Simulación"));
        statsPanel.setPreferredSize(new Dimension(800, 120));
        
        statsArea = new JTextArea(5, 60);
        statsArea.setEditable(false);
        statsArea.setFont(new Font("Monospaced", Font.PLAIN, 12));
        
        JScrollPane scrollPane = new JScrollPane(statsArea);
        statsPanel.add(scrollPane, BorderLayout.CENTER);
        
        add(statsPanel, BorderLayout.SOUTH);
    }
    
    /**
     * Ejecuta la simulación con los parámetros actuales
     */
    private void runSimulation() {
        // Actualizar parámetros del modelo
        model.setMass(massSlider.getValue());
        model.setDamping(dampingSlider.getValue());
        model.setStiffness(stiffnessSlider.getValue());
        model.setK3(k3Slider.getValue() * 1000.0);  // Escalar por 1000
        model.setExcitationFreq(freqSlider.getValue() * 2.0 * Math.PI);
        model.setExcitationAmp(ampSlider.getValue());
        
        // Ejecutar simulación
        engine.simulate();
        
        // Actualizar gráficos
        positionChart.setData(engine.getTimeHistory(), engine.getPositionHistory());
        velocityChart.setData(engine.getTimeHistory(), engine.getVelocityHistory());
        accelerationChart.setData(engine.getTimeHistory(), engine.getAccelerationHistory());
        
        // Calcular respuesta en frecuencia
        double[][] freqResponse = engine.calculateFrequencyResponse(0.1, 20.0, 200);
        java.util.List<Double> freqs = new java.util.ArrayList<>();
        java.util.List<Double> mags = new java.util.ArrayList<>();
        for (double[] point : freqResponse) {
            freqs.add(point[0]);
            mags.add(point[1]);
        }
        frequencyChart.setData(freqs, mags);
        
        // Actualizar estadísticas
        SimulationEngine.SimulationStats stats = engine.getStatistics();
        
        statsArea.setText(String.format(
            "═══════════════════════════════════════════════════════════════════════════════\n" +
            "  ESTADÍSTICAS DE LA SIMULACIÓN\n" +
            "═══════════════════════════════════════════════════════════════════════════════\n" +
            "  Desplazamiento:    RMS = %8.4f m      Máximo = %8.4f m\n" +
            "  Velocidad:         RMS = %8.4f m/s    Máximo = %8.4f m/s\n" +
            "  Aceleración:       RMS = %8.4f m/s²   Máximo = %8.4f m/s²\n" +
            "═══════════════════════════════════════════════════════════════════════════════\n" +
            "  Frecuencia Natural: %.2f Hz    |    Ratio de Amortiguamiento: %.3f\n" +
            "═══════════════════════════════════════════════════════════════════════════════",
            stats.positionRMS, stats.positionMax,
            stats.velocityRMS, stats.velocityMax,
            stats.accelerationRMS, stats.accelerationMax,
            model.getNaturalFrequency() / (2 * Math.PI),
            model.getDampingRatio()
        ));
    }
    
    /**
     * Restablece los parámetros a valores por defecto
     */
    private void resetParameters() {
        massSlider.setValue(100);
        dampingSlider.setValue(200);
        stiffnessSlider.setValue(10000);
        k3Slider.setValue(0);
        freqSlider.setValue(10);
        ampSlider.setValue(100);
        
        runSimulation();
    }
    
    /**
     * Punto de entrada de la aplicación
     */
    public static void main(String[] args) {
        // Configurar Look and Feel
        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        } catch (Exception e) {
            e.printStackTrace();
        }
        
        // Crear y mostrar la GUI en el Event Dispatch Thread
        SwingUtilities.invokeLater(() -> new SuspensionSimulatorGUI());
    }
}
