import React from 'react';
import { Link } from 'react-router-dom';

function SignUpConfirmation() {
  return (
    <div className="min-h-screen flex items-center justify-center bg-background px-4">
      <div className="w-full max-w-md p-8 space-y-6 bg-card rounded-lg border border-border text-center">
        <div className="mb-8">
          <div className="h-20 w-20 bg-primary/10 rounded-full flex items-center justify-center mx-auto mb-6">
            <svg
              className="h-10 w-10 text-primary"
              fill="none"
              viewBox="0 0 24 24"
              stroke="currentColor"
              strokeWidth="2"
            >
              <path
                strokeLinecap="round"
                strokeLinejoin="round"
                d="M3 8l7.89 5.26a2 2 0 002.22 0L21 8M5 19h14a2 2 0 002-2V7a2 2 0 00-2-2H5a2 2 0 00-2 2v10a2 2 0 002 2z"
              />
            </svg>
          </div>
          <h2 className="text-3xl font-bold tracking-tight">Check Your Email</h2>
          <p className="text-muted-foreground mt-3">
            We've sent you a verification link to complete your signup process
          </p>
        </div>

        <div className="space-y-4">
          <div className="p-4 bg-primary/5 rounded-lg border border-primary/10">
            <p className="text-sm text-muted-foreground">
              Please check your email inbox and click the verification link to activate your account.
              If you don't see the email, check your spam folder.
            </p>
          </div>

          <div className="pt-4">
            <Link
              to="/signin"
              className="text-sm text-primary hover:text-primary/80 underline-offset-4 hover:underline"
            >
              Return to Sign In
            </Link>
          </div>
        </div>

        <div className="pt-6 border-t border-border">
          <p className="text-sm text-muted-foreground">
            Didn't receive the email?{' '}
            <button className="text-primary hover:text-primary/80 hover:underline">
              Click here to resend
            </button>
          </p>
        </div>
      </div>
    </div>
  );
}

export default SignUpConfirmation; 