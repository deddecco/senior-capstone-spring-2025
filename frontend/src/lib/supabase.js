import {createClient} from '@supabase/supabase-js';

const supabaseUrl = 'https://gztnnnokimzaodmurzpf.supabase.co';
const supabaseAnonKey = 'eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJpc3MiOiJzdXBhYmFzZSIsInJlZiI6Imd6dG5ubm9raW16YW9kbXVyenBmIiwicm9sZSI6ImFub24iLCJpYXQiOjE3NDU0NDg2NDYsImV4cCI6MjA2MTAyNDY0Nn0.E2tTM5bo0VQy4Cy0iHJZ0XKUbKN99f4CnKpEO3Sm0qY';

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