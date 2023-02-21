package org.acegroup.watchtower.web.util;

import org.acegroup.watchtower.web.model.Profile;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.concurrent.BlockingQueue;
import java.util.concurrent.Executors;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadFactory;
import java.util.concurrent.atomic.AtomicBoolean;


public class ProfilerQueue {

    private static Logger log = LoggerFactory.getLogger(ProfilerQueue.class);

    private BlockingQueue<Profile> queue;

    private final ThreadFactory threadFactory;

    private Thread thread;

    private AtomicBoolean started = new AtomicBoolean(false);

    private volatile boolean shouldContinue = false;

    public ProfilerQueue() {
        this(null);
    }

    public ProfilerQueue(final ThreadFactory tf) {
        this.queue = new LinkedBlockingQueue<>();
        this.threadFactory = tf == null ? Executors.defaultThreadFactory() : tf;
        this.thread = null;
    }


    public void start() {
        if (started.getAndSet(true)) {
            return;
        }
        log.info("WorkingQueue start");
        shouldContinue = true;
        thread = threadFactory.newThread(() -> {
            while (shouldContinue) {
                try {
                    Profile req = queue.take();
                    ProfileInfoHolder.addProfilerInfo(req);
                } catch (Exception ex) {
                    log.error("start failed",ex);
                }
            }
        });
        thread.start();
    }

    public void stop() {
        started.set(false);
        shouldContinue = false;
        thread.interrupt();
        log.info("WorkingQueue end");
    }

    public void addProfileInfo(Profile info) {
        if (!started.get()) {
            start();
        }
        queue.add(info);
    }
}