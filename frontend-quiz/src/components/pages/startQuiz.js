import { motion } from "framer-motion";
import Layout from "../Layout";
import { useDarkMode } from "../../contexts/DarkModeContextProvider";

export default function StartQuizPage() {
    const { darkMode } = useDarkMode();
  return (
      <Layout>
        <motion.section
          initial={{ opacity: 0, y: 20 }}
          animate={{ opacity: 1, y: 0 }}
          transition={{ duration: 0.6 }}
          className={`${darkMode ? "text-white" : "text-gray-900"} flex flex-col items-center space-y-6`}>
            <h1 className="text-4xl font-bold">Start Quiz</h1>
        </motion.section>
      </Layout>
  );
}