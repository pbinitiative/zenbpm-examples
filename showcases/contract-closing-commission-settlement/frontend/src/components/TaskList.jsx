import { TaskCard } from './TaskCard.jsx';

export function TaskList({ tasks, loading, error, selectedTask, onSelect }) {
  if (loading) return <p style={{ padding: 16, color: '#718096', fontSize: 14, margin: 0 }}>Loading tasks…</p>;
  if (error)   return <p style={{ padding: 16, color: '#e53e3e', fontSize: 14, margin: 0 }}>Error: {error}</p>;
  if (!tasks.length) return <p style={{ padding: 16, color: '#a0aec0', fontSize: 14, margin: 0 }}>No active tasks.</p>;

  return (
    <div style={{ display: 'flex', flexDirection: 'column' }}>
      {tasks.map(task => (
        <TaskCard
          key={task.key}
          task={task}
          selected={selectedTask?.key === task.key}
          onSelect={onSelect}
        />
      ))}
    </div>
  );
}
