import { useState } from 'react'
import './App.css'

function App() {
    const [currentTime, setCurrentTime] = useState(null)
    const [timeError, setTimeError] = useState(null)
    const [profileResult, setProfileResult] = useState(null)
    const [profileError, setProfileError] = useState(null)
    const [profileInput, setProfileInput] = useState(`{
  "user_id": "02ba21b1-4432-4267-a5ca-639774679244",
  "name": "Ellie Wilson",
  "email": "ellie@ellie.com",
  "title": "Software Reliability Engineer II",
  "bio": "writing code fueled by coffee since 2017",
  "location": "Remote"
}`)

    const getCurrentTime = async () => {
        try {
            const response = await fetch('http://localhost:8080/time/current')
            if (!response.ok) throw new Error('Failed to fetch time')
            const data = await response.json()
            setCurrentTime(data)
            setTimeError(null)
        } catch (err) {
            setTimeError(err.message)
            setCurrentTime(null)
        }
    }

    const submitProfile = async () => {
        try {
            const response = await fetch('http://localhost:8080/profiles/02ba21b1-4432-4267-a5ca-639774679245', {
                method: 'PUT',
                headers: {
                    'Content-Type': 'application/json',
                },
                body: profileInput
            })
            if (!response.ok) throw new Error('Failed to submit profile')
            const data = await response.json()
            setProfileResult(data)
            setProfileError(null)
        } catch (err) {
            setProfileError(err.message)
            setProfileResult(null)
        }
    }

    return (
        <div className="container">
            <h1>API Testing Interface</h1>

            <section className="time-section">
                <h2>Get Current Time</h2>
                <button onClick={getCurrentTime}>Get Time</button>
                <div className="result">
                    {currentTime && <pre>{JSON.stringify(currentTime, null, 2)}</pre>}
                    {timeError && <div className="error">{timeError}</div>}
                </div>
            </section>

            <section className="profile-section">
                <h2>Submit Profile</h2>
                <textarea
                    value={profileInput}
                    onChange={(e) => setProfileInput(e.target.value)}
                    placeholder="Enter JSON data for profile"
                    rows="8"
                />
                <button onClick={submitProfile}>Submit Profile</button>
                <div className="result">
                    {profileResult && <pre>{JSON.stringify(profileResult, null, 2)}</pre>}
                    {profileError && <div className="error">{profileError}</div>}
                </div>
            </section>
        </div>
    )
}

export default App