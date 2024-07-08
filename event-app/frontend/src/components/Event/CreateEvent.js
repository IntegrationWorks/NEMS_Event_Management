// src/components/CreateEvent.js
import React, { useState } from 'react';
import { Modal, Button, Form } from 'react-bootstrap';
import { toast, ToastContainer } from 'react-toastify';
import 'react-toastify/dist/ReactToastify.css';
import './CreateEvent.css';

function CreateEvent({ show, handleClose }) {
  const [name, setName] = useState('');
  const [description, setDescription] = useState('');
  const [taxonomy, setTaxonomy] = useState('');
  const [version, setVersion] = useState('');

  const handleSubmit = async (e) => {
    e.preventDefault();

    const newEvent = {
      name,
      description,
      taxonomy,
      version
    };

    try {
      const response = await fetch('http://localhost:5000/events', {
        method: 'POST',
        headers: {
          'Content-Type': 'application/json',
        },
        body: JSON.stringify(newEvent),
      });

      if (response.ok) {
        toast.success('Event created successfully!');
        handleClose();
      } else {
        const errorData = await response.json();
        toast.error(`Failed to create event: ${errorData.error}`);
      }
    } catch (error) {
      console.error('Error creating event:', error);
      toast.error('Failed to create event. Network error or server is down.');
    }
  };

  return (
    <>
      <ToastContainer />
      <Modal show={show} onHide={handleClose}>
        <Modal.Header closeButton>
          <Modal.Title>Create Event</Modal.Title>
        </Modal.Header>
        <Form onSubmit={handleSubmit}>
          <Modal.Body>
            <Form.Group controlId="eventName">
              <Form.Label>Name</Form.Label>
              <Form.Control
                type="text"
                value={name}
                onChange={(e) => setName(e.target.value)}
                required
              />
            </Form.Group>
            <Form.Group controlId="eventDescription">
              <Form.Label>Description</Form.Label>
              <Form.Control
                as="textarea"
                value={description}
                onChange={(e) => setDescription(e.target.value)}
                required
              />
            </Form.Group>
            <Form.Group controlId="eventTaxonomy">
              <Form.Label>Taxonomy</Form.Label>
              <Form.Control
                type="text"
                value={taxonomy}
                onChange={(e) => setTaxonomy(e.target.value)}
                required
              />
            </Form.Group>
            <Form.Group controlId="eventVersion">
              <Form.Label>Version</Form.Label>
              <Form.Control
                type="text"
                value={version}
                onChange={(e) => setVersion(e.target.value)}
                required
              />
            </Form.Group>
          </Modal.Body>
          <Modal.Footer>
            <Button variant="secondary" onClick={handleClose}>
              Cancel
            </Button>
            <Button variant="primary" type="submit">
              Create
            </Button>
          </Modal.Footer>
        </Form>
      </Modal>
    </>
  );
}

export default CreateEvent;
