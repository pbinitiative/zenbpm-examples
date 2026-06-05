'use strict';

const express = require('express');
const path = require('path');

const app = express();
app.use(express.static(path.join(__dirname, 'public')));

const PORT = process.env.PORT || 8888;
app.listen(PORT, () => console.log(`\n  ZenBPM Showcase Launcher → http://localhost:${PORT}\n`));
