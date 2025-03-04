import React from 'react';
import { Search, Filter, Briefcase } from 'lucide-react';

const JobListings = () => {
  // Mock job listings data
  const jobListings = [
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

  return (
    <div className="space-y-6">
      <div className="flex items-center justify-between">
        <h1 className="text-3xl font-bold">Job Listings</h1>
      </div>

      {/* Search and Filter */}
      <div className="flex flex-col gap-4 md:flex-row">
        <div className="relative flex-1">
          <Search className="absolute left-3 top-1/2 h-4 w-4 -translate-y-1/2 text-gray-500" />
          <input
            type="text"
            placeholder="Search for jobs..."
            className="w-full rounded-md border border-gray-300 pl-10 py-2 text-sm focus:outline-none focus:ring-2 focus:ring-blue-500"
          />
        </div>
        <button className="inline-flex items-center gap-2 rounded-md border border-gray-300 px-4 py-2 text-sm">
          <Filter className="h-4 w-4" />
          Filter
        </button>
      </div>

      {/* Job Listings */}
      <div className="space-y-4">
        {jobListings.map((job) => (
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
                      {job.type}
                    </span>
                    <span className="inline-flex items-center rounded-full bg-green-100 px-2.5 py-0.5 text-xs font-medium text-green-800">
                      {job.salary}
                    </span>
                    <span className="inline-flex items-center rounded-full bg-gray-100 px-2.5 py-0.5 text-xs font-medium text-gray-800">
                      Posted {job.posted}
                    </span>
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
        ))}
      </div>
    </div>
  );
};

export default JobListings; 