import { useCallback, useEffect, useRef, useState } from 'react';
import { useNavigate, useParams } from 'react-router-dom';
import { v4 as uuidv4 } from 'uuid';

function Stream() {
  const { roomId } = useParams();
  const [streaming, setStreaming] = useState(false);
  const videoRef = useRef();
  const streamRef = useRef();
  const wsRef = useRef();
  const peerConnectionsRef = useRef({});
  const navigate = useNavigate();

  const startStream = useCallback(async () => {
    try {
      if (!roomId) {
        const newRoomId = uuidv4();
        navigate(`/stream/${newRoomId}`);
        return;
      }

      const stream = await navigator.mediaDevices.getUserMedia({
        video: true,
        audio: true
      });
      
      streamRef.current = stream;
      videoRef.current.srcObject = stream;
      setStreaming(true);

      wsRef.current.send(JSON.stringify({
        type: 'start-stream',
        roomId: roomId
      }));
    } catch (error) {
      console.error('Error starting stream:', error);
    }
  }, [roomId, navigate]);

  useEffect(() => {
    if (roomId) {
      startStream();
    }
  }, [roomId, startStream]);

  useEffect(() => {
    // Connect to WebSocket
    wsRef.current = new WebSocket('wss://5b96-45-249-79-237.ngrok-free.app/stream');
    
    wsRef.current.onopen = () => {
        console.log('WebSocket connected (Streamer)');
    };

    wsRef.current.onerror = (error) => {
        console.error('WebSocket error (Streamer):', error);
    };

    wsRef.current.onclose = (event) => {
        console.log('WebSocket closed (Streamer):', event.code, event.reason);
    };

    wsRef.current.onmessage = async (event) => {
        const data = JSON.parse(event.data);
        console.log('Streamer received:', data);
        
        switch (data.type) {
            case 'viewer-joined': {
                const peerConnection = createPeerConnection(data.viewerId);
                const offer = await peerConnection.createOffer();
                await peerConnection.setLocalDescription(offer);
                
                wsRef.current.send(JSON.stringify({
                    type: 'offer',
                    offer: offer,
                    viewerId: data.viewerId
                }));
                break;
            }
            case 'answer': {
                const pc = peerConnectionsRef.current[data.viewerId];
                if (pc) {
                    await pc.setRemoteDescription(new RTCSessionDescription(data.answer));
                }
                break;
            }
            case 'ice-candidate': {
                const connection = peerConnectionsRef.current[data.fromId];
                if (connection) {
                    await connection.addIceCandidate(new RTCIceCandidate(data.candidate));
                }
                break;
            }
        }
    };

    return () => {
      if (wsRef.current) {
        wsRef.current.close();
      }
    };
  }, []);

  const createPeerConnection = (viewerId) => {
    console.log('Creating peer connection for viewer:', viewerId);
    const peerConnection = new RTCPeerConnection({
      iceServers: [{ urls: 'stun:stun.l.google.com:19302' }]
    });

    peerConnection.onicecandidate = (event) => {
      console.log('Streamer ICE candidate:', event.candidate);
      if (event.candidate) {
        wsRef.current.send(JSON.stringify({
          type: 'ice-candidate',
          candidate: event.candidate,
          peerId: viewerId
        }));
      }
    };

    peerConnection.onconnectionstatechange = () => {
        console.log('Streamer connection state:', peerConnection.connectionState);
    };

    if (streamRef.current) {
      streamRef.current.getTracks().forEach(track => {
        console.log('Adding track to peer connection:', track.kind);
        peerConnection.addTrack(track, streamRef.current);
      });
    }

    peerConnectionsRef.current[viewerId] = peerConnection;
    return peerConnection;
  };

  return (
    <div>
      {!roomId ? (
        <button onClick={startStream}>Start Stream</button>
      ) : (
        <>
          <video 
            ref={videoRef} 
            autoPlay 
            muted 
            playsInline
            style={{ display: streaming ? 'block' : 'none' }}
          />
          <p>Share this link with viewers: {window.location.origin}/watch/{roomId}</p>
        </>
      )}
    </div>
  );
}

export default Stream;
