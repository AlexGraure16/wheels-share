import React, { Component } from 'react';
import {
  BrowserRouter as Router,
  Route
} from 'react-router-dom';

import WelcomePage from './components/WelcomePge/WelcomePage';
import './App.css';

const Welcome = () => (
    <WelcomePage />
);

class App extends Component {
  render() {
    return (
      <Router>
        <div className="App">
          <Route exact path="/" component={Welcome} />
        </div>
      </Router>
    );
  }
}

export default App;
