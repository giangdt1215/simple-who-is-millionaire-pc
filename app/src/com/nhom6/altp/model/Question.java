package com.nhom6.altp.model;

import java.io.Serializable;

public class Question implements Serializable {

	private static final long serialVersionUID = 1L;

	private int id, level, truecase;
	private String question, caseA, caseB, caseC, caseD;

	public Question(int id, int level, int truecase, String question,
			String caseA, String caseB, String caseC, String caseD) {
		this.id = id;
		this.level = level;
		this.truecase = truecase;
		this.question = question;
		this.caseA = caseA;
		this.caseB = caseB;
		this.caseC = caseC;
		this.caseD = caseD;
	}

	public int getId() {
		return id;
	}

	public int getLevel() {
		return level;
	}

	public int getTruecase() {
		return truecase;
	}

	public String getQuestion() {
		return question;
	}

	public String getCaseA() {
		return caseA;
	}

	public String getCaseB() {
		return caseB;
	}

	public String getCaseC() {
		return caseC;
	}

	public String getCaseD() {
		return caseD;
	}
	
	public void replaceQuestion(Question ques){
		this.id = ques.getId();
		this.level = ques.getLevel();
		this.truecase = ques.getTruecase();
		this.caseA = ques.getCaseA();
		this.caseB = ques.getCaseB();
		this.caseC = ques.getCaseC();
		this.caseD = ques.getCaseD();
		this.question = ques.getQuestion();
	}
}
