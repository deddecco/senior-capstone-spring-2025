import { BrowserRouter as Router, Routes, Route} from "react-router-dom";
import TestingAPI from './pages/TestingAPI.jsx';
import LandingPage from './pages/LandingPage.jsx';

function App(){
    return(
        <Router>
            <Routes>
                {/* Root Path > Testing Screen */}
                <Route path="/" element={<TestingAPI />} />

                {/* /landing-page > LandingPage */}
                <Route path="/landing-page" element={<LandingPage />} />
            </Routes>
        </Router>
    )
}

export default App