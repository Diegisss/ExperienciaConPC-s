#!/bin/bash

# Script para ejecutar el Simulador de Suspensión

cd "$(dirname "$0")/src"

# Compilar si es necesario
if [ ! -f "SuspensionSimulatorGUI.class" ]; then
    echo "Compilando archivos Java..."
    javac -encoding UTF-8 *.java
    if [ $? -ne 0 ]; then
        echo "Error en la compilación"
        exit 1
    fi
fi

# Ejecutar la aplicación
echo "Iniciando Simulador de Suspensión..."
java SuspensionSimulatorGUI
