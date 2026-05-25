import { useState } from 'react';
import { startProcess } from '../api/processes.js';

export function StartProcessModal({ onClose, onStarted, addToast }) {
  const [businessKey, setBusinessKey] = useState('');
  const [submitting, setSubmitting] = useState(false);

  async function handleStart() {
    setSubmitting(true);
    try {
      await startProcess({}, businessKey || null);
      addToast('Process started.', 'success');
      // Give ZenBPM time to create the first user task before refreshing
      setTimeout(onStarted, 1000);
      onClose();
    } catch (err) {
      addToast(err.message, 'error');
    } finally {
      setSubmitting(false);
    }
  }

  return (
    <div style={overlay}>
      <div style={modal}>
        <div style={{ display: 'flex', justifyContent: 'space-between', alignItems: 'center', marginBottom: 20 }}>
          <h2 style={{ margin: 0, fontSize: 18 }}>Start Process</h2>
          <button onClick={onClose} style={closeBtn}>✕</button>
        </div>

        <label style={{ display: 'block', marginBottom: 8, fontWeight: 500 }}>
          Business Key <span style={{ color: '#718096', fontWeight: 400 }}>(optional)</span>
        </label>
        <input
          type="text"
          value={businessKey}
          onChange={e => setBusinessKey(e.target.value)}
          placeholder="e.g. contract-2026-001"
          style={{ width: '100%', padding: '9px 12px', border: '1px solid #e2e8f0', borderRadius: 6, fontSize: 14, boxSizing: 'border-box' }}
        />

        <div style={{ marginTop: 24, display: 'flex', gap: 8, justifyContent: 'flex-end' }}>
          <button onClick={onClose} style={secondaryBtn}>Cancel</button>
          <button onClick={handleStart} disabled={submitting} style={primaryBtn}>
            {submitting ? 'Starting…' : 'Start'}
          </button>
        </div>
      </div>
    </div>
  );
}

const overlay = {
  position: 'fixed', inset: 0, background: 'rgba(0,0,0,0.4)',
  display: 'flex', alignItems: 'center', justifyContent: 'center', zIndex: 500,
};
const modal = {
  background: '#fff', borderRadius: 10, padding: 28,
  width: '90%', maxWidth: 480,
  boxShadow: '0 8px 32px rgba(0,0,0,0.2)',
};
const closeBtn = { background: 'none', border: 'none', fontSize: 18, cursor: 'pointer', color: '#718096' };
const primaryBtn = { padding: '9px 20px', background: '#3182ce', color: '#fff', border: 'none', borderRadius: 6, cursor: 'pointer', fontWeight: 500 };
const secondaryBtn = { padding: '9px 20px', background: '#edf2f7', color: '#2d3748', border: 'none', borderRadius: 6, cursor: 'pointer' };
