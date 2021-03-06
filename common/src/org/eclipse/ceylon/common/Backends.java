/********************************************************************************
 * Copyright (c) 2011-2017 Red Hat Inc. and/or its affiliates and others
 *
 * This program and the accompanying materials are made available under the 
 * terms of the Apache License, Version 2.0 which is available at
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * SPDX-License-Identifier: Apache-2.0 
 ********************************************************************************/
package org.eclipse.ceylon.common;

import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;

public class Backends implements Iterable<Backend> {
    private final Set<Backend> backends;
    
    private static final Map<String, Backends> collections;
    
    static {
        collections = new HashMap<String, Backends>();
    }
    
    public final static Backends ANY = new Backends(Collections.<Backend>emptySet());
    public final static Backends HEADER = fromAnnotation(Backend.Header.nativeAnnotation);
    public final static Backends JAVA = fromAnnotation(Backend.Java.nativeAnnotation);
    public final static Backends JS = fromAnnotation(Backend.JavaScript.nativeAnnotation);
    
    Backends(Set<Backend> backends) {
        // We don't allow Backend.None in a set with others
        assert (backends.size() <= 1 || !backends.contains(Backend.Header));
        this.backends = backends;
    }

    public boolean none() {
        return backends.isEmpty();
    }
    
    public boolean header() {
        return this == HEADER;
    }
    
    public boolean supports(Backend backend) {
        return !supported(backend.asSet()).none();
    }
    
    public boolean supports(Backends backends) {
        return !supported(backends).none();
    }
    
    public Backends supported(Backends backends) {
        if (this.backends.containsAll(backends.backends)) {
            return backends;
        } else {
            HashSet<Backend> result = new HashSet<Backend>(backends.backends);
            result.retainAll(this.backends);
            if (result.isEmpty()) {
                return ANY;
            } else if (result.size() == 1) {
                Backend backend = result.iterator().next();
                return backend.asSet();
            } else {
                return new Backends(result);
            }
        }
    }

    public Backends merged(Backend backend) {
        if (none()) {
            return backend.asSet();
        } else if (backends.contains(backend)) {
            return this;
        } else {
            HashSet<Backend> bs = new HashSet<Backend>(this.backends);
            bs.add(backend);
            return new Backends(bs);
        }
    }

    public Backends merged(Backends backends) {
        if (none()) {
            return backends;
        } else if (backends.none() ||
                backends.backends.equals(this.backends)) {
            return this;
        } else {
            HashSet<Backend> bs = new HashSet<Backend>(this.backends);
            bs.addAll(backends.backends);
            return new Backends(bs);
        }
    }

    @Override
    public Iterator<Backend> iterator() {
        return backends.iterator();
    }

    public static Backends fromAnnotations(Collection<String> backends) {
        if (backends == null) {
            return null;
        } else if (backends.isEmpty()) {
            return Backends.ANY;
        } else if (backends.size() == 1) {
            String backend = backends.iterator().next();
            return fromAnnotation(backend);
        } else {
            HashSet<Backend> bs = new HashSet<Backend>(backends.size());
            for (String backend : backends) {
                Backend b = Backend.fromAnnotation(backend);
                bs.add(b);
            }
            return new Backends(bs);
        }
    }

    public static Backends fromAnnotation(String backend) {
        if (backend == null) {
            return ANY;
        }
        Backends bs = collections.get(backend);
        if (bs == null) {
            bs = new Backends(Collections.singleton(Backend.fromAnnotation(backend)));
            collections.put(backend, bs);
        }
        return bs;
    }

    @Override
    public int hashCode() {
        return backends.hashCode();
    }

    @Override
    public boolean equals(Object that) {
        if (that instanceof Backends) {
            return backends.equals(((Backends)that).backends);
        } else {
            return false;
        }
    }

    public String names() {
        StringBuilder str = new StringBuilder();
        for (Backend b : backends) {
            if (str.length() > 0) {
                str.append(",");
            }
            str.append(b.nativeAnnotation);
        }
        return str.toString();
    }

    @Override
    public String toString() {
        if (none()) {
            return "<none>";
        } else if (header()) {
            return "native";
        } else {
            StringBuilder str = new StringBuilder();
            str.append("native(");
            for (Backend b : backends) {
                if (str.length() > 7) {
                    str.append(", ");
                }
                str.append("\"");
                str.append(b.nativeAnnotation);
                str.append("\"");
            }
            str.append(")");
            return str.toString();
        }
    }
}
