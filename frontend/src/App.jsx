import { Route, BrowserRouter as Router, Routes } from 'react-router-dom';
import Admin from './components/Admin';
import Home from './components/Home';
import Login from './components/Login';
import Register from './components/Register';
import Stream from './components/Stream';
import StreamList from './components/StreamList';
import Viewer from './components/Viewer';

function App() {
  return (
    <Router>
      <Routes>
        <Route path="/" element={<Home />} />
        <Route path="/login" element={<Login />} />
        <Route path="/register" element={<Register />} />
        <Route path="/stream" element={<Stream />} />
        <Route path="/stream/:roomId" element={<Stream />} />
        <Route path="/watch" element={<StreamList />} />
        <Route path="/watch/:roomId" element={<Viewer />} />
        <Route path="/admin" element={<Admin />} />
      </Routes>
    </Router>
  );
}

export default App;
