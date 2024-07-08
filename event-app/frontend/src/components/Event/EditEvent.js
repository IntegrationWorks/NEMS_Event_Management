// src/components/EditEvent.js
import React, { useEffect, useState } from 'react';
import { Modal, Button, Form } from 'react-bootstrap';
import { toast, ToastContainer } from 'react-toastify';
import 'react-toastify/dist/ReactToastify.css';
import './CreateEvent.css';

function EditEvent({ show, handleClose, eventId }) {
  const [event, setEvent] = useState(null);

  useEffect(() => {
    if (eventId) {
      fetch(`http://localhost:5000/events/${eventId}`)
        .then(response => {
          if (!response.ok) {
            throw new Error('Network response was not ok');
          }
          return response.json();
        })
        .then(data => {
          setEvent(data);
        })
        .catch(error => console.error('Error fetching event:', error));
    }
  }, [eventId]);

  const handleSubmit = async (e) => {
    e.preventDefault();

    const updatedEvent = {
      name: event.name,
      description: event.description,
      taxonomy: event.taxonomy,
      version: event.version,
    };

    try {
      const response = await fetch(`http://localhost:5000/events/${eventId}`, {
        method: 'PUT',
        headers: {
          'Content-Type': 'application/json',
        },
        body: JSON.stringify(updatedEvent),
      });

      if (response.ok) {
        toast.success('Event updated successfully!');
        handleClose(true); // Indicate that the event was updated
      } else {
        const errorData = await response.json();
        toast.error(`Failed to update event: ${errorData.error}`);
      }
    } catch (error) {
      console.error('Error updating event:', error);
      toast.error('Failed to update event. Network error or server is down.');
    }
  };

  const handleChange = (e) => {
    const { name, value } = e.target;
    setEvent(prevEvent => ({
      ...prevEvent,
      [name]: value,
    }));
  };

  if (!event) {
    return null; // or a loading spinner
  }

  return (
    <>
      <ToastContainer />
      <Modal show={show} onHide={() => handleClose(false)}>
        <Modal.Header closeButton>
          <Modal.Title>Edit Event</Modal.Title>
        </Modal.Header>
        <Form onSubmit={handleSubmit}>
          <Modal.Body>
            <Form.Group controlId="eventName">
              <Form.Label>Name</Form.Label>
              <Form.Control
                type="text"
                name="name"
                value={event.name}
                onChange={handleChange}
                required
              />
            </Form.Group>
            <Form.Group controlId="eventDescription">
              <Form.Label>Description</Form.Label>
              <Form.Control
                as="textarea"
                name="description"
                value={event.description}
                onChange={handleChange}
                required
              />
            </Form.Group>
            <Form.Group controlId="eventTaxonomy">
              <Form.Label>Taxonomy</Form.Label>
              <Form.Control
                type="text"
                name="taxonomy"
                value={event.taxonomy}
                onChange={handleChange}
                required
              />
            </Form.Group>
            <Form.Group controlId="eventVersion">
              <Form.Label>Version</Form.Label>
              <Form.Control
                type="text"
                name="version"
                value={event.version}
                onChange={handleChange}
                required
              />
            </Form.Group>
          </Modal.Body>
          <Modal.Footer>
            <Button variant="secondary" onClick={() => handleClose(false)}>
              Cancel
            </Button>
            <Button variant="primary" type="submit">
              Update
            </Button>
          </Modal.Footer>
        </Form>
      </Modal>
    </>
  );
}

export default EditEvent;
