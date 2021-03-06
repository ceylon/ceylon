/********************************************************************************
 * Copyright (c) 2011-2017 Red Hat Inc. and/or its affiliates and others
 *
 * This program and the accompanying materials are made available under the 
 * terms of the Apache License, Version 2.0 which is available at
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * SPDX-License-Identifier: Apache-2.0 
 ********************************************************************************/
package org.eclipse.ceylon.model.loader.mirror;

/**
 * A Mirror of javax.lang.model.type.TypeKind for the runtimes that do not
 * have the java.compiler module (Android for example)
 *
 * @author Stéphane Épardaud <stef@epardaud.fr>
 */
public enum TypeKind {
    /*
     * WARNING: those names MUST be the same as in the original TypeKind because we have some code
     * which converts from one type to the other based on name. 
     */
    
    // Primitives
    BOOLEAN, BYTE, CHAR, SHORT, INT, LONG, FLOAT, DOUBLE,
    // Classes
    DECLARED,
    // The rest are self-evident
    NONE, WILDCARD, VOID,
    ARRAY, TYPEVAR, ERROR, NULL;

    public boolean isPrimitive() {
        switch(this) {
        case BOOLEAN:
        case BYTE:
        case SHORT:
        case INT:
        case LONG:
        case CHAR:
        case FLOAT:
        case DOUBLE:
            return true;

        default:
            return false;
        }
    }
    
}
