import React, {useState, useEffect} from 'react';
import {Search, Star} from 'lucide-react';
import {api} from '../../lib/api';
import JobCard from '../../components/dashboard/JobCard';

const FavoriteJobs = () => {
    const [favoriteJobs, setFavoriteJobs] = useState([]);
    const [loading, setLoading] = useState(true);
    const [error, setError] = useState(null);
    const [updateMessage, setUpdateMessage] = useState('');
    const [searchQuery, setSearchQuery] = useState('');

    useEffect(() => {
        fetchFavoriteJobs();
    }, []);

    const fetchFavoriteJobs = async () => {
        try {
            setLoading(true);
            // use the favorites endpoint
            const jobs = await api.getFavoriteJobs();
            setFavoriteJobs(jobs);
            setError(null);
        } catch (err) {
            console.error('Error fetching favorite jobs:', err);
            setError('Failed to fetch favorite jobs');
            // use fallback logic similar to JobListings if needed
        } finally {
            setLoading(false);
        }
    };

    const handleToggleFavorite = async (job) => {
        try {
            // since we are in favorites view, this will always be "unfavorite"
            await api.toggleJobFavorite(job.id, 'unfavorite');

            // remove from list
            const updatedJobs = favoriteJobs.filter(j => j.id !== job.id);
            setFavoriteJobs(updatedJobs);

            // show notification
            setUpdateMessage('Job removed from favorites');

            // clear after 3 seconds
            setTimeout(() => {
                setUpdateMessage('');
            }, 3000);
        } catch (error) {
            console.error('Error removing from favorites:', error);
            setError('Failed to update favorite status');
        }
    };

    // filter jobs based on search query
    const filteredJobs = searchQuery ? favoriteJobs.filter(job => job.title.toLowerCase().includes(searchQuery.toLowerCase()) || job.company.toLowerCase().includes(searchQuery.toLowerCase()) || job.location.toLowerCase().includes(searchQuery.toLowerCase())) : favoriteJobs;

    return (<div className="space-y-6">
        <div className="flex items-center justify-between">
            <h1 className="text-3xl font-bold">My Favorites</h1>
            <div className="text-sm text-gray-500">
                {favoriteJobs.length} jobs favorited
            </div>
        </div>

        {/* Success Message */}
        {updateMessage && (<div className="rounded-md bg-green-50 p-4 text-green-700">
            {updateMessage}
        </div>)}

        {/* Search */}
        <div className="relative">
            <Search className="absolute left-3 top-1/2 h-4 w-4 -translate-y-1/2 text-gray-500"/>
            <input
                type="text"
                placeholder="Search favorited jobs..."
                className="w-full rounded-md border border-gray-300 pl-10 py-2 text-sm focus:outline-none focus:ring-2 focus:ring-blue-500"
                value={searchQuery}
                onChange={(e) => setSearchQuery(e.target.value)}
            />
        </div>

        {/* Error Message */}
        {error && (<div className="rounded-md bg-red-50 p-4 text-red-700">
            {error}
        </div>)}

        {/* Loading State */}
        {loading ? (<div className="text-center py-8">
            <p className="text-gray-500">Loading favorite jobs...</p>
        </div>) : (<div className="space-y-4">
            {filteredJobs.length === 0 ? (<div className="text-center py-8 border border-gray-200 rounded-lg">
                <p className="text-gray-500">No favorited jobs yet</p>
                <p className="text-sm text-gray-400 mt-2">Jobs you star will appear here</p>
            </div>) : (filteredJobs.map((job) => (<JobCard
                key={job.id}
                job={job}
                onToggleFavorite={handleToggleFavorite}
            />)))}
        </div>)}
    </div>);
};

export default FavoriteJobs;