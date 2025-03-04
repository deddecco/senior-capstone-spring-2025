import React from 'react';
import { Search, BookOpen, Play, Award, Clock } from 'lucide-react';

const LearningHub = () => {
  // Mock courses data
  const courses = [
    {
      id: 1,
      title: 'Modern React with Hooks',
      instructor: 'John Doe',
      level: 'Intermediate',
      duration: '6 hours',
      rating: 4.8,
      students: 12500,
      image: '📱'
    },
    {
      id: 2,
      title: 'Advanced JavaScript Concepts',
      instructor: 'Jane Smith',
      level: 'Advanced',
      duration: '8 hours',
      rating: 4.9,
      students: 9800,
      image: '💻'
    },
    {
      id: 3,
      title: 'CSS Grid and Flexbox Masterclass',
      instructor: 'Mike Johnson',
      level: 'Beginner',
      duration: '4 hours',
      rating: 4.7,
      students: 15200,
      image: '🎨'
    },
    {
      id: 4,
      title: 'Node.js Backend Development',
      instructor: 'Sarah Williams',
      level: 'Intermediate',
      duration: '10 hours',
      rating: 4.6,
      students: 8300,
      image: '🔧'
    }
  ];

  // Mock resources data
  const resources = [
    {
      id: 1,
      title: 'How to Ace Your Technical Interview',
      type: 'Article',
      readTime: '10 min read',
      image: '📝'
    },
    {
      id: 2,
      title: 'Building a Professional Developer Portfolio',
      type: 'Video',
      readTime: '15 min watch',
      image: '🎬'
    },
    {
      id: 3,
      title: 'Negotiating Your Tech Salary',
      type: 'Podcast',
      readTime: '25 min listen',
      image: '🎙️'
    }
  ];

  return (
    <div className="space-y-6">
      <div className="flex items-center justify-between">
        <h1 className="text-3xl font-bold">Learning Hub</h1>
      </div>

      {/* Search */}
      <div className="relative">
        <Search className="absolute left-3 top-1/2 h-4 w-4 -translate-y-1/2 text-gray-500" />
        <input
          type="text"
          placeholder="Search courses and resources..."
          className="w-full rounded-md border border-gray-300 pl-10 py-2 text-sm focus:outline-none focus:ring-2 focus:ring-blue-500"
        />
      </div>

      {/* Recommended Courses */}
      <div>
        <h2 className="text-xl font-semibold mb-4">Recommended Courses</h2>
        <div className="grid grid-cols-1 gap-4 md:grid-cols-2 lg:grid-cols-4">
          {courses.map((course) => (
            <div key={course.id} className="rounded-lg border bg-white overflow-hidden shadow-sm hover:shadow-md transition-shadow">
              <div className="bg-blue-100 h-32 flex items-center justify-center text-4xl">
                {course.image}
              </div>
              <div className="p-4">
                <h3 className="font-medium">{course.title}</h3>
                <p className="text-sm text-gray-600">{course.instructor}</p>
                <div className="flex items-center mt-2 text-xs text-gray-500">
                  <span className="flex items-center">
                    <Award className="h-3 w-3 mr-1" />
                    {course.level}
                  </span>
                  <span className="mx-2">•</span>
                  <span className="flex items-center">
                    <Clock className="h-3 w-3 mr-1" />
                    {course.duration}
                  </span>
                </div>
                <div className="mt-3 flex items-center justify-between">
                  <div className="text-xs text-gray-500">
                    ⭐ {course.rating} ({course.students.toLocaleString()} students)
                  </div>
                  <button className="rounded-md bg-blue-600 px-2 py-1 text-xs font-medium text-white hover:bg-blue-700">
                    <Play className="h-3 w-3 inline mr-1" />
                    Start
                  </button>
                </div>
              </div>
            </div>
          ))}
        </div>
      </div>

      {/* Career Resources */}
      <div>
        <h2 className="text-xl font-semibold mb-4">Career Resources</h2>
        <div className="grid grid-cols-1 gap-4 md:grid-cols-3">
          {resources.map((resource) => (
            <div key={resource.id} className="rounded-lg border bg-white p-4 shadow-sm hover:shadow-md transition-shadow">
              <div className="flex items-center gap-4">
                <div className="h-12 w-12 rounded-full bg-gray-100 flex items-center justify-center text-xl">
                  {resource.image}
                </div>
                <div>
                  <h3 className="font-medium">{resource.title}</h3>
                  <div className="flex items-center mt-1 text-xs text-gray-500">
                    <span className="bg-gray-100 px-2 py-0.5 rounded-full">
                      {resource.type}
                    </span>
                    <span className="mx-2">•</span>
                    <span className="flex items-center">
                      <Clock className="h-3 w-3 mr-1" />
                      {resource.readTime}
                    </span>
                  </div>
                </div>
              </div>
            </div>
          ))}
        </div>
      </div>

      {/* Learning Paths */}
      <div className="rounded-lg border bg-white p-6 shadow-sm">
        <div className="flex items-center justify-between mb-4">
          <h2 className="text-xl font-semibold">Learning Paths</h2>
          <button className="text-sm text-blue-600 hover:text-blue-800">
            View All
          </button>
        </div>
        <div className="space-y-4">
          <div className="rounded-lg border p-4 hover:bg-gray-50 transition-colors">
            <div className="flex items-center justify-between">
              <div className="flex items-center gap-3">
                <div className="rounded-full bg-blue-100 p-2">
                  <BookOpen className="h-5 w-5 text-blue-600" />
                </div>
                <div>
                  <h3 className="font-medium">Frontend Developer Path</h3>
                  <p className="text-sm text-gray-600">12 courses • 45 hours</p>
                </div>
              </div>
              <div className="text-sm">
                <span className="text-green-600 font-medium">25% complete</span>
                <div className="mt-1 h-2 w-24 bg-gray-200 rounded-full overflow-hidden">
                  <div className="h-full bg-green-600 rounded-full" style={{ width: '25%' }}></div>
                </div>
              </div>
            </div>
          </div>
          <div className="rounded-lg border p-4 hover:bg-gray-50 transition-colors">
            <div className="flex items-center justify-between">
              <div className="flex items-center gap-3">
                <div className="rounded-full bg-purple-100 p-2">
                  <BookOpen className="h-5 w-5 text-purple-600" />
                </div>
                <div>
                  <h3 className="font-medium">Full Stack Developer Path</h3>
                  <p className="text-sm text-gray-600">20 courses • 72 hours</p>
                </div>
              </div>
              <div className="text-sm">
                <span className="text-green-600 font-medium">10% complete</span>
                <div className="mt-1 h-2 w-24 bg-gray-200 rounded-full overflow-hidden">
                  <div className="h-full bg-green-600 rounded-full" style={{ width: '10%' }}></div>
                </div>
              </div>
            </div>
          </div>
        </div>
      </div>
    </div>
  );
};

export default LearningHub; 