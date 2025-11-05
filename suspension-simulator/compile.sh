#!/bin/bash

# Script de compilación para el Simulador de Suspensión

echo "═══════════════════════════════════════════════════════════"
echo "  Compilando Simulador de Suspensión"
echo "═══════════════════════════════════════════════════════════"

cd "$(dirname "$0")/src"

# Compilar todos los archivos Java
echo "Compilando archivos Java..."
javac -encoding UTF-8 *.java

if [ $? -eq 0 ]; then
    echo "✓ Compilación exitosa"
    echo ""
    echo "Para ejecutar:"
    echo "  - Interfaz gráfica:  ./run.sh"
    echo "  - Ejemplos:          ./run_example.sh"
else
    echo "✗ Error en la compilación"
    exit 1
fi
