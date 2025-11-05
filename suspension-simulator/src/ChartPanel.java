import javax.swing.*;
import java.awt.*;
import java.awt.geom.*;
import java.util.List;

/**
 * Panel personalizado para graficar resultados de la simulación
 */
public class ChartPanel extends JPanel {
    private List<Double> xData;
    private List<Double> yData;
    private String title;
    private String xLabel;
    private String yLabel;
    private Color lineColor;
    
    private double xMin, xMax, yMin, yMax;
    private boolean autoScale = true;
    
    public ChartPanel(String title, String xLabel, String yLabel) {
        this.title = title;
        this.xLabel = xLabel;
        this.yLabel = yLabel;
        this.lineColor = new Color(0, 120, 215);
        setBackground(Color.WHITE);
        setPreferredSize(new Dimension(400, 300));
    }
    
    public void setData(List<Double> xData, List<Double> yData) {
        this.xData = xData;
        this.yData = yData;
        
        if (autoScale && xData != null && !xData.isEmpty()) {
            calculateBounds();
        }
        
        repaint();
    }
    
    public void setLineColor(Color color) {
        this.lineColor = color;
    }
    
    private void calculateBounds() {
        xMin = xData.stream().mapToDouble(Double::doubleValue).min().orElse(0);
        xMax = xData.stream().mapToDouble(Double::doubleValue).max().orElse(1);
        yMin = yData.stream().mapToDouble(Double::doubleValue).min().orElse(0);
        yMax = yData.stream().mapToDouble(Double::doubleValue).max().orElse(1);
        
        // Añadir margen
        double xRange = xMax - xMin;
        double yRange = yMax - yMin;
        
        if (xRange < 1e-10) xRange = 1.0;
        if (yRange < 1e-10) yRange = 1.0;
        
        xMin -= xRange * 0.05;
        xMax += xRange * 0.05;
        yMin -= yRange * 0.1;
        yMax += yRange * 0.1;
    }
    
    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D g2 = (Graphics2D) g;
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        
        int width = getWidth();
        int height = getHeight();
        
        // Márgenes
        int marginLeft = 60;
        int marginRight = 20;
        int marginTop = 40;
        int marginBottom = 50;
        
        int plotWidth = width - marginLeft - marginRight;
        int plotHeight = height - marginTop - marginBottom;
        
        // Dibujar título
        g2.setColor(Color.BLACK);
        g2.setFont(new Font("Arial", Font.BOLD, 14));
        FontMetrics fm = g2.getFontMetrics();
        int titleWidth = fm.stringWidth(title);
        g2.drawString(title, (width - titleWidth) / 2, 20);
        
        // Dibujar ejes
        g2.setColor(Color.BLACK);
        g2.setStroke(new BasicStroke(2));
        g2.drawLine(marginLeft, marginTop + plotHeight, marginLeft + plotWidth, marginTop + plotHeight); // X
        g2.drawLine(marginLeft, marginTop, marginLeft, marginTop + plotHeight); // Y
        
        // Etiquetas de ejes
        g2.setFont(new Font("Arial", Font.PLAIN, 12));
        fm = g2.getFontMetrics();
        
        // Etiqueta X
        int xLabelWidth = fm.stringWidth(xLabel);
        g2.drawString(xLabel, marginLeft + (plotWidth - xLabelWidth) / 2, height - 10);
        
        // Etiqueta Y (rotada)
        Graphics2D g2d = (Graphics2D) g2.create();
        g2d.rotate(-Math.PI / 2);
        int yLabelWidth = fm.stringWidth(yLabel);
        g2d.drawString(yLabel, -(marginTop + plotHeight + yLabelWidth) / 2, 15);
        g2d.dispose();
        
        // Si no hay datos, terminar aquí
        if (xData == null || yData == null || xData.isEmpty()) {
            g2.setColor(Color.GRAY);
            g2.drawString("Sin datos", width / 2 - 30, height / 2);
            return;
        }
        
        // Dibujar grid
        g2.setColor(new Color(220, 220, 220));
        g2.setStroke(new BasicStroke(1));
        
        int numGridLines = 5;
        for (int i = 0; i <= numGridLines; i++) {
            int x = marginLeft + i * plotWidth / numGridLines;
            int y = marginTop + i * plotHeight / numGridLines;
            
            g2.drawLine(x, marginTop, x, marginTop + plotHeight);
            g2.drawLine(marginLeft, y, marginLeft + plotWidth, y);
        }
        
        // Dibujar marcas y valores en los ejes
        g2.setColor(Color.BLACK);
        g2.setFont(new Font("Arial", Font.PLAIN, 10));
        
        for (int i = 0; i <= numGridLines; i++) {
            // Eje X
            int x = marginLeft + i * plotWidth / numGridLines;
            double xVal = xMin + (xMax - xMin) * i / numGridLines;
            String xStr = String.format("%.2f", xVal);
            int strWidth = g2.getFontMetrics().stringWidth(xStr);
            g2.drawString(xStr, x - strWidth / 2, marginTop + plotHeight + 20);
            
            // Eje Y
            int y = marginTop + plotHeight - i * plotHeight / numGridLines;
            double yVal = yMin + (yMax - yMin) * i / numGridLines;
            String yStr = String.format("%.2f", yVal);
            g2.drawString(yStr, marginLeft - g2.getFontMetrics().stringWidth(yStr) - 5, y + 5);
        }
        
        // Dibujar datos
        g2.setColor(lineColor);
        g2.setStroke(new BasicStroke(2));
        
        Path2D path = new Path2D.Double();
        boolean first = true;
        
        for (int i = 0; i < xData.size(); i++) {
            double xVal = xData.get(i);
            double yVal = yData.get(i);
            
            int px = marginLeft + (int) ((xVal - xMin) / (xMax - xMin) * plotWidth);
            int py = marginTop + plotHeight - (int) ((yVal - yMin) / (yMax - yMin) * plotHeight);
            
            if (first) {
                path.moveTo(px, py);
                first = false;
            } else {
                path.lineTo(px, py);
            }
        }
        
        g2.draw(path);
    }
}
