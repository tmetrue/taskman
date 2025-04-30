import { BrowserRouter as Router, Routes, Route, Link, Navigate } from 'react-router-dom';
import { AppBar, Toolbar, Typography, Container, CssBaseline, Button } from '@mui/material';
import { useState, useEffect } from 'react';
import Tasks from './pages/Tasks';
import Login from './pages/Login';
import CreateTask from './pages/CreateTask';
import EditTask from './pages/EditTask';
import { authService } from './services/authService';

function App() {
  const [isAuthenticated, setIsAuthenticated] = useState(false);

  useEffect(() => {
    const token = authService.getToken();
    if (token) {
      setIsAuthenticated(true);
    }
  }, []);

  const handleLogout = () => {
    authService.logout();
    setIsAuthenticated(false);
  };

  return (
    <Router>
      <CssBaseline />
      <AppBar position="static">
        <Toolbar>
          <Typography variant="h6" component={Link} to="/" sx={{ textDecoration: 'none', color: 'inherit', flexGrow: 1 }}>
            TaskMan
          </Typography>
          {isAuthenticated && (
            <Button color="inherit" onClick={handleLogout}>
              Logout
            </Button>
          )}
        </Toolbar>
      </AppBar>
      <Container>
        <Routes>
          <Route 
            path="/" 
            element={
              isAuthenticated ? <Tasks /> : <Navigate to="/login" />
            } 
          />
          <Route 
            path="/login" 
            element={
              isAuthenticated ? <Navigate to="/" /> : <Login />
            } 
          />
          <Route 
            path="/tasks/new" 
            element={
              isAuthenticated ? <CreateTask /> : <Navigate to="/login" />
            } 
          />
          <Route 
            path="/tasks/:id/edit" 
            element={
              isAuthenticated ? <EditTask /> : <Navigate to="/login" />
            } 
          />
        </Routes>
      </Container>
    </Router>
  );
}

export default App;
