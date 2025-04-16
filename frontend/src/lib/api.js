import {getAuthHeader} from './supabase';

// Replace with your backend URL
const API_URL = process.env.REACT_APP_API_URL || 'http://localhost:8080';

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

    searchJobs: async (searchParams) => {
        // get auth token from supabase
        const headers = await getAuthHeader();

        // format the parameters correctly for the url
        const queryString = searchParams instanceof URLSearchParams
            ? searchParams.toString()
            : new URLSearchParams(searchParams).toString();

        // debug log showing what we're sending
        console.log('API call to:', `${API_URL}/jobs/search?${queryString}`);

        // actually call our backend search endpoint
        const response = await fetch(`${API_URL}/jobs/search?${queryString}`, {
            headers: {
                ...headers,
                'Content-Type': 'application/json'
            }
        });

        // log what we got back
        console.log('Response status:', response.status);

        // backend returns 204 when no matches found
        if (response.status === 204) {
            console.log('Received 204 No Content - returning empty array');
            return [];
        }

        // throw error if something went wrong
        if (!response.ok) {
            console.error(`Error response: ${response.status} ${response.statusText}`);
            throw new Error(`Search failed with status: ${response.status}`);
        }

        // parse json and return the job listings
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

        // log what we're sending for debugging
        console.log('Updating job:', jobId, 'with data:', jobData);

        try {
            const response = await fetch(`${API_URL}/jobs/${jobId}`, {
                method: 'PUT',
                headers: {
                    ...headers,
                    'Content-Type': 'application/json'
                },
                body: JSON.stringify(jobData)
            });

            if (!response.ok) {
                // try to get detailed error message
                const errorText = await response.text();
                console.error('Update failed:', response.status, errorText);
                throw new Error(`Failed to update job: ${response.status} ${errorText}`);
            }

            return response.json();
        } catch (error) {
            console.error('Job update API error:', error);
            throw error;
        }
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