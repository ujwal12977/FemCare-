# FemCare+ 🌸
> **An Intelligent, Clinically-Validated Menstrual Health & Anemia Risk Screening Platform**

[![Java](https://img.shields.io/badge/Java-17%2B-ED8B00?style=for-the-badge&logo=openjdk&logoColor=white)](https://www.java.com/)
[![HTML5](https://img.shields.io/badge/HTML5-E34F26?style=for-the-badge&logo=html5&logoColor=white)](https://developer.mozilla.org/en-US/docs/Web/HTML)
[![CSS3](https://img.shields.io/badge/CSS3-1572B6?style=for-the-badge&logo=css3&logoColor=white)](https://developer.mozilla.org/en-US/docs/Web/CSS)
[![License](https://img.shields.io/badge/License-MIT-green.svg?style=for-the-badge)](LICENSE)

FemCare+ is a lightweight, privacy-focused web application designed to empower women with clinically-backed tools for tracking menstrual cycles, screening for Heavy Menstrual Bleeding (HMB) using the Higham Pictorial Blood Loss Assessment Chart (PBAC), and evaluating nutritional anemia risks according to WHO and Indian National Health guidelines.

---

## 🌟 Key Features

1. **🩸 PBAC-Based Heavy Menstrual Bleeding (HMB) Detector**
   - Implements the validated **Higham PBAC scoring methodology** (Higham et al., 1990).
   - Weighted score calculation for lightly stained, moderately saturated, and fully soaked sanitary pads/tampons, along with small and large blood clots.
   - Clinical threshold detection ($\ge 100$ score indicates $>80\text{ mL}$ blood loss, the diagnostic standard for menorrhagia).
   - **Dynamic Duration Locking:** Restricts daily bleeding log inputs strictly to the user-defined period duration (e.g., Days 1–$N$), preventing erroneous future logging.
   - **Comprehensive Data Control:** Granular deletion system allowing users to reset individual days, remove specific logged items, clear current period data, or perform a full application reset.

2. **📅 Intelligent Period & Cycle Tracker**
   - Precise cycle day tracking and dynamic next-period prediction based on historical cycle averages.
   - Quick date adjustment and period logging with instant UI synchronization.
   - Cycle history visualization with duration and flow indicators.

3. **🩺 Multi-Factor Anemia Risk Prediction Engine**
   - Algorithmic risk evaluation based on dietary patterns (vegetarian/vegan, tea/coffee post-meal inhibitors), fatigue levels, paleness, bleeding severity, and demographic factors.
   - Clinical risk stratification: Low, Moderate, and High Risk with actionable dietary and medical consultation advice.

4. **🌐 Multilingual Women's Health Knowledge Hub**
   - Culturally accessible educational resources available in English, Hindi (हिंदी), and Marathi (मराठी).
   - Actionable guidelines on iron-rich foods, menstrual hygiene, and red-flag clinical symptoms.

5. **⚡ Zero-Dependency Pure Java Backend**
   - High-performance HTTP server built using Java's standard `com.sun.net.httpserver.HttpServer`.
   - Requires **no external frameworks** (no Spring, Maven, or Gradle dependencies needed).
   - Serves dynamic REST API endpoints and static assets with full CORS and UTF-8 encoding support.

---

## 🏗️ Architecture & Technology Stack

| Layer | Technology | Description |
|---|---|---|
| **Backend** | Java SE (`com.sun.net.httpserver`) | Lightweight embedded REST API server and static asset provider |
| **Frontend UI** | HTML5 Semantic Elements | Accessible and responsive client structure |
| **Styling** | Vanilla CSS3 | Modern design system featuring glassmorphism, responsive CSS grid/flexbox, and CSS custom properties |
| **Client Logic** | Vanilla JavaScript (inlined) | High-speed reactive DOM updates, state management, and asynchronous API integration |
| **Data Storage** | LocalStorage API | Local-first, zero-telemetry client-side privacy architecture |

---

## 🚀 Quick Start Guide

### Prerequisites
- **Java Development Kit (JDK 11 or higher)** installed on your machine.
- Verify installation:
  ```bash
  java -version
  javac -version
  ```

### Running the Application

1. **Clone the repository:**
   ```bash
   git clone https://github.com/ujwal12977/FemCare-.git
   cd FemCare-
   ```

2. **Compile the Java Backend:**
   ```bash
   javac Backend.java
   ```

3. **Start the Server:**
   ```bash
   java Backend
   ```
   *The server will start at `http://localhost:3000` (or `http://localhost:8080`).*

4. **Open in Browser:**
   Visit `http://localhost:3000` in any modern web browser (Google Chrome, Firefox, Safari, Edge).

---

## 📡 API Endpoints

| Method | Endpoint | Description |
|---|---|---|
| `GET` | `/` | Serves the main application (`index.html`) |
| `GET` | `/style.css` | Serves global stylesheets |
| `GET` | `/api/health` | Health check endpoint returning system status |
| `GET` | `/api/questions` | Returns clinical questionnaires for risk assessment |
| `POST` | `/api/anemia/assess` | Computes weighted anemia risk score from responses |
| `POST` | `/api/pbac/quick` | Calculates Higham PBAC score and clinical categorization |
| `POST` | `/api/pbac/comprehensive` | Performs in-depth PBAC multi-day cycle analysis |

---

## 🧪 Clinical Validation & Test Suite
The backend contains a built-in automated test suite verifying all 17 clinical test vectors for PBAC calculation, threshold boundaries, and risk stratification:
```bash
java Backend
```
*Console output confirms validation: `Automated Tests: 17 Passed, 0 Failed`.*

---

## 👥 Somaiya Mini-Project Synopsis Details
- **Institution:** K J Somaiya Institute of Technology (Autonomous Institute Affiliated to University of Mumbai)
- **Department:** Department of Information Technology (SY-IT, Semester III, Scheme-III)
- **Academic Year:** 2026–2027
- **Mini-Project Coordinator:** Dr. Reena Lokare
- **Head of Department:** Dr. Radhika Kotecha
- **Principal:** Dr. Vivek Sunnapwar

*See [`SYNOPSIS_REPORT.md`](SYNOPSIS_REPORT.md) for the complete academic report.*

---

## 📄 License
This project is licensed under the MIT License — see the [LICENSE](LICENSE) file for details.
