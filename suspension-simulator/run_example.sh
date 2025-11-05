#!/bin/bash

# Script para ejecutar los ejemplos del Simulador de Suspensión

cd "$(dirname "$0")/src"

# Compilar si es necesario
if [ ! -f "ExampleUsage.class" ]; then
    echo "Compilando archivos Java..."
    javac -encoding UTF-8 *.java
    if [ $? -ne 0 ]; then
        echo "Error en la compilación"
        exit 1
    fi
fi

# Ejecutar los ejemplos
echo ""
java ExampleUsage
