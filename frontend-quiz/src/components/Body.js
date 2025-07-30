import { motion } from "framer-motion";
import { TrophyIcon} from "@heroicons/react/24/solid";

export default function Body({ darkMode }) {
    const topScorers = [
      { name: "Alice Johnson", score: 95, quiz: "Math Trivia" },
      { name: "Bob Smith",   score: 92, quiz: "General Knowledge" },
      { name: "Charlie Lee", score: 89, quiz: "Science Quiz" },
    ];
    return (
        <main className="px-10 py-16 max-w-6xl mx-auto">
        {/* intro */}
        <motion.section
          initial={{ opacity: 0, y: 20 }}
          animate={{ opacity: 1, y: 0 }}
          transition={{ duration: 0.6 }}
          className="text-center mb-16">
          <h1 className="text-5xl font-extrabold mb-4">Welcome to QuizApp</h1>
          <p className="text-xl max-w-3xl mx-auto">
            Teachers can craft engaging quizzes for their classes,
            while everyone else can jump in, test their knowledge,
            and climb the leaderboards!
          </p>
        </motion.section>

        {/* top scorers */}
        <motion.section
          initial={{ opacity: 0, y: 20 }}
          animate={{ opacity: 1, y: 0 }}
          transition={{ duration: 0.6, delay: 0.15 }}
          className="mb-20">
          <h2 className="text-3xl font-semibold mb-8 text-center">Top Scorers</h2>
          <div className="grid grid-cols-3 gap-8">
            {topScorers.map((s, i) => (
              <motion.div key={i}
                initial={{ opacity: 0, scale: 0.95 }}
                animate={{ opacity: 1, scale: 1 }}
                transition={{ delay: i * 0.1 }}
                className={`${darkMode ? "bg-indigo-800" : "bg-white"}
                            p-8 rounded-xl shadow-lg hover:shadow-2xl`}>
                <TrophyIcon className={`h-12 w-12 mx-auto mb-5
                                        ${darkMode ? "text-yellow-400" : "text-yellow-500"}`} />
                <h3 className="text-xl font-bold text-center">{s.name}</h3>
                <p className="text-center mt-2">Score: {s.score}/100</p>
                <p className="text-center text-sm mt-1">Quiz: {s.quiz}</p>
              </motion.div>
            ))}
          </div>
        </motion.section>

        {/* action buttons */}
        <motion.section
          initial={{ opacity: 0, y: 20 }}
          animate={{ opacity: 1, y: 0 }}
          transition={{ duration: 0.6, delay: 0.3 }}
          className="flex justify-center space-x-8">
          {["Start Quiz", "Create Quiz", "Join Quiz"].map((label) => (
            <button key={label}
              className={`${darkMode
                  ? "bg-indigo-600 hover:bg-indigo-700"
                  : "bg-blue-600 hover:bg-blue-700"}
                  px-8 py-4 rounded-lg text-lg font-semibold transition`}>
              {label}
            </button>
          ))}
        </motion.section>
      </main>
    );
}