import React from 'react';
import { BarChart, Calendar, Star } from 'lucide-react';

const Dashboard = () => {
  // Mock data for the dashboard
  const stats = {
    totalApplications: 24,
    pendingInterviews: 3,
    savedJobs: 12
  };

  const recentActivity = [
    {
      company: 'Google',
      position: 'Senior Frontend Developer',
      time: '2 hours ago',
      icon: 'email'
    },
    {
      company: 'Microsoft',
      position: 'Full Stack Engineer',
      time: 'Yesterday',
      icon: 'calendar'
    },
    {
      company: 'Amazon',
      position: 'React Developer',
      time: '2 days ago',
      icon: 'star'
    }
  ];

  return (
    <div className="space-y-6">
      <div className="flex items-center justify-between">
        <h1 className="text-3xl font-bold">Dashboard</h1>
        <div className="flex gap-3">
          <button className="inline-flex items-center gap-2 rounded-md bg-gray-900 px-4 py-2 text-sm font-medium text-white hover:bg-gray-800">
            <span className="h-4 w-4" />
            Career Advisor
          </button>
          <button className="inline-flex items-center gap-2 rounded-md bg-gray-900 px-4 py-2 text-sm font-medium text-white hover:bg-gray-800">
            <BarChart className="h-4 w-4" />
            View Analytics
          </button>
        </div>
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
          <div className="h-64 flex items-end justify-between gap-2">
            <div className="flex flex-col items-center">
              <div className="bg-blue-500 w-12 h-48"></div>
              <span className="text-xs mt-2">Applied</span>
            </div>
            <div className="flex flex-col items-center">
              <div className="bg-blue-500 w-12 h-16"></div>
              <span className="text-xs mt-2">Interview</span>
            </div>
            <div className="flex flex-col items-center">
              <div className="bg-blue-500 w-12 h-10"></div>
              <span className="text-xs mt-2">Offer</span>
            </div>
            <div className="flex flex-col items-center">
              <div className="bg-blue-500 w-12 h-4"></div>
              <span className="text-xs mt-2">Rejected</span>
            </div>
            <div className="flex flex-col items-center">
              <div className="bg-blue-500 w-12 h-16"></div>
              <span className="text-xs mt-2">Accepted</span>
            </div>
          </div>
        </div>

        {/* Recent Activity */}
        <div className="rounded-lg border bg-white p-6 shadow-sm">
          <h3 className="text-lg font-medium mb-4">Recent Activity</h3>
          <div className="space-y-4">
            {recentActivity.map((activity, index) => (
              <div key={index} className="flex items-start gap-4">
                <div className={`rounded-full p-2 ${
                  activity.icon === 'email' ? 'bg-blue-100 text-blue-500' :
                  activity.icon === 'calendar' ? 'bg-purple-100 text-purple-500' :
                  'bg-yellow-100 text-yellow-500'
                }`}>
                  {activity.icon === 'email' && (
                    <svg xmlns="http://www.w3.org/2000/svg" width="20" height="20" viewBox="0 0 24 24" fill="none" stroke="currentColor" strokeWidth="2" strokeLinecap="round" strokeLinejoin="round" className="lucide lucide-mail"><rect width="20" height="16" x="2" y="4" rx="2"/><path d="m22 7-8.97 5.7a1.94 1.94 0 0 1-2.06 0L2 7"/></svg>
                  )}
                  {activity.icon === 'calendar' && (
                    <Calendar className="h-5 w-5" />
                  )}
                  {activity.icon === 'star' && (
                    <Star className="h-5 w-5" />
                  )}
                </div>
                <div>
                  <h4 className="font-medium">{activity.company}</h4>
                  <p className="text-sm text-gray-500">{activity.position}</p>
                  <p className="text-xs text-gray-400 mt-1">{activity.time}</p>
                </div>
              </div>
            ))}
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