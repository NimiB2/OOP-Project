package nimrodBar;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.InputMismatchException;
import java.util.Iterator;
import java.util.List;
import java.util.Scanner;
import nimrodBar.Question.eDifficulty;

public class MyMain {
	private static Scanner s = new Scanner(System.in);

	public static boolean toContinue() {
		char yOn;
		while (true) {
			try {
				yOn = s.next().charAt(0);
				s.nextLine();
				if (yOn == 'n' || yOn == 'N') {
					return false;
				} else if (yOn == 'y' || yOn == 'Y') {
					return true;
				} else {
					System.out.println("Input is incorrect, try again");
					System.out.println("Insert - (Y/N)");
				}
			} catch (Exception e) {
				System.out.println("Input is incorrect, try again");
				System.out.println("Insert - (Y/N)");
				if (s.hasNextLine()) {
					s.nextLine();
				}
			}
		}
	}

	public static void showDataBaseQuestions(DataBase db) {
		if (db.getNumOfQuestions() == 0) {
			System.out.println("There are no available questions in the database.");

		} else {
			System.out.println("All questions:");
			System.out.print(db.toString());
		}
	}

	public static void showDataBaseAnswers(DataBase db) {
		if (db.getNumOfAnswers() == 0) {
			System.out.println("There are no available answers in the database.");
		} else {
			System.out.println("");
			System.out.println(db.showAllAnswers());
		}

	}

	public static boolean addQuestionToDB(DataBase db) {
		Answer a;
		String question;
		List<eDifficulty> allDifficult = new ArrayList<eDifficulty>();
		allDifficult.add(eDifficulty.EASY);
		allDifficult.add(eDifficulty.NORMAL);
		allDifficult.add(eDifficulty.DIFFICULT);
		eDifficulty theDifficulty = Question.eDifficulty.EASY;
		boolean res = false;
		boolean fContinue = true, toContinue = false;
		int indexQ, indexA;
		int choice;
		int maxAnswers = db.getNumOfAnswers();
		int maxDifficulty = allDifficult.size();

		System.out.println("Insert the new question:");
		question = s.nextLine();

		if (!question.endsWith("?")) {
			question = question + "?";
		}
		res = db.isQuestionExists(question);

		if (res) {
			System.out.println("The question already exists in the database");
			return res;
		} else {
			do {
				try {

					System.out.println("This is the difficult level options:");
					Iterator<eDifficulty> iterator = allDifficult.iterator();
					while (iterator.hasNext()) {
						System.out.println(iterator.next().name());
					}
					System.out.println("Insert the difficulty level you want:");
					String ans = s.next();
					theDifficulty = Question.eDifficulty.valueOf(ans.toUpperCase());
					fContinue = false;
				} catch (IllegalArgumentException e) {
					System.out.println("Invalid input, try again");
					s.nextLine();
				}
			} while (fContinue);

			do {
				try {
					fContinue = true;
					choice = 0;
					System.out.println("Select the question type");
					System.out.println("1--> Open question, 2--> Multiple choice question");
					choice = s.nextInt();

					switch (choice) {
					case 1:
						indexA = -1;
						if (maxAnswers == 0) {
							System.out.println(
									"There are no available answers in the database yet.\nPlease add your answer.");
							addNewAnswerToDB(db);
							indexA = db.getNumOfAnswers();
							res = true;
						} else {
							System.out.println("Now we will add an answer to the question");

							while (!res) {
								System.out.println(db.showAllAnswers());
								System.out.println("Insert the index of the answer you want:");
								indexA = s.nextInt();
								res = Services.checkIndex(indexA, maxAnswers);
							}
						}
						indexA--;
						if (res) {
							a = db.getAnswer(indexA);
							Question op = new OpenQuestion(question, theDifficulty, a);
							res = db.addQuestion(op);
							fContinue = false;
						}
						break;

					case 2:
						MultipleChoice mc = new MultipleChoice(question, theDifficulty);
						res = db.addQuestion(mc);

						if (maxAnswers == 0) {
							System.out.println(
									"There are no available answers in the database yet.\nPlease add your answer.");
							addNewAnswerToDB(db);
							indexA = db.getNumOfAnswers();
							a = db.getAnswer(--indexA);
							mc.addAnswerC(a, setIsCorrect());

							System.out.println("Would you like to add more answers? (Y/N)");
							toContinue = toContinue();
							if (toContinue) {
								indexQ = db.getNumOfQuestions();
								res = addAnswerToMultipleChoice(db, mc);
							}

						} else if (res) {
							System.out.println("Would you like to add answers? (Y/N)");
							toContinue = toContinue();
							if (toContinue) {
								indexQ = db.getNumOfQuestions();
								res = addAnswerToMultipleChoice(db, mc);
							}
						}
						fContinue = false;
						break;

					default:
						System.out.println("The value is incorrect, enter again");
					}
				} catch (IllegalArgumentException e) {
					System.out.println("Invalid input, try again");
					fContinue = true;
					s.nextLine();
				}
			} while (fContinue);

			System.out.print("The question addition is ");
			Services.printIfSuccess(res);
			return res;
		}
	}

	public static boolean SelectAQuestion(DataBase db) {
		boolean res;
		int indexQ;

		if (db.getNumOfQuestions() > 0 && db.getNumOfAnswers() > 0) {
			indexQ = pickQuestion(db);
			res = Services.checkIndex(indexQ, db.getNumOfQuestions());

			if (res) {
				addAnswerToQuestion(db, indexQ);
			}
			return res;
		} else if (db.getNumOfQuestions() == 0) {
			System.out.println("There are no questions in the database");
			return false;
		} else {
			System.out.println("There are no answers in the database");
			return false;
		}
	}

	public static boolean addNewAnswerToDB(DataBase db) {
		boolean res;
		boolean exist;
		String answer;

		System.out.println("Write the new answer: ");
		answer = s.nextLine();
		Answer a = new Answer(answer);
		exist = db.isAnswerExist(a);

		if (exist) {
			System.out.println("\nThe answer already exist in the dataBase.");
			return false;
		}

		res = db.addAnswer(a);
		System.out.print("The answer addition is ");
		Services.printIfSuccess(res);
		System.out.println(db.showAllAnswers());

		return res;
	}

	public static boolean addAnswerToQuestion(DataBase db, int indexQ) {
		boolean res;
		Question q;

		indexQ--;
		q = db.getQuestionByIndex(indexQ);

		if (q instanceof MultipleChoice) {
			MultipleChoice mc = (MultipleChoice) q;
			res = addAnswerToMultipleChoice(db, mc);
		} else {
			OpenQuestion op = (OpenQuestion) q;
			res = setAnswerToOpenQuestion(db, op);
		}

		return res;
	}

	public static boolean setAnswerToOpenQuestion(DataBase db, OpenQuestion op) {
		int indexA = -1;
		boolean res = false, toContinue = false;

		System.out.println("\nThe existing answer :\n" + op.getAnswer().toString());
		System.out.println("This is question must have only one answer");
		System.out.println("Would you like to replace with another answer from the database? (Y/N)");
		toContinue = toContinue();
		if (toContinue) {
			do {
				try {
					indexA = pickAnswer(db, op);

					if (indexA == -1) {
						System.out.println("Try again.");
					}
				} catch (InputMismatchException e) {
					System.out.println("Invalid input, try again");
					s.nextLine();
				}
			} while (indexA == -1);
			res = op.setAnswer(db.getAnswer(indexA));

		}
		return res;
	}

	public static boolean addAnswerToMultipleChoice(DataBase db, MultipleChoice mc) {
		int indexA;
		int numAnswersDB;
		int maxAnswerQuestion;
		int numAnswersOfThisQuestion;
		boolean res = true, toContinue = true, fContinueInput;

		maxAnswerQuestion = mc.getMaxAnswers();
		numAnswersDB = db.getNumOfAnswers();
		numAnswersOfThisQuestion = mc.getNumOfAnswersC();

		while (res && toContinue && numAnswersOfThisQuestion < numAnswersDB
				&& numAnswersOfThisQuestion < maxAnswerQuestion) {
			do {
				indexA = pickAnswer(db, mc);

				if (indexA == -1) {
					System.out.println("Try again.");
				}
			} while (indexA == -1);

			res = matchingAnswerToQuestion(db, mc, indexA);
			numAnswersOfThisQuestion = mc.getNumOfAnswersC();

			if (numAnswersOfThisQuestion < maxAnswerQuestion) {
				if (numAnswersOfThisQuestion < numAnswersDB) {
					do {
						fContinueInput = true;
						System.out.println("Would you like to add more answers? (Y/N)");
						toContinue = toContinue();
						fContinueInput = false;
					} while (fContinueInput);
				}
			}
		}

		numAnswersOfThisQuestion = mc.getNumOfAnswersC();
		if (numAnswersOfThisQuestion == maxAnswerQuestion) {
			System.out.println("There are maximum (" + maxAnswerQuestion + ") answers that can be added to a question");
			return res;
		}

		if (numAnswersOfThisQuestion == db.getNumOfAnswers()) {
			System.out.println("All the answers in the database are in the question");
		}

		return res;
	}

	public static boolean matchingAnswerToQuestion(DataBase db, MultipleChoice mc, int indexA) {
		boolean isCorrect;
		boolean res;

		res = mc.isAnswerExist(db.getAnswer(indexA));

		if (res) {
			System.out.println("\nThe answer already exists!\n");
			return res;
		}

		isCorrect = setIsCorrect();
		res = mc.addAnswerC(db.getAnswer(indexA), isCorrect);
		System.out.print("The answer addition is ");
		Services.printIfSuccess(res);

		return res;
	}

	public static boolean setIsCorrect() {
		int ans;
		boolean fContinue = true;

		do {
			System.out.println("Define of answer:(1/2)\n");
			System.out.println(" 1 = 'true', 2 = 'false'.\n");
			ans = s.nextInt();

			if (ans == 1) {
				return true;
			} else if (ans == 2) {
				return false;
			} else {
				System.out.println("Wrong input, try again.");
			}
		} while (fContinue);

		return fContinue;
	}

	public static boolean deleteQuestionFromDB(DataBase db) {
		int index;
		boolean res = true;

		if (db.getNumOfQuestions() == 0) {
			System.out.println("You need to add questions to the database");
			return false;
		} else {
			index = pickQuestion(db);
			res = Services.checkIndex(index, db.getNumOfQuestions());

			if (res) {
				res = db.deleteQuestion(index - 1);
			}

			System.out.print("The question was deleted ");
			Services.printIfSuccess(res);

			if (res) {
				System.out.println(db.showQuestions());
			}

			return res;
		}

	}

	public static boolean deleteAnswerFromQuestionInDB(DataBase db) {
		int indexQ;
		int indexA;
		int maxAnswers;
		boolean res = true;
		if (db.getNumOfQuestions() > 0 && db.getNumOfAnswers() > 0) {
			indexQ = pickQuestion(db);
			res = Services.checkIndex(indexQ, db.getNumOfQuestions());

			if (res) {
				Question q = db.getQuestionByIndex(--indexQ);

				if (q instanceof MultipleChoice) {
					MultipleChoice mc = (MultipleChoice) q;

					System.out.println("The answers to the question:");
					System.out.println(mc.toString());
					System.out.println("Enter the answer index:");
					indexA = s.nextInt();
					maxAnswers = mc.getNumOfAnswersC();
					res = Services.checkIndex((indexA), maxAnswers);

					if (res) {
						res = mc.deleteAnswerC(--indexA);
						System.out.print("The answer deleted ");
					}
				} else {
					OpenQuestion op = (OpenQuestion) q;
					res = setAnswerToOpenQuestion(db, op);
					if (!res) {
						System.out.println("No change in this question");
						return res;
					} else {
						System.out.print("The answer replace ");
					}
				}

				Services.printIfSuccess(res);

				if (res) {
					System.out.println(q.toString());
				}
			}

			return res;
		} else if (db.getNumOfQuestions() == 0) {
			System.out.println("There are no questions in the database");
			return false;
		} else {
			System.out.println("There are no answers in the database");
			return false;
		}

	}

	public static int pickQuestion(DataBase db) {
		int indexQ;

		System.out.println(db.showQuestions());
		System.out.println("Enter the index of the question you want to select:");
		indexQ = s.nextInt();

		return indexQ;
	}

	public static int pickAnswer(DataBase db, Question q) {
		int indexA = -1;
		int ans;
		int countNotExist = 0;
		int maxDbAnswers = db.getNumOfAnswers();
		boolean res;

		List<Answer> indexs = new ArrayList<>();

		System.out.println("These are all the answers that do not exist in this question: ");
		for (int i = 0; i < maxDbAnswers; i++) {
			if (!q.isAnswerExist(db.getAnswer(i))) {
				indexs.add(db.getAnswer(i));
				System.out.println((++countNotExist) + ") " + db.getAnswer(i).getAnswer());
			}
		}

		System.out.println("Insert the index of the answer you want:");
		ans = s.nextInt();
		res = Services.checkIndex(ans, countNotExist);

		if (res) {
			indexA = db.getAnswerByIndex(indexs.get(ans - 1).getAnswer());

		}
		return indexA;
	}

	public static boolean createExam(DataBase db) throws FileNotFoundException, TooManyQuestionsException {
		int choice;
		int maxDbQuestions = db.getNumOfQuestions();
		int maxExamQuestions;
		boolean fContinue, examCheck = false, isNumQuestionsOk;

		if (db.getNumOfQuestions() > 0 && db.getNumOfAnswers() > 0) {
			do {
				fContinue = false;
				System.out.println("There are-> " + (maxDbQuestions) + " questions in our database.");
				System.out.println("How many questions would you like for your test?");
				maxExamQuestions = s.nextInt();
				try {
					isNumQuestionsOk = Services.cheackNumQuestionsExam(maxExamQuestions, maxDbQuestions);
					if (!isNumQuestionsOk) {
						System.out.println("This is greater then the number of questions in the database");
						fContinue = true;
					}
				} catch (TooManyQuestionsException e) {
					throw new TooManyQuestionsException();
				}
			} while (fContinue);

			do {
				fContinue = false;
				System.out.println("For Automatic Exam -> 1, For Manual Exam - > 2");
				choice = s.nextInt();

				switch (choice) {

				case 1:
					AutomaticExam AutomaticEx = new AutomaticExam();
					examCheck = AutomaticEx.createExam(db, maxExamQuestions);
					break;

				case 2:
					ManualExam manualEx = new ManualExam();
					examCheck = manualEx.createExam(db, maxExamQuestions);
					break;

				default:
					System.out.println("The value is incorrect, enter again");
					fContinue = true;
				}
			} while (fContinue);

			if (examCheck) {
				System.out.println("The test was created successfully!!\n");
			}
			return true;

		} else if (db.getNumOfQuestions() == 0) {
			System.out.println("There are no questions in the database");
			return false;
		} else {
			System.out.println("There are no answers in the database");
			return false;
		}
	}

	public static void printMenu() {
		System.out.println("------ MAIN MENU ------");
		System.out.println("[1]. Show all available questions and answers in the database.");
		System.out.println("[2]. Add new answer to the database.");
		System.out.println("[3]. Add an answer from the database to an existing question.");
		System.out.println("[4]. Add a new question to the database.");
		System.out.println("[5]. Delete an answer from the database.");
		System.out.println("[6]. Delete a question from the database.");
		System.out.println("[7]. Create an exam.");
		System.out.println("[0]. Exit program.");
	}

	public static DataBase chooseSubject(Repository rep)
			throws FileNotFoundException, IOException, ClassNotFoundException {
		DataBase db = null;
		String subject;
		boolean fContinue = true, res = false, isExists = true;
		int choice = 0, ans;

		do {
			fContinue = true;
			try {
				System.out.println(rep);
				System.out.println("For new subject->1, For existing subject ->2");
				choice = s.nextInt();
				fContinue = false;

				switch (choice) {
				case 1:
					do {
						System.out.println("Enter the name of the subject:");
						subject = s.next();
						if (!rep.isSubjectExists(subject)) {
							db = new DataBase(subject);
							rep.add(db);
							isExists = false;
						} else {
							System.out.println("The database already exists in the system, try again");
						}
					} while (isExists);
					break;

				case 2:
					while (!res) {
						try {
							System.out.println(rep);
							System.out.println("Choose the index subject for the database and the test:");
							ans = s.nextInt();
							res = Services.checkIndex(ans, rep.getNumOfSubjects());

							if (res) {
								db = rep.getDbByIndex(--ans);
								Question.setCounter(db.getNumOfQuestions());

							} else {
								System.out.println("Try again");
							}

						} catch (InputMismatchException e) {
							System.out.println("Invalid input, try again");
							res = false;
							s.nextLine();
						} catch (Exception e) {
							System.out.println(e.getMessage());
						}
					}
					break;
				default:
					System.out.println("The value is incorrect, try again");
					fContinue = true;
				}
			} catch (InputMismatchException e) {
				System.out.println("Invalid input, try again");
				fContinue = true;
				s.nextLine();
			} catch (Exception e) {
				System.out.println(e.getMessage());
			}
		} while (fContinue);
		return db;
	}

	public static void readDBFromFile(Repository rep) throws IOException, ClassNotFoundException {

		ObjectInputStream inFile = new ObjectInputStream(new FileInputStream("Rep.dat"));
		DataBase newDb = (DataBase) inFile.readObject();
		inFile.close();
		rep.addDatabase(newDb);

	}

	public static void hardCodeDB(DataBase db) {
		List<Question> questions = new ArrayList<>();
		List<Answer> answers = new ArrayList<>();

		Answer answer1 = new Answer("Paris is the capital of France.");
		Answer answer2 = new Answer("The Earth revolves around the Sun.");
		Answer answer3 = new Answer("Water boils at 100 degrees Celsius at sea level.");
		Answer answer4 = new Answer("A year has 365 days.");
		Answer answer5 = new Answer("There are 7 continents on Earth.");
		Answer answer6 = new Answer("The speed of light is approximately 299,792 kilometers per second.");

		answers.add(answer1);
		answers.add(answer2);
		answers.add(answer3);
		answers.add(answer4);
		answers.add(answer5);
		answers.add(answer6);

		Question question1 = new OpenQuestion("What is the capital of France?", eDifficulty.EASY, answer1);
		Question question2 = new OpenQuestion("What does the Earth revolve around?", eDifficulty.EASY, answer2);
		Question question3 = new OpenQuestion("At what temperature does water boil at sea level?", eDifficulty.NORMAL,
				answer3);
		Question question4 = new OpenQuestion("How many days are there in a year?", eDifficulty.EASY, answer4);
		Question question5 = new OpenQuestion("How many continents are there on Earth?", eDifficulty.NORMAL, answer5);
		Question question6 = new OpenQuestion("What is the speed of light?", eDifficulty.DIFFICULT, answer6);

		questions.add(question1);
		questions.add(question2);
		questions.add(question3);
		questions.add(question4);
		questions.add(question5);
		questions.add(question6);

		Answer mcAnswer1 = new Answer("Paris");
		Answer mcAnswer2 = new Answer("London");
		Answer mcAnswer3 = new Answer("Berlin");
		Answer mcAnswer4 = new Answer("Madrid");
		Answer mcAnswer5 = new Answer("Mercury");
		Answer mcAnswer6 = new Answer("Venus");
		Answer mcAnswer7 = new Answer("Mars");
		Answer mcAnswer8 = new Answer("Jupiter");

		answers.add(mcAnswer1);
		answers.add(mcAnswer2);
		answers.add(mcAnswer3);
		answers.add(mcAnswer4);
		answers.add(mcAnswer5);
		answers.add(mcAnswer6);
		answers.add(mcAnswer7);
		answers.add(mcAnswer8);

		MultipleChoice mcQuestion1 = new MultipleChoice("What is the capital of France?", eDifficulty.EASY);
		MultipleChoice mcQuestion2 = new MultipleChoice("Which planet is closest to the sun?", eDifficulty.EASY);
		MultipleChoice mcQuestion3 = new MultipleChoice("Where Michael Jackson was born?", eDifficulty.DIFFICULT);

		mcQuestion1.addAnswerC(mcAnswer2, false);
		mcQuestion1.addAnswerC(mcAnswer3, false);
		mcQuestion1.addAnswerC(mcAnswer1, true);
		mcQuestion1.addAnswerC(mcAnswer4, false);

		mcQuestion2.addAnswerC(mcAnswer5, true);
		mcQuestion2.addAnswerC(mcAnswer6, false);
		mcQuestion2.addAnswerC(mcAnswer7, false);
		mcQuestion2.addAnswerC(mcAnswer8, false);

		mcQuestion3.addAnswerC(mcAnswer1, false);
		mcQuestion3.addAnswerC(mcAnswer2, false);
		mcQuestion3.addAnswerC(mcAnswer3, false);
		mcQuestion3.addAnswerC(mcAnswer4, false);

		questions.add(mcQuestion1);
		questions.add(mcQuestion2);

		for (Answer answer : answers) {
			db.addAnswer(answer);
		}

		for (Question question : questions) {
			db.addQuestion(question);
		}

	}

	public static void writeToBinaryFile(DataBase db) throws FileNotFoundException, IOException {
		ObjectOutputStream outFile = new ObjectOutputStream(new FileOutputStream("Rep.dat"));
		outFile.writeObject(db);
		outFile.close();
	}

	private static DataBase addManoualDb() throws FileNotFoundException, IOException {
		DataBase db = new DataBase("General repository");
		hardCodeDB(db);
		writeToBinaryFile(db);
		return db;
	}

	public static void main(String[] args) throws FileNotFoundException, IOException, ClassNotFoundException {
		boolean fContinue = true;
		int choice;		
		//DataBase db = addManoualDb();
		
		Repository rep = new Repository();
		 readDBFromFile(rep);		 
		 DataBase db = chooseSubject(rep);
		 rep.addDatabase(db);

		do {
			try {
				printMenu();
				choice = s.nextInt();
				s.nextLine();
				switch (choice) {

				case 1:
					showDataBaseQuestions(db);
					showDataBaseAnswers(db);
					break;
				case 2:
					showDataBaseAnswers(db);
					addNewAnswerToDB(db);
					break;
				case 3:
					SelectAQuestion(db);
					break;
				case 4:
					showDataBaseQuestions(db);
					addQuestionToDB(db);
					break;
				case 5:
					showDataBaseAnswers(db);
					deleteAnswerFromQuestionInDB(db);
					break;
				case 6:
					showDataBaseQuestions(db);
					deleteQuestionFromDB(db);
					break;
				case 7:
					createExam(db);
					break;
				case 0:
					ObjectOutputStream outTheDataBase = new ObjectOutputStream(new FileOutputStream("Repository.dat"));
					outTheDataBase.writeObject(rep.getAllDb());
					outTheDataBase.close();
					System.out.println("Goodbye");
					fContinue = false;
					break;
				default:
					System.out.println("The input is incorrect");
				}

			} catch (TooManyQuestionsException e) {
				System.out.println(e.getMessage());

			} catch (FileNotFoundException e) {
				System.out.println(e.getMessage());
			} catch (InputMismatchException e) {
				System.out.println("Invalid input");
				s.nextLine();
			} catch (Exception e) {
				System.out.println(e.getMessage());
			}
		} while (fContinue);
		s.close();
	}

}
