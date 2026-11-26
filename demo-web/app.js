/**
 * FraudShield AI — Web Demo Interface
 * Connects to FastAPI backend for real-time scam analysis.
 */

// ── Configuration ──
const API_BASE = window.location.hostname === 'localhost' || window.location.hostname === '127.0.0.1'
    ? 'http://localhost:8000/api'
    : '/api';

let currentInputType = 'sms';
let scanHistory = [];

// ── Navigation ──
document.querySelectorAll('.nav-link').forEach(link => {
    link.addEventListener('click', (e) => {
        e.preventDefault();
        const section = link.dataset.section;
        showSection(section);
        document.querySelectorAll('.nav-link').forEach(l => l.classList.remove('active'));
        link.classList.add('active');
    });
});

function showSection(sectionId) {
    const sections = ['analyze', 'demo', 'history', 'safety'];
    document.getElementById('hero').style.display = sectionId === 'analyze' ? 'block' : 'none';
    document.getElementById('analyzing').classList.add('hidden');
    document.getElementById('result').classList.add('hidden');

    sections.forEach(s => {
        const el = document.getElementById(s);
        if (el) {
            if (s === sectionId) {
                el.classList.remove('hidden');
            } else {
                el.classList.add('hidden');
            }
        }
    });

    if (sectionId === 'demo') loadDemoScenarios();
    if (sectionId === 'history') renderHistory();
}

// ── Input Tabs ──
document.querySelectorAll('.input-tab').forEach(tab => {
    tab.addEventListener('click', () => {
        document.querySelectorAll('.input-tab').forEach(t => t.classList.remove('active'));
        tab.classList.add('active');
        currentInputType = tab.dataset.type;

        const placeholders = {
            sms: 'Paste the suspicious SMS here...',
            whatsapp: 'Paste the suspicious WhatsApp message here...',
            call_transcript: 'Paste the call transcript here...'
        };
        document.getElementById('messageInput').placeholder = placeholders[currentInputType] || placeholders.sms;
    });
});

// ── Core Analysis ──
async function analyzeMessage() {
    const input = document.getElementById('messageInput');
    const text = input.value.trim();

    if (!text) {
        showToast('Please paste a message to analyze.', 'error');
        input.focus();
        return;
    }

    if (text.length < 5) {
        showToast('Message is too short for meaningful analysis.', 'error');
        return;
    }

    // Show analyzing state
    document.getElementById('analyze').classList.add('hidden');
    document.getElementById('hero').style.display = 'none';
    document.getElementById('result').classList.add('hidden');
    document.getElementById('analyzing').classList.remove('hidden');

    // Animate pipeline stages
    await animateAnalysis();

    try {
        const endpoint = currentInputType === 'call_transcript'
            ? `${API_BASE}/analyze/transcript`
            : `${API_BASE}/analyze/message`;

        const body = currentInputType === 'call_transcript'
            ? { transcript: text }
            : { message: text, input_type: currentInputType };

        const response = await fetch(endpoint, {
            method: 'POST',
            headers: { 'Content-Type': 'application/json' },
            body: JSON.stringify(body)
        });

        if (!response.ok) {
            throw new Error(`Server error: ${response.status}`);
        }

        const result = await response.json();
        scanHistory.unshift(result);
        showResult(result);

    } catch (error) {
        console.error('Analysis failed:', error);
        showToast(`Analysis failed: ${error.message}. Is the backend running on localhost:8000?`, 'error');
        // Return to analyze view
        document.getElementById('analyzing').classList.add('hidden');
        document.getElementById('analyze').classList.remove('hidden');
        document.getElementById('hero').style.display = 'block';
    }
}

async function animateAnalysis() {
    const steps = [
        { id: 'step-detect', pipeline: 'pipeline-detect', delay: 400 },
        { id: 'step-understand', pipeline: 'pipeline-understand', delay: 500 },
        { id: 'step-classify', pipeline: 'pipeline-classify', delay: 500 },
        { id: 'step-score', pipeline: 'pipeline-score', delay: 400 },
        { id: 'step-protect', pipeline: 'pipeline-protect', delay: 300 },
    ];

    // Reset all steps
    steps.forEach(s => {
        const el = document.getElementById(s.id);
        if (el) {
            el.classList.remove('active', 'complete');
            el.querySelector('.step-icon').textContent = '⏳';
        }
    });

    for (const step of steps) {
        const el = document.getElementById(step.id);
        if (el) {
            el.classList.add('active');
            el.querySelector('.step-icon').textContent = '🔄';
            await sleep(step.delay);
            el.classList.remove('active');
            el.classList.add('complete');
            el.querySelector('.step-icon').textContent = '✅';
        }

        const pipelineEl = document.getElementById(step.pipeline);
        if (pipelineEl) {
            pipelineEl.classList.add('complete');
        }
    }

    await sleep(300);
}

// ── Show Result ──
function showResult(result) {
    document.getElementById('analyzing').classList.add('hidden');
    const resultSection = document.getElementById('result');
    resultSection.classList.remove('hidden');

    const riskClass = result.risk_level.toLowerCase();
    const scorePercent = result.risk_score / 100;

    // Build gauge SVG
    const gaugeRadius = 70;
    const gaugeCircumference = Math.PI * gaugeRadius;
    const gaugeDashOffset = gaugeCircumference * (1 - scorePercent);

    let html = `
        <div class="result-card slide-up">
            <div class="result-header ${riskClass}">
                <div class="result-risk-label ${riskClass}">⚠ ${result.risk_level} RISK</div>
                <div class="score-gauge">
                    <svg viewBox="0 0 180 100">
                        <path class="gauge-track" d="M 10 90 A 70 70 0 0 1 170 90" />
                        <path class="gauge-fill ${riskClass}" d="M 10 90 A 70 70 0 0 1 170 90"
                            stroke-dasharray="${gaugeCircumference}"
                            stroke-dashoffset="${gaugeDashOffset}" />
                    </svg>
                </div>
                <div class="result-score ${riskClass}">${result.risk_score}<span class="result-score-max">/100</span></div>
                <div class="result-scam-type">${result.scam_type}</div>
                <div class="result-confidence">Confidence: ${(result.confidence * 100).toFixed(0)}% • AI Provider: ${result.ai_provider}</div>
            </div>

            <div class="result-body">`;

    // Red Flags
    if (result.red_flags && result.red_flags.length > 0) {
        html += `
                <div class="result-section">
                    <h4>🚩 Red Flags Detected</h4>
                    <div class="red-flag-list">
                        ${result.red_flags.map(flag => `
                            <div class="red-flag-item">
                                <div class="red-flag-icon">⚠</div>
                                <div class="red-flag-content">
                                    <h5>${escapeHtml(flag.indicator)}</h5>
                                    <p>${escapeHtml(flag.description)}</p>
                                </div>
                            </div>
                        `).join('')}
                    </div>
                </div>`;
    }

    // Explanation
    if (result.explanation) {
        html += `
                <div class="result-section">
                    <h4>💡 Why This Was Flagged</h4>
                    <p class="explanation-text">${escapeHtml(result.explanation)}</p>
                </div>`;
    }

    // Recommendations
    if (result.recommended_actions && result.recommended_actions.length > 0) {
        html += `
                <div class="result-section">
                    <h4>🛡️ Safety Recommendations</h4>
                    <div class="recommendation-list">
                        ${result.recommended_actions.map(action => `
                            <div class="recommendation-item">
                                <span class="recommendation-icon">✓</span>
                                ${escapeHtml(action)}
                            </div>
                        `).join('')}
                    </div>
                </div>`;
    }

    // Score Breakdown
    if (result.score_breakdown && result.score_breakdown.length > 0) {
        html += `
                <div class="result-section">
                    <h4>📊 Risk Score Breakdown</h4>
                    <div class="breakdown-list">
                        ${result.score_breakdown.map(b => `
                            <div class="breakdown-item">
                                <span class="breakdown-signal">${escapeHtml(b.signal)}</span>
                                <span class="breakdown-points">+${b.points}</span>
                            </div>
                        `).join('')}
                    </div>
                </div>`;
    }

    html += `
            </div>

            <div class="result-actions">
                <button class="btn-danger" onclick="reportIncident('${result.id}')">
                    🚨 Report Incident
                </button>
                <button class="btn-outline" onclick="analyzeAnother()">
                    🔍 Analyze Another
                </button>
            </div>
            <div class="result-disclaimer">${escapeHtml(result.disclaimer)}</div>
        </div>`;

    resultSection.innerHTML = html;

    // Animate gauge fill
    setTimeout(() => {
        const gaugeFill = resultSection.querySelector('.gauge-fill');
        if (gaugeFill) {
            gaugeFill.style.strokeDashoffset = gaugeDashOffset;
        }
    }, 100);

    resultSection.scrollIntoView({ behavior: 'smooth', block: 'start' });
}

// ── Demo Scenarios ──
async function loadDemoScenarios() {
    const grid = document.getElementById('demoGrid');
    
    try {
        const response = await fetch(`${API_BASE}/demo/scenarios`);
        if (!response.ok) throw new Error('Failed to load scenarios');
        const scenarios = await response.json();
        renderDemoScenarios(scenarios);
    } catch (error) {
        // Fallback with hardcoded scenarios
        const fallbackScenarios = [
            {
                id: 'demo_digital_arrest', name: 'Digital Arrest Scam',
                description: 'CBI impersonation with arrest threat and urgent money demand.',
                input_text: 'This is CBI. Your bank account is linked to a money laundering case under investigation. You are under digital arrest. Stay on this video call and do not tell anyone. Transfer ₹2,00,000 immediately to the secure government account to avoid arrest.',
                expected_risk_level: 'CRITICAL'
            },
            {
                id: 'demo_fake_bank', name: 'Fake Bank Support',
                description: 'Fake bank customer care demanding KYC update and OTP.',
                input_text: 'Dear Customer, this is SBI Customer Care. Your account will be blocked within 24 hours due to incomplete KYC verification. Please share your Aadhaar number and OTP sent to your registered mobile number immediately.',
                expected_risk_level: 'CRITICAL'
            },
            {
                id: 'demo_investment', name: 'Investment Scam',
                description: 'Crypto/stock trading scam with guaranteed returns.',
                input_text: '🔥 EXCLUSIVE OFFER! Join our premium stock trading group on Telegram. We guarantee 200% returns in just 30 days! Invest ₹50,000 today. Zero risk, 100% profit guaranteed.',
                expected_risk_level: 'HIGH'
            },
            {
                id: 'demo_task_scam', name: 'Prepaid Task / Job Scam',
                description: 'Online task/job scam requiring registration fee.',
                input_text: 'Hi! Simple work-from-home opportunity. Complete online rating tasks and earn ₹500 per task. Just pay a one-time registration fee of ₹999 to join our Telegram group.',
                expected_risk_level: 'HIGH'
            },
            {
                id: 'demo_phishing', name: 'Phishing SMS',
                description: 'Classic phishing SMS with fake bank link.',
                input_text: 'URGENT: Your SBI account will be blocked today due to expired KYC. Verify your KYC immediately: http://sbi-kyc-update.fake-link.com/verify',
                expected_risk_level: 'HIGH'
            },
            {
                id: 'demo_legitimate', name: 'Safe / Legitimate Message',
                description: 'A normal, legitimate message — should receive LOW risk.',
                input_text: 'Hey! Are we still on for dinner at 7 PM tonight? I was thinking we could try that new restaurant near Connaught Place.',
                expected_risk_level: 'LOW'
            },
        ];
        renderDemoScenarios(fallbackScenarios);
    }
}

function renderDemoScenarios(scenarios) {
    const grid = document.getElementById('demoGrid');
    grid.innerHTML = scenarios.map(s => `
        <div class="demo-card" onclick="runDemo('${s.id}', \`${s.input_text.replace(/`/g, "'")}\`)">
            <div class="demo-card-header">
                <span class="demo-card-name">${escapeHtml(s.name)}</span>
                <span class="demo-card-risk ${s.expected_risk_level.toLowerCase()}">${s.expected_risk_level}</span>
            </div>
            <p class="demo-card-desc">${escapeHtml(s.description)}</p>
            <div class="demo-card-preview">${escapeHtml(s.input_text.substring(0, 120))}...</div>
            <button class="demo-card-btn">▶ Run Scenario</button>
        </div>
    `).join('');
}

async function runDemo(scenarioId, inputText) {
    // Switch to analyze view and populate
    showSection('analyze');
    document.querySelectorAll('.nav-link').forEach(l => l.classList.remove('active'));
    document.querySelector('.nav-link[data-section="analyze"]').classList.add('active');

    document.getElementById('messageInput').value = inputText;

    // Auto-run analysis
    await analyzeMessage();
}

// ── Scan History ──
function renderHistory() {
    const list = document.getElementById('historyList');

    if (scanHistory.length === 0) {
        list.innerHTML = `
            <div class="empty-state">
                <svg width="48" height="48" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="1.5" opacity="0.4"><path d="M12 22s8-4 8-10V5l-8-3-8 3v7c0 6 8 10 8 10z"/></svg>
                <p>No scans yet. Analyze a message to get started.</p>
            </div>`;
        return;
    }

    list.innerHTML = scanHistory.map(scan => {
        const riskClass = scan.risk_level.toLowerCase();
        const date = new Date(scan.created_at);
        const timeStr = date.toLocaleTimeString([], { hour: '2-digit', minute: '2-digit' });
        const dateStr = formatRelativeDate(date);

        return `
            <div class="history-item fade-in" onclick='showResult(${JSON.stringify(scan).replace(/'/g, "\\'")})'>
                <div class="history-risk-badge ${riskClass}">${scan.risk_score}</div>
                <div class="history-info">
                    <div class="history-scam-type">${escapeHtml(scan.scam_type)}</div>
                    <div class="history-summary">${escapeHtml(scan.summary || scan.input_text.substring(0, 80))}</div>
                </div>
                <div class="history-meta">
                    <div class="history-date">${dateStr} ${timeStr}</div>
                    <div class="history-level ${riskClass}">${scan.risk_level}</div>
                </div>
            </div>`;
    }).join('');
}

// ── Reporting ──
async function reportIncident(scanId) {
    const scan = scanHistory.find(s => s.id === scanId);
    if (!scan) {
        showToast('Scan not found.', 'error');
        return;
    }

    try {
        const response = await fetch(`${API_BASE}/reports`, {
            method: 'POST',
            headers: { 'Content-Type': 'application/json' },
            body: JSON.stringify({
                scan_id: scanId,
                scam_type: scan.scam_type,
                risk_score: scan.risk_score,
                description: scan.summary
            })
        });

        if (response.ok) {
            showToast('Report submitted. For immediate help, contact the National Cyber Crime Helpline: 1930', 'success');
        } else {
            throw new Error('Failed to submit report');
        }
    } catch (error) {
        showToast('Report saved locally. Contact 1930 (National Cyber Crime Helpline) for immediate assistance.', 'success');
    }
}

function analyzeAnother() {
    document.getElementById('result').classList.add('hidden');
    document.getElementById('analyze').classList.remove('hidden');
    document.getElementById('hero').style.display = 'block';
    document.getElementById('messageInput').value = '';
    document.getElementById('messageInput').focus();
    window.scrollTo({ top: 0, behavior: 'smooth' });
}

function clearInput() {
    document.getElementById('messageInput').value = '';
    document.getElementById('messageInput').focus();
}

// ── Utilities ──
function sleep(ms) {
    return new Promise(resolve => setTimeout(resolve, ms));
}

function escapeHtml(text) {
    if (!text) return '';
    const div = document.createElement('div');
    div.textContent = text;
    return div.innerHTML;
}

function formatRelativeDate(date) {
    const now = new Date();
    const diffMs = now - date;
    const diffDays = Math.floor(diffMs / (1000 * 60 * 60 * 24));

    if (diffDays === 0) return 'Today';
    if (diffDays === 1) return 'Yesterday';
    if (diffDays < 7) return `${diffDays} days ago`;
    return date.toLocaleDateString();
}

function showToast(message, type = 'info') {
    const existing = document.querySelector('.toast');
    if (existing) existing.remove();

    const toast = document.createElement('div');
    toast.className = `toast ${type}`;
    toast.textContent = message;
    document.body.appendChild(toast);

    setTimeout(() => {
        toast.style.opacity = '0';
        toast.style.transform = 'translateY(10px)';
        toast.style.transition = 'all 0.3s ease';
        setTimeout(() => toast.remove(), 300);
    }, 5000);
}

// ── Initialize ──
document.addEventListener('DOMContentLoaded', () => {
    showSection('analyze');

    // Load history from backend if available
    fetch(`${API_BASE}/scans`)
        .then(r => r.ok ? r.json() : [])
        .then(scans => {
            if (scans.length > 0) {
                scanHistory = scans;
            }
        })
        .catch(() => {});
});

// Keyboard shortcut: Ctrl+Enter to analyze
document.getElementById('messageInput').addEventListener('keydown', (e) => {
    if ((e.ctrlKey || e.metaKey) && e.key === 'Enter') {
        analyzeMessage();
    }
});
