import React, {useEffect, useState} from 'react';
import {Filter, Plus, Search} from 'lucide-react';
import {api} from '../../lib/api';
import InterviewCard from '../../components/dashboard/InterviewCard';
import InterviewModal from '../../components/dashboard/InterviewModal'

const InterviewListings = () => {
    // state for interview listings
    const [interviewListings, setInterviewListings] = useState([]);
    const [loading, setLoading] = useState(true);
    const [error, setError] = useState(null);
    const [updateMessage, setUpdateMessage] = useState('');

    // state for search and filters
    const [searchQuery, setSearchQuery] = useState('');
    const [filters, setFilters] = useState({
        format: '',
        round: '',
        date: '',
        time: '',
        company: ''
    });
    const [showFilters, setShowFilters] = useState(false);

    // state for modal
    const [isModalOpen, setIsModalOpen] = useState(false);
    const [newInterview, setNewInterview] = useState({
        format: 'virtual',
        round: 'screening',
        date: '',
        time: '',
        company: ''
    });

    // state for Edit Interview modal
    const [isEditModalOpen, setIsEditModalOpen] = useState(false);
    const [editingInterview, setEditingInterview] = useState(null);

    // fetch interviews on component mount
    useEffect(() => {
        fetchInterviews();
    }, []);

    const fetchInterviews = async () => {
        try {
            setLoading(true);
            try {
                const interviews = await api.getInterviews();
                setInterviewListings(interviews);
                setError(null);

                // save to localStorage as a cache
                localStorage.setItem('interviewListings', JSON.stringify(interviews));
            } catch (apiError) {
                console.error('Backend API error:', apiError);

                // provide a more descriptive error message depending on the error
                let errorMessage = 'Failed to connect to backend service';

                if (apiError.message.includes('Invalid JSON')) {
                    errorMessage = 'The server returned an invalid response. Please try again later.';
                } else if (apiError.message.includes('Failed to fetch')) {
                    errorMessage = 'Could not reach the interview service. Please check your connection.';
                }

                // try to use cached data from localStorage
                const localInterviews = localStorage.getItem('interviewListings');
                if (localInterviews) {
                    try {
                        const parsedInterviews = JSON.parse(localInterviews);
                        setInterviewListings(parsedInterviews);
                        setError(`${errorMessage} - showing cached data`);
                    } catch (cacheError) {
                        console.error('Error parsing cached interviews:', cacheError);
                        setInterviewListings([]);
                        setError(errorMessage);
                    }
                } else {
                    // no cached data - just show the error
                    setInterviewListings([]);
                    setError(errorMessage);
                }
            }
        } catch (err) {
            console.error('Error in interview fetching process:', err);
            setError('An unexpected error occurred');
            setInterviewListings([]);
        } finally {
            setLoading(false);
        }
    };

    // function to format a date from YYYY-MM-DD to MM/DD/YYYY
    const formatDate = (dateString) => {
        if (!dateString) return '';

        try {
            // if the date is in YYYY-MM-DD format
            if (dateString.includes('-')) {
                const [year, month, day] = dateString.split('-');
                return `${month}/${day}/${year}`;
            }

            // otherwise, try to create a Date object and format it
            const date = new Date(dateString);
            if (isNaN(date.getTime())) {
                return dateString; // Return the original if parsing fails
            }

            // format as MM/DD/YYYY
            return `${(date.getMonth() + 1).toString().padStart(2, '0')}/${date.getDate().toString().padStart(2, '0')}/${date.getFullYear()}`;
        } catch (e) {
            console.error('Error formatting date:', e);
            return dateString;
        }
    };

    // function to validate date is not in the past
    const validateDate = (dateValue) => {
        if (!dateValue) return false;

        const selectedDate = new Date(dateValue);
        const currentDate = new Date();

        // reset the time portion for fair comparison
        currentDate.setHours(0, 0, 0, 0);

        return selectedDate >= currentDate;
    };

    // search function for finding interviews by different criteria
    const searchInterviews = async () => {
        try {
            setLoading(true);
            setError(null);

            // build search params
            const params = new URLSearchParams();

            // use main search box for company search
            if (searchQuery.trim()) {
                params.append('company', searchQuery.trim());
            }

            // add filters from advanced search panel
            if (filters.format) params.append('format', filters.format);
            if (filters.round) params.append('round', filters.round);
            if (filters.date) params.append('date', filters.date);
            if (filters.time) params.append('time', filters.time);
            if (filters.company && !searchQuery.trim()) params.append('company', filters.company);

            // debug log
            const searchEndpoint = `/interviews/search?${params.toString()}`;
            console.log('searching with:', searchEndpoint);

            try {
                // call our backend api
                console.log('sending search request...');
                const interviews = await api.searchInterviews(params);
                console.log('got search response:', interviews);

                if (interviews && interviews.length > 0) {
                    setInterviewListings(interviews);
                    console.log(`found ${interviews.length} matching interviews`);
                } else {
                    setInterviewListings([]);
                    setError('No interviews found matching your search criteria');
                    console.log('no matching interviews found');
                }
            } catch (apiError) {
                console.error('search api error:', apiError);

                // show error to user
                let errorMessage = 'Failed to search interviews';
                if (apiError.message) {
                    errorMessage += ` - ${apiError.message}`;
                }

                setError(errorMessage);
            }
        } catch (err) {
            console.error('search error:', err);
            setError('Failed to search interviews - please try again later');
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
        searchInterviews();
    };

    // handle changes to filter fields
    const handleFilterChange = (e) => {
        const {name, value} = e.target;
        setFilters(prev => ({...prev, [name]: value}));
    };

    // reset all search filters
    const clearFilters = () => {
        setFilters({
            format: '',
            round: '',
            date: '',
            time: '',
            company: ''
        });
        setSearchQuery('');
        fetchInterviews(); // get all interviews without filters
    };

    const handleInputChange = (e) => {
        const {name, value} = e.target;
        setNewInterview(prev => ({...prev, [name]: value}));
    };

    const handleSubmit = async (e) => {
        e.preventDefault();
        try {
            // validate the interview date is not in the past
            if (!validateDate(newInterview.date)) {
                setError('Please select a date that is today or in the future');
                return;
            }

            // format the interview data for the API
            const interviewData = {
                format: newInterview.format,
                round: newInterview.round,
                date: newInterview.date,
                time: newInterview.time,
                company: newInterview.company
            };

            // try to call the API to create the interview
            try {
                await api.createInterview(interviewData);
                // show success message
                setUpdateMessage('Interview added successfully!');
                // refresh interviews list after successful creation
                fetchInterviews();
                // close the modal and reset the form
                setIsModalOpen(false);
                setNewInterview({
                    format: 'virtual',
                    round: 'screening',
                    date: '',
                    time: '',
                    company: ''
                });
                // clear any errors
                setError(null);
            } catch (apiError) {
                console.error('Backend API error:', apiError);

                // add interview locally when backend is unavailable
                const newInterviewWithId = {
                    ...interviewData,
                    id: Date.now(), // use timestamp as a temporary ID
                };

                const updatedInterviews = [newInterviewWithId, ...interviewListings];
                setInterviewListings(updatedInterviews);

                // save to localStorage
                localStorage.setItem('interviewListings', JSON.stringify(updatedInterviews));

                console.log('Interview added locally due to backend unavailability');
                setUpdateMessage('Interview added locally - will sync when backend is available');

                // close the modal and reset the form
                setIsModalOpen(false);
                setNewInterview({
                    format: 'virtual',
                    round: 'screening',
                    date: '',
                    time: '',
                    company: ''
                });
            }
        } catch (err) {
            console.error('Error creating interview:', err);
            setError('Failed to create interview. Please try again.');
        }
    };

    // functions for editing interviews
    const handleEditInterview = (interview) => {
        setEditingInterview({...interview});
        setIsEditModalOpen(true);
    };

    const handleEditInputChange = (e) => {
        const {name, value} = e.target;
        setEditingInterview(prev => ({...prev, [name]: value}));
    };

    const handleEditSubmit = async (e) => {
        e.preventDefault(); // this is crucial - prevents default form submission

        try {
            // validate the interview date is not in the past
            if (!validateDate(editingInterview.date)) {
                setError('Please select a date that is today or in the future');
                return;
            }

            // format the interview data for the API with correct field names
            const interviewData = {
                id: editingInterview.id,
                format: editingInterview.format,
                round: editingInterview.round,
                date: editingInterview.date,
                time: editingInterview.time,
                company: editingInterview.company,
                user_id: editingInterview.user_id || editingInterview.userId
            };

            console.log('Submitting interview update:', interviewData);

            // call the API to update the interview
            await api.updateInterview(editingInterview.id, interviewData);

            // on success - update UI
            fetchInterviews();
            setUpdateMessage('Interview updated successfully!');

            // clear any errors
            setError(null);

            // close the modal and reset form regardless of how it was submitted
            setIsEditModalOpen(false);
            setEditingInterview(null);

        } catch (err) {
            console.error('Error updating interview:', err);
            setError('Failed to update interview: ' + (err.message || 'Unknown error'));
            // don't close the modal on error so user can try again
        }
    };

    //func to delete interview
    const handleDeleteInterview = async (interviewId) => {
        // show confirmation dialog
        if (window.confirm('Are you sure you want to delete this interview? This action cannot be undone.')) {
            try {
                // call API to delete the interview
                await api.deleteInterview(interviewId);

                // show success message
                setUpdateMessage('Interview deleted successfully!');

                // clear message after 3 seconds
                setTimeout(() => {
                    setUpdateMessage('');
                }, 3000);

                // refresh the interview listings
                fetchInterviews();
            } catch (err) {
                console.error('Error deleting interview:', err);
                setError('Failed to delete interview: ' + (err.message || 'Unknown error'));
            }
        }
    };

    return (<div className="space-y-6">
        <div className="flex items-center justify-between">
            <h1 className="text-3xl font-bold">Interview Listings</h1>
            <button
                onClick={() => setIsModalOpen(true)}
                className="inline-flex items-center gap-2 rounded-md bg-blue-600 px-4 py-2 text-sm font-medium text-white hover:bg-blue-700"
            >
                <Plus className="h-4 w-4"/>
                Add Interview
            </button>
        </div>

        {/* Success Message */}
        {updateMessage && (<div className="rounded-md bg-green-50 p-4 text-green-700">
            {updateMessage}
        </div>)}

        {/* Search Form */}
        <form onSubmit={handleSearchSubmit} className="flex flex-col gap-4 md:flex-row">
            <div className="relative flex-1">
                <Search className="absolute left-3 top-1/2 h-4 w-4 -translate-y-1/2 text-gray-500"/>
                <input
                    type="text"
                    placeholder="Search for interviews..."
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

        {/*Expanded Filters */}
        {showFilters && (<div className="rounded-lg border bg-white p-4 shadow-sm">
            <div className="grid grid-cols-1 md:grid-cols-2 lg:grid-cols-3 gap-4">
                <div>
                    <label className="block text-sm font-medium text-gray-700 mb-1">
                        Format
                    </label>
                    <select
                        name="format"
                        value={filters.format}
                        onChange={handleFilterChange}
                        className="w-full rounded-md border border-gray-300 px-3 py-2 text-sm focus:outline-none focus:ring-2 focus:ring-blue-500"
                    >
                        <option value="">All Formats</option>
                        <option value="virtual">Virtual</option>
                        <option value="in-person">In-Person</option>
                        <option value="phone">Phone</option>
                    </select>
                </div>

                <div>
                    <label className="block text-sm font-medium text-gray-700 mb-1">
                        Round
                    </label>
                    <select
                        name="round"
                        value={filters.round}
                        onChange={handleFilterChange}
                        className="w-full rounded-md border border-gray-300 px-3 py-2 text-sm focus:outline-none focus:ring-2 focus:ring-blue-500"
                    >
                        <option value="">All Rounds</option>
                        <option value="screening">Screening</option>
                        <option value="technical">Technical</option>
                        <option value="hr">HR</option>
                        <option value="hiring manager">Hiring Manager</option>
                        <option value="final">Final</option>
                    </select>
                </div>

                <div>
                    <label className="block text-sm font-medium text-gray-700 mb-1">
                        Company
                    </label>
                    <input
                        type="text"
                        name="company"
                        value={filters.company}
                        onChange={handleFilterChange}
                        className="w-full rounded-md border border-gray-300 px-3 py-2 text-sm focus:outline-none focus:ring-2 focus:ring-blue-500"
                    />
                </div>

                <div>
                    <label className="block text-sm font-medium text-gray-700 mb-1">
                        Date
                    </label>
                    <input
                        type="date"
                        name="date"
                        value={filters.date}
                        onChange={handleFilterChange}
                        className="w-full rounded-md border border-gray-300 px-3 py-2 text-sm focus:outline-none focus:ring-2 focus:ring-blue-500"
                    />
                </div>

                <div>
                    <label className="block text-sm font-medium text-gray-700 mb-1">
                        Time
                    </label>
                    <input
                        type="time"
                        name="time"
                        value={filters.time}
                        onChange={handleFilterChange}
                        className="w-full rounded-md border border-gray-300 px-3 py-2 text-sm focus:outline-none focus:ring-2 focus:ring-blue-500"
                    />
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
                    onClick={searchInterviews}
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
            <p className="text-gray-500">Loading interview listings...</p>
        </div>) : (/* Interview Listings */
            <div className="space-y-4">
                {interviewListings.length === 0 ? (<div className="text-center py-8">
                    <p className="text-gray-500">No interview listings found. Try adjusting your search criteria.</p>
                </div>) : (interviewListings.map((interview) => (<InterviewCard
                    key={interview.id}
                    interview={interview}
                    onEdit={handleEditInterview}
                    onDelete={handleDeleteInterview}
                />)))}
            </div>)}

        <InterviewModal
            isOpen={isModalOpen}
            onClose={() => setIsModalOpen(false)}
            title="Add New Interview"
            interviewData={newInterview}
            handleInputChange={handleInputChange}
            handleSubmit={handleSubmit}
        />

        <InterviewModal
            isOpen={isEditModalOpen}
            onClose={() => setIsEditModalOpen(false)}
            title="Edit Interview"
            interviewData={editingInterview || {}}
            handleInputChange={handleEditInputChange}
            handleSubmit={handleEditSubmit}
        />
    </div>);
};

export default InterviewListings;