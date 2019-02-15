/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package lexicalanalyzer;

import java.util.ArrayList;
import java.util.Stack;
import java.util.concurrent.atomic.AtomicReference;

/**
 *
 * @author ABC
 */
public class CFG {

    ArrayList<SymbolTable> Li = new ArrayList<SymbolTable>();
    public static int index = 0;
    TokenValidate tv2 = new TokenValidate();

    Stack<Integer> stack = new Stack<Integer>();
    int scope = 0, currScope = 0;
    AtomicReference<String> T = new AtomicReference<String>();
    AtomicReference<String> TM = new AtomicReference<String>();
    AtomicReference<String> T2 = new AtomicReference<String>();
    AtomicReference<String> T1 = new AtomicReference<String>();
    AtomicReference<String> T3 = new AtomicReference<String>();
    AtomicReference<String> RT = new AtomicReference<String>();
    AtomicReference<String> NT = new AtomicReference<String>();
    AtomicReference<String> N = new AtomicReference<String>();
    AtomicReference<String> N1 = new AtomicReference<String>();
    AtomicReference<String> N2 = new AtomicReference<String>();
    AtomicReference<String> N3 = new AtomicReference<String>();
    AtomicReference<String> AM = new AtomicReference<String>();
    AtomicReference<String> AL = new AtomicReference<String>();
    AtomicReference<String> PL = new AtomicReference<String>();
    AtomicReference<String> NAL = new AtomicReference<String>();
    AtomicReference<String> NPL = new AtomicReference<String>();
    AtomicReference<String> NET = new AtomicReference<String>();
    AtomicReference<String> ET = new AtomicReference<String>();
    AtomicReference<String> PN = new AtomicReference<String>();
    AtomicReference<String> ET2 = new AtomicReference<String>();
    AtomicReference<String> NET2 = new AtomicReference<String>();
    AtomicReference<String> NN = new AtomicReference<String>();

    public CFG() {
    }

    public boolean check() {

        if (S()) {
            if (tv2.TokenList.get(index).C_p.equals("$")) {
                return true;
            } else if (tv2.TokenList.get(index).C_p.equals("AM")) {
                if (S()) {
                    if (tv2.TokenList.get(index).C_p.equals("$")) {
                        return true;
                    }
                }
            }

        }

        return false;
    }

    private boolean list_param() {
        if (tv2.TokenList.get(index).C_p.equals("[")) {
            index++;

            if (tv2.TokenList.get(index).C_p.equals("]")) {
                index++;
                return true;
            }
        } else if (tv2.TokenList.get(index).C_p.equals("ID")) {
            return true;

        }

        return false;
    }

    private boolean Expression() {

        //FIRST(<Expresssion>) = { ID, num_Cons , point_Cons , word_Cons , Char_Cons , Boolean_cons , NOT , ( , U_Opr  }
        if (tv2.TokenList.get(index).C_p.equals("ID")
                || tv2.TokenList.get(index).C_p.equals("num_Cons")
                || tv2.TokenList.get(index).C_p.equals("point_Cons")
                || tv2.TokenList.get(index).C_p.equals("word_Cons")
                || tv2.TokenList.get(index).C_p.equals("Char_Cons")
                || tv2.TokenList.get(index).C_p.equals("Boolean_Cons")
                || tv2.TokenList.get(index).C_p.equals("U_Opr")
                || tv2.TokenList.get(index).V_p.equals("NOT")
                || tv2.TokenList.get(index).C_p.equals("(")) {
            //<Expression>  <OR_Expression>
            if (OR_Expression()) {

                return true;
            }
        }
        return false;
    }

    void insert(String N, String type, String rType, int Scope) {
        Li.add(new SymbolTable(N, type, rType, Scope));
    }

    String Compare(String T1, String T2, String Op) {
        String rType = null;
        if (T2 != null) {
            ArrayList<String> ArthOp = new ArrayList<String>();
            ArrayList<String> AssgnOp = new ArrayList<String>();
            ArrayList<String> RelOp = new ArrayList<String>();
            ArthOp.add("+");
            ArthOp.add("-");
            ArthOp.add("*");
            ArthOp.add("/");
            ArthOp.add("%");

            AssgnOp.add("=");
            AssgnOp.add("+=");
            AssgnOp.add("-=");
            AssgnOp.add("*=");
            AssgnOp.add("/=");
            AssgnOp.add("%=");

            RelOp.add("<");
            RelOp.add(">");
            RelOp.add("<=");
            RelOp.add(">=");
            RelOp.add("==");
            RelOp.add("!=");
            RelOp.add("===");

            if (ArthOp.contains(Op) || AssgnOp.contains(Op)) {

                if (T1 == "num" && T2 == "num") {
                    if (Op == "/") {
                        rType = "point";
                    } else {
                        rType = "num";
                    }
                } else if ((T1 == "num" || T1 == "point") && (T2 == "num" || T2 == "point")) {
                    if (ArthOp.contains(Op)) {
                        rType = "point";
                    } else {
                        rType = T1;
                    }
                }
            } else if (Op == "=") {
                if (T1 == T2) {
                    rType = T1;
                }

            } else if (RelOp.contains(Op)) {
                if (T1 == T2) {
                    rType = "Boolean";
                }
            } else if (Op == "AND" || Op == "OR") {
                if (T1 == "Boolean" && T2 == "Boolean") {
                    rType = "Boolean";
                }
            }
        } else {
            if (Op == "NOT") {
                if (T1 == "Boolean") {
                    rType = "Boolean";
                }
            }
        }
        return rType;
    }

    String lookUp(String N, String pType, char type) {
        String ret = null;
        if (type == 'd') {
            for (SymbolTable s : Li) {
                if (s.name == N) {
                    if (s.scope == currScope) {
                        return s.type;
                    }
                }
            }
        } else if (type == 'a') {
            for (SymbolTable s : Li) {
                if (s.name == N) {
                    if (stack.contains(s.scope)) {
                        if (s.rType == null) {
                            return s.type;
                        }
                    }
                }
            }
        } else if (type == 'f') {
            for (SymbolTable s : Li) {
                if (s.name == N) {
                    if (s.type == pType) {
                        if (type == 'f') //bund mara hua hai
                        {
                            return "";
                        } else {
                            if (s.scope != -1) {
                                return s.rType;
                            }
                        }
                    }
                }
            }
        }
        return ret;
    }

    private boolean OR_Expression() {

        //FIRST(<OR_Expression>) = { ID, num_Cons , point_Cons , word_Cons , Char_Cons , Boolean_cons , NOT , ( , U_Opr  }
        if (tv2.TokenList.get(index).C_p.equals("ID")
                || tv2.TokenList.get(index).C_p.equals("num_Cons")
                || tv2.TokenList.get(index).C_p.equals("point_Cons")
                || tv2.TokenList.get(index).C_p.equals("word_Cons")
                || tv2.TokenList.get(index).C_p.equals("Char_Cons")
                || tv2.TokenList.get(index).C_p.equals("Boolean_Cons")
                || tv2.TokenList.get(index).V_p.equals("NOT")
                || tv2.TokenList.get(index).C_p.equals("U_Opr")
                || tv2.TokenList.get(index).C_p.equals("(")) {
            //<OR_Expression>  <AND_Expression> <OR_Expression2>
            if (AND_Expression()) {
                if (OR_Expression2()) {
                    return true;
                }
            }
        }
        return false;
    }

    private boolean OR_Expression2() {

        //FIRST(<OR_Expression2>) = {OR , Null}
        if (tv2.TokenList.get(index).V_p.equals("OR")) {
            //<OR_Expresson2>  OR <AND_Expression> <OR_Expresssion2> | Null
            if (tv2.TokenList.get(index).V_p.equals("OR")) {
                index++;
                if (AND_Expression()) {

                    if (OR_Expression2()) {

                        return true;
                    }
                }
            }
        } //FOLLOW(<OR_Expression2>) = { ,  , ) , } , ] , ;}
        else if (tv2.TokenList.get(index).C_p.equals(",")
                || tv2.TokenList.get(index).C_p.equals("}")
                || tv2.TokenList.get(index).C_p.equals("]")
                || tv2.TokenList.get(index).C_p.equals(";")
                || tv2.TokenList.get(index).C_p.equals(")")) {
            return true;
        }
        return false;
    }

    private boolean AND_Expression() {

        //FIRST(<AND_Expression>) = { ID, num_Cons , point_Cons , word_Cons , Char_Cons , Boolean_cons, U_Opr, NOT, ( }
        if (tv2.TokenList.get(index).C_p.equals("ID")
                || tv2.TokenList.get(index).C_p.equals("num_Cons")
                || tv2.TokenList.get(index).C_p.equals("point_Cons")
                || tv2.TokenList.get(index).C_p.equals("word_Cons")
                || tv2.TokenList.get(index).C_p.equals("Char_Cons")
                || tv2.TokenList.get(index).C_p.equals("Boolean_Cons")
                || tv2.TokenList.get(index).C_p.equals("U_Opr")
                || tv2.TokenList.get(index).V_p.equals("NOT")
                || tv2.TokenList.get(index).C_p.equals("(")) {
            //<AND_Expression>  <R_Opr> <AND_Expression2>

            if (R_Opr()) {
                if (AND_Expression2()) {

                    return true;
                }
            }
        }
        return false;
    }

    private boolean AND_Expression2() {
        //FIRST(<AND_Expression2>) = {AND , Null}
        if (tv2.TokenList.get(index).V_p.equals("AND")) {
            //<AND_Expression2>  AND <R_Opr> <AND_Expression2> | Null
            if (tv2.TokenList.get(index).V_p.equals("AND")) {
                index++;
                if (R_Opr()) {

                    if (AND_Expression2()) {

                        return true;
                    }
                }
            }
        } ///FOLLOW(<AND_Exp2>) = {OR, ,  , ) , } , ] , ;}
        else if (tv2.TokenList.get(index).V_p.equals("OR")
                || tv2.TokenList.get(index).C_p.equals(",")
                || tv2.TokenList.get(index).C_p.equals(";")
                || tv2.TokenList.get(index).C_p.equals("}")
                || tv2.TokenList.get(index).C_p.equals("]")
                || tv2.TokenList.get(index).C_p.equals(")")) {
            return true;
        }
        return false;
    }

    private boolean R_Opr() {
        //FIRST(<R_Opr>) = { ID, num_Cons , point_Cons , word_Cons , Char_Cons , Boolean_cons, NOT, U_Opr, (  }
        if (tv2.TokenList.get(index).C_p.equals("ID")
                || tv2.TokenList.get(index).C_p.equals("num_Cons")
                || tv2.TokenList.get(index).C_p.equals("point_Cons")
                || tv2.TokenList.get(index).C_p.equals("word_Cons")
                || tv2.TokenList.get(index).C_p.equals("Char_Cons")
                || tv2.TokenList.get(index).C_p.equals("Boolean_Cons")
                || tv2.TokenList.get(index).C_p.equals("U_Opr")
                || tv2.TokenList.get(index).V_p.equals("NOT")
                || tv2.TokenList.get(index).C_p.equals("(")) {
            //<R_Opr>  <E> <R_Opr2>
            if (E()) {
                if (R_Opr2()) {

                    return true;
                }
            }
        }
        return false;
    }

    private boolean R_Opr2() {

        //FIRST(<R_Opr2>) = {R_Opr , Null}
        if (tv2.TokenList.get(index).C_p.equals("R_Opr")) {
            //<R_Opr2>  R_Opr <E> <R_Opr2> | Null
            if (tv2.TokenList.get(index).C_p.equals("R_Opr")) {
                index++;
                if (E()) {

                    if (R_Opr2()) {

                        return true;
                    }
                }
            }
        } ////FOLLOW(<R_Opr2>) = {AND, OR, ,  , ) , } , ] , ;}
        else if (tv2.TokenList.get(index).V_p.equals("AND")
                || tv2.TokenList.get(index).V_p.equals("OR")
                || tv2.TokenList.get(index).C_p.equals(",")
                || tv2.TokenList.get(index).C_p.equals(")")
                || tv2.TokenList.get(index).C_p.equals("}")
                || tv2.TokenList.get(index).C_p.equals("]")
                || tv2.TokenList.get(index).C_p.equals(";")) {
            return true;
        }
        return false;
    }

    private boolean E() {
        //FIRST(<E>) = { ID, num_Cons , point_Cons , word_Cons , Char_Cons , Boolean_cons, U_Opr, (, NOT  }
        if (tv2.TokenList.get(index).C_p.equals("ID")
                || tv2.TokenList.get(index).C_p.equals("num_Cons")
                || tv2.TokenList.get(index).C_p.equals("point_Cons")
                || tv2.TokenList.get(index).C_p.equals("word_Cons")
                || tv2.TokenList.get(index).C_p.equals("Char_Cons")
                || tv2.TokenList.get(index).C_p.equals("Boolean_Cons")
                || tv2.TokenList.get(index).C_p.equals("U_Opr")
                || tv2.TokenList.get(index).V_p.equals("NOT")
                || tv2.TokenList.get(index).C_p.equals("(")) {
            //<E>  <T> <E2>
            if (T()) {
                if (E2()) {

                    return true;
                }
            }
        }
        return false;
    }

    private boolean E2() {
        //FIRST(<E2 >) = {PM , Null}
        if (tv2.TokenList.get(index).C_p.equals("PM")) {
            //<E2 >  PM <T > <E2> | Null
            if (tv2.TokenList.get(index).C_p.equals("PM")) {
                index++;
                if (T()) {
                    if (E2()) {

                        return true;
                    }
                }
            }
        } //FOLLOW(<E2>) = {R_Opr , AND , OR, ,  , ) , } , ] , ;}
        else if (tv2.TokenList.get(index).C_p.equals("R_Opr")
                || tv2.TokenList.get(index).V_p.equals("AND")
                || tv2.TokenList.get(index).V_p.equals("OR")
                || tv2.TokenList.get(index).C_p.equals(",")
                || tv2.TokenList.get(index).C_p.equals(")")
                || tv2.TokenList.get(index).C_p.equals("}")
                || tv2.TokenList.get(index).C_p.equals("]")
                || tv2.TokenList.get(index).C_p.equals(";")) {
            return true;
        }
        return false;
    }

    private boolean T() {
        //FIRST(<T>) = { ID, num_Cons , point_Cons , word_Cons , Char_Cons , Boolean_cons, U_Opr, (, NOT  }
        if (tv2.TokenList.get(index).C_p.equals("ID")
                || tv2.TokenList.get(index).C_p.equals("num_Cons")
                || tv2.TokenList.get(index).C_p.equals("point_Cons")
                || tv2.TokenList.get(index).C_p.equals("word_Cons")
                || tv2.TokenList.get(index).C_p.equals("Char_Cons")
                || tv2.TokenList.get(index).C_p.equals("Boolean_Cons")
                || tv2.TokenList.get(index).C_p.equals("U_Opr")
                || tv2.TokenList.get(index).V_p.equals("NOT")
                || tv2.TokenList.get(index).C_p.equals("(")) {
            //<T>  <F> <T2>
            if (F()) {
                if (T2()) {

                    return true;
                }
            }
        }
        return false;
    }

    private boolean T2() {
        //FIRST(<T2>) = { MDM , Null}
        if (tv2.TokenList.get(index).C_p.equals("MDM")) {
            //<T2>  MDM <F> <T2> | Null
            if (tv2.TokenList.get(index).C_p.equals("MDM")) {
                index++;
                if (F()) {

                    if (T2()) {

                        return true;
                    }
                }
            }
        } //FOLLOW(<T2>) = { PM, R_Opr , AND ,OR , ,  , ) , } , ] , ;}
        else if (tv2.TokenList.get(index).C_p.equals("PM")
                || tv2.TokenList.get(index).C_p.equals("R_Opr")
                || tv2.TokenList.get(index).V_p.equals("AND")
                || tv2.TokenList.get(index).V_p.equals("OR")
                || tv2.TokenList.get(index).C_p.equals(",")
                || tv2.TokenList.get(index).C_p.equals(")")
                || tv2.TokenList.get(index).C_p.equals("}")
                || tv2.TokenList.get(index).C_p.equals("]")
                || tv2.TokenList.get(index).C_p.equals(";")) {

            return true;
        }
        return false;
    }

    private boolean F9() {

        //FIRST(<F9>) = { . , [, U_Opr, (, NULL }
        //<F9> .ID <F10> | [<Expression>] <Member_exp3> | <Id_Op> | Null
        if (tv2.TokenList.get(index).C_p.equals(".")
                || (tv2.TokenList.get(index).C_p.equals("["))
                || (tv2.TokenList.get(index).C_p.equals("U_Opr"))
                || (tv2.TokenList.get(index).C_p.equals("("))) {
            if (tv2.TokenList.get(index).C_p.equals(".")) {

                index++;
                if (tv2.TokenList.get(index).C_p.equals("ID")) {

                    index++;
                    if (F10()) {
                        return true;
                    }
                }
            } else if (tv2.TokenList.get(index).C_p.equals("[")) {
                index++;
                if (Expression()) {
                    if (tv2.TokenList.get(index).C_p.equals("]")) {
                        index++;
                        if (Member_exp3()) {
                            return true;
                        }
                    }
                }
            } else if (Id_Op()) {
                return true;
            }

        }//FOLLOW(F9)-> {MDM, PM, R_Opr, AND, OR, , , ; , ] , ), } }
        else if ((tv2.TokenList.get(index).C_p.equals("MDM"))
                || (tv2.TokenList.get(index).C_p.equals("PM"))
                || (tv2.TokenList.get(index).C_p.equals("R_Opr"))
                || (tv2.TokenList.get(index).C_p.equals("AND"))
                || (tv2.TokenList.get(index).C_p.equals("OR"))
                || (tv2.TokenList.get(index).C_p.equals(","))
                || (tv2.TokenList.get(index).C_p.equals(";"))
                || (tv2.TokenList.get(index).C_p.equals("]"))
                || (tv2.TokenList.get(index).C_p.equals("}"))
                || (tv2.TokenList.get(index).C_p.equals(")"))) {
            return true;
        }

        return false;
    }

    private boolean F11() {
        // FIRST <F11> -> {[, (}
        // <F11> -> [<Expression>] <Member_exp3> | <Method_Call_1> <F12>

        if ((tv2.TokenList.get(index).C_p.equals("["))
                || (tv2.TokenList.get(index).C_p.equals("("))) {
            if (tv2.TokenList.get(index).C_p.equals("[")) {
                index++;
                if (Expression()) {
                    if (tv2.TokenList.get(index).C_p.equals("]")) {
                        index++;
                        if (Member_exp3()) {
                            return true;
                        }
                    }
                }
            } else if (Method_Call_1()) {
                if (F12()) {
                    return true;
                }
            }

        }

        return false;
    }

    private boolean Member_exp3() {

        // FIRST <Member_exp3> -> {. , Null}
        // <Member_exp3> -> .ID <F10> | Null
        if (tv2.TokenList.get(index).C_p.equals(".")) {
            if (tv2.TokenList.get(index).C_p.equals(".")) {
                index++;
                if (tv2.TokenList.get(index).C_p.equals("ID")) {
                    index++;
                    if (F10()) {
                        return true;
                    }
                }
            }
        } //FOLLOW(Member_exp3)-> {MDM, PM, R_Opr, AND, OR, , , ; , ] , ), } }
        else if ((tv2.TokenList.get(index).C_p.equals("MDM"))
                || (tv2.TokenList.get(index).C_p.equals("PM"))
                || (tv2.TokenList.get(index).C_p.equals("R_Opr"))
                || (tv2.TokenList.get(index).C_p.equals("AND"))
                || (tv2.TokenList.get(index).C_p.equals("OR"))
                || (tv2.TokenList.get(index).C_p.equals(","))
                || (tv2.TokenList.get(index).C_p.equals(";"))
                || (tv2.TokenList.get(index).C_p.equals("]"))
                || (tv2.TokenList.get(index).C_p.equals("}"))
                || (tv2.TokenList.get(index).C_p.equals(")"))) {
            return true;
        }

        return false;
    }

    private boolean F12() {
        //<F12>-> . <F>
        if (tv2.TokenList.get(index).C_p.equals(".")) {
            index++;
            if (F()) {
                return true;
            }
        } //FOLLOW(F12)-> {MDM, PM, R_Opr, AND, OR, , , ; , ] , ), } }
        else if ((tv2.TokenList.get(index).C_p.equals("MDM"))
                || (tv2.TokenList.get(index).C_p.equals("PM"))
                || (tv2.TokenList.get(index).C_p.equals("R_Opr"))
                || (tv2.TokenList.get(index).C_p.equals("AND"))
                || (tv2.TokenList.get(index).C_p.equals("OR"))
                || (tv2.TokenList.get(index).C_p.equals(","))
                || (tv2.TokenList.get(index).C_p.equals(";"))
                || (tv2.TokenList.get(index).C_p.equals("]"))
                || (tv2.TokenList.get(index).C_p.equals("}"))
                || (tv2.TokenList.get(index).C_p.equals(")"))) {
            return true;
        }
        return false;
    }

    private boolean F10() {
        // FIRST <F10> -> {., [, (, Null}
        // <F10> -> <F11> | .ID <F11> | Null

        if (tv2.TokenList.get(index).C_p.equals(".")
                || (tv2.TokenList.get(index).C_p.equals("["))
                || (tv2.TokenList.get(index).C_p.equals("("))) {
            if (F11()) {
                return true;
            } else if (tv2.TokenList.get(index).C_p.equals(".")) {
                index++;
                if (tv2.TokenList.get(index).C_p.equals("ID")) {
                    index++;
                    if (F11()) {
                        return true;
                    }

                }
            }

        } //FOLLOW(F10)-> {MDM, PM, R_Opr, AND, OR, , , ; , ] , ), } }
        else if ((tv2.TokenList.get(index).C_p.equals("MDM"))
                || (tv2.TokenList.get(index).C_p.equals("PM"))
                || (tv2.TokenList.get(index).C_p.equals("R_Opr"))
                || (tv2.TokenList.get(index).C_p.equals("AND"))
                || (tv2.TokenList.get(index).C_p.equals("OR"))
                || (tv2.TokenList.get(index).C_p.equals(","))
                || (tv2.TokenList.get(index).C_p.equals(";"))
                || (tv2.TokenList.get(index).C_p.equals("]"))
                || (tv2.TokenList.get(index).C_p.equals("}"))
                || (tv2.TokenList.get(index).C_p.equals(")"))) {
            return true;
        }

        return false;
    }

    private boolean F() {

        //FIRST(<F>) = { ID, num_Cons , point_Cons , word_Cons , Char_Cons , Boolean_cons , NOT , ( , U_Opr }
        if (tv2.TokenList.get(index).C_p.equals("ID")
                || tv2.TokenList.get(index).C_p.equals("num_Cons")
                || tv2.TokenList.get(index).C_p.equals("point_Cons")
                || tv2.TokenList.get(index).C_p.equals("word_Cons")
                || tv2.TokenList.get(index).C_p.equals("Char_Cons")
                || tv2.TokenList.get(index).C_p.equals("Boolean_Cons")
                || tv2.TokenList.get(index).C_p.equals("U_Opr")
                || tv2.TokenList.get(index).V_p.equals("NOT")
                || tv2.TokenList.get(index).C_p.equals("(")) {

            //<F> ID <F9> | <Const> |NOT <F> | (<Expression>) | U_Opr  ID <U_Opr_list> 
            if (tv2.TokenList.get(index).C_p.equals("ID")) {

                index++;
                if (F9()) {
                    return true;
                }
            } else if (Const()) {
                return true;
            } else if (tv2.TokenList.get(index).V_p.equals("NOT")) {

                index++;
                if (F()) {
                    return true;
                }
            } else if (tv2.TokenList.get(index).C_p.equals("(")) {
                index++;
                if (Expression()) {
                    if (tv2.TokenList.get(index).C_p.equals(")")) {
                        index++;
                        return true;
                    }
                }
            } else if (tv2.TokenList.get(index).C_p.equals("U_Opr")) {
                index++;
                if (tv2.TokenList.get(index).C_p.equals("ID")) {

                    index++;
                    if (U_Opr_list()) {
                        return true;
                    }
                }
            }

        }
        return false;
    }

    private boolean Const() {

        //FIRST(<Const>) = {  num_Cons , point_Cons , word_Cons , Char_Cons , Boolean_cons  }
        if (tv2.TokenList.get(index).C_p.equals("num_Cons")
                || tv2.TokenList.get(index).C_p.equals("word_Cons")
                || tv2.TokenList.get(index).C_p.equals("point_Cons")
                || tv2.TokenList.get(index).C_p.equals("Char_Cons")
                || tv2.TokenList.get(index).C_p.equals("Boolean_Cons")) {
            //<Const>    num_Cons , point_Cons , word_Cons , Char_Cons , Boolean_cons 
            if (tv2.TokenList.get(index).C_p.equals("num_Cons")
                    || tv2.TokenList.get(index).C_p.equals("word_Cons")
                    || tv2.TokenList.get(index).C_p.equals("point_Cons")
                    || tv2.TokenList.get(index).C_p.equals("Char_Cons")
                    || tv2.TokenList.get(index).C_p.equals("Boolean_Cons")) {

                index++;

                return true;
            }
        }
        return false;
    }

    private boolean Declaration() {

        //FIRST(<Declaration>) = { DT}
        if (tv2.TokenList.get(index).C_p.equals("DT")) {
            //<Declaration>  DT <Variable_Link> <List>
            if (tv2.TokenList.get(index).C_p.equals("DT")) {

                index++;
                if (Variable_Link()) {
                    if (List()) {
                        return true;
                    }
                }
            }
        }
        return false;
    }

    private boolean Variable_Link() {

        //FIRST(<Variable_Link>) = {ID} 
        if (tv2.TokenList.get(index).C_p.equals("ID")) {
            //<Variable_Link>  ID <Init>
            if (tv2.TokenList.get(index).C_p.equals("ID")) {

                index++;
                if (Init()) {
                    return true;
                }
            }
        }
        return false;
    }

    private boolean Init() {
        //FIRST(<Init>) = {=, Null}
        if (tv2.TokenList.get(index).V_p.equals("=")) {
            //<Init>  = <Variable_Link2> | Null
            if (tv2.TokenList.get(index).V_p.equals("=")) {
                index++;
                if (Variable_Link2()) {
                    return true;
                }

            }
            //FOLLOW(<Init>) = {,, ;}
        } else if (tv2.TokenList.get(index).V_p.equals(",")
                || tv2.TokenList.get(index).V_p.equals(";")) {
            return true;
        }

        return false;
    }

    private boolean Variable_Link2() {

        //FIRST(<Variable_Link2>  ) = {ID,<cons> ,<cons_exp>, !}
        if (tv2.TokenList.get(index).C_p.equals("num_Cons")
                || tv2.TokenList.get(index).C_p.equals("ID")
                || tv2.TokenList.get(index).C_p.equals("point_Cons")
                || tv2.TokenList.get(index).C_p.equals("word_Cons")
                || tv2.TokenList.get(index).C_p.equals("Char_Cons")
                || tv2.TokenList.get(index).C_p.equals("Boolean_Cons")
                || tv2.TokenList.get(index).V_p.equals("NOT")) {
            //<Variable_Link2>   ID <Init>| <Const> | <const_Exp>
            if (tv2.TokenList.get(index).C_p.equals("ID")) {
                index++;
                if (Init()) {
                    return true;
                }
            } else if (Const()) {
                return true;
            } else if (Const_Exp()) {
                return true;
            }
        }
        return false;
    }

    private boolean Id_Op() {

        //FIRST(<id_op>) = { Null , ( , . , U_Opr}
        if (tv2.TokenList.get(index).C_p.equals(".")
                || tv2.TokenList.get(index).C_p.equals("U_Opr")
                || tv2.TokenList.get(index).C_p.equals("(")) {
            //<id_op>  Null | <Method_Call_1> | <Member_exp> |  U_Opr

            if (Method_Call_1()) {

                return true;
            } else if (Member_exp()) {
                return true;
            } else if (tv2.TokenList.get(index).C_p.equals("U_Opr")) {

                index++;

                return true;
            }
        } ////FOLLOW(<id_op>) = {MDM , PM , R_Opr , AND , OR , ,  , ) , } , ] , ;, AM, DT , Void, ID ,Static, Class}
        else if (tv2.TokenList.get(index).C_p.equals("MDM")
                || tv2.TokenList.get(index).C_p.equals("PM")
                || tv2.TokenList.get(index).C_p.equals("R_Opr")
                || tv2.TokenList.get(index).V_p.equals("AND")
                || tv2.TokenList.get(index).V_p.equals("OR")
                || tv2.TokenList.get(index).C_p.equals(",")
                || tv2.TokenList.get(index).C_p.equals(")")
                || tv2.TokenList.get(index).C_p.equals("}")
                || tv2.TokenList.get(index).C_p.equals("]")
                || tv2.TokenList.get(index).C_p.equals("DT")
                || tv2.TokenList.get(index).C_p.equals("Void")
                || tv2.TokenList.get(index).C_p.equals("Class")
                || tv2.TokenList.get(index).C_p.equals("Static")
                || tv2.TokenList.get(index).C_p.equals("AM")
                || tv2.TokenList.get(index).C_p.equals("ID")
                || tv2.TokenList.get(index).C_p.equals(";")) {

            return true;
        }
        return false;
    }

    private boolean Member_exp() {
        //FIRST(<Member_exp>) = { . }
        if (tv2.TokenList.get(index).C_p.equals(".")) {
            //<Member_exp> -> .ID < Member_exp_2>
            if (tv2.TokenList.get(index).C_p.equals(".")) {

                index++;
                if (tv2.TokenList.get(index).C_p.equals("ID")) {

                    index++;
                    if (Member_exp_2()) {
                        return true;
                    }
                }
            }
        }
        return false;
    }

    private boolean Member_exp_2() {

        //FIRST(< Member_exp_2>) = {Null , ( , [}
        if (tv2.TokenList.get(index).C_p.equals("(")
                || tv2.TokenList.get(index).C_p.equals("[")) {
            //< Member_exp_2> -> Null | <Method_Call_1> | [<Exp>]
            if (Method_Call_1()) {

                return true;
            } else if (tv2.TokenList.get(index).C_p.equals("[")) {

                index++;

                if (Expression()) {
                    if (tv2.TokenList.get(index).C_p.equals("]")) {

                        index++;
                        return true;
                    }
                }
            }
        } //FOLLOW(<Member_exp2>) = {MDM , PM , R_Opr , AND ,OR , ,  , ) , } , ] , ;}
        else if (tv2.TokenList.get(index).C_p.equals("MDM")
                || tv2.TokenList.get(index).C_p.equals("PM")
                || tv2.TokenList.get(index).C_p.equals("R_Opr")
                || tv2.TokenList.get(index).V_p.equals("AND")
                || tv2.TokenList.get(index).V_p.equals("OR")
                || tv2.TokenList.get(index).C_p.equals(",")
                || tv2.TokenList.get(index).C_p.equals(")")
                || tv2.TokenList.get(index).C_p.equals("}")
                || tv2.TokenList.get(index).C_p.equals("]")
                || tv2.TokenList.get(index).C_p.equals(";")) {

            return true;
        }
        return false;
    }

    private boolean Const_Exp() {

        // FIRST(<Const_Exp>) = { ID, NOT}
        if (tv2.TokenList.get(index).C_p.equals("ID")
                || tv2.TokenList.get(index).V_p.equals("NOT")) {
            // <Const_Exp>   ID <Id_Op> | NOT <F> | ID [<Expression>] 
            if (tv2.TokenList.get(index).C_p.equals("ID")) {
                index++;
                if (tv2.TokenList.get(index).C_p.equals("[")) {
                    index++;
                    if (Expression()) {
                        if (tv2.TokenList.get(index).C_p.equals("]")) {
                            return true;
                        }
                    }
                }
            } else if (tv2.TokenList.get(index).V_p.equals("NOT")) {
                index++;
                if (F()) {
                    return true;
                }
            } else if (tv2.TokenList.get(index).C_p.equals("ID")) {
                index++;
                if (Id_Op()) {
                    return true;
                }
            }
        }
        return false;
    }

    private boolean List() {

        //FIRST(<List >) = {, , ;}
        if (tv2.TokenList.get(index).C_p.equals(",")
                || tv2.TokenList.get(index).C_p.equals(";")) {
            //<List >  , ID <Variable_Link2> | ;
            if (tv2.TokenList.get(index).C_p.equals(",")) {

                index++;

                if (tv2.TokenList.get(index).C_p.equals("ID")) {

                    index++;
                    if (Variable_Link2()) {
                        return true;
                    }
                }
            } else if (tv2.TokenList.get(index).C_p.equals(";")) {
                index++;
                return true;
            }
        }
        return false;
    }

    private boolean Ass_Opr() {

        //FIRST(<Ass_Opr>) = { = }
        if (tv2.TokenList.get(index).V_p.equals("=")) {
            //<Ass_opr>   = <Ass_opr2>      	
            if (tv2.TokenList.get(index).V_p.equals("=")) {
                index++;
                if (Ass_Opr2()) {
                    return true;
                }
            }
        }
        return false;
    }

    private boolean Ass_Opr2() {

        //FIRST(<Ass_Opr2>) = { ID, num_Cons , point_Cons , word_Cons , Char_Cons , Boolean_Cons, U_Opr, NOT, ( }
        if (tv2.TokenList.get(index).C_p.equals("num_Cons")
                || tv2.TokenList.get(index).C_p.equals("ID")
                || tv2.TokenList.get(index).C_p.equals("point_Cons")
                || tv2.TokenList.get(index).C_p.equals("word_Cons")
                || tv2.TokenList.get(index).C_p.equals("Char_Cons")
                || tv2.TokenList.get(index).V_p.equals("NOT")
                || tv2.TokenList.get(index).C_p.equals("U_Opr")
                || tv2.TokenList.get(index).C_p.equals("(")
                || tv2.TokenList.get(index).C_p.equals("Boolean_Cons")) {
            //<Ass_Opr2>  <Expression>;
            if (Expression()) {

                if (tv2.TokenList.get(index).C_p.equals(";")) {
                    index++;
                    return true;
                }
            }
        }
        return false;
    }

    private boolean While() {

        //FIRST(<While>) = {While}
        if (tv2.TokenList.get(index).C_p.equals("While")) {
            //<While>  While (<Expression>) <Body>
            if (tv2.TokenList.get(index).C_p.equals("While")) {
                index++;
                if (tv2.TokenList.get(index).C_p.equals("(")) {
                    index++;
                    if (Expression()) {

                        if (tv2.TokenList.get(index).C_p.equals(")")) {
                            index++;
                            if (Body()) {
                                return true;
                            }
                        }
                    }
                }
            }
        }

        return false;
    }

    private boolean S_Dt() {

        //FIRST(<S_DT>) = {ID , [}
        if (tv2.TokenList.get(index).C_p.equals("ID")
                || tv2.TokenList.get(index).C_p.equals("[")) {
            //<S_DT>  ID <ID_2> | <Arr_Dec>
            if (tv2.TokenList.get(index).C_p.equals("ID")) {
                index++;
                if (ID_2()) {
                    return true;
                }
            } else if (Arr_Dec()) {
                return true;
            }

        }
        return false;
    }

    private boolean Body() {

        //FIRST(<Body>) = {; , { , If , DT , For , While , Return ,  U_Opr , ID , Break , Continue , }
        if (tv2.TokenList.get(index).C_p.equals("While")
                || tv2.TokenList.get(index).C_p.equals("DT")
                || tv2.TokenList.get(index).C_p.equals("For")
                || tv2.TokenList.get(index).C_p.equals("If")
                || tv2.TokenList.get(index).C_p.equals("Return")
                || tv2.TokenList.get(index).C_p.equals("U_Opr")
                || tv2.TokenList.get(index).C_p.equals("ID")
                || tv2.TokenList.get(index).C_p.equals("Break")
                || tv2.TokenList.get(index).C_p.equals("Continue")
                || tv2.TokenList.get(index).C_p.equals(";")
                || tv2.TokenList.get(index).C_p.equals("{")) {
            //<Body>  ; | <S_ST> | {<M_ST>}
            if (tv2.TokenList.get(index).C_p.equals(";")) {
                index++;
                return true;
            } else if (S_St()) {
                return true;
            } else if (tv2.TokenList.get(index).C_p.equals("{")) {
                index++;

                if (M_St()) {
                    if (tv2.TokenList.get(index).C_p.equals("}")) {
                        index++;
                        return true;
                    }
                }
            }
        }

        return false;
    }

    private boolean For() {
        int a;
        //FIRST(<For>) = {For}
        if (tv2.TokenList.get(index).C_p.equals("For")) {
            //<For>  For(<F1> <F2>; <F3>) <Body>
            if (tv2.TokenList.get(index).C_p.equals("For")) {
                index++;
                if (tv2.TokenList.get(index).C_p.equals("(")) {
                    index++;
                    if (F1()) {
                        if (F2()) {
                            if (tv2.TokenList.get(index).C_p.equals(";")) {
                                index++;
                                if (F3()) {
                                    if (tv2.TokenList.get(index).C_p.equals(")")) {

                                        index++;
                                        if (Body()) {
                                            return true;
                                        }
                                    }
                                }
                            }

                        }
                    }
                }
            }
        }
        return false;
    }

    private boolean F1() {
        //FIRST(<F1>) = {DT , ID , ;}
        if (tv2.TokenList.get(index).C_p.equals("ID")
                || tv2.TokenList.get(index).C_p.equals("DT")
                || tv2.TokenList.get(index).C_p.equals(";")) {
            //<F1>  <Declaration> |ID <Assign_Op> | ;
            if (Declaration()) {
                return true;
            } else if (tv2.TokenList.get(index).C_p.equals("ID")) {

                index++;
                if (Ass_Opr()) {
                    return true;
                }
            } else if (tv2.TokenList.get(index).C_p.equals(";")) {
                index++;
                return true;
            }
        }
        return false;
    }

    private boolean F2() {

        //FIRST(<F2>) = { ID, num_Cons , point_Cons , word_Cons , Char_Cons , Boolean_cons , Null }
        if (tv2.TokenList.get(index).C_p.equals("num_Cons")
                || tv2.TokenList.get(index).C_p.equals("ID")
                || tv2.TokenList.get(index).C_p.equals("point_Cons")
                || tv2.TokenList.get(index).C_p.equals("String_Cons")
                || tv2.TokenList.get(index).C_p.equals("Char_Cons")
                || tv2.TokenList.get(index).C_p.equals("Boolean_Cons")) {
            //<F2>  <Expression> <X> | Null

            if (Expression()) {

                if (X()) {
                    return true;
                }
            }
        } ////FOLLOW(<F2>) = { ; }
        else if (tv2.TokenList.get(index).C_p.equals(";")) {
            return true;
        }
        return false;
    }

    private boolean F3() {

        //FIRST(<F3>) = {U_Opr , ID , Null}
        if (tv2.TokenList.get(index).C_p.equals("U_Opr")
                || tv2.TokenList.get(index).C_p.equals("ID")) {
            //<F3>  U_Opr ID | ID <F4>| Null
            if (tv2.TokenList.get(index).C_p.equals("U_Opr")) {
                index++;
                if (tv2.TokenList.get(index).C_p.equals("ID")) {

                    index++;
                    return true;
                }
            } else if (tv2.TokenList.get(index).C_p.equals("ID")) {
                index++;
                if (F4()) {
                    return true;
                }
            }
        } ////FOLLOW(<F3>) = { ) }
        else if (tv2.TokenList.get(index).C_p.equals(")")) {

            return true;
        }
        return false;
    }

    private boolean F4() {

        //FIRST(<F4>) = {U_Opr , Ass_Opr}
        if (tv2.TokenList.get(index).C_p.equals("U_Opr")
                || tv2.TokenList.get(index).C_p.equals("Ass_Opr")) {
            //<F4>  inc_dec | Ass_Opr <Expression>
            if (tv2.TokenList.get(index).C_p.equals("U_Opr")) {

                index++;
                return true;
            } else if (tv2.TokenList.get(index).C_p.equals("Ass_Opr")) {
                index++;
                if (Expression()) {
                    return true;
                }
            }
        }
        return false;
    }

    private boolean X() {

        //FIRST(<X>) = { , , Null}
        if (tv2.TokenList.get(index).C_p.equals(",")) {
            //<X>  , <Expression> <X> | Null
            index++;
            if (Expression()) {

                if (X()) {
                    return true;
                }
            }
        } ////FOLLOW(<X>) = { ; }
        else if (tv2.TokenList.get(index).C_p.equals(";")) {
            return true;
        }
        return false;
    }

    private boolean Return() {

        //FIRST(<Return>) = {Return}
        if (tv2.TokenList.get(index).C_p.equals("Return")) {
            //<Return> Return <Return2> 
            if (tv2.TokenList.get(index).C_p.equals("Return")) {
                index++;
                if (Return2()) {
                    return true;
                }
            }
        }
        return false;
    }

    private boolean Return2() {

        //FIRST(<Return2>) = { ; , ID, num_Cons , point_Cons , word_Cons , Char_Cons , Boolean_COns , NOT , ( , U_Opr }
        if (tv2.TokenList.get(index).C_p.equals(";")
                || tv2.TokenList.get(index).C_p.equals("num_Cons")
                || tv2.TokenList.get(index).C_p.equals("ID")
                || tv2.TokenList.get(index).C_p.equals("point_Cons")
                || tv2.TokenList.get(index).C_p.equals("word_Cons")
                || tv2.TokenList.get(index).C_p.equals("Char_Cons")
                || tv2.TokenList.get(index).C_p.equals("Boolean_Cons")
                || tv2.TokenList.get(index).V_p.equals("NOT")
                || tv2.TokenList.get(index).C_p.equals("(")
                || tv2.TokenList.get(index).C_p.equals("U_Opr")) {
            //<Return2>  ; | <Expression>;
            if (tv2.TokenList.get(index).C_p.equals(";")) {

                index++;
                return true;
            } else if (Expression()) {
                if (tv2.TokenList.get(index).C_p.equals(";")) {
                    index++;
                    return true;
                }
            }
        }
        return false;
    }

    private boolean Break() {
        //FIRST(<Break>) = {Break}
        if (tv2.TokenList.get(index).C_p.equals("Break")) {
            //<Break>  Break ;
            if (tv2.TokenList.get(index).C_p.equals("Break")) {
                index++;
                if (tv2.TokenList.get(index).C_p.equals(";")) {
                    index++;
                    return true;
                }
            }
        }
        return false;
    }

    private boolean Continue() {
        //FIRST(<Continue>) = {Continue}
        if (tv2.TokenList.get(index).C_p.equals("Continue")) {
            //<Continue>  Continue;
            if (tv2.TokenList.get(index).C_p.equals("Continue")) {
                index++;
                if (tv2.TokenList.get(index).C_p.equals(";")) {
                    index++;
                    return true;
                }
            }
        }
        return false;
    }

    private boolean S_Id() {

        ////FIRST(<S_Id>) = {U_Opr , = , ID , [ ,  .  , (  }
        if (tv2.TokenList.get(index).C_p.equals("U_Opr")
                || tv2.TokenList.get(index).V_p.equals("=")
                || tv2.TokenList.get(index).C_p.equals("ID")
                || tv2.TokenList.get(index).C_p.equals("(")
                || tv2.TokenList.get(index).C_p.equals("[")
                || tv2.TokenList.get(index).C_p.equals(".")) {

            //<S_Id>  <U_Opr>; | <Ass_Opr>| <Object_link> | <Object_Call>; | <Method_Call_1>; | [<Expression>] <Ass_Opr> | [ <Access_Array>	
            if (tv2.TokenList.get(index).C_p.equals("U_Opr")) {

                index++;
                if (tv2.TokenList.get(index).C_p.equals(";")) {
                    index++;
                    return true;
                }
            } else// if (tv2.TokenList.get(index).V_p.equals("=")) {
            if (Ass_Opr()) {//index++;
                return true;
            } else if (Object_link()) {

                return true;
            } else if (tv2.TokenList.get(index).C_p.equals("[")) {

                index++;
                if (Access_Array()) {
                    return true;
                }
            } else if (Object_Call()) {

                if (tv2.TokenList.get(index).C_p.equals(";")) {
                    index++;
                    return true;
                }
            } else if (Method_Call_1()) {
                if (tv2.TokenList.get(index).C_p.equals(";")) {
                    index++;
                    return true;
                }
            } else if (Object_link()) {
                return true;
            } else if (tv2.TokenList.get(index).C_p.equals("[")) {
                index++;

                if (Expression()) {
                    if (tv2.TokenList.get(index).C_p.equals("]")) {
                        index++;
                        if (Ass_Opr()) {
                            return true;
                        }
                    }
                }
            }

        }
        return false;
    }

    private boolean U_Opr_list() {
        //FIRST(<U_Opr_list>) = { [ , . , Null}
        if (tv2.TokenList.get(index).C_p.equals("[")
                || tv2.TokenList.get(index).C_p.equals(".")) {
            //<U_Opr_list>  [<Expression>] | .ID[<Expression>] |Null 
            if (tv2.TokenList.get(index).C_p.equals("[")) {

                index++;
                if (Expression()) {
                    if (tv2.TokenList.get(index).C_p.equals("]")) {

                        index++;
                        return true;
                    }
                }
            } else if (tv2.TokenList.get(index).C_p.equals(".")) {

                index++;
                if (tv2.TokenList.get(index).C_p.equals("ID")) {
                    index++;
                    if (tv2.TokenList.get(index).C_p.equals("[")) {

                        index++;
                        if (Expression()) {
                            if (tv2.TokenList.get(index).C_p.equals("]")) {

                                index++;

                                return true;
                            }
                        }
                    }
                }
            }
        } //FOLLOW(<U_Opr_list>) = {MDM , PM , R_Opr , AND ,OR , ,  , ) , } , ] , ;}
        else if (tv2.TokenList.get(index).C_p.equals("MDM")
                || tv2.TokenList.get(index).C_p.equals("PM")
                || tv2.TokenList.get(index).C_p.equals("R_Opr")
                || tv2.TokenList.get(index).C_p.equals("Cndtnl_Opr")
                || tv2.TokenList.get(index).C_p.equals(",")
                || tv2.TokenList.get(index).C_p.equals(")")
                || tv2.TokenList.get(index).C_p.equals("}")
                || tv2.TokenList.get(index).C_p.equals("]")
                || tv2.TokenList.get(index).C_p.equals(";")) {

            return true;
        }
        return false;
    }

    private boolean S_St() {
        //FIRST(<S_St>) = { while ,Dt  ,for, if, return, U_Opr, ID, Break, continue, <Const>, NOT, ( }
        if (tv2.TokenList.get(index).C_p.equals("While")
                || tv2.TokenList.get(index).C_p.equals("DT")
                || tv2.TokenList.get(index).C_p.equals("For")
                || tv2.TokenList.get(index).C_p.equals("If")
                || tv2.TokenList.get(index).C_p.equals("Return")
                || tv2.TokenList.get(index).C_p.equals("U_Opr")
                || tv2.TokenList.get(index).C_p.equals("ID")
                || tv2.TokenList.get(index).C_p.equals("Break")
                || tv2.TokenList.get(index).C_p.equals("Continue")
                || tv2.TokenList.get(index).C_p.equals("num_Cons")
                || tv2.TokenList.get(index).C_p.equals("point_Cons")
                || tv2.TokenList.get(index).C_p.equals("word_Cons")
                || tv2.TokenList.get(index).C_p.equals("Char_Cons")
                || tv2.TokenList.get(index).C_p.equals("Boolean_Cons")
                || tv2.TokenList.get(index).V_p.equals("NOT")
                || tv2.TokenList.get(index).C_p.equals("(")) {
            if (While()) {
                return true;
            } else if (tv2.TokenList.get(index).C_p.equals("DT")) {
                index++;
                if (S_Dt()) {
                    return true;
                }
            } else if (For()) {
                return true;
            } else if (If_Else()) {
                return true;
            } else if (Return()) {
                return true;
            } else if (tv2.TokenList.get(index).C_p.equals("ID")) {
                index++;
                if (S_Id()) {
                    return true;
                }
            } else if (Expression()) {
                if (tv2.TokenList.get(index).C_p.equals(";")) {
                    index++;
                    return true;
                }

            } else if (Break()) {
                return true;
            } else if (Continue()) {
                return true;
            }

        }

        return false;
    }

    private boolean M_St() {
        //FIRST(<M_St>) = { while ,Dt  ,for, if, return, U_Opr, ID, Break, continue, <Const>, NOT, ( }
        if (tv2.TokenList.get(index).C_p.equals("While")
                || tv2.TokenList.get(index).C_p.equals("DT")
                || tv2.TokenList.get(index).C_p.equals("For")
                || tv2.TokenList.get(index).C_p.equals("If")
                || tv2.TokenList.get(index).C_p.equals("Return")
                || tv2.TokenList.get(index).C_p.equals("U_Opr")
                || tv2.TokenList.get(index).C_p.equals("ID")
                || tv2.TokenList.get(index).C_p.equals("Break")
                || tv2.TokenList.get(index).C_p.equals("Continue")
                || tv2.TokenList.get(index).C_p.equals("num_Cons")
                || tv2.TokenList.get(index).C_p.equals("point_Cons")
                || tv2.TokenList.get(index).C_p.equals("word_Cons")
                || tv2.TokenList.get(index).C_p.equals("Char_Cons")
                || tv2.TokenList.get(index).C_p.equals("Boolean_Cons")
                || tv2.TokenList.get(index).V_p.equals("NOT")
                || tv2.TokenList.get(index).C_p.equals("(")) {
            //(<M_St>) = <S_St> <M_St> | Null
            if (S_St()) {
                if (M_St()) {
                    return true;
                }
            }
            //FOLLOW(<M_St>) = { } }
        } else if (tv2.TokenList.get(index).C_p.equals("}")) {
            return true;
        }

        return false;
    }

    private boolean If_Else() {

        if (tv2.TokenList.get(index).C_p.equals("If")) {
            //<If_Else>  If (<Expression>) {<M_St>} <O_Else>
            if (tv2.TokenList.get(index).C_p.equals("If")) {
                index++;
                if (tv2.TokenList.get(index).C_p.equals("(")) {
                    index++;
                    if (Expression()) {

                        if (tv2.TokenList.get(index).C_p.equals(")")) {
                            index++;
                            if (tv2.TokenList.get(index).C_p.equals("{")) {
                                index++;
                                if (M_St()) {
                                    if (tv2.TokenList.get(index).C_p.equals("}")) {
                                        index++;

                                        if (O_Else()) {
                                            return true;
                                        }
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }
        return false;
    }

    private boolean O_Else() {

        //FIRST(<O_Else>) = {Else, Null}
        if (tv2.TokenList.get(index).C_p.equals("Else")) {
            //<O_Else>  Else <If_Else> {<M_ST>}  |  Else{<M_ST>}  | Null
            index++;
            if (tv2.TokenList.get(index).C_p.equals("If")) {
                if (If_Else()) {
                    return true;
                }

            } else if (tv2.TokenList.get(index).C_p.equals("{")) {
                index++;

                if (M_St()) {
                    if (tv2.TokenList.get(index).C_p.equals("}")) {

                        index++;
                        return true;
                    }

                }
            }
        } //FOLLOW(<O_Else>) = { While , DT , For , If , Return ,  inc_dec , ID , Break , Continue, }}
        else if (tv2.TokenList.get(index).C_p.equals("While")
                || tv2.TokenList.get(index).C_p.equals("DT")
                || tv2.TokenList.get(index).C_p.equals("For")
                || tv2.TokenList.get(index).C_p.equals("If")
                || tv2.TokenList.get(index).C_p.equals("Return")
                || tv2.TokenList.get(index).C_p.equals("U_Opr")
                || tv2.TokenList.get(index).C_p.equals("ID")
                || tv2.TokenList.get(index).C_p.equals("Break")
                || tv2.TokenList.get(index).C_p.equals("Continue")
                || tv2.TokenList.get(index).C_p.equals("}")) {

            return true;
        }
        return false;
    }

    private boolean S() {
        AM.set("");
        //FIRST(<S>) = { AM, Class}
        if (tv2.TokenList.get(index).C_p.equals("AM")
                || tv2.TokenList.get(index).C_p.equals("Class")) {

            //<S>   <Access_Modiefir><Link_Class>
            if (Access_Modifier()) {
                if (Link_Class()) {
                    return true;
                }
            }
        }

        return false;
    }

    private boolean Access_Modifier() {

        //FIRST(<Access_Modifier>) = { AM, Null}
        if (tv2.TokenList.get(index).C_p.equals("AM")) {
            //<Access_Modifier>  AM | Null
            if (tv2.TokenList.get(index).C_p.equals("AM")) {

                index++;
                return true;
            }
        } //FOLLOW(<Access_Modifier>) = { Class , Static , DT ,Void ,ID  }
        else if (tv2.TokenList.get(index).C_p.equals("Class")
                || tv2.TokenList.get(index).C_p.equals("Static")
                || tv2.TokenList.get(index).C_p.equals("DT")
                || tv2.TokenList.get(index).C_p.equals("Void")
                || tv2.TokenList.get(index).C_p.equals("ID")) {

            return true;
        }

        return false;
    }

    private boolean Link_Class() {

        //FIRST(<Link_Class>) = {Class}
        if (tv2.TokenList.get(index).C_p.equals("Class")) {
            //<Link_Class>  class ID {<Class_Body>} | <S> 
            if (tv2.TokenList.get(index).C_p.equals("Class")) {
                index++;
                if (tv2.TokenList.get(index).C_p.equals("ID")) {
                    index++;

                    if (tv2.TokenList.get(index).C_p.equals("{")) {

                        index++;

                        if (Class_Body()) {
                            if (tv2.TokenList.get(index).C_p.equals("}")) {
                                index++;
                                return true;

                            }
                        }
                    }

                }
            }

        }

        // 
        return false;
    }

    private boolean Class_Body() {

        //FIRST(<Class_Body>) = { AM , static , DT ,void ,ID , class  , Null
        if (tv2.TokenList.get(index).C_p.equals("AM")
                || tv2.TokenList.get(index).C_p.equals("Class")
                || tv2.TokenList.get(index).C_p.equals("Static")
                || tv2.TokenList.get(index).C_p.equals("DT")
                || tv2.TokenList.get(index).C_p.equals("Void")
                || tv2.TokenList.get(index).C_p.equals("ID")) {

//<Class_Body>  <Class_Member> <Class_Body> | Null
            if (Class_Member()) {
                if (Class_Body()) {
                    return true;
                }
            }
        } //FOLLOW(<Class_Body>) = { } }
        else if (tv2.TokenList.get(index).C_p.equals("}")) {
            return true;
        }

        return false;
    }

    private boolean Class_Member() {

        //FIRST(<Class_ Member >) = { AM , static , DT ,void ,ID , class }
        if (tv2.TokenList.get(index).C_p.equals("AM")
                || tv2.TokenList.get(index).C_p.equals("Static")
                || tv2.TokenList.get(index).C_p.equals("DT")
                || tv2.TokenList.get(index).C_p.equals("Void")
                || tv2.TokenList.get(index).C_p.equals("ID")
                || tv2.TokenList.get(index).C_p.equals("Class")) {

            //<Class_Member>  <Access_Modifier><Member_Link>
            if (Access_Modifier()) {
                if (Member_Link()) {
                    return true;
                }
            }
        }
        return false;
    }

    private boolean Member_Link() {

        //FIRST(<Member_Link>) = { Static , DT ,void ,ID , class }
        if (tv2.TokenList.get(index).C_p.equals("Static")
                || tv2.TokenList.get(index).C_p.equals("DT")
                || tv2.TokenList.get(index).C_p.equals("Void")
                || tv2.TokenList.get(index).C_p.equals("ID")
                || tv2.TokenList.get(index).C_p.equals("Class")) {

            //<Member_Link>  Static <ST_P>| void ID <Method_Link1> | DT<DT_S> |ID <Constructor_obj_dec> |<Constructor_Dec> | <Link_class>
            if (tv2.TokenList.get(index).C_p.equals("Static")) {
                index++;

                if (ST_P()) {
                    return true;
                }
            } else if (tv2.TokenList.get(index).C_p.equals("Void")) {

                index++;
                if (tv2.TokenList.get(index).C_p.equals("ID")) {
                    index++;
                    if (Method_Link1()) {
                        return true;
                    }
                }
            } else if (tv2.TokenList.get(index).C_p.equals("DT")) {
                index++;
                if (DT_S()) {
                    return true;
                }
            } else if (tv2.TokenList.get(index).C_p.equals("ID")) {

                if (Constructor_obj_dec()) {
                    return true;
                }
            } else if (Link_Class()) {
                return true;
            } else if (Constructor_Dec()) {
                return true;
            }
        }
        return false;
    }

    private boolean Method_Link1() {

        //FIRST(<Method_Link1>) = { ( }
        if (tv2.TokenList.get(index).C_p.equals("(")) {
            //<Method_Link1>   (Parameter_List) {<M_St>} 
            if (tv2.TokenList.get(index).C_p.equals("(")) {

                index++;

                if (Parameter_List()) {
                    if (tv2.TokenList.get(index).C_p.equals(")")) {

                        index++;

                        if (tv2.TokenList.get(index).C_p.equals("{")) {
                            index++;

                            if (M_St()) {

                                if (tv2.TokenList.get(index).C_p.equals("}")) {
                                    index++;
                                    return true;
                                }
                            }
                        }
                    }
                }
            }
        }
        return false;
    }

    private boolean Parameter_List() {

        //FIRST(<Parameter_List>) = {DT , ID , Null}
        if (tv2.TokenList.get(index).C_p.equals("DT")
                || tv2.TokenList.get(index).C_p.equals("ID")) {
            //<Parameter_List>  DT <list_param_> ID <Parameter_List1> |  ID <list_param_> ID <Parameter_List1> | Null
            if (tv2.TokenList.get(index).C_p.equals("DT")) {

                index++;
                if (list_param()) {
                    if (tv2.TokenList.get(index).C_p.equals("ID")) {

                        index++;
                        if (Parameter_List1()) {
                            return true;
                        }
                    }
                }

            } else if (tv2.TokenList.get(index).C_p.equals("ID")) {
                index++;
                if (list_param()) {
                    if (tv2.TokenList.get(index).C_p.equals("ID")) {
                        index++;
                        if (Parameter_List1()) {
                            return true;
                        }
                    }
                }
            }

        } //FOLLOW(<Paramteter_List>) = { ) }
        else if (tv2.TokenList.get(index).C_p.equals(")")) {
            return true;
        }
        return false;
    }

    private boolean Parameter_List1() {

        //FIRST(<Parameter_List1>) = {, , Null}
        if (tv2.TokenList.get(index).C_p.equals(",")) {
            //<Parameter_List1> ,<Parameter_List> | Null
            if (tv2.TokenList.get(index).C_p.equals(",")) {
                index++;
                if (Parameter_List()) {
                    return true;
                }
            }
        } //FOLLOW(<Parameter_List1>) = { ) }//
        else if (tv2.TokenList.get(index).C_p.equals(")")) {
            return true;
        }
        return false;
    }

    private boolean Constructor_obj_dec() {

        ////FIRST(<Constructor_obj_dec>) = { ID}
        if (tv2.TokenList.get(index).C_p.equals("ID")) {
            //<Constructor_obj_dec>  <object_link> | <Constructor_Dec>
            if (Constructor_Dec()) {
                return true;
            } else if (Object_link()) {
                return true;
            }
        }
        return false;
    }

    private boolean DT_S() {
        //FIRST(<DT_S>) = {ID , [}
        if (tv2.TokenList.get(index).C_p.equals("ID")
                || tv2.TokenList.get(index).C_p.equals("[")) {
            //<DT_S> ID <ID_2>|< Arr_Dec>
            if (tv2.TokenList.get(index).C_p.equals("ID")) {
                String N = tv2.TokenList.get(index).V_p;
                index++;
                if (ID_2()) {
                    return true;
                }
            } else if (Arr_Dec()) {
                return true;
            }
        }
        return false;
    }

    private boolean Variable_Link3() {
        if (tv2.TokenList.get(index).C_p.equals(";")) {
            index++;
            return true;
        } else if (tv2.TokenList.get(index).C_p.equals(",")) {
            index++;
            if (DT_S()) {
                return true;
            }
        }
        return false;
    }

    private boolean ID_2() {

        //FIRST(<ID_2>) = {( , = , , , ; }
        if (tv2.TokenList.get(index).C_p.equals("(")
                || tv2.TokenList.get(index).V_p.equals("=")
                || tv2.TokenList.get(index).C_p.equals(",")
                || tv2.TokenList.get(index).C_p.equals(";")) {
            //<ID_2>=<Variable_Link2> <Variable_Link3> | <Method_Link1> | ,<DT_S> | ;
            if (tv2.TokenList.get(index).V_p.equals("=")) {
                index++;
                if (Variable_Link2()) {
                    if (Variable_Link3()) {

                        return true;
                    }
                }
            } else if (Method_Link1()) {
                return true;
            } else if (tv2.TokenList.get(index).C_p.equals(";")) {
                index++;
                return true;
            } else if (tv2.TokenList.get(index).C_p.equals(",")) {
                index++;
                if (DT_S()) {
                    return true;
                }
            }

        }
        return false;
    }

    private boolean ST_P() {

        //FIRST(<ST_P>) = {DT , Void}
        if (tv2.TokenList.get(index).C_p.equals("DT")
                || tv2.TokenList.get(index).C_p.equals("Void")) {
            //<ST_P>   DT <DT_S> |Void ID<Method_Link1> 
            if (tv2.TokenList.get(index).C_p.equals("DT")) {
                index++;
                if (DT_S()) {
                    return true;
                }
            } else if (tv2.TokenList.get(index).C_p.equals("Void")) {
                index++;
                if (tv2.TokenList.get(index).C_p.equals("ID")) {
                    index++;
                    if (Method_Link1()) {
                        return true;
                    }
                }
            }
        }

        return false;
    }

    private boolean Constructor_Dec() {

        //FIRST(<Constructor_Dec>) = {ID}
        if (tv2.TokenList.get(index).C_p.equals("ID")) {
            //<Constructor_Dec>   ID (<Parameter_List>) {<M-St>}
            if (tv2.TokenList.get(index).C_p.equals("ID")) {
                index++;

                if (tv2.TokenList.get(index).C_p.equals("(")) {

                    index++;
                    if (Parameter_List()) {
                        if (tv2.TokenList.get(index).C_p.equals(")")) {

                            index++;
                            if (tv2.TokenList.get(index).C_p.equals("{")) {

                                index++;

                                if (M_St()) {
                                    if (tv2.TokenList.get(index).C_p.equals("}")) {

                                        index++;
                                        return true;
                                    }
                                }
                            }
                        }
                    }
                }

            }

        }
        return false;
    }

    private boolean Access_Array() {
        //FIRST(<Access_Array >) = { ID, num_Cons , Char_Cons , word_Cons , Char_Cons, Boolean_Cons , NOT, ( , U_Opr  , ] }
        if (tv2.TokenList.get(index).C_p.equals("ID")
                || tv2.TokenList.get(index).C_p.equals("num_Cons")
                || tv2.TokenList.get(index).C_p.equals("Char_Cons")
                || tv2.TokenList.get(index).C_p.equals("word_Cons")
                || tv2.TokenList.get(index).C_p.equals("point_Cons")
                || tv2.TokenList.get(index).C_p.equals("Boolean_Cons")
                || tv2.TokenList.get(index).C_p.equals("U_Opr")
                || tv2.TokenList.get(index).C_p.equals("(")
                || tv2.TokenList.get(index).V_p.equals("NOT")
                || tv2.TokenList.get(index).C_p.equals("]")) {
            //<Access_Array>  <Expression>]<Ass_Opr> | ] ID<object_arr_dec>	

            if (Expression()) {
                if (tv2.TokenList.get(index).C_p.equals("]")) {

                    index++;
                    if (Ass_Opr()) {
                        return true;
                    }
                }
            } else if (tv2.TokenList.get(index).C_p.equals("]")) {

                index++;
                if (tv2.TokenList.get(index).C_p.equals("ID")) {
                    index++;
                    if (object_arr_dec()) {
                        return true;
                    }
                }
            }
        }

        return false;
    }

    private boolean Arr_Dec() {

        //FIRST(<Arr_Dec>) = {[}
        if (tv2.TokenList.get(index).C_p.equals("[")) {
            //<Arr_Dec>   [] ID <Array_Initialize>
            if (tv2.TokenList.get(index).C_p.equals("[")) {

                index++;
                if (tv2.TokenList.get(index).C_p.equals("]")) {

                    index++;

                    if (tv2.TokenList.get(index).C_p.equals("ID")) {

                        index++;

                        if (Array_Initialize()) {
                            return true;
                        }
                    }
                }
            }
        }
        return false;
    }

    private boolean Array_Initialize() {
        //FIRST(<Array_Initialize>) = {; , =}
        if (tv2.TokenList.get(index).C_p.equals(";")
                || tv2.TokenList.get(index).V_p.equals("=")) {
            //<IArray_Initialize>  ; | = new DT [<ID_Const>]<Array_const>
            if (tv2.TokenList.get(index).C_p.equals(";")) {
                index++;
                return true;
            } else if (tv2.TokenList.get(index).V_p.equals("=")) {
                index++;
                if (tv2.TokenList.get(index).C_p.equals("New")) {
                    index++;
                    if (tv2.TokenList.get(index).C_p.equals("DT")) {

                        index++;
                        if (tv2.TokenList.get(index).C_p.equals("[")) {

                            index++;
                            if (ID_Cons()) {
                                if (tv2.TokenList.get(index).C_p.equals("]")) {

                                    index++;
                                    if (Array_const()) {
                                        return true;
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }
        return false;
    }

    private boolean ID_Cons() {
        if (tv2.TokenList.get(index).C_p.equals("num_Cons")
                || tv2.TokenList.get(index).C_p.equals("word_Cons")
                || tv2.TokenList.get(index).C_p.equals("point_Cons")
                || tv2.TokenList.get(index).C_p.equals("Char_Cons")
                || tv2.TokenList.get(index).C_p.equals("Boolean_Cons")
                || tv2.TokenList.get(index).C_p.equals("ID")) {
            if (tv2.TokenList.get(index).C_p.equals("ID")) {
                index++;
                return true;

            } else if (Const()) {
                return true;
            }
        }

        return false;
    }

    private boolean Array_const() {
        //FIRST(<Array_const>) = {{ , ;}
        if (tv2.TokenList.get(index).C_p.equals("{")
                || tv2.TokenList.get(index).C_p.equals(";")) {
            //<Array_const>  <Array_const1> | ;
            if (tv2.TokenList.get(index).C_p.equals(";")) {
                index++;
                return true;
            } else if (Array_const1()) {
                return true;
            }
        }
        return false;
    }

    private boolean Array_const1() {

        // FIRST(<Array_const1>) = { { }
        if (tv2.TokenList.get(index).C_p.equals("{")) {
            //<Array_const1>{ <Expression> <Array_const2>
            if (tv2.TokenList.get(index).C_p.equals("{")) {
                index++;
                if (Expression()) {

                    if (Array_const2()) {
                        return true;
                    }
                }
            }
        }
        return false;
    }

    private boolean Array_const2() {
        //FIRST(<Array_const2>) = {, , } }
        if (tv2.TokenList.get(index).C_p.equals("}")
                || tv2.TokenList.get(index).C_p.equals(",")) {
            //<Array_const2> , <Exp> <Array_const2> | } ;
            if (tv2.TokenList.get(index).C_p.equals("}")) {

                index++;
                if (tv2.TokenList.get(index).C_p.equals(";")) {
                    index++;
                    return true;
                }
            } else if (tv2.TokenList.get(index).C_p.equals(",")) {
                index++;
                if (Expression()) {

                    if (Array_const2()) {
                        return true;
                    }
                }
            }
        }
        return false;
    }

    private boolean Object_link() {

        //FIRST(<Object_Link>) = {ID , [}
        if (tv2.TokenList.get(index).C_p.equals("ID")
                || tv2.TokenList.get(index).C_p.equals("[")) {
            //<Object_Link> ID <Obj_Create>| [] ID <object_arr_dec>
            if (tv2.TokenList.get(index).C_p.equals("ID")) {

                index++;
                if (Obj_Create()) {
                    return true;
                }
            } else if (tv2.TokenList.get(index).C_p.equals("[")) {

                index++;
                if (tv2.TokenList.get(index).C_p.equals("]")) {

                    index++;
                    if (tv2.TokenList.get(index).C_p.equals("ID")) {

                        index++;
                        if (object_arr_dec()) {
                            return true;
                        }
                    }
                }
            }
        }
        return false;
    }

    private boolean object_arr_dec() {

        //FIRST(<object_arr_dec>) = { = }
        if (tv2.TokenList.get(index).V_p.equals("=")) {
            //<object_arr_dec>  = new ID[<Expression>]<obj_arr_dec1>
            if (tv2.TokenList.get(index).V_p.equals("=")) {
                index++;
                if (tv2.TokenList.get(index).C_p.equals("New")) {
                    index++;
                    if (tv2.TokenList.get(index).C_p.equals("ID")) {

                        index++;
                        if (tv2.TokenList.get(index).C_p.equals("[")) {

                            index++;

                            if (Expression()) {
                                if (tv2.TokenList.get(index).C_p.equals("]")) {

                                    index++;

                                    if (obj_arr_dec1()) {
                                        return true;
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }
        return false;
    }

    private boolean obj_arr_dec1() {

        //FIRST(<obj_arr_dec1>) = { ; , { }
        if (tv2.TokenList.get(index).C_p.equals("{")
                || tv2.TokenList.get(index).C_p.equals(";")) {
            //<obj_arr_dec1>  ;| {<obj_arr_dec2>
            if (tv2.TokenList.get(index).C_p.equals(";")) {
                index++;
                return true;
            } else if (tv2.TokenList.get(index).C_p.equals("{")) {
                index++;
                if (obj_arr_dec2()) {
                    return true;
                }
            }
        }
        return false;
    }

    private boolean obj_arr_dec2() {

        //FIRST(<obj_arr_dec2>) = { first<Expression> }
        if (tv2.TokenList.get(index).C_p.equals("ID")
                || tv2.TokenList.get(index).C_p.equals("num_Cons")
                || tv2.TokenList.get(index).C_p.equals("point_Cons")
                || tv2.TokenList.get(index).C_p.equals("word_Cons")
                || tv2.TokenList.get(index).C_p.equals("Char_Cons")
                || tv2.TokenList.get(index).C_p.equals("Boolean_Cons")
                || tv2.TokenList.get(index).C_p.equals("U_Opr")
                || tv2.TokenList.get(index).C_p.equals("(")) {
            //<obj_arr_dec2>  <Expression> <obj_arr_dec3>
            if (Expression()) {

                if (obj_arr_dec3()) {
                    return true;
                }
            }

        }

        return false;
    }

    private boolean obj_arr_dec3() {
        ////FIRST(<obj_arr_dec3>) = { , , }}
        if (tv2.TokenList.get(index).C_p.equals(",")
                || tv2.TokenList.get(index).C_p.equals("}")) {
            //<obj_arr_dec3>  , <obj_arr_dec2>|}; 
            if (tv2.TokenList.get(index).C_p.equals(",")) {
                index++;
                if (obj_arr_dec2()) {
                    return true;
                }
            } else if (tv2.TokenList.get(index).C_p.equals("}")) {
                index++;
                if (tv2.TokenList.get(index).C_p.equals(";")) {
                    index++;
                    return true;
                }
            }
        }

        return false;
    }

    private boolean Obj_Create() {

        //FIRST(<Obj_Create>) = {= , , , ;}
        if (tv2.TokenList.get(index).V_p.equals("=")
                || tv2.TokenList.get(index).C_p.equals(",")
                || tv2.TokenList.get(index).C_p.equals(";")) {

            //<Obj_Create> = new ID  (<Param>) <Object_List>  |;| <Object_List>
            if (tv2.TokenList.get(index).V_p.equals("=")) {
                index++;
                if (tv2.TokenList.get(index).C_p.equals("New")) {
                    index++;
                    if (tv2.TokenList.get(index).C_p.equals("ID")) {

                        index++;
                        if (tv2.TokenList.get(index).C_p.equals("(")) {

                            index++;

                            if (Param()) {
                                if (tv2.TokenList.get(index).C_p.equals(")")) {

                                    index++;
                                    if (Object_List()) {
                                        return true;
                                    }
                                }
                            }
                        }
                    }
                }
            } else if (Object_List()) {
                return true;
            } else if (tv2.TokenList.get(index).C_p.equals(";")) {
                index++;
                return true;
            }
        }
        return false;
    }

    private boolean Object_List() {

        //FIRST(<Object_List>) = {,, ;}
        if (tv2.TokenList.get(index).C_p.equals(",")
                || tv2.TokenList.get(index).C_p.equals(";")) {

            //<Object_List> , ID<Obj_Create> | ;
            if (tv2.TokenList.get(index).C_p.equals(",")) {
                index++;
                if (tv2.TokenList.get(index).C_p.equals("ID")) {
                    index++;
                    if (Obj_Create()) {
                        return true;
                    }
                }
            } else if (tv2.TokenList.get(index).C_p.equals(";")) {
                index++;
                return true;
            }
        }
        return false;
    }

    private boolean Object_Call() {

        //FIRST(<Object_Call>) = {. , [}
        if (tv2.TokenList.get(index).C_p.equals(".")
                || tv2.TokenList.get(index).C_p.equals("[")) {
            //<Object_Call> . <Expression> | [<Expression>].<Expression>
            if (tv2.TokenList.get(index).C_p.equals(".")) {

                index++;

                if (Expression()) {

                    return true;
                }
            } else if (tv2.TokenList.get(index).C_p.equals("[")) {
                index++;

                if (Expression()) {
                    if (tv2.TokenList.get(index).C_p.equals("]")) {
                        index++;
                        if (tv2.TokenList.get(index).C_p.equals(".")) {
                            index++;

                            if (Expression()) {

                                return true;
                            }
                        }
                    }
                }
            }
        }
        return false;
    }

    private boolean Method_Call_1() {

        //FIRST(<Method_Call_1>) = { ( }
        if (tv2.TokenList.get(index).C_p.equals("(")) {
            //<Method_Call_1>  (<Param>) 
            if (tv2.TokenList.get(index).C_p.equals("(")) {

                index++;
                if (Param()) {

                    if (tv2.TokenList.get(index).C_p.equals(")")) {
                        index++;
                        return true;
                    }
                }
            }
        }
        return false;
    }

    private boolean Param() {

        //FIRST(<Param>) = {first(<Expression>) , Null}
        if (tv2.TokenList.get(index).C_p.equals("ID")
                || tv2.TokenList.get(index).C_p.equals("num_Cons")
                || tv2.TokenList.get(index).C_p.equals("point_Cons")
                || tv2.TokenList.get(index).C_p.equals("word_Cons")
                || tv2.TokenList.get(index).C_p.equals("Char_Cons")
                || tv2.TokenList.get(index).C_p.equals("Boolean_Cons")
                || tv2.TokenList.get(index).C_p.equals("U_Opr")
                || tv2.TokenList.get(index).C_p.equals("(")) {

            //<Param> <Expression> <Param1> | Null
            if (Expression()) {

                if (Param1()) {
                    return true;
                }
            }
        } ////FOLLOW(<Param>) = { ) }
        else if (tv2.TokenList.get(index).C_p.equals(")")) {

            return true;
        }
        return false;
    }

    private boolean Param1() {
        //FIRST(<Param1>) = {, , Null}
        if (tv2.TokenList.get(index).C_p.equals(",")) {
            //<Param1>  ,  ID <Param1> | Null
            index++;

            if (tv2.TokenList.get(index).C_p.equals("ID")) {
                index++;
                if (Param1()) {
                    return true;
                }
            }
        } //FOLLOW(<Param1>) = { ) }
        else if (tv2.TokenList.get(index).C_p.equals(")")) {

            return true;
        }
        return false;
    }

}
