import { useEffect, useRef, useState } from 'react';
import { useNavigate } from 'react-router-dom';

function StreamList() {
  const [activeStreams, setActiveStreams] = useState([]);
  const navigate = useNavigate();
  const wsRef = useRef();

  useEffect(() => {
    wsRef.current = new WebSocket('wss://5b96-45-249-79-237.ngrok-free.app/stream');
    
    wsRef.current.onopen = () => {
      console.log('WebSocket connected (StreamList)');
      // Request active streams
      wsRef.current.send(JSON.stringify({
        type: 'get-streams'
      }));
    };

    wsRef.current.onmessage = (event) => {
      const data = JSON.parse(event.data);
      if (data.type === 'streams-list') {
        setActiveStreams(data.streams);
      }
    };

    return () => {
      if (wsRef.current) {
        wsRef.current.close();
      }
    };
  }, []);

  return (
    <div>
      <h2>Active Streams</h2>
      {activeStreams.length === 0 ? (
        <p>No active streams</p>
      ) : (
        <ul>
          {activeStreams.map(stream => (
            <li key={stream.roomId} onClick={() => navigate(`/watch/${stream.roomId}`)}>
              Stream {stream.roomId}
            </li>
          ))}
        </ul>
      )}
    </div>
  );
}

export default StreamList; 