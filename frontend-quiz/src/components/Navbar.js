import { AcademicCapIcon, SunIcon, MoonIcon} from "@heroicons/react/24/solid";

function Navbar({ darkMode, setDarkMode }) {
  return (
    <div>
      {/* NAVBAR (laptop view) */}
      <nav className={`flex items-center justify-between px-10 py-5 border-b
                       ${darkMode ? "bg-[#181C14]" : "bg-blue-200"} shadow-md`}>
        {/* logo + name */}
        <div className="flex items-center space-x-3">
          <AcademicCapIcon className={`h-9 w-9 ${darkMode ? "text-indigo-400" : "text-blue-600"}`} />
          <span className="text-2xl font-bold tracking-wide">QuizApp</span>
        </div>

        {/* nav links */}
        <div className="flex items-center space-x-10 text-lg font-medium">
          <a href="/create-quiz"  className="hover:text-indigo-300">Create Quiz</a>
          <a href="/fun-quiz"     className="hover:text-indigo-300">Fun Quiz</a>
          <a href="/leaderboard"  className="hover:text-indigo-300">Leaderboard</a>
          <a href="/login"        className="hover:text-indigo-300">Login</a>
          <a href="/signup"       className="hover:text-indigo-300">Signup</a>

          {/* theme switch */}
          <button
            onClick={() => setDarkMode(!darkMode)}
            className={`flex items-center px-3 py-1 rounded-full
                        bg-gray-300 dark:bg-gray-700 transition`}>
            <SunIcon  className="h-5 w-5 text-yellow-500" />
            <div className={`w-5 h-5 mx-1 bg-white rounded-full shadow
                             transform transition ${darkMode ? "translate-x-5" : ""}`} />
            <MoonIcon className="h-5 w-5 text-indigo-300" />
          </button>
        </div>
      </nav>
    </div>
  );
}

export default Navbar;