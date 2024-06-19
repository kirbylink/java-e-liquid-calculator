# Building Java E-Liquid Calculator / Java E-Liquid Calculator Bauen

## Table of Contents / Inhaltsverzeichnis
- [English](#english)
  - [Prerequisites](#prerequisites)
  - [Building the Project](#building-the-project)
  - [Creating Executable Files](#creating-executable-files)
  - [Running Tests](#running-tests)
  - [Packaging for Release](#packaging-for-release)
  - [Contact](#contact)
- [Deutsch](#deutsch)
  - [Voraussetzungen](#voraussetzungen)
  - [Projekt Bauen](#projekt-bauen)
  - [Erstellen von Ausführbaren Dateien](#erstellen-von-ausführbaren-dateien)
  - [Tests Ausführen](#tests-ausführen)
  - [Verpacken für die Veröffentlichung](#verpacken-für-die-veröffentlichung)
  - [Kontakt](#kontakt)

## English

### Prerequisites
- JDK 17 or later
- Maven 3.6.3 or later
- [Warp](https://github.com/kirbylink/warp) (optional)
- [Warp4J](https://github.com/kirbylink/warp4j) (optional)

Tested and built on Debian 12.5 (Bookworm) and Raspberry Pi 4 Debian 11.9 (Bullseye)

### Building the Project
1. Clone the repository:
   ```bash
   git clone https://github.com/kirbylink/java-e-liquid-calculator.git
   cd java-e-liquid-calculator
   ```
2. Compile and package the application using Maven:
   ```bash
   mvn clean package
   ```

### Creating Executable Files
The [warp](https://github.com/kirbylink/warp) and [warp4j](https://github.com/kirbylink/warp4j) tools are used to create platform-specific executables.
1. Install warp-runner and warp4j:
   For Linux, see the [INSTALL.md of warp4j](https://github.com/kirbylink/warp4j/blob/master/INSTALL.md).
2. Generate the executables:
   ```bash
   warp4j --class-path /<path_to_project>/target/classes/de/dddns/kirbylink --prefix java-e-liquid-calculator --add-modules jdk.localedata --silent --output /<path_to_project>/target/ /<path_to_project>/target/*-jar-with-dependencies.jar
   ```
The output files will be located in the `target` directory.
Downloaded runtimes and prepared bundles are located at `$HOME/.local/share/warp4j`.

### Running Tests
To run the unit and integration tests, use the following command:
```bash
mvn clean verify
```

### Packaging for Release
To package the application for release, run:
```bash
mvn clean package
```
The output files will be located in the `target` directory.

### Contact
If you encounter any issues or have questions, please open an issue on GitHub.

## Deutsch

### Voraussetzungen
- JDK 17 oder neuer
- Maven 3.6.3 oder neuer
- [Warp](https://github.com/kirbylink/warp) (optional)
- [Warp4J](https://github.com/kirbylink/warp4j) (optional)

Getestet und gebaut auf Debian 12.5 (Bookworm) und Raspberry Pi 4 Debian 11.9 (Bullseye).

### Projekt Bauen
1. Klonen Sie das Repository:
   ```bash
   git clone https://github.com/kirbylink/java-e-liquid-calculator.git
   cd java-e-liquid-calculator
   ```
2. Kompilieren und paketieren Sie die Anwendung mit Maven:
   ```bash
   mvn clean package
   ```

### Erstellen von Ausführbaren Dateien
Die Werkzeuge [warp](https://github.com/kirbylink/warp) und [warp4j](https://github.com/kirbylink/warp4j) werden verwendet, um plattformspezifische ausführbare Dateien zu erstellen.
1. Installieren Sie warp-runner und warp4j:
   Für Linux, siehe [INSTALL.md von warp4j](https://github.com/kirbylink/warp4j/blob/master/INSTALL.md).
2. Erstellen Sie die ausführbaren Dateien:
   ```bash
   cd java-e-liquid-calculator
   warp4j --class-path /<path_to_project>/target/classes/de/dddns/kirbylink --prefix java-e-liquid-calculator --add-modules jdk.localedata --silent --output /<path_to_project>/target/ /<path_to_project>/target/*-jar-with-dependencies.jar
   ```
Die Ausgabedateien befinden sich im `target` Verzeichnis.
Heruntergeladene Laufzeiten und vorbereitete Pakete befinden sich unter `$HOME/.local/share/warp4j`.

### Tests Ausführen
Um die Unit- und Integrationstests auszuführen, verwenden Sie den folgenden Befehl:
```bash
mvn clean verify
```

### Verpacken für die Veröffentlichung
Um die Anwendung für die Veröffentlichung zu paketieren, führen Sie aus:
```bash
mvn clean package
```
Die Ausgabedateien befinden sich im `target` Verzeichnis.

### Kontakt
Wenn Sie Probleme haben oder Fragen, öffnen Sie bitte ein Issue auf GitHub.