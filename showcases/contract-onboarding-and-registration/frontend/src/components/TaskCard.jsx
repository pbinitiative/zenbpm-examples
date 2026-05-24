export function TaskCard({ task, selected, onSelect }) {
  return (
    <button
      onClick={() => onSelect(task)}
      style={{
        width: '100%',
        textAlign: 'left',
        padding: '12px 16px',
        background: selected ? '#ebf8ff' : 'transparent',
        borderLeft: `3px solid ${selected ? '#3182ce' : 'transparent'}`,
        borderRight: 'none',
        borderTop: 'none',
        borderBottom: '1px solid #e2e8f0',
        cursor: 'pointer',
        display: 'block',
        transition: 'background 0.1s',
      }}
    >
      <div style={{
        fontWeight: 600, fontSize: 14,
        color: selected ? '#2b6cb0' : '#2d3748',
        marginBottom: 3,
        whiteSpace: 'nowrap', overflow: 'hidden', textOverflow: 'ellipsis',
      }}>
        {task.name ?? task.elementId ?? task.key}
      </div>
      <div style={{ fontSize: 12, color: '#a0aec0' }}>
        {task.assignee ? `Assignee: ${task.assignee}` : 'Unassigned'}
      </div>
    </button>
  );
}
