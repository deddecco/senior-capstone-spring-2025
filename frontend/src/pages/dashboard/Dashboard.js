import React, {useEffect, useState} from 'react';
import {Calendar, Star} from 'lucide-react';
import {api} from '../../lib/api';

const Dashboard = () => {
    const [stats, setStats] = useState({
        totalJobs: 0, pendingInterviews: 0, savedJobs: 0, offers: 0, recentActivity: [], // All pipeline statuses from API
        Screening: 0, Offer: 0, Saved: 0, Hired: 0, Rejected: 0, Applied: 0, Accepted: 0, Interview: 0
    });
    const [heights, setHeights] = useState({
        appliedHeight: 0, interviewHeight: 0, offerHeight: 0, rejectionHeight: 0, hiredHeight: 0
    });

    const [loading, setLoading] = useState(true);
    const [error, setError] = useState(null);

    useEffect(() => {
        fetchDashboardData();
    }, []);

    const fetchDashboardData = async () => {
        try {
            setLoading(true);

            // Fetch jobs for stats cards and recent activity
            let jobs = [];
            try {
                jobs = await api.getJobs();
            } catch (apiError) {
                const localJobs = localStorage.getItem('jobListings');
                if (localJobs) {
                    jobs = JSON.parse(localJobs);
                }
            }

            // Fetch interviews for pending interviews stat
            let interviews = [];
            try {
                interviews = await api.getInterviews();
            } catch (err) {
                // ignore, just show 0 if error
            }

            // Fetch status counts for the pipeline bars
            let statusCounts = {};
            try {
                const response = await api.get('/jobs/status-counts');
                statusCounts = response.data;
            } catch (err) {
                // ignore, just show 0s if error
            }

            // Calculate stats for cards
            const totalJobs = jobs.length;
            const pendingInterviews = interviews.length;
            // Use API-provided Saved count for Saved Jobs
            const savedJobs = statusCounts['Saved'] || 0;
            // Use API-provided Offer count for Offers
            const offers = statusCounts['Offer'] || 0;

            // Recent activity
            const recentActivity = jobs.slice(0, 3).map(job => ({
                company: job.company, position: job.title, icon: 'email'
            }));

            // For the pipeline, use only API counts (never calculate locally)
            const pipelineStats = {
                Applied: statusCounts['Applied'] || 0,
                Interview: statusCounts['Interview'] || 0,
                Offer: statusCounts['Offer'] || 0,
                Rejected: statusCounts['Rejected'] || 0,
                Hired: statusCounts['Hired'] || 0
            };

            // Calculate bar heights
            const containerHeight = 224;
            const maxStat = Math.max(pipelineStats.Applied, pipelineStats.Interview, pipelineStats.Offer, pipelineStats.Rejected, pipelineStats.Hired) || 1;

            setStats({
                totalJobs,
                pendingInterviews,
                savedJobs,
                offers,
                recentActivity,
                Screening: statusCounts['Screening'] || 0,
                Offer: statusCounts['Offer'] || 0,
                Saved: statusCounts['Saved'] || 0,
                Hired: statusCounts['Hired'] || 0,
                Rejected: statusCounts['Rejected'] || 0,
                Applied: statusCounts['Applied'] || 0,
                Accepted: statusCounts['Accepted'] || 0,
                Interview: statusCounts['Interview'] || 0
            });

            setHeights({
                appliedHeight: (pipelineStats.Applied / maxStat) * containerHeight,
                interviewHeight: (pipelineStats.Interview / maxStat) * containerHeight,
                offerHeight: (pipelineStats.Offer / maxStat) * containerHeight,
                rejectionHeight: (pipelineStats.Rejected / maxStat) * containerHeight,
                hiredHeight: (pipelineStats.Hired / maxStat) * containerHeight
            });

            setError(null);
        } catch (err) {
            setError('Failed to fetch dashboard data');
            setStats({
                totalJobs: 0,
                pendingInterviews: 0,
                savedJobs: 0,
                offers: 0,
                recentActivity: [],
                Screening: 0,
                Offer: 0,
                Saved: 0,
                Hired: 0,
                Rejected: 0,
                Applied: 0,
                Accepted: 0,
                Interview: 0
            });
            setHeights({
                appliedHeight: 0, interviewHeight: 0, offerHeight: 0, rejectionHeight: 0, hiredHeight: 0
            });
        } finally {
            setLoading(false);
        }
    };

    if (loading) {
        return (<div className="text-center py-8">
            <p className="text-gray-500">Loading dashboard data...</p>
        </div>);
    }

    return (<div className="space-y-6">
        {error && (<div className="rounded-md bg-red-50 p-4 text-red-700 mb-4">
            {error}
        </div>)}

        <div className="flex items-center justify-between">
            <h1 className="text-3xl font-bold">Dashboard</h1>
        </div>

        {/* Stats Cards */}
        <div className="grid grid-cols-1 gap-4 md:grid-cols-3">
            <div className="rounded-lg border bg-white p-6 shadow-sm">
                <div className="flex items-center gap-4">
                    <div className="rounded-full bg-blue-100 p-3">
                        <div className="h-6 w-6 text-blue-500">
                            <svg xmlns="http://www.w3.org/2000/svg" width="24" height="24" viewBox="0 0 24 24"
                                 fill="none" stroke="currentColor" strokeWidth="2" strokeLinecap="round"
                                 strokeLinejoin="round" className="lucide lucide-briefcase">
                                <rect width="20" height="14" x="2" y="7" rx="2" ry="2"/>
                                <path d="M16 21V5a2 2 0 0 0-2-2h-4a2 2 0 0 0-2 2v16"/>
                            </svg>
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
                            <Calendar className="h-6 w-6"/>
                        </div>
                    </div>
                    <div>
                        <p className="text-sm text-gray-500">Pending Interviews</p>
                        <h3 className="text-3xl font-bold">{stats.pendingInterviews}</h3>
                    </div>
                </div>
            </div>

            <div className="rounded-lg border bg-white p-6 shadow-sm">
                <div className="flex items-center gap-4">
                    <div className="rounded-full bg-yellow-100 p-3">
                        <div className="h-6 w-6 text-yellow-500">
                            <Star className="h-6 w-6"/>
                        </div>
                    </div>
                    <div>
                        <p className="text-sm text-gray-500">Saved Jobs</p>
                        <h3 className="text-3xl font-bold">{stats.savedJobs}</h3>
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
                        <span className="mb-1 text-sm font-semibold text-gray-700">{stats.Applied}</span>
                        <div className="bg-blue-500 w-12" style={{height: heights.appliedHeight}}></div>
                        <span className="text-xs mt-2">Applied</span>
                    </div>
                    <div className="flex flex-col items-center">
                        <span className="mb-1 text-sm font-semibold text-gray-700">{stats.Interview}</span>
                        <div className="bg-blue-500 w-12" style={{height: heights.interviewHeight}}></div>
                        <span className="text-xs mt-2">Interview</span>
                    </div>
                    <div className="flex flex-col items-center">
                        <span className="mb-1 text-sm font-semibold text-gray-700">{stats.Offer}</span>
                        <div className="bg-blue-500 w-12" style={{height: heights.offerHeight}}></div>
                        <span className="text-xs mt-2">Offer</span>
                    </div>
                    <div className="flex flex-col items-center">
                        <span className="mb-1 text-sm font-semibold text-gray-700">{stats.Rejected}</span>
                        <div className="bg-blue-500 w-12" style={{height: heights.rejectionHeight}}></div>
                        <span className="text-xs mt-2">Rejected</span>
                    </div>
                    <div className="flex flex-col items-center">
                        <span className="mb-1 text-sm font-semibold text-gray-700">{stats.Hired}</span>
                        <div className="bg-blue-500 w-12" style={{height: heights.hiredHeight}}></div>
                        <span className="text-xs mt-2">Hired</span>
                    </div>
                </div>
            </div>

            {/* Recent Activity */}
            <div className="rounded-lg border bg-white p-6 shadow-sm">
                <h3 className="text-lg font-medium mb-4">Recent Activity</h3>
                <div className="space-y-4">
                    {stats.recentActivity.map((activity, index) => (<div key={index} className="flex items-start gap-4">
                        <div
                            className={`rounded-full p-2 ${activity.icon === 'email' ? 'bg-blue-100 text-blue-500' : activity.icon === 'calendar' ? 'bg-purple-100 text-purple-500' : 'bg-yellow-100 text-yellow-500'}`}>
                            {activity.icon === 'email' && (
                                <svg xmlns="http://www.w3.org/2000/svg" width="20" height="20"
                                     viewBox="0 0 24 24" fill="none" stroke="currentColor" strokeWidth="2"
                                     strokeLinecap="round" strokeLinejoin="round"
                                     className="lucide lucide-mail">
                                    <rect width="20" height="16" x="2" y="4" rx="2"/>
                                    <path d="m22 7-8.97 5.7a1.94 1.94 0 0 1-2.06 0L2 7"/>
                                </svg>)}
                            {activity.icon === 'calendar' && (<Calendar className="h-5 w-5"/>)}
                            {activity.icon === 'star' && (<Star className="h-5 w-5"/>)}
                        </div>
                        <div>
                            <h4 className="font-medium">{activity.company}</h4>
                            <p className="text-sm text-gray-500">{activity.position}</p>
                        </div>
                    </div>))}
                </div>
            </div>
        </div>

        {/* Upcoming Interviews */}
        <div className="rounded-lg border bg-white p-6 shadow-sm">
            <h3 className="text-lg font-medium mb-4">Upcoming Interviews</h3>
            <div className="text-center py-8 text-gray-500">
                No upcoming interviews scheduled
            </div>
        </div>
    </div>);
};

export default Dashboard;