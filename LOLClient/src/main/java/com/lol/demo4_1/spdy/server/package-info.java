/*
 * Copyright 2014 The Netty Project
 *
 * The Netty Project licenses this file to you under the Apache License,
 * version 2.0 (the "License"); you may not use this file except in compliance
 * with the License. You may obtain a copy of the License at:
 *
 *   http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS, WITHOUT
 * WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the
 * License for the specific language governing permissions and limitations
 * under the License.
 */

/**
 * This package contains an example SPDY HTTP web server.
 * <p>
 * This package relies on the Jetty project's implementation of the Transport Layer Security (TLS) extension for Next
 * Protocol Negotiation (NPN) for OpenJDK 7 is required. NPN allows the application layer to negotiate which
 * protocol, SPDY or HTTP, to use.
 * <p>
 * To start, run {@link com.lol.demo4_1.spdy.server.SpdyServer} with the JVM parameter:
 * {@code java -Xbootclasspath/p:<path_to_npn_boot_jar> ...}.
 * The "path_to_npn_boot_jar" is the path on the file system for the NPN Boot Jar file which can be downloaded from
 * Maven at coordinates org.mortbay.jetty.npn:npn-boot. Different versions applies to different OpenJDK versions.
 * See <a href="http://www.eclipse.org/jetty/documentation/current/npn-chapter.html">Jetty docs</a> for more
 * information.
 * <p>
 * You may also use the {@code run-example.sh} script to start the server from the command line:
 * <pre>
 *     ./run-example spdy-server
 * </pre>
 * <p>
 * Once started, you can test the server with your
 * <a href="http://en.wikipedia.org/wiki/SPDY#Browser_support_and_usage">SPDY enabled web browser</a> by navigating
 * to <a href="https://localhost:8443/">https://localhost:8443/</a>
 */
package com.lol.demo4_1.spdy.server;
