import axios from "axios";

const baseUrl = `http://localhost:8080`;

async function apiCall(method, endpoint, data = null, headers = {}) {
    const url = `${baseUrl}${endpoint}`;
    const defaultHeaders = { 'Content-Type': 'application/json', };
    const config = { method, url, headers: { ...defaultHeaders, ...headers }, ...(data && { data: data }) };
    try {
        const response = await axios(config);
        return response;
    } catch (error) {
        console.error("API call error:", error);
        throw error;
    }

}

async function getQuizQuestionsApi(category, difficulty, numberOfQuestions) {
    return apiCall('POST', `/v1-api/quiz/generate-questions`, {
        prompt: `Generate ${numberOfQuestions} multiple-choice questions on ${category} with difficulty level ${difficulty}, covering core concepts. Output ONLY in this JSON array format (no extra text): [
  {
    "title": "Question 1: Topic",
    "question": "The question text?",
    "code": "Optional code block as a string, or empty string if none",
    "options": ["Option1", "Option2", "Option3", "Option4"],
    "answer": "The correct option",
    "explanation": "Detailed explanation here"
  }
]
Ensure each question has exactly 4 options (a-d). In all string fields (especially "code" and "explanation"), represent newlines as \\n and do not use actual line breaks. Ensure the entire output is strictly valid JSON without any unescaped control characters.`
    }); 
    }

export { getQuizQuestionsApi };
