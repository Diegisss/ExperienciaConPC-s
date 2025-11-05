# Makefile para el Simulador de Suspensión

# Directorios
SRC_DIR = src
BUILD_DIR = build

# Archivos fuente
SOURCES = $(wildcard $(SRC_DIR)/*.java)
CLASSES = $(SOURCES:$(SRC_DIR)/%.java=$(BUILD_DIR)/%.class)

# Compilador y flags
JAVAC = javac
JAVA = java
JAVAC_FLAGS = -encoding UTF-8 -d $(BUILD_DIR) -sourcepath $(SRC_DIR)

# Targets
.PHONY: all clean run example gui help

all: $(BUILD_DIR) $(CLASSES)

$(BUILD_DIR):
	mkdir -p $(BUILD_DIR)

$(BUILD_DIR)/%.class: $(SRC_DIR)/%.java
	$(JAVAC) $(JAVAC_FLAGS) $<

# Ejecutar la interfaz gráfica
gui: all
	@echo "Iniciando interfaz gráfica..."
	cd $(BUILD_DIR) && $(JAVA) SuspensionSimulatorGUI

# Ejecutar el ejemplo de consola
example: all
	@echo "Ejecutando ejemplos..."
	cd $(BUILD_DIR) && $(JAVA) ExampleUsage

# Compilar sin ejecutar
compile: all
	@echo "Compilación completada."

# Limpiar archivos compilados
clean:
	rm -rf $(BUILD_DIR)
	rm -f $(SRC_DIR)/*.class
	@echo "Archivos compilados eliminados."

# Ayuda
help:
	@echo "Simulador de Suspensión - Makefile"
	@echo ""
	@echo "Targets disponibles:"
	@echo "  make          - Compila todos los archivos"
	@echo "  make gui      - Ejecuta la interfaz gráfica"
	@echo "  make example  - Ejecuta los ejemplos de consola"
	@echo "  make compile  - Solo compila sin ejecutar"
	@echo "  make clean    - Elimina archivos compilados"
	@echo "  make help     - Muestra esta ayuda"
