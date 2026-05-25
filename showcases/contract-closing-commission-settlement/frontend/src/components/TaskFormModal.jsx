import { useEffect, useRef, useState } from 'react';
import { Form } from '@bpmn-io/form-js';
import { FORM_VARIABLE } from '../config.js';
import { completeTask } from '../api/tasks.js';

export function TaskFormModal({ task, onClose, onCompleted, addToast }) {
  const containerRef = useRef(null);
  const formRef = useRef(null);
  const [submitting, setSubmitting] = useState(false);
  const [formError, setFormError] = useState(null);

  useEffect(() => {
    if (!containerRef.current) return;

    const rawSchema = task.variables?.[FORM_VARIABLE];
    let schema;
    try {
      schema = typeof rawSchema === 'string' ? JSON.parse(rawSchema) : rawSchema;
    } catch {
      setFormError('Could not parse form schema.');
      return;
    }

    if (!schema) {
      setFormError('No form schema found in task variables.');
      return;
    }

    const form = new Form({ container: containerRef.current });
    formRef.current = form;

    form.importSchema(schema, task.variables).catch(() => {
      setFormError('Failed to render form.');
    });

    return () => {
      form.destroy();
      formRef.current = null;
    };
  }, [task]);

  async function handleSubmit() {
    if (!formRef.current) return;
    const { errors, data } = formRef.current.submit();
    if (errors && Object.keys(errors).length > 0) return;

    setSubmitting(true);
    try {
      await completeTask(task.key, data);
      addToast('Task completed.', 'success');
      onCompleted();
    } catch (err) {
      addToast(err.message, 'error');
    } finally {
      setSubmitting(false);
    }
  }

  return (
    <div style={overlay}>
      <div style={modal}>
        <div style={{ display: 'flex', justifyContent: 'space-between', alignItems: 'center', marginBottom: 16 }}>
          <h2 style={{ margin: 0, fontSize: 18 }}>{task.elementId ?? task.key}</h2>
          <button onClick={onClose} style={closeBtn}>✕</button>
        </div>

        {formError
          ? <p style={{ color: '#e53e3e' }}>{formError}</p>
          : <div ref={containerRef} />
        }

        <div style={{ marginTop: 20, display: 'flex', gap: 8, justifyContent: 'flex-end' }}>
          <button onClick={onClose} style={secondaryBtn}>Cancel</button>
          <button onClick={handleSubmit} disabled={submitting || !!formError} style={primaryBtn}>
            {submitting ? 'Submitting…' : 'Complete'}
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
  width: '90%', maxWidth: 640, maxHeight: '90vh', overflowY: 'auto',
  boxShadow: '0 8px 32px rgba(0,0,0,0.2)',
};
const closeBtn = { background: 'none', border: 'none', fontSize: 18, cursor: 'pointer', color: '#718096' };
const primaryBtn = { padding: '9px 20px', background: '#3182ce', color: '#fff', border: 'none', borderRadius: 6, cursor: 'pointer', fontWeight: 500 };
const secondaryBtn = { padding: '9px 20px', background: '#edf2f7', color: '#2d3748', border: 'none', borderRadius: 6, cursor: 'pointer' };
