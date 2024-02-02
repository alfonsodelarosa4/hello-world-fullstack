import React, { useState } from 'react';
import './App.css';

function App() {
    const [name, setName] = useState('');
    const [message, setMessage] = useState('');

    const handleSubmit = async () => {
      try {
          const response = await fetch('http://localhost:8081/sayHello', {
              method: 'POST',
              headers: {
                  'Content-Type': 'application/json',
              },
              body: JSON.stringify({ name }),
          });
  
          const result = await response.json();
          setMessage(result.message);
      } catch (error) {
          console.error('Error:', error);
      }
  };

    return (
        <div className="App">
            <h1>Version 1</h1>
            <label>
                Enter your name: 
                <input type="text" value={name} onChange={(e) => setName(e.target.value)} />
            </label>
            <button onClick={handleSubmit}>Enter</button>
            <div>{message}</div>
        </div>
    );
}

export default App;
