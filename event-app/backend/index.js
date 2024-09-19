const express = require('express');
const cors = require('cors');
const { Pool } = require('pg');
require('dotenv').config();
const axios = require('axios');

const app = express();
app.use(cors());
app.use(express.json());

const pool = new Pool({
  user: process.env.DB_USER,
  host: process.env.DB_HOST,
  database: process.env.DB_NAME,
  password: process.env.DB_PASSWORD,
  port: process.env.DB_PORT,
});

// Endpoint to fetch all events
app.get('/events', async (req, res) => {
  try {
    const result = await pool.query('SELECT * FROM events');
    res.json(result.rows);
  } catch (err) {
    console.error('Error fetching events:', err.message);
    res.status(500).json({ error: 'Server error' });
  }
});

// Endpoint to create a new event
app.post('/events', async (req, res) => {
  const { name, description, taxonomy, version } = req.body;
  try {
    const result = await pool.query(
      'INSERT INTO events (name, description, taxonomy, version) VALUES ($1, $2, $3, $4) RETURNING *',
      [name, description, taxonomy, version]
    );
    res.json(result.rows[0]);
  } catch (err) {
    console.error('Error creating event:', err.message);
    res.status(500).json({ error: 'Server error' });
  }
});

// Endpoint to fetch a specific event by id
app.get('/events/:id', async (req, res) => {
  const { id } = req.params;
  try {
    const result = await pool.query('SELECT * FROM events WHERE id = $1', [id]);
    if (result.rows.length > 0) {
      res.json(result.rows[0]);
    } else {
      res.status(404).send('Event not found');
    }
  } catch (err) {
    console.error('Error fetching event:', err.message);
    res.status(500).json({ error: 'Server error' });
  }
});

// Endpoint to update an existing event by id
app.put('/events/:id', async (req, res) => {
  const { id } = req.params;
  const { name, description, taxonomy, version } = req.body;
  try {
    const result = await pool.query(
      'UPDATE events SET name = $1, description = $2, taxonomy = $3, version = $4 WHERE id = $5 RETURNING *',
      [name, description, taxonomy, version, id]
    );
    if (result.rows.length > 0) {
      res.json(result.rows[0]);
    } else {
      res.status(404).send('Event not found');
    }
  } catch (err) {
    console.error('Error updating event:', err.message);
    res.status(500).json({ error: 'Server error' });
  }
});

// Endpoint to send message to Test Publisher
app.post('/send-message', async (req, res) => {
  const { topic, payload, interval } = req.body;

  if (!Array.isArray(payload)) {
    console.log('Invalid payload format:', payload);
    return res.status(400).json({ error: 'payload must be an array of strings' });
  }

  const message = {
    topic,
    payload,
    interval: interval !== null ? interval : 0 // Default to 0 if interval is null
  };

  try {
    console.log('Payload to Test Publisher:', JSON.stringify(message, null, 2)); // Log the payload
      await axios.post(`${process.env.TEST_PUBLISHER_URL}`, message, {

      headers: { 'Content-Type': 'application/json' },
    });
    res.status(200).json({ status: 'Message sent to Test Publisher' });
  } catch (err) {
    console.error('Error sending message to Test Publisher:', err.message);
    res.status(500).json({ error: 'Failed to send message to Test Publisher' });
  }
});

const PORT = process.env.PORT || 5000;
app.listen(PORT, () => {
  console.log(`Server is running on port ${PORT}`);
});
