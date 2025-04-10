import React, { useState, useEffect } from 'react';
import { Search, Filter, Briefcase, Plus, X } from 'lucide-react';
import { api } from '../../lib/api';

const JobListings = () => {
  // State for job listings
  const [jobListings, setJobListings] = useState([]);
  const [loading, setLoading] = useState(true);
  const [error, setError] = useState(null);

  // State for search and filters
  const [searchQuery, setSearchQuery] = useState('');
  const [filters, setFilters] = useState({
    title: '',
    level: '',
    minSalary: '',
    maxSalary: '',
    location: '',
    status: ''
  });
  const [showFilters, setShowFilters] = useState(false);

  // State for modal
  const [isModalOpen, setIsModalOpen] = useState(false);
  const [newJob, setNewJob] = useState({
    title: '',
    company: '',
    location: '',
    minSalary: '',
    maxSalary: '',
    type: 'Full-time',
    status: 'Applied'
  });

  // Fetch jobs on component mount
  useEffect(() => {
    fetchJobs();
  }, []);

  const fetchJobs = async () => {
    try {
      setLoading(true);
      try {
        const jobs = await api.getJobs();
        setJobListings(jobs);
        setError(null);
      } catch (apiError) {
        console.error('Backend API error:', apiError);
        // If we can't fetch from the backend, check localStorage
        const localJobs = localStorage.getItem('jobListings');
        if (localJobs) {
          setJobListings(JSON.parse(localJobs));
          setError('Backend unavailable - showing local data');
        } else {
          // Use mock data as fallback
          const mockJobs = [
            {
              id: 1,
              title: 'Senior Frontend Developer',
              company: 'Google',
              location: 'Mountain View, CA',
              salary: '$120,000 - $150,000',
              type: 'Full-time',
              posted: '2 days ago',
              description: 'We are looking for an experienced Frontend Developer to join our team...'
            },
            {
              id: 2,
              title: 'Full Stack Engineer',
              company: 'Microsoft',
              location: 'Redmond, WA',
              salary: '$130,000 - $160,000',
              type: 'Full-time',
              posted: '3 days ago',
              description: 'Join our team to build innovative solutions using the latest technologies...'
            },
            {
              id: 3,
              title: 'React Developer',
              company: 'Amazon',
              location: 'Seattle, WA',
              salary: '$110,000 - $140,000',
              type: 'Full-time',
              posted: '1 week ago',
              description: 'We are seeking a talented React Developer to help build our next-generation web applications...'
            },
            {
              id: 4,
              title: 'Frontend Engineer',
              company: 'Netflix',
              location: 'Los Gatos, CA',
              salary: '$125,000 - $155,000',
              type: 'Full-time',
              posted: '5 days ago',
              description: 'Join our UI engineering team to create engaging user experiences...'
            },
            {
              id: 5,
              title: 'JavaScript Developer',
              company: 'Meta',
              location: 'Menlo Park, CA',
              salary: '$115,000 - $145,000',
              type: 'Full-time',
              posted: '2 weeks ago',
              description: 'We are looking for a JavaScript Developer to join our growing team...'
            }
          ];
          setJobListings(mockJobs);
          // Save mock data to localStorage
          localStorage.setItem('jobListings', JSON.stringify(mockJobs));
          setError('Backend unavailable - showing mock data');
        }
      }
    } catch (err) {
      console.error('Error fetching jobs:', err);
      setError('Failed to fetch jobs');
      // Use mock data as fallback
      setJobListings([
        {
          id: 1,
          title: 'Senior Frontend Developer',
          company: 'Google',
          location: 'Mountain View, CA',
          salary: '$120,000 - $150,000',
          type: 'Full-time',
          posted: '2 days ago',
          description: 'We are looking for an experienced Frontend Developer to join our team...'
        },
        {
          id: 2,
          title: 'Full Stack Engineer',
          company: 'Microsoft',
          location: 'Redmond, WA',
          salary: '$130,000 - $160,000',
          type: 'Full-time',
          posted: '3 days ago',
          description: 'Join our team to build innovative solutions using the latest technologies...'
        },
        {
          id: 3,
          title: 'React Developer',
          company: 'Amazon',
          location: 'Seattle, WA',
          salary: '$110,000 - $140,000',
          type: 'Full-time',
          posted: '1 week ago',
          description: 'We are seeking a talented React Developer to help build our next-generation web applications...'
        },
        {
          id: 4,
          title: 'Frontend Engineer',
          company: 'Netflix',
          location: 'Los Gatos, CA',
          salary: '$125,000 - $155,000',
          type: 'Full-time',
          posted: '5 days ago',
          description: 'Join our UI engineering team to create engaging user experiences...'
        },
        {
          id: 5,
          title: 'JavaScript Developer',
          company: 'Meta',
          location: 'Menlo Park, CA',
          salary: '$115,000 - $145,000',
          type: 'Full-time',
          posted: '2 weeks ago',
          description: 'We are looking for a JavaScript Developer to join our growing team...'
        }
      ]);
    } finally {
      setLoading(false);
    }
  };

// search function for finding jobs by different criteria
  const searchJobs = async () => {
    try {
      setLoading(true);
      setError(null);

      // build search params
      const params = new URLSearchParams();

      // use main search box for title search
      if (searchQuery.trim()) {
        params.append('title', searchQuery.trim());
      }

      // add filters from advanced search panel
      if (filters.title && !searchQuery.trim()) params.append('title', filters.title);
      if (filters.level) params.append('level', filters.level);
      if (filters.minSalary) params.append('minSalary', filters.minSalary);
      if (filters.maxSalary) params.append('maxSalary', filters.maxSalary);
      if (filters.location) params.append('location', filters.location);
      if (filters.status) params.append('status', filters.status);

      // debug log
      const searchEndpoint = `/jobs/search?${params.toString()}`;
      console.log('searching with:', searchEndpoint);

      try {
        // call our backend api
        console.log('sending search request...');
        const jobs = await api.searchJobs(params);
        console.log('got search response:', jobs);

        if (jobs && jobs.length > 0) {
          setJobListings(jobs);
          console.log(`found ${jobs.length} matching jobs`);
        } else {
          setJobListings([]);
          setError('No jobs found matching your search criteria');
          console.log('no matching jobs found');
        }
      } catch (apiError) {
        console.error('search api error:', apiError);

        // show error to user
        let errorMessage = 'Failed to search jobs';
        if (apiError.message) {
          errorMessage += ` - ${apiError.message}`;
        }

        setError(errorMessage);
      }
    } catch (err) {
      console.error('search error:', err);
      setError('Failed to search jobs - please try again later');
    } finally {
      setLoading(false);
    }
  };

// update search query when user types
  const handleSearchChange = (e) => {
    setSearchQuery(e.target.value);
  };

// handle search form submission
  const handleSearchSubmit = (e) => {
    e.preventDefault();
    searchJobs();
  };

// handle changes to filter fields
  const handleFilterChange = (e) => {
    const { name, value } = e.target;
    setFilters(prev => ({ ...prev, [name]: value }));
  };

// reset all search filters
  const clearFilters = () => {
    setFilters({
      title: '',
      level: '',
      minSalary: '',
      maxSalary: '',
      location: '',
      status: ''
    });
    setSearchQuery('');
    fetchJobs(); // get all jobs without filters
  };

  const handleInputChange = (e) => {
    const { name, value } = e.target;
    setNewJob(prev => ({ ...prev, [name]: value }));
  };

  const handleSubmit = async (e) => {
    e.preventDefault();
    try {
      // Format the job data for the API
      const jobData = {
        title: newJob.title,
        level: newJob.type, // Map type to level as expected by backend
        minSalary: parseInt(newJob.minSalary) || 0,
        maxSalary: parseInt(newJob.maxSalary) || 0,
        location: newJob.location,
        status: newJob.status,
        // Note: Backend doesn't have a company field in the Job model
        // We'll include it in the title for now
        description: `Company: ${newJob.company}`
      };

      // Try to call the API to create the job
      try {
        await api.createJob(jobData);
        // Refresh jobs list after successful creation
        fetchJobs();
      } catch (apiError) {
        console.error('Backend API error:', apiError);
        // Fallback: Add job locally when backend is unavailable
        const newJobWithId = {
          ...jobData,
          company: newJob.company, // Keep company for local display
          id: Date.now(), // Use timestamp as a temporary ID
          posted: 'Just now',
          description: 'Added locally (backend unavailable)'
        };

        const updatedJobs = [newJobWithId, ...jobListings];
        setJobListings(updatedJobs);

        // Save to localStorage
        localStorage.setItem('jobListings', JSON.stringify(updatedJobs));

        console.log('Job added locally due to backend unavailability');
      }

      // Close the modal and reset the form
      setIsModalOpen(false);
      setNewJob({
        title: '',
        company: '',
        location: '',
        minSalary: '',
        maxSalary: '',
        type: 'Full-time',
        status: 'Applied'
      });
    } catch (err) {
      console.error('Error creating job:', err);
      alert('Failed to create job. Please try again.');
    }
  };

  return (
      <div className="space-y-6">
        <div className="flex items-center justify-between">
          <h1 className="text-3xl font-bold">Job Listings</h1>
          <button
              onClick={() => setIsModalOpen(true)}
              className="inline-flex items-center gap-2 rounded-md bg-blue-600 px-4 py-2 text-sm font-medium text-white hover:bg-blue-700"
          >
            <Plus className="h-4 w-4" />
            Add Job
          </button>
        </div>

        {/* Search Form */}
        <form onSubmit={handleSearchSubmit} className="flex flex-col gap-4 md:flex-row">
          <div className="relative flex-1">
            <Search className="absolute left-3 top-1/2 h-4 w-4 -translate-y-1/2 text-gray-500" />
            <input
                type="text"
                placeholder="Search for jobs..."
                className="w-full rounded-md border border-gray-300 pl-10 py-2 text-sm focus:outline-none focus:ring-2 focus:ring-blue-500"
                value={searchQuery}
                onChange={handleSearchChange}
            />
          </div>
          <button
              type="submit"
              className="rounded-md bg-blue-600 px-4 py-2 text-sm font-medium text-white hover:bg-blue-700"
          >
            Search
          </button>
          <button
              type="button"
              onClick={() => setShowFilters(prev => !prev)}
              className="inline-flex items-center gap-2 rounded-md border border-gray-300 px-4 py-2 text-sm"
          >
            <Filter className="h-4 w-4" />
            {showFilters ? 'Hide Filters' : 'Show Filters'}
          </button>
        </form>

        {/* NEW: Expanded Filters */}
        {showFilters && (
            <div className="rounded-lg border bg-white p-4 shadow-sm">
              <div className="grid grid-cols-1 md:grid-cols-2 lg:grid-cols-3 gap-4">
                <div>
                  <label className="block text-sm font-medium text-gray-700 mb-1">
                    Job Title
                  </label>
                  <input
                      type="text"
                      name="title"
                      value={filters.title}
                      onChange={handleFilterChange}
                      className="w-full rounded-md border border-gray-300 px-3 py-2 text-sm focus:outline-none focus:ring-2 focus:ring-blue-500"
                  />
                </div>

                <div>
                  <label className="block text-sm font-medium text-gray-700 mb-1">
                    Job Type
                  </label>
                  <select
                      name="level"
                      value={filters.level}
                      onChange={handleFilterChange}
                      className="w-full rounded-md border border-gray-300 px-3 py-2 text-sm focus:outline-none focus:ring-2 focus:ring-blue-500"
                  >
                    <option value="">All Types</option>
                    <option value="Full-time">Full-time</option>
                    <option value="Part-time">Part-time</option>
                    <option value="Contract">Contract</option>
                    <option value="Internship">Internship</option>
                  </select>
                </div>

                <div>
                  <label className="block text-sm font-medium text-gray-700 mb-1">
                    Location
                  </label>
                  <input
                      type="text"
                      name="location"
                      value={filters.location}
                      onChange={handleFilterChange}
                      className="w-full rounded-md border border-gray-300 px-3 py-2 text-sm focus:outline-none focus:ring-2 focus:ring-blue-500"
                  />
                </div>

                <div>
                  <label className="block text-sm font-medium text-gray-700 mb-1">
                    Min Salary
                  </label>
                  <input
                      type="number"
                      name="minSalary"
                      value={filters.minSalary}
                      onChange={handleFilterChange}
                      className="w-full rounded-md border border-gray-300 px-3 py-2 text-sm focus:outline-none focus:ring-2 focus:ring-blue-500"
                  />
                </div>

                <div>
                  <label className="block text-sm font-medium text-gray-700 mb-1">
                    Max Salary
                  </label>
                  <input
                      type="number"
                      name="maxSalary"
                      value={filters.maxSalary}
                      onChange={handleFilterChange}
                      className="w-full rounded-md border border-gray-300 px-3 py-2 text-sm focus:outline-none focus:ring-2 focus:ring-blue-500"
                  />
                </div>

                <div>
                  <label className="block text-sm font-medium text-gray-700 mb-1">
                    Status
                  </label>
                  <select
                      name="status"
                      value={filters.status}
                      onChange={handleFilterChange}
                      className="w-full rounded-md border border-gray-300 px-3 py-2 text-sm focus:outline-none focus:ring-2 focus:ring-blue-500"
                  >
                    <option value="">All Statuses</option>
                    <option value="Applied">Applied</option>
                    <option value="Interview">Interview</option>
                    <option value="Offer">Offer</option>
                    <option value="Rejected">Rejected</option>
                    <option value="Accepted">Accepted</option>
                  </select>
                </div>
              </div>

              <div className="mt-4 flex justify-end">
                <button
                    type="button"
                    onClick={clearFilters}
                    className="mr-2 rounded-md border border-gray-300 px-4 py-2 text-sm font-medium hover:bg-gray-50"
                >
                  Clear Filters
                </button>
                <button
                    type="button"
                    onClick={searchJobs}
                    className="rounded-md bg-blue-600 px-4 py-2 text-sm font-medium text-white hover:bg-blue-700"
                >
                  Apply Filters
                </button>
              </div>
            </div>
        )}

        {/* Error Message */}
        {error && (
            <div className="rounded-md bg-red-50 p-4 text-red-700">
              {error}
            </div>
        )}

        {/* Loading State */}
        {loading ? (
            <div className="text-center py-8">
              <p className="text-gray-500">Loading job listings...</p>
            </div>
        ) : (
            /* Job Listings */
            <div className="space-y-4">
              {jobListings.length === 0 ? (
                  <div className="text-center py-8">
                    <p className="text-gray-500">No job listings found. Try adjusting your search criteria.</p>
                  </div>
              ) : (
                  jobListings.map((job) => (
                      <div key={job.id} className="rounded-lg border bg-white p-6 shadow-sm hover:shadow-md transition-shadow">
                        <div className="flex items-start justify-between">
                          <div className="flex gap-4">
                            <div className="rounded-full bg-blue-100 p-3 h-12 w-12 flex items-center justify-center">
                              <Briefcase className="h-6 w-6 text-blue-500" />
                            </div>
                            <div>
                              <h3 className="font-medium text-lg">{job.title}</h3>
                              <p className="text-gray-600">{job.company} • {job.location}</p>
                              <div className="mt-2 flex flex-wrap gap-2">
                        <span className="inline-flex items-center rounded-full bg-blue-100 px-2.5 py-0.5 text-xs font-medium text-blue-800">
                          {job.type || job.level}
                        </span>
                                <span className="inline-flex items-center rounded-full bg-green-100 px-2.5 py-0.5 text-xs font-medium text-green-800">
                          {job.salary || `$${job.minSalary || 0} - $${job.maxSalary || 0}`}
                        </span>
                                <span className="inline-flex items-center rounded-full bg-gray-100 px-2.5 py-0.5 text-xs font-medium text-gray-800">
                          {job.posted || 'Recently added'}
                        </span>
                                {job.status && (
                                    <span className="inline-flex items-center rounded-full bg-purple-100 px-2.5 py-0.5 text-xs font-medium text-purple-800">
                            {job.status}
                          </span>
                                )}
                              </div>
                            </div>
                          </div>
                          <div className="flex gap-2">
                            <button className="rounded-md bg-blue-600 px-3 py-1.5 text-sm font-medium text-white hover:bg-blue-700">
                              Apply
                            </button>
                            <button className="rounded-md border border-gray-300 px-3 py-1.5 text-sm font-medium hover:bg-gray-50">
                              Save
                            </button>
                          </div>
                        </div>
                        <p className="mt-4 text-sm text-gray-600">{job.description}</p>
                      </div>
                  ))
              )}
            </div>
        )}

        {/* Add Job Modal */}
        {isModalOpen && (
            <div className="fixed inset-0 bg-black bg-opacity-50 flex items-center justify-center z-50">
              <div className="bg-white rounded-lg p-6 w-full max-w-md">
                <div className="flex justify-between items-center mb-4">
                  <h2 className="text-xl font-bold">Add New Job</h2>
                  <button
                      onClick={() => setIsModalOpen(false)}
                      className="text-gray-500 hover:text-gray-700"
                  >
                    <X className="h-5 w-5" />
                  </button>
                </div>

                <form onSubmit={handleSubmit}>
                  <div className="space-y-4">
                    <div>
                      <label className="block text-sm font-medium text-gray-700 mb-1">
                        Job Title
                      </label>
                      <input
                          type="text"
                          name="title"
                          value={newJob.title}
                          onChange={handleInputChange}
                          required
                          className="w-full rounded-md border border-gray-300 px-3 py-2 text-sm focus:outline-none focus:ring-2 focus:ring-blue-500"
                      />
                    </div>

                    <div>
                      <label className="block text-sm font-medium text-gray-700 mb-1">
                        Company Name
                      </label>
                      <input
                          type="text"
                          name="company"
                          value={newJob.company}
                          onChange={handleInputChange}
                          required
                          className="w-full rounded-md border border-gray-300 px-3 py-2 text-sm focus:outline-none focus:ring-2 focus:ring-blue-500"
                      />
                    </div>

                    <div>
                      <label className="block text-sm font-medium text-gray-700 mb-1">
                        Location
                      </label>
                      <input
                          type="text"
                          name="location"
                          value={newJob.location}
                          onChange={handleInputChange}
                          required
                          className="w-full rounded-md border border-gray-300 px-3 py-2 text-sm focus:outline-none focus:ring-2 focus:ring-blue-500"
                      />
                    </div>

                    <div className="grid grid-cols-2 gap-4">
                      <div>
                        <label className="block text-sm font-medium text-gray-700 mb-1">
                          Min Salary
                        </label>
                        <input
                            type="number"
                            name="minSalary"
                            value={newJob.minSalary}
                            onChange={handleInputChange}
                            required
                            className="w-full rounded-md border border-gray-300 px-3 py-2 text-sm focus:outline-none focus:ring-2 focus:ring-blue-500"
                        />
                      </div>

                      <div>
                        <label className="block text-sm font-medium text-gray-700 mb-1">
                          Max Salary
                        </label>
                        <input
                            type="number"
                            name="maxSalary"
                            value={newJob.maxSalary}
                            onChange={handleInputChange}
                            required
                            className="w-full rounded-md border border-gray-300 px-3 py-2 text-sm focus:outline-none focus:ring-2 focus:ring-blue-500"
                        />
                      </div>
                    </div>

                    <div>
                      <label className="block text-sm font-medium text-gray-700 mb-1">
                        Job Type
                      </label>
                      <select
                          name="type"
                          value={newJob.type}
                          onChange={handleInputChange}
                          className="w-full rounded-md border border-gray-300 px-3 py-2 text-sm focus:outline-none focus:ring-2 focus:ring-blue-500"
                      >
                        <option value="Full-time">Full-time</option>
                        <option value="Part-time">Part-time</option>
                        <option value="Contract">Contract</option>
                        <option value="Internship">Internship</option>
                      </select>
                    </div>

                    <div>
                      <label className="block text-sm font-medium text-gray-700 mb-1">
                        Status
                      </label>
                      <select
                          name="status"
                          value={newJob.status}
                          onChange={handleInputChange}
                          className="w-full rounded-md border border-gray-300 px-3 py-2 text-sm focus:outline-none focus:ring-2 focus:ring-blue-500"
                      >
                        <option value="Applied">Applied</option>
                        <option value="Interview">Interview</option>
                        <option value="Offer">Offer</option>
                        <option value="Rejected">Rejected</option>
                        <option value="Accepted">Accepted</option>
                      </select>
                    </div>
                  </div>

                  <div className="mt-6 flex justify-end gap-3">
                    <button
                        type="button"
                        onClick={() => setIsModalOpen(false)}
                        className="rounded-md border border-gray-300 px-4 py-2 text-sm font-medium hover:bg-gray-50"
                    >
                      Cancel
                    </button>
                    <button
                        type="submit"
                        className="rounded-md bg-blue-600 px-4 py-2 text-sm font-medium text-white hover:bg-blue-700"
                    >
                      Add Job
                    </button>
                  </div>
                </form>
              </div>
            </div>
        )}
      </div>
  );
};

export default JobListings;