/*
 * Copyright Red Hat Inc. and/or its affiliates and other contributors
 * as indicated by the authors tag. All rights reserved.
 *
 * This copyrighted material is made available to anyone wishing to use,
 * modify, copy, or redistribute it subject to the terms and conditions
 * of the GNU General Public License version 2.
 * 
 * This particular file is subject to the "Classpath" exception as provided in the 
 * LICENSE file that accompanied this code.
 * 
 * This program is distributed in the hope that it will be useful, but WITHOUT A
 * WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A
 * PARTICULAR PURPOSE.  See the GNU General Public License for more details.
 * You should have received a copy of the GNU General Public License,
 * along with this distribution; if not, write to the Free Software
 * Foundation, Inc., 51 Franklin Street, Fifth Floor, Boston,
 * MA  02110-1301, USA.
 */
package org.eclipse.ceylon.compiler.java.codegen.recovery;

import java.util.List;

import org.eclipse.ceylon.compiler.typechecker.analyzer.UsageWarning;
import org.eclipse.ceylon.compiler.typechecker.tree.Message;
import org.eclipse.ceylon.compiler.typechecker.tree.Node;
import org.eclipse.ceylon.compiler.typechecker.tree.Tree;
import org.eclipse.ceylon.compiler.typechecker.tree.Visitor;

/**
 * Visitor for statements which determines whether there are any 
 * errors in the tree.
 * 
 * This visitor is fail fast.
 */
class StatementErrorVisitor 
        extends Visitor {
    
    public boolean hasErrors(Node target) {
        try{
            target.visit(this);
            return false;
        }catch(HasErrorException x){
            return true;
        }
    }

    public HasErrorException getFirstErrorMessage(Node target) {
        try{
            target.visit(this);
            return null;
        }catch(HasErrorException x){
            return x;
        }
    }

    @Override
    public void handleException(Exception e, Node that) {
        // rethrow
        throw (RuntimeException)e;
    }

    @Override
    public void visitAny(Node that) {
        // fast termination
        throwIfError(that);
        super.visitAny(that);
    }

    protected void throwIfError(Node that) {
        Message m = hasError(that);
        if (m != null) {
            throw new HasErrorException(that, m);
        }
    }

    protected Message hasError(Node that) {
        // skip only usage warnings
        List<Message> errors = that.getErrors();
        for(int i=0,l=errors.size();i<l;i++){
            Message message = errors.get(i);
            if(isError(that, message))
                return message;
        }
        return null;
    }

    /** Is the given message on the given node considered an error */
    protected boolean isError(Node that, Message message) {
        return !(message instanceof UsageWarning);
    }

    
    @Override
    public void visit(Tree.Block that) {
        // don't go there
    }
    
    @Override
    public void visit(Tree.Declaration that) {
        // don't go there
    }
    
    @Override
    public void visit(Tree.Variable that) {
        // unlike other declarations, Variables are part of the 
        // statement, and if they have a pb we're screwed, so we
        // *do* want to visit them.
         visitAny(that);
    }
}