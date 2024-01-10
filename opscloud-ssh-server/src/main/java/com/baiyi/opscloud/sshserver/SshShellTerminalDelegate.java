/*
 * Copyright (c) 2020 François Onimus
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.baiyi.opscloud.sshserver;

import org.jline.terminal.*;
import org.jline.utils.ColorPalette;
import org.jline.utils.InfoCmp;
import org.jline.utils.NonBlockingReader;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.nio.charset.Charset;
import java.util.function.IntConsumer;
import java.util.function.IntSupplier;

/**
 * <p>Shell terminal delegate</p>
 * <p>Calls are bound to terminal stored in thread context</p>
 */
@Component("terminalDelegate")
@Primary
public class SshShellTerminalDelegate implements Terminal {

    private final Terminal delegate;

    /**
     * Constructor
     *
     * @param delegate terminal delegate
     */
    public SshShellTerminalDelegate(Terminal delegate) {
        this.delegate = delegate;
    }

    private Terminal delegate() {
        SshContext current = SshShellCommandFactory.SSH_THREAD_CONTEXT.get();
        if (current != null && current.getTerminal() != null) {
            return current.getTerminal();
        }
        if (delegate == null) {
            throw new IllegalStateException("Cannot find terminal");
        }
        return delegate;
    }

    @Override
    public String getName() {
        return delegate().getName();
    }

    @Override
    public SignalHandler handle(Signal signal, SignalHandler signalHandler) {
        return delegate().handle(signal, signalHandler);
    }

    @Override
    public void raise(Signal signal) {
        delegate().raise(signal);
    }

    @Override
    public NonBlockingReader reader() {
        return delegate().reader();
    }

    @Override
    public PrintWriter writer() {
        return delegate().writer();
    }

    @Override
    public Charset encoding() {
        return delegate().encoding();
    }

    @Override
    public InputStream input() {
        return delegate().input();
    }

    @Override
    public OutputStream output() {
        return delegate().output();
    }

    @Override
    public boolean canPauseResume() {
        return delegate().canPauseResume();
    }

    @Override
    public void pause() {
        delegate().pause();
    }

    @Override
    public void pause(boolean wait) throws InterruptedException {
        delegate().pause(wait);
    }

    @Override
    public void resume() {
        delegate().resume();
    }

    @Override
    public boolean paused() {
        return delegate().paused();
    }

    @Override
    public Attributes enterRawMode() {
        return delegate().enterRawMode();
    }

    @Override
    public boolean echo() {
        return delegate().echo();
    }

    @Override
    public boolean echo(boolean b) {
        return delegate().echo(b);
    }

    @Override
    public Attributes getAttributes() {
        return delegate().getAttributes();
    }

    @Override
    public void setAttributes(Attributes attributes) {
        delegate().setAttributes(attributes);
    }

    @Override
    public Size getSize() {
        return delegate().getSize();
    }

    @Override
    public void setSize(Size size) {
        delegate().setSize(size);
    }

    @Override
    public void flush() {
        delegate().flush();
    }

    @Override
    public String getType() {
        return delegate().getType();
    }

    @Override
    public boolean puts(InfoCmp.Capability capability, Object... objects) {
        return delegate().puts(capability, objects);
    }

    @Override
    public boolean getBooleanCapability(InfoCmp.Capability capability) {
        return delegate().getBooleanCapability(capability);
    }

    @Override
    public Integer getNumericCapability(InfoCmp.Capability capability) {
        return delegate().getNumericCapability(capability);
    }

    @Override
    public String getStringCapability(InfoCmp.Capability capability) {
        return delegate().getStringCapability(capability);
    }

    @Override
    public Cursor getCursorPosition(IntConsumer intConsumer) {
        return delegate().getCursorPosition(intConsumer);
    }

    @Override
    public boolean hasMouseSupport() {
        return delegate().hasMouseSupport();
    }

    @Override
    public boolean trackMouse(MouseTracking mouseTracking) {
        return delegate().trackMouse(mouseTracking);
    }

    @Override
    public MouseEvent readMouseEvent() {
        return delegate().readMouseEvent();
    }

    @Override
    public MouseEvent readMouseEvent(IntSupplier intSupplier) {
        return delegate().readMouseEvent(intSupplier);
    }

    @Override
    public boolean hasFocusSupport() {
        return delegate().hasFocusSupport();
    }

    @Override
    public boolean trackFocus(boolean tracking) {
        return delegate().trackFocus(tracking);
    }

    @Override
    public ColorPalette getPalette() {
        return delegate().getPalette();
    }

    @Override
    public void close() throws IOException {
        delegate().close();
    }

}