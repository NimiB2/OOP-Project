package nimrodBar;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;

@SuppressWarnings("serial")
public class MultipleChoice extends Question implements Serializable {
	private final int MAX_ANSWERS = 10;
	private List<AnswerCorrect> allAnswersC = new ArrayList<>();
	
	public MultipleChoice(String aQuestion, eDifficulty theDifficult) {
		super(aQuestion, theDifficult);
	}

	@SuppressWarnings("unlikely-arg-type")
	@Override
	public boolean isAnswerExist(Answer theAnswer) {
		for (int i = 0; i < this.allAnswersC.size(); i++) {
			if (this.allAnswersC.get(i).getAnswerC().getAnswer().equalsIgnoreCase(theAnswer.getAnswer())) {
				return true;
			}
		}
		return false;
	}

	public AnswerCorrect getAnswerC(int index) {
		return allAnswersC.get(index);
	}

	public List<AnswerCorrect> getAnswers() {
		return this.allAnswersC;
	}

	public boolean setAnswerC(int index, Answer theAnswer, boolean isCorrect) {
		if (theAnswer.getAnswer().equals(null)) {
			return false;
		}
		AnswerCorrect temp = new AnswerCorrect(theAnswer, isCorrect);
		this.allAnswersC.set(index, temp);
		return true;
	}

	public boolean addAnswerC(Answer theAnswer, boolean isCorrect) {

		AnswerCorrect aC = new AnswerCorrect(theAnswer, isCorrect);
		return MyGenneric.add(aC, allAnswersC);
	}

	public boolean deleteAnswerC(int index) {
		return MyGenneric.delete(index, allAnswersC);
	}

	public int getNumOfAnswersC() {
		return this.allAnswersC.size();
	}

	public int getMaxAnswers() {
		return this.MAX_ANSWERS;
	}


	public String showAnswers() {
	    StringBuffer str = new StringBuffer();
	    if (!this.allAnswersC.isEmpty()) {
	        str.append("The answers: \n");
	        Iterator<AnswerCorrect> iterator = this.allAnswersC.iterator();
	        int index = 1;
	        while (iterator.hasNext()) {
	            str.append(index++ + ") " + iterator.next().toString());
	        }
	    } else {
	        str.append("No answers for this question\n");
	    }
	    return str.toString();
	}

	@Override
	public String toString() {
		return super.toString() + showAnswers();
	}

	@Override
	public boolean equals(Object obj) {
		if (!(obj instanceof MultipleChoice)) {
			return false;
		}
		if (!(super.equals(obj))) {
			return false;
		}
		MultipleChoice temp = (MultipleChoice) obj;
		return (this.aQuestion.equals(temp.aQuestion) && this.theDifficult.equals(temp.theDifficult)
				&& this.allAnswersC.equals(temp.allAnswersC));
	}
}
