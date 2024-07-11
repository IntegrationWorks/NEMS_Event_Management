import React, { useEffect, useState } from 'react';
import { Button, Form } from 'react-bootstrap';
import CreateEvent from './CreateEvent';
import EditEvent from './EditEvent';
import axios from 'axios';
import './ManageEvents.css';

function ManageEvents() {
  const [events, setEvents] = useState([]);
  const [selectedEvents, setSelectedEvents] = useState([]);
  const [showCreateModal, setShowCreateModal] = useState(false);
  const [showEditModal, setShowEditModal] = useState(false);
  const [editEventId, setEditEventId] = useState(null);
  const [loading, setLoading] = useState(false);
  const [selectAll, setSelectAll] = useState(false);

  const backendUrl = process.env.NODE_ENV === 'development' 
    ? 'http://localhost:5001' 
    : process.env.REACT_APP_BACKEND_URL;

  useEffect(() => {
    fetchEvents();
  }, []);

  const fetchEvents = () => {
    fetch(`${backendUrl}/events`)
      .then(response => response.json())
      .then(data => {
        setEvents(data);
      })
      .catch(error => console.error('Error fetching events:', error));
  };

  const handleNewEvent = () => {
    setShowCreateModal(true);
  };

  const handleCloseCreateModal = () => {
    setShowCreateModal(false);
    fetchEvents();
  };

  const handleEditEvent = (eventId) => {
    setEditEventId(eventId);
    setShowEditModal(true);
  };

  const handleCloseEditModal = (updated) => {
    setShowEditModal(false);
    setEditEventId(null);
    if (updated) {
      fetchEvents();
    }
  };

  const handleSelectEvent = (eventId) => {
    if (selectedEvents.includes(eventId)) {
      setSelectedEvents(selectedEvents.filter(id => id !== eventId));
    } else {
      setSelectedEvents([...selectedEvents, eventId]);
    }
  };

  const handleSelectAll = () => {
    if (selectAll) {
      setSelectedEvents([]);
      setSelectAll(false);
    } else {
      setSelectedEvents(events.map(event => event.id));
      setSelectAll(true);
    }
  };

  const handlePublishSelected = async () => {
    setLoading(true);
    const selectedEventObjects = events.filter(event => selectedEvents.includes(event.id));

    // Collect payload strings for all selected events
    const payload = selectedEventObjects.flatMap(event => [
      event.name,
      event.description,
      event.taxonomy,
      event.version
    ]);

    const message = {
      topic: selectedEventObjects[0]?.taxonomy || 'default/topic',
      payload: payload,
      interval: 3,
    };

    try {
      console.log('Sending message:', message); // Add this line to debug
      await axios.post(`${backendUrl}/send-message`, message, {
        headers: { 'Content-Type': 'application/json' },
      });
      alert('Selected events published successfully!');
    } catch (err) {
      console.error('Error publishing messages:', err.message);
      alert('Failed to publish selected events.');
    } finally {
      setLoading(false);
    }
  };

  return (
    <div className="manage-events-container">
      <h1>Manage Events</h1>
      <Button onClick={handleNewEvent} className="btn btn-primary">New Event</Button>
      <Button onClick={handlePublishSelected} className="btn btn-success" disabled={loading || selectedEvents.length === 0}>
        {loading ? 'Publishing...' : 'Publish Selected'}
      </Button>
      <table className="events-table">
        <thead>
          <tr>
            <th>
              <Form.Check
                type="checkbox"
                checked={selectAll}
                onChange={handleSelectAll}
              />
            </th>
            <th>Name</th>
            <th>Description</th>
            <th>Taxonomy</th>
            <th>Version</th>
            <th>Action</th>
          </tr>
        </thead>
        <tbody>
          {events.map(event => (
            <tr key={event.id}>
              <td>
                <Form.Check
                  type="checkbox"
                  checked={selectedEvents.includes(event.id)}
                  onChange={() => handleSelectEvent(event.id)}
                />
              </td>
              <td>{event.name}</td>
              <td>{event.description}</td>
              <td>{event.taxonomy}</td>
              <td>{event.version}</td>
              <td>
                <Button onClick={() => handleEditEvent(event.id)} className="btn btn-secondary">Edit</Button>
              </td>
            </tr>
          ))}
        </tbody>
      </table>
      <CreateEvent show={showCreateModal} handleClose={handleCloseCreateModal} />
      <EditEvent show={showEditModal} handleClose={handleCloseEditModal} eventId={editEventId} />
    </div>
  );
}

export default ManageEvents;
