# Simulador de Suspensión - Sistema Masa-Resorte-Amortiguador

## 📋 Descripción

Aplicación Java con interfaz gráfica (Swing) que simula y visualiza el comportamiento de un sistema de suspensión mecánica. Implementa ecuaciones diferenciales lineales y no lineales, métodos numéricos avanzados (Runge-Kutta 4, Newton-Raphson) y análisis de respuesta en frecuencia.

## 🎯 Características

### Modelos Matemáticos Implementados

1. **Modelo Lineal Básico**
   ```
   m·ẍ(t) + c·ẋ(t) + k·x(t) = F(t)
   ```
   - m: masa (kg)
   - c: coeficiente de amortiguamiento (N·s/m)
   - k: rigidez del resorte (N/m)
   - F(t): fuerza externa sinusoidal

2. **Modelo No Lineal**
   ```
   m·ẍ + c·ẋ + k·x + k₃·x³ + c₂·ẋ³ = F(t)
   ```
   - k₃: rigidez cúbica no lineal
   - c₂: amortiguamiento no lineal

### Métodos Numéricos

1. **Runge-Kutta de 4to Orden (RK4)**
   - Integración temporal de alta precisión
   - Paso adaptativo para estabilidad

2. **Newton-Raphson**
   - Cálculo de equilibrio estático
   - Resolución de ecuaciones no lineales
   - Convergencia cuadrática

3. **Backward Euler (Implícito)**
   - Método implícito con Newton-Raphson
   - Mayor estabilidad para sistemas rígidos

### Análisis y Visualización

- **Gráficos en Tiempo Real**:
  - Desplazamiento vs Tiempo
  - Velocidad vs Tiempo
  - Aceleración vs Tiempo
  - Respuesta en Frecuencia (Bode)

- **Estadísticas**:
  - Valores RMS (Root Mean Square)
  - Amplitudes máximas
  - Frecuencia natural (ωₙ)
  - Ratio de amortiguamiento (ζ)

## 🚀 Instalación y Ejecución

### Requisitos

- Java Development Kit (JDK) 11 o superior
- Sistema operativo: Windows, Linux, macOS

### Compilación

```bash
cd suspension-simulator/src
javac -encoding UTF-8 *.java
```

### Ejecución

**Opción 1: Script (Linux/macOS)**
```bash
./run.sh
```

**Opción 2: Comando directo**
```bash
cd src
java SuspensionSimulatorGUI
```

**Opción 3: Desde cualquier directorio**
```bash
cd suspension-simulator/src && java SuspensionSimulatorGUI
```

## 🎮 Uso de la Aplicación

### Panel de Control (Izquierda)

Ajusta los parámetros del sistema usando los sliders:

1. **Masa (kg)**: 10 - 500 kg
   - Afecta la inercia del sistema
   - Mayor masa → menor frecuencia natural

2. **Amortiguamiento (N·s/m)**: 10 - 1000
   - Controla la disipación de energía
   - Determina si el sistema es sub-amortiguado, crítico o sobre-amortiguado

3. **Rigidez (N/m)**: 1000 - 50000
   - Constante del resorte
   - Mayor rigidez → mayor frecuencia natural

4. **k₃ No-lineal (×10³)**: 0 - 100
   - Rigidez cúbica no lineal
   - Valor 0 = sistema lineal
   - Valores > 0 = comportamiento no lineal (endurecimiento)

5. **Frecuencia de Excitación (Hz)**: 1 - 50
   - Frecuencia de la fuerza externa
   - Observa resonancia cerca de la frecuencia natural

6. **Amplitud de Excitación (N)**: 10 - 500
   - Magnitud de la fuerza externa
   - Mayor amplitud → mayor respuesta

### Botones de Control

- **Ejecutar Simulación**: Corre la simulación con los parámetros actuales
- **Restablecer Valores**: Vuelve a los valores por defecto

### Propiedades del Sistema

Muestra información calculada automáticamente:
- **ωₙ**: Frecuencia natural (rad/s)
- **fₙ**: Frecuencia natural (Hz)
- **ζ**: Ratio de amortiguamiento
- **Tipo**: Sub-amortiguado / Crítico / Sobre-amortiguado

### Gráficos (Centro)

Cuatro gráficos interactivos que se actualizan en tiempo real:

1. **Desplazamiento vs Tiempo**: Posición de la masa
2. **Velocidad vs Tiempo**: Velocidad de la masa
3. **Aceleración vs Tiempo**: Aceleración de la masa
4. **Respuesta en Frecuencia**: Transmisibilidad en dB

### Panel de Estadísticas (Abajo)

Muestra métricas calculadas de la simulación:
- RMS y valores máximos de desplazamiento, velocidad y aceleración
- Frecuencia natural y ratio de amortiguamiento del sistema

## 📊 Ejemplos de Uso

### Ejemplo 1: Sistema Sub-amortiguado (Oscilatorio)

```
Masa: 100 kg
Amortiguamiento: 100 N·s/m
Rigidez: 10000 N/m
k₃: 0
Frecuencia: 5 Hz
Amplitud: 100 N
```

**Resultado**: Oscilaciones con decaimiento gradual (ζ < 1)

### Ejemplo 2: Sistema Críticamente Amortiguado

```
Masa: 100 kg
Amortiguamiento: 632 N·s/m  (≈ 2√(mk))
Rigidez: 10000 N/m
k₃: 0
Frecuencia: 5 Hz
Amplitud: 100 N
```

**Resultado**: Retorno rápido al equilibrio sin oscilaciones (ζ = 1)

### Ejemplo 3: Resonancia

```
Masa: 100 kg
Amortiguamiento: 50 N·s/m (bajo)
Rigidez: 10000 N/m
k₃: 0
Frecuencia: 5 Hz (≈ frecuencia natural)
Amplitud: 100 N
```

**Resultado**: Amplitud máxima en la respuesta (resonancia)

### Ejemplo 4: Sistema No Lineal

```
Masa: 100 kg
Amortiguamiento: 200 N·s/m
Rigidez: 10000 N/m
k₃: 50 (50000)
Frecuencia: 5 Hz
Amplitud: 200 N
```

**Resultado**: Comportamiento no lineal con armónicos superiores

## 🔬 Fundamentos Teóricos

### Frecuencia Natural

```
ωₙ = √(k/m)  [rad/s]
fₙ = ωₙ/(2π)  [Hz]
```

### Ratio de Amortiguamiento

```
ζ = c / (2·m·ωₙ)
```

- ζ < 1: Sub-amortiguado (oscilatorio)
- ζ = 1: Críticamente amortiguado
- ζ > 1: Sobre-amortiguado

### Transmisibilidad

```
|H(ω)| = 1 / √[(k - m·ω²)² + (c·ω)²]
```

Mide la relación entre la salida y la entrada en función de la frecuencia.

## 📁 Estructura del Proyecto

```
suspension-simulator/
├── src/
│   ├── SuspensionModel.java          # Modelo matemático del sistema
│   ├── NumericalSolver.java          # Métodos numéricos (RK4, Newton-Raphson)
│   ├── SimulationEngine.java         # Motor de simulación
│   ├── ChartPanel.java               # Panel de gráficos personalizado
│   └── SuspensionSimulatorGUI.java   # Interfaz gráfica principal
├── run.sh                             # Script de ejecución
└── README.md                          # Este archivo
```

## 🧮 Ecuaciones Implementadas

### 1. Ecuación de Movimiento (Forma de Estado)

```
ẋ₁ = x₂
ẋ₂ = (F - c·x₂ - k·x₁ - k₃·x₁³ - c₂·x₂³) / m
```

Donde:
- x₁ = posición
- x₂ = velocidad

### 2. Método de Runge-Kutta 4

```
k₁ = f(xₙ, tₙ)
k₂ = f(xₙ + Δt/2·k₁, tₙ + Δt/2)
k₃ = f(xₙ + Δt/2·k₂, tₙ + Δt/2)
k₄ = f(xₙ + Δt·k₃, tₙ + Δt)

xₙ₊₁ = xₙ + Δt/6·(k₁ + 2k₂ + 2k₃ + k₄)
```

### 3. Newton-Raphson para Equilibrio Estático

```
f(x) = k·x + k₃·x³ - F = 0
f'(x) = k + 3·k₃·x²

xₙ₊₁ = xₙ - f(xₙ)/f'(xₙ)
```

## 🎓 Aplicaciones

Este simulador es útil para:

1. **Diseño de Suspensiones Automotrices**
   - Optimización de confort vs control
   - Análisis de resonancia
   - Minimización de vibraciones

2. **Educación en Ingeniería**
   - Visualización de conceptos de dinámica
   - Comprensión de métodos numéricos
   - Análisis de sistemas vibratorios

3. **Investigación**
   - Estudio de sistemas no lineales
   - Validación de modelos matemáticos
   - Análisis de respuesta en frecuencia

## 🔧 Personalización

### Modificar Parámetros de Simulación

Edita `SimulationEngine.java`:

```java
// Cambiar paso de tiempo (más pequeño = más preciso)
engine = new SimulationEngine(model, 0.0001, 5.0);

// Cambiar tiempo total de simulación
engine.setTotalTime(10.0);
```

### Añadir Nuevos Tipos de Excitación

Edita `SuspensionModel.java`:

```java
public double getExternalForce(double t) {
    // Ejemplo: Excitación de rampa
    return excitationAmp * t;
    
    // Ejemplo: Excitación de pulso
    return (t < 1.0) ? excitationAmp : 0.0;
    
    // Ejemplo: Ruido aleatorio
    return excitationAmp * Math.random();
}
```

## 📝 Notas Técnicas

- **Precisión**: El método RK4 proporciona error O(Δt⁵) por paso
- **Estabilidad**: El paso de tiempo por defecto (0.001s) es adecuado para la mayoría de casos
- **Rendimiento**: La simulación de 5 segundos toma ~5000 pasos de integración
- **Memoria**: Todos los resultados se almacenan en memoria para graficación

## 🐛 Solución de Problemas

### La aplicación no inicia

```bash
# Verificar versión de Java
java -version

# Debe ser >= 11
```

### Errores de compilación

```bash
# Asegurarse de usar UTF-8 encoding
javac -encoding UTF-8 *.java
```

### Gráficos no se actualizan

- Presiona "Ejecutar Simulación" después de cambiar parámetros
- Verifica que los valores de los sliders sean razonables

## 📚 Referencias

1. **Ecuaciones Diferenciales**: Boyce & DiPrima
2. **Métodos Numéricos**: Burden & Faires
3. **Dinámica de Sistemas**: Ogata
4. **Vibraciones Mecánicas**: Rao

## 👨‍💻 Autor

Simulador desarrollado para análisis y diseño de sistemas de suspensión mecánica.

## 📄 Licencia

Este proyecto es de código abierto y está disponible para uso educativo y de investigación.

---

**¡Disfruta explorando la dinámica de sistemas de suspensión!** 🚗💨
