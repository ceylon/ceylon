/********************************************************************************
 * Copyright (c) 2011-2017 Red Hat Inc. and/or its affiliates and others
 *
 * This program and the accompanying materials are made available under the 
 * terms of the Apache License, Version 2.0 which is available at
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * SPDX-License-Identifier: Apache-2.0 
 ********************************************************************************/
package org.eclipse.ceylon.test.smoke.support;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

import org.eclipse.ceylon.cmr.impl.DefaultNode;
import org.eclipse.ceylon.cmr.impl.IOUtils;
import org.eclipse.ceylon.cmr.impl.RootNode;
import org.eclipse.ceylon.cmr.spi.ContentHandle;
import org.eclipse.ceylon.cmr.spi.ContentOptions;
import org.eclipse.ceylon.cmr.spi.ContentStore;
import org.eclipse.ceylon.cmr.spi.Node;
import org.eclipse.ceylon.cmr.spi.OpenNode;
import org.eclipse.ceylon.cmr.spi.SizedInputStream;
import org.eclipse.ceylon.cmr.spi.StructureBuilder;

/**
 * @author <a href="mailto:ales.justin@jboss.org">Ales Justin</a>
 */
public class InMemoryContentStore implements ContentStore, StructureBuilder {

    private static final byte[] MARKER = new byte[0];
    private Map<Node, byte[]> store = new HashMap<Node, byte[]>();

    @Override
    public ContentHandle peekContent(Node node) {
        byte[] result = store.get(node);
        if (result == null || result == MARKER)
            return null;

        return new InMemoryContentHandle(result);
    }

    @Override
    public ContentHandle getContent(Node node) throws IOException {
        return peekContent(node);
    }

    @Override
    public ContentHandle putContent(Node node, InputStream stream, ContentOptions options) throws IOException {
        final ByteArrayOutputStream baos = new ByteArrayOutputStream();
        IOUtils.copyStream(stream, baos, false, false);
        final byte[] bytes = baos.toByteArray();
        store.put(node, bytes);
        return new InMemoryContentHandle(bytes);
    }

    @Override
    public OpenNode createRoot() {
        return new RootNode(this, this) {
            @Override
            public boolean isRemote() {
                return true;
            }
        };
    }

    @Override
    public OpenNode create(Node parent, String child) {
        // automagically build the tree
        DefaultNode node = new DefaultNode(child) {
            @Override
            public boolean isRemote() {
                return true;
            }
        };
        store.put(node, MARKER);
        return node;
    }

    @Override
    public OpenNode find(Node parent, String child) {
        return create(parent, child);
    }

    @Override
    public Iterable<? extends OpenNode> find(Node parent) {
        return Collections.emptyList();
    }

    private static class InMemoryContentHandle implements ContentHandle {

        private byte[] bytes;
        private long lastModifed = System.currentTimeMillis();

        private InMemoryContentHandle(byte[] bytes) {
            this.bytes = bytes;
        }

        @Override
        public boolean hasBinaries() {
            return true;
        }

        @Override
        public InputStream getBinariesAsStream() throws IOException {
            return new ByteArrayInputStream(bytes);
        }

        @Override
        public SizedInputStream getBinariesAsSizedStream() throws IOException {
            return new SizedInputStream(getBinariesAsStream(), bytes.length);
        }

        @Override
        public File getContentAsFile() throws IOException {
            final File temp = File.createTempFile("ceylon-in-memory-", ".car");
            IOUtils.copyStream(getBinariesAsStream(), new FileOutputStream(temp), false, true);
            temp.deleteOnExit();
            return temp;
        }

        @Override
        public long getLastModified() throws IOException {
            return lastModifed;
        }

        @Override
        public long getSize() throws IOException {
            return bytes.length;
        }

        @Override
        public void clean() {
        }
    }

    @Override
    public String getDisplayString() {
        return "InMemoryContentStore";
    }

    @Override
    public boolean isOffline() {
        return true;
    }

    @Override
    public int getTimeout() {
        return 0;
    }

    @Override
    public boolean isHerd() {
        return false;
    }

    @Override
    public boolean canHandleFolders() {
        return true;
    }

    @Override
    public Iterable<File> getBaseDirectories() {
        return Collections.emptyList();
    }
}
