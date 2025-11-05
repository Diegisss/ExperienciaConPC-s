import java.util.ArrayList;
import java.util.List;

/**
 * Motor de simulación que integra el modelo en el tiempo
 */
public class SimulationEngine {
    private SuspensionModel model;
    private double timeStep;
    private double totalTime;
    
    // Resultados de la simulación
    private List<Double> timeHistory;
    private List<Double> positionHistory;
    private List<Double> velocityHistory;
    private List<Double> accelerationHistory;
    private List<Double> forceHistory;
    
    public SimulationEngine(SuspensionModel model, double timeStep, double totalTime) {
        this.model = model;
        this.timeStep = timeStep;
        this.totalTime = totalTime;
        
        this.timeHistory = new ArrayList<>();
        this.positionHistory = new ArrayList<>();
        this.velocityHistory = new ArrayList<>();
        this.accelerationHistory = new ArrayList<>();
        this.forceHistory = new ArrayList<>();
    }
    
    /**
     * Ejecuta la simulación usando Runge-Kutta 4
     */
    public void simulate() {
        // Limpiar historiales previos
        timeHistory.clear();
        positionHistory.clear();
        velocityHistory.clear();
        accelerationHistory.clear();
        forceHistory.clear();
        
        // Resetear el modelo
        model.reset();
        
        double t = 0.0;
        double[] state = model.getState();
        
        // Guardar estado inicial
        timeHistory.add(t);
        positionHistory.add(state[0]);
        velocityHistory.add(state[1]);
        
        double[] deriv = model.derivatives(state, t);
        accelerationHistory.add(deriv[1]);
        forceHistory.add(model.getExternalForce(t));
        
        // Integración temporal
        while (t < totalTime) {
            // Paso de Runge-Kutta
            state = NumericalSolver.rungeKutta4(model, state, t, timeStep);
            t += timeStep;
            
            // Actualizar estado del modelo
            model.setState(state);
            
            // Guardar resultados
            timeHistory.add(t);
            positionHistory.add(state[0]);
            velocityHistory.add(state[1]);
            
            deriv = model.derivatives(state, t);
            accelerationHistory.add(deriv[1]);
            forceHistory.add(model.getExternalForce(t));
        }
    }
    
    /**
     * Calcula la respuesta en frecuencia (transmisibilidad)
     */
    public double[][] calculateFrequencyResponse(double minFreq, double maxFreq, int numPoints) {
        double[][] response = new double[numPoints][2];
        
        for (int i = 0; i < numPoints; i++) {
            double freq = minFreq + (maxFreq - minFreq) * i / (numPoints - 1);
            double omega = 2.0 * Math.PI * freq;
            double transmissibility = model.getTransmissibility(omega);
            
            response[i][0] = freq;
            response[i][1] = 20.0 * Math.log10(transmissibility);  // dB
        }
        
        return response;
    }
    
    /**
     * Calcula el equilibrio estático usando Newton-Raphson
     */
    public double calculateStaticEquilibrium(double force) {
        return NumericalSolver.newtonRaphson(
            model.getStiffness(),
            model.getK3(),
            force,
            0.0,  // x0
            100,  // maxIter
            1e-6  // tol
        );
    }
    
    // Getters para los resultados
    public List<Double> getTimeHistory() { return timeHistory; }
    public List<Double> getPositionHistory() { return positionHistory; }
    public List<Double> getVelocityHistory() { return velocityHistory; }
    public List<Double> getAccelerationHistory() { return accelerationHistory; }
    public List<Double> getForceHistory() { return forceHistory; }
    
    public double getTimeStep() { return timeStep; }
    public void setTimeStep(double timeStep) { this.timeStep = timeStep; }
    
    public double getTotalTime() { return totalTime; }
    public void setTotalTime(double totalTime) { this.totalTime = totalTime; }
    
    /**
     * Calcula estadísticas de la simulación
     */
    public SimulationStats getStatistics() {
        double[] posArray = positionHistory.stream().mapToDouble(Double::doubleValue).toArray();
        double[] velArray = velocityHistory.stream().mapToDouble(Double::doubleValue).toArray();
        double[] accArray = accelerationHistory.stream().mapToDouble(Double::doubleValue).toArray();
        
        return new SimulationStats(
            NumericalSolver.calculateRMS(posArray),
            NumericalSolver.calculateMaxAmplitude(posArray),
            NumericalSolver.calculateRMS(velArray),
            NumericalSolver.calculateMaxAmplitude(velArray),
            NumericalSolver.calculateRMS(accArray),
            NumericalSolver.calculateMaxAmplitude(accArray)
        );
    }
    
    /**
     * Clase interna para almacenar estadísticas
     */
    public static class SimulationStats {
        public final double positionRMS;
        public final double positionMax;
        public final double velocityRMS;
        public final double velocityMax;
        public final double accelerationRMS;
        public final double accelerationMax;
        
        public SimulationStats(double posRMS, double posMax, double velRMS, 
                             double velMax, double accRMS, double accMax) {
            this.positionRMS = posRMS;
            this.positionMax = posMax;
            this.velocityRMS = velRMS;
            this.velocityMax = velMax;
            this.accelerationRMS = accRMS;
            this.accelerationMax = accMax;
        }
    }
}
