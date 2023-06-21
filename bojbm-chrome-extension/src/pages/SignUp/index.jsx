import React from 'react';
import { createRoot } from 'react-dom/client';

import SignUp from './SignUp';
import './index.css';

const container = document.getElementById('app-container');
const root = createRoot(container);
root.render(<SignUp />);
