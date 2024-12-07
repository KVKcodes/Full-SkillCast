import { useEffect, useRef, useState } from 'react';
import { useParams } from 'react-router-dom';

function Viewer() {
  const { roomId } = useParams();
  const videoRef = useRef();
  const wsRef = useRef();
  const peerConnectionRef = useRef();
  const [streamerId, setStreamerId] = useState(null);

  useEffect(() => {
    wsRef.current = new WebSocket('wss://5b96-45-249-79-237.ngrok-free.app/stream');
    
    wsRef.current.onopen = () => {
      console.log('WebSocket connected (Viewer)');
      wsRef.current.send(JSON.stringify({
        type: 'join-stream',
        roomId: roomId
      }));
    };
    
    wsRef.current.onerror = (error) => {
      console.error('WebSocket error (Viewer):', error);
    };
    
    wsRef.current.onclose = (event) => {
      console.log('WebSocket closed (Viewer):', event.code, event.reason);
    };
    
    wsRef.current.onmessage = async (event) => {
      const data = JSON.parse(event.data);
      console.log('Viewer received:', data);
      
      switch (data.type) {
        case 'offer': {
          setStreamerId(data.streamerId);
          peerConnectionRef.current = createPeerConnection();
          console.log('Setting remote description:', data.offer);
          await peerConnectionRef.current.setRemoteDescription(new RTCSessionDescription(data.offer));
          const answer = await peerConnectionRef.current.createAnswer();
          await peerConnectionRef.current.setLocalDescription(answer);
          
          wsRef.current.send(JSON.stringify({
            type: 'answer',
            answer: answer,
            streamerId: data.streamerId
          }));
          break;
        }
        case 'ice-candidate': {
          if (peerConnectionRef.current) {
            await peerConnectionRef.current.addIceCandidate(
              new RTCIceCandidate(data.candidate)
            );
          }
          break;
        }
      }
    };

    return () => {
      if (peerConnectionRef.current) {
        peerConnectionRef.current.close();
      }
      if (wsRef.current) {
        wsRef.current.close();
      }
    };
  }, [roomId]);

  const createPeerConnection = () => {
    console.log('Creating peer connection (Viewer)');
    const peerConnection = new RTCPeerConnection({
      iceServers: [{ urls: 'stun:stun.l.google.com:19302' }]
    });

    peerConnection.ontrack = (event) => {
      console.log('Received track:', event.track.kind);
      if (videoRef.current) {
        videoRef.current.srcObject = event.streams[0];
      }
    };

    peerConnection.onicecandidate = (event) => {
      console.log('Viewer ICE candidate:', event.candidate);
      if (event.candidate) {
        wsRef.current.send(JSON.stringify({
          type: 'ice-candidate',
          candidate: event.candidate,
          peerId: streamerId
        }));
      }
    };

    peerConnection.onconnectionstatechange = () => {
      console.log('Viewer connection state:', peerConnection.connectionState);
    };

    return peerConnection;
  };

  return (
    <div>
      <video ref={videoRef} autoPlay playsInline />
    </div>
  );
}

export default Viewer; 