export function TaskCard({ task, onOpen }) {
  return (
    <div style={{
      border: '1px solid #e2e8f0',
      borderRadius: 8,
      padding: 16,
      background: '#fff',
      display: 'flex',
      justifyContent: 'space-between',
      alignItems: 'center',
    }}>
      <div>
        <div style={{ fontWeight: 600, marginBottom: 4 }}>{task.elementId ?? task.key}</div>
        <div style={{ fontSize: 13, color: '#718096' }}>
          Instance: {task.processInstanceKey}
          {task.assignee && ` · Assignee: ${task.assignee}`}
        </div>
      </div>
      <button onClick={() => onOpen(task)} style={{
        padding: '8px 16px',
        background: '#3182ce',
        color: '#fff',
        border: 'none',
        borderRadius: 6,
        cursor: 'pointer',
        fontWeight: 500,
      }}>
        Open
      </button>
    </div>
  );
}
