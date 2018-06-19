import * as React from 'react';
import './App.css';
import AccountBookDetailsList from './AccountBookDetailsList';

interface AppState {

}

interface AppProperties {

}

class App extends React.Component<AppProperties, AppState> {

  public render() {
    return (
    <div className="App">
      <header className="App-header">
        <img src="degrasse.jpeg" className="App-logo" alt="logo" />
      </header>
      <AccountBookDetailsList/>
    </div>
    );
   }
}

export default App;
