import { useState } from "react";
import { Link } from "react-router-dom";

function TestingAPI() {
    const [currentTime, setCurrentTime] = useState(null);
    const [timeError, setTimeError] = useState(null);
    const [profileResult, setProfileResult] = useState(null);
    const [profileError, setProfileError] = useState(null);
    const [profileInput, setProfileInput] = useState(`{
  "user_id": "02ba21b1-4432-4267-a5ca-639774679244",
  "name": "Ellie Wilson",
  "email": "ellie@ellie.com",
  "title": "Software Reliability Engineer II",
  "bio": "writing code fueled by coffee since 2017",
  "location": "Remote"
}`);

    const getCurrentTime = async () => {
        try {
            const response = await fetch("http://localhost:8080/time/current");
            if (!response.ok) throw new Error("Failed to fetch time");
            const data = await response.json();
            setCurrentTime(data);
            setTimeError(null);
        } catch (err) {
            setTimeError(err.message);
            setCurrentTime(null);
        }
    };

    const submitProfile = async () => {
        try {
            const response = await fetch(
                "http://localhost:8080/profiles/02ba21b1-4432-4267-a5ca-639774679245",
                {
                    method: "PUT",
                    headers: {
                        "Content-Type": "application/json",
                    },
                    body: profileInput,
                }
            );
            if (!response.ok) throw new Error("Failed to submit profile");
            const data = await response.json();
            setProfileResult(data);
            setProfileError(null);
        } catch (err) {
            setProfileError(err.message);
            setProfileResult(null);
        }
    };

    return (
        <div className="min-h-screen bg-background flex items-center justify-center">
            <div className="w-full max-w-2xl bg-white shadow-lg rounded-lg p-8 text-center">
                <h1 className="text-3xl font-bold mb-6">API Testing Interface</h1>

                {/* Landing Page Link */}
                <div className="mb-8">
                    <Link
                        to="/"
                        className="px-8 py-3 text-lg font-medium bg-primary text-primary-foreground hover:bg-primary/90 rounded-md"
                    >
                        Home
                    </Link>
                </div>

                {/* Get Current Time Section */}
                <section className="mb-6">
                    <h2 className="text-2xl font-semibold mb-4">Get Current Time</h2>
                    <button
                        onClick={getCurrentTime}
                        className="px-4 py-2 bg-green-500 hover:bg-green-600 text-white rounded-md"
                    >
                        Get Time
                    </button>
                    <div className="mt-4 text-sm text-left bg-gray-50 p-4 rounded-md">
                        {currentTime && (
                            <pre className="overflow-auto">
                {JSON.stringify(currentTime, null, 2)}
              </pre>
                        )}
                        {timeError && <div className="text-red-500">{timeError}</div>}
                    </div>
                </section>

                {/* Submit Profile Section */}
                <section>
                    <h2 className="text-2xl font-semibold mb-4">Submit Profile</h2>
                    <textarea
                        value={profileInput}
                        onChange={(e) => setProfileInput(e.target.value)}
                        placeholder="Enter JSON data for profile"
                        rows={6}
                        className="w-full p-2 border border-gray-300 rounded-md font-mono"
                    />
                    <button
                        onClick={submitProfile}
                        className="mt-4 px-4 py-2 bg-purple-600 hover:bg-purple-700 text-white rounded-md"
                    >
                        Submit Profile
                    </button>
                    <div className="mt-4 text-sm text-left bg-gray-50 p-4 rounded-md">
                        {profileResult && (
                            <pre className="overflow-auto">
                {JSON.stringify(profileResult, null, 2)}
              </pre>
                        )}
                        {profileError && <div className="text-red-500">{profileError}</div>}
                    </div>
                </section>
            </div>
        </div>
    );
}

export default TestingAPI;
