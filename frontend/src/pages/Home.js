import React from 'react';
import { Link } from 'react-router-dom';

function Home() {
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
              <p className="text-muted-foreground">Keep all your job applications, interviews, and follow-ups in one centralized location</p>
            </div>
            <div className="space-y-4">
              <div className="h-16 w-16 rounded-full bg-primary/10 flex items-center justify-center mx-auto mb-6">
                📈
              </div>
              <h3 className="text-xl font-semibold">Track Progress</h3>
              <p className="text-muted-foreground">Monitor your application status and success rates with detailed analytics</p>
            </div>
          </div>
        </div>
      </main>
    </div>
  );
}

export default Home; 