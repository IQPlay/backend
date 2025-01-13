package fr.parisnanterre.iqplay.api;

import fr.parisnanterre.iqplaylib.api.IPlayerAnswer;
import fr.parisnanterre.iqplaylib.api.IQuestion;

public interface IAnswerValidator {
    boolean validate(IQuestion question, IPlayerAnswer answer);
}
