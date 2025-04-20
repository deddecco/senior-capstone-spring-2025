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
        try {
            const response = await fetch(`${API_URL}/jobs`, {
                headers: {
                    ...headers,
                    'Content-Type': 'application/json'
                }
            });

            // Check if response is ok (status in the range 200-299)
            if (!response.ok) {
                throw new Error(`Failed to fetch jobs: ${response.status} ${response.statusText}`);
            }

            // Check for empty response
            const text = await response.text();
            if (!text) {
                return []; // Return empty array for empty responses
            }

            try {
                // Try to parse the response as JSON
                return JSON.parse(text);
            } catch (parseError) {
                console.error('Error parsing JSON response:', parseError);
                throw new Error('Invalid JSON response from server');
            }
        } catch (error) {
            console.error('API call failed:', error);
            throw error;
        }
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
        try {
            const headers = await getAuthHeader();
            const response = await fetch(`${API_URL}/jobs`, {
                method: 'POST',
                headers: {
                    ...headers,
                    'Content-Type': 'application/json'
                },
                body: JSON.stringify(jobData)
            });

            // Log response for debugging
            console.log('Create job response status:', response.status);

            if (!response.ok) {
                let errorMessage = 'Failed to create job';
                try {
                    // Try to get detailed error from response
                    const errorData = await response.json();
                    if (errorData && errorData.message) {
                        errorMessage = errorData.message;
                    }
                } catch (parseError) {
                    // If can't parse error response, use status code
                    errorMessage = `Server error (${response.status}) - Failed to create job`;
                }
                throw new Error(errorMessage);
            }

            return response.json();
        } catch (error) {
            console.error('Create job API error:', error);
            throw error;
        }
    },

    updateJob: async (jobId, jobData) => {
        const headers = await getAuthHeader();
        try {
            console.log('Updating job:', jobId, 'with data:', jobData);

            const response = await fetch(`${API_URL}/jobs/${jobId}`, {
                method: 'PUT',
                headers: {
                    ...headers,
                    'Content-Type': 'application/json'
                },
                body: JSON.stringify(jobData)
            });

            if (!response.ok) {
                // Try to get error details from response
                let errorMessage = 'Failed to update job';
                try {
                    const errorData = await response.json();
                    if (errorData.message) {
                        errorMessage = errorData.message;
                    }
                } catch (parseError) {
                    // If can't parse response, use status
                    errorMessage = `Server error (${response.status}) - ${errorMessage}`;
                }
                throw new Error(errorMessage);
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
    },

    getFavoriteJobs: async () => {
        const headers = await getAuthHeader();
        const response = await fetch(`${API_URL}/jobs/favorites`, {
            headers: {
                ...headers,
                'Content-Type': 'application/json'
            }
        });

        if (!response.ok) {
            if (response.status === 204) {
                return []; // No content means empty array
            }
            throw new Error('Failed to fetch favorite jobs');
        }

        return response.json();
    },

    toggleJobFavorite: async (jobId, action) => {
        const headers = await getAuthHeader();
        const response = await fetch(`${API_URL}/jobs/${jobId}/${action}`, {
            method: 'PUT',
            headers: {
                ...headers,
                'Content-Type': 'application/json'
            }
        });

        if (!response.ok) {
            throw new Error(`Failed to ${action} job`);
        }

        return response.json();
    },
};