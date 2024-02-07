package nimrodBar;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;

@SuppressWarnings("serial")
public class DataBase implements Serializable {
	private String subject;
	List<Question> allQuestions = new ArrayList<>();
	List<Answer> allAnswers = new ArrayList<>();


	public DataBase(String subject) {
		this.subject = subject;
	}

	public String getSubject() {
		return this.subject;
	}

	public boolean setQuestion(int index, Question aQuestion) {
		if (index > this.allQuestions.size()) {
			return false;
		}

		if (this.allQuestions.isEmpty()) {
			return false;
		}

		if (isQuestionExists(aQuestion.getAQuestion())) {
			return false;
		}
		this.allQuestions.add(index, aQuestion);
		return true;
	}

	public Question getQuestionByIndex(int index) {
		return this.allQuestions.get(index);
	}

	public int getQuestionIndex(String aQuestion) {

		for (int i = 0; i < this.allQuestions.size(); i++) {
			if (this.allQuestions.get(i).getAQuestion().equalsIgnoreCase(aQuestion)) {
				return i;
			}
		}
		return (-1);
	}

	// change to new func
	@SuppressWarnings("unlikely-arg-type")
	public boolean isQuestionExists(String aQuestion) {
		return allQuestions.contains(aQuestion);
	}

	// change to new func
	public boolean deleteQuestion(int index) {
		return MyGenneric.delete(index, allQuestions);
	}


	public List<Question> getQuestions() {
		return this.allQuestions;
	}
	public int getNumOfQuestions() {
		return allQuestions.size();
	}

	public boolean setAnswer(int index, Answer theAnswer) {
		if (index >= this.allAnswers.size()) {
			return false;
		}

		if (this.allAnswers.isEmpty()) {
			return false;
		}

		if (isAnswerExist(theAnswer)) {
			return false;
		}

		this.allAnswers.set(index, theAnswer);
		return true;
	}

	public Answer getAnswer(int index) {
		return this.allAnswers.get(index);
	}

	public int getAnswerByIndex(String theAnswer) {

		for (int i = 0; i < this.allAnswers.size(); i++) {
			if (this.allAnswers.get(i).getAnswer().equalsIgnoreCase(theAnswer)) {
				return i;
			}
		}
		return (-1);
	}

	public boolean isAnswerExist(Answer theAnswer) {
		return this.allAnswers.contains(theAnswer);
	}

	public boolean addAnswer(Answer theAnswer) {
		return MyGenneric.add(theAnswer, allAnswers);
	}

	public boolean addQuestion(Question aQuestion) {
		return MyGenneric.add(aQuestion, allQuestions);
	}

	public boolean deleteAnswer(int index) {
		return MyGenneric.delete(index, allAnswers);
	}

	public int getNumOfAnswers() {
		return this.allAnswers.size();
	}

	public String showQuestions() {
		StringBuffer str = new StringBuffer("All questions:\n");
		Iterator<Question> iterator = this.allQuestions.iterator();
		int index = 1;
		   while (iterator.hasNext()) {
		        str.append(index++ + ") " + iterator.next().getAQuestion());
		        str.append("\n");
		    }
		return str.toString();
	}	
	


	public String showAllAnswers() {
		StringBuffer str = new StringBuffer("All the answers are in the database:\n");
		Iterator<Answer> iterator = this.allAnswers.iterator();
		int index =1;
		while (iterator.hasNext()) {
			str.append((index++) + ") " + iterator.next().getAnswer() + "\n");
		}
		return str.toString();
	}

	public String toString() {
		StringBuffer sb = new StringBuffer("Total number of questions: " + this.allQuestions.size());
		Iterator<Question> it = this.allQuestions.iterator();
		int index = 1;
		while(it.hasNext()) {
			sb.append("\n"+index++ + ")" + it.next().toString());		
		}
		sb.append("\n");
		return sb.toString();
	}


	@Override
	public boolean equals(Object obj) {
		if (!(obj instanceof DataBase)) {
			return false;
		}

		DataBase temp = (DataBase) obj;
		return (this.subject.equals(temp.subject)
				&& ((this.allAnswers.equals(temp.allAnswers)) && (this.allQuestions.equals(temp.allQuestions))));
	}
}