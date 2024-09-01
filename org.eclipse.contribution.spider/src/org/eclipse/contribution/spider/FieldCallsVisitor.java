package org.eclipse.contribution.spider;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import org.eclipse.jdt.core.dom.ASTMatcher;
import org.eclipse.jdt.core.dom.ASTNode;
import org.eclipse.jdt.core.dom.ASTVisitor;
import org.eclipse.jdt.core.dom.CastExpression;
import org.eclipse.jdt.core.dom.Expression;
import org.eclipse.jdt.core.dom.FieldAccess;
import org.eclipse.jdt.core.dom.MethodInvocation;
import org.eclipse.jdt.core.dom.SimpleName;
import org.eclipse.jdt.core.dom.TypeDeclaration;

public class FieldCallsVisitor extends ASTVisitor {
	private String fieldName;
	private List matchExpressions;
	private Set collectedMethods;
	private MethodInvocation checkCast;
	
	public FieldCallsVisitor(String fieldName) {
		this.fieldName= fieldName;
		collectedMethods= new HashSet();
	}
	
	public boolean visit(TypeDeclaration node) {
		// collects the following method invocations:
		// fieldName.method();
		// this.fieldName.method();
		// (fieldName(type)).method();
		// attribute().method();
		matchExpressions= new ArrayList();
		if (fieldName.endsWith("()")) {
			fieldName= fieldName.substring(0, fieldName.length()-2);
			MethodInvocation invocation= node.getAST().newMethodInvocation();
			invocation.setName(node.getAST().newSimpleName(fieldName));
			matchExpressions.add(invocation);
		}
		SimpleName name= node.getAST().newSimpleName(fieldName);
		matchExpressions.add(name);
		FieldAccess access= node.getAST().newFieldAccess();
		access.setExpression(node.getAST().newThisExpression());
		access.setName(name);
		matchExpressions.add(access);
		checkCast= null;
		return true;
	}
	
	public boolean visit(MethodInvocation method) {
		Expression exp= method.getExpression();
		if (exp != null) {
			for (Iterator each= matchExpressions.iterator(); each.hasNext();) {
				ASTNode node= (ASTNode) each.next();
				if (exp.subtreeMatch(new ASTMatcher(), node)) {
					collectedMethods.add(method);
					break;
				}
			}
		}
		checkCast= method;
		return true;
	}
	
	public void endVisit(MethodInvocation node) {
		super.endVisit(node);
		checkCast= null;
	}

	public boolean visit(CastExpression node) {
		SimpleName name= node.getAST().newSimpleName(fieldName);
		if (node.getExpression().subtreeMatch(new ASTMatcher(), name)) 
			collectedMethods.add(checkCast);
		return true;
	}

	public Set getMatches() {
		return collectedMethods;
	}
}