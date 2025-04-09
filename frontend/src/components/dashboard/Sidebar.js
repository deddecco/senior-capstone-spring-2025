import React from 'react';
import { Link, useLocation } from 'react-router-dom';
import { 
  Home, 
  Briefcase, 
  Bookmark, 
  Users, 
  BookOpen, 
  Settings 
} from 'lucide-react';

const Sidebar = () => {
  const location = useLocation();
  
  const isActive = (path) => {
    return location.pathname === path;
  };

  const navItems = [
    { icon: Home, label: 'Dashboard', path: '/dashboard' },
    { icon: Briefcase, label: 'Job Listings', path: '/dashboard/job-listings' },
    { icon: Bookmark, label: 'Saved Jobs', path: '/dashboard/saved-jobs' },
    { icon: Settings, label: 'Settings', path: '/dashboard/settings' },
  ];

  return (
    <div className="h-screen w-64 border-r bg-white">
      <div className="p-4 border-b">
        <h2 className="text-lg font-semibold">Menu</h2>
      </div>
      <nav className="p-2">
        <ul className="space-y-1">
          {navItems.map((item, index) => {
            const Icon = item.icon;
            const active = isActive(item.path);
            
            return (
              <li key={index}>
                <Link
                  to={item.path}
                  className={`flex items-center gap-3 rounded-md px-3 py-2 text-sm transition-colors ${
                    active 
                      ? 'bg-gray-100 text-black font-medium' 
                      : 'text-gray-700 hover:bg-gray-100'
                  }`}
                >
                  <Icon className="h-5 w-5" />
                  <span>{item.label}</span>
                </Link>
              </li>
            );
          })}
        </ul>
      </nav>
    </div>
  );
};

export default Sidebar; 