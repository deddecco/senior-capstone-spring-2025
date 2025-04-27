import React, {useEffect, useState} from 'react';
import {Calendar, Star} from 'lucide-react';
import {api} from '../../lib/api';

const Dashboard = () => {
    const [stats, setStats] = useState({
        totalJobs: 0, pendingInterviews: 0, savedJobs: 0, appliedJobs: 0, offers: 0, rejections: 0, acceptances: 0
    });
    const [heights, setHeights] = useState({
        appliedHeight: 0, interviewHeight: 0, offerHeight: 0, rejectionHeight: 0, acceptedHeight: 0
    });

    const [recentActivity, setRecentActivity] = useState([]);

    const [loading, setLoading] = useState(true);
    const [error, setError] = useState(null);

    useEffect(() => {
        fetchDashboardData();
    }, []);

    const fetchDashboardData = async () => {
        try {
            setLoading(true);

            // Fetch jobs
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

            // Fetch interviews
            let interviews = [];
            try {
                interviews = await api.getInterviews();
            } catch (err) {
                console.error('Error fetching interviews:', err);
            }

            // Calculate stats
            const totalJobs = jobs.length;
            const pendingInterviews = interviews.length;
            const savedJobs = jobs.filter(job => job.status === 'Saved').length;
            const offers = jobs.filter(job => job.status === 'Offer').length;
            const appliedJobs = jobs.filter(job => job.status === 'Applied').length;
            const rejected = jobs.filter(job => job.status === 'Rejected').length;
            const accepted = jobs.filter(job => job.status === 'Accepted').length;

            setStats({
                totalJobs, pendingInterviews, savedJobs, appliedJobs, offers, rejected, accepted
            });

            const containerHeight = 256; // px
            const maxStat = Math.max(stats.appliedJobs, stats.pendingInterviews, stats.offers, stats.rejections, stats.acceptances) || 1; // prevent division by zero

            setHeights({
                appliedHeight: (stats.appliedJobs / maxStat) * containerHeight,
                interviewHeight: (stats.pendingInterviews / maxStat) * containerHeight,
                offerHeight: (stats.offers / maxStat) * containerHeight,
                rejectionHeight: (stats.rejections / maxStat) * containerHeight,
                acceptedHeight: (stats.acceptances / maxStat) * containerHeight
            });
            // Create recent activity from jobs and interviews
            const recentJobs = jobs.slice(0, 3).map(job => ({
                company: job.company, position: job.title, // time: job.created_at ? new Date(job.created_at).toLocaleDateString() : job.posted || '2 hours ago',
                icon: 'email'
            }));

            if (recentJobs.length > 0) {
                setRecentActivity(recentJobs);
            }

            setError(null);
        } catch (err) {
            console.error('Error fetching dashboard data:', err);
            setError('Failed to fetch dashboard data');

            setStats({
                totalJobs: 0, pendingInterviews: 0, savedJobs: 0, appliedJobs: 0, offers: 0, rejected: 0, accepted: 0
            });

            setRecentActivity([]);
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


        {/*fixme heights are not scaling correctly*/}
        {/*this should come from the status-counts endpoint, not be hardcoded*/}
        {/* Application Pipeline and Recent Activity */}
        <div className="grid grid-cols-1 gap-6 md:grid-cols-2">
            {/* Application Pipeline */}
            <div className="rounded-lg border bg-white p-6 shadow-sm">
                <h3 className="text-lg font-medium mb-4">Application Pipeline</h3>
                <div className="h-64 flex items-end justify-between gap-2">
                    <div className="flex flex-col items-center">
                        <div className="bg-blue-500 w-12" style={{height: heights.appliedHeight}}></div>
                        <span className="text-xs mt-2">Applied</span>
                    </div>
                    <div className="flex flex-col items-center">
                        <div className="bg-blue-500 w-12" style={{height: heights.interviewHeight}}></div>
                        <span className="text-xs mt-2">Interview</span>
                    </div>
                    <div className="flex flex-col items-center">
                        <div className="bg-blue-500 w-12" style={{height: heights.offerHeight}}></div>
                        <span className="text-xs mt-2">Offer</span>
                    </div>
                    <div className="flex flex-col items-center">
                        <div className="bg-blue-500 w-12" style={{height: heights.rejectionHeight}}></div>
                        <span className="text-xs mt-2">Rejected</span>
                    </div>
                    <div className="flex flex-col items-center">
                        <div className="bg-blue-500 w-12" style={{height: heights.acceptedHeight}}></div>
                        <span className="text-xs mt-2">Accepted</span>
                    </div>
                </div>
            </div>

            {/* Recent Activity */}
            <div className="rounded-lg border bg-white p-6 shadow-sm">
                <h3 className="text-lg font-medium mb-4">Recent Activity</h3>
                <div className="space-y-4">
                    {recentActivity.map((activity, index) => (<div key={index} className="flex items-start gap-4">
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