# SOMAIYA VIDYAVIHAR
## K J Somaiya Institute of Technology
*(An Autonomous Institute Permanently Affiliated to the University of Mumbai)*  
### DEPARTMENT OF INFORMATION TECHNOLOGY

---

# Synopsis of Mini Project On
# **FemCare+: An Intelligent Menstrual Health, Heavy Bleeding (PBAC) & Anemia Risk Screening Platform**

**Prepared By:**
- Student 1 (Roll No. __)
- Student 2 (Roll No. __)
- Student 3 (Roll No. __)
- Student 4 (Roll No. __)

**Under the guidance of:**
- **Project Guide:** Dr. / Prof. [Name of Guide]

**Academic Year:** 2026–2027  
**Autonomy Syllabus Scheme-III — SEMESTER III (SY - IT)**

---

## CERTIFICATE

This is to certify that the following students:

| Student Name | Roll No. / Seat No. |
|---|---|
| Student 1 | [Roll No. 1] |
| Student 2 | [Roll No. 2] |
| Student 3 | [Roll No. 3] |
| Student 4 | [Roll No. 4] |

have submitted the PBL – Mini Project I Report on **"FemCare+: An Intelligent Menstrual Health, Heavy Bleeding (PBAC) & Anemia Risk Screening Platform"** as partial fulfillment of the requirements for Second Year of Engineering (3rd Semester) in S.Y. - Information Technology under my guidance during the academic year 2026–2027.

<br><br>

_____________________________ &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp; _____________________________  
**Name of Guide** &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp; **Dr. Radhika Kotecha**  
*Project Guide* &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp; *Head of Department, Professor*  
*Assistant Professor* &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp; *Department of Information Technology*  
*Department of Information Technology*

<br>

**Date of Examination:** ____________________

<br><br>

_____________________________ &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp; _____________________________  
**Signature of Internal Examiner** &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp; **Signature of External Examiner**

---

## TABLE OF CONTENTS

- Acknowledgement ............................................................................................ i
- Abstract ........................................................................................................... ii
- Chapter 1: Introduction .................................................................................... 1
  - 1.1 Motivation
  - 1.2 Problem Analysis
  - 1.3 Objectives
  - 1.4 Scope
- Chapter 2: Literature Review ............................................................................ 2
  - 2.1 Related Work
  - 2.2 Existing Systems
- Chapter 3: Proposed System .............................................................................. 3
  - 3.1 Proposed Approach and Architectural Details
  - 3.2 Innovation in Idea
  - 3.3 Timeline & Milestones
  - 3.4 Roles and Responsibilities
  - 3.5 Software Development Lifecycle (SDLC) Model
- Chapter 4: Implementation Details and Results ................................................ 4
  - 4.1 Technology Stack
  - 4.2 Implementation & Algorithmic Parameters
  - 4.3 Preliminary Results & Validation
- Chapter 5: Conclusion ...................................................................................... 5
  - 5.1 Conclusion
  - 5.2 Future Scope
- References ........................................................................................................ 6

---

## ACKNOWLEDGEMENT

We express our profound gratitude to our **Project Guide**, for their invaluable guidance, constant encouragement, and insightful feedback throughout the conception and implementation of the **FemCare+** project.

We convey our sincere appreciation to **Dr. Reena Lokare**, Mini-Project Coordinator, Department of Information Technology, for organizing the project review milestones, providing systematic project guidelines, and facilitating a collaborative academic environment.

We extend our deep gratitude to **Dr. Radhika Kotecha**, Head of the Department of Information Technology, for her leadership, encouraging technological innovation, and making departmental resources readily available to support our research and development.

Finally, we express our heartfelt thanks to our respected Principal, **Dr. Vivek Sunnapwar**, for providing state-of-the-art infrastructural facilities and fostering an academic atmosphere dedicated to engineering excellence, societal relevance, and innovation at K J Somaiya Institute of Technology.

---

## ABSTRACT

Iron Deficiency Anemia (IDA) and Heavy Menstrual Bleeding (HMB / Menorrhagia) represent two deeply intertwined yet critically under-diagnosed health conditions affecting over 50% of reproductive-age women across India and developing economies. Standard mobile menstrual tracking applications function primarily as rudimentary fertility calendars, failing to evaluate clinical blood volume loss or connect chronic menstrual blood loss with physiological iron depletion. 

This project presents **FemCare+**, an accessible, privacy-centric, clinically-grounded web platform combining:
1. Menstrual cycle tracking with dynamic duration constraints.
2. Objective menorrhagia screening via the Higham Pictorial Blood Loss Assessment Chart (PBAC) validated clinical methodology.
3. Multi-factor non-invasive nutritional anemia risk stratification based on dietary and symptomatic indicators.
4. Culturally localized health literacy education in English, Hindi, and Marathi.

The system is engineered using a zero-external-dependency pure Java HTTP server architecture paired with high-performance Vanilla HTML5/CSS3 and client-side reactive components. The PBAC engine computes weighted scores for pad/tampon saturation levels and clot occurrences against the established clinical threshold ($Score \ge 100 \implies >80\text{ mL}$ blood loss, indicating menorrhagia with 86% sensitivity and 89% specificity). Dynamic period duration locking prevents out-of-cycle logging anomalies, while a granular data management subsystem guarantees total user privacy and autonomy. Rigorous testing across 17 clinical test vectors confirms robust mathematical accuracy, offering a high-utility, non-invasive digital screening tool that bridges everyday menstrual tracking with preventive clinical healthcare.

---

## CHAPTER 1: INTRODUCTION

### 1.1 Motivation
According to the National Family Health Survey (NFHS-5), over 57% of women aged 15–49 in India suffer from anemia, with Iron Deficiency Anemia (IDA) accounting for the vast majority. A principal contributing yet chronically overlooked etiology of IDA in non-pregnant women is Heavy Menstrual Bleeding (HMB). In clinical practice, menorrhagia is defined as menstrual blood loss exceeding 80 mL per cycle or periods extending past 7 days. Despite its severe implications on physical stamina, cognitive focus, and long-term cardiovascular health, social stigma and a lack of objective measurement tools prevent women from identifying abnormal blood loss early.

Conventional commercial period trackers (e.g., Flo, Clue) treat menstrual tracking purely as a statistical calendar for ovulation and period prediction. They provide generic, qualitative flow ratings (e.g., "Light", "Medium", "Heavy") without medical grounding. Consequently, women experiencing pathologically heavy bleeding remain unaware that their symptoms warrant medical consultation. This critical gap between routine tracking and preventive healthcare motivated the development of **FemCare+**—an accessible, privacy-conscious digital screening platform that empowers women to quantify menstrual blood loss objectively and identify anemia risk factors early.

### 1.2 Problem Analysis
Traditional clinical quantification methods for menstrual blood loss, such as the alkaline hematin method, are laboratory-bound, costly, and completely impractical for daily consumer use. Conversely, self-reporting based on subjective perception is notoriously inaccurate, as women often normalize abnormally heavy flow due to familial or societal precedents.

Key problems identified include:
1. **Absence of Standardized Bleeding Quantification:** Most consumer trackers lack clinical scoring methodologies to measure blood loss.
2. **Disconnected Health Dimensions:** Menstrual blood loss and nutritional iron deficiency are treated as isolated events rather than an interconnected feedback loop.
3. **Data Privacy and Stigma:** Commercial apps routinely transmit sensitive reproductive health metrics to third-party ad networks, eroding user trust.
4. **Language & Usability Barriers:** Many advanced digital health tools are available solely in English and feature bloated, overwhelming user interfaces.

### 1.3 Objectives
- To implement an objective, validated **Higham PBAC scoring engine** capable of screening for Heavy Menstrual Bleeding ($Score \ge 100$).
- To build a **multi-factor Anemia Risk Screening module** evaluating dietary iron intake, absorption inhibitors, and clinical symptoms.
- To design a **Dynamic Period Duration Lock** that constrains daily logging strictly to the user's active bleeding days, preventing data anomalies.
- To provide a **comprehensive data management system** enabling users to edit, reset single days, delete cycle records, or purge all data locally.
- To create a **multilingual knowledge portal** offering verified medical educational content in English, Hindi, and Marathi.
- To engineer a **zero-dependency, lightweight pure Java backend** guaranteeing reliable execution, data privacy, and minimal computational overhead.

### 1.4 Scope
- **In Scope:**
  - Automated PBAC calculation for sanitary pads, tampons, and clot sizes.
  - Menstrual cycle tracking, cycle day computation, and dynamic flow progression.
  - Multi-parameter non-invasive anemia risk calculation (Low, Moderate, High risk).
  - Trilingual localized educational interface (English, Hindi, Marathi).
  - Granular client-side data deletion and local persistence.
  - RESTful Java HTTP API endpoints for algorithmic risk computation.
- **Out of Scope:**
  - Biochemical diagnosis replacing venous blood laboratory tests (e.g., Serum Ferritin, CBC).
  - Invasive medical diagnostic devices or direct prescription of pharmaceutical drugs.
  - Cloud-based multi-user synchronization or commercial advertising tracking.

---

## CHAPTER 2: LITERATURE REVIEW

### 2.1 Related Work
1. **Higham, J. M., O’Brien, P. M., & Shaw, R. W. (1990).** *"Assessment of menstrual blood loss using a pictorial chart."* *British Journal of Obstetrics and Gynaecology (BJOG)*.  
   - Established the standard Pictorial Blood Loss Assessment Chart (PBAC). Demonstrated that a cumulative cycle score of $\ge 100$ correlates with $>80\text{ mL}$ blood loss with an 86% diagnostic sensitivity and 89% specificity, proving to be a reliable, non-invasive alternative to the chemical alkaline hematin technique.
2. **Fraser, I. S., Critchley, H. O., Munro, M. G., & Broder, M. (2011).** *"The FIGO recommendations on terminologies and definitions for normal and abnormal uterine bleeding."* *International Journal of Gynecology & Obstetrics*.  
   - Standardized the FIGO classification system for Abnormal Uterine Bleeding (AUB) and defined normal menstruation parameters (frequency 24–38 days, duration $\le 8$ days, regular cycle variation $\le 7–9$ days).
3. **World Health Organization (WHO) & Ministry of Health and Family Welfare (MoHFW, 2018).** *"Anemia Mukt Bharat: Operational Guidelines for Intensified National Iron Plus Initiative."*  
   - Outlined epidemiological guidelines connecting nutritional iron intake, post-meal polyphenolic inhibitors (tannins in tea and coffee), and chronic blood loss with iron deficiency anemia.

### 2.2 Comparison with Existing Systems

| Feature / Metric | Flo Health | Clue | Standard Fertility Apps | **FemCare+ (Proposed)** |
|---|---|---|---|---|
| **Bleeding Quantification** | Subjective tags (Low/Med/High) | Qualitative labels | Qualitative tags | **Objective Higham PBAC (Score $\ge 100$)** |
| **Anemia Risk Correlation** | None | None | None | **Integrated Symptom & Dietary Risk Engine** |
| **Logging Boundaries** | Unconstrained calendar | Unconstrained calendar | Unconstrained calendar | **Dynamic Duration Locking (Days 1–$N$)** |
| **Data Privacy** | Cloud / Third-party trackers | Cloud servers | Cloud-hosted | **Local-First / Zero Telemetry Privacy** |
| **Multilingual Accessibility** | Limited regional Indian support | Western languages only | English only | **English, Hindi (हिंदी), Marathi (मराठी)** |
| **Backend Architecture** | Heavy cloud microservices | Cloud SaaS | Proprietary cloud | **Zero-Dependency Pure Java HTTP Server** |

---

## CHAPTER 3: PROPOSED SYSTEM

### 3.1 Proposed Approach and Architecture
FemCare+ is designed following a decoupled, client-server architecture. The frontend handles interactive cycle tracking, dynamic form validation, and reactive UI state management. The backend, implemented strictly in pure Java without third-party frameworks, exposes RESTful endpoints for validated algorithmic scoring and serves static web assets.

#### 3.1.1 Block Architecture
```
+------------------------------------------------------------------------+
|                          FemCare+ Client UI                            |
|  [Period Tracker]  [PBAC Bleeding Logger]  [Anemia Screening]  [Hub]   |
+-----------------------------------+------------------------------------+
                                    | (JSON / REST API)
+-----------------------------------v------------------------------------+
|                  Embedded Java HTTP Server (Port 3000)                  |
|  - HttpHandler Routing Engine                                          |
|  - Higham PBAC Calculation Engine                                      |
|  - Weighted Anemia Scoring Engine                                      |
|  - Static Asset Provider (HTML/CSS)                                    |
+-----------------------------------+------------------------------------+
                                    |
+-----------------------------------v------------------------------------+
|                      Local-First Client Storage                        |
|  - Active Period Cycles                                                |
|  - Per-Day Itemized Bleeding Scores                                    |
|  - Anemia Assessment Records                                           |
+------------------------------------------------------------------------+
```

### 3.2 Innovation in Idea
1. **Dynamic Duration Bounding:** Prevents accidental or nonsensical future logging by clamping PBAC data entry strictly to the active period duration configured by the user.
2. **Clinical Higham Scoring in Browser:** Translates visual physical evidence (pad saturation, tampon fullness, clot dimensions) into mathematically verified clinical point thresholds.
3. **Multi-Tiered Data Purge & Reset Controls:** Offers complete data autonomy—users can reset an individual day's entry, remove a specific day, clear the active cycle, or perform a total factory reset.

### 3.3 Project Timeline (Gantt Chart Representation)

| Phase | Milestone Description | Duration |
|---|---|---|
| **Phase 1** | Requirement Gathering & Clinical Literature Review (Higham PBAC, FIGO) | Weeks 1–2 |
| **Phase 2** | UI/UX Wireframing & Responsive CSS Design System | Weeks 3–4 |
| **Phase 3** | Java Backend Server & REST Endpoints Implementation | Weeks 5–6 |
| **Phase 4** | Period Tracker & PBAC Dynamic Duration Lock Implementation | Weeks 7–8 |
| **Phase 5** | Data Management, Multilingual Localization & Unit Testing (17 Vectors) | Weeks 9–10 |
| **Phase 6** | Documentation, Synopsis Preparation & Code Finalization | Weeks 11–12 |

### 3.4 Roles and Responsibilities
- **Student 1 (Full Stack & Architecture Lead):** Java backend HTTP server implementation, PBAC algorithm design, dynamic duration lock logic.
- **Student 2 (Frontend & UI/UX Developer):** CSS design system, responsive layouts, glassmorphism components, interactive cycle tracker calendar.
- **Student 3 (Clinical Research & Anemia Engine):** Literature review, WHO/MoHFW survey mapping, anemia weighted scoring model, test vector validation.
- **Student 4 (Localization & Quality Assurance):** Multilingual Hindi/Marathi translations, comprehensive data deletion subsystem testing, synopsis documentation.

### 3.5 Software Development Lifecycle (SDLC) Model
The project adopted the **Iterative & Incremental Agile Model**. This approach enabled continuous clinical validation, rapid prototyping of UI components, and incremental enhancements based on real-time feedback (such as simplifying the PBAC logging screen, adding date modification, and building dynamic duration bounds).

---

## CHAPTER 4: IMPLEMENTATION DETAILS AND RESULTS

### 4.1 Technology Stack
- **Backend Programming Language:** Java SE (JDK 17+)
- **Server Framework:** Pure `com.sun.net.httpserver.HttpServer` (Zero external dependencies)
- **Frontend Architecture:** HTML5 Semantic Structure, Vanilla CSS3 (Custom Variables, Flexbox/Grid)
- **Client Scripting:** Pure Vanilla JavaScript (Inlined, no external libraries or bundlers)
- **Data Persistence:** Browser LocalStorage API (Local-first, encrypted browser sandbox)

### 4.2 Implementation & Algorithmic Parameters

#### Higham PBAC Scoring Formula:
$$\text{PBAC Score} = \sum_{d=1}^{N} \left[ \sum (W_{\text{pad}} \times C_{\text{pad}}) + \sum (W_{\text{tampon}} \times C_{\text{tampon}}) + \sum (W_{\text{clot}} \times C_{\text{clot}}) \right]$$

Where:
- $W_{\text{pad\_light}} = 1,\; W_{\text{pad\_mod}} = 5,\; W_{\text{pad\_soaked}} = 20$
- $W_{\text{tampon\_light}} = 1,\; W_{\text{tampon\_mod}} = 5,\; W_{\text{tampon\_soaked}} = 20$
- $W_{\text{clot\_small}} = 1,\; W_{\text{clot\_large}} = 5$
- **Clinical Menorrhagia Threshold:** $\text{PBAC Score} \ge 100 \implies >80\text{ mL blood loss}$.

### 4.3 Preliminary Results & Validation
The embedded test suite in `Backend.java` validates 17 rigorous test vectors across normal, borderline, and severe clinical scenarios:
- **Test 1–6:** Pad and tampon saturation point fidelity across light, moderate, and soaked states.
- **Test 7–9:** Clot scoring accuracy for small ($1\text{ pt}$) and large ($5\text{ pts}$) clots.
- **Test 10–12:** Clinical threshold boundary conditions ($Score = 99 \to \text{Normal}$, $Score = 100 \to \text{HMB}$, $Score = 250 \to \text{Severe}$).
- **Test 13–15:** Prolonged bleeding detection (Cycle duration $>8\text{ days}$).
- **Test 16–17:** Anemia risk stratification across balanced dietary vs. symptomatic vegan cohorts.

**Test Execution Output:**
```
Running FemCare+ Clinical Verification Engine...
[PASS] Pad saturation scoring verified.
[PASS] Tampon absorption scoring verified.
[PASS] Clot dimension weights verified.
[PASS] Clinical menorrhagia cutoff (Score >= 100) validated.
[PASS] Multi-factor Anemia Risk Stratification validated.
======================================================
TOTAL TESTS: 17 | PASSED: 17 | FAILED: 0 | ACCURACY: 100%
======================================================
```

---

## CHAPTER 5: CONCLUSION & FUTURE SCOPE

### 5.1 Conclusion
The **FemCare+** platform successfully bridges the gap between everyday menstrual cycle tracking and clinically validated health screening. By engineering a zero-dependency Java backend combined with a high-performance, accessible frontend, the system provides an objective, private, and localized tool for screening Heavy Menstrual Bleeding and nutritional anemia risk. The integration of dynamic period duration bounding and multi-level data controls ensures both data integrity and user trust.

### 5.2 Future Scope
1. **Machine Learning Predictive Modeling:** Incorporating time-series cycle forecasting to identify subtle cycle variations over longitudinal 12-month spans.
2. **Offline-First PWA:** Packaging the platform as an installable Progressive Web App with service-worker caching for rural healthcare workers (ASHA workers).
3. **PDF Clinical Summary Generation:** Enabling users to export structured, clinically formatted PBAC reports to share directly with their gynecologists.

---

## REFERENCES

1. J. M. Higham, P. M. O’Brien, and R. W. Shaw, "Assessment of menstrual blood loss using a pictorial chart," *British Journal of Obstetrics and Gynaecology*, vol. 97, no. 8, pp. 734–739, Aug. 1990.
2. I. S. Fraser, H. O. Critchley, M. G. Munro, and M. Broder, "The FIGO recommendations on terminologies and definitions for normal and abnormal uterine bleeding," *International Journal of Gynecology & Obstetrics*, vol. 113, no. 1, pp. 3–13, Apr. 2011.
3. M. G. Munro, H. O. Critchley, and I. S. Fraser, "The two FIGO systems for normal and abnormal uterine bleeding symptoms and classification of causes of abnormal uterine bleeding in the reproductive years," *International Journal of Gynecology & Obstetrics*, vol. 143, no. 3, pp. 393–408, Dec. 2018.
4. World Health Organization, "Nutritional anaemias: tools for effective prevention and control," *WHO Guidelines Approved by the Guidelines Review Committee*, Geneva, 2017.
5. Ministry of Health and Family Welfare (MoHFW), Government of India, "Anemia Mukt Bharat: Operational Guidelines for Intensified National Iron Plus Initiative," New Delhi, 2018.
6. International Institute for Population Sciences (IIPS) and ICF, "National Family Health Survey (NFHS-5), 2019-21: India," Mumbai, India, 2021.
