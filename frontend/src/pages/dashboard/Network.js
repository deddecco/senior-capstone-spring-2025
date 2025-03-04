import React from 'react';
import { Search, UserPlus, Users } from 'lucide-react';

const Network = () => {
  // Mock connections data
  const connections = [
    {
      id: 1,
      name: 'John Smith',
      title: 'Senior Software Engineer at Google',
      mutualConnections: 12,
      avatar: 'JS'
    },
    {
      id: 2,
      name: 'Sarah Johnson',
      title: 'Product Manager at Microsoft',
      mutualConnections: 8,
      avatar: 'SJ'
    },
    {
      id: 3,
      name: 'Michael Brown',
      title: 'Frontend Developer at Amazon',
      mutualConnections: 5,
      avatar: 'MB'
    },
    {
      id: 4,
      name: 'Emily Davis',
      title: 'UX Designer at Apple',
      mutualConnections: 3,
      avatar: 'ED'
    }
  ];

  // Mock connection suggestions
  const suggestions = [
    {
      id: 5,
      name: 'David Wilson',
      title: 'CTO at Startup Inc.',
      mutualConnections: 15,
      avatar: 'DW'
    },
    {
      id: 6,
      name: 'Jennifer Lee',
      title: 'Engineering Manager at Netflix',
      mutualConnections: 7,
      avatar: 'JL'
    },
    {
      id: 7,
      name: 'Robert Taylor',
      title: 'Backend Developer at Meta',
      mutualConnections: 4,
      avatar: 'RT'
    }
  ];

  return (
    <div className="space-y-6">
      <div className="flex items-center justify-between">
        <h1 className="text-3xl font-bold">Network</h1>
        <div className="text-sm text-gray-500">
          {connections.length} connections
        </div>
      </div>

      {/* Search */}
      <div className="relative">
        <Search className="absolute left-3 top-1/2 h-4 w-4 -translate-y-1/2 text-gray-500" />
        <input
          type="text"
          placeholder="Search your network..."
          className="w-full rounded-md border border-gray-300 pl-10 py-2 text-sm focus:outline-none focus:ring-2 focus:ring-blue-500"
        />
      </div>

      {/* Connections */}
      <div>
        <h2 className="text-xl font-semibold mb-4">Your Connections</h2>
        <div className="grid grid-cols-1 gap-4 md:grid-cols-2">
          {connections.map((connection) => (
            <div key={connection.id} className="rounded-lg border bg-white p-4 shadow-sm hover:shadow-md transition-shadow">
              <div className="flex items-center gap-4">
                <div className="h-12 w-12 rounded-full bg-blue-100 flex items-center justify-center text-blue-600 font-medium">
                  {connection.avatar}
                </div>
                <div className="flex-1">
                  <h3 className="font-medium">{connection.name}</h3>
                  <p className="text-sm text-gray-600">{connection.title}</p>
                  <p className="text-xs text-gray-500 mt-1">
                    <Users className="h-3 w-3 inline mr-1" />
                    {connection.mutualConnections} mutual connections
                  </p>
                </div>
                <button className="rounded-md border border-gray-300 px-3 py-1.5 text-xs font-medium hover:bg-gray-50">
                  Message
                </button>
              </div>
            </div>
          ))}
        </div>
      </div>

      {/* Suggestions */}
      <div>
        <h2 className="text-xl font-semibold mb-4">People You May Know</h2>
        <div className="grid grid-cols-1 gap-4 md:grid-cols-2">
          {suggestions.map((suggestion) => (
            <div key={suggestion.id} className="rounded-lg border bg-white p-4 shadow-sm hover:shadow-md transition-shadow">
              <div className="flex items-center gap-4">
                <div className="h-12 w-12 rounded-full bg-gray-100 flex items-center justify-center text-gray-600 font-medium">
                  {suggestion.avatar}
                </div>
                <div className="flex-1">
                  <h3 className="font-medium">{suggestion.name}</h3>
                  <p className="text-sm text-gray-600">{suggestion.title}</p>
                  <p className="text-xs text-gray-500 mt-1">
                    <Users className="h-3 w-3 inline mr-1" />
                    {suggestion.mutualConnections} mutual connections
                  </p>
                </div>
                <button className="rounded-md bg-blue-600 px-3 py-1.5 text-xs font-medium text-white hover:bg-blue-700">
                  <UserPlus className="h-3 w-3 inline mr-1" />
                  Connect
                </button>
              </div>
            </div>
          ))}
        </div>
      </div>
    </div>
  );
};

export default Network; 