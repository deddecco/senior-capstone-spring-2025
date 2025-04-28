import React, { useEffect, useState } from 'react';
import { Calendar, Star, Briefcase, Mail } from 'lucide-react';
import { api } from '../../lib/api';

const Dashboard = () => {
    const [stats, setStats] = useState({
        totalJobs: 0, pendingInterviews: 0, savedJobs: 0, appliedJobs: 0, offers: 0, rejections: 0, acceptances: 0, favoriteCount: 0
    });
    const [heights, setHeights] = useState({
        appliedHeight: 0, interviewHeight: 0, offerHeight: 0, rejectionHeight: 0, acceptedHeight: 0
    });
    const [recentActivity, setRecentActivity] = useState([]);
    const [upcomingInterviews, setUpcomingInterviews] = useState([]);
    const [loading, setLoading] = useState(true);
    const [error, setError] = useState(null);

    useEffect(() => {
        fetchDashboardData();
    }, []);

    const fetchDashboardData = async () => {
        try {
            setLoading(true);

            // Fetch jobs status counts
            let statusCounts = {};
            try {
                statusCounts = await api.getStatusCounts();
            } catch (apiError) {
                console.error('Backend API error when fetching status counts:', apiError);
                // If API fails, try to calculate from jobs
                const localJobs = localStorage.getItem('jobListings');
                if (localJobs) {
                    const jobs = JSON.parse(localJobs);
                    statusCounts = calculateStatusCounts(jobs);
                }
            }

            // Fetch all jobs for recent activity
            let jobs = [];
            try {
                jobs = await api.getJobs();
            } catch (apiError) {
                console.error('Backend API error when fetching jobs:', apiError);
                // Try to get jobs from localStorage
                const localJobs = localStorage.getItem('jobListings');
                if (localJobs) {
                    jobs = JSON.parse(localJobs);
                }
            }

            // Try to fetch favorite jobs to get an accurate count
            let favoriteJobs = [];
            try {
                favoriteJobs = await api.getFavoriteJobs();
            } catch (apiError) {
                console.error('Backend API error when fetching favorite jobs:', apiError);
                // Calculate from jobs we already have
                favoriteJobs = jobs.filter(job => job.favorite);
            }

            // Fetch upcoming interviews - try to use the dedicated endpoint
            let interviews = [];
            let upcomingList = [];
            try {
                // First try to get all interviews
                interviews = await api.getInterviews();

                // Then try to get upcoming interviews directly
                try {
                    // Check if the getUpcomingInterviews method exists in the API
                    if (typeof api.getUpcomingInterviews === 'function') {
                        upcomingList = await api.getUpcomingInterviews();
                    } else {
                        // Filter for upcoming interviews (date is in the future)
                        const today = new Date();
                        today.setHours(0, 0, 0, 0); // Set to beginning of today

                        upcomingList = interviews.filter(interview => {
                            if (!interview.date) return false;
                            const interviewDate = new Date(interview.date);
                            return interviewDate >= today;
                        });
                    }
                } catch (err) {
                    console.error('Error fetching upcoming interviews, filtering manually:', err);
                    // Filter for upcoming interviews (date is in the future)
                    const today = new Date();
                    today.setHours(0, 0, 0, 0); // Set to beginning of today

                    upcomingList = interviews.filter(interview => {
                        if (!interview.date) return false;
                        const interviewDate = new Date(interview.date);
                        return interviewDate >= today;
                    });
                }

                setUpcomingInterviews(upcomingList);
            } catch (apiError) {
                console.error('Backend API error when fetching interviews:', apiError);
                // Try to get interviews from localStorage
                const localInterviews = localStorage.getItem('interviewListings');
                if (localInterviews) {
                    interviews = JSON.parse(localInterviews);

                    // Filter for upcoming
                    const today = new Date();
                    today.setHours(0, 0, 0, 0);

                    upcomingList = interviews.filter(interview => {
                        if (!interview.date) return false;
                        const interviewDate = new Date(interview.date);
                        return interviewDate >= today;
                    });

                    setUpcomingInterviews(upcomingList);
                }
            }

            // Calculate stats from status counts and jobs
            const totalJobs = jobs.length || 0;
            const savedJobs = statusCounts.Saved || 0;
            const appliedJobs = statusCounts.Applied || 0;
            const pendingInterviews = statusCounts.Interview || 0;
            const offers = statusCounts.Offer || 0;
            const rejections = statusCounts.Rejected || 0;
            const acceptances = statusCounts.Accepted || 0;
            const favoriteCount = favoriteJobs.length || 0;

            // Update stats state
            const newStats = {
                totalJobs, pendingInterviews, savedJobs, appliedJobs, offers, rejections, acceptances, favoriteCount
            };
            setStats(newStats);

            // Calculate visualization heights
            const containerHeight = 224; // px
            const maxStat = Math.max(
                newStats.appliedJobs,
                newStats.pendingInterviews,
                newStats.offers,
                newStats.rejections,
                newStats.acceptances
            ) || 1; // prevent division by zero

            setHeights({
                appliedHeight: (newStats.appliedJobs / maxStat) * containerHeight,
                interviewHeight: (newStats.pendingInterviews / maxStat) * containerHeight,
                offerHeight: (newStats.offers / maxStat) * containerHeight,
                rejectionHeight: (newStats.rejections / maxStat) * containerHeight,
                acceptedHeight: (newStats.acceptances / maxStat) * containerHeight
            });

            // Create recent activity from both jobs and interviews
            const recentItems = createRecentActivityItems(jobs, interviews);
            setRecentActivity(recentItems);

            setError(null);
        } catch (err) {
            console.error('Error fetching dashboard data:', err);
            setError('Failed to fetch dashboard data');

            setStats({
                totalJobs: 0, pendingInterviews: 0, savedJobs: 0, appliedJobs: 0, offers: 0, rejections: 0, acceptances: 0, favoriteCount: 0
            });

            setRecentActivity([]);
            setUpcomingInterviews([]);
        } finally {
            setLoading(false);
        }
    };

    // Calculate status counts from jobs array
    const calculateStatusCounts = (jobs) => {
        const counts = {};
        jobs.forEach(job => {
            if (job.status) {
                counts[job.status] = (counts[job.status] || 0) + 1;
            }
        });
        return counts;
    };

    // Create recent activity items from jobs and interviews
    const createRecentActivityItems = (jobs, interviews) => {
        // Sort jobs by lastModified date (desc)
        const sortedJobs = [...jobs].sort((a, b) => {
            const dateA = a.lastModified ? new Date(a.lastModified) : new Date(0);
            const dateB = b.lastModified ? new Date(b.lastModified) : new Date(0);
            return dateB - dateA;
        });

        // Sort interviews by most recent first (if they have a date field)
        const sortedInterviews = [...interviews].sort((a, b) => {
            const dateA = a.date ? new Date(a.date) : new Date(0);
            const dateB = b.date ? new Date(b.date) : new Date(0);
            return dateB - dateA;
        });

        // Combine jobs and interviews into a single activity feed
        const recentJobItems = sortedJobs.slice(0, 3).map(job => ({
            id: job.id,
            type: 'job',
            company: job.company || 'Unknown Company',
            position: job.title || 'Job Position',
            time: formatTimeAgo(job.lastModified),
            icon: job.favorite ? 'star' : 'briefcase',
            status: job.status
        }));

        const recentInterviewItems = sortedInterviews.slice(0, 3).map(interview => ({
            id: interview.id,
            type: 'interview',
            company: interview.company || 'Unknown Company',
            position: `${interview.format || ''} ${interview.round || 'Interview'}`,
            time: interview.date ? `${formatDateDisplay(interview.date)} at ${interview.time}` : 'Scheduled',
            icon: 'calendar'
        }));

        // Combine and sort by most recent
        const combined = [...recentJobItems, ...recentInterviewItems]
            .sort((a, b) => {
                // If we have actual timestamps, use those
                if (a.timestamp && b.timestamp) {
                    return b.timestamp - a.timestamp;
                }
                // Otherwise just interleave
                return 0;
            })
            .slice(0, 5); // Show top 5 most recent

        return combined;
    };

    // Format time ago for recent activities
    const formatTimeAgo = (timestamp) => {
        if (!timestamp) return 'Recently';

        const now = new Date();
        const time = new Date(timestamp);
        const diff = now - time;

        // Convert to appropriate unit
        const minutes = Math.floor(diff / 60000);
        const hours = Math.floor(diff / 3600000);
        const days = Math.floor(diff / 86400000);

        if (minutes < 60) {
            return minutes <= 1 ? 'Just now' : `${minutes} minutes ago`;
        } else if (hours < 24) {
            return hours === 1 ? '1 hour ago' : `${hours} hours ago`;
        } else if (days < 30) {
            return days === 1 ? 'Yesterday' : `${days} days ago`;
        } else {
            return time.toLocaleDateString();
        }
    };

    const formatDateDisplay = (dateString) => {
        if (!dateString) return '';

        try {
            if (dateString.includes('/')) return dateString;

            const parts = dateString.split('-');
            if (parts.length === 3) {
                return `${parts[1]}-${parts[2]}-${parts[0]}`;
            }

            return dateString;
        } catch (e) {
            console.error('Error formatting date:', e);
            return dateString;
        }
    };

    if (loading) {
        return (
            <div className="text-center py-8">
                <p className="text-gray-500">Loading dashboard data...</p>
            </div>
        );
    }

    return (
        <div className="space-y-6">
            {error && (
                <div className="rounded-md bg-red-50 p-4 text-red-700 mb-4">
                    {error}
                </div>
            )}

            {/* Stats Cards */}
            <div className="grid grid-cols-1 gap-4 md:grid-cols-3">
                <div className="rounded-lg border bg-white p-6 shadow-sm">
                    <div className="flex items-center gap-4">
                        <div className="rounded-full bg-blue-100 p-3">
                            <div className="h-6 w-6 text-blue-500">
                                <Briefcase className="h-6 w-6" />
                            </div>
                        </div>
                        <div>
                            <p className="text-sm text-gray-500">Total Jobs</p>
                            <h3 className="text-3xl font-bold">{stats.totalJobs}</h3>
                        </div>
                    </div>
                </div>

                <div className="rounded-lg border bg-white p-6 shadow-sm">
                    <div className="flex items-center gap-4">
                        <div className="rounded-full bg-purple-100 p-3">
                            <div className="h-6 w-6 text-purple-500">
                                <Calendar className="h-6 w-6" />
                            </div>
                        </div>
                        <div>
                            <p className="text-sm text-gray-500">Pending Interviews</p>
                            <h3 className="text-3xl font-bold">{upcomingInterviews.length}</h3>
                        </div>
                    </div>
                </div>

                <div className="rounded-lg border bg-white p-6 shadow-sm">
                    <div className="flex items-center gap-4">
                        <div className="rounded-full bg-yellow-100 p-3">
                            <div className="h-6 w-6 text-yellow-500">
                                <Star className="h-6 w-6" />
                            </div>
                        </div>
                        <div>
                            <p className="text-sm text-gray-500">Favorite Jobs</p>
                            <h3 className="text-3xl font-bold">{stats.favoriteCount}</h3>
                        </div>
                    </div>
                </div>
            </div>

            {/* Application Pipeline and Recent Activity */}
            <div className="grid grid-cols-1 gap-6 md:grid-cols-2">
                {/* Application Pipeline */}
                <div className="rounded-lg border bg-white p-6 shadow-sm">
                    <h3 className="text-lg font-medium mb-4">Application Pipeline</h3>
                    <div className="h-64 flex items-end justify-between gap-2">
                        <div className="flex flex-col items-center">
                            <span className="mb-1 text-sm font-semibold text-gray-700">{stats.appliedJobs}</span>
                            <div className="bg-blue-500 w-12" style={{ height: heights.appliedHeight }}></div>
                            <span className="text-xs mt-2">Applied</span>
                        </div>
                        <div className="flex flex-col items-center">
                            <span className="mb-1 text-sm font-semibold text-gray-700">{stats.pendingInterviews}</span>
                            <div className="bg-purple-500 w-12" style={{ height: heights.interviewHeight }}></div>
                            <span className="text-xs mt-2">Interview</span>
                        </div>
                        <div className="flex flex-col items-center">
                            <span className="mb-1 text-sm font-semibold text-gray-700">{stats.offers}</span>
                            <div className="bg-green-500 w-12" style={{ height: heights.offerHeight }}></div>
                            <span className="text-xs mt-2">Offer</span>
                        </div>
                        <div className="flex flex-col items-center">
                            <span className="mb-1 text-sm font-semibold text-gray-700">{stats.rejections}</span>
                            <div className="bg-red-500 w-12" style={{ height: heights.rejectionHeight }}></div>
                            <span className="text-xs mt-2">Rejected</span>
                        </div>
                        <div className="flex flex-col items-center">
                            <span className="mb-1 text-sm font-semibold text-gray-700">{stats.acceptances}</span>
                            <div className="bg-teal-500 w-12" style={{ height: heights.acceptedHeight }}></div>
                            <span className="text-xs mt-2">Accepted</span>
                        </div>
                    </div>
                </div>

                {/* Recent Activity */}
                <div className="rounded-lg border bg-white p-6 shadow-sm">
                    <h3 className="text-lg font-medium mb-4">Recent Activity</h3>
                    <div className="h-64 overflow-y-auto pr-2">
                        <div className="space-y-4">
                            {recentActivity.length > 0 ? (
                                recentActivity.map((activity, index) => (
                                    <div key={activity.id || index} className="flex items-start gap-4">
                                        <div
                                            className={`rounded-full p-2 ${
                                                activity.icon === 'briefcase'
                                                    ? 'bg-blue-100 text-blue-500'
                                                    : activity.icon === 'calendar'
                                                        ? 'bg-purple-100 text-purple-500'
                                                        : activity.icon === 'star'
                                                            ? 'bg-yellow-100 text-yellow-500'
                                                            : 'bg-gray-100 text-gray-500'
                                            }`}
                                        >
                                            {activity.icon === 'briefcase' && <Briefcase className="h-5 w-5" />}
                                            {activity.icon === 'calendar' && <Calendar className="h-5 w-5" />}
                                            {activity.icon === 'star' && <Star className="h-5 w-5" />}
                                            {activity.icon === 'email' && <Mail className="h-5 w-5" />}
                                        </div>
                                        <div>
                                            <h4 className="font-medium">{activity.company}</h4>
                                            <p className="text-sm text-gray-500">{activity.position}</p>
                                            {activity.status && (
                                                <span className="inline-flex items-center rounded-full bg-blue-100 px-2.5 py-0.5 text-xs font-medium text-blue-800 mt-1">
                                                    {activity.status}
                                                </span>
                                            )}
                                            <p className="text-xs text-gray-400 mt-1">{activity.time}</p>
                                        </div>
                                    </div>
                                ))
                            ) : (
                                <div className="text-center py-8 text-gray-500">
                                    <p>No recent activity to display</p>
                                </div>
                            )}
                        </div>
                    </div>
                </div>
            </div>

            {/* Upcoming Interviews */}
            <div className="rounded-lg border bg-white p-6 shadow-sm">
                <h3 className="text-lg font-medium mb-4">Upcoming Interviews</h3>
                {upcomingInterviews.length > 0 ? (
                    <div className="max-h-80 overflow-y-auto pr-2">
                        <div className="space-y-4">
                            {upcomingInterviews.map((interview) => (
                                <div key={interview.id} className="border-b pb-3 last:border-0">
                                    <div className="flex items-start gap-4">
                                        <div className="rounded-full bg-purple-100 p-2">
                                            <Calendar className="h-5 w-5 text-purple-500" />
                                        </div>
                                        <div>
                                            <h4 className="font-medium">{interview.company || "Company"}</h4>
                                            <p className="text-sm text-gray-500">
                                                {interview.round ? (interview.round.charAt(0).toUpperCase() + interview.round.slice(1)) : "Interview"} •
                                                {interview.format ? (" " + interview.format.charAt(0).toUpperCase() + interview.format.slice(1)) : ""}
                                            </p>
                                            <div className="mt-1 flex flex-wrap gap-2">
                                                <span className="inline-flex items-center rounded-full bg-blue-100 px-2.5 py-0.5 text-xs font-medium text-blue-800">
                                                    {formatDateDisplay(interview.date)} at {interview.time}
                                                </span>
                                            </div>
                                        </div>
                                    </div>
                                </div>
                            ))}
                        </div>
                    </div>
                ) : (
                    <div className="text-center py-8 text-gray-500">
                        <p>No upcoming interviews scheduled</p>
                    </div>
                )}
            </div>
        </div>
    );
};

export default Dashboard;