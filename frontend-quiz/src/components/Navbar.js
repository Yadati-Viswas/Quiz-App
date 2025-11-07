import { AcademicCapIcon, SunIcon, MoonIcon, Cog6ToothIcon, ClockIcon, ArrowUpIcon, ArrowRightOnRectangleIcon, ChevronDownIcon } from "@heroicons/react/24/solid";
import { Link } from "react-router-dom";
import { useState } from 'react';
import { useAuth, useAuthNavigate } from '../contexts/AuthContext';

function ProfileDropdown({ darkMode, toggleDarkMode }) {
  const [isOpen, setIsOpen] = useState(false);
  const { user } = useAuth();
  const { logout } = useAuthNavigate();
  
  return (
    <div className="relative">
      <button 
        onClick={() => setIsOpen(!isOpen)} 
        className={`flex items-center px-3 py-1 rounded-full ${darkMode ? 'bg-gray-700 text-white' : 'bg-gray-200 text-black'}`}
      >
        <div className={`h-6 w-6 rounded-full ${darkMode ? 'bg-indigo-500' : 'bg-blue-500'} flex items-center justify-center text-white text-sm mr-2`}>
           { (user.username && user.username.charAt(0).toUpperCase()) }
        </div>
        {user.username }
        <ChevronDownIcon className="h-4 w-4 ml-1" />
      </button>
      {isOpen && (
        <div className={`absolute right-0 mt-2 w-56 rounded-lg shadow-lg overflow-hidden ${darkMode ? 'bg-[#181C14] text-white' : 'bg-blue-100 text-black'}`}>
          <div className="px-4 py-3 border-b border-gray-200 dark:border-gray-700">
            <div className="flex items-center">
              <div className={`h-10 w-10 rounded-full ${darkMode ? 'bg-indigo-500' : 'bg-blue-500'} flex items-center justify-center text-white text-lg`}>
                { (user.username && user.username.charAt(0).toUpperCase()) }
              </div>
              <div className="ml-3">
                <p className="font-semibold">{user.username}</p>
                <p className="text-sm opacity-70">{user.email}</p>
              </div>
            </div>
          </div>
          <a href="#" className="flex items-center px-4 py-2 hover:bg-blue-200 dark:hover:bg-gray-700">
            <Cog6ToothIcon className="h-5 w-5 mr-3 opacity-80" />
            Profile Settings
          </a>
          <a href="#" className="flex items-center px-4 py-2 hover:bg-blue-200 dark:hover:bg-gray-700">
            <ClockIcon className="h-5 w-5 mr-3 opacity-80" />
            Previous Quizzes
          </a>
          <a href="#" className="flex items-center px-4 py-2 hover:bg-blue-200 dark:hover:bg-gray-700">
            <ArrowUpIcon className="h-5 w-5 mr-3 opacity-80" />
            Leaderboard
          </a>
          <a href="/" onClick={(logout)} className="flex items-center px-4 py-2 hover:bg-blue-200 dark:hover:bg-gray-700">
            <ArrowRightOnRectangleIcon className="h-5 w-5 mr-3 opacity-80" />
            Sign Out
          </a>
        </div>
      )}
    </div>
  );
}

const ProductMenuDropdown = ({ darkMode = false }) => {
  const [isOpen, setIsOpen] = useState(false);
  let timeoutId; // To handle delayed closing

  const handleMouseEnter = () => {
    clearTimeout(timeoutId); // Cancel any pending close
    setIsOpen(true); // Open the dropdown
  };

  const handleMouseLeave = () => {
    timeoutId = setTimeout(() => {
      setIsOpen(false); // Close the dropdown after a delay
    }, 200); // Adjust delay as needed (200ms works well)
  };

  return (
    <div
      className="relative"
      onMouseEnter={handleMouseEnter} // Keep dropdown open when hovering over the trigger
      onMouseLeave={handleMouseLeave} // Close dropdown when leaving the entire dropdown area
    >
      {/* Trigger */}
      <button className="flex items-center space-x-1 text-lg font-medium hover:text-indigo-400 transition">
        <span>Products</span>
        <ChevronDownIcon className={`h-4 w-4 transition-transform ${isOpen ? "rotate-180" : ""}`} />
      </button>

      {/* Mega Dropdown */}
      {isOpen && (
        <div
          className={`absolute left-1/2 transform -translate-x-1/2 top-full mt-2 w-screen max-w-xl rounded-xl overflow-hidden border
            ${darkMode ? "bg-gray-900 border-gray-800 text-white" : "bg-white border-gray-200 text-gray-900"}
            transition-all duration-200 ease-out origin-top`}
          onMouseEnter={handleMouseEnter} // Keep dropdown open when hovering over the dropdown content
          onMouseLeave={handleMouseLeave} // Close dropdown when leaving the dropdown content
        >
          <div className="space-y-5 p-6">
            {[
              { title: "OmniQuiz", desc: "Start practicing quizzes or Create a quiz of your own choice", link: "/omni-quiz" },
              { title: "Interview", desc: "Conduct stellar technical interviews", link: "/interview" },
              { title: "SkillUp", desc: "Practice courses of Your Choice", link: "/skillup" },
              { title: "Learn to Code", desc: "Coding assessments to upskill yourself", link: "/learn-to-code" },
            ].map((item) => (
              <Link to={item.link} key={item.title} className="group cursor-pointer block">
                <div>
                  <h4 className="font-semibold text-lg group-hover:text-indigo-400 transition">
                    {item.title}
                  </h4>
                  <p className={`text-sm mt-1 ${darkMode ? "text-gray-400" : "text-gray-600"}`}>
                    {item.desc}
                  </p>
                </div>
              </Link>
            ))}
          </div>
        </div>
      )}
    </div>
  );
};

function Navbar({ darkMode, toggleDarkMode}) {
  const { isAuthenticated } = useAuth();
  

  return (
    <div>
      {/* NAVBAR (laptop view) */}
      <nav className={`flex items-center justify-between px-10 py-5 border-b
                       ${darkMode ? "bg-[#181C14]" : "bg-blue-200"} shadow-md`}>
        {/* logo + name */}
        <div className="flex items-center space-x-3">
          <Link to ="/"><AcademicCapIcon className={`h-9 w-9 ${darkMode ? "text-indigo-400" : "text-blue-600"}`} />
          <p className="text-2xl font-bold tracking-wide">OmniSkill</p></Link>
        </div>

        {/* nav links */}
        <div className="flex items-center space-x-10 text-lg font-medium">
          <a href="/create-quiz"  className="hover:text-indigo-300">
          Practice
          </a>
          <a href="/start-quiz"     className="hover:text-indigo-300">
          <ProductMenuDropdown darkMode={darkMode} />
          </a>
          <a href="/leaderboard"  className="hover:text-indigo-300">Resources</a>
          {isAuthenticated ? (
            <ProfileDropdown darkMode={darkMode} toggleDarkMode={toggleDarkMode} />
          ) : (
            <>
              <a href="/login"        className="hover:text-indigo-300">Login</a>
              <a href="/signup"       className="hover:text-indigo-300">Signup</a>
            </>
          )}

          {/* theme switch */}
          <button
            onClick={() => toggleDarkMode(!darkMode)}
            className={`flex items-center px-3 py-1 rounded-full
                        bg-gray-300 dark:bg-gray-700 transition`}>
            <SunIcon  className="h-5 w-5 text-yellow-500" />
            <div className={`w-4 h-4 mx-1 bg-white rounded-full shadow
                             transform transition ${darkMode ? "translate-x-5" : ""}`} />
            <MoonIcon className=" h-5 w-5 text-indigo-300" />
          </button>
        </div>
      </nav>
    </div>
  );
}

export default Navbar;