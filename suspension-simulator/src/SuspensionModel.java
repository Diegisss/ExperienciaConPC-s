/**
 * Modelo matemático del sistema de suspensión
 * Implementa ecuaciones diferenciales lineales y no lineales
 */
public class SuspensionModel {
    // Parámetros del sistema
    private double mass;           // m (kg)
    private double damping;        // c (N·s/m)
    private double stiffness;      // k (N/m)
    private double k3;             // k3 (rigidez cúbica no lineal)
    private double c2;             // c2 (amortiguamiento no lineal)
    
    // Estado del sistema [x, v] donde x = posición, v = velocidad
    private double[] state;
    
    // Parámetros de excitación
    private double excitationFreq;  // ω (rad/s)
    private double excitationAmp;   // F0 (N)
    
    public SuspensionModel(double mass, double damping, double stiffness) {
        this.mass = mass;
        this.damping = damping;
        this.stiffness = stiffness;
        this.k3 = 0.0;  // Por defecto lineal
        this.c2 = 0.0;  // Por defecto lineal
        this.state = new double[]{0.0, 0.0};
        this.excitationFreq = 2.0 * Math.PI;  // 1 Hz
        this.excitationAmp = 100.0;  // 100 N
    }
    
    // Getters y Setters
    public double getMass() { return mass; }
    public void setMass(double mass) { this.mass = mass; }
    
    public double getDamping() { return damping; }
    public void setDamping(double damping) { this.damping = damping; }
    
    public double getStiffness() { return stiffness; }
    public void setStiffness(double stiffness) { this.stiffness = stiffness; }
    
    public double getK3() { return k3; }
    public void setK3(double k3) { this.k3 = k3; }
    
    public double getC2() { return c2; }
    public void setC2(double c2) { this.c2 = c2; }
    
    public double getExcitationFreq() { return excitationFreq; }
    public void setExcitationFreq(double freq) { this.excitationFreq = freq; }
    
    public double getExcitationAmp() { return excitationAmp; }
    public void setExcitationAmp(double amp) { this.excitationAmp = amp; }
    
    public double[] getState() { return state.clone(); }
    public void setState(double[] state) { this.state = state.clone(); }
    
    public double getPosition() { return state[0]; }
    public double getVelocity() { return state[1]; }
    
    /**
     * Calcula la fuerza externa en el tiempo t
     */
    public double getExternalForce(double t) {
        return excitationAmp * Math.sin(excitationFreq * t);
    }
    
    /**
     * Ecuación diferencial: dx/dt = f(x, t)
     * Retorna [v, a] donde v = velocidad, a = aceleración
     */
    public double[] derivatives(double[] state, double t) {
        double x = state[0];  // posición
        double v = state[1];  // velocidad
        
        // Fuerza externa
        double F = getExternalForce(t);
        
        // Fuerza del resorte (lineal + no lineal)
        double springForce = stiffness * x + k3 * Math.pow(x, 3);
        
        // Fuerza de amortiguamiento (lineal + no lineal)
        double dampingForce = damping * v + c2 * Math.pow(v, 3);
        
        // Aceleración: a = (F - c*v - k*x - k3*x³ - c2*v³) / m
        double a = (F - dampingForce - springForce) / mass;
        
        return new double[]{v, a};
    }
    
    /**
     * Calcula la frecuencia natural del sistema (rad/s)
     */
    public double getNaturalFrequency() {
        return Math.sqrt(stiffness / mass);
    }
    
    /**
     * Calcula el ratio de amortiguamiento (ζ)
     */
    public double getDampingRatio() {
        double wn = getNaturalFrequency();
        return damping / (2.0 * mass * wn);
    }
    
    /**
     * Calcula la transmisibilidad para una frecuencia dada
     * |H(ω)| = 1 / sqrt((k - m*ω²)² + (c*ω)²)
     */
    public double getTransmissibility(double omega) {
        double real = stiffness - mass * omega * omega;
        double imag = damping * omega;
        return 1.0 / Math.sqrt(real * real + imag * imag);
    }
    
    /**
     * Resetea el estado del sistema
     */
    public void reset() {
        state[0] = 0.0;
        state[1] = 0.0;
    }
}
