import React, {useEffect, useState} from 'react';
import {Bar} from 'react-chartjs-2';
import {Calendar, Star} from 'lucide-react';
import {api} from '../../lib/api';

// The order and labels for our pipeline chart
const STATUS_LABELS = ['Applied', 'Interview', 'Offer', 'Rejected', 'Accepted'];

const Dashboard = () => {
    const [stats, setStats] = useState({
        totalApplications: 0, pendingInterviews: 0, savedJobs: 0
    });
    const [recentActivity, setRecentActivity] = useState([]);
    const [statusCounts, setStatusCounts] = useState({});
    const [loading, setLoading] = useState(true);
    const [error, setError] = useState(null);

    useEffect(() => {
        fetchDashboardData();
    }, []);

    const fetchDashboardData = async () => {
        setLoading(true);
        setError(null);

        try {
            // Fetch jobs
            let jobs = [];
            try {
                jobs = await api.getJobs();
            } catch (apiError) {
                // Try to get jobs from localStorage if API fails
                const localJobs = localStorage.getItem('jobListings');
                if (localJobs) jobs = JSON.parse(localJobs);
            }

            // Fetch interviews
            let interviews = [];
            try {
                interviews = await api.getInterviews();
            } catch (err) {
                // No error set here; empty is fine
            }

            // Fetch status counts for the pipeline chart
            let statusCountsResp = {};
            try {
                // This should call /jobs/status-counts
                statusCountsResp = await api.getJobStatusCounts();
            } catch (err) {
                // If this fails, just leave as empty object
            }
            setStatusCounts(statusCountsResp || {});

            // Calculate stats
            setStats({
                totalApplications: jobs.length,
                pendingInterviews: interviews.length,
                savedJobs: jobs.filter(job => job.status === 'Saved').length
            });

            // Recent activity: just show the three most recent jobs
            const recentJobs = jobs.slice(0, 3).map(job => ({
                company: job.company || 'Company',
                position: job.title,
                time: job.created_at ? new Date(job.created_at).toLocaleDateString() : job.posted || '',
                icon: 'email'
            }));
            setRecentActivity(recentJobs);

            // Only set error if there was a genuine fetch failure
            setError(null);
        } catch (err) {
            setError('Failed to fetch dashboard data');
        } finally {
            setLoading(false);
        }
    };

    // Prepare data for the bar chart
    const pipelineChartData = {
        labels: STATUS_LABELS, datasets: [{
            label: 'Applications',
            data: STATUS_LABELS.map(label => statusCounts[label.toLowerCase()] || 0),
            backgroundColor: ['rgba(59, 130, 246, 0.7)', // blue
                'rgba(139, 92, 246, 0.7)', // purple
                'rgba(253, 224, 71, 0.7)', // yellow
                'rgba(239, 68, 68, 0.7)', // red
                'rgba(34, 197, 94, 0.7)' // green
            ],
            borderRadius: 6
        }]
    };

    const pipelineChartOptions = {
        plugins: {
            legend: {display: false}
        }, scales: {
            y: {beginAtZero: true, ticks: {stepSize: 1}}
        }, maintainAspectRatio: false
    };

    if (loading) {
        return (<div className="text-center py-8">
                <p className="text-gray-500">Loading dashboard data...</p>
            </div>);
    }

  return (
    <div className="space-y-6">
      {error && (
        <div className="rounded-md bg-red-50 p-4 text-red-700 mb-4">
          {error}
        </div>
      )}
      
      <div className="flex items-center justify-between">
        <h1 className="text-3xl font-bold">Dashboard</h1>
      </div>

      {/* Stats Cards */}
      <div className="grid grid-cols-1 gap-4 md:grid-cols-3">
        <div className="rounded-lg border bg-white p-6 shadow-sm">
          <div className="flex items-center gap-4">
            <div className="rounded-full bg-blue-100 p-3">
              <div className="h-6 w-6 text-blue-500">
                <svg xmlns="http://www.w3.org/2000/svg" width="24" height="24" viewBox="0 0 24 24" fill="none" stroke="currentColor" strokeWidth="2" strokeLinecap="round" strokeLinejoin="round" className="lucide lucide-briefcase"><rect width="20" height="14" x="2" y="7" rx="2" ry="2"/><path d="M16 21V5a2 2 0 0 0-2-2h-4a2 2 0 0 0-2 2v16"/></svg>
              </div>
            </div>
            <div>
              <p className="text-sm text-gray-500">Total Applications</p>
              <h3 className="text-3xl font-bold">{stats.totalApplications}</h3>
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
              <h3 className="text-3xl font-bold">{stats.pendingInterviews}</h3>
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
                    <div className="h-64">
                        <Bar data={pipelineChartData} options={pipelineChartOptions}/>
                        {/* Optionally, show a message if all counts are zero */}
                        {pipelineChartData.datasets[0].data.every(count => count === 0) && (
                            <div className="text-center text-gray-400 mt-8">No applications yet.</div>)}
                    </div>
                </div>

                {/* Recent Activity */}
                <div className="rounded-lg border bg-white p-6 shadow-sm">
                    <h3 className="text-lg font-medium mb-4">Recent Activity</h3>
                    <div className="space-y-4">
                        {recentActivity.length === 0 ? (<div className="text-gray-500">No recent
                                activity.</div>) : (recentActivity.map((activity, index) => (
                                <div key={index} className="flex items-start gap-4">
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
                                        <p className="text-xs text-gray-400 mt-1">{activity.time}</p>
                                    </div>
                                </div>)))}
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
    </div>
  );
};

export default Dashboard;