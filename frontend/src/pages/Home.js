import React, {useEffect} from 'react';
import {Link, useNavigate} from 'react-router-dom';
import {useAuth} from '../contexts/AuthContext';

function Home() {
    const {user} = useAuth();
    const navigate = useNavigate();

    useEffect(() => {
        // If user is authenticated, redirect to Dashboard
        if (user) {
            navigate('/dashboard');
        }
    }, [user, navigate]);

    // Only render the landing page (non-authenticated view)
    return (
        <div className="min-h-screen bg-gradient-to-br from-blue-50 to-purple-100">
            <nav
                className="fixed w-full top-0 z-50 bg-background/95 backdrop-blur supports-[backdrop-filter]:bg-background/60 border-b border-border">
                <div className="container flex h-16 items-center justify-between">
                    <div className="text-2xl font-extrabold tracking-tight text-gray-900">JobTracker</div>
                    <div className="flex gap-4">
                        <Link to="/signin" className="px-4 py-2 text-sm font-medium text-gray-900 hover:text-gray-700">
                            Sign In
                        </Link>
                        <Link to="/signup"
                              className="px-4 py-2 text-sm font-medium bg-gradient-to-r from-blue-500 to-purple-500 text-white hover:brightness-110 rounded-md transition">
                            Sign Up
                        </Link>
                    </div>
                </div>
            </nav>

            <main className="container pt-32 pb-16">
                <div className="flex flex-col items-center text-center gap-4">
                    <h1 className="text-5xl md:text-6xl font-extrabold tracking-tight leading-tight">
                        Track Your Job Search Journey
                    </h1>
                    <p className="text-xl text-muted-foreground max-w-[700px] mx-auto">
                        Organize your job applications, track your progress, and land your dream job with our powerful
                        job application tracking platform.
                    </p>
                    <div className="flex gap-4 mt-8">
                        <Link to="/signup"
                              className="px-8 py-3 text-lg font-medium bg-gradient-to-r from-blue-500 to-purple-500 text-white rounded-lg shadow-md hover:brightness-110 transition">
                            Get Started
                        </Link>
                    </div>
                </div>

                {/* Why Choose Section - Enhanced */}
                <div className="mt-32">
                    <div className="text-center mb-16">
                        <h2 className="text-4xl font-extrabold mb-4 bg-gradient-to-r from-blue-600 to-purple-600 bg-clip-text text-transparent">Why
                            Choose JobTracker?</h2>
                        <div
                            className="w-24 h-1 bg-gradient-to-r from-blue-500 to-purple-500 mx-auto rounded-full"></div>
                    </div>

                    <div className="grid grid-cols-1 md:grid-cols-2 gap-12">
                        <div
                            className="bg-white rounded-2xl shadow-xl p-8 transform transition-all duration-300 hover:scale-105 hover:shadow-2xl">
                            <div
                                className="h-20 w-20 rounded-full bg-gradient-to-br from-blue-400 to-blue-600 flex items-center justify-center mx-auto mb-6 shadow-lg">
                                <svg xmlns="http://www.w3.org/2000/svg" className="h-10 w-10 text-white" fill="none"
                                     viewBox="0 0 24 24" stroke="currentColor">
                                    <path strokeLinecap="round" strokeLinejoin="round" strokeWidth={2}
                                          d="M5 13l4 4L19 7"/>
                                </svg>
                            </div>
                            <h3 className="text-2xl font-bold mb-3 text-center">Stay Organized</h3>
                            <p className="text-gray-600 text-center">
                                Keep all your job applications, interviews, and follow-ups in one centralized location
                                with our intuitive dashboard and smart organization tools.
                            </p>
                        </div>

                        <div
                            className="bg-white rounded-2xl shadow-xl p-8 transform transition-all duration-300 hover:scale-105 hover:shadow-2xl">
                            <div
                                className="h-20 w-20 rounded-full bg-gradient-to-br from-purple-400 to-purple-600 flex items-center justify-center mx-auto mb-6 shadow-lg">
                                <svg xmlns="http://www.w3.org/2000/svg" className="h-10 w-10 text-white" fill="none"
                                     viewBox="0 0 24 24" stroke="currentColor">
                                    <path strokeLinecap="round" strokeLinejoin="round" strokeWidth={2}
                                          d="M9 19v-6a2 2 0 00-2-2H5a2 2 0 00-2 2v6a2 2 0 002 2h2a2 2 0 002-2zm0 0V9a2 2 0 012-2h2a2 2 0 012 2v10m-6 0a2 2 0 002 2h2a2 2 0 002-2m0 0V5a2 2 0 012-2h2a2 2 0 012 2v14a2 2 0 01-2 2h-2a2 2 0 01-2-2z"/>
                                </svg>
                            </div>
                            <h3 className="text-2xl font-bold mb-3 text-center">Track Progress</h3>
                            <p className="text-gray-600 text-center">
                                Monitor your application status and success rates with detailed analytics, beautiful
                                visualizations, and actionable insights.
                            </p>
                        </div>
                    </div>
                </div>

                {/* Features Section - Enhanced */}
                <div className="mt-32 mb-16">
                    <div className="text-center mb-16">
                        <h2 className="text-4xl font-extrabold mb-4 bg-gradient-to-r from-blue-600 to-purple-600 bg-clip-text text-transparent">Features</h2>
                        <div
                            className="w-24 h-1 bg-gradient-to-r from-blue-500 to-purple-500 mx-auto rounded-full"></div>
                        <p className="text-gray-600 mt-4 max-w-2xl mx-auto">Our comprehensive set of tools designed to
                            streamline your job search</p>
                    </div>

                    <div className="grid grid-cols-1 md:grid-cols-3 gap-8">
                        <div
                            className="bg-white rounded-2xl shadow-lg p-8 border-t-4 border-blue-500 transform transition-all duration-300 hover:-translate-y-2 hover:shadow-xl">
                            <div
                                className="h-16 w-16 rounded-full bg-blue-100 flex items-center justify-center mx-auto mb-6">
                                <svg xmlns="http://www.w3.org/2000/svg" className="h-8 w-8 text-blue-500" fill="none"
                                     viewBox="0 0 24 24" stroke="currentColor">
                                    <path strokeLinecap="round" strokeLinejoin="round" strokeWidth={2}
                                          d="M13 10V3L4 14h7v7l9-11h-7z"/>
                                </svg>
                            </div>
                            <h3 className="text-xl font-bold mb-3 text-center text-gray-800">Fast &amp; Responsive</h3>
                            <p className="text-gray-600 text-center">
                                Experience a swift and responsive interface for seamless tracking, with instant updates
                                and smooth interactions.
                            </p>
                        </div>

                        <div
                            className="bg-white rounded-2xl shadow-lg p-8 border-t-4 border-purple-500 transform transition-all duration-300 hover:-translate-y-2 hover:shadow-xl">
                            <div
                                className="h-16 w-16 rounded-full bg-purple-100 flex items-center justify-center mx-auto mb-6">
                                <svg xmlns="http://www.w3.org/2000/svg" className="h-8 w-8 text-purple-500" fill="none"
                                     viewBox="0 0 24 24" stroke="currentColor">
                                    <path strokeLinecap="round" strokeLinejoin="round" strokeWidth={2}
                                          d="M12 15v2m-6 4h12a2 2 0 002-2v-6a2 2 0 00-2-2H6a2 2 0 00-2 2v6a2 2 0 002 2zm10-10V7a4 4 0 00-8 0v4h8z"/>
                                </svg>
                            </div>
                            <h3 className="text-xl font-bold mb-3 text-center text-gray-800">Secure</h3>
                            <p className="text-gray-600 text-center">
                                Your data is protected with top-level security measures, including end-to-end encryption
                                and regular security audits.
                            </p>
                        </div>

                        <div
                            className="bg-white rounded-2xl shadow-lg p-8 border-t-4 border-indigo-500 transform transition-all duration-300 hover:-translate-y-2 hover:shadow-xl">
                            <div
                                className="h-16 w-16 rounded-full bg-indigo-100 flex items-center justify-center mx-auto mb-6">
                                <svg xmlns="http://www.w3.org/2000/svg" className="h-8 w-8 text-indigo-500" fill="none"
                                     viewBox="0 0 24 24" stroke="currentColor">
                                    <path strokeLinecap="round" strokeLinejoin="round" strokeWidth={2}
                                          d="M9.663 17h4.673M12 3v1m6.364 1.636l-.707.707M21 12h-1M4 12H3m3.343-5.657l-.707-.707m2.828 9.9a5 5 0 117.072 0l-.548.547A3.374 3.374 0 0014 18.469V19a2 2 0 11-4 0v-.531c0-.895-.356-1.754-.988-2.386l-.548-.547z"/>
                                </svg>
                            </div>
                            <h3 className="text-xl font-bold mb-3 text-center text-gray-800">Intuitive Design</h3>
                            <p className="text-gray-600 text-center">
                                A user-friendly layout that makes navigation a breeze, with thoughtful design that
                                anticipates your needs.
                            </p>
                        </div>
                    </div>
                </div>
            </main>

            <footer className="border-t border-border py-4">
                <div className="container text-center text-sm text-muted-foreground">
                    © {new Date().getFullYear()} JobTracker. All rights reserved.
                </div>
            </footer>
        </div>
    );
}

export default Home;