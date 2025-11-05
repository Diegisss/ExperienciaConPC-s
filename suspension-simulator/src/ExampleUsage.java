/**
 * Ejemplo de uso programático del simulador de suspensión
 * Muestra cómo usar las clases sin la interfaz gráfica
 */
public class ExampleUsage {
    
    public static void main(String[] args) {
        System.out.println("═══════════════════════════════════════════════════════════");
        System.out.println("  SIMULADOR DE SUSPENSIÓN - Ejemplo de Uso Programático");
        System.out.println("═══════════════════════════════════════════════════════════\n");
        
        // Ejemplo 1: Sistema lineal básico
        example1_LinearSystem();
        
        // Ejemplo 2: Sistema no lineal
        example2_NonlinearSystem();
        
        // Ejemplo 3: Análisis de resonancia
        example3_ResonanceAnalysis();
        
        // Ejemplo 4: Equilibrio estático con Newton-Raphson
        example4_StaticEquilibrium();
    }
    
    /**
     * Ejemplo 1: Sistema lineal básico
     */
    private static void example1_LinearSystem() {
        System.out.println("─────────────────────────────────────────────────────────");
        System.out.println("EJEMPLO 1: Sistema Lineal Básico");
        System.out.println("─────────────────────────────────────────────────────────");
        
        // Crear modelo con parámetros típicos de suspensión automotriz
        double mass = 250.0;           // kg (masa de 1/4 de vehículo)
        double damping = 1500.0;       // N·s/m
        double stiffness = 20000.0;    // N/m
        
        SuspensionModel model = new SuspensionModel(mass, damping, stiffness);
        model.setExcitationFreq(2.0 * Math.PI * 1.5);  // 1.5 Hz
        model.setExcitationAmp(500.0);  // 500 N
        
        // Calcular propiedades del sistema
        double wn = model.getNaturalFrequency();
        double fn = wn / (2.0 * Math.PI);
        double zeta = model.getDampingRatio();
        
        System.out.println("Parámetros:");
        System.out.printf("  Masa:              %.1f kg\n", mass);
        System.out.printf("  Amortiguamiento:   %.1f N·s/m\n", damping);
        System.out.printf("  Rigidez:           %.1f N/m\n", stiffness);
        System.out.println("\nPropiedades:");
        System.out.printf("  Frecuencia natural: %.2f Hz (%.2f rad/s)\n", fn, wn);
        System.out.printf("  Ratio amortiguamiento: %.3f\n", zeta);
        System.out.printf("  Tipo: %s\n", 
            zeta < 1 ? "Sub-amortiguado" : (zeta == 1 ? "Crítico" : "Sobre-amortiguado"));
        
        // Ejecutar simulación
        SimulationEngine engine = new SimulationEngine(model, 0.001, 3.0);
        engine.simulate();
        
        // Obtener estadísticas
        SimulationEngine.SimulationStats stats = engine.getStatistics();
        System.out.println("\nResultados de la simulación:");
        System.out.printf("  Desplazamiento máximo: %.4f m\n", stats.positionMax);
        System.out.printf("  Velocidad máxima:      %.4f m/s\n", stats.velocityMax);
        System.out.printf("  Aceleración máxima:    %.4f m/s²\n", stats.accelerationMax);
        System.out.printf("  Desplazamiento RMS:    %.4f m\n", stats.positionRMS);
        System.out.println();
    }
    
    /**
     * Ejemplo 2: Sistema no lineal
     */
    private static void example2_NonlinearSystem() {
        System.out.println("─────────────────────────────────────────────────────────");
        System.out.println("EJEMPLO 2: Sistema No Lineal");
        System.out.println("─────────────────────────────────────────────────────────");
        
        SuspensionModel model = new SuspensionModel(100.0, 200.0, 10000.0);
        
        // Añadir no linealidades
        model.setK3(50000.0);  // Rigidez cúbica (endurecimiento)
        model.setC2(10.0);     // Amortiguamiento no lineal
        
        model.setExcitationFreq(2.0 * Math.PI * 5.0);  // 5 Hz
        model.setExcitationAmp(200.0);
        
        System.out.println("Parámetros no lineales:");
        System.out.printf("  k₃ (rigidez cúbica):        %.1f N/m³\n", model.getK3());
        System.out.printf("  c₂ (amortiguamiento cúbico): %.1f N·s³/m³\n", model.getC2());
        
        // Comparar con sistema lineal equivalente
        SuspensionModel linearModel = new SuspensionModel(100.0, 200.0, 10000.0);
        linearModel.setExcitationFreq(model.getExcitationFreq());
        linearModel.setExcitationAmp(model.getExcitationAmp());
        
        SimulationEngine nonlinearEngine = new SimulationEngine(model, 0.001, 3.0);
        SimulationEngine linearEngine = new SimulationEngine(linearModel, 0.001, 3.0);
        
        nonlinearEngine.simulate();
        linearEngine.simulate();
        
        SimulationEngine.SimulationStats nonlinearStats = nonlinearEngine.getStatistics();
        SimulationEngine.SimulationStats linearStats = linearEngine.getStatistics();
        
        System.out.println("\nComparación Lineal vs No Lineal:");
        System.out.println("                        Lineal      No Lineal   Diferencia");
        System.out.printf("  Desp. máximo (m):     %.4f      %.4f      %.2f%%\n",
            linearStats.positionMax, nonlinearStats.positionMax,
            100.0 * (nonlinearStats.positionMax - linearStats.positionMax) / linearStats.positionMax);
        System.out.printf("  Acel. máxima (m/s²):  %.4f      %.4f      %.2f%%\n",
            linearStats.accelerationMax, nonlinearStats.accelerationMax,
            100.0 * (nonlinearStats.accelerationMax - linearStats.accelerationMax) / linearStats.accelerationMax);
        System.out.println();
    }
    
    /**
     * Ejemplo 3: Análisis de resonancia
     */
    private static void example3_ResonanceAnalysis() {
        System.out.println("─────────────────────────────────────────────────────────");
        System.out.println("EJEMPLO 3: Análisis de Resonancia");
        System.out.println("─────────────────────────────────────────────────────────");
        
        SuspensionModel model = new SuspensionModel(100.0, 100.0, 10000.0);
        double wn = model.getNaturalFrequency();
        double fn = wn / (2.0 * Math.PI);
        
        System.out.printf("Frecuencia natural del sistema: %.2f Hz\n", fn);
        System.out.println("\nBarrido de frecuencias:");
        System.out.println("  Frecuencia (Hz)  |  Amplitud (m)  |  Amplificación");
        System.out.println("  ─────────────────────────────────────────────────");
        
        double[] testFreqs = {fn * 0.5, fn * 0.8, fn * 1.0, fn * 1.2, fn * 1.5, fn * 2.0};
        
        for (double freq : testFreqs) {
            model.setExcitationFreq(2.0 * Math.PI * freq);
            model.setExcitationAmp(100.0);
            
            SimulationEngine engine = new SimulationEngine(model, 0.001, 5.0);
            engine.simulate();
            
            SimulationEngine.SimulationStats stats = engine.getStatistics();
            double amplification = stats.positionMax / (100.0 / model.getStiffness());
            
            System.out.printf("  %8.2f         |  %8.4f      |  %8.2fx", 
                freq, stats.positionMax, amplification);
            
            if (Math.abs(freq - fn) < 0.5) {
                System.out.print("  ← RESONANCIA");
            }
            System.out.println();
        }
        System.out.println();
    }
    
    /**
     * Ejemplo 4: Equilibrio estático con Newton-Raphson
     */
    private static void example4_StaticEquilibrium() {
        System.out.println("─────────────────────────────────────────────────────────");
        System.out.println("EJEMPLO 4: Equilibrio Estático (Newton-Raphson)");
        System.out.println("─────────────────────────────────────────────────────────");
        
        double k = 10000.0;   // N/m
        double k3 = 50000.0;  // N/m³
        double F = 1000.0;    // N
        
        System.out.println("Resolviendo: k·x + k₃·x³ = F");
        System.out.printf("  k  = %.1f N/m\n", k);
        System.out.printf("  k₃ = %.1f N/m³\n", k3);
        System.out.printf("  F  = %.1f N\n", F);
        
        // Resolver con Newton-Raphson
        double x = NumericalSolver.newtonRaphson(k, k3, F, 0.0, 100, 1e-9);
        
        System.out.printf("\nSolución: x = %.6f m\n", x);
        
        // Verificar la solución
        double residual = k * x + k3 * Math.pow(x, 3) - F;
        System.out.printf("Verificación: k·x + k₃·x³ - F = %.2e (debe ser ≈ 0)\n", residual);
        
        // Comparar con solución lineal
        double xLinear = F / k;
        System.out.printf("\nComparación con solución lineal:\n");
        System.out.printf("  x (lineal):     %.6f m\n", xLinear);
        System.out.printf("  x (no lineal):  %.6f m\n", x);
        System.out.printf("  Diferencia:     %.2f%%\n", 
            100.0 * (x - xLinear) / xLinear);
        System.out.println();
    }
}
