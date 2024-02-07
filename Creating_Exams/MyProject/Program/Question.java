package nimrodBar;

import java.io.Serializable;

@SuppressWarnings("serial")
public abstract class Question implements Serializable {
	public enum eDifficulty {
		EASY, NORMAL, DIFFICULT
	};

	protected eDifficulty theDifficult;
	protected static int counter;
	protected String aQuestion;
	protected int SerialNum;

	public Question(String aQuestion, eDifficulty theDifficult) {
		this.theDifficult = theDifficult;
		setAQuestion(aQuestion);
		this.SerialNum = ++counter;
	}
	
	public abstract boolean isAnswerExist(Answer theAnswer);

	public static int getNumOfQuestion() {
		return counter;
	}

	public String getAQuestion() {
		return aQuestion;
	}

	public int setSerialNum(int num) {
		this.SerialNum = num;
		return this.SerialNum;
	}

	public static void setCounter(int numOfQuestions) {
		counter = numOfQuestions;
	}

	public boolean setAQuestion(String aQuestion) {
		if (aQuestion == null) {
			return false;
		}

		if (!aQuestion.endsWith("?")) {
			aQuestion = aQuestion + "?";
		}
		this.aQuestion = aQuestion;
		return true;
	}


	@Override
	public boolean equals(Object obj) {
		if (!(obj instanceof Question)) {
			return false;
		}
		Question temp = (Question) obj;
		return ((this.aQuestion.equals(temp.aQuestion)) && (this.theDifficult == temp.theDifficult)
				&& (this.SerialNum == temp.SerialNum));
	}

	@Override
	public String toString() {
		StringBuffer str = new StringBuffer(aQuestion + "");
		str.append("\n(Serial num #" + SerialNum + ",Difficulty level:" + theDifficult + ")\n");

		return str.toString();
	}

}
