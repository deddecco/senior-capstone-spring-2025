import React from 'react';
import { Search, Star, Briefcase } from 'lucide-react';

const SavedJobs = () => {
  // Mock saved jobs data
  const savedJobs = [
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
  ];

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

      {/* Saved Jobs */}
      <div className="space-y-4">
        {savedJobs.map((job) => (
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
                      {job.type}
                    </span>
                    <span className="inline-flex items-center rounded-full bg-green-100 px-2.5 py-0.5 text-xs font-medium text-green-800">
                      {job.salary}
                    </span>
                    <span className="inline-flex items-center rounded-full bg-yellow-100 px-2.5 py-0.5 text-xs font-medium text-yellow-800">
                      Saved {job.saved}
                    </span>
                  </div>
                </div>
              </div>
              <div className="flex gap-2">
                <button className="rounded-md bg-blue-600 px-3 py-1.5 text-sm font-medium text-white hover:bg-blue-700">
                  Apply
                </button>
                <button className="rounded-md border border-gray-300 px-3 py-1.5 text-sm font-medium hover:bg-gray-50">
                  <Star className="h-4 w-4 fill-yellow-400 text-yellow-400" />
                </button>
              </div>
            </div>
            <p className="mt-4 text-sm text-gray-600">{job.description}</p>
          </div>
        ))}
      </div>
    </div>
  );
};

export default SavedJobs; 