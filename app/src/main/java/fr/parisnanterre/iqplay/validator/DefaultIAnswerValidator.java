package fr.parisnanterre.iqplay.validator;

import fr.parisnanterre.iqplay.api.IAnswerValidator;
import fr.parisnanterre.iqplaylib.api.IPlayerAnswer;
import fr.parisnanterre.iqplaylib.api.IQuestion;

public class DefaultIAnswerValidator implements IAnswerValidator {

    @Override
    public boolean validate(IQuestion question, IPlayerAnswer answer) {
        return question.correctAnswer().answer().equals(answer.answer());
    }
}

