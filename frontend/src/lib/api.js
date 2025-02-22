import { getAuthHeader } from './supabase';

// Replace with your backend URL
const API_URL = 'http://localhost:8080';

export const api = {
  // Profile endpoints
  getCurrentProfile: async () => {
    const headers = await getAuthHeader();
    const response = await fetch(`${API_URL}/profiles/current`, {
      headers: {
        ...headers,
        'Content-Type': 'application/json'
      }
    });
    if (!response.ok) {
      throw new Error('Failed to fetch profile');
    }
    return response.json();
  },

  updateProfile: async (profileData) => {
    const headers = await getAuthHeader();
    const response = await fetch(`${API_URL}/profiles/current`, {
      method: 'PUT',
      headers: {
        ...headers,
        'Content-Type': 'application/json'
      },
      body: JSON.stringify(profileData)
    });
    if (!response.ok) {
      throw new Error('Failed to update profile');
    }
    return response.json();
  },

  // Job endpoints
  getJobs: async () => {
    const headers = await getAuthHeader();
    const response = await fetch(`${API_URL}/jobs`, {
      headers: {
        ...headers,
        'Content-Type': 'application/json'
      }
    });
    if (!response.ok) {
      throw new Error('Failed to fetch jobs');
    }
    return response.json();
  },

  createJob: async (jobData) => {
    const headers = await getAuthHeader();
    const response = await fetch(`${API_URL}/jobs`, {
      method: 'POST',
      headers: {
        ...headers,
        'Content-Type': 'application/json'
      },
      body: JSON.stringify(jobData)
    });
    if (!response.ok) {
      throw new Error('Failed to create job');
    }
    return response.json();
  },

  updateJob: async (jobId, jobData) => {
    const headers = await getAuthHeader();
    const response = await fetch(`${API_URL}/jobs/${jobId}`, {
      method: 'PUT',
      headers: {
        ...headers,
        'Content-Type': 'application/json'
      },
      body: JSON.stringify(jobData)
    });
    if (!response.ok) {
      throw new Error('Failed to update job');
    }
    return response.json();
  },

  deleteJob: async (jobId) => {
    const headers = await getAuthHeader();
    const response = await fetch(`${API_URL}/jobs/${jobId}`, {
      method: 'DELETE',
      headers: {
        ...headers,
        'Content-Type': 'application/json'
      }
    });
    if (!response.ok) {
      throw new Error('Failed to delete job');
    }
    return response.json();
  },

  // Interview endpoints
  getInterviews: async () => {
    const headers = await getAuthHeader();
    const response = await fetch(`${API_URL}/interviews`, {
      headers: {
        ...headers,
        'Content-Type': 'application/json'
      }
    });
    if (!response.ok) {
      throw new Error('Failed to fetch interviews');
    }
    return response.json();
  },

  createInterview: async (interviewData) => {
    const headers = await getAuthHeader();
    const response = await fetch(`${API_URL}/interviews`, {
      method: 'POST',
      headers: {
        ...headers,
        'Content-Type': 'application/json'
      },
      body: JSON.stringify(interviewData)
    });
    if (!response.ok) {
      throw new Error('Failed to create interview');
    }
    return response.json();
  },

  updateInterview: async (interviewId, interviewData) => {
    const headers = await getAuthHeader();
    const response = await fetch(`${API_URL}/interviews/${interviewId}`, {
      method: 'PUT',
      headers: {
        ...headers,
        'Content-Type': 'application/json'
      },
      body: JSON.stringify(interviewData)
    });
    if (!response.ok) {
      throw new Error('Failed to update interview');
    }
    return response.json();
  },

  deleteInterview: async (interviewId) => {
    const headers = await getAuthHeader();
    const response = await fetch(`${API_URL}/interviews/${interviewId}`, {
      method: 'DELETE',
      headers: {
        ...headers,
        'Content-Type': 'application/json'
      }
    });
    if (!response.ok) {
      throw new Error('Failed to delete interview');
    }
    return response.json();
  }
}; 