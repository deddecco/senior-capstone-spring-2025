import React from 'react';
import { Mail } from 'lucide-react';

/**
 * Basic Help & Support component that displays the complete user manual
 */
const HelpAndSupport = () => {
  return (
      <div className="space-y-6">
        <h2 className="text-xl font-semibold">User Manual</h2>

        {/* Introduction Section */}
        <div className="bg-white rounded-lg border p-6">
          <h3 className="text-lg font-medium mb-4">Introduction</h3>
          <p className="text-gray-700 mb-4">
            JobTracker is an application designed to help you organize and manage your job search process.
            With JobTracker, you can keep track of job applications, schedule and monitor interviews,
            save your favorite jobs, and gain insights into your job search progress.
          </p>
          <p className="text-gray-700">
            This user manual will guide you through all the features and functionality of the JobTracker
            application, helping you make the most of the tools available to streamline your job search journey.
          </p>
        </div>

        {/* System Requirements */}
        <div className="bg-white rounded-lg border p-6">
          <h3 className="text-lg font-medium mb-4">System Requirements</h3>
          <p className="text-gray-700 mb-4">
            JobTracker is a web-based application that runs in your browser, so there's no software to download or install.
            To use JobTracker effectively, you'll need:
          </p>
          <ul className="list-disc pl-6 space-y-2 text-gray-700">
            <li><strong>Device:</strong> Any computer, tablet, or smartphone</li>
            <li><strong>Operating System:</strong> Any operating system (Windows, macOS, Linux, iOS, Android)</li>
            <li>
              <strong>Web Browser:</strong> A modern, up-to-date web browser:
              <ul className="list-disc pl-6 mt-2">
                <li>Google Chrome (version 90 or newer)</li>
                <li>Mozilla Firefox (version 88 or newer)</li>
                <li>Safari (version 14 or newer)</li>
                <li>Microsoft Edge (Chromium version, 90 or newer)</li>
              </ul>
            </li>
            <li><strong>Internet Connection:</strong> A stable internet connection</li>
          </ul>
        </div>

        {/* Accessing the Application */}
        <div className="bg-white rounded-lg border p-6">
          <h3 className="text-lg font-medium mb-4">Accessing the Application</h3>
          <p className="text-gray-700 mb-4">
            Since JobTracker is a web application, you don't need to install anything. To access it:
          </p>
          <ol className="list-decimal pl-6 space-y-2 text-gray-700">
            <li>Open your preferred web browser.</li>
            <li>In the address bar, type: https://www.jobtrack.site/</li>
            <li>Press Enter to navigate to the JobTracker website.</li>
            <li>You'll be directed to the JobTracker homepage where you can sign in or create a new account.</li>
          </ol>
        </div>

        {/* Installation Troubleshooting */}
        <div className="bg-white rounded-lg border p-6">
          <h3 className="text-lg font-medium mb-4">Installation Troubleshooting</h3>
          <p className="text-gray-700 mb-4">
            Since JobTracker is a web application that runs in your browser, most installation issues are related to browser access.
            If you're having trouble accessing JobTracker:
          </p>

          <h4 className="font-medium mt-4 mb-2">Can't Access the Website:</h4>
          <ul className="list-disc pl-6 space-y-1 text-gray-700">
            <li>Check your internet connection</li>
            <li>Try accessing another website to confirm internet connectivity</li>
            <li>Clear your browser cache and cookies</li>
            <li>Try a different browser</li>
          </ul>

          <h4 className="font-medium mt-4 mb-2">Page Loading Issues:</h4>
          <ul className="list-disc pl-6 space-y-1 text-gray-700">
            <li>Ensure JavaScript is enabled in your browser settings</li>
            <li>Check if you have browser extensions that might be blocking the site</li>
            <li>Try opening the site in an incognito/private browsing window</li>
          </ul>

          <h4 className="font-medium mt-4 mb-2">Account Creation Problems:</h4>
          <ul className="list-disc pl-6 space-y-1 text-gray-700">
            <li>Make sure you're using a valid email address</li>
            <li>Check that your password meets the minimum requirements (at least 8 characters)</li>
            <li>Verify that you've checked the confirmation email sent after registration</li>
          </ul>

          <h4 className="font-medium mt-4 mb-2">General Performance Issues:</h4>
          <ul className="list-disc pl-6 space-y-1 text-gray-700">
            <li>Close unnecessary browser tabs and applications</li>
            <li>Restart your browser</li>
            <li>Check for browser updates and install if available</li>
          </ul>
        </div>

        {/* Getting Started */}
        <div className="bg-white rounded-lg border p-6">
          <h3 className="text-lg font-medium mb-4">Getting Started</h3>

          <h4 className="font-medium mt-4 mb-2">Creating an Account</h4>
          <p className="text-gray-700 mb-2">Before you can use JobTracker, you need to create an account:</p>
          <ol className="list-decimal pl-6 space-y-2 text-gray-700">
            <li>Navigate to the JobTracker home page: https://www.jobtrack.site/</li>
            <li>Click on the Sign-Up button in the top-right corner of the screen.</li>
            <li>On the sign-up page, fill in the following information:
              <ul className="list-disc pl-6 mt-2">
                <li>Full Name</li>
                <li>Email Address</li>
                <li>Password (at least 8 characters)</li>
                <li>Confirm Password</li>
              </ul>
            </li>
            <li>Click the Sign-Up button to create your account.</li>
          </ol>

          <h4 className="font-medium mt-4 mb-2">Account Confirmation</h4>
          <p className="text-gray-700 mb-2">After creating your account, you'll need to confirm your email address:</p>
          <ol className="list-decimal pl-6 space-y-2 text-gray-700">
            <li>Check your email inbox for a verification message from JobTracker.</li>
            <li>Click on the verification link in the email.</li>
            <li>If you don't see the email, check your spam folder.</li>
            <li>You can request a new verification email from the confirmation page if needed.</li>
          </ol>

          <h4 className="font-medium mt-4 mb-2">Logging In</h4>
          <p className="text-gray-700 mb-2">Once you have created an account, you can log into JobTracker:</p>
          <ol className="list-decimal pl-6 space-y-2 text-gray-700">
            <li>Navigate to the JobTracker home page.</li>
            <li>Click on the Sign In button in the top-right corner.</li>
            <li>Enter your email address and password.</li>
            <li>Click the Sign In button.</li>
          </ol>
        </div>

        {/* Dashboard Overview */}
        <div className="bg-white rounded-lg border p-6">
          <h3 className="text-lg font-medium mb-4">Dashboard Overview</h3>
          <p className="text-gray-700 mb-4">
            After logging in, you'll be taken to your dashboard, which provides an overview of your job search activities:
          </p>

          <p className="text-gray-700 mb-2">The dashboard includes:</p>
          <ul className="list-disc pl-6 space-y-2 text-gray-700">
            <li>
              <strong>Summary Statistics:</strong> At the top of the page, you'll see cards showing:
              <ul className="list-disc pl-6 mt-2">
                <li>Total number of jobs you're tracking</li>
                <li>Number of pending interviews</li>
                <li>Number of favorite jobs</li>
              </ul>
            </li>
            <li><strong>Application Pipeline:</strong> A visual representation of your applications by status (Applied, Interview, Offer, Rejected, and Accepted).</li>
            <li><strong>Recent Activity:</strong> Shows your most recent job and interview activities.</li>
            <li><strong>Upcoming Interviews:</strong> Lists any interviews scheduled in the near future.</li>
          </ul>

          <p className="text-gray-700 mt-4 mb-2">The left sidebar contains navigation links to different sections of the application:</p>
          <ul className="list-disc pl-6 space-y-1 text-gray-700">
            <li>Dashboard</li>
            <li>Job Listings</li>
            <li>Favorite Jobs</li>
            <li>Interviews</li>
            <li>Settings</li>
          </ul>
        </div>

        {/* Managing Job Listings */}
        <div className="bg-white rounded-lg border p-6">
          <h3 className="text-lg font-medium mb-4">Managing Job Listings</h3>

          <h4 className="font-medium mt-4 mb-2">Viewing Job Listings</h4>
          <p className="text-gray-700 mb-2">To view all your job listings:</p>
          <ol className="list-decimal pl-6 space-y-2 text-gray-700">
            <li>Click on Job Listings in the left sidebar menu.</li>
            <li>You'll see a list of all the jobs you've added to your tracker.</li>
            <li>Each job card displays key information:
              <ul className="list-disc pl-6 mt-2">
                <li>Job title</li>
                <li>Company name</li>
                <li>Location</li>
                <li>Job level</li>
                <li>Salary range</li>
                <li>Application status</li>
              </ul>
            </li>
          </ol>

          <h4 className="font-medium mt-4 mb-2">Adding a New Job</h4>
          <p className="text-gray-700 mb-2">To add a new job to your tracker:</p>
          <ol className="list-decimal pl-6 space-y-2 text-gray-700">
            <li>On the Job Listings page, click the Add Job button in the top-right corner.</li>
            <li>Fill out the job details in the form:
              <ul className="list-disc pl-6 mt-2">
                <li>Job Title (required)</li>
                <li>Company Name (required)</li>
                <li>Location (required)</li>
                <li>Minimum Salary (required)</li>
                <li>Maximum Salary (required)</li>
                <li>Job Level (select from dropdown)</li>
                <li>Status (select from dropdown)</li>
              </ul>
            </li>
            <li>Click Add Job to save the new job.</li>
          </ol>

          <h4 className="font-medium mt-4 mb-2">Editing Job Details</h4>
          <p className="text-gray-700 mb-2">To edit an existing job:</p>
          <ol className="list-decimal pl-6 space-y-2 text-gray-700">
            <li>On the Job Listings page, find the job you want to edit.</li>
            <li>Click the Edit button on the job card.</li>
            <li>Update the information in the edit form.</li>
            <li>Click Save Changes to update the job.</li>
          </ol>

          <h4 className="font-medium mt-4 mb-2">Deleting a Job</h4>
          <p className="text-gray-700 mb-2">To delete a job from your tracker:</p>
          <ol className="list-decimal pl-6 space-y-2 text-gray-700">
            <li>On the Job Listings page, find the job you want to delete.</li>
            <li>Click the Delete button on the job card.</li>
            <li>Confirm the deletion when prompted.</li>
          </ol>

          <h4 className="font-medium mt-4 mb-2">Marking Jobs as Favorite</h4>
          <p className="text-gray-700 mb-2">To mark a job as a favorite:</p>
          <ol className="list-decimal pl-6 space-y-2 text-gray-700">
            <li>On any job card, click the star icon button.</li>
            <li>The star will fill with yellow color to indicate it's now a favorite.</li>
            <li>Click the same button again to unfavorite the job.</li>
          </ol>

          <h4 className="font-medium mt-4 mb-2">Searching for Jobs</h4>
          <p className="text-gray-700 mb-2">To search through your job listings:</p>
          <ol className="list-decimal pl-6 space-y-2 text-gray-700">
            <li>On the Job Listings page, use the search bar at the top.</li>
            <li>Type keywords related to the job title, company, or location.</li>
            <li>Press Enter or click the search button to see results.</li>
          </ol>

          <h4 className="font-medium mt-4 mb-2">Using Advanced Filters</h4>
          <p className="text-gray-700 mb-2">For more specific job searches:</p>
          <ol className="list-decimal pl-6 space-y-2 text-gray-700">
            <li>Click the Show Filters button next to the search bar.</li>
            <li>The filters panel will expand with additional options:
              <ul className="list-disc pl-6 mt-2">
                <li>Job Title</li>
                <li>Job Level</li>
                <li>Location</li>
                <li>Minimum Salary</li>
                <li>Maximum Salary</li>
                <li>Status</li>
              </ul>
            </li>
            <li>Enter your criteria and click Apply Filters.</li>
            <li>To reset all filters, click Clear Filters.</li>
          </ol>
        </div>

        {/* Managing Interviews */}
        <div className="bg-white rounded-lg border p-6">
          <h3 className="text-lg font-medium mb-4">Managing Interviews</h3>

          <h4 className="font-medium mt-4 mb-2">Viewing Interviews</h4>
          <p className="text-gray-700 mb-2">To view all your interviews:</p>
          <ol className="list-decimal pl-6 space-y-2 text-gray-700">
            <li>Click on Interviews in the left sidebar menu.</li>
            <li>You'll see a list of all the interviews you've scheduled.</li>
            <li>Each interview card displays:
              <ul className="list-disc pl-6 mt-2">
                <li>Company name</li>
                <li>Interview format (Virtual, In-Person, Phone)</li>
                <li>Interview round (Screening, Technical, HR, Hiring Manager, Final)</li>
                <li>Date and time</li>
              </ul>
            </li>
          </ol>

          <h4 className="font-medium mt-4 mb-2">Adding a New Interview</h4>
          <p className="text-gray-700 mb-2">To add a new interview:</p>
          <ol className="list-decimal pl-6 space-y-2 text-gray-700">
            <li>On the Interview Listings page, click the Add Interview button in the top-right corner.</li>
            <li>Fill out the interview details:
              <ul className="list-disc pl-6 mt-2">
                <li>Company Name (required)</li>
                <li>Interview Format (select from dropdown)</li>
                <li>Interview Round (select from dropdown)</li>
                <li>Date (required)</li>
                <li>Time (required)</li>
              </ul>
            </li>
            <li>Click Add Interview to save.</li>
          </ol>

          <h4 className="font-medium mt-4 mb-2">Editing Interview Details</h4>
          <p className="text-gray-700 mb-2">To edit an existing interview:</p>
          <ol className="list-decimal pl-6 space-y-2 text-gray-700">
            <li>On the Interview Listings page, find the interview you want to edit.</li>
            <li>Click the Edit button on the interview card.</li>
            <li>Update the information in the edit form.</li>
            <li>Click Save Changes to update the interview.</li>
          </ol>

          <h4 className="font-medium mt-4 mb-2">Deleting an Interview</h4>
          <p className="text-gray-700 mb-2">To delete an interview:</p>
          <ol className="list-decimal pl-6 space-y-2 text-gray-700">
            <li>On the Interview Listings page, find the interview you want to delete.</li>
            <li>Click the Delete button on the interview card.</li>
            <li>Confirm the deletion when prompted.</li>
          </ol>

          <h4 className="font-medium mt-4 mb-2">Searching for Interviews</h4>
          <p className="text-gray-700 mb-2">To search through your interviews:</p>
          <ol className="list-decimal pl-6 space-y-2 text-gray-700">
            <li>On the Interview Listings page, use the search bar at the top.</li>
            <li>Type keywords related to the company name.</li>
            <li>Press Enter or click the search button to see results.</li>
            <li>You can also use the filter button to access advanced search options.</li>
          </ol>
        </div>

        {/* Favorites */}
        <div className="bg-white rounded-lg border p-6">
          <h3 className="text-lg font-medium mb-4">Favorites</h3>

          <h4 className="font-medium mt-4 mb-2">Viewing Favorite Jobs</h4>
          <p className="text-gray-700 mb-2">To view all your favorite jobs:</p>
          <ol className="list-decimal pl-6 space-y-2 text-gray-700">
            <li>Click on Favorite Jobs in the left sidebar menu.</li>
            <li>You'll see a list of all jobs you've marked as favorites.</li>
          </ol>

          <h4 className="font-medium mt-4 mb-2">Managing Favorites</h4>
          <p className="text-gray-700 mb-2">To manage your favorites:</p>
          <ol className="list-decimal pl-6 space-y-2 text-gray-700">
            <li>On the Favorite Jobs page, you can use the search bar to find specific favorites.</li>
            <li>Click the star icon on any job card to remove it from favorites.</li>
            <li>You can edit job details directly from this page using the Edit button.</li>
          </ol>
        </div>

        {/* Settings */}
        <div className="bg-white rounded-lg border p-6">
          <h3 className="text-lg font-medium mb-4">Settings</h3>

          <h4 className="font-medium mt-4 mb-2">Viewing Profile Information</h4>
          <p className="text-gray-700 mb-2">To view your profile:</p>
          <ol className="list-decimal pl-6 space-y-2 text-gray-700">
            <li>Click on Settings in the left sidebar menu.</li>
            <li>Your profile information will be displayed, including:
              <ul className="list-disc pl-6 mt-2">
                <li>Full Name</li>
                <li>Email</li>
                <li>Phone</li>
                <li>Location</li>
                <li>Bio (professional summary)</li>
              </ul>
            </li>
          </ol>

          <h4 className="font-medium mt-4 mb-2">Updating Profile Information</h4>
          <p className="text-gray-700 mb-2">To update your profile:</p>
          <ol className="list-decimal pl-6 space-y-2 text-gray-700">
            <li>On the Settings page, update any field you wish to change.</li>
            <li>Click Save Changes to update your profile.</li>
            <li>A confirmation message will appear when your changes are saved successfully.</li>
          </ol>
        </div>

        {/* Troubleshooting */}
        <div className="bg-white rounded-lg border p-6">
          <h3 className="text-lg font-medium mb-4">Troubleshooting</h3>

          <h4 className="font-medium mt-4 mb-2">Common Issues</h4>
          <div className="mb-4">
            <p className="font-medium">Cannot Log In:</p>
            <ul className="list-disc pl-6 space-y-1 text-gray-700">
              <li>Verify that you're using the correct email and password.</li>
              <li>Ensure that you've confirmed your email address.</li>
              <li>Try resetting your password by clicking "Forgot Password" on the login page.</li>
            </ul>
          </div>

          <div className="mb-4">
            <p className="font-medium">Changes Not Saving:</p>
            <ul className="list-disc pl-6 space-y-1 text-gray-700">
              <li>Make sure you're connected to the internet.</li>
              <li>Confirm that you clicked the save button after making changes.</li>
              <li>Try refreshing the page and attempting the action again.</li>
            </ul>
          </div>

          <div className="mb-4">
            <p className="font-medium">Page Not Loading:</p>
            <ul className="list-disc pl-6 space-y-1 text-gray-700">
              <li>Check your internet connection.</li>
              <li>Try refreshing the page.</li>
              <li>Clear your browser cache and cookies.</li>
            </ul>
          </div>

          <h4 className="font-medium mt-6 mb-2">Error Messages</h4>
          <div className="mb-4">
            <p className="font-medium">"Failed to fetch":</p>
            <ul className="list-disc pl-6 space-y-1 text-gray-700">
              <li>This usually indicates a problem with your internet connection.</li>
              <li>Check your network connection and try again.</li>
            </ul>
          </div>

          <div className="mb-4">
            <p className="font-medium">"Not authorized":</p>
            <ul className="list-disc pl-6 space-y-1 text-gray-700">
              <li>Your session may have expired.</li>
              <li>Try logging out and logging back in.</li>
            </ul>
          </div>
        </div>

        {/* Support and Feedback */}
        <div className="bg-white rounded-lg border p-6">
          <h3 className="text-lg font-medium mb-4">Support and Feedback</h3>
          <p className="text-gray-700 mb-4">
            If you encounter any issues not covered in this manual or have suggestions for improving JobTracker:
          </p>
          <div className="flex justify-center space-x-4">
            <a
                href="mailto:adasi5@uis.edu"
                className="inline-flex items-center gap-2 rounded-md bg-blue-600 px-4 py-2 text-sm font-medium text-white hover:bg-blue-700"
            >
              <Mail className="h-4 w-4" />
              Email Support
            </a>
            <a
                href="mailto:nelsn2@uis.edu"
                className="inline-flex items-center gap-2 rounded-md border border-gray-300 bg-white px-4 py-2 text-sm font-medium text-gray-700 hover:bg-gray-50"
            >
              <Mail className="h-4 w-4" />
              Alternative Contact
            </a>
          </div>
          <p className="text-center mt-6 text-gray-700">
            Thank you for using JobTracker! We're committed to helping you organize your job search and achieve your career goals.
          </p>
        </div>
      </div>
  );
};

export default HelpAndSupport;