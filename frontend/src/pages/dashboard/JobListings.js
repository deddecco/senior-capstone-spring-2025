import React, {useEffect, useState} from 'react';
import {Filter, Plus, Search} from 'lucide-react';
import {api} from '../../lib/api';
import JobCard from '../../components/dashboard/JobCard';
import JobModal from '../../components/dashboard/JobModal'

const JobListings = () => {
    // State for job listings
    const [jobListings, setJobListings] = useState([]);
    const [loading, setLoading] = useState(true);
    const [error, setError] = useState(null);
    const [updateMessage, setUpdateMessage] = useState('');

    // State for search and filters
    const [searchQuery, setSearchQuery] = useState('');
    const [filters, setFilters] = useState({
        title: '', level: '', minSalary: '', maxSalary: '', location: '', status: ''
    });
    const [showFilters, setShowFilters] = useState(false);

    // State for modal
    const [isModalOpen, setIsModalOpen] = useState(false);
    const [newJob, setNewJob] = useState({
        title: '', company: '', location: '', minSalary: '', maxSalary: '', type: 'Full-time', status: 'Applied'
    });

    // state for Edit Job modal
    const [isEditModalOpen, setIsEditModalOpen] = useState(false);
    const [editingJob, setEditingJob] = useState(null);

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

                // save to localStorage as a cache
                localStorage.setItem('jobListings', JSON.stringify(jobs));
            } catch (apiError) {
                console.error('Backend API error:', apiError);

                // provide a more descriptive error message depending on the error
                let errorMessage = 'Failed to connect to backend service';

                if (apiError.message.includes('Invalid JSON')) {
                    errorMessage = 'The server returned an invalid response. Please try again later.';
                } else if (apiError.message.includes('Failed to fetch')) {
                    errorMessage = 'Could not reach the job service. Please check your connection.';
                }

                // try to use cached data from localStorage
                const localJobs = localStorage.getItem('jobListings');
                if (localJobs) {
                    try {
                        const parsedJobs = JSON.parse(localJobs);
                        setJobListings(parsedJobs);
                        setError(`${errorMessage} - showing cached data`);
                    } catch (cacheError) {
                        console.error('Error parsing cached jobs:', cacheError);
                        setJobListings([]);
                        setError(errorMessage);
                    }
                } else {
                    // no cached data - just show the error
                    setJobListings([]);
                    setError(errorMessage);
                }
            }
        } catch (err) {
            console.error('Error in job fetching process:', err);
            setError('An unexpected error occurred');
            setJobListings([]);
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
        const {name, value} = e.target;
        setFilters(prev => ({...prev, [name]: value}));
    };

// reset all search filters
    const clearFilters = () => {
        setFilters({
            title: '', level: '', minSalary: '', maxSalary: '', location: '', status: ''
        });
        setSearchQuery('');
        fetchJobs(); // get all jobs without filters
    };

    const handleInputChange = (e) => {
        const {name, value} = e.target;
        setNewJob(prev => ({...prev, [name]: value}));
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
                company: newJob.company
            };

            // Try to call the API to create the job
            try {
                await api.createJob(jobData);
                // Show success message
                setUpdateMessage('Job added successfully!');
                // Refresh jobs list after successful creation
                fetchJobs();
                // close the modal and reset the form
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
            } catch (apiError) {
                console.error('Backend API error:', apiError);

                // add job locally when backend is unavailable
                const newJobWithId = {
                    ...jobData, company: newJob.company, // Keep company for local display
                    id: Date.now(), // Use timestamp as a temporary ID
                    posted: 'Just now', description: 'Added locally (backend unavailable)'
                };

                const updatedJobs = [newJobWithId, ...jobListings];
                setJobListings(updatedJobs);

                // Save to localStorage
                localStorage.setItem('jobListings', JSON.stringify(updatedJobs));

                console.log('Job added locally due to backend unavailability');
                setUpdateMessage('Job added locally - will sync when backend is available');

                // close the modal and reset the form
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
            }
        } catch (err) {
            console.error('Error creating job:', err);
            setError('Failed to create job. Please try again.');
        }
    };

    // functions for editing jobs
    const handleEditJob = (job) => {
        setEditingJob({...job});
        setIsEditModalOpen(true);
    };

    const handleEditInputChange = (e) => {
        const {name, value} = e.target;
        setEditingJob(prev => ({...prev, [name]: value}));
    };

    const handleEditSubmit = async (e) => {
        e.preventDefault(); // this is crucial - prevents default form submission

        try {
            // format the job data for the API with correct field names
            const jobData = {
                id: editingJob.id,
                title: editingJob.title,
                level: editingJob.level,
                minSalary: parseInt(editingJob.minSalary) || 0,
                maxSalary: parseInt(editingJob.maxSalary) || 0,
                location: editingJob.location,
                status: editingJob.status,
                company: editingJob.company,
                userId: editingJob.userId || editingJob.user_id,
                favorite: editingJob.favorite
            };

            console.log('Submitting job update:', jobData);

            // call the API to update the job
            await api.updateJob(editingJob.id, jobData);

            // on success - update UI
            fetchJobs();
            setUpdateMessage('Job updated successfully!');

            // close the modal and reset form regardless of how it was submitted
            setIsEditModalOpen(false);
            setEditingJob(null);

        } catch (err) {
            console.error('Error updating job:', err);
            setError('Failed to update job: ' + (err.message || 'Unknown error'));
            // don't close the modal on error so user can try again
        }
    };

    //func to delete job
    const handleDeleteJob = async (jobId) => {
        // Show confirmation dialog
        if (window.confirm('Are you sure you want to delete this job? This action cannot be undone.')) {
            try {
                // call API to delete the job
                await api.deleteJob(jobId);

                // show success message
                setUpdateMessage('Job deleted successfully!');

                // clear message after 3 seconds
                setTimeout(() => {
                    setUpdateMessage('');
                }, 3000);

                // refresh the job listings
                fetchJobs();
            } catch (err) {
                console.error('Error deleting job:', err);
                setError('Failed to delete job: ' + (err.message || 'Unknown error'));
            }
        }
    };

    const handleToggleFavorite = async (job) => {
        try {
            const endpoint = job.favorite ? 'unfavorite' : 'favorite';
            await api.toggleJobFavorite(job.id, endpoint);

            // update the job in the list
            const updatedJobs = jobListings.map(j => j.id === job.id ? {...j, favorite: !j.favorite} : j);
            setJobListings(updatedJobs);

            // show notification
            setUpdateMessage(`Job ${job.favorite ? 'removed from' : 'added to'} favorites`);

            // clear notification after 3 seconds
            setTimeout(() => {
                setUpdateMessage('');
            }, 3000);

        } catch (error) {
            console.error('Error toggling favorite:', error);
            setError('Failed to update favorite status');
        }
    };

    return (<div className="space-y-6">
        <div className="flex items-center justify-between">
            <h1 className="text-3xl font-bold">Job Listings</h1>
            <button
                onClick={() => setIsModalOpen(true)}
                className="inline-flex items-center gap-2 rounded-md bg-blue-600 px-4 py-2 text-sm font-medium text-white hover:bg-blue-700"
            >
                <Plus className="h-4 w-4"/>
                Add Job
            </button>
        </div>

        {/* success Message */}
        {updateMessage && (<div className="rounded-md bg-green-50 p-4 text-green-700">
            {updateMessage}
        </div>)}

        {/* Search Form */}
        <form onSubmit={handleSearchSubmit} className="flex flex-col gap-4 md:flex-row">
            <div className="relative flex-1">
                <Search className="absolute left-3 top-1/2 h-4 w-4 -translate-y-1/2 text-gray-500"/>
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
                <Filter className="h-4 w-4"/>
                {showFilters ? 'Hide Filters' : 'Show Filters'}
            </button>
        </form>

        {/* NEW: Expanded Filters */}
        {showFilters && (<div className="rounded-lg border bg-white p-4 shadow-sm">
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
                        Job Level
                    </label>
                    <select
                        name="level"
                        value={filters.level}
                        onChange={handleFilterChange}
                        className="w-full rounded-md border border-gray-300 px-3 py-2 text-sm focus:outline-none focus:ring-2 focus:ring-blue-500"
                    >
                        <option value="">All Levels</option>
                        <option value="Internship">Internship</option>
                        <option value="Entry Level">Entry Level</option>
                        <option value="Mid-Level">Mid-Level</option>
                        <option value="Senior">Senior</option>
                        <option value="Manager">Manager</option>
                        <option value="Director">Director</option>
                        <option value="Executive">Executive</option>
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
                        <option value="Saved">Saved</option>
                        <option value="Applied">Applied</option>
                        <option value="Interview">Interview</option>
                        <option value="Offer">Offer</option>
                        <option value="Rejected">Rejected</option>
                        <option value="Hired">Hired</option>
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
        </div>)}

        {/* Error Message */}
        {error && (<div className="rounded-md bg-red-50 p-4 text-red-700">
            {error}
        </div>)}

        {/* Loading State */}
        {loading ? (<div className="text-center py-8">
            <p className="text-gray-500">Loading job listings...</p>
        </div>) : (/* Job Listings */
            <div className="space-y-4">
                {jobListings.length === 0 ? (<div className="text-center py-8">
                    <p className="text-gray-500">No job listings found. Try adjusting your search criteria.</p>
                </div>) : (jobListings.map((job) => (<JobCard
                    key={job.id}
                    job={job}
                    onEdit={handleEditJob}
                    onDelete={handleDeleteJob}
                    onToggleFavorite={handleToggleFavorite}
                />)))}
            </div>)}

        <JobModal
            isOpen={isModalOpen}
            onClose={() => setIsModalOpen(false)}
            title="Add New Job"
            jobData={newJob}
            handleInputChange={handleInputChange}
            handleSubmit={handleSubmit}
        />

        <JobModal
            isOpen={isEditModalOpen}
            onClose={() => setIsEditModalOpen(false)}
            title="Edit Job"
            jobData={editingJob || {}}
            handleInputChange={handleEditInputChange}
            handleSubmit={handleEditSubmit}
        />
    </div>);
};

export default JobListings;