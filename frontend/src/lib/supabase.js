import { createClient } from '@supabase/supabase-js';

const supabaseUrl = 'https://neuurudvqouzefhvugzt.supabase.co';
const supabaseAnonKey = 'eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJpc3MiOiJzdXBhYmFzZSIsInJlZiI6Im5ldXVydWR2cW91emVmaHZ1Z3p0Iiwicm9sZSI6ImFub24iLCJpYXQiOjE3NDAwNzI2MTEsImV4cCI6MjA1NTY0ODYxMX0.5LcQWLhCQn8vsuL6wmeQ3RUvM6fG6lOljlidxPlzebc';

export const supabase = createClient(supabaseUrl, supabaseAnonKey);

export const getAuthHeader = async () => {
  const session = await supabase.auth.getSession();
  const token = session?.data?.session?.access_token;
  
  if (!token) {
    throw new Error('No authentication token found');
  }
  
  return {
    Authorization: `Bearer ${token}`
  };
};