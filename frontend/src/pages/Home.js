import React, { useEffect, useState } from 'react';
import { Link, useNavigate } from 'react-router-dom';
import { useAuth } from '../contexts/AuthContext';
import { api } from '../lib/api';

function Home() {
  const { user, signOut } = useAuth();
  const navigate = useNavigate();
  const [jobs, setJobs] = useState([]);
  const [loading, setLoading] = useState(true);
  const [error, setError] = useState(null);

  useEffect(() => {
    if (user) {
      fetchJobs();
    } else {
      setLoading(false);
    }
  }, [user]);

  const fetchJobs = async () => {
    try {
      const jobsData = await api.getJobs();
      setJobs(jobsData);
    } catch (err) {
      setError('Failed to fetch jobs');
      console.error('Error fetching jobs:', err);
    } finally {
      setLoading(false);
    }
  };

  const handleSignOut = async () => {
    try {
      await signOut();
      navigate('/signin');
    } catch (err) {
      console.error('Error signing out:', err);
    }
  };

  // Loading state
  if (loading) {
    return (
        <div className="min-h-screen bg-background flex items-center justify-center">
          <div className="text-xl">Loading...</div>
        </div>
    );
  }

  // Authenticated view
  if (user) {
    return (
        <div className="min-h-screen bg-background">
          <nav className="fixed w-full top-0 z-50 bg-background/95 backdrop-blur supports-[backdrop-filter]:bg-background/60 border-b border-border">
            <div className="container flex h-16 items-center justify-between">
              <div className="text-2xl font-bold text-primary">JobTracker</div>
              <div className="flex items-center gap-4">
                <span className="text-sm text-muted-foreground">Welcome, {user.email}</span>
                <button
                    onClick={handleSignOut}
                    className="px-4 py-2 text-sm font-medium text-primary hover:text-primary/80"
                >
                  Sign Out
                </button>
              </div>
            </div>
          </nav>

          <main className="container pt-24">
            {error && (
                <div className="mb-4 p-4 bg-destructive/10 text-destructive rounded-md">
                  {error}
                </div>
            )}

            <div className="grid grid-cols-1 md:grid-cols-3 gap-8">
              <div className="col-span-2">
                <h2 className="text-2xl font-bold mb-6">Your Job Applications</h2>
                {jobs.length === 0 ? (
                    <div className="text-center p-8 border border-border rounded-lg">
                      <p className="text-muted-foreground">No job applications yet</p>
                      <button className="mt-4 px-4 py-2 bg-primary text-primary-foreground rounded-md">
                        Add Your First Job
                      </button>
                    </div>
                ) : (
                    <div className="space-y-4">
                      {jobs.map((job) => (
                          <div key={job.id} className="p-4 border border-border rounded-lg">
                            <h3 className="font-semibold">{job.title}</h3>
                            <p className="text-sm text-muted-foreground">{job.company}</p>
                            <div className="mt-2 flex items-center gap-2">
                        <span className="px-2 py-1 text-xs bg-primary/10 rounded-full">
                          {job.status}
                        </span>
                            </div>
                          </div>
                      ))}
                    </div>
                )}
              </div>

              <div>
                <h2 className="text-2xl font-bold mb-6">Quick Stats</h2>
                <div className="space-y-4">
                  <div className="p-4 border border-border rounded-lg">
                    <h3 className="text-sm text-muted-foreground">Total Applications</h3>
                    <p className="text-2xl font-bold">{jobs.length}</p>
                  </div>
                  {/* Add more stats as needed */}
                </div>
              </div>
            </div>
          </main>

          <footer className="mt-8 border-t border-border py-4">
            <div className="container text-center text-sm text-muted-foreground">
              © {new Date().getFullYear()} JobTracker. All rights reserved.
            </div>
          </footer>
        </div>
    );
  }

  // Non-authenticated view (original landing page)
  return (
      <div className="min-h-screen bg-background">
        <nav className="fixed w-full top-0 z-50 bg-background/95 backdrop-blur supports-[backdrop-filter]:bg-background/60 border-b border-border">
          <div className="container flex h-16 items-center justify-between">
            <div className="text-2xl font-bold text-primary">JobTracker</div>
            <div className="flex gap-4">
              <Link to="/signin" className="px-4 py-2 text-sm font-medium text-primary hover:text-primary/80">
                Sign In
              </Link>
              <Link to="/signup" className="px-4 py-2 text-sm font-medium bg-primary text-primary-foreground hover:bg-primary/90 rounded-md">
                Sign Up
              </Link>
            </div>
          </div>
        </nav>

        <main className="container pt-32 pb-16">
          <div className="flex flex-col items-center text-center gap-4">
            <h1 className="text-4xl sm:text-5xl md:text-6xl lg:text-7xl font-bold tracking-tight">
              Track Your Job Search Journey
            </h1>
            <p className="text-xl text-muted-foreground max-w-[700px] mx-auto">
              Organize your job applications, track your progress, and land your dream job with our powerful job application tracking platform.
            </p>
            <div className="flex gap-4 mt-8">
              <Link to="/signup" className="px-8 py-3 text-lg font-medium bg-primary text-primary-foreground hover:bg-primary/90 rounded-md">
                Get Started
              </Link>
              <button className="px-8 py-3 text-lg font-medium border border-input bg-background hover:bg-accent hover:text-accent-foreground rounded-md">
                Learn More
              </button>
              {/* New API Testing button */}
              <Link to="/testingapi" className="px-8 py-3 text-lg font-medium border border-input bg-accent text-accent-foreground hover:bg-accent/90 rounded-md">
                API Testing
              </Link>
              <Link to="/design" className="px-8 py-3 text-lg font-medium border border-input bg-accent text-accent-foreground hover:bg-accent/90 rounded-md">
                Design
              </Link>
            </div>
          </div>

          <div className="grid grid-cols-1 md:grid-cols-3 gap-8 mt-24">
            <div className="flex flex-col items-center text-center p-6 rounded-lg border border-border bg-card">
              <div className="h-12 w-12 rounded-full bg-primary/10 flex items-center justify-center mb-4">
                📝
              </div>
              <h3 className="text-xl font-semibold mb-2">Track Applications</h3>
              <p className="text-muted-foreground">Keep track of all your job applications in one place</p>
            </div>
            <div className="flex flex-col items-center text-center p-6 rounded-lg border border-border bg-card">
              <div className="h-12 w-12 rounded-full bg-primary/10 flex items-center justify-center mb-4">
                📊
              </div>
              <h3 className="text-xl font-semibold mb-2">Analytics Dashboard</h3>
              <p className="text-muted-foreground">Get insights into your application process</p>
            </div>
            <div className="flex flex-col items-center text-center p-6 rounded-lg border border-border bg-card">
              <div className="h-12 w-12 rounded-full bg-primary/10 flex items-center justify-center mb-4">
                🔍
              </div>
              <h3 className="text-xl font-semibold mb-2">Job Search</h3>
              <p className="text-muted-foreground">Find and apply to jobs from multiple sources</p>
            </div>
          </div>

          <div className="mt-32 text-center">
            <h2 className="text-3xl font-bold mb-8">Why Choose JobTracker?</h2>
            <div className="grid grid-cols-1 md:grid-cols-2 gap-12">
              <div className="space-y-4">
                <div className="h-16 w-16 rounded-full bg-primary/10 flex items-center justify-center mx-auto mb-6">
                  🎯
                </div>
                <h3 className="text-xl font-semibold">Stay Organized</h3>
                <p className="text-muted-foreground">
                  Keep all your job applications, interviews, and follow-ups in one centralized location
                </p>
              </div>
              <div className="space-y-4">
                <div className="h-16 w-16 rounded-full bg-primary/10 flex items-center justify-center mx-auto mb-6">
                  📈
                </div>
                <h3 className="text-xl font-semibold">Track Progress</h3>
                <p className="text-muted-foreground">
                  Monitor your application status and success rates with detailed analytics
                </p>
              </div>
            </div>
          </div>

          {/* New Features Section */}
          <div className="mt-16 text-center">
            <h2 className="text-3xl font-bold mb-8">Features</h2>
            <div className="grid grid-cols-1 md:grid-cols-3 gap-8">
              <div className="flex flex-col items-center text-center p-6 rounded-lg border border-border bg-card">
                <div className="h-12 w-12 rounded-full bg-primary/10 flex items-center justify-center mb-4">
                  ⚡
                </div>
                <h3 className="text-xl font-semibold mb-2">Fast &amp; Responsive</h3>
                <p className="text-muted-foreground">
                  Experience a swift and responsive interface for seamless tracking.
                </p>
              </div>
              <div className="flex flex-col items-center text-center p-6 rounded-lg border border-border bg-card">
                <div className="h-12 w-12 rounded-full bg-primary/10 flex items-center justify-center mb-4">
                  🔒
                </div>
                <h3 className="text-xl font-semibold mb-2">Secure</h3>
                <p className="text-muted-foreground">
                  Your data is protected with top-level security measures.
                </p>
              </div>
              <div className="flex flex-col items-center text-center p-6 rounded-lg border border-border bg-card">
                <div className="h-12 w-12 rounded-full bg-primary/10 flex items-center justify-center mb-4">
                  💡
                </div>
                <h3 className="text-xl font-semibold mb-2">Intuitive Design</h3>
                <p className="text-muted-foreground">
                  A user-friendly layout that makes navigation a breeze.
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
