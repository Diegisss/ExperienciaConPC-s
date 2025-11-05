/**
 * Métodos numéricos para resolver ecuaciones diferenciales
 * Implementa Runge-Kutta 4, Newton-Raphson, y métodos implícitos
 */
public class NumericalSolver {
    
    /**
     * Método de Runge-Kutta de 4to orden (RK4)
     * Resuelve dx/dt = f(x, t) desde t hasta t+dt
     */
    public static double[] rungeKutta4(SuspensionModel model, double[] state, double t, double dt) {
        // k1 = f(x, t)
        double[] k1 = model.derivatives(state, t);
        
        // k2 = f(x + dt/2 * k1, t + dt/2)
        double[] state2 = new double[]{
            state[0] + 0.5 * dt * k1[0],
            state[1] + 0.5 * dt * k1[1]
        };
        double[] k2 = model.derivatives(state2, t + 0.5 * dt);
        
        // k3 = f(x + dt/2 * k2, t + dt/2)
        double[] state3 = new double[]{
            state[0] + 0.5 * dt * k2[0],
            state[1] + 0.5 * dt * k2[1]
        };
        double[] k3 = model.derivatives(state3, t + 0.5 * dt);
        
        // k4 = f(x + dt * k3, t + dt)
        double[] state4 = new double[]{
            state[0] + dt * k3[0],
            state[1] + dt * k3[1]
        };
        double[] k4 = model.derivatives(state4, t + dt);
        
        // x_new = x + dt/6 * (k1 + 2*k2 + 2*k3 + k4)
        double[] newState = new double[2];
        newState[0] = state[0] + dt / 6.0 * (k1[0] + 2*k2[0] + 2*k3[0] + k4[0]);
        newState[1] = state[1] + dt / 6.0 * (k1[1] + 2*k2[1] + 2*k3[1] + k4[1]);
        
        return newState;
    }
    
    /**
     * Método de Newton-Raphson para resolver f(x) = 0
     * Usado para equilibrio estático: k*x + k3*x³ - F = 0
     */
    public static double newtonRaphson(double k, double k3, double F, double x0, int maxIter, double tol) {
        double x = x0;
        
        for (int i = 0; i < maxIter; i++) {
            // f(x) = k*x + k3*x³ - F
            double fx = k * x + k3 * Math.pow(x, 3) - F;
            
            // f'(x) = k + 3*k3*x²
            double dfx = k + 3 * k3 * x * x;
            
            // Evitar división por cero
            if (Math.abs(dfx) < 1e-10) {
                break;
            }
            
            // x_new = x - f(x)/f'(x)
            double xNew = x - fx / dfx;
            
            // Verificar convergencia
            if (Math.abs(xNew - x) < tol) {
                return xNew;
            }
            
            x = xNew;
        }
        
        return x;
    }
    
    /**
     * Método de Backward Euler (implícito) con Newton-Raphson
     * Resuelve: m*(x_n+1 - 2*x_n + x_n-1)/dt² + c*(x_n+1 - x_n)/dt + k*x_n+1 + k3*x_n+1³ = F_n+1
     */
    public static double backwardEuler(SuspensionModel model, double xn, double xnm1, double t, double dt, int maxIter, double tol) {
        double m = model.getMass();
        double c = model.getDamping();
        double k = model.getStiffness();
        double k3 = model.getK3();
        double F = model.getExternalForce(t);
        
        double x = xn;  // Estimación inicial
        
        for (int i = 0; i < maxIter; i++) {
            // R(x) = m*(x - 2*xn + xnm1)/dt² + c*(x - xn)/dt + k*x + k3*x³ - F
            double R = m * (x - 2*xn + xnm1) / (dt * dt) + 
                       c * (x - xn) / dt + 
                       k * x + 
                       k3 * Math.pow(x, 3) - F;
            
            // dR/dx = m/dt² + c/dt + k + 3*k3*x²
            double dR = m / (dt * dt) + c / dt + k + 3 * k3 * x * x;
            
            if (Math.abs(dR) < 1e-10) {
                break;
            }
            
            double xNew = x - R / dR;
            
            if (Math.abs(xNew - x) < tol) {
                return xNew;
            }
            
            x = xNew;
        }
        
        return x;
    }
    
    /**
     * Calcula el RMS (Root Mean Square) de un array de valores
     */
    public static double calculateRMS(double[] values) {
        double sum = 0.0;
        for (double v : values) {
            sum += v * v;
        }
        return Math.sqrt(sum / values.length);
    }
    
    /**
     * Calcula la amplitud máxima de un array de valores
     */
    public static double calculateMaxAmplitude(double[] values) {
        double max = Double.NEGATIVE_INFINITY;
        for (double v : values) {
            max = Math.max(max, Math.abs(v));
        }
        return max;
    }
}
