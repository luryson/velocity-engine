package org.apache.velocity.runtime.parser.node;

/* Generated By:JJTree: Do not edit this line. ASTIntegerRange.java */
/*
 * The Apache Software License, Version 1.1
 *
 * Copyright (c) 2000-2001 The Apache Software Foundation.  All rights
 * reserved.
 *
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted provided that the following conditions
 * are met:
 *
 * 1. Redistributions of source code must retain the above copyright
 *    notice, this list of conditions and the following disclaimer.
 *
 * 2. Redistributions in binary form must reproduce the above copyright
 *    notice, this list of conditions and the following disclaimer in
 *    the documentation and/or other materials provided with the
 *    distribution.
 *
 * 3. The end-user documentation included with the redistribution, if
 *    any, must include the following acknowlegement:
 *       "This product includes software developed by the
 *        Apache Software Foundation (http://www.apache.org/)."
 *    Alternately, this acknowlegement may appear in the software itself,
 *    if and wherever such third-party acknowlegements normally appear.
 *
 * 4. The names "The Jakarta Project", "Velocity", and "Apache Software
 *    Foundation" must not be used to endorse or promote products derived
 *    from this software without prior written permission. For written
 *    permission, please contact apache@apache.org.
 *
 * 5. Products derived from this software may not be called "Apache"
 *    nor may "Apache" appear in their names without prior written
 *    permission of the Apache Group.
 *
 * THIS SOFTWARE IS PROVIDED ``AS IS'' AND ANY EXPRESSED OR IMPLIED
 * WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE IMPLIED WARRANTIES
 * OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE ARE
 * DISCLAIMED.  IN NO EVENT SHALL THE APACHE SOFTWARE FOUNDATION OR
 * ITS CONTRIBUTORS BE LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL,
 * SPECIAL, EXEMPLARY, OR CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT
 * LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES; LOSS OF
 * USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND
 * ON ANY THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY,
 * OR TORT (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT
 * OF THE USE OF THIS SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF
 * SUCH DAMAGE.
 * ====================================================================
 *
 * This software consists of voluntary contributions made by many
 * individuals on behalf of the Apache Software Foundation.  For more
 * information on the Apache Software Foundation, please see
 * <http://www.apache.org/>.
 */


/**
 * handles the range 'operator'  [ n .. m ]
 * 
 * Please look at the Parser.jjt file which is
 * what controls the generation of this class.
 *
 * @author <a href="mailto:geirm@optonline.net">Geir Magnusson Jr.</a>
 * @version $Id: ASTIntegerRange.java,v 1.3 2001/03/19 17:17:49 geirm Exp $ 
*/

import java.util.ArrayList;

import org.apache.velocity.context.InternalContextAdapter;
import org.apache.velocity.runtime.Runtime;
import org.apache.velocity.runtime.parser.Parser;

import org.apache.velocity.exception.MethodInvocationException;

public class ASTIntegerRange extends SimpleNode {
  
    public ASTIntegerRange(int id) 
    {
        super(id);
    }

    public ASTIntegerRange(Parser p, int id) 
    {
        super(p, id);
    }

    /** Accept the visitor. **/
    public Object jjtAccept(ParserVisitor visitor, Object data) 
    {
        return visitor.visit(this, data);
    }

    /**
     *  does the real work.  Creates an Vector of Integers with the
     *  right value range
     *
     *  @param context  app context used if Left or Right of .. is a ref
     *  @return Object array of Integers
     */
    public Object value( InternalContextAdapter context)
        throws MethodInvocationException
    { 
        /*
         *  get the two range ends
         */

        Object left = jjtGetChild(0).value( context );
        Object right = jjtGetChild(1).value( context );

        /*
         *  if either is null, lets log and bail
         */

        if (left == null || right == null)
        {
            Runtime.error( ( left == null ? "Left" : "Right" ) + " side of range operator [n..m] has null value."
                           + " Operation not possible. "
                           +  context.getCurrentTemplateName() + " [line " + getLine() 
                           + ", column " + getColumn() + "]");
            return null;
        }
        
        /*
         *  if not an Integer, not much we can do either
         */

        if ( !( left instanceof Integer )  || !( right instanceof Integer ))
        {
            Runtime.error( ( !( left instanceof Integer ) ? "Left" : "Right" ) 
                           + " side of range operator is not a valid type. "
                           + "Currently only integers (1,2,3...) and Integer type is supported. "
                           + context.getCurrentTemplateName() + " [line " + getLine() 
                           + ", column " + getColumn() + "]");
 
            return null;
        }


        /*
         *  get the two integer values of the ends of the range
         */

        int l = ( (Integer) left ).intValue() ;
        int r = (  (Integer) right ).intValue();

        /*
         *  find out how many there are
         */

        int num = Math.abs( l - r );
        num += 1;

        /*
         *  see if your increment is Pos or Neg
         */

        int delta = ( l >= r ) ? -1 : 1;

        /*
         *  make the vector and fill it
         */

        ArrayList foo = new ArrayList();
        int val = l;

        for(int i =0; i < num; i++)
        {
            foo.add( new Integer( val ));
            val += delta;
        }

        return foo;
    }
}

