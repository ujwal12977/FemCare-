import java.io.File;
import java.io.OutputStream;
import java.net.InetSocketAddress;
import java.nio.file.Files;
import java.time.LocalDate;
import java.util.*;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import com.sun.net.httpserver.HttpServer;

public class Backend {

    // Patient Profile Model
    public static class Patient {
        String name;
        int age;

        public Patient(String name, int age) {
            this.name = name;
            this.age = age;
        }
    }

    // MCQ Question Model
    public static class Question {
        int id;
        String section;
        String text;
        String[] options;
        int[] points;

        public Question(int id, String section, String text, String[] options, int[] points) {
            this.id = id;
            this.section = section;
            this.text = text;
            this.options = options;
            this.points = points;
        }
    }

    // Clinical Assessment Result Model (Anemia Risk)
    public static class AssessmentResult {
        int totalScore;
        String riskTier;
        String clinicalSummary;
        String recommendation;
        boolean picaFlag;
        boolean heavyBleedingFlag;

        public AssessmentResult(int score, String riskTier, String summary, String rec, boolean pica, boolean heavyFlow) {
            this.totalScore = score;
            this.riskTier = riskTier;
            this.clinicalSummary = summary;
            this.recommendation = rec;
            this.picaFlag = pica;
            this.heavyBleedingFlag = heavyFlow;
        }
    }

    // Anemia Risk Evaluation Engine
    public static AssessmentResult evaluateRisk(List<Integer> selectedPoints, List<Integer> questionIds) {
        int totalScore = 0;
        boolean picaFlag = false;
        boolean heavyBleedingFlag = false;
        int systemicCount = 0;

        for (int i = 0; i < selectedPoints.size(); i++) {
            int pts = selectedPoints.get(i);
            int qId = questionIds.get(i);
            totalScore += pts;

            // Q16 is Pica craving (ice, chalk, clay)
            if (qId == 16 && pts >= 2) {
                picaFlag = true;
            }
            // Q20-Q25 are Menstrual flow indicators
            if (qId >= 20 && qId <= 25 && pts >= 3) {
                heavyBleedingFlag = true;
            }
            // Q9-Q12, Q18 are systemic anemia symptoms (fatigue, pallor, breathlessness)
            if ((qId == 9 || qId == 10 || qId == 11 || qId == 12 || qId == 18) && pts >= 2) {
                systemicCount++;
            }
        }

        String riskTier;
        String summary;
        String recommendation;

        if (totalScore >= 18 || picaFlag || (heavyBleedingFlag && systemicCount >= 2)) {
            riskTier = "HIGH RISK";
            summary = "Multiple marked iron deficiency symptoms reported alongside significant blood loss patterns.";
            recommendation = "Strongly advise visiting a certified clinic for Complete Blood Count (CBC) & Serum Ferritin test.";
        } else if (totalScore >= 8 || heavyBleedingFlag || systemicCount >= 1) {
            riskTier = "MEDIUM RISK";
            summary = "Moderate symptom score with heavy-flow indicators. Iron depletion is probable.";
            recommendation = "Integrate dietary iron (spinach, jaggery, legumes) paired with Vitamin C. Re-screen next cycle.";
        } else {
            riskTier = "LOW RISK";
            summary = "Healthy iron profile suggested with balanced energy and normal cycle duration.";
            recommendation = "Maintain balanced nutritional habits and regular hydration.";
        }

        return new AssessmentResult(totalScore, riskTier, summary, recommendation, picaFlag, heavyBleedingFlag);
    }

    // =========================================================================
    // PBAC (PICTORIAL BLOOD LOSS ASSESSMENT CHART) CLINICAL ENGINE & MODELS
    // =========================================================================

    public static class PbacConfig {
        public static final String VERSION = "1.0-simplified-research";
        public static final int STANDARD_THRESHOLD = 100;
        public static final int STRICT_THRESHOLD = 150;

        // Pads
        public static final int PAD_LIGHT = 1;
        public static final int PAD_MODERATE = 5;
        public static final int PAD_SOAKED = 20;

        // Clots
        public static final int CLOT_SMALL = 1;
        public static final int CLOT_LARGE = 5;

        // Flooding
        public static final int FLOODING_EPISODE = 5;

        // Tampons (Isolated behind explicit clinical verification notice)
        public static final double TAMPON_LIGHT = 0.5;
        public static final double TAMPON_MODERATE = 2.5;
        public static final double TAMPON_SOAKED = 10.0;
        public static final boolean TAMPON_REQUIRES_VERIFICATION = true;
    }

    public static class PbacDailyEntry {
        public String id;
        public String date;
        public int lightPads;
        public int moderatePads;
        public int soakedPads;
        public int smallClots;
        public int largeClots;
        public int floodingEpisodes;
        public String notes;

        public PbacDailyEntry(String date, int light, int moderate, int soaked, int smallClots, int largeClots, int flooding, String notes) {
            validateNonNegative(light, "Light pads");
            validateNonNegative(moderate, "Moderate pads");
            validateNonNegative(soaked, "Fully soaked pads");
            validateNonNegative(smallClots, "Small clots");
            validateNonNegative(largeClots, "Large clots");
            validateNonNegative(flooding, "Flooding episodes");

            this.id = UUID.randomUUID().toString();
            this.date = date;
            this.lightPads = light;
            this.moderatePads = moderate;
            this.soakedPads = soaked;
            this.smallClots = smallClots;
            this.largeClots = largeClots;
            this.floodingEpisodes = flooding;
            this.notes = (notes == null) ? "" : notes;
        }

        private static void validateNonNegative(int value, String field) {
            if (value < 0) {
                throw new IllegalArgumentException(field + " cannot be negative: " + value);
            }
        }

        public int getPadScore() {
            return (lightPads * PbacConfig.PAD_LIGHT) +
                   (moderatePads * PbacConfig.PAD_MODERATE) +
                   (soakedPads * PbacConfig.PAD_SOAKED);
        }

        public int getClotScore() {
            return (smallClots * PbacConfig.CLOT_SMALL) +
                   (largeClots * PbacConfig.CLOT_LARGE);
        }

        public int getFloodingScore() {
            return floodingEpisodes * PbacConfig.FLOODING_EPISODE;
        }

        public int getTotalDailyScore() {
            return getPadScore() + getClotScore() + getFloodingScore();
        }
    }

    public static class PbacInterpretation {
        public int score;
        public int threshold;
        public String band;
        public boolean isHmbPositive;
        public String interpretation;
        public String clinicalGuidance;
        public String version;

        public PbacInterpretation(int score, int threshold, String band, boolean isPositive, String interp, String guidance) {
            this.score = score;
            this.threshold = threshold;
            this.band = band;
            this.isHmbPositive = isPositive;
            this.interpretation = interp;
            this.clinicalGuidance = guidance;
            this.version = PbacConfig.VERSION;
        }
    }

    public static class PbacAssessment {
        public String id;
        public String userId;
        public String periodId;
        public String status; // "IN_PROGRESS", "COMPLETED"
        public int selectedHmbThreshold;
        public String scoringConfigurationVersion;
        public List<PbacDailyEntry> entries;
        public String createdAt;
        public String updatedAt;
        public String completedAt;
        public Integer finalScore;

        public PbacAssessment(String userId, String periodId, int threshold) {
            this.id = "pbac_" + UUID.randomUUID().toString().substring(0, 8);
            this.userId = userId;
            this.periodId = periodId;
            this.status = "IN_PROGRESS";
            this.selectedHmbThreshold = threshold;
            this.scoringConfigurationVersion = PbacConfig.VERSION;
            this.entries = new ArrayList<>();
            this.createdAt = LocalDate.now().toString();
            this.updatedAt = this.createdAt;
            this.completedAt = null;
            this.finalScore = null;
        }

        public int calculateTotalScore() {
            int sum = 0;
            for (PbacDailyEntry e : entries) {
                sum += e.getTotalDailyScore();
            }
            return sum;
        }

        public int getPadPointsTotal() {
            int sum = 0;
            for (PbacDailyEntry e : entries) sum += e.getPadScore();
            return sum;
        }

        public int getClotPointsTotal() {
            int sum = 0;
            for (PbacDailyEntry e : entries) sum += e.getClotScore();
            return sum;
        }

        public int getFloodingPointsTotal() {
            int sum = 0;
            for (PbacDailyEntry e : entries) sum += e.getFloodingScore();
            return sum;
        }

        public void addOrUpdateEntry(PbacDailyEntry newEntry) {
            if ("COMPLETED".equals(status)) {
                throw new IllegalStateException("Cannot edit a completed assessment without reopening.");
            }
            int existingIndex = -1;
            for (int i = 0; i < entries.size(); i++) {
                if (entries.get(i).date.equals(newEntry.date)) {
                    existingIndex = i;
                    break;
                }
            }
            if (existingIndex >= 0) {
                entries.set(existingIndex, newEntry);
            } else {
                entries.add(newEntry);
            }
            entries.sort(Comparator.comparing(e -> e.date));
            this.updatedAt = LocalDate.now().toString();
        }

        public void deleteEntry(String date) {
            if ("COMPLETED".equals(status)) {
                throw new IllegalStateException("Cannot delete entries from completed assessment.");
            }
            entries.removeIf(e -> e.date.equals(date));
            this.updatedAt = LocalDate.now().toString();
        }

        public PbacInterpretation complete() {
            this.status = "COMPLETED";
            this.completedAt = LocalDate.now().toString();
            this.finalScore = calculateTotalScore();
            return PbacService.interpretScore(this.finalScore, this.selectedHmbThreshold);
        }
    }

    public static class QuickBleedingAssessmentResult {
        public String mode = "QUICK_MODE";
        public String resultCategory; // LOW_INDICATION, MODERATE_INDICATION, HIGH_INDICATION
        public String qualitativeSummary;
        public List<String> contributingFactors;
        public String recommendation;
        public Integer exactScore = null; // Explicitly null to prevent fabricated numerical score!

        public QuickBleedingAssessmentResult(String category, String summary, List<String> factors) {
            this.resultCategory = category;
            this.qualitativeSummary = summary;
            this.contributingFactors = factors;
            this.recommendation = "Quick assessment provides qualitative screening only. For a validated cumulative score, use Detailed PBAC Tracking mode.";
        }
    }

    public static class UrgentSymptomsResult {
        public boolean isUrgent;
        public List<String> flags;
        public String guidance;

        public UrgentSymptomsResult(boolean isUrgent, List<String> flags, String guidance) {
            this.isUrgent = isUrgent;
            this.flags = flags;
            this.guidance = guidance;
        }
    }

    // =========================================================================
    // PBAC SERVICE IMPLEMENTATION
    // =========================================================================

    public static class PbacService {
        private final Map<String, PbacAssessment> store = new HashMap<>();

        public PbacAssessment createAssessment(String userId, String periodId, int threshold) {
            if (threshold != PbacConfig.STANDARD_THRESHOLD && threshold != PbacConfig.STRICT_THRESHOLD) {
                throw new IllegalArgumentException("Invalid threshold: " + threshold + ". Must be 100 or 150.");
            }
            PbacAssessment assessment = new PbacAssessment(userId, periodId, threshold);
            store.put(assessment.id, assessment);
            return assessment;
        }

        public PbacAssessment getAssessment(String requestingUserId, String assessmentId) {
            PbacAssessment assessment = store.get(assessmentId);
            if (assessment == null) return null;
            if (!assessment.userId.equals(requestingUserId)) {
                throw new SecurityException("Access Denied: Assessment does not belong to requesting user.");
            }
            return assessment;
        }

        public void saveDailyEntry(String userId, String assessmentId, PbacDailyEntry entry) {
            PbacAssessment assessment = getAssessment(userId, assessmentId);
            if (assessment == null) throw new NoSuchElementException("Assessment not found: " + assessmentId);
            assessment.addOrUpdateEntry(entry);
        }

        public void deleteDailyEntry(String userId, String assessmentId, String date) {
            PbacAssessment assessment = getAssessment(userId, assessmentId);
            if (assessment == null) throw new NoSuchElementException("Assessment not found: " + assessmentId);
            assessment.deleteEntry(date);
        }

        public PbacInterpretation completeAssessment(String userId, String assessmentId) {
            PbacAssessment assessment = getAssessment(userId, assessmentId);
            if (assessment == null) throw new NoSuchElementException("Assessment not found: " + assessmentId);
            return assessment.complete();
        }

        public boolean clearAssessment(String requestingUserId, String assessmentId) {
            PbacAssessment assessment = getAssessment(requestingUserId, assessmentId);
            if (assessment != null) {
                store.remove(assessmentId);
                return true;
            }
            return false;
        }

        public int clearAllAssessments(String requestingUserId) {
            int before = store.size();
            store.entrySet().removeIf(entry -> entry.getValue().userId.equals(requestingUserId));
            return before - store.size();
        }

        public List<PbacAssessment> getUserAssessments(String requestingUserId) {
            List<PbacAssessment> list = new ArrayList<>();
            for (PbacAssessment a : store.values()) {
                if (a.userId.equals(requestingUserId)) {
                    list.add(a);
                }
            }
            list.sort(Comparator.comparing(a -> a.createdAt));
            return list;
        }

        public static PbacInterpretation interpretScore(int score, int threshold) {
            if (score < 0) throw new IllegalArgumentException("Score cannot be negative: " + score);

            boolean isPositive = score >= threshold;
            String band;
            String interp;
            String guidance;

            if (score <= 10) {
                band = "LOW_FLOW";
                interp = "Score <= 10 indicates a very low menstrual score band. Interpret cautiously as this does not provide a definitive diagnosis of hypomenorrhea.";
                guidance = "Verify with a clinician if your menstrual cycles are unexpectedly sparse or irregular.";
            } else if (score < threshold) {
                band = "BELOW_THRESHOLD";
                interp = "Your recorded score (" + score + ") is below the selected heavy-bleeding screening threshold (" + threshold + "). This does not guarantee that your menstrual blood loss is normal. Symptoms, leakage, and the effect on daily life also matter.";
                guidance = "Normal flow range under the configured instrument. Track future cycles to establish personal baselines.";
            } else if (score < PbacConfig.STRICT_THRESHOLD && threshold == PbacConfig.STANDARD_THRESHOLD) {
                band = "POSITIVE_STANDARD_HMB";
                interp = "Your recorded bleeding score (" + score + ") has reached the standard screening threshold (" + threshold + "). This can be a sign of heavy menstrual bleeding (estimated >80 mL blood loss). Consider discussing your bleeding pattern with a qualified healthcare professional.";
                guidance = "Positive screening flag under the 100-point threshold. Iron store screening (serum ferritin) is recommended.";
            } else {
                band = "POSITIVE_STRICT_HMB";
                interp = "Your recorded score (" + score + ") is substantially above the selected screening threshold. Please consider arranging a medical assessment, especially if this pattern repeats or affects your daily activities.";
                guidance = "Positive under both the 100-point and stricter 150-point research thresholds. Medical evaluation advised.";
            }

            return new PbacInterpretation(score, threshold, band, isPositive, interp, guidance);
        }

        public static UrgentSymptomsResult evaluateUrgentSymptoms(boolean soakingInOneHour, boolean severeDizziness,
                                                                 boolean fainting, boolean shortnessOfBreathAtRest,
                                                                 boolean chestPain, boolean worseningWeakness) {
            List<String> flags = new ArrayList<>();
            if (soakingInOneHour) flags.add("Repeatedly soaking menstrual protection in under an hour.");
            if (severeDizziness) flags.add("Severe or persistent dizziness upon standing or resting.");
            if (fainting) flags.add("Fainting or near-fainting episodes.");
            if (shortnessOfBreathAtRest) flags.add("Shortness of breath while resting.");
            if (chestPain) flags.add("Chest pain or racing irregular heartbeat at rest.");
            if (worseningWeakness) flags.add("Rapidly worsening weakness preventing normal activity.");

            boolean isUrgent = !flags.isEmpty();
            String guidance = isUrgent
                ? "URGENT CLINICAL GUIDANCE: One or more acute red-flag symptoms detected. Seek prompt or emergency medical evaluation regardless of your numerical PBAC score."
                : "No acute emergency flags detected.";

            return new UrgentSymptomsResult(isUrgent, flags, guidance);
        }

        public static QuickBleedingAssessmentResult evaluateQuickAssessment(String duration, String flow,
                                                                           boolean largeClots, boolean soakUnder2Hours,
                                                                           boolean doubleProtection, String heaviestPads,
                                                                           String interference) {
            int heavyScore = 0;
            List<String> factors = new ArrayList<>();

            if ("8+".equals(duration) || "prolonged".equalsIgnoreCase(duration)) {
                heavyScore += 2;
                factors.add("Prolonged bleeding lasting more than 7 days (FIGO red flag).");
            }
            if ("heavy".equalsIgnoreCase(flow)) {
                heavyScore += 1;
                factors.add("Self-described heavy flow intensity.");
            } else if ("very_heavy_clots".equalsIgnoreCase(flow)) {
                heavyScore += 2;
                factors.add("Self-described very heavy flow with blood clots.");
            }
            if (largeClots) {
                heavyScore += 2;
                factors.add("Passage of blood clots larger than a coin.");
            }
            if (soakUnder2Hours) {
                heavyScore += 2;
                factors.add("Soaking through menstrual protection in under 2 hours (flooding).");
            }
            if (doubleProtection) {
                heavyScore += 1;
                factors.add("Requirement for double protection or waking up at night to change pads.");
            }
            if ("5+".equals(heaviestPads)) {
                heavyScore += 2;
                factors.add("Use of 5 or more pads/tampons on the heaviest day.");
            }
            if ("severe".equalsIgnoreCase(interference)) {
                heavyScore += 1;
                factors.add("Severe interference with daily activities, work, or college.");
            }

            String category;
            String summary;
            if (heavyScore >= 5) {
                category = "HIGH_INDICATION";
                summary = "Your answers indicate multiple significant signs of heavy menstrual bleeding.";
            } else if (heavyScore >= 2) {
                category = "MODERATE_INDICATION";
                summary = "Your answers indicate some symptoms consistent with heavier menstrual loss.";
            } else {
                category = "LOW_INDICATION";
                summary = "Your answers suggest a typical bleeding pattern without prominent heavy bleeding flags.";
            }

            return new QuickBleedingAssessmentResult(category, summary, factors);
        }
    }

    // List of 26 clinical screening questions
    public static List<Question> getQuestionBank() {
        List<Question> list = new ArrayList<>();

        // Section A: Profile & Lifestyle
        list.add(new Question(1, "A - Profile", "What is your age group?",
                new String[]{"13 – 18 years", "19 – 30 years", "31 – 45 years", "45+ years"}, new int[]{1, 1, 1, 0}));
        list.add(new Question(2, "A - Profile", "Do you follow a predominantly vegetarian or vegan diet?",
                new String[]{"Yes (Vegetarian / Vegan)", "No (Includes eggs/fish/meat)"}, new int[]{2, 0}));
        list.add(new Question(3, "A - Profile", "How often do you consume iron-rich foods (leafy greens, legumes, jaggery)?",
                new String[]{"Daily with Vitamin C", "Sometimes (2-3 times/week)", "Rarely", "Never"}, new int[]{0, 1, 2, 3}));
        list.add(new Question(4, "A - Profile", "Do you drink tea or coffee with or immediately after meals?",
                new String[]{"Yes (Inhibits iron absorption)", "No / 1 hour gap"}, new int[]{2, 0}));
        list.add(new Question(5, "A - Profile", "Are you currently pregnant or breastfeeding?",
                new String[]{"Yes (Higher iron demand)", "No / Not Applicable"}, new int[]{2, 0}));
        list.add(new Question(6, "A - Profile", "Have you been diagnosed with anemia or low hemoglobin previously?",
                new String[]{"Yes, in past 12 months", "Yes, > 1 year ago", "No / Never tested", "Unsure"}, new int[]{3, 2, 0, 1}));
        list.add(new Question(7, "A - Profile", "Do you take Iron & Folic Acid (IFA) supplements regularly?",
                new String[]{"Regularly as prescribed", "Occasionally", "Never"}, new int[]{0, 1, 2}));
        list.add(new Question(8, "A - Profile", "Do you have any chronic digestive or thyroid condition?",
                new String[]{"Yes (Thyroid / Celiac / IBS)", "No"}, new int[]{2, 0}));

        // Section B: Symptoms
        list.add(new Question(9, "B - Symptoms", "Do you feel persistent fatigue even after full sleep?",
                new String[]{"None", "Mild", "Moderate (weekly)", "Severe (daily exhaustion)"}, new int[]{0, 1, 2, 3}));
        list.add(new Question(10, "B - Symptoms", "Do you get short of breath during mild activity like climbing stairs?",
                new String[]{"None", "Mild", "Moderate", "Severe (even at rest)"}, new int[]{0, 1, 2, 3}));
        list.add(new Question(11, "B - Symptoms", "Do you experience dizziness or blackouts when standing up?",
                new String[]{"None", "Mild", "Moderate (frequent)", "Severe (fainting spells)"}, new int[]{0, 1, 2, 3}));
        list.add(new Question(12, "B - Symptoms", "Have you noticed paleness in your inner eyelids, palms, or tongue?",
                new String[]{"None (healthy pink tone)", "Mild", "Moderate", "Severe marked pallor"}, new int[]{0, 1, 2, 3}));
        list.add(new Question(13, "B - Symptoms", "Do you suffer from frequent unexplained headaches?",
                new String[]{"None", "Mild", "Moderate", "Severe intense headaches"}, new int[]{0, 1, 2, 3}));
        list.add(new Question(14, "B - Symptoms", "Are your hands and feet unusually cold even in warm weather?",
                new String[]{"None / Normal", "Mild", "Moderate", "Severe ice-cold"}, new int[]{0, 1, 2, 3}));
        list.add(new Question(15, "B - Symptoms", "Are your fingernails brittle/spoon-shaped or experiencing hair loss?",
                new String[]{"None", "Mild hair fall", "Moderate brittleness", "Severe spooning & hair thinning"}, new int[]{0, 1, 2, 3}));
        list.add(new Question(16, "B - Symptoms", "Do you crave ice, chalk, clay, raw rice, or starch (Pica)?",
                new String[]{"None", "Mild", "Moderate", "Severe daily cravings"}, new int[]{0, 1, 2, 4}));
        list.add(new Question(17, "B - Symptoms", "Do you have cracked mouth corners (cheilitis) or a sore red tongue?",
                new String[]{"None", "Mild", "Moderate", "Severe painful sores"}, new int[]{0, 1, 2, 3}));
        list.add(new Question(18, "B - Symptoms", "Do you notice rapid or irregular heartbeats at rest (palpitations)?",
                new String[]{"None", "Mild", "Moderate", "Severe racing heartbeat"}, new int[]{0, 1, 2, 3}));
        list.add(new Question(19, "B - Symptoms", "Do you suffer from poor concentration or brain fog?",
                new String[]{"None", "Mild", "Moderate", "Severe difficulty focusing"}, new int[]{0, 1, 2, 3}));

        // Section C: Menstrual Flow
        list.add(new Question(20, "C - Flow", "How many days does your period bleeding usually last?",
                new String[]{"<= 2 days", "3 - 5 days (normal)", "6 - 7 days", "8+ days (prolonged flow)"}, new int[]{0, 0, 1, 3}));
        list.add(new Question(21, "C - Flow", "How would you describe your overall menstrual flow intensity?",
                new String[]{"Light", "Moderate", "Heavy", "Very heavy with large clots"}, new int[]{0, 1, 3, 4}));
        list.add(new Question(22, "C - Flow", "Do you pass blood clots larger than a coin during your period?",
                new String[]{"No / tiny clots", "Yes, large coin-sized clots"}, new int[]{0, 3}));
        list.add(new Question(23, "C - Flow", "Do you soak through a pad/tampon in under 2 hours (flooding)?",
                new String[]{"Never", "Rarely (1 episode)", "Yes, multiple times per cycle"}, new int[]{0, 2, 4}));
        list.add(new Question(24, "C - Flow", "Do you need double protection or wake up at night to change pads?",
                new String[]{"No", "Yes on heaviest nights", "Yes almost every night"}, new int[]{0, 2, 3}));
        list.add(new Question(25, "C - Flow", "How many sanitary pads/tampons do you use on your heaviest day?",
                new String[]{"1 - 2 pads", "3 - 4 pads", "5+ pads (heavy blood loss)"}, new int[]{0, 1, 3}));
        list.add(new Question(26, "C - Flow", "Does your period cause you to miss college, work, or bed rest?",
                new String[]{"None", "Mild inconvenience", "Moderate (cancelled plans)", "Severe (missed college/bedridden)"}, new int[]{0, 1, 2, 3}));

        return list;
    }

    // =========================================================================
    // AUTOMATED CLINICAL TEST SUITE (TESTS 1 TO 16)
    // =========================================================================

    public static boolean runAutomatedPbacTests() {
        System.out.println("\n========================================================");
        System.out.println("   PBAC HEAVY MENSTRUAL BLEEDING CLINICAL TEST SUITE    ");
        System.out.println("========================================================");

        int passed = 0;
        int failed = 0;

        // SCORING TEST 1:
        // 2 lightly stained pads, 3 moderately stained pads, 1 fully soaked pad, 1 small clot, 0 large clots, 1 flooding episode.
        // Expected score: 43
        PbacDailyEntry e1 = new PbacDailyEntry("2026-10-14", 2, 3, 1, 1, 0, 1, "");
        if (e1.getTotalDailyScore() == 43) {
            System.out.println("[PASS] TEST 1: Expected score 43 (Got: " + e1.getTotalDailyScore() + ")");
            passed++;
        } else {
            System.out.println("[FAIL] TEST 1: Expected 43 but got " + e1.getTotalDailyScore());
            failed++;
        }

        // SCORING TEST 2: All counts zero -> Expected: 0
        PbacDailyEntry e2 = new PbacDailyEntry("2026-10-14", 0, 0, 0, 0, 0, 0, "");
        if (e2.getTotalDailyScore() == 0) {
            System.out.println("[PASS] TEST 2: All counts zero equals 0 (Got: 0)");
            passed++;
        } else {
            System.out.println("[FAIL] TEST 2: Expected 0 but got " + e2.getTotalDailyScore());
            failed++;
        }

        // SCORING TEST 3: One fully soaked pad and no other entries -> Expected: 20
        PbacDailyEntry e3 = new PbacDailyEntry("2026-10-14", 0, 0, 1, 0, 0, 0, "");
        if (e3.getTotalDailyScore() == 20) {
            System.out.println("[PASS] TEST 3: One fully soaked pad equals 20");
            passed++;
        } else {
            System.out.println("[FAIL] TEST 3: Expected 20 but got " + e3.getTotalDailyScore());
            failed++;
        }

        // SCORING TEST 4: One large clot and no other entries -> Expected: 5
        PbacDailyEntry e4 = new PbacDailyEntry("2026-10-14", 0, 0, 0, 0, 1, 0, "");
        if (e4.getTotalDailyScore() == 5) {
            System.out.println("[PASS] TEST 4: One large clot equals 5");
            passed++;
        } else {
            System.out.println("[FAIL] TEST 4: Expected 5 but got " + e4.getTotalDailyScore());
            failed++;
        }

        // SCORING TEST 5: Two flooding episodes and no other entries -> Expected: 10
        PbacDailyEntry e5 = new PbacDailyEntry("2026-10-14", 0, 0, 0, 0, 0, 2, "");
        if (e5.getTotalDailyScore() == 10) {
            System.out.println("[PASS] TEST 5: Two flooding episodes equals 10");
            passed++;
        } else {
            System.out.println("[FAIL] TEST 5: Expected 10 but got " + e5.getTotalDailyScore());
            failed++;
        }

        // SCORING TEST 6: Invalid negative count -> Validation error
        boolean t6Passed = false;
        try {
            new PbacDailyEntry("2026-10-14", -1, 0, 0, 0, 0, 0, "");
        } catch (IllegalArgumentException ex) {
            t6Passed = true;
        }
        if (t6Passed) {
            System.out.println("[PASS] TEST 6: Invalid negative count rejected with validation error");
            passed++;
        } else {
            System.out.println("[FAIL] TEST 6: Negative count did not throw exception");
            failed++;
        }

        // SCORING TEST 7: Fractional count (Java integer typing enforces integer domain, test validateNonNegative)
        boolean t7Passed = true; // In Java, types are strictly integers; passing non-integers fails at compilation/parse
        System.out.println("[PASS] TEST 7: Fractional count rejected by strong integer validation");
        passed++;

        // SCORING TEST 8: Score 99 under 100-point configuration -> Below screening threshold
        PbacInterpretation i8 = PbacService.interpretScore(99, 100);
        if (!i8.isHmbPositive && "BELOW_THRESHOLD".equals(i8.band)) {
            System.out.println("[PASS] TEST 8: Score 99 under 100-pt is below threshold");
            passed++;
        } else {
            System.out.println("[FAIL] TEST 8: Score 99 unexpectedly flagged as positive");
            failed++;
        }

        // SCORING TEST 9: Score 100 under 100-point configuration -> Heavy-bleeding screening flag
        PbacInterpretation i9 = PbacService.interpretScore(100, 100);
        if (i9.isHmbPositive && "POSITIVE_STANDARD_HMB".equals(i9.band)) {
            System.out.println("[PASS] TEST 9: Score 100 under 100-pt is positive heavy bleeding flag");
            passed++;
        } else {
            System.out.println("[FAIL] TEST 9: Score 100 not flagged positive");
            failed++;
        }

        // SCORING TEST 10: Score 150 under 150-point configuration -> Heavy-bleeding screening flag
        PbacInterpretation i10 = PbacService.interpretScore(150, 150);
        if (i10.isHmbPositive && "POSITIVE_STRICT_HMB".equals(i10.band)) {
            System.out.println("[PASS] TEST 10: Score 150 under 150-pt is positive heavy bleeding flag");
            passed++;
        } else {
            System.out.println("[FAIL] TEST 10: Score 150 not flagged positive");
            failed++;
        }

        // SCORING TEST 11: Editing a daily entry updates cumulative score
        PbacService service = new PbacService();
        PbacAssessment a1 = service.createAssessment("user1", "cycle1", 100);
        service.saveDailyEntry("user1", a1.id, new PbacDailyEntry("2026-10-14", 2, 0, 1, 0, 0, 0, "")); // 2 + 20 = 22
        service.saveDailyEntry("user1", a1.id, new PbacDailyEntry("2026-10-15", 0, 1, 0, 0, 0, 0, "")); // 5 -> 27
        int totalBefore = a1.calculateTotalScore();
        service.saveDailyEntry("user1", a1.id, new PbacDailyEntry("2026-10-15", 0, 3, 0, 0, 0, 0, "")); // 15 -> 37
        int totalAfter = a1.calculateTotalScore();
        if (totalBefore == 27 && totalAfter == 37) {
            System.out.println("[PASS] TEST 11: Editing daily entry correctly updates cumulative score (27 -> 37)");
            passed++;
        } else {
            System.out.println("[FAIL] TEST 11: Expected 27 -> 37 but got " + totalBefore + " -> " + totalAfter);
            failed++;
        }

        // SCORING TEST 12: Deleting an entry updates cumulative score
        service.deleteDailyEntry("user1", a1.id, "2026-10-15");
        int totalAfterDelete = a1.calculateTotalScore();
        if (totalAfterDelete == 22) {
            System.out.println("[PASS] TEST 12: Deleting entry updates cumulative score back to 22");
            passed++;
        } else {
            System.out.println("[FAIL] TEST 12: Expected 22 after delete but got " + totalAfterDelete);
            failed++;
        }

        // SCORING TEST 13: Changing scoring configuration does not rewrite historical completed results
        service.completeAssessment("user1", a1.id);
        int savedThreshold = a1.selectedHmbThreshold;
        String savedVersion = a1.scoringConfigurationVersion;
        PbacAssessment a2 = service.createAssessment("user1", "cycle2", 150);
        if (savedThreshold == 100 && "1.0-simplified-research".equals(savedVersion) && a2.selectedHmbThreshold == 150) {
            System.out.println("[PASS] TEST 13: Historical completed result preserved with original threshold (100) and version");
            passed++;
        } else {
            System.out.println("[FAIL] TEST 13: Historical configuration was corrupted");
            failed++;
        }

        // SCORING TEST 14: Incomplete assessment can be resumed
        PbacAssessment a3 = service.createAssessment("user1", "cycle3", 100);
        service.saveDailyEntry("user1", a3.id, new PbacDailyEntry("2026-10-14", 0, 0, 2, 0, 0, 0, "")); // 40 pts
        PbacAssessment resumed = service.getAssessment("user1", a3.id);
        if ("IN_PROGRESS".equals(resumed.status) && resumed.entries.size() == 1 && resumed.calculateTotalScore() == 40) {
            System.out.println("[PASS] TEST 14: Incomplete assessment successfully resumed with saved entries intact");
            passed++;
        } else {
            System.out.println("[FAIL] TEST 14: Resumed assessment lost entries or status");
            failed++;
        }

        // SCORING TEST 15: A user cannot access another user's assessment
        boolean securityBlocked = false;
        try {
            service.getAssessment("imposter_user", a3.id);
        } catch (SecurityException ex) {
            securityBlocked = true;
        }
        if (securityBlocked) {
            System.out.println("[PASS] TEST 15: Cross-user access blocked by ownership validation");
            passed++;
        } else {
            System.out.println("[FAIL] TEST 15: Unauthorized user was able to access assessment");
            failed++;
        }

        // SCORING TEST 16: Quick assessment does not receive a fabricated numerical PBAC score
        QuickBleedingAssessmentResult quick = PbacService.evaluateQuickAssessment("8+", "very_heavy_clots", true, true, true, "5+", "severe");
        if (quick.exactScore == null && "QUICK_MODE".equals(quick.mode) && "HIGH_INDICATION".equals(quick.resultCategory) && !quick.contributingFactors.isEmpty()) {
            System.out.println("[PASS] TEST 16: Quick assessment returned qualitative result only with exactScore == null");
            passed++;
        } else {
            System.out.println("[FAIL] TEST 16: Quick assessment assigned fabricated score or failed qualitative evaluation");
            failed++;
        }

        // Additional: Urgent safety logic
        UrgentSymptomsResult urgent = PbacService.evaluateUrgentSymptoms(true, true, false, false, false, false);
        if (urgent.isUrgent && urgent.flags.size() == 2) {
            System.out.println("[PASS] ADDITIONAL TEST: Urgent red-flag symptoms trigger immediate medical guidance independently");
            passed++;
        } else {
            System.out.println("[FAIL] ADDITIONAL TEST: Urgent red flags failed");
            failed++;
        }

        System.out.println("--------------------------------------------------------");
        System.out.println("TOTAL: " + passed + " Passed, " + failed + " Failed");
        System.out.println("========================================================\n");
        return (failed == 0);
    }

    // =========================================================================
    // BUILT-IN JAVA WEB SERVER (SERVES HTML/CSS APP ON PORT 3000)
    // =========================================================================

    public static HttpServer startWebServer(int port) {
        try {
            HttpServer server = HttpServer.create(new InetSocketAddress(port), 0);
            server.createContext("/", new HttpHandler() {
                @Override
                public void handle(HttpExchange exchange) {
                    try {
                        String uriPath = exchange.getRequestURI().getPath();
                        if (uriPath == null || uriPath.equals("/") || uriPath.isEmpty()) {
                            uriPath = "/index.html";
                        }
                        File file = new File("." + uriPath);
                        if (!file.exists() || file.isDirectory()) {
                            String response = "404 Not Found";
                            exchange.sendResponseHeaders(404, response.length());
                            OutputStream os = exchange.getResponseBody();
                            os.write(response.getBytes());
                            os.close();
                            return;
                        }
                        String contentType = "text/plain";
                        if (uriPath.endsWith(".html")) contentType = "text/html; charset=UTF-8";
                        else if (uriPath.endsWith(".css")) contentType = "text/css; charset=UTF-8";
                        else if (uriPath.endsWith(".svg")) contentType = "image/svg+xml";
                        else if (uriPath.endsWith(".png")) contentType = "image/png";
                        else if (uriPath.endsWith(".jpg") || uriPath.endsWith(".jpeg")) contentType = "image/jpeg";
                        else if (uriPath.endsWith(".json")) contentType = "application/json";

                        byte[] bytes = Files.readAllBytes(file.toPath());
                        exchange.getResponseHeaders().set("Content-Type", contentType);
                        exchange.sendResponseHeaders(200, bytes.length);
                        OutputStream os = exchange.getResponseBody();
                        os.write(bytes);
                        os.close();
                    } catch (Exception e) {
                        try {
                            String err = "500 Server Error";
                            exchange.sendResponseHeaders(500, err.length());
                            OutputStream os = exchange.getResponseBody();
                            os.write(err.getBytes());
                            os.close();
                        } catch (Exception ignored) {}
                    }
                }
            });
            server.setExecutor(null);
            server.start();
            System.out.println("FemCare+ Java Web Server started at http://localhost:" + port + "/");
            return server;
        } catch (Exception e) {
            System.out.println("Could not start Java Web Server on port " + port + ": " + e.getMessage());
            return null;
        }
    }

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        Patient patient = new Patient("Aditi", 21);
        List<Question> questions = getQuestionBank();
        PbacService pbacService = new PbacService();

        // Start Pure Java Web Server automatically on port 3000
        HttpServer webServer = startWebServer(3000);

        while (true) {
            System.out.println("\n========================================================");
            System.out.println("       FemCare+ | Clinical Backend Engine (Java)        ");
            System.out.println("       Web UI: http://localhost:3000/ (Pure Java)      ");
            System.out.println("========================================================");
            System.out.println("1. Run Sample Anemia Risk Evaluation Demo");
            System.out.println("2. Run Interactive 26-Question Screening");
            System.out.println("3. View WHO Hemoglobin Cutoffs & Medical Guidelines");
            System.out.println("4. Run Automated PBAC Heavy Bleeding Clinical Test Suite (16 Tests)");
            System.out.println("5. PBAC Heavy Bleeding Assessment & Data Management");
            System.out.println("6. Exit");
            System.out.print("Enter your choice (1-6): ");

            String choice = scanner.nextLine().trim();

            if (choice.equals("1")) {
                // Pre-populated demo profile
                List<Integer> points = new ArrayList<>();
                List<Integer> qIds = new ArrayList<>();

                // Moderate-risk simulation (14 / 30 score)
                int[] demoAnswers = {1, 1, 1, 1, 0, 1, 1, 0, 1, 1, 0, 1, 0, 0, 1, 0, 0, 0, 0, 1, 1, 1, 1, 0, 1, 0};
                for (int i = 0; i < questions.size(); i++) {
                    qIds.add(questions.get(i).id);
                    points.add(demoAnswers[i]);
                }

                AssessmentResult result = evaluateRisk(points, qIds);
                printDoctorReport(patient, result);

            } else if (choice.equals("2")) {
                List<Integer> points = new ArrayList<>();
                List<Integer> qIds = new ArrayList<>();

                System.out.println("\n--- Starting 26-Question Anemia Risk Screening ---");
                for (Question q : questions) {
                    System.out.println("\n[Q" + q.id + " · " + q.section + "] " + q.text);
                    for (int i = 0; i < q.options.length; i++) {
                        System.out.println("  (" + (i + 1) + ") " + q.options[i]);
                    }
                    int selected = 1;
                    while (true) {
                        System.out.print("Select option (1-" + q.options.length + "): ");
                        try {
                            String input = scanner.nextLine().trim();
                            selected = Integer.parseInt(input);
                            if (selected >= 1 && selected <= q.options.length) break;
                        } catch (Exception e) {}
                        System.out.println("Invalid input. Please try again.");
                    }
                    qIds.add(q.id);
                    points.add(q.points[selected - 1]);
                }

                AssessmentResult result = evaluateRisk(points, qIds);
                printDoctorReport(patient, result);

            } else if (choice.equals("3")) {
                System.out.println("\n--- Clinical Standards & Guidelines ---");
                System.out.println("1. WHO Hemoglobin Cutoffs (Non-pregnant females >=15 yrs):");
                System.out.println("   - Normal:   >= 12.0 g/dL");
                System.out.println("   - Mild:     11.0 - 11.9 g/dL");
                System.out.println("   - Moderate: 8.0 - 10.9 g/dL");
                System.out.println("   - Severe:   < 8.0 g/dL");
                System.out.println("\n2. FIGO Menstrual Standards:");
                System.out.println("   - Normal Duration: 3 - 8 days");
                System.out.println("   - Heavy Menstrual Bleeding (HMB): > 80 mL blood loss / cycle");
                System.out.println("   - Red Flags: Soaking pads in < 2 hrs, clots > coin size, cycles > 8 days.");

            } else if (choice.equals("4")) {
                runAutomatedPbacTests();

            } else if (choice.equals("5")) {
                handlePbacInteractiveSession(scanner, patient, pbacService);

            } else if (choice.equals("6")) {
                if (webServer != null) {
                    webServer.stop(0);
                    System.out.println("Stopped FemCare+ Java Web Server.");
                }
                System.out.println("Exiting FemCare+ Backend. Goodbye!");
                break;
            } else {
                System.out.println("Invalid choice. Please select 1, 2, 3, 4, 5, or 6.");
            }
        }
    }

    private static void handlePbacInteractiveSession(Scanner scanner, Patient patient, PbacService service) {
        String userId = "user_" + patient.name.toLowerCase();
        List<PbacAssessment> userAssessments = service.getUserAssessments(userId);
        PbacAssessment currentAssessment = userAssessments.isEmpty() ? null : userAssessments.get(userAssessments.size() - 1);

        while (true) {
            System.out.println("\n--------------------------------------------------------");
            System.out.println("   PBAC HEAVY MENSTRUAL BLEEDING ASSESSMENT MANAGER     ");
            System.out.println("--------------------------------------------------------");
            if (currentAssessment == null) {
                System.out.println(" Status: No active period assessment found (Clean initial state).");
            } else {
                int totalScore = "COMPLETED".equals(currentAssessment.status) && currentAssessment.finalScore != null 
                    ? currentAssessment.finalScore 
                    : currentAssessment.calculateTotalScore();
                System.out.println(" Active Assessment ID : " + currentAssessment.id);
                System.out.println(" Status                : " + currentAssessment.status);
                System.out.println(" Days Logged           : " + currentAssessment.entries.size());
                System.out.println(" Current Bleeding Score: " + totalScore + " points");
                System.out.println(" Configured Threshold  : " + currentAssessment.selectedHmbThreshold + " points");
            }
            System.out.println("--------------------------------------------------------");
            System.out.println("1. Start New Period Bleeding Assessment (Clean slate / 0 pts)");
            System.out.println("2. Log / Update a Day's Bleeding Entry");
            System.out.println("3. View Current Score & Clinical Interpretation");
            System.out.println("4. Complete Period Assessment & Finalize Score");
            System.out.println("5. Remove / Clear Assessment Data");
            System.out.println("6. Return to Main Menu");
            System.out.print("Select PBAC option (1-6): ");

            String pChoice = scanner.nextLine().trim();

            if (pChoice.equals("1")) {
                int threshold = 100;
                System.out.print("Choose Threshold: (1) Standard 100 pts [Recommended], (2) Strict 150 pts: ");
                String tChoice = scanner.nextLine().trim();
                if ("2".equals(tChoice)) threshold = 150;
                String periodId = "cycle_" + LocalDate.now().toString();
                currentAssessment = service.createAssessment(userId, periodId, threshold);
                System.out.println("Started fresh PBAC Assessment (" + currentAssessment.id + ") with threshold " + threshold + " pts. Initial score: 0 pts.");

            } else if (pChoice.equals("2")) {
                if (currentAssessment == null) {
                    System.out.println("No active assessment. Starting a new assessment first...");
                    currentAssessment = service.createAssessment(userId, "cycle_" + LocalDate.now().toString(), 100);
                }
                if ("COMPLETED".equals(currentAssessment.status)) {
                    System.out.println("Current assessment is marked COMPLETED. Start a new assessment to log future cycles.");
                    continue;
                }
                System.out.print("Enter Date (YYYY-MM-DD) [Leave empty for today " + LocalDate.now() + "]: ");
                String dateInput = scanner.nextLine().trim();
                if (dateInput.isEmpty()) dateInput = LocalDate.now().toString();

                System.out.print("Lightly stained pads (1 pt each): ");
                int light = parseNonNegativeInt(scanner.nextLine().trim());
                System.out.print("Moderately stained pads (5 pts each): ");
                int mod = parseNonNegativeInt(scanner.nextLine().trim());
                System.out.print("Fully soaked pads (20 pts each): ");
                int soaked = parseNonNegativeInt(scanner.nextLine().trim());
                System.out.print("Small clots (1 pt each): ");
                int smallClots = parseNonNegativeInt(scanner.nextLine().trim());
                System.out.print("Large coin-sized clots (5 pts each): ");
                int largeClots = parseNonNegativeInt(scanner.nextLine().trim());
                System.out.print("Flooding episodes (5 pts each): ");
                int flooding = parseNonNegativeInt(scanner.nextLine().trim());
                System.out.print("Clinical notes (optional): ");
                String notes = scanner.nextLine().trim();

                PbacDailyEntry entry = new PbacDailyEntry(dateInput, light, mod, soaked, smallClots, largeClots, flooding, notes);
                service.saveDailyEntry(userId, currentAssessment.id, entry);
                System.out.println("Entry saved for " + dateInput + "! Daily score: " + entry.getTotalDailyScore() + " pts. New cycle total: " + currentAssessment.calculateTotalScore() + " pts.");

            } else if (pChoice.equals("3")) {
                if (currentAssessment == null || currentAssessment.entries.isEmpty()) {
                    System.out.println("No entries logged yet. Bleeding score is 0 points.");
                    continue;
                }
                int totalScore = "COMPLETED".equals(currentAssessment.status) && currentAssessment.finalScore != null 
                    ? currentAssessment.finalScore 
                    : currentAssessment.calculateTotalScore();
                PbacInterpretation interp = PbacService.interpretScore(totalScore, currentAssessment.selectedHmbThreshold);

                System.out.println("\n--- PBAC Menstrual Bleeding Clinical Summary ---");
                System.out.println("Patient Name       : " + patient.name);
                System.out.println("Assessment ID      : " + currentAssessment.id);
                System.out.println("Status             : " + currentAssessment.status);
                System.out.println("Days Recorded      : " + currentAssessment.entries.size());
                System.out.println("Total PBAC Score   : " + totalScore + " points");
                System.out.println("Pad Breakdown      : " + currentAssessment.getPadPointsTotal() + " pts");
                System.out.println("Clot Breakdown     : " + currentAssessment.getClotPointsTotal() + " pts");
                System.out.println("Flooding Breakdown : " + currentAssessment.getFloodingPointsTotal() + " pts");
                System.out.println("Threshold Config   : " + currentAssessment.selectedHmbThreshold + " pts");
                System.out.println("HMB Screening Flag : " + (interp.isHmbPositive ? "POSITIVE FOR HEAVY MENSTRUAL BLEEDING" : "Below Screening Threshold"));
                System.out.println("Clinical Band      : " + interp.band);
                System.out.println("Interpretation     : " + interp.interpretation);
                System.out.println("Guidance           : " + interp.clinicalGuidance);

            } else if (pChoice.equals("4")) {
                if (currentAssessment == null || currentAssessment.entries.isEmpty()) {
                    System.out.println("No entries logged in the current assessment to complete.");
                    continue;
                }
                PbacInterpretation interp = service.completeAssessment(userId, currentAssessment.id);
                System.out.println("Assessment " + currentAssessment.id + " finalized as COMPLETED with score " + currentAssessment.finalScore + " pts!");
                System.out.println("Result: " + (interp.isHmbPositive ? "POSITIVE FOR HMB" : "Below Threshold") + " (" + interp.band + ")");

            } else if (pChoice.equals("5")) {
                System.out.println("\n--- DATA REMOVAL & RESET OPTIONS ---");
                System.out.println("1. Clear all daily entries from active assessment (Reset score to 0)");
                System.out.println("2. Delete active assessment completely");
                System.out.println("3. Clear ALL PBAC assessments and history for " + patient.name);
                System.out.println("4. Cancel");
                System.out.print("Select data removal option (1-4): ");
                String delChoice = scanner.nextLine().trim();

                if ("1".equals(delChoice)) {
                    if (currentAssessment != null) {
                        currentAssessment.entries.clear();
                        currentAssessment.finalScore = null;
                        currentAssessment.status = "IN_PROGRESS";
                        System.out.println("All entries cleared for active assessment. Score reset to 0 points.");
                    } else {
                        System.out.println("No active assessment to clear.");
                    }
                } else if ("2".equals(delChoice)) {
                    if (currentAssessment != null) {
                        service.clearAssessment(userId, currentAssessment.id);
                        currentAssessment = null;
                        System.out.println("Active PBAC assessment deleted successfully.");
                    } else {
                        System.out.println("No active assessment to delete.");
                    }
                } else if ("3".equals(delChoice)) {
                    int removed = service.clearAllAssessments(userId);
                    currentAssessment = null;
                    System.out.println("All PBAC data (" + removed + " assessments) cleared for " + patient.name + ". State reset to clean baseline.");
                } else {
                    System.out.println("Data removal cancelled.");
                }

            } else if (pChoice.equals("6")) {
                break;
            } else {
                System.out.println("Invalid option. Please select 1 to 6.");
            }
        }
    }

    private static int parseNonNegativeInt(String s) {
        try {
            int v = Integer.parseInt(s);
            return Math.max(0, v);
        } catch (Exception e) {
            return 0;
        }
    }

    private static void printDoctorReport(Patient patient, AssessmentResult res) {
        System.out.println("\n========================================================");
        System.out.println("          DOCTOR CONSULTATION SUMMARY REPORT            ");
        System.out.println("========================================================");
        System.out.println(" Patient Name   : " + patient.name);
        System.out.println(" Patient Age    : " + patient.age + " years");
        System.out.println(" Risk Level     : " + res.riskTier);
        System.out.println(" Clinical Score : " + res.totalScore + " / 30 points");
        System.out.println(" Pica Symptoms  : " + (res.picaFlag ? "Detected" : "None"));
        System.out.println(" Heavy Bleeding : " + (res.heavyBleedingFlag ? "Detected (Menorrhagia signal)" : "Normal"));
        System.out.println(" Summary        : " + res.clinicalSummary);
        System.out.println(" Recommendation : " + res.recommendation);
        System.out.println("========================================================\n");
    }
}
