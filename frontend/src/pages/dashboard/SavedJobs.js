import React, { useState, useEffect } from 'react';
import { Search, Star, Briefcase } from 'lucide-react';
import { api } from '../../lib/api';

const SavedJobs = () => {
  // State for saved jobs
  const [savedJobs, setSavedJobs] = useState([]);
  const [loading, setLoading] = useState(true);
  const [error, setError] = useState(null);

  // Fetch saved jobs on component mount
  useEffect(() => {
    fetchSavedJobs();
  }, []);

  const fetchSavedJobs = async () => {
    try {
      setLoading(true);
      // Assuming the API has a method to get saved jobs
      // If not, we can filter jobs with status "Saved" from all jobs
      try {
        const jobs = await api.getJobs();
        const saved = jobs.filter(job => job.status === 'Saved');
        setSavedJobs(saved);
        setError(null);
      } catch (apiError) {
        console.error('Backend API error:', apiError);
        // If we can't fetch from the backend, check localStorage for any saved jobs
        const localJobs = localStorage.getItem('savedJobs');
        if (localJobs) {
          setSavedJobs(JSON.parse(localJobs));
        } else {
          // Use mock data as fallback
          setSavedJobs([
            {
              id: 1,
              title: 'Senior Frontend Developer',
              company: 'Google',
              location: 'Mountain View, CA',
              salary: '$120,000 - $150,000',
              type: 'Full-time',
              saved: '2 days ago',
              description: 'We are looking for an experienced Frontend Developer to join our team...'
            },
            {
              id: 2,
              title: 'Full Stack Engineer',
              company: 'Microsoft',
              location: 'Redmond, WA',
              salary: '$130,000 - $160,000',
              type: 'Full-time',
              saved: '3 days ago',
              description: 'Join our team to build innovative solutions using the latest technologies...'
            },
            {
              id: 3,
              title: 'React Developer',
              company: 'Amazon',
              location: 'Seattle, WA',
              salary: '$110,000 - $140,000',
              type: 'Full-time',
              saved: '1 week ago',
              description: 'We are seeking a talented React Developer to help build our next-generation web applications...'
            }
          ]);
        }
        setError('Backend unavailable - showing local data');
      }
    } catch (err) {
      console.error('Error fetching saved jobs:', err);
      setError('Failed to fetch saved jobs');
      // Use mock data as fallback
      setSavedJobs([
        {
          id: 1,
          title: 'Senior Frontend Developer',
          company: 'Google',
          location: 'Mountain View, CA',
          salary: '$120,000 - $150,000',
          type: 'Full-time',
          saved: '2 days ago',
          description: 'We are looking for an experienced Frontend Developer to join our team...'
        },
        {
          id: 2,
          title: 'Full Stack Engineer',
          company: 'Microsoft',
          location: 'Redmond, WA',
          salary: '$130,000 - $160,000',
          type: 'Full-time',
          saved: '3 days ago',
          description: 'Join our team to build innovative solutions using the latest technologies...'
        },
        {
          id: 3,
          title: 'React Developer',
          company: 'Amazon',
          location: 'Seattle, WA',
          salary: '$110,000 - $140,000',
          type: 'Full-time',
          saved: '1 week ago',
          description: 'We are seeking a talented React Developer to help build our next-generation web applications...'
        }
      ]);
    } finally {
      setLoading(false);
    }
  };

  return (
    <div className="space-y-6">
      <div className="flex items-center justify-between">
        <h1 className="text-3xl font-bold">Saved Jobs</h1>
        <div className="text-sm text-gray-500">
          {savedJobs.length} jobs saved
        </div>
      </div>

      {/* Search */}
      <div className="relative">
        <Search className="absolute left-3 top-1/2 h-4 w-4 -translate-y-1/2 text-gray-500" />
        <input
          type="text"
          placeholder="Search saved jobs..."
          className="w-full rounded-md border border-gray-300 pl-10 py-2 text-sm focus:outline-none focus:ring-2 focus:ring-blue-500"
        />
      </div>

      {/* Error Message */}
      {error && (
        <div className="rounded-md bg-red-50 p-4 text-red-700">
          {error}
        </div>
      )}

      {/* Loading State */}
      {loading ? (
        <div className="text-center py-8">
          <p className="text-gray-500">Loading saved jobs...</p>
        </div>
      ) : (
        /* Saved Jobs */
        <div className="space-y-4">
          {savedJobs.length === 0 ? (
            <div className="text-center py-8 border border-gray-200 rounded-lg">
              <p className="text-gray-500">No saved jobs yet</p>
              <p className="text-sm text-gray-400 mt-2">Jobs you save will appear here</p>
            </div>
          ) : (
            savedJobs.map((job) => (
              <div key={job.id} className="rounded-lg border bg-white p-6 shadow-sm hover:shadow-md transition-shadow">
                <div className="flex items-start justify-between">
                  <div className="flex gap-4">
                    <div className="rounded-full bg-yellow-100 p-3 h-12 w-12 flex items-center justify-center">
                      <Briefcase className="h-6 w-6 text-yellow-500" />
                    </div>
                    <div>
                      <h3 className="font-medium text-lg">{job.title}</h3>
                      <p className="text-gray-600">{job.company} • {job.location}</p>
                      <div className="mt-2 flex flex-wrap gap-2">
                        <span className="inline-flex items-center rounded-full bg-blue-100 px-2.5 py-0.5 text-xs font-medium text-blue-800">
                          {job.type || job.level || 'Full-time'}
                        </span>
                        <span className="inline-flex items-center rounded-full bg-green-100 px-2.5 py-0.5 text-xs font-medium text-green-800">
                          {job.salary || `$${job.minSalary || 0} - $${job.maxSalary || 0}`}
                        </span>
                        <span className="inline-flex items-center rounded-full bg-yellow-100 px-2.5 py-0.5 text-xs font-medium text-yellow-800">
                          {job.saved ? `Saved ${job.saved}` : 'Saved'}
                        </span>
                      </div>
                    </div>
                  </div>
                  <div className="flex gap-2">
                    <button className="rounded-md bg-blue-600 px-3 py-1.5 text-sm font-medium text-white hover:bg-blue-700">
                      Apply
                    </button>
                    <button 
                      onClick={async () => {
                        try {
                          // Update job status to remove from saved
                          const updatedJob = { ...job, status: 'Applied' };
                          await api.updateJob(job.id, updatedJob);
                          // Refresh saved jobs
                          fetchSavedJobs();
                        } catch (err) {
                          console.error('Error updating job:', err);
                          alert('Failed to update job status');
                        }
                      }}
                      className="rounded-md border border-gray-300 px-3 py-1.5 text-sm font-medium hover:bg-gray-50"
                    >
                      <Star className="h-4 w-4 fill-yellow-400 text-yellow-400" />
                    </button>
                  </div>
                </div>
                <p className="mt-4 text-sm text-gray-600">{job.description}</p>
              </div>
            ))
          )}
        </div>
      )}
    </div>
  );
};

export default SavedJobs; 