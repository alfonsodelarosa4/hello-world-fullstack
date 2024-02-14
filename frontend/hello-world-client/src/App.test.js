import { render, screen } from '@testing-library/react';
import App from './App';

test('renders the label with "Enter your name:"', () => {
  render(<App />);
  const labelElement = screen.getByText('Enter your name:');
  expect(labelElement).toBeInTheDocument();
});
