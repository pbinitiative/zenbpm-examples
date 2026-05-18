import { TaskCard } from './TaskCard.jsx';

export function TaskList({ tasks, loading, error, onOpen }) {
  if (loading) return <p style={{ color: '#718096' }}>Loading tasks…</p>;
  if (error)   return <p style={{ color: '#e53e3e' }}>Error: {error}</p>;
  if (!tasks.length) return <p style={{ color: '#718096' }}>No active tasks.</p>;

  return (
    <div style={{ display: 'flex', flexDirection: 'column', gap: 12 }}>
      {tasks.map(task => (
        <TaskCard key={task.key} task={task} onOpen={onOpen} />
      ))}
    </div>
  );
}
