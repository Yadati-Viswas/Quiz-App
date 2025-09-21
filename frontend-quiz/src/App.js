import React from "react";
import './App.css';
import {BrowserRouter as Router, Route, Routes} from 'react-router-dom';
import HomePage from "./HomePage";
import StartQuizPage from "./components/pages/startQuiz";
import CreateQuizPage from "./components/pages/createQuiz";
import { DarkModeContextProvider } from "./contexts/DarkModeContextProvider";
// import CreateQuizPage from "./CreateQuizPage";

export default function App() {
  return (
    <div>
      <DarkModeContextProvider>
      <Router>
        <Routes>
          <Route path="/"  element={<HomePage/>} />
          <Route path="/start-quiz" element={<StartQuizPage/>} />
          <Route path="/create-quiz" element={<CreateQuizPage/>} />
          {/* <Route path="/leaderboard" component={LeaderboardPage} />
          <Route path="/login" component={LoginPage} />
          <Route path="/signup" component={SignupPage} />
          <Route path="*" component={NotFoundPage} /> */}
        </Routes>
      </Router>
      </DarkModeContextProvider>
    </div>
  );
}