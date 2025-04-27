import React from 'react';
import {Link, Outlet, useNavigate} from 'react-router-dom';
import Sidebar from './Sidebar';
import {useAuth} from '../../contexts/AuthContext';

const DashboardLayout = ({children}) => {
    const {user, signOut} = useAuth();
    const navigate = useNavigate();

    const handleSignOut = async () => {
        try {
            const {error} = await signOut();
            if (error) {
                console.error('Error signing out:', error);
            } else {
                navigate('/');
            }
        } catch (err) {
            console.error('Error signing out:', err);
        }
    };

    return (<div className="flex h-screen bg-gray-50">
            <Sidebar/>

            <div className="flex-1 flex flex-col overflow-hidden">
                <header className="bg-white border-b h-16 flex items-center justify-between px-6">
                    <Link to="/dashboard" className="text-2xl font-bold text-blue-600">
                        JobTracker
                    </Link>

                    <div className="flex items-center gap-4">
                        {user && (<>
                <span className="text-sm text-gray-600">
                  Welcome, {user.email}
                </span>
                                <button
                                    onClick={handleSignOut}
                                    className="text-sm text-blue-600 hover:text-blue-800"
                                >
                                    Sign Out
                                </button>
                            </>)}
                    </div>
                </header>

                <main className="flex-1 overflow-y-auto p-6">
                    {children || <Outlet/>}
                </main>
            </div>
        </div>);
};

export default DashboardLayout; 