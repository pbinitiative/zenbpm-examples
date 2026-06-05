import { useEffect, useRef, useState } from 'react';
import { Form } from '@bpmn-io/form-js';
import { FORM_VARIABLE } from '../config.js';
import { completeTask } from '../api/tasks.js';

export function TaskDetail({ task, onClose, onCompleted, addToast }) {
  const containerRef = useRef(null);
  const formRef = useRef(null);
  const [submitting, setSubmitting] = useState(false);
  const [formError, setFormError] = useState(null);

  useEffect(() => {
    setFormError(null);
    if (!task || !containerRef.current) return;

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

  /* Empty state */
  if (!task) {
    return (
      <div style={{
        height: '100%', display: 'flex', flexDirection: 'column',
        alignItems: 'center', justifyContent: 'center',
        color: '#cbd5e0', gap: 14,
      }}>
        <svg width="52" height="52" viewBox="0 0 24 24" fill="none"
          stroke="currentColor" strokeWidth="1.2" strokeLinecap="round" strokeLinejoin="round">
          <rect x="3" y="3" width="18" height="18" rx="2.5" />
          <line x1="8" y1="9" x2="16" y2="9" />
          <line x1="8" y1="13" x2="13" y2="13" />
        </svg>
        <p style={{ margin: 0, fontSize: 15, color: '#a0aec0' }}>
          Select a task to view its details
        </p>
      </div>
    );
  }

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
    <div style={{ padding: '32px 36px', maxWidth: 760 }}>

      {/* Task header */}
      <div style={{ marginBottom: 28 }}>
        <div style={{
          fontSize: 11, fontWeight: 600, color: '#a0aec0',
          textTransform: 'uppercase', letterSpacing: '0.07em', marginBottom: 6,
        }}>
          Task Detail
        </div>
        <h2 style={{ margin: '0 0 8px', fontSize: 22, fontWeight: 700, color: '#1a202c' }}>
          {task.name ?? task.elementId ?? task.key}
        </h2>
        <div style={{ display: 'flex', gap: 20, fontSize: 13, color: '#718096' }}>
          <span>Instance: <strong style={{ color: '#4a5568' }}>{task.processInstanceKey}</strong></span>
          {task.assignee && <span>Assignee: <strong style={{ color: '#4a5568' }}>{task.assignee}</strong></span>}
        </div>
      </div>

      {/* Form area */}
      <div style={{
        background: '#fff', borderRadius: 8,
        border: '1px solid #e2e8f0',
        padding: 24, marginBottom: 24,
        boxShadow: '0 1px 3px rgba(0,0,0,0.04)',
      }}>
        {formError
          ? <p style={{ color: '#e53e3e', margin: 0 }}>{formError}</p>
          : <div ref={containerRef} />
        }
      </div>

      {/* Actions */}
      <div style={{ display: 'flex', gap: 10, justifyContent: 'flex-end' }}>
        <button onClick={onClose} style={secondaryBtn}>Cancel</button>
        <button
          onClick={handleSubmit}
          disabled={submitting || !!formError}
          style={{ ...primaryBtn, opacity: (submitting || !!formError) ? 0.6 : 1 }}
        >
          {submitting ? 'Submitting…' : 'Complete Task'}
        </button>
      </div>
    </div>
  );
}

const primaryBtn = {
  padding: '9px 22px', background: '#3182ce', color: '#fff',
  border: 'none', borderRadius: 6, cursor: 'pointer', fontWeight: 500, fontSize: 14,
};
const secondaryBtn = {
  padding: '9px 22px', background: '#edf2f7', color: '#2d3748',
  border: 'none', borderRadius: 6, cursor: 'pointer', fontSize: 14,
};
