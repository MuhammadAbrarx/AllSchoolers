package abdullahsoft.com.thenewspaperapp.Parser;

import android.util.Log;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.StringTokenizer;
import java.util.regex.Pattern;

/**
 * Created by Md. Abrar on 1/16/2017.
 */

public class iDieNonSubmittedAnswerParser
{
    private String ansType_radio = "radio";
    private String ansType_checkbox = "checkbox";
    private String ansType_text = "text";
    private String ansType_option = "option";

    private String questionType_parent = "parent";
    private String questionType_extension = "extension";

    // Just use the appropriate method: String#split().
    //
    // String string = "004-034556";
    // String[] parts = string.split("-");
    // String part1 = parts[0]; // 004
    // String part2 = parts[1]; // 034556
    // Note that this takes a regular expression, so remember to escape special characters if necessary.
    //
    // there are 12 characters with special meanings: the backslash \, the caret ^, the dollar sign $, the period or dot ., the vertical bar or pipe symbol |, the question mark ?, the asterisk or star *, the plus sign +, the opening parenthesis (, the closing parenthesis ), and the opening square bracket [, the opening curly brace {, These special characters are often called "metacharacters".
    //     So, if you want to split on e.g. period/dot . which means "any character" in regex, use either backslash \ to escape the individual special character like so split("\\."), or use character class [] to represent literal character(s) like so split("[.]"), or use Pattern#quote() to escape the entire string like so split(Pattern.quote(".")).
    //
    //     String[] parts = string.split(Pattern.quote(".")); // Split on period.
    // To test beforehand if the string contains certain character(s), just use String#contains().
    //
    // if (string.contains("-")) {
    //     // Split it.
    // } else {
    //     throw new IllegalArgumentException("String " + string + " does not contain -");
    // }
    // No, this does not take a regular expression. For that, use String#matches() instead.
    //
    //         If you'd like to retain the split character in the resulting parts, then make use of positive lookaround. In case you want to have the split character to end up in left hand side, use positive lookbehind by prefixing ?<= group on the pattern.
    //
    // String string = "004-034556";
    // String[] parts = string.split("(?<=-)");
    // String part1 = parts[0]; // 004-
    // String part2 = parts[1]; // 034556
    // In case you want to have the split character to end up in right hand side, use positive lookahead by prefixing ?= group on the pattern.
    //
    //     String string = "004-034556";
    //     String[] parts = string.split("(?=-)");
    //     String part1 = parts[0]; // 004
    //     String part2 = parts[1]; // -034556
    //     If you'd like to limit the number of resulting parts, then you can supply the desired number as 2nd argument of split() method.
    //
    //     String string = "004-034556-42";
    //     String[] parts = string.split("-", 2);
    //     String part1 = parts[0]; // 004
    //     String part2 = parts[1]; // 034556-42

    String TAG = "tempAnswerParser";

    public iDieNonSubmittedAnswerParser ()
    {

    }

    public ArrayList<String> retrievePreSubmittedAnswers(String answerString,String answerType)
    {
        ArrayList<String> arrList_presubmittedAnswers = null;
        // if (arrList_presubmittedAnswers!=null)
        // {
        //     // forceful checking because of static values
        //     arrList_presubmittedAnswers.clear();
        // }
        arrList_presubmittedAnswers = new ArrayList<>();
        // splitter may cause error here, so using tokenizer
        // StringTokenizer tokens = new StringTokenizer(answerString, "||");
        //
        // String first = tokens.nextToken();// this will contain "Fruit"
        // String second = tokens.nextToken();// this will contain " they taste good"
        // in the case above I assumed the string has always that syntax (foo: bar)
        // but you may want to check if there are tokens or not using the hasMoreTokens met
        
        // String splitter = Pattern.quote("||");
        // String[] answers = answerString.split(Pattern.quote("||"));
        String[] answers;
        // answers = answerString.split("\\|\\|");

        Pattern pattern = Pattern.compile("\\|\\|");
        Log.d(TAG,"answerString : "+answerString);
        answers = pattern.split(answerString);
        // System.out.println(Arrays.toString(answers));

        // pattern = Pattern.compile(Pattern.quote("||"));
        // answers = pattern.split(answerString);
        // System.out.println(Arrays.toString(answers));

        for (int i = 0; i <answers.length;i++)
        {
            arrList_presubmittedAnswers.add(answers[i]);
            Log.d(TAG,"arrList_presubmittedAnswers : "+arrList_presubmittedAnswers.get(i));
            Log.d(TAG,"answers[i] : "+answers[i]);
        }

        return arrList_presubmittedAnswers;
    }
}
