package com.parsleyj.smallsteptrack.program;

import com.parsleyj.smallsteptrack.SmallStepSemanticObject;
import com.parsleyj.smallsteptrack.parser.ParseTreeNode;

/**
 * Created by Giuseppe on 24/03/16.
 * TODO: javadoc
 */
public class CBOConverterMethod<T extends SmallStepSemanticObject>  implements CaseConverterMethod {
    private BinaryOperationConverterMethod<T> method;
    public CBOConverterMethod(BinaryOperationConverterMethod<T> method) {
        this.method = method;
    }

    @Override
    public SmallStepSemanticObject convert(ParseTreeNode node, Semantics s) {
        return method.convert(s.resolve(node.get(0)), s.resolve(node.get(2)));
    }

    @FunctionalInterface
    public interface BinaryOperationConverterMethod<T>{
        T convert(T a, T b);
    }
}
