import React, { useState, useEffect } from 'react';
import { api } from '../../lib/api'; // Use the centralized API utility
import { User, Bell, Shield, HelpCircle } from 'lucide-react';

const Settings = () => {
  const [activeTab, setActiveTab] = useState('profile');
  const [userData, setUserData] = useState({
    name: '',
    email: '',
    phoneNumber: '',
    location: '',
    bio: '',
  });

  const [isLoading, setIsLoading] = useState(false);
  const [error, setError] = useState(null);

  // Fetch the current user's profile using the API
  const fetchProfile = async () => {
    setIsLoading(true);
    try {
      const data = await api.getCurrentProfile();
      setUserData(data); // Populate userData with the fetched backend profile
    } catch (err) {
      setError(err.message || 'Failed to load profile.');
    } finally {
      setIsLoading(false);
    }
  };

  // Update the user's profile using the API
  const updateProfile = async (updatedData) => {
    setIsLoading(true);
    try {
      const updatedProfile = await api.updateProfile(updatedData);
      setUserData(updatedProfile); // Update UI with the newly updated profile data
      setError(null); // Clear any previous errors
    } catch (err) {
      setError(err.message || 'Failed to update profile.');
    } finally {
      setIsLoading(false);
    }
  };

  // Load the profile when the component is mounted
  useEffect(() => {
    fetchProfile();
  }, []);

  const handleProfileUpdate = (e) => {
    e.preventDefault();
    const formData = new FormData(e.target);
    const updatedData = {
      name: formData.get('name'),
      email: formData.get('email'),
      phoneNumber: formData.get('phoneNumber'),
      location: formData.get('location'),
      bio: formData.get('bio'),
    };

    updateProfile(updatedData); // Call the API update function
  };

  const tabs = [
    { id: 'profile', label: 'Profile', icon: User },
    { id: 'notifications', label: 'Notifications', icon: Bell },
    { id: 'privacy', label: 'Privacy & Security', icon: Shield },
    { id: 'help', label: 'Help & Support', icon: HelpCircle },
  ];

  return (
      <div className="space-y-6">
        <div className="flex items-center justify-between">
          <h1 className="text-3xl font-bold">Settings</h1>
        </div>

        <div className="grid grid-cols-1 gap-6 md:grid-cols-4">
          {/* Tabs */}
          <div className="space-y-2">
            {tabs.map((tab) => {
              const Icon = tab.icon;
              const isActive = activeTab === tab.id;

              return (
                  <button
                      key={tab.id}
                      onClick={() => setActiveTab(tab.id)}
                      className={`flex w-full items-center gap-3 rounded-md px-3 py-2 text-sm transition-colors ${
                          isActive
                              ? 'bg-blue-100 text-blue-700 font-medium'
                              : 'text-gray-700 hover:bg-gray-100'
                      }`}
                  >
                    <Icon className="h-5 w-5" />
                    <span>{tab.label}</span>
                  </button>
              );
            })}
          </div>

          {/* Content */}
          <div className="rounded-lg border bg-white p-6 shadow-sm md:col-span-3">
            {activeTab === 'profile' && (
                <div className="space-y-6">
                  <h2 className="text-xl font-semibold">Profile Information</h2>

                  {isLoading ? (
                      <p>Loading...</p>
                  ) : error ? (
                      <p className="text-red-500">{error}</p>
                  ) : (
                      <form onSubmit={handleProfileUpdate}>
                        <div className="flex flex-col gap-4 sm:flex-row">
                          <div className="flex-1">
                            <div className="mb-4">
                              <label className="block text-sm font-medium text-gray-700 mb-1">
                                Full Name
                              </label>
                              <input
                                  type="text"
                                  name="name"
                                  defaultValue={userData.name}
                                  className="w-full rounded-md border border-gray-300 px-3 py-2 text-sm focus:outline-none focus:ring-2 focus:ring-blue-500"
                                  required
                              />
                            </div>

                            <div className="mb-4">
                              <label className="block text-sm font-medium text-gray-700 mb-1">
                                Email
                              </label>
                              <input
                                  type="email"
                                  name="email"
                                  defaultValue={userData.email}
                                  className="w-full rounded-md border border-gray-300 px-3 py-2 text-sm focus:outline-none focus:ring-2 focus:ring-blue-500"
                                  required
                              />
                            </div>

                            <div className="mb-4">
                              <label className="block text-sm font-medium text-gray-700 mb-1">
                                Phone
                              </label>
                              <input
                                  type="tel"
                                  name="phoneNumber"
                                  defaultValue={userData.phoneNumber}
                                  className="w-full rounded-md border border-gray-300 px-3 py-2 text-sm focus:outline-none focus:ring-2 focus:ring-blue-500"
                              />
                            </div>

                            <div className="mb-4">
                              <label className="block text-sm font-medium text-gray-700 mb-1">
                                Location
                              </label>
                              <input
                                  type="text"
                                  name="location"
                                  defaultValue={userData.location}
                                  className="w-full rounded-md border border-gray-300 px-3 py-2 text-sm focus:outline-none focus:ring-2 focus:ring-blue-500"
                              />
                            </div>

                            <div className="mb-4">
                              <label className="block text-sm font-medium text-gray-700 mb-1">
                                Bio
                              </label>
                              <textarea
                                  name="bio"
                                  defaultValue={userData.bio}
                                  className="w-full rounded-md border border-gray-300 px-3 py-2 text-sm focus:outline-none focus:ring-2 focus:ring-blue-500"
                                  rows="4"
                              />
                            </div>
                          </div>
                        </div>
                        <button
                            type="submit"
                            className="px-4 py-2 rounded-md bg-blue-600 text-white font-medium hover:bg-blue-700"
                        >
                          Save Changes
                        </button>
                      </form>
                  )}
                </div>
            )}
          </div>
        </div>
      </div>
  );
};

export default Settings;