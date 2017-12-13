package abdullahsoft.com.thenewspaperapp.Model;

/**
 * Created by Md. Abrar on 10/18/2016.
 */

public class QuestionariesObject
{
    public int question_id;
    public int parent_id;
    public int answer_field_count;
    public int answer_id;
    public String question;
    public String answer_type;
    public String question_type;
    public String parent_question;
    public String answer;//this is actually a Json Array

//    public Questionaries_AnswerField answer_field;

//    public QuestionariesObject extension = null;
    public int extension = 0 ;//serves as a boolean value

    public QuestionariesObject()
    {

    }

    public interface FIELD_NAMES {
        public static final String QUESTION_ID = "question_id";
        public static final String QUESTION = "question";
        public static final String ANSWER_TYPE = "answer_type";
        public static final String QUESTION_TYPE = "question_type";
        public static final String PARENT_ID = "parent_id";
        public static final String PARENT_QUESTION = "parent_question";
        public static final String ANSWER_FIELD_COUNT = "answer_field_count";
        public static final String ANSWER_ID = "answer_id";
        public static final String ANSWER = "answer";//this is actually a Json Array
        public static final String EXTENSION = "extension";

        public static final String ANSWER_FIELD = "answer_field";
    }
}
