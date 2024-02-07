package nimrodBar;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.List;

public class Services {

	public static boolean checkIndex(int index, int max) {
		if (index <= 0) {
			System.out.println("\nThe index must be greater than 0");
			return false;
		}

		if (index > max) {
			System.out.println("\nYou entered too large an index");
			return false;
		} else
			return true;

	}

	public static void printIfSuccess(boolean res) {
		if (res) {
			System.out.println("Success");
		} else {
			System.out.println("Failed");
		}
	}

	public static boolean checkForContinue(char ans) throws Exception {

		if (ans == 'n' || ans == 'N') {
			return false;

		} else if (ans == 'y' || ans == 'Y') {
			return true;

		} else {

			throw new Exception("Input is incorrect");
		}
	}

	public static boolean cheackNumQuestionsExam(int maxExamQuestions, int countNotExist)
			throws TooManyQuestionsException {

		if (maxExamQuestions > countNotExist) {
			return false;
		} else if ((maxExamQuestions > 10 || maxExamQuestions <= 0)) {
			throw new TooManyQuestionsException();
		} else {
			return true;
		}
	}

	public static void createExamFiles(DataBase exam, int maxExamQuestions, StringBuffer examName,
			StringBuffer solutionName, int flag) throws FileNotFoundException {
		File ex, so;
		PrintWriter pw, pw1;

		ex = new File(examName.toString());
		so = new File(solutionName.toString());
		pw = new PrintWriter(ex);
		pw1 = new PrintWriter(so);

		pw.print(Services.examToString(exam, maxExamQuestions, flag));
		pw1.print(Services.solutionToString(exam, maxExamQuestions));
		pw.close();
		pw1.close();
	}

	public static String examToString(DataBase exam, int maxExamQuestions, int flag) {
		Question q;
		int maxAnswers;

		StringBuffer str = new StringBuffer("Questions:\n");

		for (int i = 0; i < maxExamQuestions; i++) {
			q = exam.getQuestionByIndex(i);

			str.append("\nQuestion " + (i + 1) + ") " + q.getAQuestion());
			str.append("\n(Serial num #" + q.SerialNum + ",Difficulty level:" + q.theDifficult + ")\n");

			if (q instanceof MultipleChoice) {
				MultipleChoice mc = (MultipleChoice) q;
				maxAnswers = mc.getNumOfAnswersC();
				str.append("Answers:\n\n");
				for (int j = 0; j < maxAnswers; j++) {
					str.append((j + 1) + ") " + mc.getAnswerC(j).getAnswerC().getAnswer() + "\n");
				}
				str.append((mc.getNumOfAnswersC() + 1) + ") None of the above\n");

				if (flag == 1) {
					str.append((mc.getNumOfAnswersC() + 2) + ") More than one answer is correct\n");
				}
			} else {
				str.append("This is an open question, write the solution in full:\n\n");
			}
		}
		str.append("\n");
		return str.toString();
	}

	public static String solutionToString(DataBase exam, int maxQuestions) {
		Question q;
		int maxAnswers;

		Answer theCorrectAnswer = new Answer("More than one answer is correct");
		int countTrue = 0, corrcetIndex = 0;
		int j;
		StringBuffer str = new StringBuffer("Questions:\n");
		for (int i = 0; i < maxQuestions; i++) {
			q = exam.getQuestionByIndex(i);
			str.append("\nQuestion" + (i + 1) + ") " + q.getAQuestion());
			str.append("\n(Serial num #" + q.SerialNum + ",Difficulty level:" + q.theDifficult + ")\n");

			if (q instanceof MultipleChoice) {
				MultipleChoice mc = (MultipleChoice) q;
				maxAnswers = mc.getNumOfAnswersC();

				for (j = 0, countTrue = 0, corrcetIndex = 0; j < maxAnswers; j++) {
					if (mc.getAnswerC(j).getIsCorrect()) {
						countTrue++;
						theCorrectAnswer = mc.getAnswerC(j).getAnswerC();
						corrcetIndex = j;
					}
				}
				corrcetIndex++;
				str.append("\nThe correct answer is:\n");

				if (countTrue == 0) {
					theCorrectAnswer = new Answer("None of the above");
					corrcetIndex = mc.getNumOfAnswersC() + 2;
				}

				else if (countTrue > 1) {
					theCorrectAnswer = new Answer("More than one answer is correct");
					corrcetIndex = mc.getNumOfAnswersC() + 1;
				}
				str.append(corrcetIndex + ") " + theCorrectAnswer.getAnswer() + "\n");

			} else {
				OpenQuestion open = (OpenQuestion) q;
				str.append(1 + ") " + open.getAnswer() + "\n");
			}
		}

		return str.toString();
	}

}
